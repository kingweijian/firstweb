package general.ThreadTest.Producerconsumer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.*;

/**
 * ClassName Consumer
 * Author weijian
 * DateTime 2020/2/16 8:29 PM
 * Version 1.0
 */
public class TestLiftOffRunner {
    public static void getKey(String msg){
        System.out.println (msg);
        try {
            new BufferedReader (new InputStreamReader (System.in)).readLine ();
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }

    static void test(String msg, BlockingQueue<LiftOff> queue){
        System.out.println (msg);
        // ******************************
        // 实例化消费者，并放入消费队列
        LiftOffRunner liftOff = new LiftOffRunner (queue);
        // 定义一个线程 启动消费者
        Thread thread = new Thread (liftOff);
        //启动  启动时 因为 BlockingQueue 是空的，所以这个线程会被挂起，等待 blockingqueue 中被添加任务，然后再执行  这个步骤由 blockingqueue 的机制实现
        thread.start ();
        // *******************************

        //********************************
        // 模拟生成者往消费者队列里面放入任务
        for (int i = 0 ; i < 5 ; i++){
            liftOff.add (new LiftOff (5));
        }
        //********************************
        getKey ("Press 'Enter' (" + msg + ")");
        thread.interrupt ();
        System.out.println ("Finished " + msg + " test");
    }

    public static void main(String[] args){
        test ("LinkedBlockinQueue",new LinkedBlockingQueue<LiftOff> ());

        test("ArrayBlockingQueue",new ArrayBlockingQueue<LiftOff> (1));

        test("SynchronousQueue", new SynchronousQueue<LiftOff> ());

    }

}
