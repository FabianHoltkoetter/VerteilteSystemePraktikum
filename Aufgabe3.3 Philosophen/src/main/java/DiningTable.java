import java.util.concurrent.locks.ReentrantLock;

/**
 * Organization: HM FK07.
 * Project: VerteilteSystemePraktikum, PACKAGE_NAME
 * Author(s): Rene Zarwel + Fabian Holtkötter
 * Date: 06.04.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
public class DiningTable{

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

        //Philosopher runs around the table till he got a seat + forks
        for (int i = 0; ; i = ( i + 1 ) % seats.length) {

            //Sit down and try to get the left and right fork
            if (seats[i].tryLock()) {

                //Set index of left and right fork
                int left = i;
                int right = (i + 1) % forks.length;

                //Try to get the forks
                if (forks[left].tryLock() && forks[right].tryLock()) {

                    philosopher.takeAll(i, left, right);

                    return;

                } else {
                    //Drop fork if held
                    if(forks[left].isHeldByCurrentThread())
                        forks[left].unlock();

                    //Stand up
                    seats[i].unlock();
                }
            }
        }

    }

    public void leaveSeat(int seatNumber) {
        //Release forks
        forks[seatNumber].unlock();
        forks[(seatNumber + 1) % forks.length].unlock();
        //Stand up
        seats[seatNumber].unlock();
    }
}
