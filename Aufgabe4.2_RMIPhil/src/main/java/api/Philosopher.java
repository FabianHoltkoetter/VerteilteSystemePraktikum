package api;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Fabian on 01.06.2016.
 */
public interface Philosopher extends Remote {
    void setAllowedToEat(boolean allowedToEat) throws RemoteException;
    int getEatCounter() throws RemoteException;
    boolean isHungry() throws RemoteException;
    void stop() throws RemoteException;
    void start() throws RemoteException;
    void replaceStoppedTable(String id, TablePart tablePart) throws RemoteException;
}
