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
    Socket broker = new Socket();
    BufferedReader br;
    PrintWriter pr;
    OutputStream out;
    ObjectInputStream in;

    public Publisher(ChannelName username, String folder, Socket broker){
        this.folder = folder;
        channelName = username;
        this.broker = broker;
    }

    public Publisher() {
        channelName = new ChannelName("Eko");
    }



    @Override
    public void run() {
        try {
            loadAvailableFiles(folder, channelName.getChannelName());
            ServerSocket s = new ServerSocket(port);
            Scanner skr = new Scanner(System.in);
            while(true){
                //printfiles
                String fileName = skr.nextLine();
                String hashtag = skr.nextLine();
                String [] hashtags = hashtag.split(",");
                VideoFile video = null;
                for (VideoFile v : videoFiles){
                    if (v.videoName.equals(fileName)) {
                        video = v;
                        break;
                    }
                }
                video.addHastags(hashtags);
                uploadVideo(video,broker);
            }

            //notifyBrokers
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadVideo(VideoFile videoFile,Socket broker) throws IOException {
        byte[] videoData = Util.loadVideoFromDiskToRam(videoFile);
        List<byte[]> chunckedVideo = Util.chunkifyFile(videoData);
        out = broker.getOutputStream();
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
