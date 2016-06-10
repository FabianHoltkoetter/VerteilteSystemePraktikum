package manager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

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
  private static final Logger LOG = LogManager.getLogger(BindingProxyImpl.class.getName());

  public BindingProxyImpl() {
    super();
  }

  @Override
  public void proxyRebind(String name, Remote object) throws RemoteException {
    try {
      Registry registry = LocateRegistry.getRegistry();
      registry.rebind(name, object);
      LOG.info("Bound " + name + " to registry." );
    } catch (Exception e) {
      LOG.error(name + " was not bound!");
      e.printStackTrace();
    }
  }
}
