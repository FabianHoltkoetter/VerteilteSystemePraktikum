package table;

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

  private ReentrantLock locker = new ReentrantLock();
  private String uuid = "";

  private boolean isFree(){
    return uuid.length() == 0;
  }

  public boolean tryBlock(String uuid){

    if(locker.tryLock() && isFree()) {

      this.uuid = uuid;

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
