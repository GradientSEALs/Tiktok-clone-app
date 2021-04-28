import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ConsumerHandler extends Thread {
    public ObjectOutputStream oos;
    public ObjectInputStream ois;
}