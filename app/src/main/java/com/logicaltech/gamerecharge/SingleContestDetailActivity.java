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
    String srno,userId,playerstatus="0",message,gametype,game_amt_type,ded_mainbal_amt,ded_boncash_amt;
    ArrayList<TopScoreModel> arrayList =new ArrayList<>();
    ArrayList<PriceModel> arrayList1 =new ArrayList<>();
    CardView CV_Price_Dis;
    RecyclerView RecyclerView_Top_Three_Contest,RV_Price_Distribution;
    ProgressBar progressBar;
    TextView TV_Price,TV_Total_Player,TV_Game_Name,TV_Remaing_Time,TV_Contest_Amount_List,TV_Dis_Player,TV_Dis_Price,TV_Bonus_Cash;
    int join_contest_amt;
    ImageView img_back_arrow;
    RelativeLayout RL_Video_Game,RL_Game_List;
    Dialog dialog;
    SessionManeger sessionManeger;
    GridLayoutManager mGridLayoutManagerBrand;
    LinearLayout LL_Bonus_Cash;
    WindowManager.LayoutParams lp;
    Button Btn_playGame;
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

    }

    public void init() {
        TV_Price = (TextView) findViewById(R.id.tv_wining_price);
        TV_Total_Player  = (TextView) findViewById(R.id.tv_total_player);
       // TV_Cotest_Join = (TextView) findViewById(R.id.tv_contest_amount);
        TV_Game_Name = (TextView) findViewById(R.id.tv_cotest_name);
        TV_Remaing_Time = (TextView) findViewById(R.id.tv_remaing_time_contest_detail);
        TV_Dis_Player = (TextView) findViewById(R.id.tv_total_player_price_distribution);
        img_back_arrow = (ImageView) findViewById(R.id.img_back_arrow_contest_detail);
        Btn_playGame = (Button) findViewById(R.id.rl_play_game);
        CV_Price_Dis = (CardView) findViewById(R.id.cv_cotest_price_distribution);
        progressBar = (ProgressBar) findViewById(R.id.progrebar_single_contest);
        RL_Video_Game = (RelativeLayout) findViewById(R.id.rl_all_how_to_play);
        RecyclerView_Top_Three_Contest = (RecyclerView) findViewById(R.id.rv_top_three_score);
        RV_Price_Distribution = (RecyclerView) findViewById(R.id.rv_price_disctribution);
        TV_Dis_Price = (TextView) findViewById(R.id.tv_total_player_price_total);
        mGridLayoutManagerBrand = new GridLayoutManager(SingleContestDetailActivity.this, 1);
        RV_Price_Distribution.setLayoutManager(mGridLayoutManagerBrand);
        RL_Game_List = (RelativeLayout) findViewById(R.id.rl_all_game);
        TV_Bonus_Cash = (TextView) findViewById(R.id.txt_bounse_cash);
        LL_Bonus_Cash = (LinearLayout) findViewById(R.id.ll_bounce_cash1);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView_Top_Three_Contest.setLayoutManager(horizontalLayoutManagaer);
        clickable();
    }

    public void clickable() {
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
                Intent intent = new Intent(SingleContestDetailActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        img_back_arrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        Btn_playGame.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // joinContest(userId,srno);
                if (playerstatus.equals("1"))
                {
                    if (game_amt_type.equals("POINTS"))
                    {
                        showTokenDialogBox();
                    }
                    else
                    {
                        showCustomDialog();
                    }
                }
                else if(playerstatus.equals("2"))
                {
                    if (gametype.equals("1"))
                    {
                        Intent intent = new Intent(SingleContestDetailActivity.this,JumpFishActivity.class);
                        intent.putExtra("gtype",gametype);
                        intent.putExtra("cob","0");
                        intent.putExtra("srno",srno);
                        startActivity(intent);
                    }
                    else if(gametype.equals("2"))
                    {
                        Intent intent = new Intent(SingleContestDetailActivity.this,WebView2048Activity.class);
                        intent.putExtra("url","http://site0.bidbch.com/games/2048/index.html");
                        intent.putExtra("gtype",gametype);
                        intent.putExtra("srno",srno);
                        intent.putExtra("cob","0");
                        startActivity(intent);
                    }
                    else if(gametype.equals("3"))
                    {
                        Intent intent = new Intent(SingleContestDetailActivity.this,WebView2048Activity.class);
                        intent.putExtra("url","http://site0.bidbch.com/games/catchdots/index.html");
                        intent.putExtra("gtype",gametype);
                        intent.putExtra("cob","0");
                        intent.putExtra("srno",srno);
                        startActivity(intent);
                    }
                    else if(gametype.equals("4"))
                    {
                        Intent intent = new Intent(SingleContestDetailActivity.this,WebView2048Activity.class);
                        intent.putExtra("url","http://site0.bidbch.com/games/fastarrow/index.html");
                        intent.putExtra("gtype",gametype);
                        intent.putExtra("srno",srno);
                        intent.putExtra("cob","0");
                        startActivity(intent);
                    }
                    else if(gametype.equals("5"))
                    {
                        Intent intent = new Intent(SingleContestDetailActivity.this,WebView2048Activity.class);
                        intent.putExtra("url","http://site0.bidbch.com/games/pingpong/index.html");
                        intent.putExtra("gtype",gametype);
                        intent.putExtra("srno",srno);
                        intent.putExtra("cob","0");
                        startActivity(intent);
                    }
                    else if(gametype.equals("6"))
                    {
                        Intent intent = new Intent(SingleContestDetailActivity.this,WebView2048Activity.class);
                        intent.putExtra("url","http://site0.bidbch.com/games/pingpong/index.html");
                        intent.putExtra("gtype",gametype);
                        intent.putExtra("srno",srno);
                        intent.putExtra("cob","0");
                        startActivity(intent);
                    }
                    else if(gametype.equals("7"))
                    {
                        Intent intent = new Intent(SingleContestDetailActivity.this,WebView2048Activity.class);
                        intent.putExtra("url","http://site0.bidbch.com/games/DotsPong/index.html");
                        intent.putExtra("gtype",gametype);
                        intent.putExtra("srno",srno);
                        intent.putExtra("cob","0");
                        startActivity(intent);
                    }
                    else if(gametype.equals("8"))
                    {
                        Intent intent = new Intent(SingleContestDetailActivity.this,WebView2048Activity.class);
                        intent.putExtra("url","http://site0.bidbch.com/games/DotsAttack/index.html");
                        intent.putExtra("gtype",gametype);
                        intent.putExtra("srno",srno);
                        intent.putExtra("cob","0");
                        startActivity(intent);
                    }
                    else if(gametype.equals("9"))
                    {
                        Intent intent = new Intent(SingleContestDetailActivity.this,WebView2048Activity.class);
                        intent.putExtra("url","http://site0.bidbch.com/games/JumpNinjaHero/index.html");
                        intent.putExtra("gtype",gametype);
                        intent.putExtra("srno",srno);
                        intent.putExtra("cob","0");
                        startActivity(intent);
                    }
                    else if(gametype.equals("11"))
                    {
                        Intent intent = new Intent(SingleContestDetailActivity.this,WebView2048Activity.class);
                        intent.putExtra("url","http://site0.bidbch.com/games/ShotPong/index.html");
                        intent.putExtra("gtype",gametype);
                        intent.putExtra("srno",srno);
                        intent.putExtra("cob","0");
                        startActivity(intent);
                    }
                    else if(gametype.equals("12"))
                    {
                        Intent intent = new Intent(SingleContestDetailActivity.this,WebView2048Activity.class);
                        intent.putExtra("url","http://site0.bidbch.com/games/FlyingTriangle/index.html");
                        intent.putExtra("gtype",gametype);
                        intent.putExtra("srno",srno);
                        intent.putExtra("cob","0");
                        startActivity(intent);
                    }
                    else if(gametype.equals("13"))
                    {
                        Intent intent = new Intent(SingleContestDetailActivity.this,WebView2048Activity.class);
                        intent.putExtra("url","http://site0.bidbch.com/games/CrazyChicks/index.html");
                        intent.putExtra("gtype",gametype);
                        intent.putExtra("srno",srno);
                        startActivity(intent);
                        intent.putExtra("cob","0");
                    }
                    else if(gametype.equals("14"))
                    {
                        Intent intent = new Intent(SingleContestDetailActivity.this,WebView2048Activity.class);
                        intent.putExtra("url","http://site0.bidbch.com/games/BrightBall/index.html");
                        intent.putExtra("gtype",gametype);
                        intent.putExtra("srno",srno);
                        intent.putExtra("cob","0");
                        startActivity(intent);
                    }
                    else if(gametype.equals("15"))
                    {
                        Intent intent = new Intent(SingleContestDetailActivity.this,WebView2048Activity.class);
                        intent.putExtra("url","http://site0.bidbch.com/games/penaltykick/index.html");
                        intent.putExtra("gtype",gametype);
                        intent.putExtra("srno",srno);
                        intent.putExtra("cob","0");
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
                        game_amt_type = jsonObject2.getString("game_amt_type");
                        join_contest_amt = jsonObject2.getInt("entry_amt");
                        String flag = jsonObject2.getString("flag");
                        String ttime = jsonObject2.getString("ttime");
                        String time_left = jsonObject2.getString("time_left");
                        String game_name = jsonObject2.getString("game_name");
                        String total_joining = jsonObject2.getString("total_joining");
                        ded_mainbal_amt = jsonObject2.getString("ded_mainbal_amt");
                        ded_boncash_amt = jsonObject2.getString("ded_boncash_amt");
                        TV_Game_Name.setText(""+game_name);
                        TV_Price.setText( winning_amt);
                        TV_Dis_Price.setText("\u20B9 "+winning_amt);
                        TV_Total_Player.setText(total_joining+"/"+total_Memb);
                        TV_Dis_Player.setText(""+total_joining+"/"+total_Memb);
                        if (game_amt_type.equals("RUPEES"))
                        {
                            TV_Bonus_Cash.setText("\u20B9 "+ded_boncash_amt+" Use in bounes cash");
                        }
                        else
                        {
                            LL_Bonus_Cash.setVisibility(View.GONE);
                        }
                    //    join_contest_amt = Integer.parseInt(entry_amt);
                        int sec  = Integer.parseInt(time_left);
                        reverseTimer(sec,TV_Remaing_Time);
                        if (flag.equals("InActive"))
                        {
                            Btn_playGame.setText("CONTEST CLOSE");
                            Btn_playGame.setClickable(false);
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
                tv.setText(String.format("%02d", hours) + "H " + String.format("%02d", minutes) + "M " + String.format("%02d", seconds)+"S");
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
        final  TextView TV_Total_Amount,TV_Cotest_Amount,TV_Bounce_Cash,TV_Main_Wallet;
        final  ImageView img_close;
        final Button btn;
        final RelativeLayout RL_info;
        String tokenmy="0";

        lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        TV_Total_Amount = (TextView) dialog.findViewById(R.id.tv_total_amount);
        TV_Cotest_Amount = (TextView) dialog.findViewById(R.id.tv_contest_amount);
        TV_Bounce_Cash = (TextView) dialog.findViewById(R.id.tv_use_bounce_cash);
        TV_Main_Wallet = (TextView) dialog.findViewById(R.id.tv_deposit_and_cash);
        RL_info = (RelativeLayout) dialog.findViewById(R.id.rl_bounce_cash);
        img_close = (ImageView) dialog.findViewById(R.id.img_tournament_close);
        btn = (Button) dialog.findViewById(R.id.btn_join_contest);

        TV_Total_Amount.setText(""+Constant.TOTAL_BALANCE);
        TV_Cotest_Amount.setText(""+join_contest_amt);
        TV_Bounce_Cash.setText(""+ded_boncash_amt);
        TV_Main_Wallet.setText(""+ded_mainbal_amt);

        RL_info.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showBounceCash();
            }
        });
        if (Constant.TOTAL_BALANCE<join_contest_amt)
        {
            Toast.makeText(SingleContestDetailActivity.this,"insufficient balance ",Toast.LENGTH_SHORT).show();
            btn.setText("ADD FUND");
            tokenmy = "1";
        }
        else
        {
            if (Constant.TOTAL_DEPOSIT_CASH<Float.parseFloat(ded_mainbal_amt))
            {
                Toast.makeText(SingleContestDetailActivity.this,"insufficient Main balance ",Toast.LENGTH_SHORT).show();
                btn.setText("ADD FUND");
                tokenmy = "1";
            }
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
        btn.setOnClickListener(new View.OnClickListener()
        {
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
                                intent.putExtra("cob","0");
                                startActivity(intent);
                            }
                            else if (gametype.equals("2"))
                            {
                                Intent intent = new Intent(SingleContestDetailActivity.this,WebView2048Activity.class);
                                intent.putExtra("url","http://site0.bidbch.com/games/2048/index.html");
                                intent.putExtra("gtype",gametype);
                                intent.putExtra("srno",srno);
                                intent.putExtra("cob","0");
                                startActivity(intent);
                            }
                            else if(gametype.equals("3"))
                            {
                                Intent intent = new Intent(SingleContestDetailActivity.this,WebView2048Activity.class);
                                intent.putExtra("url","http://site0.bidbch.com/games/catchdots/index.html");
                                intent.putExtra("gtype",gametype);
                                intent.putExtra("srno",srno);
                                intent.putExtra("cob","0");
                                startActivity(intent);
                            }
                            else if(gametype.equals("4"))
                            {
                                Intent intent = new Intent(SingleContestDetailActivity.this,WebView2048Activity.class);
                                intent.putExtra("url","http://site0.bidbch.com/games/fastarrow/index.html");
                                intent.putExtra("gtype",gametype);
                                intent.putExtra("srno",srno);
                                intent.putExtra("cob","0");
                                startActivity(intent);
                            }
                            else if(gametype.equals("5"))
                            {
                                Intent intent = new Intent(SingleContestDetailActivity.this,WebView2048Activity.class);
                                intent.putExtra("url","http://site0.bidbch.com/games/pingpong/index.html");
                                intent.putExtra("gtype",gametype);
                                intent.putExtra("srno",srno);
                                intent.putExtra("cob","0");
                                startActivity(intent);
                            }
                            else if(gametype.equals("6"))
                            {
                                Intent intent = new Intent(SingleContestDetailActivity.this,WebView2048Activity.class);
                                intent.putExtra("url","http://site0.bidbch.com/games/pingpong/index.html");
                                intent.putExtra("gtype",gametype);
                                intent.putExtra("srno",srno);
                                intent.putExtra("cob","0");
                                startActivity(intent);
                            }
                            else if(gametype.equals("7"))
                            {
                                Intent intent = new Intent(SingleContestDetailActivity.this,WebView2048Activity.class);
                                intent.putExtra("url","http://site0.bidbch.com/games/DotsPong/index.html");
                                intent.putExtra("gtype",gametype);
                                intent.putExtra("srno",srno);
                                intent.putExtra("cob","0");
                                startActivity(intent);
                            }
                            else if(gametype.equals("8"))
                            {
                                Intent intent = new Intent(SingleContestDetailActivity.this,WebView2048Activity.class);
                                intent.putExtra("url","http://site0.bidbch.com/games/DotsAttack/index.html");
                                intent.putExtra("gtype",gametype);
                                intent.putExtra("srno",srno);
                                intent.putExtra("cob","0");
                                startActivity(intent);
                            }
                            else if(gametype.equals("9"))
                            {
                                Intent intent = new Intent(SingleContestDetailActivity.this,WebView2048Activity.class);
                                intent.putExtra("url","http://site0.bidbch.com/games/JumpNinjaHero/index.html");
                                intent.putExtra("gtype",gametype);
                                intent.putExtra("srno",srno);
                                intent.putExtra("cob","0");
                                startActivity(intent);
                            }
                            else if(gametype.equals("11"))
                            {
                                Intent intent = new Intent(SingleContestDetailActivity.this,WebView2048Activity.class);
                                intent.putExtra("url","http://site0.bidbch.com/games/ShotPong/index.html");
                                intent.putExtra("gtype",gametype);
                                intent.putExtra("srno",srno);
                                intent.putExtra("cob","0");
                                startActivity(intent);
                            }
                            else if(gametype.equals("12"))
                            {
                                Intent intent = new Intent(SingleContestDetailActivity.this,WebView2048Activity.class);
                                intent.putExtra("url","http://site0.bidbch.com/games/FlyingTriangle/index.html");
                                intent.putExtra("gtype",gametype);
                                intent.putExtra("srno",srno);
                                intent.putExtra("cob","0");
                                startActivity(intent);
                            }
                            else if(gametype.equals("13"))
                            {
                                Intent intent = new Intent(SingleContestDetailActivity.this,WebView2048Activity.class);
                                intent.putExtra("url","http://site0.bidbch.com/games/CrazyChicks/index.html");
                                intent.putExtra("gtype",gametype);
                                intent.putExtra("srno",srno);
                                intent.putExtra("cob","0");
                                startActivity(intent);
                            }
                            else if(gametype.equals("14"))
                            {
                                Intent intent = new Intent(SingleContestDetailActivity.this,WebView2048Activity.class);
                                intent.putExtra("url","http://site0.bidbch.com/games/BrightBall/index.html");
                                intent.putExtra("gtype",gametype);
                                intent.putExtra("srno",srno);
                                intent.putExtra("cob","0");
                                startActivity(intent);
                            }
                            else if(gametype.equals("15"))
                            {
                                Intent intent = new Intent(SingleContestDetailActivity.this,WebView2048Activity.class);
                                intent.putExtra("url","http://site0.bidbch.com/games/penaltykick/index.html");
                                intent.putExtra("gtype",gametype);
                                intent.putExtra("srno",srno);
                                intent.putExtra("cob","0");
                                startActivity(intent);
                            }
                        }
                    else
                        {
                            Toast.makeText(SingleContestDetailActivity.this,msg+"",Toast.LENGTH_SHORT).show();
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
                        Btn_playGame.setText("PAY AND PLAY");
                    }
                    else
                    {
                        Btn_playGame.setText("PLAY NOW");
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

    private void showTokenDialogBox() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_token_contest_conform);
        dialog.setCancelable(true);
        final  TextView TV_Total_Token,TV_Cotest_Token,TV_Remaing_Amount;
        final  ImageView img_close;
        final Button btn;
        String tokenmy="0";

        lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        TV_Total_Token = (TextView) dialog.findViewById(R.id.tv_total_amount);
        TV_Cotest_Token = (TextView) dialog.findViewById(R.id.tv_contest_amount);
        TV_Remaing_Amount = (TextView) dialog.findViewById(R.id.tv_remaining_left_amount);
        img_close = (ImageView) dialog.findViewById(R.id.img_tournament_close);
        btn = (Button) dialog.findViewById(R.id.btn_join_contest);

        TV_Total_Token.setText(""+Constant.TOTAL_COIN);
        TV_Cotest_Token.setText(""+join_contest_amt);

        int RemaingAmt = Constant.TOTAL_COIN - join_contest_amt;

        TV_Remaing_Amount.setText(""+RemaingAmt);
        if (Constant.TOTAL_COIN<join_contest_amt)
        {
            Toast.makeText(SingleContestDetailActivity.this,"insufficient Balance,Please add fund through paytm",Toast.LENGTH_SHORT).show();
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
        btn.setOnClickListener(new View.OnClickListener()
        {
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
                    joinContest(userId,srno);
                }
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void showBounceCash() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_bounce_cash);
        dialog.setCancelable(true);
        lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((Button) dialog.findViewById(R.id.btn_close)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

}
