package philosopher;

import api.BindingProxy;
import api.Manager;
import api.Philosopher;
import api.TablePart;
import manager.ManagerImpl;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Fabian on 01.06.2016.
 */
public class PhilosopherImpl implements Philosopher, Runnable {
    private static final Logger LOG = Logger.getLogger(ManagerImpl.class.getName());
    public static final int MEALS_BEFORE_SLEEP = 3;
    public static final int MEDITATION_TIME = 5;
    public static final int EAT_TIME = 1;
    public static final int SLEEP_TIME = 10;

    private final String id = UUID.randomUUID().toString();
    private final Manager manager;

    public boolean allowedToEat = true;
    private int eatCounter = 0;
    private int meditationTime = MEDITATION_TIME;

    public PhilosopherImpl(String ip) {
        super();
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            Philosopher stub = (Philosopher) UnicastRemoteObject.exportObject(this, 0);
            Registry registry = LocateRegistry.getRegistry(ip);
            ((BindingProxy) registry.lookup(BindingProxy.NAME)).proxyRebind(id, this);
            LOG.info(String.format("Philosopher %s bound to registry.", id));
            manager = (Manager) registry.lookup(Manager.NAME);
            manager.registerPhilosopher(id);
            LOG.info(String.format("Philosopher %s registered in manager.", id));
        } catch (Exception e) {
            LOG.severe(String.format("Problem binding Philosopher %s.", id));
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {

                // Meditate
                Thread.sleep(meditationTime);


                if (isAllowedToEat()) {

                    // Go to table
                    Map<TablePart, Integer> forkIndices;
                    TablePart tablePart = manager.getRandomTablePart();
                    LOG.info(String.format("Got TablePart %s", id, tablePart.getId()));
                    forkIndices = tablePart.takeSeat(id);
                    while (forkIndices.size() == 1) {
                        tablePart = forkIndices.keySet().iterator().next();
                        forkIndices = tablePart.takeSeat(id);
                    }
                    LOG.info("Took seat.");

                    // Eat
                    Thread.sleep(EAT_TIME);
                    setEatCounter(getEatCounter() + 1);

                    LOG.info("Finished Eating.");

                    // Leave table
                    forkIndices.forEach((tablePartt, forkIndex) -> {
                        try {
                            LOG.info(String.format("ForkIndex is %s on TablePart %s", forkIndex, tablePartt.getId()));
                            tablePartt.leaveSeat(forkIndex);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                            LOG.severe("Error in leaveSeat on TP");
                        }
                    });

                    if (eatCounter % MEALS_BEFORE_SLEEP == 0) {
                        //Sleep after eatCount meals
                        Thread.sleep(SLEEP_TIME);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.severe("An Error occured.");
        }
    }

    public String getId() {
        return id;
    }

    public int getEatCounter() {
        return eatCounter;
    }

    public synchronized void setEatCounter(int eatCounter) {
        this.eatCounter = eatCounter;
    }

    public boolean isAllowedToEat() {
        return allowedToEat;
    }

    public synchronized void setAllowedToEat(boolean allowedToEat) {
        this.allowedToEat = allowedToEat;
    }
}
