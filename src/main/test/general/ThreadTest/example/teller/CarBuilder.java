package general.ThreadTest.example.teller;

import general.ThreadTest.newconpnent.SemaphoreDemo;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

/**
 * ClassName CarBuilder
 * Author weijian
 * DateTime 2020/4/12 3:46 PM
 * Version 1.0
 */
public class CarBuilder {
    public static void main(String[] args){
        CarQueue chassisQueue = new CarQueue (),
                 finishQueue = new CarQueue ();
        ExecutorService executorService = Executors.newCachedThreadPool ();
        RobotPool robotPool = new RobotPool ();
        executorService.execute (new EngineRobot (robotPool));
        executorService.execute (new DriveTrainRobot (robotPool));
        executorService.execute (new WheelsRobot (robotPool));
        executorService.execute (new Assembler ( chassisQueue, finishQueue, robotPool));
        executorService.execute (new Roporter (finishQueue));
        executorService.execute (new ChassisBuilder (chassisQueue));
        try {
            Thread.sleep (7000);
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }
        executorService.shutdownNow ();
    }
}
// car 基类 各个线程操作的对象，记录各个阶段的状态和一些数据。
class Car{
//    private static int counter = 0;

    private final int id;
    private boolean engine = false, driveTrain = false, wheels =false;

    public Car(int id) {
        this.id = id;
    }
    public Car(){ id = -1;}

    public synchronized int getId() {
        return id;
    }

    public synchronized void addEngine(){
        engine = true;
    }


    public synchronized void addDriverTrain(){
        driveTrain = true;
    }

    public synchronized void addWheels(){
        wheels = true;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", engine=" + engine +
                ", driveTrain=" + driveTrain +
                ", wheels=" + wheels +
                '}';
    }
}

// car队列，继承 LinkedBlockingQueue ，方便各个开发时识别
class CarQueue extends LinkedBlockingQueue<Car>{}

// 生产 car 对象，并放入执行的队列中；
class ChassisBuilder implements Runnable{
    public static Logger logger = Logger.getLogger (ChassisBuilder.class);
    private CarQueue cars;
    private static int counter = 0;

    public ChassisBuilder(CarQueue cars) {
        this.cars = cars;
    }

    @Override
    public void run() {
        try {
            while(!Thread.currentThread ().isInterrupted ()){
                Thread.sleep (500);
                Car car = new Car (counter++);
                logger.info ("ChassisBuilder create " + car);
                cars.put (car);
            }
        } catch (InterruptedException e) {
            logger.info ("ChassisBuilder via ");
        }
    }
}

// 组装 执行car 各个阶段需要做的任务 ， 从car待组装队列中取出，执行各个阶段的任务，执行完放入完成队列
class Assembler implements Runnable{
    Logger logger = Logger.getLogger (Assembler.class);
    private CarQueue chassisQueue ,finishingQueue;
    private Car car;
    private CyclicBarrier barrier = new CyclicBarrier (4);
    private RobotPool robotPool;

    public Assembler(CarQueue chassisQueue, CarQueue finishingQueue, RobotPool robotPool) {
        this.chassisQueue = chassisQueue;
        this.finishingQueue = finishingQueue;
        this.robotPool = robotPool;
    }

    public Car car() {
        return car;
    }

    public CyclicBarrier barrier(){
        return barrier;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread ().isInterrupted ()) {
                car = chassisQueue.take ();
                robotPool.hire (EngineRobot.class,this);
                robotPool.hire (DriveTrainRobot.class,this);
                robotPool.hire (WheelsRobot.class,this);
                barrier.await ();
                finishingQueue.put (car);
            }
        } catch (InterruptedException e) {
            logger.info ("Assembler Reporter via interrout");
        } catch (BrokenBarrierException e) {
            throw  new RuntimeException (e);
        }
        logger.info ("Assembler Off");
    }

}

class Roporter implements Runnable{
    private CarQueue carQueue;
    Logger logger = Logger.getLogger (Roporter.class);

    public Roporter(CarQueue carQueue) {
        this.carQueue = carQueue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread ().isInterrupted ()){
                logger.info (carQueue.take ());
            }
        }catch (InterruptedException e){
            logger.info ("Exiting Reporter via interrout");
        }
    }
}
// DriveTrain 阶段需要做的事 继承Robot 因为 做事是 robot 在做，而这个类是决定做什么
class DriveTrainRobot extends Robot{
    Logger logger = Logger.getLogger (DriveTrainRobot.class);
    public DriveTrainRobot(RobotPool robotPool) {
        super(robotPool);
    }
    protected void performService(){
        logger.info (this + " installing DriveTrain");

    }
}
// wheels 阶段需要做的事 继承Robot 因为 做事是 robot 在做，而这个类是决定做什么
class WheelsRobot extends Robot{
    Logger logger = Logger.getLogger (WheelsRobot.class);
    public WheelsRobot(RobotPool robotPool) {
        super(robotPool);
    }
    protected void performService(){
        logger.info (this + " installing Wheels");

    }
}
// Engine 阶段需要做的事  继承Robot 因为 做事是 robot 在做，而这个类是决定做什么
class EngineRobot extends Robot{
    Logger logger = Logger.getLogger (EngineRobot.class);
    public EngineRobot(RobotPool robotPool) {
        super(robotPool);
    }
    @Override
    protected void performService(){
        logger.info (this + " installing engine");

    }
}
// 抽象 robot 需要做的事
abstract class Robot implements Runnable{
    Logger logger = Logger.getLogger (Robot.class);
    private RobotPool robotPool;
    private boolean engage = false;
    public Robot(RobotPool robotPool) {
        this.robotPool = robotPool;
    }
    protected Assembler assembler;
    public Robot assignAssembler(Assembler assembler){
        this.assembler = assembler;
        return this;
    }
    public synchronized void engage() {
        engage = true;
        notifyAll ();
    }

    abstract protected void performService();

    @Override
    public void run() {
        try {

            powerDown ();
            while (!Thread.currentThread ().isInterrupted ()){
                performService ();
                assembler.barrier ().await ();
                powerDown ();
            }
        }catch (InterruptedException e){
            logger.info ("Exiting " + this + " via interrupt");
        } catch (BrokenBarrierException e) {
            logger.info ("BrokenBarrierException");
        }
        logger.info (this + " Off");
    }

    private synchronized void powerDown() throws InterruptedException {
        engage = false;
        assembler = null;
        // 加入这个 robot
        robotPool.release (this);
        try {

            while (engage == false)
                wait ();
        }catch (InterruptedException e){
            logger.info (e);
        }
    }

    @Override
    public String toString() {
        return this.getClass ().getName ();
    }
}

// robot 线程池
class RobotPool{
    private Set<Robot> pool = new HashSet<Robot> ();
    public synchronized void add(Robot r){
        pool.add (r);
        // 添加car 之后 通知所有 robot 有任务进来
        notifyAll ();
    }
    // 事情做完，删除当前 robot
    public synchronized void hire(Class<? extends Robot> robotType, Assembler d) throws InterruptedException {
        for (Robot robot : pool){
            if(robot.getClass ().equals (robotType)){
                pool.remove (robot);
                robot.assignAssembler(d);
                robot.engage ();
                return;
            }
        }
        wait ();
        hire (robotType,d);
    }

    public synchronized void release(Robot robot) { add (robot); }
}
