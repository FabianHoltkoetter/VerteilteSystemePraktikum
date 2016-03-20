class Consumer extends Thread {

    private Datastore datastore;

    Consumer(Datastore datastore) {
        this.datastore = datastore;
    }

    @Override
    public void run() {
        while (true) {
            String value = datastore.getData();
            System.out.println(String.format("Consumer %d received Data: \"%s\"", this.getId(), value));
        }
    }

}
