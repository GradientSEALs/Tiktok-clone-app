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
import java.util.Scanner;


@SuppressWarnings("all")
public class Broker extends Node {

    public ArrayList<String> brokerhashtag = new ArrayList<>();
    public ArrayList<String> brokerchannelnameslist = new ArrayList<>();
    public ArrayList<VideoFile> VideosPublisherConnection = new ArrayList<>();

    InetAddress ipaddress;
    int hashid;
    int port;
    public ArrayList<AppNodes> registeredAppNodes = new ArrayList<>();

    public Broker(){
    }

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
        String input = (ipaddress.toString() + ":" + port);
        hashid = Util.getModMd5(input); //create hashid
        Node.brokers.add(this);
        System.out.println(Node.brokers);
        ServerSocket serverSocket = null;
        Handler handler;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Port is now open" + port);
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

    public ArrayList<VideoFile> getVideos() {
        return VideosPublisherConnection;
    }

    public ArrayList<String> getBrokerchannelnameslist() {
        return brokerchannelnameslist;
    }

    public int getPort() {
        return port;
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
                            if (brokerchannelnameslist.contains(channelName)) {
                                System.out.println("Channel already exists");
                                oos.writeByte(-1);
                                oos.flush();
                            } else {
                                oos.writeByte(1);
                                oos.flush();
                                AppNodes appn = (AppNodes) ois.readObject();
                                registeredAppNodes.add(appn);   //puts appnode in broker list
                                appnodes.add(appn);         //puts appnode in node list
                                brokerchannelnameslist.add(channelName);  //puts channel in broker list
                                channelnameslist.add(channelName);  //puts channel in node list
                            }


                            break;
                        case 2: //publish a video
                            byte t = ois.readByte();
                            String[] hashtags =(String[])ois.readObject();
                            if(t == 1){
                                Generalhashtags.add(hashtags[0]); ////puts the hashtag in the node hashtag
                                brokerhashtag.add(hashtags[0]); //puts the hashtag in the broker hashtag
                            }

                            channelName = ois.readUTF();
                            String filename = ois.readUTF();
                            AppNodes appn = (AppNodes) ois.readObject();
                            ArrayList<String> hashtagss = new ArrayList<>();
                            for(String has: hashtags){
                                hashtagss.add(has);
;                            }
                            VideosPublisherConnection.add(new VideoFile(filename,channelName,hashtagss, appn)); //adds all information for the video file
                            System.out.println("database updated");
                            break;

                        case 3: //find a video or a channel and deliver the video

                            byte action = ois.readByte();
                            if(action==1){ //shows video with the hashtag that was requested
                                String hashtag = ois.readUTF();
                                ArrayList<VideoFile> interestingvideos= new ArrayList<>();
                                for(VideoFile v:getVideos()){
                                    if(v.getAssociatedHashtags().contains(hashtag)){
                                        interestingvideos.add(v);
                                    }

                                }
                                oos.writeObject(interestingvideos);
                                String request = ois.readUTF(); //handles the request and is responsible for sending the video




                            }
                            else{

                            }
                            break;

                        case 4: //subscrie customer to a hashtag or channel
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


    public void readBrokers(String pathname){

        try {
            File f = new File("tiktok/brokers/" + pathname);
            Scanner myReader = new Scanner(f);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] data2 = data.split(",");
                int brokerport = Integer.parseInt(data2[1]);
                String brokerip = data2[0];
                Socket fellow = new Socket(brokerip,brokerport);


            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}


