import java.util.Random;
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
public class DiningTable {

  private final ReentrantLock[] forks;
  private Random rGenerator = new Random();

  public DiningTable(int seats) {

    this.forks = new ReentrantLock[(seats != 1) ? seats : 2];

    for (int i = 0; i < forks.length; i++) {
      this.forks[i] = new ReentrantLock();
    }
  }

  public void takeSeat(Philosopher philosopher) throws InterruptedException {

    while (!Thread.currentThread().isInterrupted()) {

      //Philosopher runs around the table till he got a seat + forks
      for (int i = 0; i < forks.length; i++) {

        //Set index of left and right fork
        int left = i;
        int right = (i + 1) % forks.length;
        boolean rand = rGenerator.nextBoolean();

        //Try to get the forks
        if (((rand) ? forks[left].tryLock() : forks[right].tryLock()) &&
            ((!rand) ? forks[left].tryLock() : forks[right].tryLock())) {

          philosopher.takeAll(left, right);

          return;

        } else {
          //Drop forks if held
          if (forks[left].isHeldByCurrentThread())
            forks[left].unlock();

          if (forks[right].isHeldByCurrentThread())
            forks[right].unlock();
        }

      }

      //Wait till a seat gets free
      synchronized (forks){
          forks.wait();
      }
    }
  }

  public void leaveSeat(int seatNumber) {
    //Release forks
    forks[seatNumber].unlock();
    forks[(seatNumber + 1) % forks.length].unlock();

    //Notify Waiting philosophers
    synchronized (forks) {
      forks.notify();
    }


  }
}
