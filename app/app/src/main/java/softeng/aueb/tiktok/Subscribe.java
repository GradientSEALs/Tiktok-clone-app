package softeng.aueb.tiktok;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Comparator;
import java.util.TreeMap;


public class Subscribe extends Fragment implements View.OnClickListener{

    EditText hashtags;
    Button sub;
    View view;
    int port;
    String myChannelName;
    TreeMap<Integer, String> brokers = new TreeMap<>();

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
        brokers.put(Util.getModMd5("192.168.1.4,4000"),"192.168.1.4,4000");
        brokers.put(Util.getModMd5("192.168.1.4,4001"),"192.168.1.4,4001");
        brokers.put(Util.getModMd5("192.168.1.4,4002"),"192.168.1.4,4002");
    }

    @Override
    public void onClick(View v) {
        Subscriber s = new Subscriber();
        s.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,hashtags.getText().toString());
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
            toSubHaSH /= 3;
            Log.v("NNAMe",Integer.toString(toSubHaSH));
            String temp="";
            for (int i : brokers.keySet()){
                if (toSubHaSH < i){
                    temp = brokers.get(i);
                    break;
                }
            }
            String[] temp2 = temp.split(",");
            port = Integer.parseInt(temp2[1]);
            Log.v("tag",myChannelName);
            try {
                requestSocket = new Socket("192.168.1.4",port);
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

