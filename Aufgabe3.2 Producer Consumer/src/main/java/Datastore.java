import java.util.LinkedList;
import java.util.Queue;

/**
 * Datastore puffert die produzierten Daten
 * und kümmert sich im die Synchronisierung
 */
class Datastore{
    /** Daten verfügbar **/
    private boolean available = false;
    /** Gepufferte Daten **/
    private final Queue<String> datastore = new LinkedList<>();

    /**
     * Daten Consumieren.
     * @return
     */
    synchronized String getData() {
        //Warten bis Daten verfügbar
        while (!available) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //Daten konsumieren
        String value =  datastore.remove();
        available = !datastore.isEmpty();
        notify();
        return value;
    }

    /**
     * Daten produzieren
     * @param newData
     */
    synchronized void addData(String newData) {
        datastore.add(newData);
        available = true;
        notify();
    }
}
