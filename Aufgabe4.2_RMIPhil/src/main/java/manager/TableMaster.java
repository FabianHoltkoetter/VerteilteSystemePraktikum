package manager;

import api.Manager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Fabian on 08.06.2016.
 */
public class TableMaster extends Thread {
  private static final Logger LOG = LogManager.getLogger(TableMaster.class.getName());
  private static final int PAUSE = 5000;

  private final List<String> stoppedPhils = new ArrayList<>();

  private double avgEatCout;
  private final Manager manager;

  public TableMaster(String ip) {
    try {
      Registry registry = LocateRegistry.getRegistry(ip);
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
        Map<String, Integer> philosophers = manager.getPhilosophersEatCount();

        LOG.info(String.format("TableMaster woke up and received %d philosophers.", philosophers.size()));

        if(philosophers.size() == 0)
          continue;

        avgEatCout = philosophers.values().stream().mapToInt(i->i).average().orElse(0.0);

        LOG.info(String.format("Average eat count is %f", avgEatCout));

        //Allow or forbid philosopher to eat
        philosophers.entrySet().forEach(p -> {
          try {
            if(Double.compare(avgEatCout * 1.1, p.getValue()) == -1 ){
              if(!stoppedPhils.contains(p.getKey())) {
                manager.getPhilosopher(p.getKey()).setAllowedToEat(false);
                stoppedPhils.add(p.getKey());
                LOG.debug("Stopped phil " + p.getKey() + " with: " + p.getValue());
              }
            } else if(stoppedPhils.contains(p.getKey())){
              manager.getPhilosopher(p.getKey()).setAllowedToEat(true);
              stoppedPhils.remove(p.getKey());
            }
          } catch (Exception e) {
            LOG.debug("Error on Connection");
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
