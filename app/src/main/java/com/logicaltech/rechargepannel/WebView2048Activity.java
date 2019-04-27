package com.logicaltech.rechargepannel;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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

    @Override
    public void onBackPressed()
    {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit game?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface arg0, int arg1)
                    {
                        Intent intent = new Intent(WebView2048Activity.this,MainActivity.class);
                        startActivity(intent);
                    }
                }).create().show();
    }
}
