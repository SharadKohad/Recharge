package com.logicaltech.gamerecharge;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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

public class HighScoreActivity extends AppCompatActivity
{
    TextView TV_HighScore_Text,TV_HighScore,TV_Last_High_Score;
    String srno,gtype,highScore,userId;
    int scorevalue;
    int topScore=0;
    SessionManeger sessionManeger;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_book);

        sessionManeger = new SessionManeger(getApplicationContext());
        handler=new Handler();
        HashMap<String, String> hashMap = sessionManeger.getUserDetails();
        //   HashMap<String, String> hashMap1 = sessionManeger.getFlyFishHighScore();
        userId = hashMap.get(SessionManeger.MEMBER_ID);
        TV_HighScore_Text = (TextView) findViewById(R.id.tv_new_high_score_text);
        TV_HighScore = (TextView) findViewById(R.id.tv_new_high_score);
        TV_Last_High_Score = (TextView) findViewById(R.id.tv_new_high_last);

        scorevalue = getIntent().getExtras().getInt("score");
        srno = getIntent().getExtras().getString("srno");
        gtype = getIntent().getExtras().getString("gtype");
        getHighScoreByContest(userId,srno);
    }

    public void getHighScoreByContest(final String MemberCode, final String Srno)
    {
        //progressBar.setVisibility(View.VISIBLE);
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = Constant.URL+"getPlayerHighscoreByContest?membercode="+MemberCode+"&ContestID="+Srno;
        StringRequest jsonObjRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    highScore = jsonObject.getString("score");
                    if (highScore.equals("null"))
                    {
                        TV_HighScore.setText(""+scorevalue);
                        TV_Last_High_Score.setVisibility(View.GONE);
                    }
                    else
                    {
                        TV_HighScore_Text.setText("NEW HI-SCORE");
                        topScore =  Integer.parseInt(highScore);
                        if (topScore<=scorevalue)
                        {
                            TV_HighScore.setText(""+scorevalue);
                         //   TV_Last_High_Score.setText("Latest Score "+topScore);
                            TV_Last_High_Score.setVisibility(View.GONE);
                        }
                        else
                        {
                            TV_HighScore.setText(""+topScore);
                            TV_Last_High_Score.setText("Latest Score "+scorevalue);
                        }
                    }
                    handler.postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {

                            //Toast.makeText(getContext(),"Game Over:",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(HighScoreActivity.this, GameOverActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("score",scorevalue);
                            intent.putExtra("srno", srno);
                            intent.putExtra("gtype",gtype);
                            startActivity(intent);

                        }
                    },3000);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
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
                return params;
            }
        };
        MySingalton.getInstance(getApplicationContext()).addRequestQueue(jsonObjRequest);
        jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(200000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyRequestQueue.add(jsonObjRequest);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("Really Exit?").setMessage("Are you sure you want to exit game?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface arg0, int arg1)
                    {
                        Intent intent = new Intent(HighScoreActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                }).create().show();
    }
}
