import java.util.Observable;

/**
 * Organization: HM FK07.
 * Project: VerteilteSystemePraktikum, PACKAGE_NAME
 * Author(s): Rene Zarwel + Fabian Holtkötter
 * Date: 06.04.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
public class Philosopher extends Observable implements Runnable {

    public static final int MEALS_BEFORE_SLEEP = 3;
    public static final int MEDITATION_TIME = 5;
    public static final int EAT_TIME = 1;
    public static final int SLEEP_TIME = 10;

    public final String PREFIX;
    public final int id;
    public boolean allowedToEat = true;

    private final DiningTable diningTable;

    private int seatNumber = -1;
    private int leftForkNumber = -1;
    private int rightForkNumber = -1;
    private int eatCounter = 0;

    private int meditationTime = MEDITATION_TIME;

    public Philosopher(int id, DiningTable diningTable, boolean hungry) {

        this.diningTable = diningTable;
        this.id = id;

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < id; i++)
            builder.append("\t\t\t\t");
        builder.append("P");
        builder.append(id);

        PREFIX = builder.toString();

        if(hungry)
            meditationTime /= 3;
    }

    @Override
    public void run() {
        int seatBuffer;

        while (!Thread.currentThread().isInterrupted()) {

            try {
                // Meditate
                Thread.sleep(meditationTime);
            } catch (InterruptedException e) {
                break;

            }

            if(isAllowedToEat()) {

                // Go to table
                diningTable.takeSeat(this);

                try {
                    // Eat
                    Thread.sleep(EAT_TIME);
                    setEatCounter(getEatCounter() + 1);
                } catch (InterruptedException e) {
                    break;

                }


                //Leave table
                seatBuffer = getSeatNumber();
                releaseAll();
                diningTable.leaveSeat(seatBuffer);


                if (eatCounter == MEALS_BEFORE_SLEEP) {
                    //Sleep after eatCount meals
                    try {
                        Thread.sleep(SLEEP_TIME);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        }
    }

    public boolean isEating(){
        return getLeftForkNumber() >= 0 &&
                getRightForkNumber() >= 0 &&
                getSeatNumber() >= 0;
    }

    public void takeAll(int seatNumber, int leftForkNumber, int rightForkNumber){
        setSeatNumber(seatNumber);
        setLeftForkNumber(leftForkNumber);
        setRightForkNumber(rightForkNumber);
        setChanged();
        notifyObservers();
    }

    private void releaseAll(){
        setSeatNumber(-1);
        setLeftForkNumber(-1);
        setRightForkNumber(-1);
        setChanged();
        notifyObservers();
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public int getLeftForkNumber() {
        return leftForkNumber;
    }

    public void setLeftForkNumber(int leftForkNumber) {
        this.leftForkNumber = leftForkNumber;
    }

    public int getRightForkNumber() {
        return rightForkNumber;
    }

    public void setRightForkNumber(int rightForkNumber) {
        this.rightForkNumber = rightForkNumber;
    }

    public int getEatCounter() {
        return eatCounter;
    }

    public synchronized void setEatCounter(int eatCounter) {
        this.eatCounter = eatCounter;
    }

    public boolean isAllowedToEat() {
        return allowedToEat;
    }

    public synchronized void setAllowedToEat(boolean allowedToEat) {
        this.allowedToEat = allowedToEat;
    }

    @Override
    public String toString() {
        return "Philosopher " + id + " got " + getEatCounter() + " meals";
    }
}
