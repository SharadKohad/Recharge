package com.logicaltech.rechargepannel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class GameZoneActivity extends AppCompatActivity
{
    LinearLayout LL_jump_fish;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_zone);
        LL_jump_fish = (LinearLayout) findViewById(R.id.linear_layout_jump_fish);

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
    }
}
