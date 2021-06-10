package softeng.aueb.tiktok;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;


public class Subscribe extends Fragment implements View.OnClickListener{

    EditText hashtags;
    Button sub;
    View view;
    int port;
    String myChannelName;
    ArrayList<String> brokers = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.subscribe_layout,container,false);
        hashtags = view.findViewById(R.id.toSub);
        sub = view.findViewById(R.id.sub);
        sub.setOnClickListener(this);

        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        Tiktokactivity tiktok = (Tiktokactivity)getActivity();
        assert tiktok != null;
        //port = tiktok.port;
        myChannelName = tiktok.username;
        //brokers = tiktok.brokers;
        brokers.add("localhost:4000");
        brokers.add("localhost:4001");
        brokers.add("localhost:4002");
    }

    @Override
    public void onClick(View v) {
        new Subscriber().execute(hashtags.getText().toString());
    }


    private class Subscriber extends AsyncTask<String, String ,String>{

        String toSub;
        Socket requestSocket = null;
        ObjectInputStream in = null;
        ObjectOutputStream out = null;
        @Override
        protected String doInBackground(String... strings) {
            toSub = strings[0];
            int toSubHaSH = Util.getModMd5(toSub);
            ArrayList<Integer> hashes = new ArrayList<>();
            for (String ip : brokers){
                hashes.add(Util.getModMd5(ip));
            }
            Collections.sort(hashes);
            /*for (int hash:hashes){
                if (toSubHaSH<hash){

                }
            }*/
            try {
                requestSocket = new Socket("10.0.2.2",4000);
                in  = new ObjectInputStream(requestSocket.getInputStream());
                out = new ObjectOutputStream(requestSocket.getOutputStream());

                out.writeByte(7);
                out.flush();

                out.writeObject(myChannelName);
                out.flush();
                
                out.writeObject(toSub);
                out.flush();

            } catch (IOException e) {
                e.printStackTrace();

            }
            return null;
        }
    }
}
