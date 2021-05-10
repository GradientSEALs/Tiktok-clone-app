import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.util.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectOutputStream;
import org.json.JSONArray;
import org.json.JSONObject;

public class Consumer extends Thread {
    private String ipAddress;
    private int port;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    int brokerport;
    InetAddress brokerip;
    Socket brokersocket;
    Object lock = new Object();

public Consumer(int port) {
    this.port = port;
}


    @Override
    public void run() {

        try {
            ServerSocket s = new ServerSocket(port); //might not be needed
            Scanner skr = new Scanner(System.in);

            System.out.println("Do you want to find a video based on hashtag or channelname?");
            String decision = skr.nextLine();
            if(decision.equals("hashtag")){
                System.out.println("What is the hashtag you want to find?");
                decision = skr.nextLine();

                for(Broker b: Node.brokers){ //finds the broker that handles this hashtag
                    if(b.brokerhashtag.contains(decision)){
                        brokerport = b.getPort();
                        brokerip = b.getBrokerIP();
                        brokersocket = new Socket(brokerip,brokerport);
                        break;
                    }
                }

                oos = new ObjectOutputStream(brokersocket.getOutputStream());
                ois = new ObjectInputStream(brokersocket.getInputStream());

                oos.writeByte(1);
                oos.flush();
                oos.writeChars(decision);
                oos.flush();
                ArrayList<VideoFile> interestingvideos = (ArrayList<VideoFile>) ois.readObject();
                for(VideoFile v: interestingvideos){ //show all videos to the user
                    System.out.print(v.videoName + " ,");
                }
                System.out.print("\n");
                System.out.println("Which video do you want to view?");
                String answer = skr.nextLine();

                oos.writeChars(answer); //send video request to the broker
                oos.flush();


                oos.close();
                ois.close();

            }
            else{
                System.out.println("What is the Channel Name you want to find?");
                decision = skr.nextLine();

                for(Broker b: Node.brokers){ //finds the broker that handles videos of this channelname
                    if(b.brokerchannelnameslist.contains(decision)){
                        brokerport = b.getPort();
                        brokerip = b.getBrokerIP();
                        brokersocket = new Socket(brokerip,brokerport);
                        break;
                    }
                }

                oos = new ObjectOutputStream(brokersocket.getOutputStream());
                ois = new ObjectInputStream(brokersocket.getInputStream());

                oos.writeByte(-1);
                oos.flush();




                oos.close();
                ois.close();


            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void register(Broker b, String message) {
        try {

            System.out.println("Sending register request...");
            oos.writeObject(message);
            oos.flush();
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