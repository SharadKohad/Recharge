package com.logicaltech.gamerecharge;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

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
import java.util.HashMap;
import java.util.Map;

import hb.xvideoplayer.MxVideoPlayer;
import hb.xvideoplayer.MxVideoPlayerWidget;
import util.Constant;

public class GameVideoActivity extends AppCompatActivity
{
    String gtype,video_url = "";
    ProgressDialog pd;
    ImageView BackArrow;
    MxVideoPlayerWidget videoPlayerWidget;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_video);
        gtype = getIntent().getStringExtra("gametype");
        BackArrow = (ImageView) findViewById(R.id.img_back_arrow_video_play);
        videoPlayerWidget = (MxVideoPlayerWidget) findViewById(R.id.mpw_video_player);

        gameVideo(gtype);

        BackArrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
    }

    public void gameVideo(final String gameType) {
        pd = new ProgressDialog(GameVideoActivity.this);
        pd.setMessage("Buffering video please wait...");
        pd.show();
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        //  String url = Constant.URL+"addSignUp"; // <----enter your post url here
        String url = Constant.URL+"getGameVideosByType?Type="+gameType;
        JsonArrayRequest MyStringRequest = new JsonArrayRequest(Request.Method.POST, url, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                try
                {
                    pd.dismiss();
                    for (int i = 0; i < response.length(); i++)
                    {
                        JSONObject jsonObject2 = response.getJSONObject(i);
                        String game_name = jsonObject2.getString("game_name");
                        String logo = jsonObject2.getString("logo");
                        video_url = jsonObject2.getString("videourl");
                        videoPlayerWidget.startPlay("http://site17.bidbch.com/"+video_url, MxVideoPlayer.SCREEN_LAYOUT_NORMAL, game_name);
                    }
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
        MyStringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyRequestQueue.add(MyStringRequest);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
      //  player.pause();
    }
}
