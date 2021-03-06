package philosopher;

import api.BindingProxy;
import api.Manager;
import api.Philosopher;
import api.SimpleRMIClientSocketFactory;
import api.SimpleRMIServerSocketFactory;
import api.TablePart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Fabian on 01.06.2016.
 */
public class PhilosopherImpl implements Philosopher, Runnable, Serializable {
    private static final Logger LOG = LogManager.getLogger(PhilosopherImpl.class.getName());
    public static final int MEALS_BEFORE_SLEEP = 3;
    public static final int MEDITATION_TIME = 5;
    public static final int EAT_TIME = 1;
    public static final int SLEEP_TIME = 10;

    private final String id = UUID.randomUUID().toString();
    private final Manager manager;

    private boolean alive = true;

    private boolean allowedToEat = true;
    private boolean hungry = false;
    private int eatCounter = 0;
    private int meditationTime = MEDITATION_TIME;

    private TablePart firstTable;

    public PhilosopherImpl(String ip, String ownIP, Integer eatCounter, boolean hungry) {
        this(ip,ownIP, hungry);
        this.eatCounter = eatCounter;
    }

    public PhilosopherImpl(String managerIP, String ownIP, boolean hungry) {
        super();
        this.hungry = hungry;

        if (this.hungry) {
            meditationTime /= 3;
        }

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            LOG.debug("Started Philosopher with ID " + id);

            SimpleRMIClientSocketFactory csf = new SimpleRMIClientSocketFactory(ownIP);
            SimpleRMIServerSocketFactory ssf = new SimpleRMIServerSocketFactory();

            Philosopher stub = (Philosopher) UnicastRemoteObject.exportObject(this, 0, csf, ssf);
            Registry registry = LocateRegistry.getRegistry(managerIP);
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
        while (alive) {

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
                    } catch (Exception e) {
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
                        } catch (Exception e) {
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
                    } catch (Exception e) {
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
                validTable = firstTable != null;
                LOG.debug(validTable ? "Got new table" : "Retrying in 5 second.");
            } catch (Exception e) {
                LOG.error("Error retrieving first table. Retrying in 5 second.");
                validTable = false;
            }
            if (!validTable) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e1) {
                    LOG.error("Got interrupted while sleeping.");
                }
            }
        }

        return firstTable;
    }

    @Override
    public void replaceStoppedTable(String id, TablePart tablePart) throws RemoteException {
        if (getFirstTable().getId().equals(id)) {
            firstTable = tablePart;
        }
    }

    public boolean isHungry() {
        return hungry;
    }

    @Override
    public void stop() throws RemoteException {
        alive = false;
    }

    @Override
    public void start() throws RemoteException {
        alive = true;
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
        if(allowedToEat)
            LOG.debug("Puhh...can eat again.");
        else
            LOG.debug("GOT STOPPED with eatCount: " + eatCounter);
    }
}
