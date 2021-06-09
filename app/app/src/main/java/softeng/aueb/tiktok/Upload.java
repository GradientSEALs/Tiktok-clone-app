package softeng.aueb.tiktok;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import softeng.aueb.tiktok.AndroidUtil;

public class Upload extends Fragment implements View.OnClickListener{

    private static int VIDEO_RECORD_CODE = 101;
    private static int CAMERA_PERMISSION_CODE = 100;



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
            case R.id.CaptureVideo:  //capture


                if(isCameraPresentInPhone()){
                    Log.i("VIDEO_RECORD_TAG", "Camera is detected");
                    getCameraPermission();
                    recordVideo();

                }
                else{
                    Log.i("VIDEO_RECORD_TAG", "No camera is detected");
                }


            case R.id.LookIntoFiles: //pick a file

                


                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,@Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
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




}
