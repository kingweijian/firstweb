package general.ThreadTest.Pipeline;

import java.io.IOException;
import java.io.PipedReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ClassName PipedRead
 * Author weijian
 * DateTime 2020/2/19 3:10 PM
 * Version 1.0
 */
public class Receiver implements Runnable {
    private PipedReader reader ;

    public Receiver(Sender sender) {
        try {
            this.reader = new PipedReader (sender.getPipedWriter ());
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread ().isInterrupted ()) {
                System.out.println ("Reader : " + (char) reader.read ());
            }
        } catch (IOException e) {
            e.printStackTrace ();
        }finally {
            try {
                reader.close ();
            } catch (IOException e) {
                e.printStackTrace ();
            }
        }

    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool ();
        Sender sender = new Sender ();
        Receiver receiver = new Receiver (sender);
        executorService.execute (sender);
//        executorService.execute (new Receiver (sender));
//        executorService.execute (receiver);
        Thread.sleep (20000);
        sender.getPipedWriter ();

        executorService.shutdownNow ();

    }
}
