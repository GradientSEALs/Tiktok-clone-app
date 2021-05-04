import java.io.*;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;


@SuppressWarnings("all")
public class Broker extends Node {

    InetAddress ipaddress;

    public Broker(){}

    public static void main(String[] args) throws UnknownHostException {
        new Broker().run();
    }

    public void run() throws UnknownHostException {
        Random r = new Random();
        int port = r.nextInt(8000-4000) + 4000;
        ipaddress = InetAddress.getLocalHost();
        ServerSocket serverSocket = null;
        Node n = new Node();
        try {
            serverSocket = new ServerSocket(port);
            n.brokers.add(this);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client has connected");
                System.out.println("Connection received from " + socket.getInetAddress().getHostName() + " : " + socket.getPort());
                Handler handler = new Handler(socket);
                handler.start();
            }



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public InetAddress getBrokerIP(){
        return ipaddress;
    }


    public static class Handler extends Thread {
        private Socket conn;
        public ObjectOutputStream oos;
        public ObjectInputStream ois;

        public Handler(Socket conn) throws IOException {
            this.conn = conn;
            oos = new ObjectOutputStream(conn.getOutputStream());
            ois = new ObjectInputStream(conn.getInputStream());
        }

        public void run(){

            try{
                int choice = (int) ois.readObject(); //we take the choice

                switch (choice) {
                    case 1:
                        String channelName = ois.readUTF();
                        if(registeredPublishers.contains(channelName)){
                            System.out.println("Channel already exists");
                            oos.writeByte(-1);
                        }
                        else{oos.writeByte(1);}
                        oos.flush();
                        break;

                }
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }

            finally {
                try {
                    ois.close();
                    oos.close();
                    conn.close();
                    //  System.exit(1);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
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


}


