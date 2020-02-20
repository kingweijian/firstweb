package general.ThreadTest.Pipeline;

import java.io.IOException;
import java.io.PipedWriter;
import java.util.Random;

/**
 * ClassName PipedWiter
 * Author weijian
 * DateTime 2020/2/19 3:10 PM
 * Version 1.0
 */
public class Sender implements Runnable{
    private PipedWriter pipedWriter = new PipedWriter ();
    private Random random = new Random (47);

    public PipedWriter getPipedWriter() {
        return pipedWriter;
    }

    @Override
    public void run() {

        try {
            while (true) {
                for (char i = 'a'; i <= 'z'; i++) {
                    pipedWriter.write (i);
                    System.out.println (i);
//                    Thread.sleep (100 + random.nextInt (500));
                }
            }
//        } catch (InterruptedException e) {
//            e.printStackTrace ();
        } catch (IOException e) {
            e.printStackTrace ();
        }finally {
            try {
                pipedWriter.close ();
            } catch (IOException e) {
                e.printStackTrace ();
            }
        }

    }
}

