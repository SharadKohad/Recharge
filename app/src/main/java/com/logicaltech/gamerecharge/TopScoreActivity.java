package com.logicaltech.gamerecharge;

import android.content.Intent;
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

import adpter.TopScoreAdpter;
import model.TopScoreModel;
import util.Constant;

public class TopScoreActivity extends AppCompatActivity
{
    ImageView Img_Back;
    ArrayList<TopScoreModel> arrayList =new ArrayList<>();
    RecyclerView RecyclerView_Contest_Type;
    GridLayoutManager mGridLayoutManagerBrand;
    ProgressBar progressBar;
    String price;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_score);
        Img_Back = (ImageView) findViewById(R.id.img_back_arrow_top_score);
        RecyclerView_Contest_Type = (RecyclerView) findViewById(R.id.rv_top_score);
        mGridLayoutManagerBrand = new GridLayoutManager(TopScoreActivity.this, 1);
        RecyclerView_Contest_Type.setLayoutManager(mGridLayoutManagerBrand);
        progressBar = (ProgressBar) findViewById(R.id.progrebar_top_score);

        Img_Back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(TopScoreActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        price = getIntent().getExtras().getString("price");
        topScorePerticulerContst(getIntent().getExtras().getString("srno"));
    }

    public void topScorePerticulerContst(final String srno) {
        progressBar.setVisibility(View.VISIBLE);
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
                    progressBar.setVisibility(View.INVISIBLE);
                    arrayList.clear();
                    for (int i = 0; i < response.length(); i++)
                    {
                        JSONObject jsonObject2 = response.getJSONObject(i);
                        String score = jsonObject2.getString("score");
                        String username = jsonObject2.getString("username");
                        String userFile = jsonObject2.getString("userFile");

                        TopScoreModel model = new TopScoreModel();
                        model.setScore(score);
                        model.setUsername(username);
                        model.setPrice(price);
                        model.setUserFile(userFile);
                        arrayList.add(model);
                    }
                    TopScoreAdpter operator_adapter = new TopScoreAdpter(arrayList,getApplicationContext());
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
