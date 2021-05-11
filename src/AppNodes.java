import org.apache.commons.lang3.ObjectUtils;
import org.json.JSONException;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.*;
import java.util.*;
@SuppressWarnings("all")


public class AppNodes extends Node {
    Scanner skr;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    ServerSocket AppServer;
    private Socket brokerSocket = null;
    boolean FirstContact = false;
    public String name;
    Publisher pr = null ;
    Consumer cr = null;
    volatile ArrayList<VideoFile> videoFiles = new ArrayList<>();
    Map<Integer,Util.Pair<String, Integer>> map;

    public static void main(String args[]) {

        try {
            new AppNodes().startService();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startService() throws IOException {

        boolean loginFlag = false;
        skr = new Scanner(System.in);
        Random r = new Random();
        int port = r.nextInt(10000-8001) + 8001;
        boolean whileFlag = true;
         //Ypotheto oti tha yparxei enas broker pou tha einai me gnosto port eksarxis kai tha kanei handle username passwords kai tha dinei ta ips ton allwn broker

        try {
            AppServer = new ServerSocket(port);
            InetAddress brokerIP = InetAddress.getLocalHost();
            int brokerPort = 4000;
            brokerSocket = new Socket(brokerIP,brokerPort);
            ois = new ObjectInputStream(brokerSocket.getInputStream());
            oos = new ObjectOutputStream(brokerSocket.getOutputStream());

            while(whileFlag){

                if(loginFlag==false) {
                    System.out.println("*********");
                    System.out.println("Welcome to Tik Tok");
                    System.out.println("Options");
                    System.out.println("1. Register");
                    System.out.println("*********");
                }
                else {
                    System.out.println("*********");
                    System.out.println("Welcome to Tik Tok");
                    System.out.println("2. Publish A Video "); //push function
                    System.out.println("3. Find A Video "); //pull function
                    System.out.println("4. Subscribe to a Hashtag or a Channel"); //pull function
                    System.out.println("Please pick the service you want");
                    System.out.println("*********");
                }

                boolean flag = true;
                byte answer = 0;
                if (flag) {
                    answer = Byte.parseByte(skr.nextLine());
                    System.out.println(answer);
                }
                ChannelName cn = null;

                switch (answer) {
                    case 1: //register
                        System.out.println("skr");
                        System.out.println("Enter Channel Name");
                        name = skr.nextLine();
                        boolean reply = register(brokerSocket,answer);

                        if (reply) {
                            System.out.println("You are Registered");
                            loginFlag = true;
                        }
                        else{
                            System.out.println("Trying to find right broker");
                            Util.Pair<String,Integer> rightBrokerInfo = (Util.Pair<String,Integer>)ois.readObject();
                            ois.close();
                            oos.close();
                            brokerSocket.close();
                            brokerSocket = new Socket(rightBrokerInfo.item1, rightBrokerInfo.item2);
                            ois = new ObjectInputStream(brokerSocket.getInputStream());
                            oos = new ObjectOutputStream(brokerSocket.getOutputStream());
                            loginFlag = register(brokerSocket,answer);
                        }
                        map = (Map<Integer,Util.Pair<String, Integer>>) ois.readObject();
                        break;
                    case 2: //publish video
                        System.out.println("Please choose your directory");
                        String fileName = "tsimpouki.mp4";
                        String hashtag = "hawk tsimpoukis papas";
                        String[] hashtags = hashtag.split(" ");
                        System.out.println(hashtag.length());

                        for (String hash : hashtags) {
                            System.out.println("I just got into hashtag for");
                            for (int brokerID : map.keySet()) {
                                System.out.println("I just did broker for");
                                int hashtag_hash = Util.getModMd5(hash);
                                if (hashtag_hash < brokerID) {
                                    try {
                                        brokerSocket = new Socket(map.get(brokerID).item1, map.get(brokerID).item2);
                                        notify(brokerSocket, new VideoFile(fileName), hash);
                                        System.out.println("I just did notify");

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                oos = null;
                                ois = null;
                            }
                        }




                        //pr = new Publisher(AppServer.accept(),);
                        //pr.notify();
                        //pr.start();
                        //pr.start();
                        //pr.join();
                        System.out.println("Finished case 2 from appnodes");

                        break;

                    case 3: //find video

                        cr = new Consumer(port);
                        cr.run();
                        //find a video
                        //antistoixo me to pano
                        //Consumer cr = new Consumer();
                        //cr.run();

                        break;

                    case 4: //declare intention to subscribe to a channel or hashtag
                        break;
                }
                //pr.join();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public boolean register(Socket s,byte answer) throws IOException {
        oos.writeByte(answer); //sending choice to a random broker
        oos.flush();
        int hash = Util.getModMd5(name);
        System.out.println(Util.getModMd5(name));
        try{
            oos.writeObject(name);
            oos.flush();
            oos.writeInt(hash);
            oos.flush();
            System.out.println("1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean reply = ois.readBoolean();
        System.out.println("Sending register request...");
        return reply;
    }

    public void login(Message message){
        try {
            System.out.println("Sending login request...");
            oos.writeObject(message);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean notify(Socket broker, VideoFile video,String hashtag) throws IOException {
        boolean notified = false;
        //Util.debug("ton pairneis");
        if (ois == null && oos == null){
            Util.debug("GIATI KLAIEI O MIKROS");
            ois = new ObjectInputStream(broker.getInputStream());
            oos = new ObjectOutputStream(broker.getOutputStream());
        }
        //Util.debug("Writing name");
        Util.debug("Wrote choice in notify");
        oos.writeByte(2); //to enter in the correct case
        oos.flush();
        oos.writeObject(hashtag); //channelName
        oos.flush();
        //Util.debug("Writing video");
        oos.writeObject(video); //Video
        oos.flush();
        oos.writeObject(hashtag); //hashtag
        oos.flush();

        //Util.debug("Reading response");
        notified = ois.readBoolean();
        return notified;
    }



    public String getChannelName(){return name;}


    /*public void loadAvailableFiles(String folder, String channel){
        File directory = new File(""+folder);
        File[] contents = directory.listFiles();
        if (contents == null){
            System.out.println("DEN VRIKA TPT AFENTIKO");
            return;
        }
        for ( File f : contents) {
            if (f.getName().endsWith(".mp4")) {
                videoFiles.add(new VideoFile(f.getName(),channel,folder));
            }
        }*/
}
