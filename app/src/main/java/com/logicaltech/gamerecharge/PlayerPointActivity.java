package com.logicaltech.gamerecharge;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adpter.PlayerPointAdpter;
import model.PlayerModel;
import model.PlayerPointModel;
import util.Constant;
import util.MySingalton;
import util.SessionManeger;

public class PlayerPointActivity extends AppCompatActivity
{
    private Context context;
    String mid,membercode;
    SessionManeger sessionManeger;
    public int totalscore;
    RecyclerView RV_PlayerPoint;
    ArrayList<PlayerPointModel> arrPlayerPoint ;
    ImageView IV_Back_Arrow;
    ProgressBar progressBar;
    TextView txtScore;
    Dialog dialog;
    WindowManager.LayoutParams lp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_point);
        init();
    }

    private void init() {
        context = this;
        sessionManeger = new SessionManeger(context);
        HashMap<String, String> hashMap = sessionManeger.getUserDetails();
        membercode = hashMap.get(SessionManeger.MEMBER_ID);
        arrPlayerPoint = new ArrayList<>();
        RV_PlayerPoint = findViewById(R.id.rv_playerpoint);
        IV_Back_Arrow = findViewById(R.id.img_back_playerpoint);
        progressBar = findViewById(R.id.progreebar);
        txtScore = findViewById(R.id.textview_submitscore);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        RV_PlayerPoint.setLayoutManager(horizontalLayoutManagaer);
        mid = getIntent().getStringExtra("mid");
        cricketHighScore(membercode,mid);
        clickLisner();

    }

    public void clickLisner() {
        IV_Back_Arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                showCustomDialog();
            }
        });
    }

    public void cricketHighScore(final String membercode,final String unique_id) {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(context);
        String url = Constant.URL+"getCricketPlayerPointsByUID?membercode="+membercode+"&Unique_ID="+unique_id;
        JsonArrayRequest MyStringRequest = new JsonArrayRequest(Request.Method.POST, url, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                try
                {
                    progressBar.setVisibility(View.GONE);
                    String respo = response.toString();
                    if (respo.equals("[]"))
                    {
                        Toast.makeText(context,"Sorry You can not join contest on this Match:",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        for (int i = 0; i < response.length(); i++)
                        {
                            PlayerPointModel playerPointModel = new PlayerPointModel();
                            JSONObject jsonObject1 = response.getJSONObject(i);
                            playerPointModel.setName(jsonObject1.getString("name"));
                            int score = jsonObject1.getInt("total_score");
                            playerPointModel.setPoint(score);
                            totalscore = totalscore+score;
                            arrPlayerPoint.add(playerPointModel);
                        }
                        PlayerPointAdpter playerPointAdpter = new PlayerPointAdpter(context,arrPlayerPoint);
                        RV_PlayerPoint.setAdapter(playerPointAdpter);
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

    private void showCustomDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_cricket_final_score);
        dialog.setCancelable(true);
        final TextView TV_Total_coin;
        final Button btn;
        lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        TV_Total_coin = (TextView) dialog.findViewById(R.id.tv_recied_coin);
        btn = (Button) dialog.findViewById(R.id.btn_coin_add);
        TV_Total_coin.setText("Better Luck Next Time");
        TV_Total_coin.setText(""+totalscore);
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
              //  submitScore(membercode,srno,Integer.toString(totalscore));
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
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
                        Toast.makeText(PlayerPointActivity.this,"Your score submit "+message,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context,TopScoreActivity.class);
                      //  intent.putExtra("srno",srno);
                        intent.putExtra("price","00");
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(PlayerPointActivity.this," "+message,Toast.LENGTH_SHORT).show();
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
