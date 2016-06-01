package client;
import api.RemoteThingInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
  public static void main(String args[]) {
    if (System.getSecurityManager() == null) {
      System.setSecurityManager(new SecurityManager());
    }
    try {
      String name = "Compute";
      Registry registry = LocateRegistry.getRegistry("10.179.9.28");
      RemoteThingInterface remoteThingInterface1 = (RemoteThingInterface) registry.lookup(name);
      remoteThingInterface1.print();
      RemoteThingInterface remoteThingInterface2 = remoteThingInterface1.getCompute();
      remoteThingInterface2.print();
    } catch (Exception e) {
      System.err.println("Client exception:");
      e.printStackTrace();
    }
  }
}