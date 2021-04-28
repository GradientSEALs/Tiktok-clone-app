import java.util.*;

public class Value {
    private VideoFile videoFile;

    public void setVideoFile(VideoFile videoFile) {
        this.videoFile = videoFile;
    }

    public VideoFile getVideoFile() {
        return videoFile;
    }

    public Value(VideoFile videoFile) {
        this.videoFile = videoFile;
    }
}
