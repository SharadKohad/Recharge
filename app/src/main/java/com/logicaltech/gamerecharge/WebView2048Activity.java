package com.logicaltech.gamerecharge;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebView2048Activity extends AppCompatActivity
{
    WebView mywebview;
    String current_url,srno,gtype,cob="0";
    int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_web_view2048);
        mywebview = (WebView)findViewById(R.id.webView2048);
        current_url = getIntent().getStringExtra("url");
        srno = getIntent().getStringExtra("srno");
        gtype = getIntent().getStringExtra("gtype");
        cob = getIntent().getStringExtra("cob");
        mywebview  = new WebView(this);
        mywebview.getSettings().setJavaScriptEnabled(true); // enable javascript
        mywebview .loadUrl(current_url);
        setContentView(mywebview);
        if (gtype.equals("9")||gtype.equals("15"))
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        if (cob.equals("0")) {
            mywebview.setWebViewClient(new WebViewClient()
            {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url)
                {
                    String numberOnly= url.replaceAll("[^0-9]", "");
                    Intent intent = new Intent(WebView2048Activity.this,HighScoreActivity.class);
                    intent.putExtra("score",Integer.parseInt(numberOnly));
                    intent.putExtra("gtype",gtype);
                    intent.putExtra("srno",srno);
                    startActivity(intent);
                    finish();
                    return super.shouldOverrideUrlLoading(view,url);
                }
            });
        }
        else
            {
                mywebview.setWebViewClient(new WebViewClient()
            {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url)
                {
                    count++;
                    if (count==1)
                    {
                        String numberOnly= url.replaceAll("[^0-9]", "");
                        Intent intent = new Intent(WebView2048Activity.this,Battle_ResultActivity.class);
                        intent.putExtra("score",Integer.parseInt(numberOnly));
                        intent.putExtra("gtype",gtype);
                        intent.putExtra("srno",srno);
                        startActivity(intent);
                        System.out.println("if Count "+count);
                    }
                    else
                    {
                        System.out.println("Else Count "+count);
                    }
                    finish();
                    return super.shouldOverrideUrlLoading(view,url);
                }
            });
        }
    }
    @Override
    public void onBackPressed()
    {
        new AlertDialog.Builder(this).setTitle("Really Exit?").setMessage("Are you sure you want to exit game?")
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
