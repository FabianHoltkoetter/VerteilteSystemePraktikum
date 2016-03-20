import java.util.Random;

class Producer extends Thread{

    private static final int MAX_WAITTIME = 1500;
    private static final int MIN_WAITTIME = 200;
    private final Datastore datastore;
    private int runningNumber = 0;

    Producer(Datastore datastore){
        this.datastore = datastore;
    }

    @Override
    public void run() {
        while (true){
            try{
                Thread.sleep(new Random().nextInt((MAX_WAITTIME - MIN_WAITTIME) + 1) + MIN_WAITTIME);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
            System.out.println(String.format("Producer %d adding Data.", this.getId()));
            datastore.addData(String.format("Producer: %d, Number: %d", this.getId(), runningNumber++));
        }
    }
}
