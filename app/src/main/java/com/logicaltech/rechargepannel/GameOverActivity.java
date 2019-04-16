package com.logicaltech.rechargepannel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import util.Constant;
import util.MySingalton;
import util.SessionManeger;

public class GameOverActivity extends AppCompatActivity
{
    public int  scorevalue ;
    private Button StartGameAgain,Btn_Home;
    private TextView tvscore;
    SessionManeger sessionManeger;
    ProgressBar progressBar;
    String userId,srno,sessionScoreFish="0",session_srno="0";
    int topScore=0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        sessionManeger = new SessionManeger(getApplicationContext());
        HashMap<String, String> hashMap = sessionManeger.getUserDetails();
        HashMap<String, String> hashMap1 = sessionManeger.getFlyFishHighScore();
      //  HashMap<String, String> hashMap2 = sessionManeger.getCurrentSrno();
        userId = hashMap.get(SessionManeger.MEMBER_ID);
      //  sessionScoreFish = hashMap1.get(SessionManeger.FLY_FISH_SCORE);

        progressBar = (ProgressBar) findViewById(R.id.progrebar_gameover);
        StartGameAgain = (Button) findViewById(R.id.play_again_btn);
        tvscore = (TextView) findViewById(R.id.score);
        Btn_Home = (Button) findViewById(R.id.btn_home_btn);
        scorevalue = getIntent().getExtras().getInt("score");
        srno = getIntent().getExtras().getString("srno");
      //  sessionManeger.createSessionFlyFish(Integer.toString(scorevalue));
        submitScore(userId,srno,Integer.toString(scorevalue));

        StartGameAgain.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(GameOverActivity.this,JumpFishActivity.class);
                intent.putExtra("srno",srno);
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

    public void submitScore(final String MemberCode, final String Srno, final String Score)
    {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = Constant.URL+"addPlayedGameScore";
        StringRequest jsonObjRequest = new StringRequest(Request.Method.PUT,url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("msg");
                    if (status.equals("1"))
                    {
                        Toast.makeText(GameOverActivity.this,"Your score submit "+message,Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(GameOverActivity.this," "+message,Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e)
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        progressBar.setVisibility(View.INVISIBLE);
                        VolleyLog.d("volley", "Error: " + error.getMessage());
                        error.printStackTrace();
                    }
                }) {
            @Override
            public String getBodyContentType()
            {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("membercode", MemberCode);
                params.put("srno",Srno);
                params.put("score",Score);
                return params;
            }
        };
        MySingalton.getInstance(getApplicationContext()).addRequestQueue(jsonObjRequest);
        jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(200000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyRequestQueue.add(jsonObjRequest);
    }
}
