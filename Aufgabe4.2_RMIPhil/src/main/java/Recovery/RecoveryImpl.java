package Recovery;

import api.BindingProxy;
import api.Manager;
import api.Recovery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import philosopher.PhilosopherImpl;
import table.TablePartImpl;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.dgc.VMID;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
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

  private static final Logger LOG = LogManager.getLogger(RecoveryImpl.class.getName());

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

        Manager manager = (Manager) registry.lookup(Manager.NAME);
        manager.registerRecovery(ID);
        LOG.info(String.format("Recovery %s registered in manager.", ID));
      }

    } catch (Exception e) {
      e.printStackTrace();
      LOG.error(String.format("Problem binding Recovery %s", ID));
    }
  }

  public RecoveryImpl(String ip) {
    super();

    this.ip = ip;
  }

  @Override
  public void restartPhilosopher(Integer eatCount, boolean hungry) throws RemoteException {
    new Thread(new PhilosopherImpl(ip, eatCount, hungry)).start();
  }

  @Override
  public void restartTablePart() throws RemoteException {
    new TablePartImpl(ip);
  }
}
