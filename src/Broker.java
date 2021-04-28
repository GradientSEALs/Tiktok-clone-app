import java.io.IOException;
import java.util.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Broker extends Node {

    List<Consumer> registeredUsers = new List<>();
    List<Publisher> registeredPublishers = new List<>();

    public static void main(String args[]) throws IOException {
        ServerSocket providerSocket;
        Socket connection = null;

        try {
            providerSocket = new ServerSocket(1441, 10);
            System.out.println("The server is open at port: " + 1441);
            System.out.println("Server socket created.Waiting for connection...");

            while (true) {
                try {
                    //Client connection
                    connection = providerSocket.accept();
                    Thread handler = new Handler(connection);
                    handler.start();

                } catch (Exception e) {
                    System.err.println("IOException");
                }

            }
        } catch (IOException e) {
            System.err.println("IOException");
        }

    }



















}
