import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Node {

    public static ArrayList<Broker> brokers = new ArrayList<>();

    public static ArrayList<AppNodes> appnodes = new ArrayList<>();

    public static ArrayList<String> Generalhashtags = new ArrayList<>();

    Socket socket;
    protected String ipAddress;
    protected int port;

    public Node() {
    }

    public static void main(String[] args) {
    }



    public Node(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }


    public int getPort() {
        return port;
    }



    public ArrayList<Broker> getBrokers(){
        return brokers;
    };

    public ArrayList<AppNodes> getAppnodes(){
        return appnodes;
    };

    public ArrayList<String> getHashtags(){
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
