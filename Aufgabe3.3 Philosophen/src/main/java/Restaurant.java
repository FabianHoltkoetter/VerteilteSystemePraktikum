/**
 * Organization: HM FK07.
 * Project: VerteilteSystemePraktikum, PACKAGE_NAME
 * Author(s): Rene Zarwel + Fabian Holtkötter
 * Date: 06.04.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
public class Restaurant {
    private static final String ERROR_PARAMETERS = "Please specify the numbers of how many philosophers and seats you want to start.";

    public static void main(String[] args){
        if (args.length != 2) {
            throw new IllegalArgumentException(ERROR_PARAMETERS);
        }

        //Get phil. and seat count
        int philosophersCount;
        int seatCount;
        try {
            philosophersCount = Integer.parseInt(args[0]);
            seatCount = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ERROR_PARAMETERS);
        }

        //Init all
        DiningTable table = new DiningTable(seatCount);
        View view = new View();
        view.start();

        for(int i = 0; i < philosophersCount; i++) {
            Philosopher philosopher = new Philosopher(i, table);
            philosopher.addObserver(view);

            new Thread(philosopher).start();
        }


    }

}