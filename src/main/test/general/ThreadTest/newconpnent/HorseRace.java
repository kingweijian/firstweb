package general.ThreadTest.newconpnent;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ClassName HorseRace
 * Author weijian
 * DateTime 2020/3/2 11:53 PM
 * Version 1.0
 */
public class HorseRace {
    private static final int FINISH_LINE = 75;
    Logger logger = Logger.getLogger (HorseRace.class);
    private List<Horse> horses = new ArrayList<Horse> ();
    private ExecutorService executorService = Executors.newCachedThreadPool ();

    private CyclicBarrier cyclicBarrier;

    public HorseRace(int nhorses, final int pause) {

        init (nhorses,pause);

        for (int i = 0; i < nhorses; i++){
            Horse horse = new Horse (cyclicBarrier);
            horses.add (horse);
            executorService.execute (horse);
        }
    }

    public void init (int nhorses, final int pause){
        cyclicBarrier = new CyclicBarrier (nhorses, new Runnable () {
            @Override
            public void run() {
                StringBuffer stringBuffer = new StringBuffer ();
                for (int i = 0; i <FINISH_LINE;i++){
                    stringBuffer.append ("=");
                }
                logger.info (stringBuffer.toString ());

                for (Horse horse : horses)
                    logger.info (horse.tracks ());

                for (Horse horse : horses){
                    if(horse.getStrids () >= FINISH_LINE){
                        executorService.shutdownNow ();
                        logger.info ("WIN " + horse);
                        return;
                    }
                }

                try {
                    Thread.sleep (pause);
                } catch (InterruptedException e) {
                    e.printStackTrace ();
                }

            }

        });

    }
    public static void main(String[] args){


        new HorseRace (7,200);
    }
}
