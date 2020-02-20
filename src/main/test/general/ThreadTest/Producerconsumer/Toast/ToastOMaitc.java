package general.ThreadTest.Producerconsumer.Toast;

import java.sql.BatchUpdateException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ClassName ToastOmaitc
 * Author weijian
 * DateTime 2020/2/17 4:28 PM
 * Version 1.0
 */
public class ToastOMaitc {
    public static void main(String[] args) throws InterruptedException {
        ToastQueue
                dayQueue = new ToastQueue (),
                bufferedQueue = new ToastQueue (),
                finishedQueue = new ToastQueue ();
        ExecutorService executorService = Executors.newCachedThreadPool ();
        executorService.execute (new Toaster (dayQueue));
        executorService.execute (new Buttered (bufferedQueue,dayQueue));
        executorService.execute (new Jummed (bufferedQueue,finishedQueue));
        executorService.execute (new Eater (finishedQueue));
        Thread.sleep (5000);
        executorService.shutdownNow ();
    }
}
