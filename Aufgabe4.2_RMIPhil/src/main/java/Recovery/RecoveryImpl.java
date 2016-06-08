package Recovery;

import api.Recovery;
import philosopher.PhilosopherImpl;
import table.TablePartImpl;

import java.rmi.RemoteException;

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

  public final String ip;

  public RecoveryImpl(String ip) {
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
