package general;

/**
 * ClassName Unit
 * Author weijian
 * DateTime 2019/12/25 12:06 AM
 * Version 1.0
 */
public class Unit {
    public static void print(String s){
        System.out.println(s);
    }

    public static void sleep(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
