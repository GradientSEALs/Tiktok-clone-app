import java.util.*;

public class ChannelName {
    private String channelName;

    public ArrayList <String> hashtagsPublished = new ArrayList<>();

    HashMap <String,ArrayList<Value>> userVideoFilesMap = new HashMap<>();

    public ChannelName(String name){
        channelName = name;
    }

    public void setChannelName(String name){
        channelName = name;
    }


}
