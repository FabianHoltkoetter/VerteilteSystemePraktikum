import java.util.LinkedList;
import java.util.Queue;

class Datastore{
    private boolean available = false;
    private final Queue<String> datastore = new LinkedList<>();

    synchronized String getData() {
        while (!available) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String value =  datastore.remove();
        available = !datastore.isEmpty();
        notifyAll();
        return value;
    }

    synchronized void addData(String newData) {
        datastore.add(newData);
        available = true;
        notifyAll();
    }
}
