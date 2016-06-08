package manager;

import api.BindingProxy;
import api.Manager;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;

/**
 * Organization: HM FK07.
 * Project: VerteilteSystemePraktikum, manager
 * Author(s): Rene Zarwel
 * Date: 08.06.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
public class ManagerController {

  private static final Logger LOG = Logger.getLogger(ManagerController.class.getName());

  private static Manager manager;
  private static BindingProxy binder;

  public static void main(String[] args) {
    // Init Manager and Binder
    if (System.getSecurityManager() == null) {
      System.setSecurityManager(new SecurityManager());
    }
    try {
      Registry registry = LocateRegistry.getRegistry();

      manager = (Manager) UnicastRemoteObject.exportObject(new ManagerImpl(registry), 0);
      binder = (api.BindingProxy) UnicastRemoteObject.exportObject(new BindingProxyImpl(), 0);

      registry.rebind(Manager.NAME, manager);
      registry.rebind(BindingProxy.NAME, binder);

      LOG.info("Manager and Binder bound to registry.");
    } catch (Exception e) {
      LOG.severe("Exception while binding Manager/Binder.");
      e.printStackTrace();
    }


    //Controll Loop to take actions from console
    while (!Thread.interrupted()) {
      //Read input

      //Process input

    }
  }
}
