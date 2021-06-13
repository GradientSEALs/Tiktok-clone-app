package softeng.aueb.tiktok;

import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoViewHolder>{

    private List<VideoFile> videoItems;

    public VideosAdapter(List<VideoFile> videoItems) {
        this.videoItems = videoItems;
    }

    @NonNull
    @NotNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new VideoViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_container_video,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull VideoViewHolder holder, int position) {
        holder.setVideoData(videoItems.get(position));
    }

    @Override
    public int getItemCount() {
        return videoItems.size()    ;
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {

        VideoView videoView;
        TextView textVideoTitle, textVideoHashtags;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoView);
            textVideoTitle = itemView.findViewById(R.id.textVideoTitle);
            textVideoHashtags = itemView.findViewById(R.id.textVideoHashtags);
        }

        void setVideoData(VideoFile videoItem) {
            textVideoTitle.setText(videoItem.getVideoName());
            textVideoHashtags.setText(videoItem.getAssociatedHashtags().toString());
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();

                    float videoRatio = mp.getVideoWidth() / (float) mp.getVideoHeight();
                    float screenRatio = videoView.getWidth() / (float) videoView.getHeight();

                    float scale = videoRatio / screenRatio;
                    if (scale > 1f) {
                        videoView.setScaleX(scale);
                    } else {
                        videoView.setScaleY(1f / scale);
                    }


                }
            });

            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.start();
                }
            });

        }
    }
}
