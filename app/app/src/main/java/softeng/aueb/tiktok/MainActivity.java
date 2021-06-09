package softeng.aueb.tiktok;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

@SuppressWarnings("all")
public class MainActivity extends AppCompatActivity {
    Button buttonEnter;
    String username;
    EditText text;
    ArrayList<Integer> brokers;
    String port = "4000";
    String resp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         buttonEnter = findViewById(R.id.reg_button);
         text = findViewById(R.id.channel_name);


    }
    @Override
    protected void onStart() {


        super.onStart();

        buttonEnter.setOnClickListener(v -> {
            ClientRunner runn = new ClientRunner();
            username = text.getText().toString();
            runn.execute(port);
            Intent intent = new Intent(MainActivity.this,Tiktokactivity.class);
            intent.putExtra("username",username);
            startActivity(intent);
        });
    }

    private class ClientRunner extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            Socket requestSocket = null;
            ObjectInputStream in = null;
            ObjectOutputStream out = null;
            int _port = Integer.parseInt(port);
            boolean flag = true;
            do {
                try {
                    requestSocket = new Socket("10.0.2.2", _port);
                    out = new ObjectOutputStream(requestSocket.getOutputStream());
                    //in = new ObjectInputStream(requestSocket.getInputStream());

                    out.writeByte(1);
                    out.flush();

                    out.writeObject(username);
                    out.flush();

                    boolean correctBroker = in.readBoolean();
                    resp = Boolean.toString(correctBroker);
                    Log.e("DEBIG RECEIVED", resp);
                    if (!correctBroker) {
                        brokers = (ArrayList<Integer>) in.readObject();
                        _port = in.readInt();
                        continue;
                    }else
                        brokers = (ArrayList<Integer>) in.readObject();
                    brokers = (ArrayList<Integer>) in.readObject();
                    flag = false;
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        in.close();
                        out.close();
                        requestSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }while (flag);
            return resp;
        }
    }
}
