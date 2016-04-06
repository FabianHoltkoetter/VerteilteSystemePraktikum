/**
 * Created by Fabian on 30.03.2016.
 */
public class Restaurant {
    private static final String ERROR_PARAMETERS = "Please specify the numbers of how many philosophers and seats you want to start.";

    public static void main(String[] args){
        if (args.length != 2) {
            throw new IllegalArgumentException(ERROR_PARAMETERS);
        }

        int philosophersCount;
        int seatCount;
        try {
            philosophersCount = Integer.parseInt(args[0]);
            seatCount = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ERROR_PARAMETERS);
        }

        DiningTable table = new DiningTable(seatCount);
        for(int i = 0; i < philosophersCount; i++) {
            new Philosopher(i, table).start();
        }
    }

}
