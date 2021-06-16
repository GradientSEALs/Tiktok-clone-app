package softeng.aueb.tiktok;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import softeng.aueb.tiktok.ui.main.SectionsPagerAdapter;
import softeng.aueb.tiktok.databinding.ActivityTiktokactivityBinding;

@SuppressWarnings("all")
public class Tiktokactivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 7;
    private final String FOLDERNAME = "tiktok";
    ImageButton capture;
    ImageButton gallery;
    TextView _username;
    private ActivityTiktokactivityBinding binding;
    int port;
    String username;
    ArrayList<String> brokers;
    ServerSocket server;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        if (ContextCompat.checkSelfPermission(Tiktokactivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
            createDirectory(FOLDERNAME);
        }
        else {
            askPermission();
        }
        new Consumer().execute();
        super.onCreate(savedInstanceState);
        username = getIntent().getStringExtra("username");
        brokers = getIntent().getExtras().getStringArrayList("brokers");
        port = getIntent().getIntExtra("port",4000);
        binding = ActivityTiktokactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        _username = binding.username;
        _username.setText(username);


        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

        Bundle bundle = new Bundle();
        bundle.putString("user",username);
        capture = findViewById(R.id.CaptureVideo);
        gallery = findViewById(R.id.LookIntoFiles);



    }

    private void askPermission(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String [] permissions, @NonNull @NotNull int[] grantResults){
        if (requestCode == REQUEST_CODE){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED);{
                createDirectory(FOLDERNAME);
            }
        }
        else{
            Toast.makeText(Tiktokactivity.this,"Permission Denied",Toast.LENGTH_SHORT).show();
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void createDirectory(String foldername){
        file = new File(Environment.getDataDirectory(),foldername);
        if(!file.exists()){
            file.mkdir();
            Toast.makeText(Tiktokactivity.this,"Successful",Toast.LENGTH_SHORT).show();
        }else
        {
            Toast.makeText(Tiktokactivity.this,"Folder Already Exists",Toast.LENGTH_SHORT).show();
        }

    }

    public  boolean isStoragePermissionGranted() {
        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            Log.v("TAG", "Permission is granted");
            return true;
        } else {

            Log.v("TAG", "Permission is revoked");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return false;
        }
    }
    private class Consumer extends AsyncTask<Socket,String,String> {


        Socket conn;
        ObjectOutputStream out;
        ObjectInputStream in;

        @Override
        protected String doInBackground(Socket... sockets) {
            try {
                server = new ServerSocket(7000);
                conn = server.accept();
                out = new ObjectOutputStream(conn.getOutputStream());
                in = new ObjectInputStream(conn.getInputStream());

                byte resp = in.readByte();
                if (resp == 0)
                    return null;
                while(in.readByte()==1){
                String videoName = (String) in.readObject();
                FileOutputStream fout = new FileOutputStream(file+"/"+videoName);
                byte[] bytes = new byte[512];
                try {
                    for (; ; ) {
                        bytes = (byte[]) in.readObject();
                        if (bytes == null) {
                            break;
                        }
                        fout.write(bytes);

                    }
                    fout.close();
                    } catch (IOException e) {
                     e.printStackTrace();
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}