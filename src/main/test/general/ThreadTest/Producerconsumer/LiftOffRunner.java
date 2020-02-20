package general.ThreadTest.Producerconsumer;

import java.util.concurrent.BlockingQueue;

/**
 * 消费者
 * ClassName Proucer
 * Author weijian
 * DateTime 2020/2/16 8:24 PM
 * Version 1.0
 */
public class LiftOffRunner implements Runnable{
    private BlockingQueue<LiftOff> liftOffs ;

    public LiftOffRunner(BlockingQueue<LiftOff> liftOffs) {
        this.liftOffs = liftOffs;
    }

    public void add(LiftOff liftOff){
        try {
            liftOffs.put (liftOff);
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }
    }



    @Override
    public void run() {
        try {
            while (!Thread.currentThread ().isInterrupted ()){
                LiftOff liftOff = liftOffs.take ();
                liftOff.run ();
            }
        }catch (InterruptedException e){
            e.printStackTrace ();
        }

    }
}
