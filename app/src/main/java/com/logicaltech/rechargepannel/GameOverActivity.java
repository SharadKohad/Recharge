package com.logicaltech.rechargepannel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameOverActivity extends AppCompatActivity
{
    public int  scorevalue ;
    private Button StartGameAgain,Btn_Home;
    private TextView tvscore;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        StartGameAgain = (Button) findViewById(R.id.play_again_btn);
        tvscore = (TextView) findViewById(R.id.score);
        Btn_Home = (Button) findViewById(R.id.btn_home_btn);
        scorevalue = getIntent().getExtras().getInt("score");
        StartGameAgain.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(GameOverActivity.this,JumpFishActivity.class);
                startActivity(intent);
            }
        });
        tvscore.setText("Score: "+scorevalue);

        Btn_Home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(GameOverActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
