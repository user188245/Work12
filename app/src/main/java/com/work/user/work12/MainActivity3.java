package com.work.user.work12;

import android.content.Intent;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity3 extends AppCompatActivity implements View.OnClickListener{
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> array;
    ListView listView;

    Handler handler = new Handler();
    Thread thread = new Thread() {
        @Override
        public void run() {
            try {
                URL url = new
                        URL("https://news.google.com/news?cf=all&hl=ko&pz=1&ned=kr&topic=m&output=rss");
                HttpURLConnection urlConnection =
                        (HttpURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() ==
                        HttpURLConnection.HTTP_OK) {
                    int itemCount = readData(urlConnection.getInputStream());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            arrayAdapter.notifyDataSetChanged();
                        }
                    });
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"1:" + e.getClass().getSimpleName(),Toast.LENGTH_SHORT).show();
            }
        }

        int readData(InputStream is) {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            try {
                DocumentBuilder builder = builderFactory.newDocumentBuilder();
                Document document = builder.parse(is);
                int datacount = parseDocument(document);
                return datacount;
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"2:" +e.getClass().getSimpleName(),Toast.LENGTH_SHORT).show();
            }
            return 0;
        }

        private int parseDocument(Document doc) {
            Element docEle = doc.getDocumentElement();
            NodeList nodelist = docEle.getElementsByTagName("item");
            int count = 0;
            if ((nodelist != null) && (nodelist.getLength() > 0)) {
                for (int i = 0; i < nodelist.getLength(); i++) {
                    String newsItem = getTagData(nodelist, i);
                    if (newsItem != null) {
                        array.add(newsItem);
                        count++;
                    }
                }
            }
            return count;
        }

        private String getTagData(NodeList nodelist, int index) {
            String newsItem = null;
            try {
                Element entry = (Element) nodelist.item(index);
                Element title = (Element) entry.getElementsByTagName("title").item(0);
                Element pubDate = (Element) entry.getElementsByTagName("pubDate").item(0);
                String titleValue = null;
                if (title != null) {
                    Node firstChild = title.getFirstChild();
                    if (firstChild != null) titleValue = firstChild.getNodeValue();
                }
                String pubDateValue = null;
                if(pubDate != null){
                    Node firstChild = pubDate.getFirstChild();
                    if(firstChild != null) pubDateValue = firstChild.getNodeValue();
                }
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
                    Date date = new Date();
                    newsItem = titleValue + "-" + simpleDateFormat.format(date.parse(pubDateValue));
                }catch(IllegalArgumentException e){
                    newsItem = titleValue + "-" + pubDateValue.toString();
                }

            } catch (DOMException e) {
                e.printStackTrace();
            }
            return newsItem;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);


        array = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,array);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(arrayAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,0,0,"이전").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(MainActivity3.this,MainActivity2.class);
                startActivity(intent);
                return true;
            }
        });
        menu.add(0,1,0,"다음").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(MainActivity3.this,MainActivity4.class);
                startActivity(intent);
                return true;
            }
        });


        return true;
    }

    @Override
    public void onClick(View v) {
        array.clear();
        handler.post(thread);
    }
}
