import java.io.*;
import java.util.*;
import java.net.ServerSocket;
import java.net.Socket;
import org.json.JSONArray;
import org.json.JSONObject;

public class Publisher extends Node {

    private ChannelName channelName;
    public ArrayList <String> hashtags = new ArrayList<>();
    ObjectOutputStream oos;
    ObjectInputStream ois;
    String username,hash;

    public Publisher(ChannelName username,String hash){
        channelName = username;
        this.hash = hash;
    }


    public static void main(String[] args) throws Exception{
        int port;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            System.err.println("Args Input error");
            return;
        }

        Publisher publisher = new Publisher();
        ServerSocket s = new ServerSocket(port);

        while(true){
            Socket conn = s.accept();
            System.out.println("A new broker was connected");
            Handler handler = new Handler(conn, publisher);
            handler.start();
        }
    }

    public void addHashtag(String hashtag){
        if (!hashtags.contains(hashtag))
            hashtags.add(hashtag);
        else
            System.out.println("hashtag already exists.");
    }

    public void removeHashtag (String hashtag){
        if (hashtags.contains(hashtag))
            hashtags.remove(hashtag);
        System.out.println("hashtag wasn't removed.");
    }


    public static class Handler extends Thread{

        Socket brokerSocket;
        Publisher pu;

        public Handler(Socket conn, Publisher pu){
            this.brokerSocket = conn;
            this.pu = pu;
        }

        public void run() {

            // Get broker request
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(brokerSocket.getInputStream()));
                PrintWriter pr = new PrintWriter(brokerSocket.getOutputStream(), true);
                OutputStream out = brokerSocket.getOutputStream();
                String responseString = br.readLine(); // Wait here for request
                JSONObject request = new JSONObject(responseString);
                String action = request.getString("action");
                if (action.equalsIgnoreCase("GET_VIDEO")){
                    String hashtag = request.getString("Hashtag");
                    String channelname = request.getString("Channelname");

                    VideoFile video = getVideoByHashtag(hashtag);

                    if (video == null){
                        System.out.println("No video with that hashtag");
                        return;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        public VideoFile getVideoByHashtag(String Hashtag){
            return new VideoFile();
        }

    //public Broker hashTopic(String hashTopic)

    //public void push (String,Value)

    //public void notifyFailure (Broker b)

    //public void notifyBrokersForHashtags(String)

    // public ArrayList<Value> generateChunks (String)



    }
}
