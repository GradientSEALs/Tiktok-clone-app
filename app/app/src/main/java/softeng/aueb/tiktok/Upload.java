package softeng.aueb.tiktok;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;

import org.jetbrains.annotations.NotNull;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class Upload extends Fragment implements View.OnClickListener{

    private static int VIDEO_RECORD_CODE = 101;
    private static int CAMERA_PERMISSION_CODE = 100;

    ArrayList<String> brokers = new ArrayList<>();
    String channelname;
    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
    VideoFile video;
    String path;
    private Uri videoUri = null;
    View view;
    ImageButton capture;
    ImageButton files;
    EditText hashtags;
    Button uploadButton;
    EditText videoName;
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.upload_layout,container,false);
        capture = view.findViewById(R.id.CaptureVideo);
        files = view.findViewById(R.id.LookIntoFiles);
        hashtags = view.findViewById(R.id.hashtags);
        uploadButton = view.findViewById(R.id.uploadButton);
        videoName = view.findViewById(R.id.videoname);

        capture.setOnClickListener(this);
        files.setOnClickListener(this);
        uploadButton.setOnClickListener(this);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Tiktokactivity tiktok = (Tiktokactivity) getActivity();
        assert tiktok != null;
        //port = tiktok.port;
        channelname = tiktok.username;
        brokers.add("10.0.2.2:4000");
        brokers.add("10.0.2.2:4001");
        brokers.add("10.0.2.2:4002");
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.CaptureVideo:  //capture


                if(isCameraPresentInPhone()){
                    Log.i("VIDEO_RECORD_TAG", "Camera is detected");
                    getCameraPermission();
                    recordVideo();

                }
                else{
                    Log.i("VIDEO_RECORD_TAG", "No camera is detected");
                }
                break;

            case R.id.LookIntoFiles: //pick a file

                getCameraPermission();
                videopicker();
                break;
            case R.id.uploadButton:
                //mmr.setDataSource(path);

                video = new VideoFile(videoName.getText().toString()+".mp4");
                video.setPath(path);
                video.setChannelName(channelname);
                video.setAssociatedHashtags(hashtags.getText().toString());
                new Publisher().execute(video);


                break;
        }
    }


    private void videopicker(){
        Intent intent = new Intent(getActivity(), FilePickerActivity.class);
        intent.putExtra(FilePickerActivity.CONFIGS,new Configurations.Builder()
        .setCheckPermission(true)
        .setShowVideos(true)
        .setShowImages(false)
        .enableVideoCapture(true)
        .setMaxSelection(1)
        .setSkipZeroSizeFiles(true)
        .build());
        startActivityForResult(intent,104);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,@Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode){
            case 101:
                if(requestCode==VIDEO_RECORD_CODE ) {

                    if (resultCode == RESULT_OK) {
                        videoUri = data.getData();
                        Log.i("VIDEO_RECORD_TAG","VIDEO RECORDED AT PATH : " + videoUri);
                    } else if(resultCode == RESULT_CANCELED) {
                        Log.i("VIDEO_RECORD_TAG","VIDEO RECORDING IS CANCELLED");
                    }
                    else{
                        Log.i("VIDEO_RECORD_TAG","VIDEO RECORDING FAILED");

                    }
                }

                break;

            case 104:
                if(resultCode==RESULT_OK && data!= null) {
                    ArrayList<MediaFile> mediaFiles = data.getParcelableArrayListExtra(
                            FilePickerActivity.MEDIA_FILES
                    );
                    path = mediaFiles.get(0).getPath();

                    displatToast("Video Path: " + path);

                }
                break;

        }


    }

    private void displatToast(String s) {
        Toast.makeText(getActivity().getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }


    private boolean isCameraPresentInPhone(){
        if(getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
            return true;
        }
        else{
            return false;
        }

    }

    private void getCameraPermission(){
        if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA)
        == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(getActivity(),new String[]
                    {Manifest.permission.CAMERA},CAMERA_PERMISSION_CODE );
        }

    }

    private void recordVideo() {
        Intent camera = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        //File video_file = getFilepath();
        startActivityForResult(camera, VIDEO_RECORD_CODE);
    }

    private class Publisher extends AsyncTask<VideoFile,String,String>{

        Socket broker = null;
        ObjectInputStream in = null;
        ObjectOutputStream out = null;
        String hashtag;
        int port;


        @Override
        protected String doInBackground(VideoFile... videos) {
            hashtag = hashtags.getText().toString();
            int toSubHaSH = Util.getModMd5(hashtag);
            ArrayList<Integer> hashes = new ArrayList<>();
            for (String ip : brokers){
                hashes.add(Util.getModMd5(ip));
            }
            Collections.sort(hashes);
            brokers.sort((o1, o2) -> {
                int hash1 = Util.getModMd5(o1);
                int hash2 = Util.getModMd5(o2);
                return Integer.compare(hash1, hash2);
            });
            String temp = "";

            for (int i = 0; i<3; i++){
                if (toSubHaSH< hashes.get(i)){
                    temp = brokers.get(i);
                }
            }
            /*String[] temp2 = temp.split(":");
            port = Integer.parseInt(temp2[1]);*/
            try {
                broker = new Socket("10.0.2.2",4002);
                in = new ObjectInputStream(broker.getInputStream());
                out = new ObjectOutputStream(broker.getOutputStream());
                out.writeByte(2);
                out.flush();

                out.writeObject(video);
                out.flush();

                out.writeObject(hashtag);
                out.flush();

                byte[] videoData = Util.loadVideoFromDiskToRam(video);
                List<byte[]> chunckedVideo = Util.chunkifyFile(videoData);
                for (byte[] data : chunckedVideo) {
                    out.write(data);
                }
                out.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }


}
