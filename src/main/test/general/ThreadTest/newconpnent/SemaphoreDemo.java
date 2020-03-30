package general.ThreadTest.newconpnent;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * ClassName SemaphoreDemo
 * Author weijian
 * DateTime 2020/3/9 12:08 AM
 * Version 1.0
 */
public class SemaphoreDemo {
    static Logger logger = Logger.getLogger (SemaphoreDemo.class);
    final static int SIZE =25;
    public static void main(String[] args) throws InterruptedException {
        final Pool<Fat> pool = new Pool<Fat> (Fat.class,SIZE);
        ExecutorService executorService = Executors.newCachedThreadPool ();
        for (int i = 0 ; i < SIZE; i++){
            executorService.execute (new CheckoutTask<Fat> (pool));
        }
        logger.info ("ALL checkoutTask created");
        List<Fat> list = new ArrayList<Fat> ();
        for (int i = 0 ; i <SIZE ;i++){
            Fat fat = pool.checkOut ();
            logger.info (i + " : main thread check Out");
            fat.operation ();
            list.add (fat);
        }
        Future<?> blocked = executorService.submit (new Runnable () {
            @Override
            public void run() {
                try {
                    pool.checkOut ();
                    logger.info ("我被执行了");
                } catch (InterruptedException e) {
                    e.printStackTrace ();
                }
            }
        });
        Thread.sleep (2000);
        blocked.cancel (true);
        logger.info ("Checking in objects in " + list);
        for (Fat f : list)
            pool.checkIn (f);
        for (Fat f : list)
            pool.checkIn (f);

        executorService.shutdownNow ();
    }
}

class CheckoutTask<T> implements Runnable{
    Logger logger = Logger.getLogger (CheckoutTask.class);
    private static int counter = 0;
    private final int id =counter++;
    private Pool<T> pool;

    public CheckoutTask(Pool<T> pool) {
        this.pool = pool;
    }

    @Override
    public void run() {
        T item = null;
        try {
            item = pool.checkOut ();
            logger.info (this + " checked out " + item);
            Thread.sleep (1000);
            pool.checkIn (item);
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }
    }

    @Override
    public String toString() {
        return "CheckedTask{" +
                "id=" + id +
                '}';
    }
}
