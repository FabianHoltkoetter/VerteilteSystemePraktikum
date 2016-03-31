import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Fabian on 30.03.2016.
 */
public class DiningTable {

    private ReentrantLock[] seats;
    private ReentrantLock[] forks;

    public DiningTable(int seats) {
        this.seats = new ReentrantLock[seats];
        this.forks = new ReentrantLock[seats];

        for (int i = 0; i < seats; i++) {
            this.seats[i] = new ReentrantLock();
            forks[i] = new ReentrantLock();
        }
    }

    public void takeSeat(Philosopher philosopher) {
        boolean tookSeat = false;
        while (!tookSeat) {
            for (int i = 0; i < seats.length; i++) {
                synchronized (this) {
                    if (seats[i].tryLock()) {
                        if (!forks[i].isLocked() && !forks[(i + 1) % forks.length].isLocked()) {
                            philosopher.setSeat(seats[i]);
                            philosopher.setLeftFork(forks[i]);
                            philosopher.setRightFork(forks[(i + 1) % forks.length]);
                            tookSeat = true;
                        } else {
                            seats[i].unlock();
                        }
                    }
                }
            }
        }

    }
}
