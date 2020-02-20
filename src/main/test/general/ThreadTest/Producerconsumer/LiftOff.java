package general.ThreadTest.Producerconsumer;

/**
 * ClassName LiftOff
 * Author weijian
 * DateTime 2020/2/16 8:41 PM
 * Version 1.0
 */
public class LiftOff implements Runnable {
    private int i = 0;
    public LiftOff(int i ) {
        this.i = i;
    }

    @Override
    public void run() {
//        for (;i>0;i--){
            System.out.println ( Thread.currentThread ().getName () + "  ---  "+ i);
//        }
    }
}
