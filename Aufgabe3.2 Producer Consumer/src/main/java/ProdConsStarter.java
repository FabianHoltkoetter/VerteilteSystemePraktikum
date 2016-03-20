import java.util.ArrayList;
import java.util.List;

public class ProdConsStarter {
    private static final String ERROR_PARAMETERS = "Please specify the numbers of how many producers and cnsumers you want to start.";

    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException(ERROR_PARAMETERS);
        }

        int producerCount;
        int consumerCount;
        try {
            producerCount = Integer.parseInt(args[0]);
            consumerCount = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ERROR_PARAMETERS);
        }

        Datastore datastore = new Datastore();
        List<Producer> producers = new ArrayList<>();
        List<Consumer> consumers = new ArrayList<>();
        for (int i = 0; i < producerCount; i++) {
            producers.add(new Producer(datastore));
        }
        for (int i = 0; i < consumerCount; i++) {
            consumers.add(new Consumer(datastore));
        }

        producers.stream().forEach(Thread::start);
        consumers.stream().forEach(Thread::start);
    }

}
