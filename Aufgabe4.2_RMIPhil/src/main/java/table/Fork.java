package table;

import philosopher.PhilosopherImpl;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Organization: HM FK07.
 * Project: VerteilteSystemePraktikum, table
 * Author(s): Rene Zarwel
 * Date: 08.06.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
public class Fork {

  //Max Block Time to prevent Dead Philosopher Blocking
  private static final long MAX_BLOCK_TIME = PhilosopherImpl.EAT_TIME * 5;

  private ReentrantLock locker = new ReentrantLock();
  private String uuid = "";
  private long blockStart;

  private boolean isFree(){

    long currentTime = System.currentTimeMillis();

    return uuid.length() == 0 || (currentTime - blockStart) > MAX_BLOCK_TIME;
  }

  public boolean tryBlock(String uuid){

    if(locker.tryLock() && isFree()) {

      this.uuid = uuid;
      blockStart = System.currentTimeMillis();

      locker.unlock();
      return true;

    } else {
      if(locker.isHeldByCurrentThread())
        locker.unlock();
      return false;
    }

  }
  public void unblock(){
    locker.lock();
    uuid = "";
    locker.unlock();
  }

}
