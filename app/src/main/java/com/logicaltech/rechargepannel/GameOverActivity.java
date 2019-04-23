package com.logicaltech.rechargepannel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adpter.TopthreeScoreAdapter;
import model.TopScoreModel;
import util.Constant;
import util.MySingalton;
import util.SessionManeger;

public class GameOverActivity extends AppCompatActivity
{
    public int  scorevalue ;
    private Button StartGameAgain,Btn_Home;
    private TextView tvscore,TV_High_Score;
    SessionManeger sessionManeger;
    ProgressBar progressBar;
    String userId,srno,sessionScoreFish="0";
    int topScore=0;
    RecyclerView RecyclerView_Top_Three_Contest;
    ArrayList<TopScoreModel> arrayList =new ArrayList<>();
    RelativeLayout RL_Top_Score;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        sessionManeger = new SessionManeger(getApplicationContext());
        HashMap<String, String> hashMap = sessionManeger.getUserDetails();
        HashMap<String, String> hashMap1 = sessionManeger.getFlyFishHighScore();
        userId = hashMap.get(SessionManeger.MEMBER_ID);
        sessionScoreFish = hashMap1.get(SessionManeger.FLY_FISH_SCORE);
        topScore =  Integer.parseInt(sessionScoreFish);
        progressBar = (ProgressBar) findViewById(R.id.progrebar_gameover);
        StartGameAgain = (Button) findViewById(R.id.play_again_btn);
        tvscore = (TextView) findViewById(R.id.score);
        TV_High_Score = (TextView) findViewById(R.id.highscore);
        RL_Top_Score = (RelativeLayout) findViewById(R.id.rl_top_score_game_over);
        Btn_Home = (Button) findViewById(R.id.btn_home_btn);
        scorevalue = getIntent().getExtras().getInt("score");
        srno = getIntent().getExtras().getString("srno");
        RecyclerView_Top_Three_Contest = (RecyclerView) findViewById(R.id.rv_top_three_score_gover);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView_Top_Three_Contest.setLayoutManager(horizontalLayoutManagaer);
        topScorePerticulerContst(srno);

        if (topScore<=0)
        {
            sessionManeger.createSessionFlyFish(Integer.toString(scorevalue));
            TV_High_Score.setText(""+scorevalue);
        }
        else if (topScore<scorevalue)
        {
            sessionManeger.createSessionFlyFish(Integer.toString(scorevalue));
            TV_High_Score.setText(""+scorevalue);
        }
        else
        {
            Toast.makeText(GameOverActivity.this,"High Score Submit ",Toast.LENGTH_SHORT).show();
            TV_High_Score.setText(""+topScore);
        }

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

        tvscore.setText(""+scorevalue);

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

    public void topScorePerticulerContst(final String srno) {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        //  String url = Constant.URL+"addSignUp"; // <----enter your post url here
        String url = Constant.URL+"getHighestScoreByContest?ContestID="+srno;
        JsonArrayRequest MyStringRequest = new JsonArrayRequest(Request.Method.POST, url, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                try
                {
                    String res = response.toString();
                    if (res.equals("[]"))
                    {

                    }
                    else
                    {
                        RL_Top_Score.setVisibility(View.VISIBLE);
                        String tempscore="0",temprank="0",tempusername = "";
                        arrayList.clear();
                        for (int i = 0; i < response.length(); i++)
                        {
                            JSONObject jsonObject2 = response.getJSONObject(i);
                            if (i==0)
                            {
                                String score = jsonObject2.getString("score");
                                String username = jsonObject2.getString("username");
                                TopScoreModel model = new TopScoreModel();
                                temprank = "1";
                                tempscore = score;
                                tempusername = username;
                            }
                            else if (i==1)
                            {
                                String score = jsonObject2.getString("score");
                                String username = jsonObject2.getString("username");
                                TopScoreModel model = new TopScoreModel();
                                model.setRank("2");
                                model.setScore(score);
                                model.setUsername(username);
                                arrayList.add(model);

                                TopScoreModel model1 = new TopScoreModel();
                                model1.setRank(temprank);
                                model1.setScore(tempscore);
                                model1.setUsername(tempusername);
                                arrayList.add(model1);
                            }
                            else
                            {
                                String score = jsonObject2.getString("score");
                                String username = jsonObject2.getString("username");
                                TopScoreModel model = new TopScoreModel();
                                model.setRank("3");
                                model.setScore(score);
                                model.setUsername(username);
                                arrayList.add(model);
                            }
                        }
                    }

                    TopthreeScoreAdapter operator_adapter = new TopthreeScoreAdapter(arrayList,getApplicationContext());
                    RecyclerView_Top_Three_Contest.setAdapter(operator_adapter);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error)
            {
                //This code is executed if there is an error.
                String message= "";
                if (error instanceof ServerError)
                {
                    message = "The server could not be found. Please try again after some time!!";
                }
                else if (error instanceof TimeoutError)
                {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
                System.out.println("error........"+error);
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Accept","application/json");
                headers.put("Content-Type","application/json");
                return headers;
            }
        };
        MyStringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,   DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyRequestQueue.add(MyStringRequest);
    }

}
