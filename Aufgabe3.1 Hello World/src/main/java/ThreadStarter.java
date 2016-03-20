public class ThreadStarter {

    private static final String MESSAGE = "Hello World!";
    private static final String ERROR_PARAMETERS = "Please specify the number of threads you want to start.";

    public static void main(String[] args){
        if(args.length != 1){
            throw new IllegalArgumentException(ERROR_PARAMETERS);
        }

        int threadcount;
        try{
            threadcount = Integer.parseInt(args[0]);
        } catch (NumberFormatException e){
            throw new IllegalArgumentException(ERROR_PARAMETERS);
        }

        for(int i = 0; i < threadcount; i++) {
            new Thread(() -> System.out.println(MESSAGE)).start();
        }
    }

}
