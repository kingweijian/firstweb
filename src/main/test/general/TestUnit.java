package general;

import com.annotations.Admin_entity;
import com.core.unit.Obtain_Package_Class;
import com.core.unit.Unit;
import com.sql.dbutils.Operating;
import com.sql.dbutils.SQL_DBUtil;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.experimental.theories.Theory;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Pattern;

/**
 * ClassName TestUnit
 * Author weijian
 * DateTime 2019/11/26 10:50 PM
 * Version 1.0
 */
public class TestUnit {
    Operating operating = new Operating();
    Logger logger = Logger.getLogger(TestUnit.class);
    @Test
    public void ThreadTestOne(){
        new Thread(new ThreadOne()).start();
        new Thread(new ThreadMore()).start();
    }
    @Test
    public void ThreadTestMore(){
//      for ( int i = 0 ; i <=5 ; i++){
//          new Thread(new ThreadMore()).start();
//      }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("yyyyMMdd");
        logger.info (simpleDateFormat.format (new Date ()));
        Calendar c = Calendar.getInstance ();

    }
    @Test
    public void ThreadTestCachedPool() throws InterruptedException {
        // 创建一个有缓存的线程池
        ExecutorService executorService = Executors.newCachedThreadPool();
        for ( int i = 0 ; i <=5 ; i++){
            executorService.execute(new ThreadMore());
        }
        executorService.shutdown();
        Thread.sleep(1000);
//        executorService.execute(new ThreadMore());

    }

    @Test
    public void ThreadTestFiexedPool(){
//       通过newFixedThreadPool 提前创建线程，可以减少每次都要创建线程的事件
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for ( int i = 0 ; i <=9 ; i++){
            executorService.execute(new ThreadMore());
        }
        executorService.shutdown();

    }
    @Test
    public void ThreadTestCallableInterface(){
        ExecutorService executorService = Executors.newCachedThreadPool();
        List<Future<String>> lists = new ArrayList<Future<String>>();
        for (int i = 0; i<=10 ; i++){
            lists.add(executorService.submit(new TaskWithCallable(i)));
        }

        try {
            for (Future<String> s : lists) {
                System.out.println(s.get());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }finally {
            executorService.shutdown();
        }


    }
    @Test
    public void DeamonFinally(){
//        Thread thread = new Thread(new DeamonFinallyTest());
//        thread.setDaemon(true);
//        thread.start();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new DeamonFinallyTest());
//        executorService.shutdown();
    }

    @Test
    public void ThreadTestSinglePool(){
//      SingleThreadpool 会提供一个队列来装添加进来的任务，执行时会安装这个队列来执行，执行时
//        前一个任务没有结束之前下一个任务不会开启
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        for ( int i = 0 ; i <=9 ; i++){
            executorService.execute(new ThreadMore());
        }
        executorService.shutdown();

    }

    @Test
    public void ThreadTestDeamon(){
//      后台线程测试
        DeamonThreadTest deamonThreadTest = new DeamonThreadTest();
        for (int j = 0 ; j < 5 ; j++){
            Thread thread =  new Thread(deamonThreadTest);
            thread.setDaemon(true);
            thread.start();
        }
        try {
            Thread.sleep(10000);
            deamonThreadTest.stopThread();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void ThreadTestDeamonFactory(){
//      后台线程 工厂
       ExecutorService executorService = Executors.newCachedThreadPool(new DeamonThreadTest());
        for (int j = 0 ; j < 5 ; j++){
            executorService.execute(new DeamonThreadTest());

        }
        try {
            Thread.sleep(10000);
            executorService.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void ThreadLife(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Thread.yield();
            }
        });
        thread.start();

//        thread.resume();
//        thread.suspend();
//        thread.stop();
//        Thread.yield();
    }


    @Test
    public void testVolatile(){
        TestVolatile testVolatile = new TestVolatile();
        try {
            for (int i =0 ; i<5; i++){
                new Thread(new TestVolatile()).start();
                Thread.sleep(1000);
            }
            Thread.sleep(3000);
            System.out.print("设置线程结束变量 isStart 为false");
            testVolatile.isStart = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void ExecoptionThreadTest(){
        // 验证在线程外部捕获异常，事实是在线程外部是捕获不到异常的
        try {

           new Thread(new ExecoptionThreadTest()).start();
        }catch (Exception e){
            // 没有捕获到任何异常
            System.out.println("catch Execoption Thread!!");
        }

        // 使用UncaughtExecopyionHandler 处理线程的异常，这个处理线程的标准是在线程死亡前处理未被捕获的异常
//        需要使用Executor对象来创建线程，创建线程的工程需要我们自定义，因为我们要为线程设我们自己的Uncaught
//        当然也可以为单个线程设置，但是如果涉及到批量使用，建议使用ThreadFactory
        ExecutorService executorService = Executors.newCachedThreadPool(new ExecoptionThreadFactory());
        executorService.execute(new ExecoptionThreadTest());
        executorService.shutdown();


        System.out.println("\r\n\r\n");


        Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExecoptionHandler());
        ExecutorService executorService1 = Executors.newCachedThreadPool(new DeamonThreadTest());
        executorService1.execute(new ExecoptionThreadTest());
        executorService1.shutdown();

    }

    @Test
    public void sharedThreadTesh(){

        IntGenerator intGenerator = new IntGenerator() {
            private int currentEvenVaule = 0;
            @Override
            public synchronized int next() {
                ++currentEvenVaule;
                ++currentEvenVaule;
                Thread.yield();
                return currentEvenVaule;
            }
        };

        EvenChecker.test(intGenerator,10);
//        val even ---> 2
//        val even ---> 4
//         为什么会出现这样的情况？ 因为是线程在改变cahecled变量时，其他线程拿到的 cahecled 变量时true 所以拿到true 的这个线程执行了
    }

    @Test
    public void SynchronizedTest() throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0 ; i < 5; i++){
//            executorService.execute(new SynchronizedTest(i));
            new Thread(new SynchronizedTest(i)).start();
            System.out.println( " main --> " + Thread.currentThread().getState());
//            Thread.sleep(500);
        }
        executorService.shutdown();

            Thread.sleep(3000);

    }
    @Test
    public void ThreadLocalTest() throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0 ; i < 5; i++){
            executorService.execute(new ThreadLocalTest(i));
        }
        Thread.sleep(3000);
        executorService.shutdown();

    }

    @Test
    public void TestGetContextClassLoader(){
        Obtain_Package_Class obtain_package_class = new Obtain_Package_Class();
        URL url = null;
        try {
            Enumeration<URL> urls =  obtain_package_class.findAllClassPathResources("com.handlers");
            while(urls.hasMoreElements() ) {
                url = urls.nextElement();
                System.out.print(url.getFile());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Pattern INNER_PATTERN = Pattern.compile("\\$+.", Pattern.CASE_INSENSITIVE);
        System.out.println(INNER_PATTERN.matcher("com.handlers.Test$ydawd"));
        System.out.println(INNER_PATTERN.matcher("Testy$dawd").find());
    }

    @Test
    public void TestCarforWaxAndBuffed() throws InterruptedException {
        Car car = new Car();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new WaxOn(car));
        executorService.execute(new Buffed(car));
        Thread.sleep(5000);
        executorService.shutdown();
    }

    @Test
    public void ProucerAndConsumerTest(){
        new Restaurant();
    }

    @Test
    public void TestSQL(){
        SQL_DBUtil sql_dbUtil = new SQL_DBUtil("test");
        ResultSet rs = null;
        try {
            sql_dbUtil.getConn();
            Admin_entity admin_entity = null;
            List<Admin_entity> ret = new ArrayList<>();
            rs = operating.db_select(operating.sql_select("users", "id,account,password", "", "", ""), sql_dbUtil);
            while(rs.next()) {
                admin_entity = new Admin_entity();
                admin_entity.setId(rs.getInt("id"));
                admin_entity.setUsername(rs.getString("account"));
                admin_entity.setPassword(rs.getString("password"));
                ret.add(admin_entity);
                logger.info(admin_entity.toString());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                Unit.closeSQL(null,rs,sql_dbUtil);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
//#1(9),#0(9),#1(8),#2(9),#0(8),#3(9),
//#2(8),#4(9),#2(7),#1(7),#2(6),#4(8),
//#3(8),#0(7),#3(7),#4(7),#2(5),#5(9),
//#1(6),#5(8),#2(4),#4(6),#3(6),#0(6),
//#3(5),#4(5),#2(3),#4(4),#5(7),#1(5),
//#5(6),#4(3),#2(2),#3(4),#0(5),#3(3),
//#2(1),#4(2),#5(5),#1(4),#5(4),#4(1),
//#2(end),#3(2),#0(4),#3(1),#4(end),#5(3),
//#1(3),#5(2),#3(end),#5(1),#0(3),#5(end),
//#1(2),#0(2),#1(1),#0(1),#1(end),#0(end),