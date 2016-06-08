package starter;

import api.Philosopher;
import philosopher.PhilosopherImpl;

/**
 * Created by Fabian on 08.06.2016.
 */
public class StartPhilosopher {
    public static void main(String[] args) {
        PhilosopherImpl p  = new PhilosopherImpl("localhost");
        new Thread(p).start();
    }
}
