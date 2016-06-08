package philosopher;

import api.Manager;
import api.Philosopher;
import api.TablePart;
import manager.ManagerImpl;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Created by Fabian on 01.06.2016.
 */
public class PhilosopherImpl implements Philosopher, Runnable {
    private static final Logger LOG = Logger.getLogger(ManagerImpl.class.getName());
    public static final int MEALS_BEFORE_SLEEP = 3;
    public static final int MEDITATION_TIME = 5;
    public static final int EAT_TIME = 1;
    public static final int SLEEP_TIME = 10;

    private final String ID = UUID.randomUUID().toString();
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
            registry.rebind(ID, stub);
            LOG.log(Level.INFO, String.format("Philosopher %s bound to registry.", ID));
            manager = (Manager) registry.lookup(Manager.NAME);
            manager.registerPhilosopher(ID);
            LOG.log(Level.INFO, String.format("Philosopher %s registered in manager.", ID));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, String.format("Problem binding Philosopher %s.", ID));
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
                    TablePart randomTablePart = manager.getRandomTablePart();
                    LOG.log(Level.INFO, String.format("Philosopher %s got TablePart %s", ID, randomTablePart.getID()));
                    forkIndices = randomTablePart.takeSeat(ID);
                    while (forkIndices == null) {
                        randomTablePart = manager.getNextTablePart(randomTablePart.getID());
                        forkIndices = randomTablePart.takeSeat(ID);
                    }
                    LOG.log(Level.INFO, String.format("Philosopher %s took seat.", ID));

                    // Eat
                    Thread.sleep(EAT_TIME);
                    setEatCounter(getEatCounter() + 1);

                    // Leave table
                    forkIndices.forEach((tablePart, forkIndex) -> tablePart.leaveSeat(forkIndex, ID));

                    if (eatCounter % MEALS_BEFORE_SLEEP == 0) {
                        //Sleep after eatCount meals
                        Thread.sleep(SLEEP_TIME);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.log(Level.SEVERE, String.format("Error in run of Philosopher %s", ID));
        }
    }

    public String getID() {
        return ID;
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
