import java.util.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectOutputStream;

public class Publisher {

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

    //public Broker hashTopic(String hashTopic)

    //public void push (String,Value)

    //public void notifyFailure (Broker b)

    //public void notifyBrokersForHashtags(String)

   // public ArrayList<Value> generateChunks (String)



}
