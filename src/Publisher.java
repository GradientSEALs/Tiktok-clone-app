import java.io.*;
import java.util.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;

import org.apache.tika.exception.TikaException;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

public class Publisher extends Thread {

    private String folder;
    private ChannelName channelName;
    volatile ArrayList<VideoFile> videoFiles = new ArrayList<>();
    private static int port;
    Socket broker = null;
    BufferedReader br;
    PrintWriter pr;
    ObjectOutputStream out;
    ObjectInputStream in;
    String name;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    public Publisher(ChannelName username, String folder, Socket broker){
        this.folder = folder;
        channelName = username;
        this.broker = broker;
    }

    public Publisher(String name,int port) {
        this.name = name;
        this.port = port;
        channelName = new ChannelName(name);
    }



    @Override
    public void run() {
        try {
            Node n = new Node();
            loadAvailableFiles(folder, channelName.getChannelName());
            ServerSocket s = new ServerSocket(port);
            Scanner skr = new Scanner(System.in);
            System.out.println("Please give file name");
            String fileName = skr.nextLine();
            System.out.println("Please give hashtags(separate with a comma)");
            String hashtag = skr.nextLine();
            String [] hashtags = hashtag.split(",");
            String hash = hashtags[0];
            int hashid = Util.getModMd5(hash);
            //connection with appropriate broker
            int brokerPort;
            ArrayList<Broker> brokers = new ArrayList<Broker>();
            brokers = n.getBrokers(); //loads the list of brokers
            boolean exists = false;
            for (Broker b: brokers){ //finds the broker that already has the hashtag
                    if (b.getHashtags().contains(hashtag)) {
                        broker = new Socket(b.getBrokerIP(), b.getPort());
                        exists = true;
                }
            }
            if (!exists){
                for (Broker b: brokers) {
                    if (hashid <= b.getHashID()) { //checks if hashid smaller than broker hashid
                        broker = new Socket(b.getBrokerIP(), b.getPort());
                        break;
                    }


                }
                //we have to create new hashtag to brokers
            }
            oos = new ObjectOutputStream(broker.getOutputStream());
            ois = new ObjectInputStream(broker.getInputStream());

            oos.writeObject(2); //sending choice to the correct broker
            oos.flush();

            if(!exists){
                oos.writeByte(1);
                oos.flush();
                oos.writeObject(hash);
                System.out.println("database updated");

            }







            /*VideoFile video = null;
            for (VideoFile v : videoFiles){
                if (v.videoName.equals(fileName)) {
                    video = v;
                    break;
                }
            }
            video.addHastags(hashtags);
            uploadVideo(video,broker);*/
            //notifyBrokers
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadVideo(VideoFile videoFile,Socket broker) throws IOException {
        byte[] videoData = Util.loadVideoFromDiskToRam(videoFile);
        List<byte[]> chunckedVideo = Util.chunkifyFile(videoData);
        out = (ObjectOutputStream) broker.getOutputStream();
        for (byte[] data: chunckedVideo){
            out.write(data);
            out.flush();
        }
    }

    public void loadAvailableFiles(String folder, String channel){
        File directory = new File(folder);
        File[] contents = directory.listFiles();
        for ( File f : contents) {
            if (f.getName().endsWith(".mp4")) {
                try {
                    videoFiles.add(new VideoFile(f.getName(),channel,folder));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    //public Broker hashTopic(String hashTopic)

    //public void push (String,Value)

    //public void notifyFailure (Broker b)

    //public void notifyBrokersForHashtags(String)

    // public ArrayList<Value> generateChunks (String)

}
