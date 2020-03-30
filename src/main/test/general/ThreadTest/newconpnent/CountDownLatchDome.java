package general.ThreadTest.newconpnent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ClassName CountDownLatchDome
 * Author weijian
 * DateTime 2020/2/24 6:58 PM
 * Version 1.0
 */
public class CountDownLatchDome {
    public final static int SIZE = 100;
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool ();
        CountDownLatch countDownLatch = new CountDownLatch (SIZE);
        for (int i = 0; i< 10;i++){
            executorService.execute (new WaitingTask (countDownLatch));
        }
        for (int i = 0; i< SIZE;i++){
            executorService.execute (new TaskPortion (countDownLatch));
        }
        countDownLatch.await ();
        System.out.println ("Launched all task!!!");
        executorService.shutdownNow ();

    }
}
