package philosopher;

import api.BindingProxy;
import api.Manager;
import api.Philosopher;
import api.TablePart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Fabian on 01.06.2016.
 */
public class PhilosopherImpl implements Philosopher, Runnable {
    private static final Logger LOG = LogManager.getLogger(PhilosopherImpl.class.getName());
    public static final int MEALS_BEFORE_SLEEP = 3;
    public static final int MEDITATION_TIME = 5;
    public static final int EAT_TIME = 1;
    public static final int SLEEP_TIME = 10;

    private final String id = UUID.randomUUID().toString();
    private final Manager manager;

    private boolean allowedToEat = true;
    private boolean hungry = false;
    private int eatCounter = 0;
    private int meditationTime = MEDITATION_TIME;

    private TablePart firstTable;

    public PhilosopherImpl(String ip, Integer eatCounter, boolean hungry) {
        this(ip, hungry);
        this.eatCounter = eatCounter;
    }

    public PhilosopherImpl(String ip, boolean hungry) {
        super();
        this.hungry = hungry;

        if (this.hungry) {
            meditationTime /= 3;
        }

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            Philosopher stub = (Philosopher) UnicastRemoteObject.exportObject(this, 0);
            Registry registry = LocateRegistry.getRegistry(ip);
            ((BindingProxy) registry.lookup(BindingProxy.NAME)).proxyRebind(id, this);
            LOG.info(String.format("Philosopher %s bound to registry.", id));
            manager = (Manager) registry.lookup(Manager.NAME);
            manager.registerPhilosopher(id, this.hungry);
            LOG.info(String.format("Philosopher %s registered in manager.", id));
        } catch (Exception e) {
            LOG.error(String.format("Problem binding Philosopher %s.", id));
            throw new RuntimeException(e.getMessage());
        }

        firstTable = getFirstTable();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {

            // Meditate
            try {
                Thread.sleep(meditationTime);
            } catch (InterruptedException e) {
                LOG.error("Philosopher got interrupted while sleeping.");
            }

            if (isAllowedToEat()) {
                // Go to table
                Map<Integer, TablePart> forkIndices = new LinkedHashMap<>();
                TablePart currentTablePart = getFirstTable();

                while (forkIndices.keySet().size() == 0) {
                    try {
                        LOG.debug(String.format("Got TablePart %s", id, currentTablePart.getId()));
                        forkIndices = currentTablePart.takeSeat(id);
                    } catch (RemoteException e) {
                        LOG.error("Couldn't reach first TablePart. Retreiving new first Table.");
                        currentTablePart = getFirstTable();
                    }
                }

                while (forkIndices.size() != 2) {
                    currentTablePart = forkIndices.get(TablePart.NEXT_TABLEPART);
                    boolean validRequest = false;
                    while(!validRequest) {
                        try {
                            forkIndices = currentTablePart.takeSeat(id);
                            validRequest = true;
                        } catch (RemoteException e) {
                            LOG.error("Error trying to take seat on table. Retrying from first Table.");
                            currentTablePart = getFirstTable();
                            validRequest = false;
                        }
                    }
                }

                LOG.debug("Took seat.");

                // Eat
                try {
                    Thread.sleep(EAT_TIME);
                } catch (InterruptedException e) {
                    LOG.error("Philosopher got interrupted during eating.");
                }
                setEatCounter(getEatCounter() + 1);

                LOG.debug("Finished Eating.");

                // Leave table
                forkIndices.forEach((forkIndex, tablePart) -> {
                    try {
                        LOG.debug(String.format("ForkIndex is %s on TablePart %s", forkIndex, tablePart.getId()));
                        tablePart.leaveSeat(forkIndex);
                        firstTable = tablePart;
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        LOG.error("Error in leaveSeat on TP");
                    }
                });

                if (eatCounter % MEALS_BEFORE_SLEEP == 0) {
                    //Sleep after eatCount meals
                    try {
                        Thread.sleep(SLEEP_TIME);
                    } catch (InterruptedException e) {
                        LOG.error("Philosopher got interrupted during meditation.");
                    }
                }
            }
        }
    }

    private TablePart getFirstTable() {
        boolean validTable = firstTable != null;
        try {
            if (validTable) {
                firstTable.getId();
            }
        } catch (RemoteException rme) {
            validTable = false;
        }

        while (!validTable) {
            try {
                firstTable = manager.getRandomTablePart();
                validTable = true;
            } catch (RemoteException e) {
                LOG.error("Error retrieving first table. Retrying in 1 second.");
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e1) {
                    LOG.error("Philosopher got interrupted while waiting.");
                }
            }
        }

        return firstTable;
    }

    public boolean isHungry() {
        return hungry;
    }

    @Override
    public void stop() throws RemoteException {
        Thread.currentThread().interrupt();
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
