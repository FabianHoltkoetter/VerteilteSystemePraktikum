package manager;

import api.Manager;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Fabian on 14.06.2016.
 */
public class ControlTerminal implements Runnable {

    public static void main(String[] args) {
        if(System.getProperty("java.security.policy") == null || System.getProperty("java.security.policy").isEmpty())
            System.setProperty("java.security.policy","file:./security.policy");

        if(args.length != 1){
            throw new IllegalArgumentException("please specify ip of registry as argument.");
        }
        new Thread(new ControlTerminal(args[0])).start();
    }

    private Manager manager;

    public ControlTerminal(String ip) {
        // Init Manager and Binder
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            Registry registry = LocateRegistry.getRegistry(ip);
            manager = (Manager) registry.lookup(Manager.NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        //Controll Loop to take actions from console
        while (!Thread.interrupted()) {
            //Read inputt
            Scanner scanner = new Scanner(System.in);
            System.out.print(">>> ");
            String command = scanner.next();

            if (command.equals("ps")) {
                listAll();
            } else if (command.startsWith("stop")) {
                stop(scanner.next());
            } else {
                displayHelp();
            }
        }
    }

    private void listAll() {
        try {
            List<String> tps = manager.getTableIds();
            List<String> phils = manager.getPhilosopherIds();

            System.out.println("Table Parts:");
            tps.stream().forEach(System.out::println);
            System.out.println("Philosophers:");
            phils.stream().forEach(System.out::println);
        } catch (RemoteException e) {
            System.out.println(String.format("An error occured: %s", e.getMessage()));
        }
    }

    private void stop(String id) {
        try {
            manager.stopRemote(id);
        } catch (RemoteException e) {
            System.out.println(String.format("An error occured: %s", e.getMessage()));
        }
    }

    private void displayHelp() {
        System.out.println("Available commands:");
        System.out.println("ps        - Displays all running Table Parts and Philosophers");
        System.out.println("stop <id> - Stops the remote object with the given id.");
    }
}
