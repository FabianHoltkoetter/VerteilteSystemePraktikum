package manager;

import api.Philosopher;
import api.TablePart;

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
    private List<String> philosopherIds = new ArrayList<>();
    private List<String> tableIds = new ArrayList<>();
    private ThreadLocalRandom randomGenerator = ThreadLocalRandom.current();

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

        //Remove all illegal Philosophers
        philosopherIds.removeAll(illegalPhilosophers);
        illegalPhilosophers.forEach(philosopher -> {
            try {
                registry.unbind(philosopher);
            } catch (Exception e1) {
                e1.printStackTrace();
                LOG.severe("Error while removing dead Philosopher or already removed");
            }
        });

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

                try {
                    registry.unbind(tableIds.remove(index));
                } catch (Exception e1) {
                    e1.printStackTrace();
                    LOG.severe("Error while removing dead TablePart or already removed");
                }

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

                try {
                    registry.unbind(tableIds.remove(index));
                } catch (Exception e1) {
                    e1.printStackTrace();
                    LOG.severe("Error while removing dead TablePart or already removed");
                }

                index = randomGenerator.nextInt(tableIds.size());
            }
        }
    }
}
