package manager;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
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
public class BindingProxyImpl implements api.BindingProxy {
  private static final Logger LOG = Logger.getLogger(BindingProxyImpl.class.getName());

  public BindingProxyImpl() {
    super();
  }

  public static void main(String[] args) {
    if (System.getSecurityManager() == null) {
      System.setSecurityManager(new SecurityManager());
    }
    try {
      api.BindingProxy stub = (api.BindingProxy) UnicastRemoteObject.exportObject(new BindingProxyImpl(), 0);
      Registry registry = LocateRegistry.getRegistry();
      registry.rebind(api.BindingProxy.NAME, stub);
      LOG.info("BindingProxyImpl bound to registry.");
    } catch (Exception e) {
      LOG.severe("Exception while binding BindingProxyImpl");
      e.printStackTrace();
    }
  }

  @Override
  public void proxyRebind(String name, Remote object) throws RemoteException {
    try {
      Registry registry = LocateRegistry.getRegistry();
      registry.rebind(name, object);
      LOG.info("Bound " + name + " to registry." );
    } catch (Exception e) {
      LOG.severe(name + " was not bound!");
      e.printStackTrace();
    }
  }
}
