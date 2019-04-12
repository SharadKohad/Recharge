package com.logicaltech.rechargepannel;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import util.SessionManeger;

public class SpashActivity extends AppCompatActivity
{
    Handler handler;
    LinearLayout li1,li2;
    Animation uptodown,downtoup;
    SessionManeger sessionManeger;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash);
        sessionManeger = new SessionManeger(getApplicationContext());

        li1 = (LinearLayout) findViewById(R.id.li1);
        li2 = (LinearLayout) findViewById(R.id.li2);
        uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        downtoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);
        li1.setAnimation(uptodown);
        li2.setAnimation(downtoup);
        handler=new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if (sessionManeger.checkLogin())
                {
                    Intent intent=new Intent(SpashActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Intent intent=new Intent(SpashActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },2000);
    }
}
