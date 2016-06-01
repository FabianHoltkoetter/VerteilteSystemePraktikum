package server;

import api.Manager;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Organization: HM FK07.
 * Project: VerteilteSystemePraktikum, server
 * Author(s): Rene Zarwel
 * Date: 01.06.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
public class ManagerThing implements Manager {
  public ManagerThing() {
    super();
  }

  public static void main(String[] args) {
    if (System.getSecurityManager() == null) {
      System.setSecurityManager(new SecurityManager());
    }
    try {
      Manager stub = (Manager) UnicastRemoteObject.exportObject(new ManagerThing(), 0);
      Registry registry = LocateRegistry.getRegistry();
      server.ManagerThing manager = (ManagerThing) registry.lookup("manager");
      manager.proxyRebind("manager", stub);
      System.out.println("manager bound");
    } catch (Exception e) {
      System.err.println("exception:");
      e.printStackTrace();
    }
  }
  @Override
  public void proxyRebind(String name, Remote object) throws RemoteException {
    try {
      Registry registry = LocateRegistry.getRegistry();
      registry.bind(name, object);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
