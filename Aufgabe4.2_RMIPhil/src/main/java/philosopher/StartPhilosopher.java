package philosopher;

import Recovery.RecoveryImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.RMISocketFactory;

/**
 * Created by Fabian on 08.06.2016.
 */
public class StartPhilosopher {

    private static final Logger LOG = LogManager.getLogger(StartPhilosopher.class.getName());


    public static void main(String[] args) {
        if (System.getProperty("java.security.policy") == null || System.getProperty("java.security.policy").isEmpty())
            System.setProperty("java.security.policy", "file:./security.policy");

        if (args.length == 2) {

            String ip = args[0];
            String ownIP = args[1];

            PhilosopherImpl p = new PhilosopherImpl(ip, ownIP, false);
            new Thread(p).start();


            RecoveryImpl.startRecovery(ip, ownIP);

        } else {
            LOG.info("No IP provided in args");
        }
    }
}
