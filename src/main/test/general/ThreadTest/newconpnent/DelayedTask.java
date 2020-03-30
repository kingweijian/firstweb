package general.ThreadTest.newconpnent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import static java.util.concurrent.TimeUnit.*;
import  java.util.concurrent.TimeUnit;

/**
 * 可以用来打日志
 * ClassName DelayedTask
 * Author weijian
 * DateTime 2020/3/4 12:07 AM
 * Version 1.0
 */
public class DelayedTask implements Runnable, Delayed {

    private static int counter= 0;

    private final int id = counter++;

    private final int delta;

    private final long trigger;

    protected static List<DelayedTask> sequence = new ArrayList<DelayedTask> ();

    public DelayedTask(int delayInMilliseconds) {
        this.delta = delayInMilliseconds;
        this.trigger = System.nanoTime () + NANOSECONDS.convert (delta, MILLISECONDS);
        sequence.add (this);
    }
    @Override
    public long getDelay(TimeUnit unit){
        return unit.convert (trigger - System.nanoTime (),NANOSECONDS);
    }

    // 比较延时，队列里面的元素以此来排序
    @Override
    public int compareTo(Delayed o) {
        DelayedTask that = (DelayedTask) o;
        if(trigger < that.trigger) return -1;
        if(trigger > that.trigger) return 1;
        return 0;
    }

    @Override
    public void run() {
        System.out.print (this + " \t");
    }

    @Override
    public String toString() {
        return String.format ("[%1$-4d]",delta)+ " task " + id;
    }

    public String summary(){
        return "(" + id + ":" + delta + ")";
    }

     static class EndSentinel extends DelayedTask{
        private ExecutorService e;
        public EndSentinel(int delayInMilliseconds,ExecutorService e) {
            super (delayInMilliseconds);
            this.e = e;
        }

        public void run(){
            for(DelayedTask delayedTask : sequence){
                System.out.print (delayedTask.summary () + " \t");
            }
            System.out.println ();
            System.out.println (this + "Calling shutDownNow() ");
//            e.shutdownNow ();
        }
    }

     static class DelayTaskConsumer implements Runnable{

        private DelayQueue<DelayedTask> q;

         public DelayTaskConsumer(DelayQueue<DelayedTask> q) {
             this.q = q;
         }

         @Override
         public void run() {
             while (!Thread.currentThread ().isInterrupted ()) {
                 try {
                     q.take ().run ();
                 } catch (InterruptedException e) {
                     e.printStackTrace ();
                 }
             }
             System.out.println ("Finished DelayQueueTaskConsumer!");
         }
     }
}
