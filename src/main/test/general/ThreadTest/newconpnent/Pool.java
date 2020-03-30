package general.ThreadTest.newconpnent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * ClassName Pool
 * Author weijian
 * DateTime 2020/3/9 12:09 AM
 * Version 1.0
 */
public class Pool<T> {
    private int size;
    // 存储需要执行
    private List<T> itmes = new ArrayList<T> ();
    // 原子变量，用来控制迁出操作，为什么要用原子变量，因为原子变量是直接修改的内存，修改之后每次被调用时访问的是实时修改的值
    private volatile boolean[] checkedOut;

    private Semaphore avliable;

    public Pool(Class<T> classObject, int size) {
        this.size = size;
        avliable = new Semaphore (size,true);
        checkedOut = new boolean[size];
        for (int i = 0; i < size; i++ ){
            try {
                itmes.add (classObject.newInstance ());
            } catch (InstantiationException e) {
                e.printStackTrace ();
            } catch (IllegalAccessException e) {
                e.printStackTrace ();
            }
        }
    }
    // 签出时返回签出对象，使用完必须调用checkIn签回 获得信号量
    public T checkOut() throws InterruptedException {
        avliable.acquire ();
        return getItem();
    }

    // 签回对象  用完释放信号量
    public void checkIn(T x){
       if (releaseItem (x))
            avliable.release ();
    }
    private synchronized boolean releaseItem(T item){
        int index = itmes.indexOf (item);
        if(index == -1) return false;
        if(checkedOut[index]){
            checkedOut[index] = false;
            return true;
        }
        return false;
    }
    private synchronized T getItem() {
        for (int i = 0; i < size; i++){
            if(!checkedOut[i]) {
                checkedOut[i] = true;
                return itmes.get (i);
            }
        }
        return null;
    }



}
