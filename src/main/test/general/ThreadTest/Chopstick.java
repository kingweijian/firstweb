package general.ThreadTest;

/**
 * ClassName Chopstick 筷子
 * Author weijian
 * DateTime 2020/1/4 10:46 PM
 * Version 1.0
 */
public class Chopstick {
    public boolean taken = false;
    /**
    * @Description: n拿起筷子
    * @Param: []
    * @return: void
    * @Author: weijian
    * @Date: 2020/1/4
    */
    public synchronized void take() throws InterruptedException {
        while (taken)
            wait();
        taken = true;
    }
    /**
    * @Description: 放下筷子
    * @Param: []
    * @return: void
    * @Author: weijian
    * @Date: 2020/1/4
    */
    public synchronized void drop(){
        taken = false;
        notifyAll();
    }
}
