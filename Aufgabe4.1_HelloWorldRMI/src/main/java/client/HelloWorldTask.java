package client;

import api.Task;

import java.io.Serializable;

/**
 * Created by Fabian on 18.05.2016.
 */
public class HelloWorldTask implements Task<String>, Serializable {

    private static final long serialVersionUID = 1337;

    public String execute() {
        String x = "Hello World!";
        System.out.println(x);
        return x;
    }

}
