import java.io.*;
import java.util.*;
import java.net.Socket;
import org.json.JSONException;
@SuppressWarnings("all")
public class Publisher extends Thread {

    private String folder;
    private final String channelName;
    volatile ArrayList<VideoFile> videoFiles = new ArrayList<>();
    Socket broker = null;
    String name;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    public Publisher(String username, String folder, Socket broker){
        this.folder = folder;
        channelName = username;
        this.broker = broker;
    }

    public Publisher(String name,int port) {
        this.name = name;
        channelName = name;
    }

    @Override
    public void run() {
        loadAvailableFiles(folder, channelName);
        Scanner skr = new Scanner(System.in);
        System.out.println(videoFiles);
        System.out.println("Please give file name");
        String fileName = skr.nextLine();
        System.out.println("Please give hashtags(separate with a comma)");
        String hashtag = skr.nextLine();
        try {
            findAppropriateBroker(channelName);
            notify(broker,new VideoFile(fileName,channelName,folder));
        }catch (ClassNotFoundException | IOException | JSONException e){
            e.printStackTrace();
        }
        String [] hashtags = hashtag.split(",");
        for (String hash: hashtags){
            try {
                findAppropriateBroker(hash);
                notify(broker,new VideoFile(fileName,channelName,folder));
            } catch (IOException | ClassNotFoundException | JSONException e) {
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
        File directory = new File(folder);
        File[] contents = directory.listFiles();
        assert contents != null;
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

    public boolean notify(Socket broker, VideoFile video) throws IOException {
        boolean notified = false;
        oos = new ObjectOutputStream(broker.getOutputStream());
        ois = new ObjectInputStream(broker.getInputStream());
        oos.writeObject(video);
        oos.flush();
        notified = ois.readBoolean();
        oos.close();
        ois.close();
        return notified;
    }

    public void findAppropriateBroker(String str) throws IOException, ClassNotFoundException {
        ois = new ObjectInputStream(broker.getInputStream());
        oos = new ObjectOutputStream(broker.getOutputStream());
        oos.writeInt(2);
        oos.flush();
        oos.writeChars(str);
        boolean appropriate = ois.readBoolean();
        if (appropriate){
            return;
        }
        Util.Pair<String,Integer> newBrokerInfo = (Util.Pair<String,Integer>)ois.readObject();
        ois.close();
        oos.close();
        broker.close();
        broker = new Socket(newBrokerInfo.item1, newBrokerInfo.item2);
    }


    //public Broker hashTopic(String hashTopic)

    //public void push (String,Value)

    //public void notifyFailure (Broker b)

    //public void notifyBrokersForHashtags(String)

    // public ArrayList<Value> generateChunks (String)

}
