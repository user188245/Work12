package com.work.user.work12;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class MainActivity4 extends AppCompatActivity implements View.OnClickListener{
    TextView textView1;
    EditText editText_id, editText_password;

    Handler handler = new Handler();
    Thread thread = new Thread() {
        @Override
        public void run() {
            try {
                URL url = new
                        URL("http://jerry1004.dothome.co.kr/info/login.php");
                HttpURLConnection httpURLConnection =
                        (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                String postData = "userid=" + URLEncoder.encode(editText_id.getText().toString())
                        + "&password=" + URLEncoder.encode(editText_password.getText().toString());
                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postData.getBytes());
                outputStream.flush();
                outputStream.close();
                InputStream inputStream;
                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
                    inputStream = httpURLConnection.getInputStream();
                else
                    inputStream = httpURLConnection.getErrorStream();
                final String result = loginResult(inputStream);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (result.equals("FAIL"))
                            textView1.setText("로그인이 실패했습니다.");
                        else
                            textView1.setText(result + "님 로그인 성공");
                    }
                });
            } catch (Exception e) {
            }


        }
        String loginResult(InputStream is){
            String data = "";
            Scanner s = new Scanner(is);
            while(s.hasNext()) data += s.nextLine();
            s.close();
            return data;
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,1,0,"이전").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(MainActivity4.this,MainActivity3.class);
                startActivity(intent);
                return true;
            }
        });
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        textView1 = (TextView) findViewById(R.id.textView1);
        editText_id = (EditText) findViewById(R.id.editText_id);
        editText_password = (EditText) findViewById(R.id.editText_pw);
    }

    @Override
    public void onClick(View v) {
        handler.post(thread);

    }
}
