import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
@SuppressWarnings("all")


public class AppNodes {



    public static void main(String args[]) throws IOException{
        ServerSocket AppNodesSocket;

        try{
            AppNodesSocket = new ServerSocket(1441, 10);
            System.out.println("The server is open at port: " + 1441);
            System.out.println("Server socket created.Waiting for connection...");
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("*********");
        System.out.println("Welcome to Tik Tok");
        System.out.println("Options");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Publish A Video");
        System.out.println("4. Find A Video");
        System.out.println("*********");
        Scanner skr = new Scanner(System.in);
        int answer = Integer.parseInt(skr.nextLine());
        switch (answer) {
        case 1: //publish

        case 2: //


        }
    }

    public static class Handler extends Thread{


    }

}


