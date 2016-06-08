package table;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Fabian on 08.06.2016.
 */
public class StartTablePart {
    private static final Logger LOG = Logger.getLogger(StartTablePart.class.getName());


    public static void main(String[] args) {

        if(args.length == 1) {

            new TablePartImpl(args[0]);

        } else {
            LOG.log(Level.INFO, "No IP provided in args");
        }
    }
}
