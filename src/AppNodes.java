import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.*;
import java.util.*;
@SuppressWarnings("all")


public class AppNodes extends Node {

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
        Scanner skr = new Scanner(System.in);
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
                        ArrayList<Broker> b = getBrokers();
                        int randomchoice = r.nextInt(b.size() -1) + 1;
                        Broker br = b.get(randomchoice);
                        int tempport = br.getPort();
                        InetAddress brokerip = br.getBrokerIP();

                        brokerSocket = new Socket(brokerip,tempport);
                        oos = new ObjectOutputStream(brokerSocket.getOutputStream()); //here we have to think of a method to find one random broker
                        ois = new ObjectInputStream(brokerSocket.getInputStream());

                        oos.writeObject(answer); //sending choice to a random broker
                        oos.flush();


                        System.out.println("Enter Channel Name");
                        name = skr.nextLine();
                        try{
                            System.out.println("Sending register request...");
                            oos.writeObject(name);
                            oos.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //byte reply = 0;
                        byte reply = ois.readByte();

                        if (reply == -1) {
                            System.out.println("There was an error");
                        } else {
                            System.out.println("You are Registered");
                            loginFlag = true;
                            oos.writeObject(this);
                            oos.flush();

                        }

                        oos.close();
                        ois.close();
                        brokerSocket.close();
                        break;


                    case 2: //publish video
                        System.out.println("Please choose the name of your channel");
                        name = skr.nextLine();
                        // perhaps we can also send the ip and port for the appropriate broker
                        Publisher pr = new Publisher(name,port);
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
        } catch (IOException e) {
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


    public String getChannelName(){return name;}




    public static class Handler extends Thread{


    }

}
