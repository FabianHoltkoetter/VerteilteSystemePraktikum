package table;

import api.BindingProxy;
import api.Manager;
import api.TablePart;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by René Zarwel on 01.06.2016.
 */
public class TablePartImpl implements TablePart {

  private static final Logger LOG = Logger.getLogger(TablePart.class.getName());


  private final String id = UUID.randomUUID().toString();

  private Fork leftFork = new Fork();
  private Fork rightFork = new Fork();

  private TablePart nextTablePart;

  public TablePartImpl(String ip) {

    super();
    if (System.getSecurityManager() == null) {
      System.setSecurityManager(new SecurityManager());
    }
    try {
      //Register on Registry
      TablePart stub = (TablePart) UnicastRemoteObject.exportObject(this, 0);
      Registry registry = LocateRegistry.getRegistry(ip);
      BindingProxy bindingProxy = (BindingProxy) registry.lookup(BindingProxy.NAME);
      bindingProxy.proxyRebind(id, stub);

      LOG.log(Level.INFO, String.format("TablePart %s bound to registry.", id));

      Manager manager = (Manager) registry.lookup(Manager.NAME);
      manager.registerTablepart(id);

      LOG.log(Level.INFO, String.format("TablePart %s registered in manager.", id));

      nextTablePart = manager.getNextTablePart(id);

    } catch (Exception e) {
      LOG.log(Level.SEVERE, String.format("Problem binding TablePart %s.", id));
      throw new RuntimeException(e.getMessage());
    }


  }

  @Override
  public Map<Integer, TablePart> takeSeat(String uuid) throws RemoteException {

    Map<Integer, TablePart> result = new LinkedHashMap<>();

    //Try to get both forks
    if(takeLeftFork(uuid) && takeRightFork(uuid)){

      LOG.log(Level.INFO, String.format("Got both forks on TablePart %s.", id));

      result.put(0, this);
      result.put(1, this);
      LOG.log(Level.INFO, String.format("Return: %s.", result.toString()));
      return result;

    } else {
      //Dont got both forks, free and move further
      leftFork.unblock();
      LOG.log(Level.INFO, String.format("Dont got both forks on TablePart %s.", id));

      //Try to get right fork and left fork of next TP
      if(takeRightFork(uuid)){

        LOG.log(Level.INFO, String.format("Got right fork on TablePart %s.", id));

        //Cache locally to prevent failure on nextTablePart change
        TablePart nextTP = nextTablePart;

        if(nextTP.takeLeftFork(uuid)){

          LOG.log(Level.INFO, String.format("Got left fork on TablePart %s.", nextTP.getId()));

          result.put(1, this);
          result.put(0, nextTP);
          LOG.log(Level.INFO, String.format("Return: %s.", result.toString()));
          return result;
        } else {
          rightFork.unblock();
        }
      }

    }


    LOG.log(Level.INFO, String.format("Got no forks on TablePart %s.", id));
    result.put(-1, nextTablePart);
    LOG.log(Level.INFO, String.format("Return: %s.", result.toString()));
    return result;
  }

  @Override
  public boolean takeLeftFork(String uuid) throws RemoteException {
    return leftFork.tryBlock(uuid);
  }

  @Override
  public boolean takeRightFork(String uuid) throws RemoteException {
    return rightFork.tryBlock(uuid);
  }

  @Override
  public void leaveSeat(Integer seatNumber) throws RemoteException {
    switch (seatNumber) {
      case 0: leftFork.unblock();
        break;
      case 1: rightFork.unblock();
        break;
      default:
        LOG.log(Level.INFO, "Wrong Seat Number");
    }
  }

  @Override
  public void setNextTablePart(TablePart nextTablePart) throws RemoteException {
    this.nextTablePart = nextTablePart;
  }

  @Override
  public String getId() throws RemoteException {
    return id;
  }
}
