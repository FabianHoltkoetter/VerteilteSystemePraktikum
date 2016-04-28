package server;

import api.Compute;
import api.Task;
import sun.rmi.transport.tcp.TCPTransport;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;

public class ComputeEngine implements Compute {

    public ComputeEngine() {
        super();
    }

    public <T> T executeTask(Task<T> t) {
        try {
            System.out.println(TCPTransport.getClientHost());
        } catch (ServerNotActiveException e) {
            e.printStackTrace();
        }
        return t.execute();
    }

    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "Compute";
            Compute engine = new ComputeEngine();
            Compute stub =
                    (Compute) UnicastRemoteObject.exportObject(engine, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("server.ComputeEngine bound");
        } catch (Exception e) {
            System.err.println("server.ComputeEngine exception:");
            e.printStackTrace();
        }
    }
}
