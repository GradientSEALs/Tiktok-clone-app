import java.io.*;
import java.util.*;
import java.net.Socket;

@SuppressWarnings("all")
public class Publisher extends Thread {

    VideoFile video;
    Socket connection = null;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    Map<Integer, Util.Pair<String, Integer>> map;

    public Publisher(Socket connection, VideoFile video) {
        this.connection = connection;
        this.video = video;
    }

    @Override
    public void run() {
        try {
            push(video, connection);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void push(VideoFile videoFile, Socket consumer) throws IOException {
        byte[] videoData = Util.loadVideoFromDiskToRam(videoFile);
        List<byte[]> chunckedVideo = Util.chunkifyFile(videoData);
        oos = (ObjectOutputStream) consumer.getOutputStream();
        for (byte[] data : chunckedVideo) {
            oos.write(data);
            oos.flush();
        }
    }
}