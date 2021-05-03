import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp4.MP4Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class VideoFile {
    public String videoName;
    public String channelName;
    public String dateCreated;
    public String length;
    public String frameWidth;
    public String frameHeight;
    public JSONObject videoDetails;

    public String[] associatedHashtags;


    public VideoFile(String videoName, String channelName, String dateCreated, String length,
                     String frameWidth, String frameHeight, String[] associatedHashtags) throws JSONException {
        this.videoName = videoName;
        this.channelName = channelName;
        this.dateCreated = dateCreated;
        this.length = length;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.associatedHashtags = associatedHashtags;
        this.videoDetails = new JSONObject();
        videoDetails.put("action","GET_VIDEO");
        videoDetails.put("videoName",videoName);
        videoDetails.put("channelName",channelName);
        videoDetails.put("length",length);
        videoDetails.put("frameWidth",frameWidth);
        videoDetails.put("frameHeight",frameHeight);
        videoDetails.put("associatedHashtags", Arrays.toString(associatedHashtags));
        videoDetails.put("dateCreated",dateCreated);
    }

    public VideoFile loadVideo(String channelName, String path, String name, String[] hashtags) throws IOException, TikaException, SAXException, JSONException {
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        FileInputStream inputstream = new FileInputStream(path+name);
        ParseContext pcontext = new ParseContext();

        MP4Parser MP4Parser = new MP4Parser();
        MP4Parser.parse(inputstream, handler, metadata,pcontext);
        String[] metadataNames = metadata.names();

        String _dateCreated = metadataNames[3];
        String _length = metadataNames[6];
        String _frameWidth = metadataNames[5];
        String _frameHeight = metadataNames[2];
        return new VideoFile(name,channelName,_dateCreated,_length,_frameWidth,_frameHeight,hashtags);
    }
}
