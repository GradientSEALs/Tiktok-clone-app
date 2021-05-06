import com.uwyn.jhighlight.fastutil.Hash;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;


@SuppressWarnings("all")
public class Broker extends Node {

    public ArrayList<String> brokerhashtag = new ArrayList<>();
    public ArrayList<String> brokerchannelnameslist = new ArrayList<>();
    public ArrayList<VideoFile> VideosPublisherConnection = new ArrayList<>();


    public HashMap<Integer,Util.Pair<String, Integer>> ListOfBrokers = new HashMap<>();
    public HashSet<String> hashTags = new HashSet<>();
    public HashSet<String> channels = new HashSet<>();

    public HashMap<Integer,HashSet<String>> fellowBrokersInfo = new HashMap<>();




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
            Broker broker = new Broker();
            broker.init(args[0],args[1],args[2]);
            broker.run();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void run() throws UnknownHostException {
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
                    System.out.println(choice);
                    switch (choice) {
                        case 1: //register
                            System.out.println("12");
                            String channelName = (String) ois.readObject();
                            System.out.println(channelName);
                            int channelHash = ois.readInt();
                            System.out.println(hashid +"===="+ channelHash);
                            if (channelHash < hashid) {
                                System.out.println("1");
                                oos.writeBoolean(true);
                                oos.flush();
                                System.out.println("1");
                                brokerchannelnameslist.add(channelName);
                                System.out.println(ListOfBrokers.keySet().toString());//puts channel in broker list
                            }else{
                                for (Integer broker_hash : ListOfBrokers.keySet()){
                                    if (channelHash < broker_hash){
                                        System.out.println("1");
                                        oos.writeBoolean(false);
                                        oos.writeObject(ListOfBrokers.get(broker_hash));
                                        oos.flush();
                                        System.out.println("1");
                                        break;
                                    }
                                }
                            }
                            break;
                        case 2: //publish a video
                            String videoHashtag = ois.readUTF();
                            int videoHashID = Util.getModMd5(videoHashtag);
                            videoHashID %= 3;
                            boolean appropriate = false;
                            if (videoHashID < hashid) {
                                oos.writeBoolean(true);
                                oos.flush();
                                appropriate = true ;
                            }
                            else {
                                for (Integer broker_hash : ListOfBrokers.keySet()) {
                                    if (videoHashID < broker_hash) {
                                        oos.writeBoolean(false);
                                        oos.flush();
                                        oos.writeObject(ListOfBrokers.get(broker_hash));
                                        oos.flush();
                                        break;
                                    }
                                }
                            }
                            if (!appropriate)
                                break;
                            else{
                                VideoFile newVideo = (VideoFile) ois.readObject();
                                hashTags.add(videoHashtag);
                                VideosPublisherConnection.add(newVideo);
                            }
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
    public void init(String ip,String port, String pathname) throws UnknownHostException {
        hashid = Util.getModMd5(ip+":"+port);
        this.port = Integer.parseInt(port);
        this.ipaddress = InetAddress.getByName(ip);
        try {
            File f = new File("brokers/" + pathname);
            Scanner myReader = new Scanner(f);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                int otherHash = Util.getModMd5(data);
                String[] data2 = data.split(",");
                int brokerport = Integer.parseInt(data2[1]);
                String brokerip = data2[0];
                ListOfBrokers.put(otherHash,new Util.Pair<>(brokerip,brokerport));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

/*    public void update() {
        try {
            fellowBrokersInfo.clear();
            for (Util.Pair<String, Integer> broker_info : ListOfBrokers) {
                Socket fellob = new Socket(broker_info.item1, broker_info.item2);
                ObjectInputStream ois = new ObjectInputStream(fellob.getInputStream());
                HashSet<String> brokerHash = (HashSet<String>) ois.readObject();
                int id = Util.getModMd5(broker_info.item1 + broker_info.item2.toString());
                fellowBrokersInfo.put(id, brokerHash);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/

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

}


