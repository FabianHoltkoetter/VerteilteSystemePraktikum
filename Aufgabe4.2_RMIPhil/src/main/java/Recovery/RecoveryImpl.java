package Recovery;

import api.BindingProxy;
import api.Recovery;
import philosopher.PhilosopherImpl;
import table.TablePartImpl;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.dgc.VMID;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;

/**
 * Organization: HM FK07.
 * Project: VerteilteSystemePraktikum, Recovery
 * Author(s): Rene Zarwel
 * Date: 08.06.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
public class RecoveryImpl implements Recovery {

  private static final Logger LOG = Logger.getLogger(RecoveryImpl.class.getName());

  private static final String ID = new VMID().toString();


  public final String ip;

  public static void startRecovery(String ip){
    if (System.getSecurityManager() == null) {
      System.setSecurityManager(new SecurityManager());
    }
    try {

      Registry registry = LocateRegistry.getRegistry(ip);

      //Test if already running on this machine
      try {
        registry.lookup(ID);

      } catch (NotBoundException exception){

        //Bind new Recovery on this machine
        RecoveryImpl recovery = new RecoveryImpl(ip);
        Recovery stub = (Recovery) UnicastRemoteObject.exportObject(recovery, 0);
        ((BindingProxy) registry.lookup(BindingProxy.NAME)).proxyRebind(ID, stub);

        LOG.info(String.format("Recovery %s bound to registry.", ID));
      }

    } catch (Exception e) {
      LOG.severe(String.format("Problem binding Recovery %s", ID));
    }
  }

  public RecoveryImpl(String ip) {
    super();

    this.ip = ip;
  }

  @Override
  public void restartPhilosopher(Integer eatCount) throws RemoteException {
    new PhilosopherImpl(ip, eatCount);
  }

  @Override
  public void restartTablePart() throws RemoteException {
    new TablePartImpl(ip);
  }
}
