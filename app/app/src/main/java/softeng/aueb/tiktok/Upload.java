package softeng.aueb.tiktok;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import static android.app.Activity.RESULT_OK;

public class Upload extends Fragment implements View.OnClickListener{

    private static int VIDEO_REQUEST_CODE = 101;
    private Uri videoUri = null;
    View view;
    ImageButton capture;
    ImageButton files;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.upload_layout,container,false);
        capture = view.findViewById(R.id.CaptureVideo);
        files = view.findViewById(R.id.LookIntoFiles);

        capture.setOnClickListener(this);
        files.setOnClickListener(this);


        return view;
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.CaptureVideo:
            Intent camera = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            //File video_file = getFilepath();
            startActivityForResult(camera, VIDEO_REQUEST_CODE);
                break;
            case R.id.LookIntoFiles:

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,@Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==VIDEO_REQUEST_CODE ) {
            if (resultCode == RESULT_OK) {
                videoUri = data.getData();
                Log.i("VIDEO_RECORD_TAG","VIDEO RECORDED AT PATH" + videoUri);
            } else {
                Log.i("VIDEO_RECORD_TAG","VIDEO RECORDING FAILED");

            }
        }
    }


    public File getFilepath(){
        File folder = new File("sdcard/tiktok");
        if(!folder.exists()){
            folder.mkdir();
        }

        File video_file = new File(folder,"sample_video.mp4");



        return video_file;
    }



}
