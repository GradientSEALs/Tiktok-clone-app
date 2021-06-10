package softeng.aueb.tiktok;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

@SuppressWarnings("all")
public class MainActivity extends AppCompatActivity implements AsyncResponse {
    Button buttonEnter;
    String username;
    EditText text;
    ArrayList<String> brokers;
    int appBroker = 4000;

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
            ClientRunner runn = new ClientRunner(this);
            username = text.getText().toString();
            runn.execute("4000");
            Intent intent = new Intent(MainActivity.this,Tiktokactivity.class);
            intent.putExtra("username",username);
            intent.putStringArrayListExtra("brokers",brokers);
            intent.putExtra("port",appBroker);
            startActivity(intent);
        });
    }

    @Override
    public void processFinish(ArrayList<String> brokerPorts, int appBrokerPort) {
        this.brokers = brokerPorts;
        this.appBroker = appBrokerPort;
        Toast.makeText(this, "Read File Successfully!", Toast.LENGTH_LONG).show();
    }

    private class ClientRunner extends AsyncTask<String,String,String>{
        boolean flag = true;
        int _port = 4000;
        ArrayList<String> _brokers;
        public AsyncResponse delegate = null;

        ClientRunner(AsyncResponse delegate){
            this.delegate = delegate;
        }

        @Override
        protected String doInBackground(String... strings) {
            Socket requestSocket = null;
            ObjectInputStream in = null;
            ObjectOutputStream out = null;


            do {
                try {
                    requestSocket = new Socket("10.0.2.2", _port);
                    out = new ObjectOutputStream(requestSocket.getOutputStream());
                    in = new ObjectInputStream(requestSocket.getInputStream());

                    out.writeByte(1);
                    out.flush();

                    out.writeObject(username);
                    out.flush();

                    boolean correctBroker = in.readBoolean();
                    resp = Boolean.toString(correctBroker);
                    Log.e("DEBUG RECEIVED", resp);
                    if (!correctBroker) {
                        _port = (int) in.readObject();
                        continue;
                    }else
                        flag = false;
                    _brokers = (ArrayList<String>) in.readObject();
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
            //appBroker = _port;
            return resp;
        }
        protected void onProgressUpdate(Integer... progress) {
            setProgress(progress[0]);
        }
        protected void onPostExecute(Long result) {
            delegate.processFinish(brokers,appBroker);
            Log.w("hello","TElos");
        }

    }




}
