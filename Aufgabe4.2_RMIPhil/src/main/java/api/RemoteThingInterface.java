package api;

    import java.rmi.Remote;
    import java.rmi.RemoteException;

public interface RemoteThingInterface extends Remote {
  RemoteThingInterface getCompute() throws RemoteException;
  void print() throws RemoteException;
}
