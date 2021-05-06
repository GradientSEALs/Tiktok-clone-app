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

                int answer = Integer.parseInt(skr.nextLine());

                ChannelName cn = null;

                switch (answer) {
                    case 1: //register
                        InetAddress brokerIP = InetAddress.getLocalHost();
                        int brokerPort = 4000;
                        brokerSocket = new Socket(brokerIP,brokerPort);
                        ois = new ObjectInputStream(brokerSocket.getInputStream());
                        oos = new ObjectOutputStream(brokerSocket.getOutputStream());
                        System.out.println("skr");
                        boolean reply = register(brokerSocket,answer);

                        if (reply) {
                            System.out.println("You are Registered");
                            loginFlag = true;
                        }
                        else{
                            System.out.println("Trying to find right broker");
                            Util.Pair<String,Integer> rightBrokerInfo = (Util.Pair<String,Integer>)ois.readObject();
                            oos.close();
                            ois.close();
                            brokerSocket.close();
                            brokerSocket = new Socket(rightBrokerInfo.item1, rightBrokerInfo.item2);
                            ois = new ObjectInputStream(brokerSocket.getInputStream());
                            oos = new ObjectOutputStream(brokerSocket.getOutputStream());
                            register(brokerSocket,answer);
                        }
                        break;
                    case 2: //publish video
                        System.out.println("Please choose your directory");
                        String path = skr.nextLine();
                        Publisher pr = new Publisher(name,path,brokerSocket);
                        pr.notify();
                        pr.run();
                        //pr.start();
                        break;

                    case 3: //find video

                        Consumer cr = new Consumer(port);
                        cr.run();
                        //find a video
                        //antistoixo me to pano
                        //Consumer cr = new Consumer();
                        //cr.run();
                        break;

                    case 4: //declare intention to subscribe to a channel or hashtag
                        break;
            }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean register(Socket s,int answer) throws IOException {
        oos.writeObject(answer); //sending choice to a random broker
        System.out.println("Enter Channel Name");
        name = skr.nextLine();
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


    public String getChannelName(){return name;}




    public static class Handler extends Thread{


    }

}
