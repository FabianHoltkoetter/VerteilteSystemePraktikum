package table;

import Recovery.RecoveryImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Rene Zarwel on 10.06.2016.
 */
public class StartTablePart {
  private static final Logger LOG = LogManager.getLogger(StartTablePart.class.getName());


  public static void main(String[] args) {

    if(System.getProperty("java.security.policy") == null || System.getProperty("java.security.policy").isEmpty())
      System.setProperty("java.security.policy","file:./security.policy");

    if (args.length == 1) {

      String ip = args[0];

      new TablePartImpl(ip);

      RecoveryImpl.startRecovery(ip);

    } else {
      LOG.info("No IP provided in args");
    }
  }
}
