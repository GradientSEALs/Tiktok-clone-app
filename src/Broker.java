import netscape.javascript.JSObject;
import org.json.JSONObject;

import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;

@SuppressWarnings("all")

public class Broker extends Node {

    ArrayList<Consumer> registeredConsumers = new ArrayList<>();
    ArrayList<Publisher> registeredPublishers = new ArrayList<>();
    ArrayList<String> hashtags = new ArrayList<>();
    static int port;
    static int ipaddress;
    ObjectOutputStream oos = null;


    public static void main(String[] args) throws IOException {
        new Broker().run();

    }

    public int getPort(){
        return port;
    }

    public ArrayList<String> getHashtags(){
        return hashtags;
    }

    public void run(){
        ServerSocket providerSocket;
        Socket connection = null;

        Random r = new Random();
        port = r.nextInt(8000-3000) + 3000;


        try {
            providerSocket = new ServerSocket(port, 10);
            System.out.println("The server is open at port: " + port);
            System.out.println("Broker created.Waiting for connection...");
            Node.brokers.add(this); //adds himself and the hashtags on the broker list

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
        volatile Socket consumerSocket;
        volatile Socket publisherSocket;
        PublisherHandler ph;
        ConsumerHandler ch;
        BufferedReader bf;
        DataOutputStream dos;
        PrintWriter pr;
        InputStream in;
        OutputStream out;


        public Handler(Socket so){
                //consumer = so;
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

    public static class ConsumerHandler extends Thread {
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
            return;
        }
    }
    public Consumer acceptConsumer(){
        try {
            ObjectOutputStream outputStream;
            String ipAddress = super.getIpAddress();
            int port = super.getPort();
            boolean exists = registeredConsumers.contains(ipAddress);
            if (exists){
                oos.writeByte(-1);
                oos.flush();
                System.out.println("This user already exists");
            }
            else{
                //registeredConsumers.add(ipAddress);
                oos.writeByte(1);
                oos.flush();
                System.out.println("A new user just registered with ip " + ipAddress);
                //System.out.println(registeredPeers.get(username).toString());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    //public void calculateKeys()

    //public Publisher acceptConnection()

    //public Consumer acceptConsumer()

    //public void notifyPublisher(String s)

    //public void notifyBrokersOnChanges()

    //public void pull (String s)

    //public String finalConsumer()
}


