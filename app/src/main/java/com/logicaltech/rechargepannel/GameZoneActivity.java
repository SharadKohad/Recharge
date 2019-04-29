package com.logicaltech.rechargepannel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GameZoneActivity extends AppCompatActivity
{
    LinearLayout LL_jump_fish,LL_2048,LL_Three,LL_four,LL_Five,LL_Six,LL_Seven,LL_Eight,LL_Nine,LL_Ten,LL_eleven,LL_twelve,LL_thirtten,LL_fourtheen,LL_fiveteen;
    Intent intent;
    ImageView imageView_Back_Arrow;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_zone);
        LL_jump_fish = (LinearLayout) findViewById(R.id.linear_layout_jump_fish);
        LL_2048 = (LinearLayout) findViewById(R.id.linear_layout_2048);
        LL_Three = (LinearLayout) findViewById(R.id.linear_layout_three_game);
        LL_four = (LinearLayout) findViewById(R.id.linear_layout_jump_game_four);
        LL_Five = (LinearLayout) findViewById(R.id.linear_layout_game_five);
        LL_Six = (LinearLayout) findViewById(R.id.linear_layout_six);
        LL_Seven = (LinearLayout) findViewById(R.id.linear_layout_jump_game_seven);
        LL_Eight = (LinearLayout) findViewById(R.id.linear_layout_game_eight);
        LL_Nine = (LinearLayout) findViewById(R.id.linear_layout_nine);
        LL_Ten = (LinearLayout) findViewById(R.id.linear_layout_jump_game_ten);
        LL_eleven = (LinearLayout) findViewById(R.id.linear_layout_game_eleven);
        LL_twelve = (LinearLayout) findViewById(R.id.linear_layout_twelve);
        LL_thirtten = (LinearLayout) findViewById(R.id.linear_layout_jump_game_thirteen);
        LL_fourtheen = (LinearLayout) findViewById(R.id.linear_layout_game_fourteen);
        LL_fiveteen = (LinearLayout) findViewById(R.id.linear_layout_fiveteen);

        imageView_Back_Arrow = (ImageView) findViewById(R.id.img_back_arrow_contest_detail);

        imageView_Back_Arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        LL_jump_fish.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
              //  intent = new Intent(GameZoneActivity.this,JumpFishActivity.class);
                intent = new Intent(GameZoneActivity.this,ContestListActivity.class);
                intent.putExtra("gtype","1");
                startActivity(intent);
            }
        });

        LL_2048.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameZoneActivity.this,WebView2048Activity.class);
                intent.putExtra("url","http://site17.bidbch.com/game/index.html");
                intent.putExtra("srno","2");
                startActivity(intent);
            }
        });

        LL_Three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameZoneActivity.this,WebView2048Activity.class);
                intent.putExtra("url","https://buy-instant-html5games.com/books-tower");
                intent.putExtra("srno","3");
                startActivity(intent);
            }
        });

        LL_four.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(GameZoneActivity.this,WebView2048Activity.class);
                intent.putExtra("url","https://buy-instant-html5games.com/fast-arrow");
                intent.putExtra("srno","4");
                startActivity(intent);
            }
        });

        LL_Five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameZoneActivity.this,WebView2048Activity.class);
                intent.putExtra("url","https://buy-instant-html5games.com/ping-pong");
                intent.putExtra("srno","5");
                startActivity(intent);
            }
        });

        LL_Six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameZoneActivity.this,WebView2048Activity.class);
                intent.putExtra("url","https://buy-instant-html5games.com/catch-dots");
                intent.putExtra("srno","6");
                startActivity(intent);
            }
        });

        LL_Seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameZoneActivity.this,WebView2048Activity.class);
                intent.putExtra("url","https://buy-instant-html5games.com/dots-pong");
                intent.putExtra("srno","7");
                startActivity(intent);
            }
        });

        LL_Eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameZoneActivity.this,WebView2048Activity.class);
                intent.putExtra("url","https://buy-instant-html5games.com/go-to-dot");
                intent.putExtra("srno","8");
                startActivity(intent);
            }
        });

        LL_Nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameZoneActivity.this,WebView2048Activity.class);
                intent.putExtra("url","https://buy-instant-html5games.com/color-circle");
                intent.putExtra("srno","9");
                startActivity(intent);
            }
        });

        LL_Ten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameZoneActivity.this,WebView2048Activity.class);
                intent.putExtra("url","https://buy-instant-html5games.com/retro-speed");
                intent.putExtra("srno","10");
                startActivity(intent);
            }
        });

        LL_eleven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameZoneActivity.this,WebView2048Activity.class);
                intent.putExtra("url","https://buy-instant-html5games.com/santa-runner");
                intent.putExtra("srno","11");
                startActivity(intent);
            }
        });

        LL_twelve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameZoneActivity.this,WebView2048Activity.class);
                intent.putExtra("url","https://buy-instant-html5games.com/save-rocket-html5-game");
                intent.putExtra("srno","12");
                startActivity(intent);
            }
        });

        LL_thirtten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameZoneActivity.this,WebView2048Activity.class);
                intent.putExtra("url","https://buy-instant-html5games.com/color-tower");
                intent.putExtra("srno","13");
                startActivity(intent);
            }
        });

        LL_fourtheen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameZoneActivity.this,WebView2048Activity.class);
                intent.putExtra("url","https://buy-instant-html5games.com/falling-bottle-challenge");
                intent.putExtra("srno","14");
                startActivity(intent);
            }
        });

        LL_fiveteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameZoneActivity.this,WebView2048Activity.class);
                intent.putExtra("url","https://buy-instant-html5games.com/saws");
                intent.putExtra("srno","15");
                startActivity(intent);
            }
        });
    }
}
