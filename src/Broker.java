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

    public ArrayList<String> brokerhashtag = new ArrayList<>();
    InetAddress ipaddress;
    int hashid;
    int port;
    public static ArrayList<AppNodes> registeredAppNodes = new ArrayList<>();

    public Broker(){}

    @Override
    public String toString() {
        return "Broker{" +
                "brokerhashtag=" + brokerhashtag +
                ", ipaddress=" + ipaddress +
                ", hashid=" + hashid +
                ", port=" + port +
                '}';
    }

    public static void main(String[] args) {
        try {
            new Broker().run();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void run() throws UnknownHostException {
        Random r = new Random();
        port = r.nextInt(8000-4000) + 4000;
        ipaddress = InetAddress.getLocalHost();
        String input = (ipaddress.toString() + ":" +port);
        hashid = Util.getModMd5(input); //create hashid
        ServerSocket serverSocket = null;
        Handler handler;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Port is now open");
            super.brokers.add(this);
            System.out.println(brokers);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client has connected");
                System.out.println("Connection received from " + socket.getInetAddress().getHostName() + " : " + socket.getPort());
                handler = new Handler(socket);
                handler.start();
            }
            //handler.join();



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public InetAddress getBrokerIP(){
        return ipaddress;
    }
    public int getHashID(){return hashid;}
    public ArrayList<AppNodes> getRegisteredAppNodes(){ return registeredAppNodes; }
    public ArrayList<String> getBrokerhashtag(){return brokerhashtag;}

    private class Handler extends Thread {
        private Socket conn;
        public ObjectOutputStream oos;
        public ObjectInputStream ois;

        public Handler(Socket conn) throws IOException {
            this.conn = conn;
            oos = new ObjectOutputStream(conn.getOutputStream());
            ois = new ObjectInputStream(conn.getInputStream());
        }

        public void run(){

            try {

                while (true) {
                    int choice = (int) ois.readObject(); //we take the choice

                    switch (choice) {
                        case 1: //register
                            String channelName = ois.readUTF();
                            if (registeredAppNodes.contains(channelName)) {
                                System.out.println("Channel already exists");
                                oos.writeByte(-1);
                                oos.flush();
                            } else {
                                oos.writeByte(1);
                                oos.flush();
                                AppNodes appn = (AppNodes) ois.readObject();
                                registeredAppNodes.add(appn);
                            }


                            break;
                        case 2: //publish a video
                            byte t = ois.readByte();
                            if(t == 1){
                                String hashtag =(String)ois.readObject();
                                Generalhashtags.add(hashtag);
                                brokerhashtag.add(hashtag);
                                System.out.println("database updated");

                            }

                            break;
                    }
                }

                } catch(ClassNotFoundException | IOException e){
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


