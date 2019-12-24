package general;

import java.util.concurrent.Callable;

/**
 * ClassName Thread_test
 * Author weijian
 * DateTime 2019/11/26 10:32 PM
 * Version 1.0
 */
public class Thread_test {

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(new ThreadOne()).start();
        }


    }

}

class ThreadOne implements Runnable {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getId() + " -- " + 1);
    }
}

class ThreadMore implements Runnable {
    public boolean procsssing = true;
    protected int countDown = 10;
    private static int tashcount = 0;
    public final int id = tashcount++;


    @Override
    public void run() {
        while (countDown-- > 0) {
            System.out.println( Thread.currentThread() +  " --- #"+id+"(" + (countDown > 0 ? countDown : "end") + "),");
            // 告诉cpu我当前的工作做完了，可以分时间给其他线程做事了
            Thread.yield();
        }
        System.out.println("执行完了！！" + id);

    }
}

class TestVolatile implements  Runnable{
    public volatile boolean isStart = true;
    public static int taskcount = 0;
    public final int id = taskcount++;
    @Override
    public void run() {

        while (isStart){
            try {
                System.out.println("我执行了！！！ $(" + id + ")" );
                Thread.sleep(1000);
                Thread.yield();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {

        System.out.println("线程被终结！！！ isStart变量的值是 ： "+isStart+" $(" + id + ")" );


        return super.clone();
    }
}

class TaskWithCallable implements Callable<String>{
    int id = 0;
    public TaskWithCallable(int id){
        this.id = id;
    }

    @Override
    public String call() throws Exception {
        return "result of TaskWithCallable " + id;
    }
}
