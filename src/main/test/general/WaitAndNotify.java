package general;

/**
 * ClassName WaitAndNotify
 * Author weijian
 * DateTime 2019/12/22 6:10 PM
 * Version 1.0
 */
public class WaitAndNotify {
}

/**
 * 这个例子是为车子上漆和抛光
 */
class Car{
    // 完成标识
    private boolean waxOn = false;
    // 完成作业
    public synchronized void waxed(){
        waxOn = true;
        notifyAll();
    }
    // 抛光作业
    public synchronized void buffed(){
        waxOn = false;
        notifyAll();
    }
    // 等待完成作业
    public synchronized void waitForWaxing() throws InterruptedException {
        while(waxOn)
            wait();
    }

    //等待抛光作业
    public synchronized void waitForBuffed() throws InterruptedException {
        while (!waxOn)
            wait();
    }
}

class WaxOn implements Runnable{
    private Car car;
    public WaxOn(Car car){
        this.car = car;
    }
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()){
            System.out.println("Wax On ! ");
            try {
                Thread.sleep(200);
                // 涂漆作业完毕
                car.waxed();
                // 调用等待方法等待再次涂漆工作，等待再次涂漆工作
                car.waitForWaxing();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
class Buffed implements Runnable{
    private Car car;
    public Buffed(Car car){
        this.car = car;
    }
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()){
            System.out.println("wax Off!");
            try {
                Thread.sleep(200);
                // 抛光作业完毕
                car.buffed();;
                // 抛光操作进入等到状态
                car.waitForBuffed();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}