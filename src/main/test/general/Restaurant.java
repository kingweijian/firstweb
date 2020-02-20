package general;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 生产者与消费者例子
 * 一个饭店，厨师和服务员
 * ClassName ProucerAndConsumer
 * Author weijian
 * DateTime 2019/12/24 11:46 PM
 * Version 1.0
 */
public class Restaurant {
    Meal meal;
    ExecutorService executorService = Executors.newCachedThreadPool();
    WaitPerson waitPerson = new WaitPerson(this);
    Chef chef = new Chef(this);
    public Restaurant(){
        executorService.execute(waitPerson);
        executorService.execute(chef);
    }
    public static void main(String[] args){
        new Restaurant();
    }
}
// 餐厅
//class Restaurant{
//    Meal meal;
//    ExecutorService executorService = Executors.newCachedThreadPool();
//    WaitPerson waitPerson = new WaitPerson(this);
//    Chef chef = new Chef(this);
//    public Restaurant(){
//        executorService.execute(waitPerson);
//        executorService.execute(chef);
//    }
//}
// 餐 meal
class Meal{
    private final int orderNum;
    public Meal(int orderNum){
        this.orderNum = orderNum;
    }

    @Override
    public String toString() {
        return "Meal " + orderNum;
    }
}
// 等待人
class WaitPerson implements Runnable{
    private Restaurant restaurant;
    public  WaitPerson(Restaurant restaurant){
        this.restaurant = restaurant;
    }
    @Override
    public void run() {
        try {

            while (!Thread.interrupted()){
                synchronized (this){
                    while (restaurant.meal == null){
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                Unit.print("WaitPerson got " + restaurant.meal);
                synchronized (restaurant.chef){
//                Unit.print("I'm fucking coming! ");
                    restaurant.meal = null;
                    restaurant.chef.notifyAll();
                }
//                Unit.sleep(100);

            }
        }catch (Exception e){
            Unit.print("WaitPerson InterruptedException!!");
        }
    }
}
// 厨师
class Chef implements Runnable{
    private  int i = 0;
    private Restaurant restaurant ;
    public Chef(Restaurant restaurant){
        this.restaurant = restaurant;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                synchronized (this) {
                    while (restaurant.meal != null) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (++i == 10) {
                    Unit.print("Out of foot, closing!!");
                    restaurant.executorService.shutdownNow();
                    System.exit(0);
                }
                // 到这里表示单子已经被拿走，可以开始下一个单子了，拿到单子
                System.out.print("Order Up ! ");
                synchronized (restaurant.waitPerson) {
                    restaurant.meal = new Meal(i);
                    restaurant.waitPerson.notifyAll();
                }
//                Unit.sleep(100);
            }
        }catch (Exception e){
            Unit.print("Chef nterruptedException!!");
        }
    }
}