package general.ThreadTest.newconpnent;

import org.apache.log4j.Logger;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * ClassName TaskPortion
 * Author weijian
 * DateTime 2020/2/24 6:48 PM
 * Version 1.0
 */
public class TaskPortion implements Runnable {
    public final static Logger logger = Logger.getLogger (TaskPortion.class);
    private static int counter = 0;
    private final int id = counter++;
    private Random random = new Random (47);
    private final CountDownLatch countDownLatch ;

    public TaskPortion(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        doWork ();
        countDownLatch.countDown ();
    }
    public void doWork(){
        try {
            Thread.sleep (random.nextInt (2000));
            logger.info (this + " completed");
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }

    }

    @Override
    public String toString() {
        return String.format ("%1$-3d",id);
    }
}
class WaitingTask implements Runnable{
    Logger logger = Logger.getLogger (WaitingTask.class);
    private static int counter = 0;
    private final int id = counter++;
    private Random random = new Random (47);
    private final CountDownLatch latch;

    public WaitingTask(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            latch.await ();
            logger.info ("Latch barrier passed for " + this);
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }
    }

    @Override
    public String toString() {
        return String.format ("Waiting Task %1$-3d",id);
    }
}

