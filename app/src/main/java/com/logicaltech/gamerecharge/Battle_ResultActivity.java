package com.logicaltech.gamerecharge;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import util.Constant;
import util.MySingalton;
import util.SessionManeger;

public class Battle_ResultActivity extends AppCompatActivity
{
    private Context context;
    private ImageView Img_Back,Img_Result;
    private TextView TV_Coin,TV_Balance,TV_P2_Score,TV_P2_Name,TV_P1_Score,TV_P1_Name,TVp1_Wining_Amt,TVp2_Wining_Amt;
    private ProgressBar progressBar;
    String srno,gtype,userId,userName;
    int p1scorevalue;
    SessionManeger sessionManeger;
    private CircleImageView imgPlayer1,Img_player2;
    private Button Btn_finduser;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle__result);
       // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        init();
    }

    private void init() {
        context = this;
        sessionManeger = new SessionManeger(getApplicationContext());
        HashMap<String, String> hashMap = sessionManeger.getUserDetails();
        userId = hashMap.get(SessionManeger.MEMBER_ID);
        userName = hashMap.get(SessionManeger.KEY_NAME);

        Img_Back = findViewById(R.id.img_back_battle);
        TV_Coin = findViewById(R.id.tv_total_coin);
        TV_Balance = findViewById(R.id.tv_total_income);
        TV_Coin.setText(""+ Constant.TOTAL_COIN);
        TV_Balance.setText(""+Constant.TOTAL_BALANCE);
        progressBar = findViewById(R.id.progrebar_result);
        TV_P1_Score = findViewById(R.id.txt_Player1_score);
        Img_Result = findViewById(R.id.imgresult);

        TVp1_Wining_Amt = findViewById(R.id.p1_wining_amt);
        TVp2_Wining_Amt = findViewById(R.id.p2_wining_amt);

        imgPlayer1 = findViewById(R.id.img_player1);
        Btn_finduser = findViewById(R.id.btn_findnew);

        if (hashMap.get(SessionManeger.KEY_PHOTO).equals(""))
        {

        }
        else
        {
            Picasso.with(context).load(hashMap.get(SessionManeger.KEY_PHOTO)).into(imgPlayer1);
        }


        TV_P2_Name = findViewById(R.id.txt_player2name);
        TV_P1_Name = findViewById(R.id.txt_player1name);

        TV_P2_Score = findViewById(R.id.txt_Player2_score);
        Img_player2 = findViewById(R.id.img_player2);

        p1scorevalue = getIntent().getExtras().getInt("score");
        srno = getIntent().getExtras().getString("srno");
        gtype = getIntent().getExtras().getString("gtype");

        TV_P1_Score.setText("SCORED: "+p1scorevalue);
        TV_P1_Name.setText(""+userName);


        clickeable();

        battleList(srno);

    }

    private void clickeable() {
        Img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MainActivity.class);
                startActivity(intent);
            }
        });

        Btn_finduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,SingleBattleDetailActivity.class);
                intent.putExtra("srno",srno);
                intent.putExtra("gametype",gtype);
                startActivity(intent);
            }
        });


    }

    public void battleList(final String bId ) {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        //  String url = Constant.URL+"addSignUp"; // <----enter your post url here
        String url = Constant.URL+"getBattlePlayerDetails?BattleID="+bId;
        JsonArrayRequest MyStringRequest = new JsonArrayRequest(Request.Method.POST, url, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                try
                {
                    submitScoreInBattle(userId,srno,Integer.toString(p1scorevalue));
                    progressBar.setVisibility(View.INVISIBLE);
                    String res = response.toString();
                    if (res.equals("[]"))
                    {
                        battleList("0");
                    }
                    else
                    {
                        for (int i = 0; i < response.length(); i++)
                        {
                            JSONObject jsonObject2 = response.getJSONObject(i);
                            String membercode = jsonObject2.getString("membercode");
                            if (userId.equals(membercode))
                            {

                            }
                            else
                            {
                                String p2_score = jsonObject2.getString("memb_name");
                                TV_P2_Name.setText(""+p2_score);
                                TV_P2_Score.setText("SCORE: "+jsonObject2.getString("Score"));
                                Picasso.with(context).load(jsonObject2.getString("userFile")).into(Img_player2);

                                if (Integer.parseInt(jsonObject2.getString("Score"))<p1scorevalue)
                                {
                                    Toast.makeText(context,"You Win",Toast.LENGTH_SHORT).show();
                                    TVp1_Wining_Amt.setText(jsonObject2.getString("winning_amt"));
                                    TVp2_Wining_Amt.setText("0");
                                    Img_Result.setImageResource(R.drawable.won);
                                }
                                else
                                {
                                    TVp1_Wining_Amt.setText("0");
                                    TVp2_Wining_Amt.setText(jsonObject2.getString("winning_amt"));
                                    Toast.makeText(context,"You Loss",Toast.LENGTH_SHORT).show();
                                    Img_Result.setImageResource(R.drawable.loose);
                                }
                                break;
                            }
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
                progressBar.setVisibility(View.INVISIBLE);
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

    public void submitScoreInBattle(final String MemberCode, final String Srno, final String Score) {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = Constant.URL+"addBattlePlayedScore";
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
                        Toast.makeText(Battle_ResultActivity.this,"Your score submit "+message,Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(Battle_ResultActivity.this," "+message,Toast.LENGTH_SHORT).show();
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
                params.put("bid",Srno);
                params.put("score",Score);
                return params;
            }
        };
        MySingalton.getInstance(getApplicationContext()).addRequestQueue(jsonObjRequest);
        jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(200000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyRequestQueue.add(jsonObjRequest);
    }
}
