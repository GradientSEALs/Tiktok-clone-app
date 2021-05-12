import java.io.FileOutputStream;
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

    FileOutputStream out;
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
            out = new FileOutputStream("dir1"+videoname);
            oos.flush();

            byte[] bytes = new byte[512*16];
            int count = 0;
            while ((count=ois.read(bytes)) > 0 ) {
                out.write(bytes);
            }

            System.out.println("I am in Consumer Thread");


        } catch (IOException e) {
            e.printStackTrace();
        }



    }


}