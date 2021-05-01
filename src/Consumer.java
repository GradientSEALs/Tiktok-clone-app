import java.util.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectOutputStream;

public class Consumer extends Node {
    private ObjectOutputStream buffer;


    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(args[0]);
        ServerSocket s = new ServerSocket(port);


        while (true) {
            Socket so = s.accept();
            Broker.Handler handler = new Broker.Handler(so);
            handler.start();
            System.out.println("A new client was connected");
        }


    }

    public void register(Broker b, String message) {
        try {
            System.out.println("Sending register request...");
            buffer.writeObject(message);
            buffer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static class Handler extends Thread {

        Socket so;

        public Handler(Socket so) {
            this.so = so;
        }

        public void run() {


        }


        //public void disconnect (Broker,String)

        //public void playData (String,Value)
    }
}