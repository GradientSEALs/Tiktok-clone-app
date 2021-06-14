package softeng.aueb.tiktok;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class Livefeed extends Fragment {

    String path;
    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View rootview = inflater.inflate(R.layout.livefeed_layout,container,false);


        final ViewPager2 videosViewPager = rootview.findViewById(R.id.videoViewPager);

        List<VideoFile> videoItems = new ArrayList<>();

        //Here you have to insert the videos that you want to be displayed

        VideoFile video1 = new VideoFile("ody");
        VideoFile video2 = new VideoFile("kota");
        video1.setAssociatedHashtags("pipis");
        video2.setAssociatedHashtags("pipis");
        video1.setPath("https://file-examples-com.github.io/uploads/2017/04/file_example_MP4_480_1_5MG.mp4");
        video2.setPath("https://file-examples-com.github.io/uploads/2017/04/file_example_MP4_480_1_5MG.mp4");
        videoItems.add(video1);
        videoItems.add(video2);

        videosViewPager.setAdapter(new VideosAdapter(videoItems));

        return rootview;

    }







}
