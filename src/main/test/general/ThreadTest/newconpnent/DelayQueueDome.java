package general.ThreadTest.newconpnent;

import java.util.Random;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ClassName DelayQueueDome
 * Author weijian
 * DateTime 2020/3/4 12:35 AM
 * Version 1.0
 */
public class DelayQueueDome {
    public static void main(String[] args){
        Random random = new Random (47);
        ExecutorService executorService = Executors.newCachedThreadPool ();
        DelayQueue<DelayedTask> delayedTasks = new DelayQueue<DelayedTask> ();
        for (int i = 0; i < 20 ; i++){
            delayedTasks.put (new DelayedTask (random.nextInt (5000)));
        }

        delayedTasks.add (new DelayedTask.EndSentinel (5000,executorService));

        executorService.execute (new DelayedTask.DelayTaskConsumer (delayedTasks));
        delayedTasks.put (new DelayedTask (4999));

        delayedTasks.put (new DelayedTask (4999));

        delayedTasks.put (new DelayedTask (4999));


    }
}
