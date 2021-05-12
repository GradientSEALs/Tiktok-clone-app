import java.io.*;
import java.net.*;
import java.util.*;


@SuppressWarnings("all")
public class Broker extends Node {

    public ArrayList<String> brokerhashtag = new ArrayList<>();
    public ArrayList<String> brokerchannelnameslist = new ArrayList<>();
    public ArrayList<VideoFile> VideosPublisherConnection = new ArrayList<>();




    public volatile HashMap<String,ArrayList<VideoFile>> channelContent = new HashMap<>();
    public Map<String,Util.Pair<InetAddress,Integer>> VideoOwnerConnection = new HashMap<>();
    public Map<Integer,Util.Pair<String, Integer>> ListOfBrokers = new TreeMap<>();
    public HashSet<String> hashTags = new HashSet<>();
    public HashSet<String> channels = new HashSet<>();

    public HashMap<Integer,HashSet<String>> fellowBrokersInfo = new HashMap<>();



    InetAddress owneraddress;
    int ownerport;
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
        private boolean exit = false;
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
                while (!exit) {

                    byte choice = ois.readByte(); //we take the choice
                    switch (choice) {
                        case 1: //register
                            String channelName = (String) ois.readObject();
                            System.out.println(channelName);
                            int channelHash = ois.readInt();
                            channelHash /= 3;
                            System.out.println(hashid + "====" + channelHash);
                            for (int brokerID : ListOfBrokers.keySet()) {
                                if (channelHash < brokerID && brokerID == hashid) {
                                    System.out.println("Writing true");
                                    oos.writeBoolean(true);
                                    oos.flush();
                                    System.out.println("Adding to list");
                                    brokerchannelnameslist.add(channelName);
                                    System.out.println(ListOfBrokers.keySet().toString());
                                    oos.writeObject(ListOfBrokers);
                                    oos.flush();
                                    break;
                                } else if (channelHash < brokerID) {
                                    System.out.println("To next Broker");
                                    oos.writeBoolean(false);
                                    oos.flush();
                                    oos.writeObject(ListOfBrokers.get(brokerID));
                                    oos.flush();
                                    _stop();
                                    System.out.println("Left");
                                    oos.writeObject(ListOfBrokers);
                                    oos.flush();
                                    break;
                                } else continue;
                            }
                            oos.writeObject(ListOfBrokers);
                            oos.flush();
                            break;
                        case 2: //publish a video
                            Util.debug("Reading channel name and videoname");
                            channelName = (String) ois.readObject();
                            VideoFile video = (VideoFile) ois.readObject();
                            VideosPublisherConnection.add(video);
                            String hashtag = (String) ois.readObject();
                            Util.debug("adding to map");
                            Util.debug(channelName);
                            VideoOwnerConnection.put(video.getVideoName(),new Util.Pair<InetAddress,Integer>(conn.getLocalAddress(), conn.getPort()));
                            channelContent.computeIfAbsent(channelName, k -> new ArrayList<VideoFile>()).add(video);
                            hashTags.add(hashtag);
                            Util.debug("Added hashtag to broker's hashtag");
                            channels.add(video.getChannelName());
                            Util.debug("Added channel to broker's channel's");
                            oos.writeBoolean(true); //says to the AppNode that we were notified
                            oos.flush();
                            System.out.println("I just returned notify");


                            break;
                        case 3: //find a video or a channel and deliver the video
                            byte action = ois.readByte();
                            System.out.println(action);
                            if (action == 1) {
                                ArrayList<String> hashtagnames = new ArrayList<>();
                                for (String s : hashTags) {
                                    hashtagnames.add(s);
                                }
                                oos.writeObject(hashtagnames);
                                oos.flush();
                            } else {
                                ArrayList<String> channelnames = new ArrayList<>();
                                for (String s : channels) {
                                    channelnames.add(s);
                                }
                                oos.writeObject(channelnames);
                                oos.flush();
                            }


                            break;
                        case 4: //subscribe customer to a hashtag or channel
                            break;

                        case 5: //process of returning channels or hashtags requested by AppNodes

                            String desired = (String) ois.readObject();
                            HashSet<String> interestingvideos = new HashSet<>();
                            for (VideoFile v : VideosPublisherConnection) {
                                if (v.getChannelName().equals(desired) || v.associatedHashtags.contains(desired)) {
                                    //checks if we want this video
                                    interestingvideos.add(v.getVideoName());

                                }

                            }
                            System.out.println(interestingvideos);
                            oos.writeObject(interestingvideos);
                            oos.flush();


                            break;


                        case 6: // process to send the video

                            String filename = (String) ois.readObject();
                            boolean exists = false;

                            for(VideoFile v: VideosPublisherConnection){
                                if(v.videoName.equals(filename)){
                                    exists=true;
                                    Util.Pair<InetAddress,Integer> contact = VideoOwnerConnection.get(v.videoName);
                                     owneraddress = contact.item1;
                                     ownerport = contact.item2;
                                    break;
                                }
                            }
                            oos.writeBoolean(exists);
                            oos.flush();
                            if(exists){
                                Socket ownersocket = new Socket(owneraddress,ownerport);






                            }



                            break;
                    }
                }
            }
            catch (EOFException e){
                System.out.println("Exeis EOFE");
            }
            catch(ClassNotFoundException | IOException e){
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
        public void _stop()
        {
            exit = true;
        }
    }
    public void init(String ip,String port, String pathname) throws UnknownHostException {
        hashid = Util.getModMd5(ip+","+port);
        this.port = Integer.parseInt(port);
        this.ipaddress = InetAddress.getByName(ip);
        ListOfBrokers.put(hashid,new Util.Pair<String,Integer>(ip,this.port));
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


