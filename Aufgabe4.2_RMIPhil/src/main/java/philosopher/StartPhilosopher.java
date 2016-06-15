package philosopher;

import Recovery.RecoveryImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Fabian on 08.06.2016.
 */
public class StartPhilosopher {

  private static final Logger LOG = LogManager.getLogger(StartPhilosopher.class.getName());


  public static void main(String[] args) {

    if(System.getProperty("java.security.policy") == null || System.getProperty("java.security.policy").isEmpty())
      System.setProperty("java.security.policy","file:./security.policy");

    if (args.length == 1) {

      String ip = args[0];

      PhilosopherImpl p = new PhilosopherImpl(ip, false);
      new Thread(p).start();


      RecoveryImpl.startRecovery(ip);

    } else {
      LOG.info("No IP provided in args");
    }
  }
}
