import java.io.*;
import java.util.*;
import java.net.Socket;

import org.apache.log4j.helpers.UtilLoggingLevel;
import org.json.JSONException;
@SuppressWarnings("all")
public class Publisher extends Thread {

    Object lock = new Object();
    private String folder;
    private final String channelName;
    volatile ArrayList<VideoFile> videoFiles = new ArrayList<>();
    Socket broker = null;
    String name;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    public Publisher(String username, String folder, Socket broker, ObjectOutputStream oos , ObjectInputStream ois){
        this.folder = folder;
        channelName = username;
        this.broker = broker;
        this.oos = oos;
        this.ois = ois;
    }

    public Publisher(String name,int port) {
        this.name = name;
        channelName = name;
    }

    @Override
    public void run() {
        /*loadAvailableFiles(folder, channelName);
        Scanner skr = new Scanner(System.in);
        videoFiles.forEach((v) -> System.out.println(v));*/
        String fileName = "tsimpouki.mp4";
        System.out.println("Please give hashtags(separate with a comma)");
        String hashtag = "HAWK SNIK PIPES";
        try {
            //findAppropriateBroker(channelName);
            Util.debug("ton pairneis ston tetatragono");
            notify(broker, new VideoFile(fileName, channelName, folder));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] hashtags = hashtag.split(" ");
        System.out.println(hashtag.length());
        for (String hash : hashtags) {
            //Util.debug(hash);
            try {
                findAppropriateBroker(hash);
                notify(broker, new VideoFile(fileName, channelName, folder));
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        try {
            broker.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void push(VideoFile videoFile,Socket consumer) throws IOException {
        byte[] videoData = Util.loadVideoFromDiskToRam(videoFile);
        List<byte[]> chunckedVideo = Util.chunkifyFile(videoData);
        oos = (ObjectOutputStream) consumer.getOutputStream();
        for (byte[] data: chunckedVideo){
            oos.write(data);
            oos.flush();
        }
    }

    public void loadAvailableFiles(String folder, String channel){
        File directory = new File(""+folder);
        File[] contents = directory.listFiles();
        if (contents == null){
            System.out.println("DEN VRIKA TPT AFENTIKO");
            return;
        }
        for ( File f : contents) {
            if (f.getName().endsWith(".mp4")) {
                videoFiles.add(new VideoFile(f.getName(),channel,folder));
            }
        }

    }

    public boolean notify(Socket broker, VideoFile video) throws IOException {
        boolean notified = false;
        Util.debug("ton pairneis");
        if (ois == null && oos == null){
            Util.debug("GIATI KLAIEI O MIKROS");
            ois = new ObjectInputStream(broker.getInputStream());
            oos = new ObjectOutputStream(broker.getOutputStream());
        }
        Util.debug("Writing name");
        oos.writeByte(2);
        oos.flush();
        oos.writeObject(channelName);
        oos.flush();
        Util.debug("Writing video");
        oos.writeObject(video);
        oos.flush();
        Util.debug("Reading response");
        notified = ois.readBoolean();
        return notified;
    }

    public void findAppropriateBroker(String str) throws IOException, ClassNotFoundException {
        if (ois == null && oos == null){
            ois = new ObjectInputStream(broker.getInputStream());
            oos = new ObjectOutputStream(broker.getOutputStream());
        }
        oos.writeObject(str);
        boolean appropriate = ois.readBoolean();
        if (appropriate){
            return;
        }
        Util.Pair<String,Integer> newBrokerInfo = (Util.Pair<String,Integer>)ois.readObject();
       /* ois.close();
        oos.close();*/

        broker = new Socket(newBrokerInfo.item1, newBrokerInfo.item2);
        oos = new ObjectOutputStream(broker.getOutputStream());
        ois = new ObjectInputStream(broker.getInputStream());
    }


    //public Broker hashTopic(String hashTopic)

    //public void push (String,Value)

    //public void notifyFailure (Broker b)

    //public void notifyBrokersForHashtags(String)

    // public ArrayList<Value> generateChunks (String)

}
