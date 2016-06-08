package manager;

import api.Philosopher;
import api.TablePart;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by Fabian on 01.06.2016.
 */
public class ManagerImpl implements api.Manager {
  private static final Logger LOG = Logger.getLogger(ManagerImpl.class.getName());
  private final Registry registry;
  private final ArrayList<String> philosopherIds = new ArrayList<>();
  private final ArrayList<String> tableIds = new ArrayList<>();
  private final ArrayList<String> recoveryIds = new ArrayList<>();
  private ThreadLocalRandom randomGenerator = ThreadLocalRandom.current();

  public ManagerImpl(Registry registry) {
    super();
    this.registry = registry;
  }

  @Override
  public void registerRecovery(String vmid) throws RemoteException {
    synchronized (recoveryIds) {
      LOG.info(String.format("Adding Recovery %s", vmid));
      recoveryIds.add(vmid);
    }
  }

  @Override
  public void unregisterRecovery(String vmid) throws RemoteException {
    synchronized (recoveryIds) {
      LOG.info(String.format("Removing Recovery %s", vmid));
      recoveryIds.remove(vmid);
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
            LOG.severe("Found dead TablePart");

            unregisterTablepart(lastId);
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
        LOG.severe("Error while setting next TablePart");
      }
    }
  }

  @Override
  public void unregisterTablepart(String uid) {
    synchronized (tableIds) {
      LOG.info(String.format("Remove TablePart %s", uid));
      try {
        tableIds.remove(uid);
        registry.unbind(uid);
      } catch (Exception e1) {
        e1.printStackTrace();
        LOG.severe("Error while removing dead TablePart or already removed");
      }
    }
  }

  @Override
  public void registerPhilosopher(String uid) {
    synchronized (philosopherIds) {
      LOG.info(String.format("Adding Philosopher %s", uid));
      philosopherIds.add(uid);
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
        LOG.severe("Error while removing Philosopher or already removed");
      }
    }
  }

  @Override
  public List<Philosopher> getPhilosophers() {
    List<String> illegalPhilosophers = new ArrayList<>();
    List<Philosopher> philosophers = philosopherIds.stream().map(s -> {
      try {
        Philosopher lookup = (Philosopher) registry.lookup(s);
        lookup.getEatCounter();
        return lookup;
      } catch (Exception e) {
        illegalPhilosophers.add(s);
        e.printStackTrace();
        LOG.severe(String.format("Philosopher %s  not found. Removing from active philosophers.", s));
      }
      return null;
    }).filter(r -> r != null).collect(Collectors.toList());

    //Remove all illegal Philosophers
    illegalPhilosophers.forEach(this::unregisterPhilosopher);

    return philosophers;
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
        LOG.severe(String.format("TablePart %s  not found. Removing from active table parts.", tableIds.get(index)));

        unregisterTablepart(tableIds.get(index));

        if (index == tableIds.size()) {
          index = 0;
        }
      }
    }
  }

  @Override
  public TablePart getRandomTablePart() {
    TablePart tablePart;

    int index = randomGenerator.nextInt(tableIds.size());

    while (true) {
      try {
        tablePart = (TablePart) registry.lookup(tableIds.get(index));
        LOG.info(String.format("Got TablePart %s", tablePart.getId()));
        return tablePart;
      } catch (Exception e) {
        e.printStackTrace();
        LOG.severe(String.format("TablePart %s  not found. Removing from active table parts.", tableIds.get(index)));

        unregisterTablepart(tableIds.get(index));

        index = randomGenerator.nextInt(tableIds.size());
      }
    }
  }
}
