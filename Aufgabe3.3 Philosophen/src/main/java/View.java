import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.SynchronousQueue;

/**
 * Organization: HM FK07.
 * Project: VerteilteSystemePraktikum, PACKAGE_NAME
 * Author(s): Rene Zarwel + Fabian Holtkötter
 * Date: 06.04.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
public class View extends Thread implements Observer{

    private SynchronousQueue<String> queue = new SynchronousQueue<>();


    @Override
    public void update(Observable o, Object arg) {
        Philosopher philosopher = (Philosopher)o;

        StringBuilder builder = new StringBuilder(philosopher.PREFIX);

        if(philosopher.isEating())
            builder.append("\tGabel links: ")
                    .append(philosopher.getLeftForkNumber())
                    .append("\tSitz: ")
                    .append(philosopher.getSeatNumber())
                    .append("\tGabel rechts: ")
                    .append(philosopher.getRightForkNumber())
                    .append("\t Gegessen: ")
                    .append(philosopher.getEatCounter());
        else
            builder.append("\t hat aufgehört zu essen!");
        
        builder.append("\n\n");

        try {
            queue.put(builder.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(){

        while (true)
            try {
                System.out.println(queue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

    }
}
