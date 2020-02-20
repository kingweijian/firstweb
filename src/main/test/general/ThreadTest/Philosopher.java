package general.ThreadTest;

import java.util.Random;

/**
 * ClassName Philosopher 哲学家
 * Author weijian
 * DateTime 2020/1/4 10:59 PM
 * Version 1.0
 */
public class Philosopher implements Runnable{
    private Chopstick left;
    private Chopstick right;
    private final int id;
    private  final int ponderFactor;
    Random random = new Random(47);


    public Philosopher(Chopstick left, Chopstick right, int id, int ponderFactor) {
        this.left = left;
        this.right = right;
        this.id = id;
        this.ponderFactor = ponderFactor;
    }
    /**
    * @Description: 思考时间
    * @Param: []
    * @return: void
    * @Author: weijian
    * @Date: 2020/1/4
    */
    public void puse() throws InterruptedException {
        if(ponderFactor == 0) return;
        Thread.sleep(random.nextInt(ponderFactor * 250));
    }


    @Override
    public void run() {
        try{
            while (!Thread.interrupted()){
                System.out.println(this + " thinking");
                puse();

                System.out.println(this + " grabbing right");
                right.take();

                System.out.println(this + " grabbing left");
                left.take();

                right.drop();
                left.drop();
            }
        }catch (Exception e){
            System.out.println(this + " exiting via interrupt" );
        }finally {
//            System.out.println(this + " exiting via interrupt" );
        }
    }

    @Override
    public String toString() {
        return "Philosopher{" + id +
                '}';
    }
}
