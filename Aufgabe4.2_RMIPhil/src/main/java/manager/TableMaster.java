package manager;

import api.Manager;
import api.Philosopher;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Fabian on 08.06.2016.
 */
public class TableMaster extends Thread {
    private static final Logger LOG = Logger.getLogger(TableMaster.class.getName());
    private static final int PAUSE = 50;

    private int avgEatCout;
    private final Manager manager;

    public TableMaster() {
        try {
            Registry registry = LocateRegistry.getRegistry();
            manager = (Manager) registry.lookup(Manager.NAME);
            LOG.info("TableMaster received Manager.");
        } catch (Exception e) {
            LOG.severe("Error in TableMaster.");
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void run() {
        LOG.log(Level.INFO, "TableMaster started!");
        while (!isInterrupted()) {
            try {
                //Calculate average eat count
                List<Philosopher> philosophers = manager.getPhilosophers();
                LOG.info(String.format("Manager received %d philosophers.", philosophers.size()));
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

                Thread.sleep(PAUSE);
            } catch (Exception e) {
                e.printStackTrace();
                LOG.severe("Error in TableMaster!");
                break;
            }
        }
    }

}
