import java.util.ArrayList;
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
                if (philosopher.takeSeat(seats[i])) {
                    if (philosopher.takeLeftFork(forks[i])) {
                        if (philosopher.takeRightFork(forks[(i + 1) % seats.length])) {
                            tookSeat = true;
                        } else {
                            forks[i].unlock();
                            seats[i].unlock();
                        }
                    } else {
                        seats[i].unlock();
                    }
                }
            }
        }

    }

    public void leaveSeat(Philosopher philosopher) {
        for (int i = 0; i < seats.length; i++) {
            if (seats[i].equals(philosopher)) {
                seats[i] = null;
            }
        }
        notifyAll();
    }
}
