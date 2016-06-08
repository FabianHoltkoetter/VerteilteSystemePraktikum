package manager;

import api.Manager;
import api.Philosopher;
import api.TablePart;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by Fabian on 01.06.2016.
 */
public class ManagerImpl implements api.Manager {
    private static final Logger LOG = Logger.getLogger(ManagerImpl.class.getName());
    private final Registry registry;
    private List<String> philosopherIds = new ArrayList<>();
    private List<String> tableIds = new ArrayList<>();

    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            Manager stub = (Manager) UnicastRemoteObject.exportObject(new ManagerImpl(), 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(Manager.NAME, stub);
            LOG.log(Level.INFO, "ManagerImpl bound to registry.");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Exception while binding Manager.");
            e.printStackTrace();
        }
    }

    public ManagerImpl() {
        super();
        try {
            registry = LocateRegistry.getRegistry();
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't get registry in Manager.");
        }
    }

    @Override
    public void registerTablepart(String uid) {
        tableIds.add(uid);
    }

    @Override
    public void registerPhilosopher(String uid) {
        philosopherIds.add(uid);
    }

    @Override
    public List<Philosopher> getPhilosophers() {
        List<String> illegalPhilosophers = new ArrayList<>();
        List<Philosopher> philosophers = philosopherIds.stream().map(s -> {
            try {
                return (Philosopher) registry.lookup(s);
            } catch (Exception e) {
                illegalPhilosophers.add(s);
                e.printStackTrace();
                LOG.log(Level.SEVERE, String.format("Philosopher %s  not found. Removing from active philosophers.", s));
            }
            return null;
        }).filter(r -> r != null).collect(Collectors.toList());
        philosopherIds.removeAll(illegalPhilosophers);
        return philosophers;
    }

    @Override
    public TablePart getNextTablePart(String myUid) {
        TablePart tablePart = null;
        int index = (tableIds.indexOf(myUid) + 1) % tableIds.size();
        while (tablePart == null) {
            try {
                tablePart = (TablePart) registry.lookup(tableIds.get(index));
            } catch (Exception e) {
                e.printStackTrace();
                LOG.log(Level.SEVERE, String.format("TablePart %s  not found. Removing from active table parts.", tableIds.get(index)));
                tableIds.remove(index);
                if (index >= tableIds.size() - 1) {
                    index = 0;
                }
            }
        }
        return tablePart;
    }

    @Override
    public TablePart getRandomTablePart() {
        TablePart tablePart = null;
        Random randomGenerator = new Random();
        int index = randomGenerator.nextInt(tableIds.size());

        while (tablePart == null) {
            try {
                tablePart = (TablePart) registry.lookup(tableIds.get(index));
            } catch (Exception e) {
                e.printStackTrace();
                LOG.log(Level.SEVERE, String.format("TablePart %s  not found. Removing from active table parts.", tableIds.get(index)));
                tableIds.remove(index);
                index = randomGenerator.nextInt(tableIds.size());
            }
        }

        return tablePart;
    }
}
