package general.ThreadTest.Producerconsumer.Toast;

import org.apache.log4j.Logger;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * ClassName Toast
 * Author weijian
 * DateTime 2020/2/17 2:32 PM
 * Version 1.0
 */
public class Toast {
    Logger logger = Logger.getLogger (Toast.class);
    public enum Status{ DRY, BUTTERED, JAMMED};
    private Status status = Status.DRY;
     private final int id;
     public Toast(int id){
         this.id = id;
     }
    public void buttered(){
         status = Status.BUTTERED;
    }

    public void jammed(){
         status = Status.JAMMED;
    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Toast{" +
                "status=" + status +
                ", id=" + id +
                '}';
    }
}

class ToastQueue extends LinkedBlockingQueue<Toast>{}

class Toaster implements Runnable{
    Logger logger = Logger.getLogger (Toaster.class);
    private ToastQueue ToastQueue ;

    private int counnt = 0;

    private Random random = new Random (47);
    public Toaster(ToastQueue ToastQueue) {
        this.ToastQueue = ToastQueue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread ().isInterrupted ()) {
                Thread.sleep (100 + random.nextInt (500));
                Toast toast = new Toast (counnt++);
                logger.info (toast);
                ToastQueue.put (toast);
            }
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }
    }
}

class Buttered implements Runnable{
    Logger logger = Logger.getLogger (Buttered.class);
    private ToastQueue butteredQueue , dry;

    public Buttered(ToastQueue butteredQueue, ToastQueue dry) {
        this.butteredQueue = butteredQueue;
        this.dry = dry;
    }

    @Override
    public void run() {
        while (!Thread.currentThread ().isInterrupted ()){
            try {
                Toast toast = dry.take ();
                toast.buttered ();
                logger.info (toast);
                butteredQueue.put (toast);
            } catch (InterruptedException e) {
                e.printStackTrace ();
            }

           logger.info ("Buffered off !!");
        }
    }
}

class Jummed implements Runnable{
    Logger logger = Logger.getLogger (Jummed.class);
    ToastQueue buffered, finished;

    public Jummed(ToastQueue buffered, ToastQueue finished) {
        this.buffered = buffered;
        this.finished = finished;
    }



    @Override
    public void run() {
        while (!Thread.currentThread ().isInterrupted ()){
            Toast Toaster = null;
            try {
                Toaster = buffered.take ();
                Toaster.jammed ();
                logger.info (Toaster);
                finished.put (Toaster);
            } catch (InterruptedException e) {
                e.printStackTrace ();
            }
        }
       logger.info ("Jummer Off  !!");
    }
}

class Eater implements Runnable{
    Logger logger = Logger.getLogger (Eater.class);
    private ToastQueue finished;
    private int counnt = 0;

    public Eater(ToastQueue finished) {
        this.finished = finished;
    }

    @Override
    public void run() {
        while (!Thread.currentThread ().isInterrupted ()){
            try {
                Toast toast = finished.take ();
                if(toast.getId () != counnt++ || toast.getStatus () != Toast.Status.JAMMED){
                   logger.info (" >>>> Error " + toast);
                    System.exit (1);

                }else {
                   logger.info ("Chomp " + toast);
                }
            } catch (InterruptedException e) {
                e.printStackTrace ();
            }

        }
       logger.info ("Eater off ");
    }
}
