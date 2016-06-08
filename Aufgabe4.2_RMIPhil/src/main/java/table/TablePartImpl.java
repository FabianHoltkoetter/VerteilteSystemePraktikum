package table;

import api.TablePart;

import java.rmi.RemoteException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by René Zarwel on 01.06.2016.
 */
public class TablePartImpl implements TablePart {

  private final String id = UUID.randomUUID().toString();

  private Fork leftFork = new Fork();
  private Fork rightFork = new Fork();

  private TablePart nextTablePart;

  public TablePartImpl() {



  }

  public Map<TablePart, Integer> takeSeat(String uuid) throws RemoteException {

    Map<TablePart, Integer> result = new LinkedHashMap<>();

    //Try to get both forks
    if(takeLeftFork(uuid) && takeRightFork(uuid)){
      result.put(this, 0);
      result.put(this, 1);
      return result;

    } else {
      //Dont got both forks, free and move further
      leftFork.unblock();

      //Try to get right fork and left fork of next TP
      if(takeRightFork(uuid)){

        if(nextTablePart.takeLeftFork(uuid)){
          result.put(this, 1);
          result.put(nextTablePart, 0);
          return result;
        } else {
          rightFork.unblock();
        }
      }

    }

    result.put(nextTablePart, null);
    return result;
  }

  public boolean takeLeftFork(String uuid) throws RemoteException {
    return leftFork.tryBlock(uuid);
  }

  public boolean takeRightFork(String uuid) throws RemoteException {
    return rightFork.tryBlock(uuid);
  }

  public void leaveSeat(Integer seatNumber) throws RemoteException {
    switch (seatNumber) {
      case 0: leftFork.unblock();
        break;
      case 1: rightFork.unblock();
        break;
    }
  }

  @Override
  public String getId() throws RemoteException {
    return id;
  }
}
