package com.logicaltech.gamerecharge;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adpter.CricketContestAdpter;
import model.ContestModel;
import util.Constant;

public class UpcomeingCricketContestActivity extends AppCompatActivity
{
    ImageView IV_backArrow;
    String gtype,mid,srno;
    RecyclerView RecyclerView_Circket_Contest_Type;
    GridLayoutManager mGridLayoutManagerBrand;
    ProgressBar progressBar;
    private Context context;
    ArrayList<ContestModel> arrayList =new ArrayList<>();
    TextView TV_gametitle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcomeing_cricket_contest);
        init();
    }

    private void init()
    {
        context = this;
        gtype = getIntent().getExtras().getString("gtype");
        mid = getIntent().getExtras().getString("m_id");
        RecyclerView_Circket_Contest_Type = findViewById(R.id.rv_cricket_contest);
        progressBar = findViewById(R.id.progrebar_cricket_contest);
        TV_gametitle = findViewById(R.id.tv_match_title);
        IV_backArrow = findViewById(R.id.img_back_arrow_matchcontest);
        mGridLayoutManagerBrand = new GridLayoutManager(UpcomeingCricketContestActivity.this, 1);
        RecyclerView_Circket_Contest_Type.setLayoutManager(mGridLayoutManagerBrand);
        cricketContestList(gtype,mid);
        setListeners();

    }
    private void setListeners()
    {
        IV_backArrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }


    public void cricketContestList(final String gametype,final String mid)
    {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = Constant.URL+"getCricketSettingByTypeUID?Type="+gametype+"&Unique_ID="+mid;
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
                        String flag = jsonObject2.getString("flag");
                        if (flag.equals("Active"))
                        {
                            srno = jsonObject2.getString("srno");
                            String total_Memb = jsonObject2.getString("total_Memb");
                            String total_time = jsonObject2.getString("max_time");
                            String winning_amt = jsonObject2.getString("winning_amt");
                            String entry_amt = jsonObject2.getString("entry_amt");
                            String ttime = jsonObject2.getString("ttime");
                            String time_left = jsonObject2.getString("time_left");
                            String game_name = jsonObject2.getString("match_team");
                            String total_joining = jsonObject2.getString("total_joining");
                            String payout_status = jsonObject2.getString("payout_status");
                            String game_amt_type = jsonObject2.getString("game_amt_type");
                            String match_unique_id = jsonObject2.getString("match_unique_id");
                            TV_gametitle.setText(""+game_name);
                            ContestModel model = new ContestModel();
                            model.setSrno(srno);
                            model.setTotal_Memb(total_Memb);
                            model.setTotal_time(total_time);
                            model.setWiningprice(winning_amt);
                            model.setTotal_joining(total_joining);
                            model.setTime_left(time_left);
                            model.setMatch_unique_id(match_unique_id);
                            model.setGame_amt_type(game_amt_type);
                            model.setEnteryfee(entry_amt);
                            model.setFlag(flag);
                            model.setDate(ttime);
                            model.setPayout_status(payout_status);
                            arrayList.add(model);
                        }
                    }
                    CricketContestAdpter cricketContestAdpter = new CricketContestAdpter(arrayList,getApplicationContext());
                    RecyclerView_Circket_Contest_Type.setAdapter(cricketContestAdpter);
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
}
