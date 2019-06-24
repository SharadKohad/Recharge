package com.logicaltech.gamerecharge;

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
        init();
    }

    public void init() {
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
        LL_fiveteen = (LinearLayout) findViewById(R.id.linear_layout_fiftheen);

        imageView_Back_Arrow = (ImageView) findViewById(R.id.img_back_arrow_contest_detail);

        imageView_Back_Arrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
    }

    public void buttonClicked(View view) {
        if (view.getId() == R.id.linear_layout_jump_fish)
        {
            Intent intent = new Intent(GameZoneActivity.this,WebView2048Activity.class);
            intent.putExtra("url","http://preview.codecanyon.net/item/snake-html5-game/full_screen_preview/13266764");
            intent.putExtra("gtype","4");
            intent.putExtra("srno","7");
            intent.putExtra("cob","0");
            startActivity(intent);
        }
        else if (view.getId() == R.id.linear_layout_2048)
        {
            Intent intent = new Intent(GameZoneActivity.this,WebView2048Activity.class);
            intent.putExtra("url","http://showcase.codethislab.com/games/katana_fruit/");
            intent.putExtra("gtype","4");
            intent.putExtra("srno","7");
            intent.putExtra("cob","0");
            startActivity(intent);
            //button2 action
        }
        else if (view.getId() == R.id.linear_layout_three_game)
        {
            Intent intent = new Intent(GameZoneActivity.this,WebView2048Activity.class);
            intent.putExtra("url","https://polargames.com.br/preview/cannonball/");
            intent.putExtra("gtype","4");
            intent.putExtra("srno","7");
            intent.putExtra("cob","0");
            startActivity(intent);
        }
        else if(view.getId() == R.id.linear_layout_jump_game_four)
        {
            Intent intent = new Intent(GameZoneActivity.this,WebView2048Activity.class);
            intent.putExtra("url","http://showcase.codethislab.com/games/brick_out/");
            intent.putExtra("gtype","4");
            intent.putExtra("srno","7");
            intent.putExtra("cob","0");
            startActivity(intent);
        }
        else if(view.getId() == R.id.linear_layout_game_five)
        {
            Intent intent = new Intent(GameZoneActivity.this,WebView2048Activity.class);
            intent.putExtra("url","http://preview.codecanyon.net/item/jumpy-car-html5-game-admob-construct-2-capx/full_screen_preview/17236957");
            intent.putExtra("gtype","4");
            intent.putExtra("srno","7");
            intent.putExtra("cob","0");
            startActivity(intent);
        }
        else if(view.getId() == R.id.linear_layout_six)
        {
            Intent intent = new Intent(GameZoneActivity.this,WebView2048Activity.class);
            intent.putExtra("url","http://preview.codecanyon.net/item/fruit-matching-html5-matching-game/full_screen_preview/13885226");
            intent.putExtra("gtype","4");
            intent.putExtra("srno","7");
            intent.putExtra("cob","0");
            startActivity(intent);
        }
        else if (view.getId() == R.id.linear_layout_jump_game_seven)
        {
            Intent intent = new Intent(GameZoneActivity.this,WebView2048Activity.class);
            intent.putExtra("url","https://preview.codecanyon.net/item/monster-truck-soccer/full_screen_preview/21094890");
            intent.putExtra("gtype","4");
            intent.putExtra("srno","7");
            intent.putExtra("cob","0");
            startActivity(intent);
        }
        else if (view.getId() == R.id.linear_layout_game_eight)
        {
            Intent intent = new Intent(GameZoneActivity.this,WebView2048Activity.class);
            intent.putExtra("url","http://preview.codecanyon.net/item/run-into-death-html5-shooter-game/full_screen_preview/18486533");
            intent.putExtra("gtype","4");
            intent.putExtra("srno","8");
            intent.putExtra("cob","0");
            startActivity(intent);
        }
        else if (view.getId() == R.id.linear_layout_nine)
        {
            Intent intent = new Intent(GameZoneActivity.this,WebView2048Activity.class);
            intent.putExtra("url","http://preview.codecanyon.net/item/playful-kitty-html5-construct-game/full_screen_preview/13731155");
            intent.putExtra("gtype","4");
            intent.putExtra("srno","8");
            intent.putExtra("cob","0");
            startActivity(intent);
        }
        else if (view.getId() == R.id.linear_layout_jump_game_ten)
        {
            Intent intent = new Intent(GameZoneActivity.this,WebView2048Activity.class);
            intent.putExtra("url","http://preview.codecanyon.net/item/super-titagon-html5-game-admob/full_screen_preview/16889568");
            intent.putExtra("gtype","4");
            intent.putExtra("srno","8");
            intent.putExtra("cob","0");
            startActivity(intent);
        }
        else if (view.getId() == R.id.linear_layout_game_eleven)
        {
            Intent intent = new Intent(GameZoneActivity.this,WebView2048Activity.class);
            intent.putExtra("url","http://preview.codecanyon.net/item/little-world-jellys/full_screen_preview/19183089");
            intent.putExtra("gtype","4");
            intent.putExtra("srno","8");
            intent.putExtra("cob","0");
            startActivity(intent);
        }
        else if (view.getId() == R.id.linear_layout_twelve)
        {
            Intent intent = new Intent(GameZoneActivity.this,WebView2048Activity.class);
            intent.putExtra("url","https://preview.codecanyon.net/item/ninja-action-2-html5-game-capx/full_screen_preview/22060541");
            intent.putExtra("gtype","4");
            intent.putExtra("srno","8");
            intent.putExtra("cob","0");
            startActivity(intent);
        }
        else if (view.getId() == R.id.linear_layout_jump_game_thirteen)
        {
            Intent intent = new Intent(GameZoneActivity.this,WebView2048Activity.class);
            intent.putExtra("url","http://preview.codecanyon.net/item/knife-break/full_screen_preview/21381690");
            intent.putExtra("gtype","4");
            intent.putExtra("srno","8");
            intent.putExtra("cob","0");
            startActivity(intent);
        }
        else if (view.getId() == R.id.linear_layout_game_fourteen)
        {
            Intent intent = new Intent(GameZoneActivity.this,WebView2048Activity.class);
            intent.putExtra("url","https://preview.codecanyon.net/item/piggy-night-html5-game-capx/full_screen_preview/23243816");
            intent.putExtra("gtype","4");
            intent.putExtra("srno","8");
            intent.putExtra("cob","0");
            startActivity(intent);
        }
        else if (view.getId() == R.id.linear_layout_fiftheen)
        {
            Intent intent = new Intent(GameZoneActivity.this,WebView2048Activity.class);
            intent.putExtra("url","http://showcase.codethislab.com/games/cricket_batter_challenge/");
            intent.putExtra("gtype","4");
            intent.putExtra("srno","8");
            intent.putExtra("cob","0");
            startActivity(intent);
        }
    }

}
