import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Fabian on 30.03.2016.
 */
public class Philosopher extends Thread {

    public static final int MEDITATION_TIME = 1000;
    public static final int EAT_TIME = 1000;
    public static final int SLEEP_TIME = 1000;

    private int id;
    private ReentrantLock leftFork;
    private ReentrantLock rightFork;
    private ReentrantLock seat;
    private DiningTable diningTable;
    private int eatCounter = 0;

    public Philosopher(int id, DiningTable diningTable) {
        this.id = id;
        this.diningTable = diningTable;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Meditate
                Thread.sleep(MEDITATION_TIME);
            } catch (InterruptedException e) {
                System.out.println(String.format("Philosopher %d got interrupted while meditating.", id));
            }
            System.out.println(String.format("Philospher %d finished meditating.", id));

            // Go to Table
            diningTable.takeSeat(this);
            System.out.println(String.format("Philospher %d took a seat.", id));

            try {
                // Eat
                Thread.sleep(EAT_TIME);
                eatCounter++;
            } catch (InterruptedException e) {
                System.out.println(String.format("Philosopher %d got interrupted while eating.", id));
            }
            System.out.println(String.format("Philospher %d finished eating", id));

            // Leave Forks and Seat
            leftFork.unlock();
            rightFork.unlock();
            seat.unlock();
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
