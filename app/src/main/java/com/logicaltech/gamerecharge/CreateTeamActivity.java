package com.logicaltech.gamerecharge;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import adpter.TempViewPlayerAdapter;
import model.PlayerModel;
import util.Constant;
import util.MySingalton;
import util.SessionManeger;

public class CreateTeamActivity extends AppCompatActivity
{
    private Context context;
    ImageView IV_Back_Arrow;
    String membercode,mid,game_amt_type,ded_mainbal_amt,ded_boncash_amt,srno;
    ArrayList<PlayerModel> playerModels ;
    TempViewPlayerAdapter mAdapter;
    LinearLayout TV_Create_Team;
    SessionManeger sessionManeger;
    ProgressBar progressBar;
    TextView TV_Remaing_Time,TV_Game_Name,TV_Price,TV_Total_Player,TV_Cotest_Join;
    int join_contest_amt;
    Dialog dialog;
    WindowManager.LayoutParams lp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);
        sessionManeger = new SessionManeger(getApplicationContext());
        HashMap<String, String> hashMap = sessionManeger.getUserDetails();
        membercode = hashMap.get(SessionManeger.MEMBER_ID);

        init();
    }
    private void init()
    {
        context = this;
        IV_Back_Arrow = findViewById(R.id.img_back_create_team);
        TV_Create_Team = findViewById(R.id.textview_create_team);
        progressBar = findViewById(R.id.progrebar_tempplayerselected);
        TV_Remaing_Time = findViewById(R.id.tv_remaing_time_contest_detail);
        TV_Game_Name = findViewById(R.id.tv_cotest_name);
        TV_Price = findViewById(R.id.tv_wining_price);
        TV_Total_Player  = findViewById(R.id.tv_total_player);
        TV_Cotest_Join = findViewById(R.id.tv_contest_amount);


        //fetch intent data
        mid = getIntent().getStringExtra("mid");
        playerModels = getIntent().getParcelableArrayListExtra("playerlist");
        srno = getIntent().getStringExtra("srno");
        setListeners();
        setRecyclerView();
        singleContestDetail("10",srno,"1");
    }

    private void setListeners()
    {
        IV_Back_Arrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        TV_Create_Team.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
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
        });
    }

    private void setRecyclerView() {
        RecyclerView mRecyclerView = findViewById(R.id.rv_create_player);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new TempViewPlayerAdapter(getApplicationContext(),playerModels);
        mRecyclerView.setAdapter(mAdapter);
    }

    private String getSelectedPlayerId() {
        String pid = "";
        for (PlayerModel playerModel : playerModels)
        {
           pid = pid+playerModel.getPid()+"~";
        }
        if (pid.substring(pid.length()-1).equalsIgnoreCase("~"))
        {
            pid = pid.substring(0,pid.length()-1);
        }
        return pid;
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
                    for (int i = 0; i < response.length(); i++)
                    {
                        JSONObject jsonObject2 = response.getJSONObject(i);
                        String total_Memb = jsonObject2.getString("total_Memb");
                        String winning_amt = jsonObject2.getString("winning_amt");
                        game_amt_type = jsonObject2.getString("game_amt_type");
                        join_contest_amt = jsonObject2.getInt("entry_amt");

                        String time_left = jsonObject2.getString("time_left");
                        String game_name = jsonObject2.getString("game_name");
                        String total_joining = jsonObject2.getString("total_joining");
                        ded_mainbal_amt = jsonObject2.getString("ded_mainbal_amt");
                        ded_boncash_amt = jsonObject2.getString("ded_boncash_amt");
                        TV_Game_Name.setText(""+game_name);
                        TV_Price.setText("\u20B9 "+winning_amt);
                   //     TV_Dis_Price.setText("\u20B9 "+winning_amt);
                        TV_Total_Player.setText(total_joining+"/"+total_Memb);
                   //     TV_Dis_Player.setText(""+total_joining+"/"+total_Memb);
                        //    join_contest_amt = Integer.parseInt(entry_amt);
                        int sec  = Integer.parseInt(time_left);
                        reverseTimer(sec,TV_Remaing_Time);
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

    private void showTokenDialogBox()
    {
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
            Toast.makeText(CreateTeamActivity.this,"insufficient Balance,Please add fund through paytm",Toast.LENGTH_SHORT).show();
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
                    Intent intent = new Intent(CreateTeamActivity.this,AccountActivity.class);
                    startActivity(intent);
                }
                else
                {
                    createTeam(membercode,getSelectedPlayerId(),mid);
                }
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void createTeam(final String membercode, final String PID,final String Unique_ID) {
        progressBar.setVisibility(View.VISIBLE);
        TV_Create_Team.setVisibility(View.GONE);
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = Constant.URL+"addCricketChoosePlayers";
        StringRequest jsonObjRequest = new StringRequest(Request.Method.PUT,url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("msg");
                    if (status.equals("1"))
                    {
                        joinContest(membercode,srno);
                    }
                    else
                    {
                        Toast.makeText(CreateTeamActivity.this," "+message,Toast.LENGTH_SHORT).show();
                        TV_Create_Team.setVisibility(View.VISIBLE);
                    }
                }
                catch (JSONException e)
                {
                    progressBar.setVisibility(View.GONE);
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
                })
        {
            @Override
            public String getBodyContentType()
            {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("membercode", membercode);
                params.put("PID",PID);
                params.put("Unique_ID", Unique_ID);
                return params;
            }
        };
        MySingalton.getInstance(getApplicationContext()).addRequestQueue(jsonObjRequest);
        jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(200000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyRequestQueue.add(jsonObjRequest);
    }
    public void joinContest(final String MemberCode, final String Srno)
    {
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
                    JSONObject jsonObject = new JSONObject(response);
                    String  pstatus = jsonObject.getString("status");
                    String  msg = jsonObject.getString("msg");
                    if (pstatus.equals("1")||pstatus.equals("2"))
                    {
                        Intent intent = new Intent(CreateTeamActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(CreateTeamActivity.this,msg+"",Toast.LENGTH_SHORT).show();
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
            Toast.makeText(CreateTeamActivity.this,"insufficient balance ",Toast.LENGTH_SHORT).show();
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
                    Intent intent = new Intent(CreateTeamActivity.this,AccountActivity.class);
                    startActivity(intent);
                }
                else
                {
                    joinContest(membercode,srno);
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
