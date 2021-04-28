import java.util.*;
import java.net.ServerSocket;
import java.net.Socket;


public class PublisherHandler extends Thread {
    Handler handler;

    public PublisherHandler(Handler handler) {
        this.handler = handler;
    }


    //public void run() {


}