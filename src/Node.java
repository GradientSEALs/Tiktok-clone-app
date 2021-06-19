

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Node {

    public static ArrayList<Broker> brokers = new ArrayList<>();



    public static HashSet<String> Generalhashtags = new HashSet<>();

    public static HashSet<String> channelnameslist = new HashSet<>();



    public Node() {
    }

    public static ArrayList<Broker> getBrokers(){
        return brokers;
    };




    public static HashSet<String> getChannelnameslist() {
        return channelnameslist;
    }



    public static HashSet<String> getHashtags() {
        return Generalhashtags;
    }

}
