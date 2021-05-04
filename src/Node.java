import java.io.IOException;
import java.util.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.net.InetAddress;

public class Node {

    public static ArrayList<Broker> brokers = new ArrayList<>();
    Socket socket;
    protected String ipAddress;
    protected int port;

    public Node() {
    }

    public Node(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void init(int i){};

    public ArrayList<Broker> getBrokers(){
        return brokers;
    };

    public Socket connect(){
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

    public void updateNodes(){};
}
