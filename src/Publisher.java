import java.io.*;
import java.net.ServerSocket;
import java.util.*;
import java.net.Socket;

@SuppressWarnings("all")
public class Publisher extends Thread {

    VideoFile video;
    Socket connection = null;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    String directory;



    public Publisher(Socket conn,String directory){
        this.connection=conn;
        this.directory=directory;
    }

    @Override
    public void run() {

        try {
            oos = new ObjectOutputStream(connection.getOutputStream());
            ois = new ObjectInputStream(connection.getInputStream());

            String videonamewanted = (String) ois.readObject(); //video name

            System.out.println("I am in publisher Thread");

            String path = directory + "/" + videonamewanted;

            push(path,connection);

            System.out.println("Finished video sending");


        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            try{
                oos.close();
                ois.close();
                connection.close();

            }
            catch (IOException e) {
                e.printStackTrace();
            }


        }


    }

    public void push(VideoFile videoFile, Socket consumer) throws IOException {
        byte[] videoData = Util.loadVideoFromDiskToRam(videoFile);
        List<byte[]> chunckedVideo = Util.chunkifyFile(videoData);
        for (byte[] data : chunckedVideo) {
            oos.write(data);
            oos.flush();
        }
    }

    //============= OVERLOADED FOR STRING PATH INPUT ===========================//
    public void push(String path, Socket consumer) throws IOException {
        byte[] videoData = Util.loadVideoFromDiskToRam(path);
        List<byte[]> chunckedVideo = Util.chunkifyFile(videoData);
        for (byte[] data : chunckedVideo) {
            oos.write(data);

        }
        oos.flush();
    }
}