package softeng.aueb.tiktok;
import java.io.Serializable;
import java.util.*;
@SuppressWarnings("All")
public class VideoFile implements Serializable {
    public String videoName;
    public String channelName;
    //public JSONObject videoDetails;
    public String path;
    public ArrayList<String> associatedHashtags;
    private static final long serialVersionUID = -2723363051253966964L;

    public VideoFile(String videoName,String channelName,ArrayList<String> associatedHashtags){
        this.videoName = videoName;
        this.channelName = channelName;
        this.associatedHashtags = associatedHashtags;
    }

    public VideoFile(String videoName, String channelName,String path, String[] hashtags){
        this.videoName = videoName;
        this.channelName = channelName;;
        this.path = path;
        associatedHashtags = new ArrayList<>();
        for(String s: hashtags){
            associatedHashtags.add(s);
        }

        /*this.videoDetails = new JSONObject();
        videoDetails.put("action","GET_VIDEO");
        videoDetails.put("videoName",videoName);
        videoDetails.put("channelName",channelName);
        videoDetails.put("path",path);*/
    }
    public VideoFile(String videoName){
        this.videoName = videoName;
        /*this.videoDetails = new JSONObject();
        try {
            videoDetails.put("action","GET_VIDEO");
            videoDetails.put("videoName",videoName);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }

    public void addHastags( ArrayList<String> hashtags){
        this.channelName = channelName;
        this.associatedHashtags = hashtags;
        /*try {
            videoDetails.put("associatedHashtags", Arrays.toString(new ArrayList[]{associatedHashtags}));
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
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
