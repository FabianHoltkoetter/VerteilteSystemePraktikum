package manager;

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

  public static void main(String[] args) {

    // Init Manager
    if (System.getSecurityManager() == null) {
      System.setSecurityManager(new SecurityManager());
    }
    try {
      manager = (Manager) UnicastRemoteObject.exportObject(new ManagerImpl(), 0);
      Registry registry = LocateRegistry.getRegistry();
      registry.rebind(Manager.NAME, manager);
      LOG.info("ManagerImpl bound to registry.");
    } catch (Exception e) {
      LOG.severe("Exception while binding Manager.");
      e.printStackTrace();
    }


    //Controll Loop to take actions from console
    while (!Thread.interrupted()){
      //Read input

      //Process input

    }
  }
}
