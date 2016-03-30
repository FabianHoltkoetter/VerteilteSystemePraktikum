import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Fabian on 30.03.2016.
 */
public class Philosopher {

    public static final int MEDITATION_TIME = 1000;
    public static final int EAT_TIME = 1000;
    public static final int SLEEP_TIME = 1000;

    private int id;
    private ReentrantLock leftFork;
    private ReentrantLock rightFork;
    private int eatCounter = 0;

    public Philosopher(int id, ReentrantLock leftFork, ReentrantLock rightFork) {
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
    }



}
