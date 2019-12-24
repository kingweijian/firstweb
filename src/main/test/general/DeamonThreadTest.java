package general;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 测试后台线程和线程工程
 *
 * ClassName DeamonThreadTest
 * Author weijian
 * DateTime 2019/12/3 10:06 PM
 * Version 1.0
 */
public class DeamonThreadTest implements ThreadFactory,Runnable,ThreadControl {
    public volatile boolean start = true;


    @Override
    public void run() {
        while (start){
            try {
                Thread.sleep(1000);
                System.out.println(Thread.currentThread() + " " + this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void startThread(int i,DeamonThreadTest deamonThreadTest) {
        for (int j = 0 ; i < j ; i++){
           Thread thread =  new Thread(deamonThreadTest);
           thread.setDaemon(true);
           thread.start();
        }
    }

    @Override
    public void stopThread() {
        this.start = false;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        return thread;
    }
}

interface ThreadControl{
   public void startThread(int i,DeamonThreadTest deamonThreadTest);
   public void stopThread();
}

class DeamonFinallyTest implements Runnable{

    @Override
    public void run() {
        System.out.println("Start Thread！！");
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            System.out.println("This should always run?");
        }
    }
}

class ExecoptionThreadTest implements Runnable{

    @Override
    public void run() {
        Thread t = Thread.currentThread();
        System.out.println( "Catch Handler : " + t.getUncaughtExceptionHandler() );
        throw new RuntimeException();
    }
}

class MyUncaughtExecoptionHandler implements Thread.UncaughtExceptionHandler{
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println(" Catch : " + e + "\n\r");
    }
}

class ExecoptionThreadFactory implements ThreadFactory{

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r);
        t.setUncaughtExceptionHandler(
                new MyUncaughtExecoptionHandler()
        );
        System.out.println(" 设置线程捕获异常的方式为:" + t.getUncaughtExceptionHandler());
        return t;
    }
}

abstract class IntGenerator{
    public volatile boolean cahecled = true;

    public void cahecled(){ cahecled = false;}
    public abstract int next();
    public boolean isCahecled(){return cahecled;}
}

class EvenChecker implements Runnable{
   IntGenerator intGenerator = null;

   public final int id ;
   public EvenChecker(IntGenerator intGenerator,int ident){
       this.intGenerator = intGenerator;
       this.id = ident;
   }

    @Override
    public void run() {
        while (intGenerator.isCahecled()){
            int val = intGenerator.next();
            if(val % 2 == 0){
                System.out.println("val even ---> " + val);
                intGenerator.cahecled();
            }
        }
    }

    public static void test(IntGenerator intGenerator,int count){
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < count ;i++){
            executorService.execute(new EvenChecker(intGenerator,i));
        }
        executorService.shutdown();
    }


}

class SynchronizedTest implements  Runnable{
    int i = 0;
    public  SynchronizedTest(int i ){
        this.i = i;
    }
//    阻塞状态是否打印不出？，因为打印的时候线程都是在执行的状态，阻塞的话线程是不被执行的
    @Override
    public void run() {
        Thread t = Thread.currentThread();
        System.out.println(" 这是这个方法返回的 整形 --- > " + Test(t));
        System.out.println(" run " + this.i +  "--> " + t.getState());
    }
    public synchronized int Test(Thread t){

        try {
            System.out.println(" synchronized ---> " + t.getState());
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this.i;
    }
}

class ThreadLocalHandle{
    private static ThreadLocal<Integer> local = new ThreadLocal<Integer>(){
        @Override
        protected Integer initialValue() {
            Random random = new Random(47);
            return random.nextInt(10000);
        }
    };

    public static void increment(){
        local.set(local.get() + 1);
    }

    public static int get(){
        return local.get();
    }
}

// 本地存储 ， 使用ThreadLocal对象进行存储
class ThreadLocalTest implements Runnable{

    private int id;
    // set id
    public ThreadLocalTest(int id){
        this.id = id;
    }

    @Override
    public String toString() {
        return "#(" + id + ") " + ThreadLocalHandle.get();
    }

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()){
            ThreadLocalHandle.increment();
            System.out.println(this);
            Thread.yield();
        }
    }
}