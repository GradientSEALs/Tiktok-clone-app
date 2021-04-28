import java.util.*;

public class VideoFile {
    private String videoName;
    private String channelName;
    private String dateCreated;
    private String length;
    private String framerate;
    private String frameWidth;
    private String frameHeight;

    public ArrayList<String> associatedHashtags;
    public byte[] videoFileChunk;


    public VideoFile(String videoName, String channelName, String dateCreated, String length, String framerate,
                     String frameWidth, String frameHeight, ArrayList<String> associatedHashtags, byte[] videoFileChunk)
    {
        this.videoName = videoName;
        this.channelName = channelName;
        this.dateCreated = dateCreated;
        this.length = length;
        this.framerate = framerate;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.associatedHashtags = associatedHashtags;
        this.videoFileChunk = videoFileChunk;
    }
}
