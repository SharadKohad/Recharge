package com.logicaltech.rechargepannel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebView2048Activity extends AppCompatActivity
{
    WebView mywebview;
    String current_url,srno;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view2048);
        mywebview = (WebView)findViewById(R.id.webView2048);
        current_url = getIntent().getStringExtra("url");
        srno = getIntent().getStringExtra("srno");
        mywebview  = new WebView(this);
        mywebview.getSettings().setJavaScriptEnabled(true); // enable javascript
        mywebview .loadUrl(current_url);
        setContentView(mywebview);
        mywebview.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                if (url.contains("bestscore"))
                {
                    Intent intent = new Intent(WebView2048Activity.this,GameOverActivity.class);
                    startActivity(intent);
                }
                else if(url.contains("fail"))
                {
                    finish();
                }
                return super.shouldOverrideUrlLoading(view,url);
            }
        });

    }
}
