package api;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Fabian on 01.06.2016.
 */
public interface Philosopher extends Remote {
    public void setAllowedToEat(boolean allowedToEat) throws RemoteException;
    public int getEatCounter() throws RemoteException;
    public boolean isHungry() throws RemoteException;
}
