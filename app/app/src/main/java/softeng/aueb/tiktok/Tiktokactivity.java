package softeng.aueb.tiktok;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageButton;

import softeng.aueb.tiktok.ui.main.SectionsPagerAdapter;
import softeng.aueb.tiktok.databinding.ActivityTiktokactivityBinding;

public class Tiktokactivity extends AppCompatActivity {


    ImageButton capture;
    ImageButton gallery;
    String username;

    private ActivityTiktokactivityBinding binding;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        username = getIntent().getStringExtra("username");

        binding = ActivityTiktokactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);


        capture = findViewById(R.id.CaptureVideo);
        gallery = findViewById(R.id.LookIntoFiles);


    }

}