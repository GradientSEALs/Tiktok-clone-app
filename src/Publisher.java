import java.util.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectOutputStream;

public class Publisher implements Node {

    private ChannelName channelName;
    public ArrayList <String> hashtags = new ArrayList<>();

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

    public void getBrokerList(){
        for (int i=0;i<hashtags.size();i++)
            System.out.println(hashtags.get(i));
    }

    @Override
    public void getBrokers() {
        for (int i=0;i<brokers.size();i++)
            System.out.println(brokers.get(i));
    }

    @Override
    public void connect() {
        System.out.println("Connection");

    }

    @Override
    public void disconnect() {
        System.out.println("Disconnect");

    }

    @Override
    public void updateNodes() {
        System.out.println("UpdateNodes");

    }

    //public Broker hashTopic(String hashTopic)

    //public void push (String,Value)

    //public void notifyFailure (Broker b)

    //public void notifyBrokersForHashtags(String)

   // public ArrayList<Value> generateChunks (String)



}
