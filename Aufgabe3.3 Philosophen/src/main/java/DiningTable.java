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
            this.forks[i] = new ReentrantLock();
        }
    }

    public void takeSeat(Philosopher philosopher) {
        boolean tookSeat = false;
        while (!tookSeat) {
            for (int i = 0; i < seats.length && !tookSeat; i++) {
                if (seats[i].tryLock()) {
                    int left = i;
                    int right = (i + 1) % forks.length;
                    if (forks[left].tryLock() && forks[right].tryLock()) {
                        philosopher.setSeatNumber(i);
                        System.out.println(String.format("%s platz %d",
                                philosopher.PREFIX, i));
                        tookSeat = true;
                    } else {
                        seats[i].unlock();

                        if(forks[left].isHeldByCurrentThread())
                            forks[left].unlock();
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
