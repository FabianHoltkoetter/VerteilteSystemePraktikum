import java.util.ArrayList;
import java.util.List;

/**
 * Organization: HM FK07.
 * Project: VerteilteSystemePraktikum, PACKAGE_NAME
 * Author(s): Rene Zarwel
 * Date: 08.04.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
public class TableMaster extends Thread {

    private static final int PAUSE = 50;

    private List<Philosopher> childs = new ArrayList<>();
    private int avgEatCout;

    @Override
    public void run() {
        while (!isInterrupted()){
            //Calculate average eat count
            childs.forEach(philosopher ->
                avgEatCout += philosopher.getEatCounter()
            );
            avgEatCout /= childs.size();

            //Allow or forbid philosopher to eat
            childs.forEach(p -> p.setAllowedToEat(p.getEatCounter() < avgEatCout * 1.1));

            avgEatCout = 0;

            try {
                Thread.sleep(PAUSE);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void addPhilosopher(Philosopher philosopher){
        childs.add(philosopher);
    }
}
