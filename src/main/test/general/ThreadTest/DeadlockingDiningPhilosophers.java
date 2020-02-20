package general.ThreadTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ClassName DeadlockingDiningPhilosophers 哲学家吃饭产生的死锁例子
 * Author weijian
 * DateTime 2020/1/4 11:11 PM
 * Version 1.0
 */
public class DeadlockingDiningPhilosophers {
    public static void main(String[] args) throws InterruptedException {
        int ponder =2,size = 2;
        ExecutorService executorService = Executors.newCachedThreadPool();
        Chopstick[] chopsticks = new Chopstick[size];
        for (int i = 0 ; i < size ; i++)
            chopsticks[i] = new Chopstick();

            // 死锁
//        for (int i = 0; i < size; i++)
//            executorService.execute(new Philosopher(chopsticks[i],chopsticks[(i+1)%size],i,ponder));

        // 解决死锁
        for (int i = 0; i < size; i++){
            if(i < ( size -1 ))
                executorService.execute(new Philosopher(chopsticks[i],chopsticks[(i+1)%size],i,ponder));
            else
                executorService.execute(new Philosopher(chopsticks[0],chopsticks[size-1],i,ponder));
        }
//        System.out.println(executorService.isTerminated());
        Thread.sleep(5000);
        executorService.shutdownNow();

//        System.out.println(executorService.isTerminated());
    }
}
