package manager;

import api.Manager;
import api.Philosopher;
import api.Recovery;
import api.TablePart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Created by Fabian on 01.06.2016.
 */
public class ManagerImpl implements Manager, Runnable {
    private static final Logger LOG = LogManager.getLogger(ManagerImpl.class.getName());
    public static final int CHECK_INTERVAL = 1_000;
    private final Registry registry;
    private final Map<String, Map.Entry<Integer, Boolean>> philosopherIds = new LinkedHashMap<>();
    private final ArrayList<String> tableIds = new ArrayList<>();
    private final ArrayList<String> recoveryIds = new ArrayList<>();
    private ThreadLocalRandom randomGenerator = ThreadLocalRandom.current();

    public ManagerImpl(Registry registry) {
        super();
        this.registry = registry;
    }

    @Override
    public void registerRecovery(String vmid) {
        synchronized (recoveryIds) {
            LOG.info(String.format("Adding Recovery %s", vmid));
            if(!recoveryIds.contains(vmid))
              recoveryIds.add(vmid);
        }
    }

    @Override
    public void unregisterRecovery(String vmid) {
        synchronized (recoveryIds) {
            LOG.info(String.format("Removing Recovery %s", vmid));
            recoveryIds.remove(vmid);
          try {
            registry.unbind(vmid);
          } catch (Exception e) {
            LOG.error("Error on unbind Recovery.");
          }
        }
    }

    @Override
    public void registerTablepart(String uid) {
        synchronized (tableIds) {
            LOG.info(String.format("Adding TablePart %s", uid));

            try {
                //Find new Table
                TablePart newTablePart = (TablePart) registry.lookup(uid);
                tableIds.add(uid);

                //Get last TP and set the new TP as next
                String lastId = null;
                TablePart last;
                boolean set = false;
                while (!set) {
                    try {
                        lastId = tableIds.get((tableIds.size() - 1) % tableIds.size());
                        last = (TablePart) registry.lookup(lastId);

                        last.setNextTablePart(newTablePart);
                        set = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        LOG.error("Found dead TablePart");

                        unregisterTablepart(lastId);

                        if(tableIds.size() == 0)
                          throw new RuntimeException("Cant connect to any tablepart.");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                LOG.error("Error while setting next TablePart");
            }
        }
    }

    @Override
    public void unregisterTablepart(String uid) {
        synchronized (tableIds) {
            LOG.info(String.format("Remove TablePart %s", uid));

            //Stop if not in the list
            if (!tableIds.contains(uid))
                return;

            //Get table id of table before the one to be removed
            int indexBefore = (tableIds.indexOf(uid) - 1) % tableIds.size();
            String tmpID = tableIds.get((indexBefore == -1) ? tableIds.size() - 1 : indexBefore);

            try {
                //Remove Table
                tableIds.remove(uid);
                registry.unbind(uid);

                //Stop if there are no more TPs
                if (tableIds.size() == 0)
                    return;

                //Reset next TP on TP before
                TablePart before = (TablePart) registry.lookup(tmpID);
                tmpID = tableIds.get((tableIds.indexOf(tmpID) + 1) % tableIds.size());
                TablePart next = (TablePart) registry.lookup(tmpID);
                before.setNextTablePart(next);

            } catch (Exception e1) {
                e1.printStackTrace();
                LOG.error("Error while removing dead TablePart or already removed");
            }
        }
    }

    @Override
    public void reportDeadTablepart(String uid) {
        LOG.info(String.format("TablePart reported as dead: %s", uid));
        unregisterTablepart(uid);

        int ran = randomGenerator.nextInt(recoveryIds.size());

        Recovery recovery;

        while (recoveryIds.size() > 0) {
            try {
                recovery = (Recovery) registry.lookup(recoveryIds.get(ran));
                recovery.restartTablePart();
                LOG.info(String.format("Started new TablePart on: %s", recoveryIds.get(ran)));
                return;

            } catch (Exception e) {
                unregisterRecovery(recoveryIds.get(ran));
                ran = randomGenerator.nextInt(recoveryIds.size());
            }
        }

        LOG.info("Can't start new TablePart, all Recoveries are down.");

    }

    @Override
    public void registerPhilosopher(String uid, boolean hungry) {
        synchronized (philosopherIds) {
            LOG.info(String.format("Adding Philosopher %s", uid));
            philosopherIds.put(uid, new AbstractMap.SimpleEntry<>(0, hungry));
        }
    }

    @Override
    public void unregisterPhilosopher(String uid) {
        synchronized (philosopherIds) {
            LOG.info(String.format("Remove Philosopher %s", uid));
            try {
                philosopherIds.remove(uid);
                registry.unbind(uid);
            } catch (Exception e1) {
                e1.printStackTrace();
                LOG.error("Error while removing Philosopher or already removed");
            }
        }
    }

    @Override
    public void reportDeadPhilosopher(String uid) {
        LOG.info(String.format("Philosopher reported as dead: %s", uid));

        Integer eatCount = philosopherIds.get(uid).getKey();
        Boolean hungry = philosopherIds.get(uid).getValue();
        unregisterPhilosopher(uid);

        int ran = randomGenerator.nextInt(recoveryIds.size());

        Recovery recovery;

        while (recoveryIds.size() > 0)
            try {
                recovery = (Recovery) registry.lookup(recoveryIds.get(ran));
                recovery.restartPhilosopher(eatCount, hungry);
                LOG.info(String.format("Started new Philosopher on: %s", recoveryIds.get(ran)));
                return;

            } catch (Exception e) {
                e.printStackTrace();
                unregisterRecovery(recoveryIds.get(ran));
                ran = randomGenerator.nextInt(recoveryIds.size());
            }

        LOG.info("Can't start new Philosopher, all Recoveries are down.");
    }

    @Override
    public Map<String, Integer> getPhilosophersEatCount() {
        return philosopherIds.entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getKey()));
    }

    @Override
    public TablePart getNextTablePart(String myUid) {
        TablePart tablePart;
        int index = (tableIds.indexOf(myUid) + 1) % tableIds.size();
        while (true) {
            try {
                tablePart = (TablePart) registry.lookup(tableIds.get(index));
                tablePart.getId();
                return tablePart;
            } catch (Exception e) {

                e.printStackTrace();
                LOG.error(String.format("TablePart %s  not found. Removing from active table parts.", tableIds.get(index)));

                reportDeadTablepart(tableIds.get(index));

                if (index == tableIds.size()) {
                    index = 0;
                }
            }
        }
    }

    @Override
    public TablePart getRandomTablePart() {
        TablePart tablePart;

        if(tableIds.size() == 0)
          return null;


        int index = randomGenerator.nextInt( tableIds.size());

        while (true) {
            try {
                tablePart = (TablePart) registry.lookup(tableIds.get(index));
                LOG.info(String.format("Got TablePart %s", tablePart.getId()));
                return tablePart;
            } catch (Exception e) {
                e.printStackTrace();
                LOG.error(String.format("TablePart %s  not found. Removing from active table parts.", tableIds.get(index)));

                reportDeadTablepart(tableIds.get(index));

                index = randomGenerator.nextInt(tableIds.size());
            }
        }
    }

    @Override
    public Philosopher getPhilosopher(String id) throws RemoteException {
        try {
            return (Philosopher) registry.lookup(id);
        } catch (NotBoundException e) {
            LOG.error("given philosopher id dont exist anymore.");
            return null;
        }
    }

    @Override
    public List<String> getTableIds() throws RemoteException {
        return tableIds;
    }

    @Override
    public List<String> getPhilosopherIds() throws RemoteException {
        return new ArrayList<>(philosopherIds.keySet());
    }

    @Override
    public void stopRemote(String id) throws RemoteException {
        if (tableIds.contains(id)) {
          unregisterTablepart(id);
          LOG.debug("Table stopped: " + id);

          philosopherIds.keySet().forEach(philosopherID -> {

            try {
              Philosopher philosopher = (Philosopher) registry.lookup(philosopherID);

              philosopher.replaceStoppedTable(id, getRandomTablePart());

            } catch (Exception e) {
              LOG.debug("Error on replacing tablepart: " + e.getMessage());
            }

          });

        } else if (philosopherIds.containsKey(id)) {
            try {
                Philosopher lookup = (Philosopher) registry.lookup(id);
                lookup.stop();
            } catch (NotBoundException | RemoteException e) {
                LOG.error(e.getMessage());
            }
            unregisterPhilosopher(id);
            LOG.debug("Philosopher stopped: " + id);
        } else {
          LOG.error("Unknown ID: " + id);
        }
    }

    /**
     * Checks every second if all tableparts are available.
     */
    @Override
    public void run() {
        while (!Thread.interrupted()) {

            Map<String, Map.Entry<Integer,Boolean>> philEatCache = new HashMap<>();

                // Check all Philosophers
            List<String> deadPhils = philosopherIds.keySet().stream().filter(philID -> {
                    try {
                        Philosopher lookup = (Philosopher) registry.lookup(philID);
                        //Try to get eatCounter and save it in list;
                        int eatCount = lookup.getEatCounter();
                        philEatCache.put(
                            philID,
                            new AbstractMap.SimpleEntry<>(
                                eatCount,
                                philosopherIds.get(philID).getValue()));
                        return false;
                    } catch (NotBoundException | RemoteException e) {
                        LOG.error(String.format("Philosopher %s  not found. Removing from active philosophers.", philID));
                        return true;
                    }
                }).collect(Collectors.toList());
            //Save all the EatCount Data
            synchronized (philosopherIds) {
                philEatCache.entrySet().forEach(entry -> philosopherIds.put(entry.getKey(), entry.getValue()));
            }

            // Check all TableParts
            List<String> deadTables = tableIds.stream().filter(tableID -> {
                try {
                    TablePart lookup = (TablePart) registry.lookup(tableID);
                    lookup.getId();
                    return false;
                } catch (NotBoundException | RemoteException e) {
                    LOG.error(String.format("TablePart %s  not found. Removing from active tableparts.", tableID));
                    return true;
                }
            }).collect(Collectors.toList());

            deadPhils.forEach(this::reportDeadPhilosopher);
            deadTables.forEach(this::reportDeadTablepart);

            // Sleep for specified time
            try {
                Thread.sleep(CHECK_INTERVAL);
            } catch (InterruptedException e) {
                LOG.error("Manager interrupted while sleeping");
            }
        }
    }
}
