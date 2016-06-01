package manager;

import api.BindingProxy;
import api.Manager;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;

/**
 * Created by Fabian on 01.06.2016.
 */
public class ManagerImpl implements api.Manager {
    private static final Logger LOG = Logger.getLogger(ManagerImpl.class.getName());

    public ManagerImpl() {
        super();
    }

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
}
