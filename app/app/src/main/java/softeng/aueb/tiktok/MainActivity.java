package softeng.aueb.tiktok;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button buttonEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         buttonEnter = findViewById(R.id.reg_button);



    }
    @Override
    protected void onStart() {


        super.onStart();

        buttonEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Tiktokactivity.class);
                intent.putExtra("username",findViewById(R.id.channel_name).toString());
                startActivity(intent);
            }
        });
    }
}
