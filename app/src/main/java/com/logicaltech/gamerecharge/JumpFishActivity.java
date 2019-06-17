package com.logicaltech.gamerecharge;

import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import util.FlyingFishView;

public class JumpFishActivity extends AppCompatActivity
{
    FlyingFishView gameView;
    private Handler handler= new Handler();
    private final static long Interval = 30;
    public static String SRNO="";
    public static String GTYPE  ;
    public static String COB = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        gameView = new FlyingFishView(this);
        setContentView(gameView);
        Timer timer = new Timer();
        SRNO = getIntent().getExtras().getString("srno");
        GTYPE = getIntent().getExtras().getString("gtype");
        COB = getIntent().getExtras().getString("cob");

        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                handler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        gameView.invalidate();
                    }
                });
            }
        },0,Interval);
    }
    @Override
    public void onBackPressed()
    {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit game?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                }).create().show();
    }
}
