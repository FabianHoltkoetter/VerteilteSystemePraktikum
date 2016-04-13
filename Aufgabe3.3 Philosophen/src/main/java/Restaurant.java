import java.util.ArrayList;
import java.util.List;

/**
 * Organization: HM FK07.
 * Project: VerteilteSystemePraktikum, PACKAGE_NAME
 * Author(s): Rene Zarwel + Fabian Holtkötter
 * Date: 06.04.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
public class Restaurant {
    private static final String ERROR_PARAMETERS = "Please specify the numbers of how many philosophers and seats you want to start.";

    public static final int RUN_TIME = 10 * 1000;

    public static void main(String[] args){
        if (args.length != 3) {
            throw new IllegalArgumentException(ERROR_PARAMETERS);
        }

        //Get phil. and seat count
        int philosophersCount;
        int hungryPhilosophersCount;
        int seatCount;
        try {
            philosophersCount = Integer.parseInt(args[0]);
            hungryPhilosophersCount = Integer.parseInt(args[1]);
            seatCount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ERROR_PARAMETERS);
        }

        //Init all
        DiningTable table = new DiningTable(seatCount);
        List<Philosopher> philosophers = new ArrayList<>();
        TableMaster master = new TableMaster();
        View view = new View();
        view.start();

        //Hold threads
        List<Thread> threadList = new ArrayList<>();
        threadList.add(master);
        threadList.add(view);

        for(int i = 0; i < philosophersCount; i++) {
            Philosopher philosopher = new Philosopher(i, table, i < hungryPhilosophersCount);
            philosopher.addObserver(view);

            master.addPhilosopher(philosopher);
            philosophers.add(philosopher);

            Thread pThread = new Thread(philosopher);
            threadList.add(pThread);
            pThread.start();
        }

        master.start();

        try {
            Thread.sleep(RUN_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        threadList.forEach(Thread::interrupt);

        philosophers.forEach(System.out::println);

    }

}
