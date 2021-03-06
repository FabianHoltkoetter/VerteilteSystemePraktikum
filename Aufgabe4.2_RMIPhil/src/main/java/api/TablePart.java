package api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

/**
 * Created by Fabian on 01.06.2016.
 */
public interface TablePart extends Remote {

  static final Integer NEXT_TABLEPART = -1;

  String getId() throws RemoteException;
  void setNextTablePart(TablePart nextTablePart) throws RemoteException;
  Map<Integer, TablePart> takeSeat(String uuid) throws RemoteException;
  boolean takeLeftFork(String uuid) throws RemoteException;
  boolean takeRightFork(String uuid) throws RemoteException;
  void leaveSeat(Integer seatNumber) throws RemoteException;
}
