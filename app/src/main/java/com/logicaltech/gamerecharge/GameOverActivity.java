package com.logicaltech.gamerecharge;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.gson.JsonArray;

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
    String userId,srno,sessionScoreFish="0",gtype,userName;
    int topScore=0;
    RecyclerView RecyclerView_Top_Three_Contest;
    ArrayList<TopScoreModel> arrayList =new ArrayList<>();
    RelativeLayout RL_Top_Score;
    Intent intent;
    LinearLayout LL_whatup_status,LL_whatups,LL_share;
    ImageView IV_Close,IV_ScoreBoard;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        sessionManeger = new SessionManeger(getApplicationContext());
        HashMap<String, String> hashMap = sessionManeger.getUserDetails();
     //   HashMap<String, String> hashMap1 = sessionManeger.getFlyFishHighScore();
        userId = hashMap.get(SessionManeger.MEMBER_ID);
        userName = hashMap.get(SessionManeger.KEY_NAME);
        //  sessionScoreFish = hashMap1.get(SessionManeger.FLY_FISH_SCORE);
        //topScore =  Integer.parseInt(sessionScoreFish);
        progressBar = (ProgressBar) findViewById(R.id.progrebar_gameover);
        StartGameAgain = (Button) findViewById(R.id.play_again_btn);
        tvscore = (TextView) findViewById(R.id.score);
        TV_High_Score = (TextView) findViewById(R.id.highscore);
        RL_Top_Score = (RelativeLayout) findViewById(R.id.rl_top_score_game_over);
        Btn_Home = (Button) findViewById(R.id.btn_home_btn);
        LL_whatup_status = (LinearLayout) findViewById(R.id.ll_wahtup_status);
        LL_whatups = (LinearLayout) findViewById(R.id.ll_wahtups);
        LL_share = (LinearLayout) findViewById(R.id.ll_share);
        IV_Close = (ImageView) findViewById(R.id.img_home);
        IV_ScoreBoard = (ImageView) findViewById(R.id.img_scoreladder);

        scorevalue = getIntent().getExtras().getInt("score");
        srno = getIntent().getExtras().getString("srno");
        gtype = getIntent().getExtras().getString("gtype");

        RecyclerView_Top_Three_Contest = (RecyclerView) findViewById(R.id.rv_top_three_score_gover);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView_Top_Three_Contest.setLayoutManager(horizontalLayoutManagaer);

        topScorePerticulerContst(srno);

        getHighScoreByContest(userId,srno);

        submitScore(userId,srno,Integer.toString(scorevalue));

        StartGameAgain.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (gtype.equals("1"))
                {
                    intent = new Intent(GameOverActivity.this,JumpFishActivity.class);
                    intent.putExtra("srno",srno);
                    startActivity(intent);
                }
                else if(gtype.equals("2"))
                {
                    Intent intent = new Intent(GameOverActivity.this,WebView2048Activity.class);
                    intent.putExtra("url","http://site0.bidbch.com/games/2048/index.html");
                    intent.putExtra("gtype",gtype);
                    intent.putExtra("srno",srno);
                    intent.putExtra("cob","0");
                    startActivity(intent);
                }
                else if(gtype.equals("3"))
                {
                    Intent intent = new Intent(GameOverActivity.this,WebView2048Activity.class);
                    intent.putExtra("url","http://site0.bidbch.com/games/catchdots/index.html");
                    intent.putExtra("gtype",gtype);
                    intent.putExtra("srno",srno);
                    intent.putExtra("cob","0");
                    startActivity(intent);
                }
                else if(gtype.equals("4"))
                {
                    Intent intent = new Intent(GameOverActivity.this,WebView2048Activity.class);
                    intent.putExtra("url","http://site0.bidbch.com/games/fastarrow/index.html");
                    intent.putExtra("gtype",gtype);
                    intent.putExtra("srno",srno);
                    intent.putExtra("cob","0");
                    startActivity(intent);
                }
                else if(gtype.equals("5"))
                {
                    Intent intent = new Intent(GameOverActivity.this,WebView2048Activity.class);
                    intent.putExtra("url","http://site0.bidbch.com/games/pingpong/index.html");
                    intent.putExtra("gtype",gtype);
                    intent.putExtra("srno",srno);
                    intent.putExtra("cob","0");
                    startActivity(intent);
                }
                else if(gtype.equals("7"))
                {
                    Intent intent = new Intent(GameOverActivity.this,WebView2048Activity.class);
                    intent.putExtra("url","http://site0.bidbch.com/games/DotsPong/index.html");
                    intent.putExtra("gtype",gtype);
                    intent.putExtra("srno",srno);
                    intent.putExtra("cob","0");
                    startActivity(intent);
                }
                else if(gtype.equals("8"))
                {
                    Intent intent = new Intent(GameOverActivity.this,WebView2048Activity.class);
                    intent.putExtra("url","http://site0.bidbch.com/games/DotsAttack/index.html");
                    intent.putExtra("gtype",gtype);
                    intent.putExtra("srno",srno);
                    intent.putExtra("cob","0");
                    startActivity(intent);
                }
                else if(gtype.equals("9"))
                {
                    Intent intent = new Intent(GameOverActivity.this,WebView2048Activity.class);
                    intent.putExtra("url","http://site0.bidbch.com/games/JumpNinjaHero/index.html");
                    intent.putExtra("gtype",gtype);
                    intent.putExtra("srno",srno);
                    intent.putExtra("cob","0");
                    startActivity(intent);
                }
                else if(gtype.equals("11"))
                {
                    Intent intent = new Intent(GameOverActivity.this,WebView2048Activity.class);
                    intent.putExtra("url","http://site0.bidbch.com/games/ShotPong/index.html");
                    intent.putExtra("gtype",gtype);
                    intent.putExtra("srno",srno);
                    intent.putExtra("cob","0");
                    startActivity(intent);
                }
                else if(gtype.equals("12"))
                {
                    Intent intent = new Intent(GameOverActivity.this,WebView2048Activity.class);
                    intent.putExtra("url","http://site0.bidbch.com/games/FlyingTriangle/index.html");
                    intent.putExtra("gtype",gtype);
                    intent.putExtra("srno",srno);
                    intent.putExtra("cob","0");
                    startActivity(intent);
                }
                else if(gtype.equals("13"))
                {
                    Intent intent = new Intent(GameOverActivity.this,WebView2048Activity.class);
                    intent.putExtra("url","http://site0.bidbch.com/games/CrazyChicks/index.html");
                    intent.putExtra("gtype",gtype);
                    intent.putExtra("srno",srno);
                    intent.putExtra("cob","0");
                    startActivity(intent);
                }
                else if(gtype.equals("14"))
                {
                    Intent intent = new Intent(GameOverActivity.this,WebView2048Activity.class);
                    intent.putExtra("url","http://site0.bidbch.com/games/BrightBall/index.html");
                    intent.putExtra("gtype",gtype);
                    intent.putExtra("srno",srno);
                    intent.putExtra("cob","0");
                    startActivity(intent);
                }
                else if(gtype.equals("15"))
                {
                    Intent intent = new Intent(GameOverActivity.this,WebView2048Activity.class);
                    intent.putExtra("url","http://site0.bidbch.com/games/penaltykick/index.html");
                    intent.putExtra("gtype",gtype);
                    intent.putExtra("srno",srno);
                    intent.putExtra("cob","0");
                    startActivity(intent);
                }
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
                finish();
            }
        });

        IV_Close.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(GameOverActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        IV_ScoreBoard.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(GameOverActivity.this,TopScoreActivity.class);
                intent.putExtra("price","100");
                intent.putExtra("srno",srno);
                startActivity(intent);
                finish();
            }
        });

        LL_whatup_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.setPackage("com.whatsapp");           // so that only Whatsapp reacts and not the chooser
                i.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                i.putExtra(Intent.EXTRA_TEXT, "Here get 100 Tokens to play with me to GOC. Click the link "+"http://www.arenaitech.com/"+ " to download the App and use my invite code "+userName+ " to register.");
                startActivity(i);
            }
        });

        LL_whatups.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.setPackage("com.whatsapp");           // so that only Whatsapp reacts and not the chooser
                i.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                i.putExtra(Intent.EXTRA_TEXT, "Here get 50 Tokens and 50 Rs bounce cash to play with me to Elit Play. Click the link "+"http://www.arenaitech.com/"+ " to download the App and use my invite code "+userName+ " to register.");
                startActivity(i);
            }
        });

        LL_share.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
            intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "Here get 50 Tokens and 50 Rs bounce cash to play with me to Elit Play. Click the link "+"http://www.arenaitech.com/"+ " to download the App and use my invite code "+userName+ " to register.");
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out this site!");
            startActivity(Intent.createChooser(intent, "Share"));
            }
        });
    }

    public void submitScore(final String MemberCode, final String Srno, final String Score) {
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
                    /*else
                    {
                        Toast.makeText(GameOverActivity.this," "+message,Toast.LENGTH_SHORT).show();
                    }*/
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
        try
            {
                JSONArray response = Constant.jsonArrayTopThreePlayer;
                String res = response.toString();
                if (res.equals("[]"))
                {

                }
                else
                {
                        RL_Top_Score.setVisibility(View.VISIBLE);
                        String tempscore="0",temprank="0",tempusername = "",tempUserfile = "";
                        arrayList.clear();
                        for (int i = 0; i < response.length(); i++)
                        {
                            JSONObject jsonObject2 = response.getJSONObject(i);
                            if (i==0)
                            {
                                String score = jsonObject2.getString("score");
                                String username = jsonObject2.getString("username");
                                String userFile = jsonObject2.getString("userFile");

                                TopScoreModel model = new TopScoreModel();
                                temprank = "1";
                                tempscore = score;
                                tempusername = username;
                                tempUserfile = userFile;

                            }
                            else if (i==1)
                            {
                                String score = jsonObject2.getString("score");
                                String username = jsonObject2.getString("username");
                                String userFile = jsonObject2.getString("userFile");

                                TopScoreModel model = new TopScoreModel();
                                model.setRank("2");
                                model.setScore(score);
                                model.setUsername(username);
                                model.setUserFile(userFile);
                                arrayList.add(model);

                                TopScoreModel model1 = new TopScoreModel();
                                model1.setRank(temprank);
                                model1.setScore(tempscore);
                                model1.setUsername(tempusername);
                                model1.setUserFile(tempUserfile);
                                arrayList.add(model1);
                            }
                            else
                            {
                                String score = jsonObject2.getString("score");
                                String username = jsonObject2.getString("username");
                                String userFile = jsonObject2.getString("userFile");

                                TopScoreModel model = new TopScoreModel();
                                model.setRank("3");
                                model.setScore(score);
                                model.setUsername(username);
                                model.setUserFile(userFile);
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

    /*public void topScorePerticulerContst(final String srno) {
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
                        String tempscore="0",temprank="0",tempusername = "",tempUserfile = "";
                        arrayList.clear();
                        for (int i = 0; i < response.length(); i++)
                        {
                            JSONObject jsonObject2 = response.getJSONObject(i);
                            if (i==0)
                            {
                                String score = jsonObject2.getString("score");
                                String username = jsonObject2.getString("username");
                                String userFile = jsonObject2.getString("userFile");

                                TopScoreModel model = new TopScoreModel();
                                temprank = "1";
                                tempscore = score;
                                tempusername = username;
                                tempUserfile = userFile;

                            }
                            else if (i==1)
                            {
                                String score = jsonObject2.getString("score");
                                String username = jsonObject2.getString("username");
                                String userFile = jsonObject2.getString("userFile");

                                TopScoreModel model = new TopScoreModel();
                                model.setRank("2");
                                model.setScore(score);
                                model.setUsername(username);
                                model.setUserFile(userFile);
                                arrayList.add(model);

                                TopScoreModel model1 = new TopScoreModel();
                                model1.setRank(temprank);
                                model1.setScore(tempscore);
                                model1.setUsername(tempusername);
                                model1.setUserFile(tempUserfile);
                                arrayList.add(model1);
                            }
                            else
                            {
                                String score = jsonObject2.getString("score");
                                String username = jsonObject2.getString("username");
                                String userFile = jsonObject2.getString("userFile");

                                TopScoreModel model = new TopScoreModel();
                                model.setRank("3");
                                model.setScore(score);
                                model.setUsername(username);
                                model.setUserFile(userFile);
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
    }*/
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit game?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface arg0, int arg1)
                    {
                        intent = new Intent(GameOverActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                }).create().show();
    }

    public void getHighScoreByContest(final String MemberCode, final String Srno) {
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
                        sessionScoreFish = jsonObject.getString("score");
                        if (sessionScoreFish.equals("null"))
                        {
                            TV_High_Score.setText(""+scorevalue);
                        }
                        else
                        {
                            topScore =  Integer.parseInt(sessionScoreFish);
                            if (topScore<=scorevalue)
                            {
                                // sessionManeger.createSessionFlyFish(Integer.toString(scorevalue));
                                TV_High_Score.setText(""+scorevalue);
                            }
                            else
                            {
                                TV_High_Score.setText(""+topScore);
                            }
                        }
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

}
