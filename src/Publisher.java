import java.io.ObjectInputStream;
import java.util.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectOutputStream;

public class Publisher extends Node {

    private ChannelName channelName;
    public ArrayList <String> hashtags = new ArrayList<>();
    ObjectOutputStream oos;
    ObjectInputStream ois;



    public static void main(String[] args) throws Exception{
        int port, noPublisher;
        try {
            port = Integer.parseInt(args[0]);
            noPublisher = Integer.parseInt(args[1]);
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

        Socket conn;
        Publisher pu;

        public Handler(Socket conn, Publisher pu){
            this.conn = conn;
            this.pu = pu;
        }



    }

    //public Broker hashTopic(String hashTopic)

    //public void push (String,Value)

    //public void notifyFailure (Broker b)

    //public void notifyBrokersForHashtags(String)

    // public ArrayList<Value> generateChunks (String)



}