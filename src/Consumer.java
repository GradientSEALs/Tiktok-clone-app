import java.util.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectOutputStream;
import org.json.JSONArray;
import org.json.JSONObject;

public class Consumer extends Node {
    private String ipAddress;
    private int port;
    private ObjectOutputStream buffer;


public Consumer(String ipAddress,int port){
    super(ipAddress,port);
}
    public static void main(String[] args) throws Exception{

    Handler handler = new Handler();

    //noinspection InfiniteLoopStatement
        while(true)
    {

        System.out.println("hi");

    }
}

    public void register(Broker b, String message) {
        try {

            System.out.println("Sending register request...");
            buffer.writeObject(message);
            buffer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkHashtagExists(String hashtag){
        List <String> hashtags = new ArrayList<>();
        for (int i=0;i<hashtags.size();i++){
            if (hashtags.contains(hashtag))
                return true;
        }
        return false;
    }




    public static class Handler extends Thread {



        public Handler() {
        }

        public void run(){
        }

        //public void disconnect (Broker,String)

        //public void playData (String,Value)
    }
}