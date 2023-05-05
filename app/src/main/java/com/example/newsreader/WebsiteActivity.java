package com.example.newsreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebsiteActivity extends AppCompatActivity {

    // WebView to display the website
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website);

        // Get the intent that started this activity
        Intent intent = getIntent();

        // Check if the intent is not null
        if(intent != null){
            // Get the URL from the intent extra
            String url = intent.getStringExtra("url");
            // Check if the URL is not null
            if(url != null){
                // Find the WebView in the layout and set it up
                webView = findViewById(R.id.webView);
                // Set WebViewClient so the website will be opened inside the app
                webView.setWebViewClient(new WebViewClient());
                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadUrl(url);
            }
        }

    }

    @Override
    public void onBackPressed() {
        // Check if the WebView can go back, go back
        if(webView.canGoBack()){
            getObbDir();
        }else {
            // Otherwise, call the parent implementation
            super.onBackPressed();
        }
    }
}