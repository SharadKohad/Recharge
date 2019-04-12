package com.logicaltech.rechargepannel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import util.SessionManeger;

public class AccountActivity extends AppCompatActivity
{
    Button Btn_logout;
    SessionManeger sessionManeger;
    ImageView Img_back_arrow;
    LinearLayout LL_ProfileDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        sessionManeger = new SessionManeger(getApplicationContext());
        init();
    }

    public void init()
    {
        Btn_logout = (Button) findViewById(R.id.button_logout);
        Img_back_arrow = (ImageView) findViewById(R.id.img_back_arrow_profile);
        LL_ProfileDetail = (LinearLayout) findViewById(R.id.linear_layout_view_profile);

        Img_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        LL_ProfileDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountActivity.this,ProfileDetailActivity.class);
                startActivity(intent);
            }
        });

        Btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(AccountActivity.this)
                        .setMessage("Are you sure you want to logout?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                sessionManeger.logoutUser();
                                finish();
                            }
                        }).setNegativeButton("No", null).show();
            }
        });
    }
}
