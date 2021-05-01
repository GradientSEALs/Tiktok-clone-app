import java.io.IOException;
import java.util.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Broker extends Node {

    List<Consumer> registeredUsers = new List<>();
    List<Publisher> registeredPublishers = new List<>();

    public static void main(String[] args) throws Exception{
        int port = Integer.parseInt(args[0]);
        ServerSocket s = new ServerSocket(port);


        while(true){
            Socket so = s.accept();
            Handler handler = new Handler(so);
            handler.start();
            System.out.println("A new client was connected");
        }
    }


    public static class Handler extends Thread{
        ConsumerHandler consumerh;
        PublisherHandler publisherh;
        Socket so;

        public Handler(Socket so){
            this.so =so;
        }

        public void run(){
            consumerh = new ConsumerHandler(this);
            publisherh = new PublisherHandler(this);
            consumerh.start();
            publisherh.start();

            try{
                consumerh.join();
                publisherh.join();
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
        }


    }

    public static class PublisherHandler extends Thread {
        Handler parent;

        public PublisherHandler(Handler parent) {
            this.parent = parent;
        }

        @Override
        public void run() {

        }
    }

    public static class ConsumerHandler extends Thread {
        Handler parent;

        public ConsumerHandler(Handler parent) {
            this.parent = parent;
        }

        @Override
        public void run() {

        }
    }

    //public void calculateKeys()

    //public Publisher acceptConnection()

    //public Consumer acceptConsumer()

    //public void notifyPublisher(String s)

    //public void notifyBrokersOnChanges()

    //public void pull (String s)

    //public String finalConsumer()




}
