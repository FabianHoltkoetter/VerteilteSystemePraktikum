package table;

import api.BindingProxy;
import api.Manager;
import api.SimpleRMIClientSocketFactory;
import api.SimpleRMIServerSocketFactory;
import api.TablePart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by René Zarwel on 01.06.2016.
 */
public class TablePartImpl implements TablePart, Serializable{

  private static final Logger LOG = LogManager.getLogger(TablePart.class.getName());


  private final String id = UUID.randomUUID().toString();

  private Fork leftFork = new Fork();
  private Fork rightFork = new Fork();

  private TablePart nextTablePart;

  public TablePartImpl(String managerIP, String ownIP) {

    super();
    if (System.getSecurityManager() == null) {
      System.setSecurityManager(new SecurityManager());
    }
    try {
      LOG.debug("Started TablePart with ID " + id);
      //Register on Registry

      SimpleRMIClientSocketFactory csf = new SimpleRMIClientSocketFactory(ownIP);
      SimpleRMIServerSocketFactory ssf = new SimpleRMIServerSocketFactory();

      TablePart stub = (TablePart) UnicastRemoteObject.exportObject(this, 0,csf, ssf);
      Registry registry = LocateRegistry.getRegistry(managerIP);
      BindingProxy bindingProxy = (BindingProxy) registry.lookup(BindingProxy.NAME);
      bindingProxy.proxyRebind(id, stub);

      LOG.info(String.format("TablePart %s bound to registry.", id));

      Manager manager = (Manager) registry.lookup(Manager.NAME);
      manager.registerTablepart(id);

      LOG.info(String.format("TablePart %s registered in manager.", id));

      nextTablePart = manager.getNextTablePart(id);

    } catch (Exception e) {
      LOG.error(String.format("Problem binding TablePart %s.", id));
      throw new RuntimeException(e.getMessage());
    }


  }

  @Override
  public Map<Integer, TablePart> takeSeat(String uuid) throws RemoteException {

    Map<Integer, TablePart> result = new LinkedHashMap<>();

    //Try to get both forks
    if(takeLeftFork(uuid) && takeRightFork(uuid)){

      LOG.info(String.format("Got both forks on TablePart %s.", id));

      result.put(0, this);
      result.put(1, this);
      LOG.info(String.format("Return: %s.", result.toString()));
      return result;

    } else {
      //Dont got both forks, free and move further
      leftFork.unblock();
      LOG.info(String.format("Dont got both forks on TablePart %s.", id));

      //Try to get right fork and left fork of next TP
      if(takeRightFork(uuid)){

        LOG.info(String.format("Got right fork on TablePart %s.", id));

        //Cache locally to prevent failure on nextTablePart change
        TablePart nextTP = nextTablePart;

        if(nextTP.takeLeftFork(uuid)){

          LOG.info(String.format("Got left fork on TablePart %s.", nextTP.getId()));

          result.put(1, this);
          result.put(0, nextTP);
          LOG.info(String.format("Return: %s.", result.toString()));
          return result;
        } else {
          rightFork.unblock();
        }
      }

    }


    LOG.info(String.format("Got no forks on TablePart %s.", id));
    result.put(-1, nextTablePart);
    LOG.info(String.format("Return: %s.", result.toString()));
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
        LOG.info("Wrong Seat Number");
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
