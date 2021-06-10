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


public class Subscribe extends Fragment implements View.OnClickListener{

    EditText hashtags;
    Button sub;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.subscribe_layout,container,false);
        hashtags = view.findViewById(R.id.hashtags);
        sub = view.findViewById(R.id.sub);
        sub.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        new Subscriber().execute(hashtags.getText().toString());
    }


    private class Subscriber extends AsyncTask<String, String ,String>{

        String[] _hashtags;
        Socket requestSocket = null;
        ObjectInputStream in = null;
        ObjectOutputStream out = null;
        @Override
        protected String doInBackground(String... strings) {
            _hashtags = strings[0].split(" ");
            try {
                requestSocket = new Socket("10.0.2.2",4000);
                in  = new ObjectInputStream(requestSocket.getInputStream());
                out = new ObjectOutputStream(requestSocket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();

            }
            return null;
        }
    }
}
