package manager;

import api.BindingProxyInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Organization: HM FK07.
 * Project: VerteilteSystemePraktikum, server
 * Author(s): Rene Zarwel
 * Date: 01.06.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
public class BindingProxy implements BindingProxyInterface {
  private static final Logger LOG = Logger.getLogger(BindingProxy.class.getName());

  public BindingProxy() {
    super();
  }

  public static void main(String[] args) {
    if (System.getSecurityManager() == null) {
      System.setSecurityManager(new SecurityManager());
    }
    try {
      BindingProxyInterface stub = (BindingProxyInterface) UnicastRemoteObject.exportObject(new BindingProxy(), 0);
      Registry registry = LocateRegistry.getRegistry();
      registry.rebind(BindingProxyInterface.NAME, stub);
      LOG.log(Level.INFO, "BindingProxy bound to registry.");
    } catch (Exception e) {
      LOG.log(Level.SEVERE, "exception:");
      e.printStackTrace();
    }
  }
  @Override
  public void proxyRebind(String name, Remote object) throws RemoteException {
    try {
      Registry registry = LocateRegistry.getRegistry();
      registry.rebind(name, object);
      LOG.log(Level.INFO, "Bound " + name + " to registry." );
    } catch (Exception e) {
      LOG.log(Level.SEVERE, name + " was not bound!");
      e.printStackTrace();
    }
  }
}
