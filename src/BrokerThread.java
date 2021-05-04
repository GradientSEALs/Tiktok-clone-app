import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.function.Consumer;

public class BrokerThread {



    volatile ArrayList<Consumer> registeredConsumers = new ArrayList<>();
    volatile ArrayList<Publisher> registeredPublishers = new ArrayList<>();
    volatile ArrayList<String> hashtags = new ArrayList<>();
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
    public void acceptConsumer(){
        String ipAddress = super.getIpAddress();
        int port = super.getPort();
        boolean exists = registeredConsumers.contains(ipAddress);
        try{
            if (exists){
                oos.writeByte(-1);
                oos.flush();
                System.out.println("This user already exists");
            }
            else{
                //registeredC
                oos.writeByte(1);
                oos.flush();
                System.out.println("A new user just registered with ip " + ipAddress);
                //System.out.println(registeredPeers.get(username).toString());

            }
        } catch (IOException e) {
            e.printStackTrace();
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
