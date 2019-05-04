package com.logicaltech.gamerecharge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

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

import adpter.ContestHistoryAdapter;
import model.ContestJoinHisModel;
import util.Constant;
import util.SessionManeger;

public class ContextParticipetionActivity extends AppCompatActivity
{
    ImageView Img_Back;
    ArrayList<ContestJoinHisModel> arrayList =new ArrayList<>();
    RecyclerView RecyclerView_Contest_Type;
    GridLayoutManager mGridLayoutManagerBrand;
    ProgressBar progressBar;
    SessionManeger sessionManeger;
    String memberCode;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context_participetion);
        Img_Back = (ImageView) findViewById(R.id.img_back_arrow_join);
        RecyclerView_Contest_Type = (RecyclerView) findViewById(R.id.rv_contest_history);
        progressBar = (ProgressBar) findViewById(R.id.progrebar_contest);
        sessionManeger = new SessionManeger(getApplicationContext());
        HashMap<String, String> hashMap = sessionManeger.getUserDetails();
        memberCode = hashMap.get(SessionManeger.MEMBER_ID);
        mGridLayoutManagerBrand = new GridLayoutManager(ContextParticipetionActivity.this, 1);
        RecyclerView_Contest_Type.setLayoutManager(mGridLayoutManagerBrand);

        Img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        contestHistory(memberCode);
    }

    public void contestHistory(final String memberCode)
    {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        //  String url = Constant.URL+"addSignUp"; // <----enter your post url here
        String url = Constant.URL+"getContestParticipateDtl?membercode="+memberCode;
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
                        String srno = jsonObject2.getString("srno");
                        String ttime = jsonObject2.getString("ttime");
                        String entry_amt = jsonObject2.getString("entry_amt");

                            ContestJoinHisModel model = new ContestJoinHisModel();
                            model.setSrno(srno);
                            model.setTtime(ttime);
                            model.setEntry_amt(entry_amt);
                            arrayList.add(model);

                    }
                    ContestHistoryAdapter operator_adapter = new ContestHistoryAdapter(arrayList,getApplicationContext());
                    RecyclerView_Contest_Type.setAdapter(operator_adapter);
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
