import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Fabian on 30.03.2016.
 */
public class Philosopher extends Thread {

    public static final int MEALS_BEFORE_SLEEP = 3;
    public static final int MEDITATION_TIME = 3000;
    public static final int EAT_TIME = 2000;
    public static final int SLEEP_TIME = 5000;

    private final String PREFIX;

    private int id;
    private ReentrantLock leftFork;
    private ReentrantLock rightFork;
    private ReentrantLock seat;
    private DiningTable diningTable;
    private int eatCounter = 0;

    public Philosopher(int id, DiningTable diningTable) {
        this.id = id;
        this.diningTable = diningTable;

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < id; i++)
            builder.append("\t");
        builder.append("P");
        builder.append(this.id);

        PREFIX = builder.toString();
    }

    @Override
    public void run() {
        while (true) {
            System.out.println(String.format("%s 🙏", PREFIX));
            try {
                // Meditate
                Thread.sleep(MEDITATION_TIME);
            } catch (InterruptedException e) {
                System.out.println(String.format("%s got interrupted while meditating.", PREFIX));
            }

            System.out.println(String.format("%s 🔎", PREFIX));
            // Go to Table
            diningTable.takeSeat(this);

            System.out.println(String.format("%s 🍜", PREFIX));
            try {
                // Eat
                Thread.sleep(EAT_TIME);
                eatCounter++;
            } catch (InterruptedException e) {
                System.out.println(String.format("%s got interrupted while eating.", PREFIX));
            }

            // Leave Forks and Seat
            leftFork.unlock();
            rightFork.unlock();
            seat.unlock();

            if (eatCounter == MEALS_BEFORE_SLEEP) {
                System.out.println(String.format("%s 🏨", PREFIX));
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    System.out.println(String.format("%s got interrupted while eating.", PREFIX));
                }
            }
        }
    }

    public boolean takeLeftFork(ReentrantLock leftFork) {
        if (leftFork.tryLock()) {
            this.leftFork = leftFork;
            return true;
        }
        return false;
    }

    public boolean takeRightFork(ReentrantLock rightFork) {
        if (rightFork.tryLock()) {
            this.rightFork = rightFork;
            return true;
        }
        return false;
    }

    public boolean takeSeat(ReentrantLock seat) {
        if (seat.tryLock()) {
            this.seat = seat;
            return true;
        }
        return false;
    }
}
