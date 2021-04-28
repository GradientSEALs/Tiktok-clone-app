import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Handler extends Thread {
    private Socket consumerSocket;
    private Socket publisherSocket;

    public Handler(Socket consumerSocket){
        this.consumerSocket = consumerSocket;
    }


}