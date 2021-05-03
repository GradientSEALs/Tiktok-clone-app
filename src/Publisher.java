import java.io.*;
import java.util.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.tika.exception.TikaException;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

public class Publisher extends Thread {


    private ChannelName channelName;
    public ConcurrentLinkedQueue<VideoFile> videoFiles = new ConcurrentLinkedQueue<>();
    private static int port;
    BufferedReader br;
    PrintWriter pr;
    OutputStream out;

    public Publisher(ChannelName username){
        channelName = username;
    }

    public Publisher() {
        channelName = new ChannelName("Eko");
    }



    @Override
    public void run() {
        try {
            ServerSocket s = new ServerSocket(port);
            Scanner skr = new Scanner(System.in);
            String filePath = skr.nextLine();
            String fileName = skr.nextLine();
            String hashtag = skr.nextLine();
            String [] hashtags = hashtag.split(",");
            VideoFile video = null;
            video.loadVideo(channelName.getChannelName(), filePath,fileName,hashtags);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TikaException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    public void uploadVideo(String path,String name,Socket broker) throws IOException {
        byte[] videoData = Util.loadVideoFromDiskToRam(path,name);
        List<byte[]> chunckedVideo = Util.chunkifyFile(videoData);
        out = broker.getOutputStream();
        for (byte[] data: chunckedVideo){
            out.write(data);
            out.flush();
        }
    }



    //public Broker hashTopic(String hashTopic)

    //public void push (String,Value)

    //public void notifyFailure (Broker b)

    //public void notifyBrokersForHashtags(String)

    // public ArrayList<Value> generateChunks (String)

}
