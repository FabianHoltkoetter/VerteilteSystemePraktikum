package manager;

import api.Philosopher;
import api.TablePart;

import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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

    public ManagerImpl(Registry registry) {
        super();
        this.registry = registry;
    }

    @Override
    public void registerTablepart(String uid) {
        LOG.info(String.format("Adding TablePart %s", uid));
        tableIds.add(uid);
    }

    @Override
    public void registerPhilosopher(String uid) {
        LOG.info(String.format("Adding Philosopher %s", uid));
        philosopherIds.add(uid);
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
                tablePart.getId();
            } catch (Exception e) {
                tablePart = null;
                e.printStackTrace();
                LOG.severe(String.format("TablePart %s  not found. Removing from active table parts.", tableIds.get(index)));
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
                LOG.info(String.format("Got TablePart %s", tablePart.getId()));
            } catch (Exception e) {
                tablePart = null;
                e.printStackTrace();
                LOG.severe(String.format("TablePart %s  not found. Removing from active table parts.", tableIds.get(index)));
                tableIds.remove(index);
                index = randomGenerator.nextInt(tableIds.size());
            }
        }

        return tablePart;
    }
}
