package api;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Organization: HM FK07.
 * Project: VerteilteSystemePraktikum, api
 * Author(s): Rene Zarwel
 * Date: 08.06.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
public interface Recovery extends Remote {
  void restartPhilosopher(Integer eatCount, boolean hungry) throws RemoteException;
  void restartTablePart() throws RemoteException;
}
