import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.util.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectOutputStream;
import org.json.JSONArray;
import org.json.JSONObject;

public class Consumer extends Thread {


    ObjectOutputStream oos;
    ObjectInputStream ois;
    String videoname;
    Socket conn;


public Consumer(Socket conn, String videoname) {

    this.conn = conn;
    this.videoname=videoname;
}


    @Override
    public void run() {

        try {
            ois = new ObjectInputStream(conn.getInputStream());
            oos = new ObjectOutputStream(conn.getOutputStream());

            oos.writeObject(videoname);
            oos.flush();

            System.out.println("I am in Consumer Thread");


        } catch (IOException e) {
            e.printStackTrace();
        }



    }


}