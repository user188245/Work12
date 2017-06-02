package com.work.user.work12;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    final static String SERVER_IP = "0.0.0.0";
    final static int SERVER_PORT = 200;

    MyThread myThread;


    class MyThread implements Runnable{

        @Override
        public void run() {
            try {
                Socket socket = new Socket(SERVER_IP,SERVER_PORT);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                String msg = "Client>>" + editText.getText().toString();
                objectOutputStream.writeObject(msg);
                objectOutputStream.flush();

                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                final String data = (String) objectInputStream.readObject();
                myHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),data,Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    EditText editText;
    Button button;

    Handler myHandler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        button = (Button)findViewById(R.id.button);
        editText = (EditText)findViewById(R.id.editText);
        myHandler = new Handler();
        myThread = new MyThread();
    }

    @Override
    public void onClick(View v) {
        myHandler.post(myThread);
    }

}
