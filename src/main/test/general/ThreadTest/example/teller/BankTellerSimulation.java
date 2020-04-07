package general.ThreadTest.example.teller;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class BankTellerSimulation{
    static final int MAX_LINE_SIZE = 50;
    static final int ADJUSTTMENT_PERIOD = 1000;
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool ();
        CoustemerLine coustomers = new CoustemerLine (MAX_LINE_SIZE);
        executorService.execute (new CusttomerGenrator (coustomers));
        executorService.execute (new TellerManager (executorService, coustomers, ADJUSTTMENT_PERIOD));

        Thread.sleep (10000);
        executorService.shutdownNow ();
    }
}


class Coustomer {
    private final int serversTime;

    public Coustomer(int serversTime) {
        this.serversTime = serversTime;
    }

    public int getServersTime() {
        return serversTime;
    }

    @Override
    public String toString() {
        return "[" +
                serversTime +
                ']';
    }
}

class CoustemerLine extends ArrayBlockingQueue<Coustomer> {

    public CoustemerLine(int maxLineSize) {
        super (maxLineSize);
    }

    public String toString() {

        if (this.size () == 0){
            return "null";
        }
        StringBuffer stringBuffer = new StringBuffer ();
        for (Coustomer coustomer : this)
            stringBuffer.append (coustomer);
        return stringBuffer.toString ();
    }
}

class CusttomerGenrator implements Runnable
{
    CoustemerLine coustomers;
    private static Random random = new Random (47);

    public CusttomerGenrator(CoustemerLine coustomers) {
        this.coustomers = coustomers;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread ().isInterrupted ()){

                coustomers.put (new Coustomer (random.nextInt (1000)));
                Thread.sleep (random.nextInt (300));
            }
        } catch (InterruptedException e) {
            System.out.println ("CurtomerGenerator Interrupted");
        }
        System.out.println ("CurtomerGenerator terminating");
    }
}

class Teller implements Runnable,Comparable<Teller>{

    private static int counter = 0;
    private final int id = counter++;
    private int customerServved = 0;
    private CoustemerLine coustomers ;
    private boolean servingCustomerLine = true;

    public Teller(CoustemerLine coustomers){
        this.coustomers = coustomers;
    }

    public synchronized void doSomethingEles(){
        customerServved = 0;
        servingCustomerLine = false;
    }

    public synchronized void servingCustomerLine(){
        assert !servingCustomerLine:"already serving " + this;
        servingCustomerLine = true;
        notifyAll ();
    }

    public String shortString(){
        return "T " + id;
    }
    @Override
    public int compareTo(Teller o) {
        return customerServved < o.customerServved ? -1 : (customerServved == o.customerServved ? 0 : 1);
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread ().isInterrupted ()){
                Coustomer coustomer =coustomers.take ();
                Thread.sleep (coustomer.getServersTime ());
                synchronized (this){
                    customerServved++;
                    while (!servingCustomerLine)
                        wait ();
                }
            }
        } catch (InterruptedException e) {
            System.out.println (this + " Interrupted");
        }
        System.out.println (this + " terminating");
    }

    @Override
    public String toString() {
        return "Teller " + id + " ";
    }
}

class TellerManager implements Runnable{
    private ExecutorService executorService;
    private CoustemerLine coustomers;
    private PriorityQueue<Teller> workingTelles = new PriorityQueue<Teller> ();
    private Queue<Teller> tellersDoinOtherThings = new LinkedList<Teller> ();
    private int adjustmentPeriod;
    private static Random random = new Random (47);

    public TellerManager(ExecutorService executorService,CoustemerLine coustomers,int adjustmentPeriod) {
        this.executorService = executorService;
        this.adjustmentPeriod = adjustmentPeriod;
        this.coustomers = coustomers;
        Teller teller = new Teller (coustomers);
        executorService.execute (teller);
        workingTelles.add (teller);
    }

    public void adjustTellerNumber(){
        if(coustomers.size () / workingTelles.size () > 2){
            if(tellersDoinOtherThings.size () > 0){
                Teller teller = tellersDoinOtherThings.remove ();
                teller.servingCustomerLine ();
                workingTelles.offer (teller);
                return;
            }
            Teller teller = new Teller (coustomers);
            executorService.execute (teller);
            workingTelles.add (teller);
            return;
        }

        if(workingTelles.size () > 1 && coustomers.size () / workingTelles.size () < 2)
            reassignOneTeller ();
        if(coustomers.size () == 0)
            while (workingTelles.size () > 0)
                reassignOneTeller ();

    }

    public void reassignOneTeller(){
        Teller teller = workingTelles.poll ();
        teller.doSomethingEles ();
        tellersDoinOtherThings.offer (teller);
    }

    @Override
    public void run() {
        try {
             while (!Thread.currentThread ().isInterrupted ()){
                Thread.sleep (adjustmentPeriod);
                adjustTellerNumber ();
                System.out.println (coustomers + "{");
                for (Teller teller : workingTelles)
                    System.out.println ("\t" + teller.toString ());
                System.out.println ("}");
             }
        } catch (InterruptedException e) {
            System.out.println (this + " Interrupted");
        }
        System.out.println (this + " terminating");
    }

    @Override
    public String toString() {
        return "TellerManager";
    }
}
