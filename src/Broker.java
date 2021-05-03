import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.function.Consumer;

@SuppressWarnings("all")

public class Broker extends Node {

    ArrayList<Consumer> registeredUsers = new ArrayList<>();
    ArrayList<Publisher> registeredPublishers = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket providerSocket;
        Socket connection = null;

        try {
            providerSocket = new ServerSocket(1441, 10);
            System.out.println("The server is open at port: " + 1441);
            System.out.println("Server socket created.Waiting for connection...");

            //nonit
            while (true) {
                try {
                    //Client connection
                    connection = providerSocket.accept();
                    Thread handler = new Handler(connection);
                    handler.start();
                    System.out.println("A new client was connected");

                } catch (Exception e) {
                    System.err.println("IOException");
                }

            }
        } catch (IOException e) {
            System.err.println("IOException");
        }

    }

    public static class Handler extends Thread{
        BufferedReader bf;
        DataOutputStream dos;
        PrintWriter pr;
        InputStream in;
        OutputStream out;
        Socket consumer;
        PublisherHandler ph;
        ConsumerHandler ch;

        public Handler(Socket so){
                consumer = so;
                ch = new ConsumerHandler(this);
                ph = new PublisherHandler(this);
                ch.start();
                ph.start();
            try{
                ch.join();
                ph.join();
                bf = new BufferedReader(new InputStreamReader(so.getInputStream()));
                dos = new DataOutputStream(so.getOutputStream());
                pr= new PrintWriter(so.getOutputStream(),true);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
/*    @Override
    public List<Broker> getBrokers() {
        for (int i=0;i<brokers.size();i++)
            System.out.println(brokers.get(i));
        return null;
    }

    @Override
    public void connect() {
        System.out.println("Connection");

    }

    @Override
    public void disconnect() {
        System.out.println("Disconnect");

    }

    @Override
    public void updateNodes() {
        System.out.println("UpdateNodes");

    }*/

    //public void calculateKeys()

    //public Publisher acceptConnection()

    //public Consumer acceptConsumer()

    //public void notifyPublisher(String s)

    //public void notifyBrokersOnChanges()

    //public void pull (String s)

    //public String finalConsumer()

    public String calculateHash(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] messageDigest = md.digest(input.getBytes());

        // Convert byte array into signum representation
        BigInteger no = new BigInteger(1, messageDigest);

        // Convert message digest into hex value
        String hashtext = no.toString(16);

        // Add preceding 0s to make it 32 bit
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }

        // return the HashText
        return hashtext;
    }

    public static class ConsumerHandler extends Thread{
        Handler parent;

        public ConsumerHandler(Handler parent){
            this.parent = parent;

        }
        @Override
        public void run(){

        }
    }

    public static class PublisherHandler extends Thread{
        Handler parent;

        public PublisherHandler(Handler parent){
            this.parent = parent;

        }
        @Override
        public void run(){

        }
    }
}


