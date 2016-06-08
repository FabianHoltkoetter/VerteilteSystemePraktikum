package philosopher;

import Recovery.RecoveryImpl;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Fabian on 08.06.2016.
 */
public class StartPhilosopher {

  private static final Logger LOG = Logger.getLogger(StartPhilosopher.class.getName());


  public static void main(String[] args) {

    if (args.length == 1) {

      String ip = args[0];

      PhilosopherImpl p = new PhilosopherImpl(ip);
      new Thread(p).start();


      RecoveryImpl.startRecovery(ip);

    } else {
      LOG.log(Level.INFO, "No IP provided in args");
    }
  }
}
