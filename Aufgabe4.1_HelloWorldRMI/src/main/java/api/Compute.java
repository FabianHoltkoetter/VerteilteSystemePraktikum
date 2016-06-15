package api;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Compute extends Remote {
    void sayHello() throws RemoteException;
}
