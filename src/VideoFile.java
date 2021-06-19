import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("All")
public class VideoFile implements Serializable {
    public String videoName;
    public String channelName;
    String duration;
    String frames;
    public String path;
    public ArrayList<String> associatedHashtags;
    private static final long serialVersionUID = -2723363051253966964L;

    public VideoFile(String videoName, String channelName, ArrayList<String> associatedHashtags){
        this.videoName = videoName;
        this.channelName = channelName;
        this.associatedHashtags = associatedHashtags;
    }

    public VideoFile(String videoName, String channelName, String path, String[] hashtags){
        this.videoName = videoName;
        this.channelName = channelName;;
        this.path = path;
        associatedHashtags = new ArrayList<>();
        for(String s: hashtags){
            associatedHashtags.add(s);
        }

       
    }
    public VideoFile(String videoName){
        this.videoName = videoName;

    }

    public void addHastags( ArrayList<String> hashtags){
        this.channelName = channelName;
        this.associatedHashtags = hashtags;

    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setFrames(String frames) {
        this.frames = frames;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setAssociatedHashtags(String hashtag) {
        this.associatedHashtags.add(hashtag);
    }

    @Override
    public String toString() {
        return "VideoFile{" +
                "videoName='" + videoName + '\'' +
                '}';
    }

    public ArrayList<String> getAssociatedHashtags() {
        return associatedHashtags;
    }
}
