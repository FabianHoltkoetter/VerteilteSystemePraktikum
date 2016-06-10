package manager;

import api.Manager;
import api.Philosopher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

/**
 * Created by Fabian on 08.06.2016.
 */
public class TableMaster extends Thread {
  private static final Logger LOG = LogManager.getLogger(TableMaster.class.getName());
  private static final int PAUSE = 5000;

  private int avgEatCout;
  private final Manager manager;

  public TableMaster() {
    try {
      Registry registry = LocateRegistry.getRegistry();
      manager = (Manager) registry.lookup(Manager.NAME);
      LOG.info("TableMaster received Manager.");
    } catch (Exception e) {
      LOG.error("Error in TableMaster.");
      throw new RuntimeException(e.getMessage());
    }
  }

  @Override
  public void run() {
    LOG.info("TableMaster started!");
    while (!isInterrupted()) {
      try {
        Thread.sleep(PAUSE);

        //Calculate average eat count
        List<Philosopher> philosophers = manager.getPhilosophers();

        LOG.info(String.format("TableMaster woke up and received %d philosophers.", philosophers.size()));

        if(philosophers.size() == 0)
          continue;

        philosophers.forEach(philosopher -> {
              try {
                avgEatCout += philosopher.getEatCounter();
              } catch (RemoteException e) {
                e.printStackTrace();
              }
            }
        );

        avgEatCout /= philosophers.size();
        LOG.info(String.format("Average eat count is %d", avgEatCout));

        //Allow or forbid philosopher to eat
        philosophers.forEach(p -> {
          try {
            p.setAllowedToEat(p.getEatCounter() < avgEatCout * 1.1);
          } catch (RemoteException e) {
            e.printStackTrace();
          }
        });

        avgEatCout = 0;

      } catch (Exception e) {
        e.printStackTrace();
        LOG.error("Error in TableMaster!");
      }
    }
  }

}
