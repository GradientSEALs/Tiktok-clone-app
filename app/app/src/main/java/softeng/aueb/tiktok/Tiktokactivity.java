package softeng.aueb.tiktok;

import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.widget.ImageButton;
import android.widget.TextView;

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

    File directory = new File(Environment.getExternalStorageState(),"tiktok") ;
    ImageButton capture;
    ImageButton gallery;
    TextView _username;
    private ActivityTiktokactivityBinding binding;
    int port;
    String username;
    ArrayList<String> brokers;
    ServerSocket server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (!directory.exists()){
            directory.mkdirs();

            if(!directory.isDirectory()){
                AlertDialog.Builder builder = new AlertDialog.Builder(Tiktokactivity.this);
                String message = "Failed to create directory";
                builder.setMessage(message);
                builder.show();
            }
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
                FileOutputStream fout = new FileOutputStream(directory+"/"+videoName);
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