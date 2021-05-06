import org.checkerframework.checker.units.qual.A;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Node {

    public static ArrayList<Broker> brokers = new ArrayList<>();

    public static ArrayList<AppNodes> appnodes = new ArrayList<>();

    public static HashSet<String> Generalhashtags = new HashSet<>();

    public static HashSet<String> channelnameslist = new HashSet<>();





    public Node() {
    }

    public static ArrayList<Broker> getBrokers(){
        return brokers;
    };

    public static ArrayList<AppNodes> getAppnodes(){
        return appnodes;
    };


    public static HashSet<String> getChannelnameslist() {
        return channelnameslist;
    }



    public static HashSet<String> getHashtags() {
        return Generalhashtags;
    }

    /*public Socket connect(){
        while (true){
            try {
                InetAddress host = InetAddress.getByName(ipAddress);
                socket = new Socket(host,port);
                return socket;
            }  catch (IOException e) {
                System.out.println(e);
            }
        }

    }

    public void disconnect(Socket socket){
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateNodes(){};*/
}
