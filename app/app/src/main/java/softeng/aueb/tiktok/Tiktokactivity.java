package softeng.aueb.tiktok;

import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import softeng.aueb.tiktok.ui.main.SectionsPagerAdapter;
import softeng.aueb.tiktok.databinding.ActivityTiktokactivityBinding;

public class Tiktokactivity extends AppCompatActivity {

    String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath(); ;
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

                FileOutputStream fout = new FileOutputStream(directory);

                byte[] bytes = new byte[512];
                int count = 0;
                while ((count=in.read(bytes)) > 0 ) {
                    fout.write(bytes);
                    System.out.println(bytes.length);
                }
                fout.flush();


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}