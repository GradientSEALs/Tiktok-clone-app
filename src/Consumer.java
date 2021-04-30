import java.util.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectOutputStream;

public class Consumer implements Node{
    private ObjectOutputStream buffer;

    public void register (Broker b,String message) {
        try {
            System.out.println("Sending register request...");
            buffer.writeObject(message);
            buffer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getBrokers() {
        for (int i=0;i<brokers.size();i++)
            System.out.println(brokers.get(i));
    }

    @Override
    public void connect() {
        System.out.println("Connection");

    }

    @Override
    public void disconnect() {
        System.out.println("Disconnect");

    }

    @Override
    public void updateNodes() {
        System.out.println("UpdateNodes");

    }
    //public void disconnect (Broker,String)

    //public void playData (String,Value)
}
