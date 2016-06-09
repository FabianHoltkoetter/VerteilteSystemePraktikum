package api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Centralized Manager to manage the restaurant. Is run together with {@link BindingProxy} and the central RMIRegistry on one Machine.
 */
public interface Manager extends Remote {
  /**
   * Name to be used to register in the RMI
   */
  public static final String NAME = "manager";

  /**
   * Registers a {@link Recovery} in the restaurant
   *
   * @param vmid The uid with which the @{@link Recovery} is stored in the RMI
   */
  public void registerRecovery(String vmid) throws RemoteException;

  /**
   * Unregisters a {@link Recovery} in the restaurant
   *
   * @param vmid The uid of the @{@link Recovery} to be removed
   */
  public void unregisterRecovery(String vmid) throws RemoteException;

  /**
   * Registers a {@link TablePart} in the restaurant
   *
   * @param uid The uid with which the @{@link TablePart} is stored in the RMI
   */
  public void registerTablepart(String uid) throws RemoteException;

  /**
   * Unregisters a {@link TablePart} in the restaurant
   *
   * @param uid The uid of the @{@link TablePart} to be removed
   */
  public void unregisterTablepart(String uid) throws RemoteException;

  /**
   * Reports a dead {@link TablePart} in the restaurant
   *
   * @param uid The uid of the @{@link TablePart} to be reported
   */
  public void reportDeadTablepart(String uid) throws RemoteException;

  /**
   * Registers a {@link Philosopher} in the restaurant
   *
   * @param uid The uid with which the {@link Philosopher} is stored in the RMI
   */
  public void registerPhilosopher(String uid) throws RemoteException;

  /**
   * Unregisters a {@link Philosopher} in the restaurant
   *
   * @param uid The uid of the @{@link Philosopher} to be removed
   */
  public void unregisterPhilosopher(String uid) throws RemoteException;

  /**
   * Reports a dead {@link Philosopher} in the restaurant
   *
   * @param uid The uid of the @{@link Philosopher} to be reported
   */
  public void reportDeadPhilosopher(String uid) throws RemoteException;

  /**
   * Get all registered {@link Philosopher}
   *
   * @return List of all registered {@link Philosopher}
   */
  public List<Philosopher> getPhilosophers() throws RemoteException;

  /**
   * Returns the next tablepart. To be used by {@link TablePart} to "close" the table.
   *
   * @param myUid The UID of this table.
   * @return The next table.
   */
  public TablePart getNextTablePart(String myUid) throws RemoteException;

  /**
   * Returns a random table part. To be used by {@link Philosopher} to get the first table part to look for a free seat.
   *
   * @return A random table part.
   */
  public TablePart getRandomTablePart() throws RemoteException;
}
