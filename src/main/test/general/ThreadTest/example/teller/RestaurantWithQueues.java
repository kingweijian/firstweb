package general.ThreadTest.example.teller;


import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

/**
 * ClassName RestaurantWithQueues
 * Author weijian
 * DateTime 2020/4/7 5:35 PM
 * Version 1.0
 */
public class RestaurantWithQueues {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool ();
        Restaurant restaurant = new Restaurant (executorService,5,2);
        executorService.execute (restaurant);
        Thread.sleep (1000);
        executorService.shutdownNow ();
    }
}

class Order{
    private static int counter = 0;
    private final int id = counter++;
    private Customer customer;
    private WaitPerson waitPerson;
    private Food food;

    public Order(Customer customer, WaitPerson waitPerson, Food food) {
        this.customer = customer;
        this.waitPerson = waitPerson;
        this.food = food;
    }

    public Food itme(){return food;}
//

    public Customer getCustomer() {
        return customer;
    }

    public WaitPerson getWaitPerson() {
        return waitPerson;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customer=" + customer +
                ", waitPerson=" + waitPerson +
                ", food=" + food +
                '}';
    }
}

class Food{
    private static int counter = 0;
    private final int id = counter++;
    private static Random random =new Random (47);
    public enum Foods {
        SPRING_ROLLS,
        BURRITO,
        SOUP,
        VINDALOO

    }
    private Foods foods;

    public Food(Foods foods) {
        this.foods = foods;
    }

    public Foods getFoods() {
        return foods;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Food " + id + " " + foods ;
    }
}

class Course{
    static Map<Integer,Course> courses = new HashMap<Integer,Course> ();
    static Random random = new Random (47);
//    private static WaitPerson waitPerson = new WaitPerson ();
    Food.Foods[] foods = Food.Foods.values ();
    static {
        for (int i = 0 ;i < 10 ; i++)
        courses.put (i,new Course ());
    }

    public Food randomSelection(){

        Food food = new Food (foods[random.nextInt (foods.length)]);

        return food;
    }

}

class Plate{
    private final Order order;
    private final Food food;

    public Plate(Order order, Food food) {
        this.order = order;
        this.food = food;
    }

    public Food getFood() {
        return food;
    }

    public Order getOrder() {
        return order;
    }

    @Override
    public String toString() {
        return food.toString ();
    }
}

class Customer implements Runnable{
    private static int counter = 0;
    private final int id = counter++;
    private WaitPerson waitPerson;
    private SynchronousQueue<Plate> placeSetting = new SynchronousQueue<Plate> ();

    public Customer(WaitPerson waitPerson) {
        this.waitPerson = waitPerson;
    }

    public void deliver(Plate p) throws InterruptedException {
        placeSetting.put (p);
    }

    @Override
    public void run() {
        for (Course course : Course.courses.values ()){
            Food food = course.randomSelection ();
            try {

                waitPerson.placeOrder (this,food);
                System.out.println (this + " eating" + placeSetting.take ());
            }catch (InterruptedException e){
                System.out.println (this + " waiting for " + course + " interrupted" );
                break;
            }
        }
        System.out.println (this+" finished meal , leaving");
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                '}';
    }
}

class WaitPerson implements Runnable{
    private static int counter = 0;
    private final int id = counter++;
    private Restaurant restaurant;
    BlockingQueue<Plate> filledOrders = new LinkedBlockingQueue<Plate> ();
    public WaitPerson() {
    }

    public WaitPerson(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
    public void placeOrder(Customer customer,Food food){
        try {
            restaurant.orders.put (new Order (customer,this,food));
        } catch (InterruptedException e) {
            System.out.println (this + " placeOrder interrput");
        }
    }

    @Override
    public void run() {

        try {
            while (!Thread.currentThread ().isInterrupted ()) {
                Plate plate = filledOrders.take ();
                System.out.println (this + " received " + plate + " delivering to " + plate.getOrder ().getCustomer ());
                plate.getOrder ().getCustomer ().deliver (plate);
            }
        } catch (InterruptedException e) {
           System.out.println (this+ " interrupted" );
        }
        System.out.println (this +" off duty");
    }

    @Override
    public String toString() {
        return "WaitPerson{" +
                "id=" + id +
                '}';
    }
}

class Chef implements Runnable{
    private static int counter = 0;
    private final int id = counter++;
    private Restaurant restaurant;
    private Random random = new Random (47);

    public Chef(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread ().isInterrupted ()){
                Order order = restaurant.orders.take ();
                Food requestItem = order.itme ();
                Plate plate = new Plate (order,requestItem);
                order.getWaitPerson ().filledOrders.put (plate);
                Thread.sleep (random.nextInt (500));
            }
        }catch (InterruptedException e){
            System.out.println (this + " off duty");
        }
    }

    @Override
    public String toString() {
        return "Chef " +
                    id +
                '}';
    }
}

class Restaurant implements Runnable{
    private List<WaitPerson> waitPersons = new ArrayList<WaitPerson> ();
    private List<Chef> chefs = new ArrayList<Chef> ();
    private ExecutorService executorService;
    public BlockingQueue<Order> orders = new LinkedBlockingQueue<Order> ();
    private Random random = new Random (47);

    public Restaurant(ExecutorService executorService, int nWaitPersons,int nChef) {
        this.executorService = executorService;
        for (int i = 0; i < nWaitPersons; i++){
            WaitPerson waitPerson = new WaitPerson (this);
            waitPersons.add (waitPerson);
            executorService.execute (waitPerson);
        }

        for (int i = 0; i < nChef; i++){
            Chef chef = new Chef (this);
            chefs.add (chef);
            executorService.execute (chef);
        }
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread ().isInterrupted ()) {
                WaitPerson wp = waitPersons.get (random.nextInt (waitPersons.size ()));
                Customer customer = new Customer (wp);
                executorService.execute (customer);
                Thread.sleep (100);
            }
        } catch (InterruptedException e) {
            System.out.println ("Restaurant interrupter");
        }
        System.out.println ("Restaurant closeing");
    }
}