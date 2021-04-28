import java.util.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectOutputStream;

public class Consumer extends Node{
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

    //public void disconnect (Broker,String)

    //public void playData (String,Value)
}
