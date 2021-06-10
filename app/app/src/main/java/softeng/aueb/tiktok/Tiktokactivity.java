package softeng.aueb.tiktok;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import softeng.aueb.tiktok.ui.main.SectionsPagerAdapter;
import softeng.aueb.tiktok.databinding.ActivityTiktokactivityBinding;

public class Tiktokactivity extends AppCompatActivity {


    ImageButton capture;
    ImageButton gallery;
    TextView _username;
    private ActivityTiktokactivityBinding binding;
    int port;
    String username;
    ArrayList<String> brokers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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


        capture = findViewById(R.id.CaptureVideo);
        gallery = findViewById(R.id.LookIntoFiles);




    }

}