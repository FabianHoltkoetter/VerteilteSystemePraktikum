package manager;

import Recovery.RecoveryImpl;
import api.BindingProxy;
import api.Manager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMISocketFactory;
import java.rmi.server.UnicastRemoteObject;

/**
 * Organization: HM FK07.
 * Project: VerteilteSystemePraktikum, manager
 * Author(s): Rene Zarwel
 * Date: 08.06.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
public class ManagerController {

    private static final Logger LOG = LogManager.getLogger(ManagerController.class.getName());

    private static Manager manager;
    private static BindingProxy binder;

    public static void main(String[] args) {


        try {
            RMISocketFactory.setSocketFactory(new RMISocketFactory() {
                public Socket createSocket(String host, int port)
                        throws IOException {
                    Socket socket = new Socket();
                    socket.setSoTimeout(2000);
                    socket.setSoLinger(false, 0);
                    socket.connect(new InetSocketAddress(host, port), 2000);
                    return socket;
                }

                public ServerSocket createServerSocket(int port)
                        throws IOException {
                    return new ServerSocket(port);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (args.length == 1) {

            String hostIP = args[0];

            if (System.getProperty("java.security.policy") == null || System.getProperty("java.security.policy").isEmpty())
                System.setProperty("java.security.policy", "file:./security.policy");

            // Init Manager and Binder
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }
            try {
                Registry registry = LocateRegistry.getRegistry(hostIP);

                ManagerImpl obj = new ManagerImpl(registry);
                manager = (Manager) UnicastRemoteObject.exportObject(obj, 0);
                binder = (api.BindingProxy) UnicastRemoteObject.exportObject(new BindingProxyImpl(hostIP), 0);

                new Thread(obj).start();

                registry.rebind(Manager.NAME, manager);
                registry.rebind(BindingProxy.NAME, binder);

                LOG.info("Manager and Binder bound to registry.");

                new TableMaster(hostIP).start();

                RecoveryImpl.startRecovery(hostIP, hostIP);

            } catch (Exception e) {
                LOG.error("Exception while binding Manager/Binder.");
                e.printStackTrace();
            }
        } else {
            LOG.info("No IP provided in args");
        }
    }
}
