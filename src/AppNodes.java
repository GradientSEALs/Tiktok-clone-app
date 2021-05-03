import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
@SuppressWarnings("all")


public class AppNodes {

    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    ServerSocket AppServer;
    private Socket trackerConn = null;
    private String username,password;

    public  void main(String args[]) throws IOException {

            startService();
    }

    public void startService() throws IOException {

        boolean loginFlag = false;
        Scanner skr = new Scanner(System.in);
        Random r = new Random();
        int port = r.nextInt(8000-3000) + 3000;
        boolean whileFlag = true;
        trackerConn = new Socket("localhost",1441); //Ypotheto oti tha yparxei enas broker pou tha einai me gnosto port eksarxis kai tha kanei handle username passwords kai tha dinei ta ips ton allwn broker

        try {

            oos = new ObjectOutputStream(trackerConn.getOutputStream()); //here we have to think of a method to find one random broker
            ois = new ObjectInputStream(trackerConn.getInputStream());

            AppServer = new ServerSocket(port);

            while(whileFlag){
            if(loginFlag=false) {
                System.out.println("*********");
                System.out.println("Welcome to Tik Tok");
                System.out.println("Options");
                System.out.println("1. Register (1)");
                System.out.println("2. Login (2)");
                System.out.println("*********");
            }
            else {
                System.out.println("*********");
                System.out.println("Welcome to Tik Tok");
                System.out.println(" Publish A Video (3)");
                System.out.println(" Find A Video (4)");
                System.out.println("*********");
            }

            System.out.println("Please pick the service you want");
            int answer = Integer.parseInt(skr.nextLine());
            oos.writeObject(answer);
            oos.flush();
            Message credentials;
            ChannelName cn = null;
            switch (answer) {
                case 1: //register
                    System.out.println("Enter username");
                    this.setUsername(skr.nextLine());
                    System.out.println("Enter password");
                    this.setPassword(skr.nextLine());
                    credentials = new Message(username, password);
                    this.register(credentials);
                    //byte reply = 0;
                    byte reply = ois.readByte();

                    if (reply == -1) {
                        System.out.println("There was an error");
                    } else {
                        System.out.println("You are Registered");
                        cn = new ChannelName(username);
                    }
                    break;

                case 2: //login
                    System.out.println("Enter username");
                    this.setUsername(skr.nextLine());
                    System.out.println("Enter password");
                    this.setPassword(skr.nextLine());
                    credentials = new Message(username, password);
                    this.login(credentials);
                    //byte reply2 = -1;
                    byte reply2 = ois.readByte();
                    //when a user tries to login w/out being registered, there is a message only in handler
                    if (reply2 == -1) {
                        System.out.println("There was an error");
                    } else {
                        System.out.println("You are Logged in");
                        loginFlag = true;
                    }
                    break;

                case 3: //publish video
                    System.out.println("Please choose hashtag for the video");
                    String hash = skr.nextLine();
                    // perhaps we can also send the ip and port for the appropriate broker
                    Publisher pr = new Publisher(cn, hash);
                    //pr.start();

                case 4: //find a video
                    //antistoixo me to pano
                    //Consumer cr = new Consumer();

            }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void register(Message message){
        try{
            System.out.println("Sending register request...");
            oos.writeObject(message);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public static class Handler extends Thread{


    }

}


