package philosopher;

import api.Manager;
import api.Philosopher;
import manager.ManagerImpl;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Fabian on 01.06.2016.
 */
public class PhilosopherImpl implements Philosopher {
    private static final Logger LOG = Logger.getLogger(ManagerImpl.class.getName());

    private final String id = UUID.randomUUID().toString();

    public PhilosopherImpl(String ip){
        super();
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            Philosopher stub = (Philosopher) UnicastRemoteObject.exportObject(this, 0);
            Registry registry = LocateRegistry.getRegistry(ip);
            registry.rebind(id, stub);
            LOG.log(Level.INFO, String.format("Philosopher %s bound to registry.", id));
            Manager manager = (Manager) registry.lookup(Manager.NAME);
            manager.registerPhilosopher(id);
            LOG.log(Level.INFO, String.format("Philosopher %s registered in manager.", id));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, String.format("Problem binding Philosopher %s.", id));
            e.printStackTrace();
        }
    }

}
