package com.logicaltech.gamerecharge;

import android.app.Dialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
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

import adpter.PriceAdapter;
import adpter.TopthreeScoreAdapter;
import model.PriceModel;
import model.TopScoreModel;
import util.Constant;
import util.MySingalton;
import util.SessionManeger;

public class SingleContestDetailActivity extends AppCompatActivity
{
    String srno,userId,playerstatus="0",message,gametype;
    ArrayList<TopScoreModel> arrayList =new ArrayList<>();
    ArrayList<PriceModel> arrayList1 =new ArrayList<>();
    CardView CV_Price_Dis;
    RecyclerView RecyclerView_Top_Three_Contest,RV_Price_Distribution;
    ProgressBar progressBar;
    TextView TV_Price,TV_Total_Player,TV_Cotest_Join,TV_Game_Name,TV_Remaing_Time,TV_Contest_Amount_List,TV_Dis_Player,TV_Dis_Price;
    int join_contest_amt;
    ImageView img_back_arrow;
    RelativeLayout RL_play_game,RL_Video_Game,RL_Game_List;
    Dialog dialog;
    SessionManeger sessionManeger;
    GridLayoutManager mGridLayoutManagerBrand;
    WindowManager.LayoutParams lp;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_contest_detail);
        sessionManeger = new SessionManeger(getApplicationContext());
        HashMap<String, String> hashMap = sessionManeger.getUserDetails();
        userId = hashMap.get(SessionManeger.MEMBER_ID);
  //      progressBar = (ProgressBar) findViewById(R.id.progrebar_contest_detail);
        init();

        srno = getIntent().getExtras().getString("srno");
        gametype = getIntent().getExtras().getString("gametype");

        joinContestStatus(userId,srno);
        singleContestDetail(gametype,srno,"1");
        topScorePerticulerContst(srno);
        topScorePriceDistribution(srno);

        img_back_arrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        RL_play_game.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
               // joinContest(userId,srno);
                if (playerstatus.equals("1"))
                {
                    showCustomDialog();
                }
                else if(playerstatus.equals("2"))
                {
                    if (gametype.equals("1"))
                    {
                        Intent intent = new Intent(SingleContestDetailActivity.this,JumpFishActivity.class);
                        intent.putExtra("gtype",gametype);
                        intent.putExtra("srno",srno);
                        startActivity(intent);
                    }
                    else if(gametype.equals("2"))
                    {
                        Intent intent = new Intent(SingleContestDetailActivity.this,WebView2048Activity.class);
                        intent.putExtra("url","http://site0.bidbch.com/games/2048/index.html");
                        intent.putExtra("gtype",gametype);
                        intent.putExtra("srno",srno);
                        startActivity(intent);
                    }
                    else if(gametype.equals("3"))
                    {
                        Intent intent = new Intent(SingleContestDetailActivity.this,WebView2048Activity.class);
                        intent.putExtra("url","http://site0.bidbch.com/games/catchdots/index.html");
                        intent.putExtra("gtype",gametype);
                        intent.putExtra("srno",srno);
                        startActivity(intent);
                    }
                    else if(gametype.equals("4"))
                    {
                        Intent intent = new Intent(SingleContestDetailActivity.this,WebView2048Activity.class);
                        intent.putExtra("url","http://site0.bidbch.com/games/fastarrow/index.html");
                        intent.putExtra("gtype",gametype);
                        intent.putExtra("srno",srno);
                        startActivity(intent);
                    }
                    else if(gametype.equals("5"))
                    {
                        Intent intent = new Intent(SingleContestDetailActivity.this,WebView2048Activity.class);
                        intent.putExtra("url","http://site0.bidbch.com/games/pingpong/index.html");
                        intent.putExtra("gtype",gametype);
                        intent.putExtra("srno",srno);
                        startActivity(intent);
                    }
                    else if(gametype.equals("6"))
                    {
                        Intent intent = new Intent(SingleContestDetailActivity.this,WebView2048Activity.class);
                        intent.putExtra("url","http://site0.bidbch.com/games/pingpong/index.html");
                        intent.putExtra("gtype",gametype);
                        intent.putExtra("srno",srno);
                        startActivity(intent);
                    }
                    else if(gametype.equals("7"))
                    {
                        Intent intent = new Intent(SingleContestDetailActivity.this,WebView2048Activity.class);
                        intent.putExtra("url","http://site0.bidbch.com/games/DotsPong/index.html");
                        intent.putExtra("gtype",gametype);
                        intent.putExtra("srno",srno);
                        startActivity(intent);
                    }
                }
                else
                {
                    Toast.makeText(SingleContestDetailActivity.this," "+message,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void init() {
        TV_Price = (TextView) findViewById(R.id.tv_wining_price);
        TV_Total_Player  = (TextView) findViewById(R.id.tv_total_player);
        TV_Cotest_Join = (TextView) findViewById(R.id.tv_contest_amount);
        TV_Game_Name = (TextView) findViewById(R.id.tv_cotest_name);
        TV_Remaing_Time = (TextView) findViewById(R.id.tv_remaing_time_contest_detail);
        TV_Dis_Player = (TextView) findViewById(R.id.tv_total_player_price_distribution);
        img_back_arrow = (ImageView) findViewById(R.id.img_back_arrow_contest_detail);
        RL_play_game = (RelativeLayout) findViewById(R.id.rl_play_game);
        CV_Price_Dis = (CardView) findViewById(R.id.cv_cotest_price_distribution);
        progressBar = (ProgressBar) findViewById(R.id.progrebar_single_contest);
        RL_Video_Game = (RelativeLayout) findViewById(R.id.rl_all_how_to_play);
        RecyclerView_Top_Three_Contest = (RecyclerView) findViewById(R.id.rv_top_three_score);
        RV_Price_Distribution = (RecyclerView) findViewById(R.id.rv_price_disctribution);
        TV_Dis_Price = (TextView) findViewById(R.id.tv_total_player_price_total);
        mGridLayoutManagerBrand = new GridLayoutManager(SingleContestDetailActivity.this, 1);
        RV_Price_Distribution.setLayoutManager(mGridLayoutManagerBrand);
        RL_Game_List = (RelativeLayout) findViewById(R.id.rl_all_game);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView_Top_Three_Contest.setLayoutManager(horizontalLayoutManagaer);

        RL_Video_Game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SingleContestDetailActivity.this,GameVideoActivity.class);
                intent.putExtra("gametype",gametype);
                startActivity(intent);
            }
        });

        RL_Game_List.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SingleContestDetailActivity.this,GameZoneActivity.class);
                startActivity(intent);
            }
        });
    }
    public void singleContestDetail(final String gametype,final String srno,final String status) {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        //  String url = Constant.URL+"addSignUp"; // <----enter your post url here
        String url = Constant.URL+"getGameSettingByType?Type="+gametype+"&ContestID="+srno+"&Status="+status;
        JsonArrayRequest MyStringRequest = new JsonArrayRequest(Request.Method.POST, url, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                try
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    arrayList.clear();
                    for (int i = 0; i < response.length(); i++)
                    {
                        JSONObject jsonObject2 = response.getJSONObject(i);
                        String total_Memb = jsonObject2.getString("total_Memb");
                        String winning_amt = jsonObject2.getString("winning_amt");
                        join_contest_amt = jsonObject2.getInt("entry_amt");
                        String flag = jsonObject2.getString("flag");
                        String ttime = jsonObject2.getString("ttime");
                        String time_left = jsonObject2.getString("time_left");
                        String game_name = jsonObject2.getString("game_name");
                        String total_joining = jsonObject2.getString("total_joining");
                        TV_Game_Name.setText(""+game_name);
                        TV_Price.setText("\u20B9 "+winning_amt);
                        TV_Dis_Price.setText("\u20B9 "+winning_amt);
                        TV_Total_Player.setText(total_joining+"/"+total_Memb);
                        TV_Dis_Player.setText(""+total_joining+"/"+total_Memb);
                    //    join_contest_amt = Integer.parseInt(entry_amt);
                        int sec  = Integer.parseInt(time_left);
                        reverseTimer(sec,TV_Remaing_Time);
                        if (flag.equals("InActive"))
                        {
                            TV_Cotest_Join.setText("CONTEST CLOSE");
                            RL_play_game.setClickable(false);
                        }
                        else
                        {
                           // TV_Cotest_Join.setText("PLAY WITH   "+join_contest_amt);
                        }
                    }
                }
                catch (JSONException e)
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error)
            {
         //       progressBar.setVisibility(View.INVISIBLE);
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
        MyStringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyRequestQueue.add(MyStringRequest);
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
        MyStringRequest.setRetryPolicy(new DefaultRetryPolicy(200000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,   DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyRequestQueue.add(MyStringRequest);
    }
    public void reverseTimer(int Seconds,final TextView tv) {
        new CountDownTimer(Seconds* 1000+1000, 1000)
        {
            public void onTick(long millisUntilFinished)
            {
                int seconds = (int) (millisUntilFinished / 1000);
                int hours = seconds / (60 * 60);
                int tempMint = (seconds - (hours * 60 * 60));
                int minutes = tempMint / 60;
                seconds = tempMint - (minutes * 60);
                tv.setText(String.format("%02d", hours) + "hr " + String.format("%02d", minutes) + "m " + String.format("%02d", seconds)+"s");
            }
            public void onFinish()
            {
                tv.setText("Completed");
            }
        }.start();
    }
    private void showCustomDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_contest_conform);
        dialog.setCancelable(true);
        final  TextView TV_Total_Amount,TV_Cotest_Amount,TV_Remaing_Amount;
        final  ImageView img_close;
        final Button btn;
        String tokenmy="0";

        lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        TV_Total_Amount = (TextView) dialog.findViewById(R.id.tv_total_amount);
        TV_Cotest_Amount = (TextView) dialog.findViewById(R.id.tv_contest_amount);
        TV_Remaing_Amount = (TextView) dialog.findViewById(R.id.tv_remaining_left_amount);
        img_close = (ImageView) dialog.findViewById(R.id.img_tournament_close);
        btn = (Button) dialog.findViewById(R.id.btn_join_contest);
        TV_Total_Amount.setText(""+Constant.TOTAL_BALANCE);
        TV_Cotest_Amount.setText(""+join_contest_amt);
        int RemaingAmt = Constant.TOTAL_BALANCE - join_contest_amt;
        TV_Remaing_Amount.setText(""+RemaingAmt);
        if (Constant.TOTAL_BALANCE<join_contest_amt)
        {
            Toast.makeText(SingleContestDetailActivity.this,"insufficient balance ",Toast.LENGTH_SHORT).show();
            btn.setText("ADD FUND");
            tokenmy = "1";
        }

        img_close.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });

        final String finalTokenmy = tokenmy;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                if (finalTokenmy.equals("1"))
                {
                    Intent intent = new Intent(SingleContestDetailActivity.this,AccountActivity.class);
                    startActivity(intent);
                }
                else
                {
                   // Intent intent = new Intent(SingleContestDetailActivity.this,JumpFishActivity.class);
                    //intent.putExtra("srno",srno);
                   // startActivity(intent);
                    joinContest(userId,srno);
                }
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
    public void joinContest(final String MemberCode, final String Srno) {
        //progressBar.setVisibility(View.VISIBLE);
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = Constant.URL+"addContest";
        StringRequest jsonObjRequest = new StringRequest(Request.Method.PUT,url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
        //            progressBar.setVisibility(View.INVISIBLE);
                    JSONObject jsonObject = new JSONObject(response);
                    String  pstatus = jsonObject.getString("status");
                    String  msg = jsonObject.getString("msg");
                    if (pstatus.equals("1")||pstatus.equals("2"))
                        {
                            if (gametype.equals("1"))
                            {
                                Intent intent = new Intent(SingleContestDetailActivity.this,JumpFishActivity.class);
                                intent.putExtra("gtype",gametype);
                                intent.putExtra("srno",srno);
                                startActivity(intent);
                            }
                            else if (gametype.equals("2"))
                            {
                                Intent intent = new Intent(SingleContestDetailActivity.this,WebView2048Activity.class);
                                intent.putExtra("url","http://site0.bidbch.com/games/2048/index.html");
                                intent.putExtra("gtype",gametype);
                                intent.putExtra("srno",srno);
                                startActivity(intent);
                            }
                            else if(gametype.equals("3"))
                            {
                                Intent intent = new Intent(SingleContestDetailActivity.this,WebView2048Activity.class);
                                intent.putExtra("url","http://logicalsolutiontech.com/game/catchdots/index.html");
                                intent.putExtra("gtype",gametype);
                                intent.putExtra("srno",srno);
                                startActivity(intent);
                            }
                            else if(gametype.equals("4"))
                            {
                                Intent intent = new Intent(SingleContestDetailActivity.this,WebView2048Activity.class);
                                intent.putExtra("url","http://logicalsolutiontech.com/game/fastarrow/index.html");
                                intent.putExtra("gtype",gametype);
                                intent.putExtra("srno",srno);
                                startActivity(intent);
                            }
                            else if(gametype.equals("5"))
                            {
                                Intent intent = new Intent(SingleContestDetailActivity.this,WebView2048Activity.class);
                                intent.putExtra("url","http://site0.bidbch.com/games/pingpong/index.html");
                                intent.putExtra("gtype",gametype);
                                intent.putExtra("srno",srno);
                                startActivity(intent);
                            }
                            else if(gametype.equals("6"))
                            {
                                Intent intent = new Intent(SingleContestDetailActivity.this,WebView2048Activity.class);
                                intent.putExtra("url","http://site0.bidbch.com/games/pingpong/index.html");
                                intent.putExtra("gtype",gametype);
                                intent.putExtra("srno",srno);
                                startActivity(intent);
                            }
                            else if(gametype.equals("7"))
                            {
                                Intent intent = new Intent(SingleContestDetailActivity.this,WebView2048Activity.class);
                                intent.putExtra("url","http://site0.bidbch.com/games/DotsPong/index.html");
                                intent.putExtra("gtype",gametype);
                                intent.putExtra("srno",srno);
                                startActivity(intent);
                            }
                        }
                    else
                        {
                            Toast.makeText(SingleContestDetailActivity.this," "+msg,Toast.LENGTH_SHORT).show();
                        }
                }
                catch (JSONException e)
                {
         //           progressBar.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                  //      progressBar.setVisibility(View.INVISIBLE);
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
                return params;
            }
        };
        MySingalton.getInstance(getApplicationContext()).addRequestQueue(jsonObjRequest);
        jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(200000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyRequestQueue.add(jsonObjRequest);
    }
    public void topScorePriceDistribution(final String srno) {
    //    progressBar.setVisibility(View.VISIBLE);
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        //  String url = Constant.URL+"addSignUp"; // <----enter your post url here
        String url = Constant.URL+"getRankAmtDetails?ContestID="+srno;
        JsonArrayRequest MyStringRequest = new JsonArrayRequest(Request.Method.POST, url, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                try
                {
         //           progressBar.setVisibility(View.INVISIBLE);
                    arrayList1.clear();
                    String res = response.toString();
                    if (res.equals("[]"))
                    {

                    }
                    else
                    {
                        CV_Price_Dis.setVisibility(View.VISIBLE);
                        for (int i = 0; i < response.length(); i++)
                        {
                            JSONObject jsonObject2 = response.getJSONObject(i);
                            String rank_From = jsonObject2.getString("rank_From");
                            String rank_To = jsonObject2.getString("rank_To");
                            String amount = jsonObject2.getString("amount");
                            PriceModel model = new PriceModel();
                            model.setRankfrom(rank_From);
                            model.setRankto(rank_To);
                            model.setRankamount(amount);
                            arrayList1.add(model);
                        }
                        PriceAdapter operator_adapter = new PriceAdapter(arrayList1,getApplicationContext());
                        RV_Price_Distribution.setAdapter(operator_adapter);
                    }
                }
                catch (JSONException e)
                {
     //               progressBar.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error)
            {
    //            progressBar.setVisibility(View.INVISIBLE);
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
        MyStringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyRequestQueue.add(MyStringRequest);
    }
    public void joinContestStatus(final String MemberCode, final String Srno) {
        //progressBar.setVisibility(View.VISIBLE);
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = Constant.URL+"checkplayerExists?membercode="+MemberCode+"&srno="+Srno;
        StringRequest jsonObjRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    playerstatus = jsonObject.getString("status");
                    message = jsonObject.getString("msg");
                    if (playerstatus.equals("1"))
                    {
                        TV_Cotest_Join.setText("PAY AND PLAY");
                    }
                    else
                    {
                        TV_Cotest_Join.setText("PLAY NOW");
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
