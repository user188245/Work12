package com.work.user.work12;

import android.content.Intent;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener{

    EditText e1;
    TextView e2;
    Button b2;

    Handler handler = new Handler();
    MThread thread;

    class MThread extends Thread{

        String urlstr;

        public MThread(String url){
            this.urlstr = url;
        }


        @Override
        public void run(){
            try {
                URL url = new URL(urlstr);
                HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(10000);
                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    final String data = readData(urlConnection.getInputStream());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            e2.setText(data);
                        }
                    });
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
            }
    }
        String readData(InputStream is){
            String data = "";
            Scanner s = new Scanner(is);
            while(s.hasNext()) data += s.nextLine() + "\n";
            s.close();
            return data;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.enableDefaults();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        e1 = (EditText) findViewById(R.id.e1);
        e2 = (TextView) findViewById(R.id.e2);
        b2 = (Button) findViewById(R.id.b2);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,1,0,"다음").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(MainActivity2.this,MainActivity3.class);
                startActivity(intent);
                return true;
            }
        });
        return true;
    }

    @Override
    public void onClick(View v) {
        handler.post(new MThread(e1.getText().toString()));
    }
}
