package api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

/**
 * Centralized Manager to manage the restaurant. Is run together with {@link BindingProxy} and the central RMIRegistry on one Machine.
 */
public interface Manager extends Remote {
  /**
   * Name to be used to register in the RMI
   */
  String NAME = "manager";

  /**
   * Registers a {@link Recovery} in the restaurant
   *
   * @param vmid The uid with which the @{@link Recovery} is stored in the RMI
   */
  void registerRecovery(String vmid) throws RemoteException;

  /**
   * Unregisters a {@link Recovery} in the restaurant
   *
   * @param vmid The uid of the @{@link Recovery} to be removed
   */
  void unregisterRecovery(String vmid) throws RemoteException;

  /**
   * Registers a {@link TablePart} in the restaurant
   *
   * @param uid The uid with which the @{@link TablePart} is stored in the RMI
   */
  void registerTablepart(String uid) throws RemoteException;

  /**
   * Unregisters a {@link TablePart} in the restaurant
   *
   * @param uid The uid of the @{@link TablePart} to be removed
   */
  void unregisterTablepart(String uid) throws RemoteException;

  /**
   * Reports a dead {@link TablePart} in the restaurant
   *
   * @param uid The uid of the @{@link TablePart} to be reported
   */
  void reportDeadTablepart(String uid) throws RemoteException;

  /**
   * Registers a {@link Philosopher} in the restaurant
   *
   * @param uid The uid with which the {@link Philosopher} is stored in the RMI
   */
  void registerPhilosopher(String uid, boolean hungry) throws RemoteException;

  /**
   * Unregisters a {@link Philosopher} in the restaurant
   *
   * @param uid The uid of the @{@link Philosopher} to be removed
   */
  void unregisterPhilosopher(String uid) throws RemoteException;

  /**
   * Reports a dead {@link Philosopher} in the restaurant
   *
   * @param uid The uid of the @{@link Philosopher} to be reported
   */
  void reportDeadPhilosopher(String uid) throws RemoteException;

  /**
   * Get all registered Philosopher IDs with eat count.
   *
   * @return List of all registered {@link Philosopher}
   */
  Map<String, Integer> getPhilosophersEatCount() throws RemoteException;

  /**
   * Get a specific {@link Philosopher}.
   *
   * @return {@link Philosopher} with the given id.
   */
  Philosopher getPhilosopher(String id) throws RemoteException;

  /**
   * Returns the next tablepart. To be used by {@link TablePart} to "close" the table.
   *
   * @param myUid The UID of this table.
   * @return The next table.
   */
  TablePart getNextTablePart(String myUid) throws RemoteException;

  /**
   * Returns a random table part. To be used by {@link Philosopher} to get the first table part to look for a free seat.
   *
   * @return A random table part.
   */
  TablePart getRandomTablePart() throws RemoteException;

  /**
   * Returns all table ids.
   * @return all table ids.
   * @throws RemoteException
     */
  List<String> getTableIds() throws RemoteException;

  /**
   * Returns all philosopher ids.
   * @return all philosopher ids.
   * @throws RemoteException
     */
  List<String> getPhilosopherIds() throws RemoteException;

  /**
   * Shuts down a table or philosopher.
   * @param id ID of tablepart or philosopher.
   * @throws RemoteException
     */
  void stopRemote(String id) throws RemoteException;
}
