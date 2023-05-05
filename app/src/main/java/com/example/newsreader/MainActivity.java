package com.example.newsreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private ArrayList<NewsItem> news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize RecyclerView and NewsAdapter
        recyclerView = findViewById(R.id.recyclerView);
        news = new ArrayList<>();
        newsAdapter = new NewsAdapter(this);
        recyclerView.setAdapter(newsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize RecyclerView and NewsAdapter
        new GetNews().execute();
    }

    private class GetNews extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            InputStream inputStream = getInputStream();

            if(inputStream != null){
                try {
                    // Initialize XML Pull Parser to parse RSS feed data
                    initXMLPullParser(inputStream);
                } catch (XmlPullParserException | IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            // Update NewsAdapter with news data
            newsAdapter.setNewsItems(news);
        }

        private InputStream getInputStream(){

            try {
                // Create URL object for RSS feed
                URL url = new URL("https://hnrss.org/newest");
                // Open HTTP connection and set request method
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                // Get input stream from connection
                return connection.getInputStream();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private void initXMLPullParser(InputStream inputStream) throws XmlPullParserException, IOException {
            Log.d(TAG, "initializing XML Pull Parser");

            // Create XmlPullParser object and initialize it with input stream
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
            parser.setInput(inputStream,null);
            parser.next();

            // Require the start tag for "rss" element
            parser.require(XmlPullParser.START_TAG, null, "rss");

            while (parser.next() != XmlPullParser.END_TAG){
                if(parser.getEventType() != XmlPullParser.START_TAG){
                    continue;
                }

                // Require the start tag for "channel" element
                parser.require(XmlPullParser.START_TAG, null, "channel");
                while (parser.next() != XmlPullParser.END_TAG){
                    if(parser.getEventType() != XmlPullParser.START_TAG){
                        continue;
                    }

                    if(parser.getName().equals("item")){
                        //Require the start tag for "item" element
                        parser.require(XmlPullParser.START_TAG, null, "item");

                        String title = ""; // String to store title of the news item
                        String link = "";  // String to store link of the website of

                        while (parser.next() != XmlPullParser.END_TAG){
                            if(parser.getEventType() != XmlPullParser.START_TAG){
                                continue;
                            }

                            String tagName = parser.getName(); //string to store the target tags
                            if(tagName.equals("title")){
                                title = getContent(parser, "title"); //collecting the title of the Item

                            }else if(tagName.equals("link")){
                                link = getContent(parser, "link");  //collecting the link of the item

                            }else {
                                skipTag(parser); //skip tags that are not item
                            }

                        }
                        NewsItem item = new NewsItem(title,link); //Creating a new NewsItem after collecting the data required
                        news.add(item);

                    }else{
                        skipTag(parser);
                    }

                }


            }
        }

        private String getContent(XmlPullParser parser, String tagName) throws XmlPullParserException, IOException {
            String content ="";
            // Require the start tag for element
            parser.require(XmlPullParser.START_TAG, null, tagName);

            //Only collect content of the tag if next item in the parser is text
            if(parser.next() == XmlPullParser.TEXT){
                content = parser.getText();
                parser.next();
            }
            return content;
        }

        private void skipTag(XmlPullParser parser) throws XmlPullParserException, IOException {
            if (parser.getEventType() != XmlPullParser.START_TAG){
                throw new IllegalStateException();
            }

            int number = 1; //tracker

            while (number !=0){
                switch (parser.next()){

                    case(XmlPullParser.START_TAG):
                        number++; //increment when encounter START_TAG
                        break;

                    case(XmlPullParser.END_TAG):
                        number--; //decrement when encounter END_TAG
                        break;
                    default:
                        break;
                }
            }


        }
    }

}