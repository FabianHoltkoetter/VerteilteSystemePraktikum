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
            for (int i = 0; i < seats.length && !tookSeat; i++) {
                synchronized (this) {
                    if (seats[i].tryLock()) {
                        int left = i;
                        int right = (i + 1) % forks.length;
                        if (!forks[left].isLocked() && !forks[right].isLocked()) {
                            forks[left].lock();
                            forks[right].lock();
                            philosopher.setSeatNumber(i);
                            System.out.println(String.format("%s took seat %d with forks %d and %d",
                                    philosopher.PREFIX, i, i, (i + 1) % forks.length));
                            tookSeat = true;
                        } else {
                            seats[i].unlock();
                        }
                    }
                }
            }
        }
    }

    public synchronized void leaveSeat(int seatNumber) {
        seats[seatNumber].unlock();
        forks[seatNumber].unlock();
        forks[(seatNumber + 1) % forks.length].unlock();
    }
}
