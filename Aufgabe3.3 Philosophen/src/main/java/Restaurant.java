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

    public static final int RUN_TIME = 60 * 1000;

    public static void main(String[] args) {
        if (args.length != 3 && args.length != 4) {
            throw new IllegalArgumentException(ERROR_PARAMETERS);
        }

        //Get phil. and seat count
        int philosophersCount;
        int hungryPhilosophersCount;
        int seatCount;
        boolean viewActive = false;
        try {
            philosophersCount = Integer.parseInt(args[0]);
            hungryPhilosophersCount = Integer.parseInt(args[1]);
            seatCount = Integer.parseInt(args[2]);
            if (args.length == 4) {
                viewActive = Boolean.parseBoolean(args[3]);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ERROR_PARAMETERS);
        }

        //Init all
        DiningTable table = new DiningTable(seatCount);
        List<Philosopher> philosophers = new ArrayList<>();
        TableMaster master = new TableMaster();
        View view = new View();
        if (viewActive)
            view.start();

        //Hold threads
        List<Thread> threadList = new ArrayList<>();
        threadList.add(master);
        if (viewActive)
            threadList.add(view);

        for (int i = 0; i < philosophersCount; i++) {
            Philosopher philosopher = new Philosopher(i, table, i < hungryPhilosophersCount);
            if (viewActive)
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

        resultsToMarkdown(philosophers);
        System.exit(0);
    }

    private static void resultsToMarkdown(List<Philosopher> philosophers) {
        int ROWS_PER_COLUMN = 10;
        StringBuilder builder = new StringBuilder();
        int rows = philosophers.size() < ROWS_PER_COLUMN ? philosophers.size() : ROWS_PER_COLUMN;
        int columns = (philosophers.size() + ROWS_PER_COLUMN - 1) / ROWS_PER_COLUMN;
        // Build header-line
        for (int i = 0; i < columns; i++) {
            builder.append("| Philosoph | Essvorgänge ");
        }
        builder.append("\n");
        // Build seperator-line
        for (int i = 0; i < columns; i++) {
            builder.append("| --- | --- ");
        }
        builder.append("\n");
        // Build data-lines
        int totalMealCount = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                int philosopherIndex = i + rows * j;
                if (philosopherIndex < philosophers.size()) {
                    builder.append("|\tP")
                            .append(philosopherIndex)
                            .append("\t|\t")
                            .append(philosophers.get(philosopherIndex).getEatCounter());
                    totalMealCount += philosophers.get(philosopherIndex).getEatCounter();
                }
            }
            builder.append("\n");
        }
        // Build total data
        builder.append(String.format("Gesamtzahl: %d", totalMealCount))
                .append("\n")
                .append(String.format("Durchschnitt: %d", totalMealCount/philosophers.size()));

        System.out.println(builder.toString());
    }

    private static void resultsToConsole(List<Philosopher> philosophers) {
        long mealCount = philosophers.stream()
                .peek(System.out::println)
                .mapToInt(Philosopher::getEatCounter)
                .sum();

        System.out.println("Gesamtzahl: " + mealCount);
        System.out.println("Durchschnitt: " + mealCount / philosophers.size());
    }
}
