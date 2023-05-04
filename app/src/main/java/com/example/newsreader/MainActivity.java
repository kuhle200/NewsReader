package com.example.newsreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);

    }

    private class GetNews extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            InputStream inputStream = getInputStream();

            if(inputStream != null){
                try {
                    initXMLPullParser(inputStream);
                } catch (XmlPullParserException | IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        private InputStream getInputStream(){

            try {
                URL url = new URL("https://hnrss.org/newest");
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                return connection.getInputStream();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private void initXMLPullParser(InputStream inputStream) throws XmlPullParserException, IOException {
            Log.d(TAG, "initializing XML Pull Parser");

            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
            parser.setInput(inputStream,null);
            parser.next();

            parser.require(XmlPullParser.START_TAG, null, "rss");

            while (parser.next() != XmlPullParser.END_TAG){
                if(parser.getEventType() != XmlPullParser.START_TAG){
                    continue;
                }

                parser.require(XmlPullParser.START_TAG, null, "channel");
                while (parser.next() != XmlPullParser.END_TAG){
                    if(parser.getEventType() != XmlPullParser.START_TAG){
                        continue;
                    }

                    if(parser.getName().equals("item")){
                        parser.require(XmlPullParser.START_TAG, null, "item");

                        String title = "";
                        String description = "";
                        String link = "";
                        String date = "";

                        while (parser.next() != XmlPullParser.END_TAG){
                            if(parser.getEventType() != XmlPullParser.START_TAG){
                                continue;
                            }

                            String tagName = parser.getName();
                            if(tagName.equals("title")){
                                // TODO: 2023/05/04 get content

                            }else if (tagName.equals("description")){
                                // TODO: 2023/05/04 get content

                            }else if(tagName.equals("link")){
                                // TODO: 2023/05/04 get content

                            }else if (tagName.equals("pubdate")){
                                // TODO: 2023/05/04 get content

                            }else {
                                // TODO: 2023/05/04 skip the tag
                            }

                        }


                    }else{
                        // TODO: 2023/05/04 skip tag
                    }

                }


            }
        }

    }

}