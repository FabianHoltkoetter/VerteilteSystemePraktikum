package philosopher;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Fabian on 08.06.2016.
 */
public class StartPhilosopher {

    private static final Logger LOG = Logger.getLogger(StartPhilosopher.class.getName());


    public static void main(String[] args) {

        if(args.length == 1) {

            PhilosopherImpl p  = new PhilosopherImpl(args[0]);
            new Thread(p).start();

        } else {
            LOG.log(Level.INFO, "No IP provided in args");
        }
    }
}
