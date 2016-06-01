package server;

import api.Manager;
import api.RemoteThingInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RemoteThing implements RemoteThingInterface {

  public RemoteThing() {
    super();
  }

  public static void main(String[] args) {
    if (System.getSecurityManager() == null) {
      System.setSecurityManager(new SecurityManager());
    }
    try {
      String name = args[0];
      RemoteThingInterface engine = new RemoteThing();
      RemoteThingInterface stub =
          (RemoteThingInterface) UnicastRemoteObject.exportObject(engine, 0);
      Registry registry = LocateRegistry.getRegistry("10.179.9.28");
      Manager manager = (Manager) registry.lookup("manager");
      manager.proxyRebind(name, stub);
      System.out.println("server.RemoteThing bound");
    } catch (Exception e) {
      System.err.println("server.RemoteThing exception:");
      e.printStackTrace();
    }
  }

  @Override
  public RemoteThingInterface getCompute() throws RemoteException {
    try {
      String name = "Compute2";
      Registry registry = LocateRegistry.getRegistry("10.179.9.28");
      Remote lookup = registry.lookup(name);
      RemoteThingInterface comp = (RemoteThingInterface) lookup;
      return comp;
    } catch (Exception e) {
      System.err.println("Client exception:");
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public void print() throws RemoteException {
    System.out.println("Hello from this engine!");
  }
}