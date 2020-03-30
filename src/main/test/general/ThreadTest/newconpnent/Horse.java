package general.ThreadTest.newconpnent;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * ClassName Horse
 * Author weijian
 * DateTime 2020/3/2 11:43 PM
 * Version 1.0
 */
public class Horse implements Runnable {

    private static int counter = 0;

    private final int id = counter++;
    private int strids = 0;

    private static Random random = new Random (47);

    private  CyclicBarrier cyclicBarrier;

    public Horse(CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
    }

    public synchronized int getStrids(){
        return strids;
    }
    @Override
    public void run() {
        try {
            while (!Thread.currentThread ().isInterrupted ()){
                synchronized (this){
                    strids += random.nextInt (3);
                }
                    cyclicBarrier.await ();
            }
        } catch (InterruptedException e) {
            e.printStackTrace ();
        } catch (BrokenBarrierException e) {
            e.printStackTrace ();
        }

    }

    @Override
    public String toString() {
        return "Horse{" +
                "id=" + id +
                ", strids=" + strids +
                '}';
    }

    public String tracks(){
        StringBuffer stringBuffer = new StringBuffer ();
        for (int i = 0; i < getStrids (); i++){
            stringBuffer.append (i);
        }
        return stringBuffer.toString ();
    }
}
