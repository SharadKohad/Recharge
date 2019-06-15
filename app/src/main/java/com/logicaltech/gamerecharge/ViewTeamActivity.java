package com.logicaltech.gamerecharge;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adpter.MatchesAdapter;
import adpter.TeamAdapter;
import adpter.ViewTeamPlayerAdpter;
import model.PlayerModel;
import model.TeamModel;
import util.Constant;
import util.MySingalton;
import util.SessionManeger;

public class ViewTeamActivity extends AppCompatActivity
{
    private Context context;
    ImageView IV_Back_Arrow;
    SessionManeger sessionManeger;
    String membercode,mid,srno;
    private TextView txtTeam1, txtTeam2;
    TextView TVp1,TVp2,TVp3,TVp4,TVp5,TVp6,TVp7,TVp8,TVp9,TVp10,TVp11;
    TextView TTVp1,TTVp2,TTVp3,TTVp4,TTVp5,TTVp6,TTVp7,TTVp8,TTVp9,TTVp10,TTVp11;
    private ArrayList<String> arrTeam1;
    private ArrayList<String> arrCountry;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_team);
        sessionManeger = new SessionManeger(getApplicationContext());
        HashMap<String, String> hashMap = sessionManeger.getUserDetails();
        membercode = hashMap.get(SessionManeger.MEMBER_ID);
        mid = getIntent().getExtras().getString("mid");
        srno = getIntent().getStringExtra("srno");
        init();
    }

    private void init() {
        context = this;
        arrTeam1 = new ArrayList();
        arrCountry = new ArrayList<>();
        IV_Back_Arrow = findViewById(R.id.img_back_viewTeamPlayer);
        progressBar = findViewById(R.id.progrebar_view_team_player);
        txtTeam1 = findViewById(R.id.tv_team1);
        txtTeam2 = findViewById(R.id.tv_team2);
        TVp1 = findViewById(R.id.tv_p1);
        TVp2 = findViewById(R.id.tv_p2);
        TVp3 = findViewById(R.id.tv_p3);
        TVp4 = findViewById(R.id.tv_p4);
        TVp5 = findViewById(R.id.tv_p5);
        TVp6 = findViewById(R.id.tv_p6);
        TVp7 = findViewById(R.id.tv_p7);
        TVp8 = findViewById(R.id.tv_p8);
        TVp9 = findViewById(R.id.tv_p9);
        TVp10 = findViewById(R.id.tv_p10);
        TVp11 = findViewById(R.id.tv_p11);

        TTVp1 = findViewById(R.id.tm_tv_p1);
        TTVp2 = findViewById(R.id.tm_tv_p2);
        TTVp3 = findViewById(R.id.tm_tv_p3);
        TTVp4 = findViewById(R.id.tm_tv_p4);
        TTVp5 = findViewById(R.id.tm_tv_p5);
        TTVp6 = findViewById(R.id.tm_tv_p6);
        TTVp7 = findViewById(R.id.tm_tv_p7);
        TTVp8 = findViewById(R.id.tm_tv_p8);
        TTVp9 = findViewById(R.id.tm_tv_p9);
        TTVp10 = findViewById(R.id.tm_tv_p10);
        TTVp11 = findViewById(R.id.tm_tv_p11);
        setListeners();
        getViewTeamPlayertAPI(membercode,mid,srno);
    }

    private void setListeners(){
        IV_Back_Arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void getViewTeamPlayertAPI(final String membercode,final String mid,final String contestid) {
        progressBar.setVisibility(View.VISIBLE);
        String strurl = Constant.URL+"getFantasySquad?membercode="+membercode+"&unique_id="+mid+"&ContestID="+contestid;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, strurl, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    progressBar.setVisibility(View.GONE);
                    JSONArray jsonArray = response.getJSONArray("squad");
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String tName = jsonObject1.getString("name");
                        JSONArray playersArray = jsonObject1.getJSONArray("players");
                        if (playersArray.length()==0)
                        {
                            Toast.makeText(getApplicationContext(),"Please select Team",Toast.LENGTH_SHORT).show();
                        }
                        if (i==0)
                        {
                            txtTeam1.setText(""+tName);
                            for (int j = 0;j<playersArray.length();j++)
                            {
                                JSONObject jsonObject2 = playersArray.getJSONObject(j);
                                String pname = jsonObject2.getString("name");
                                String teamname = jsonObject2.getString("teamname");
                                arrTeam1.add(pname);
                                arrCountry.add(teamname);
                            }
                        }
                        else
                        {
                            txtTeam2.setText(""+tName);
                            for (int j = 0;j<playersArray.length();j++)
                            {
                                JSONObject jsonObject2 = playersArray.getJSONObject(j);
                                String pname = jsonObject2.getString("name");
                                String teamname = jsonObject2.getString("teamname");
                                arrTeam1.add(pname);
                                arrCountry.add(teamname);
                            }
                        }
                    }
                    for (int i=0;i<arrTeam1.size();i++)
                    {
                        if (i==0)
                        {
                            TVp1.setText(""+arrTeam1.get(i));
                            TTVp1.setText(""+arrCountry.get(i));
                        }
                        else if(i==1)
                        {
                            TVp2.setText(""+arrTeam1.get(i));
                            TTVp2.setText(""+arrCountry.get(i));
                        }
                        else if(i==2)
                        {
                            TVp3.setText(""+arrTeam1.get(i));
                            TTVp3.setText(""+arrCountry.get(i));
                        }
                        else if(i==3)
                        {
                            TVp4.setText(""+arrTeam1.get(i));
                            TTVp4.setText(""+arrCountry.get(i));
                        }
                        else if(i==4)
                        {
                            TVp5.setText(""+arrTeam1.get(i));
                            TTVp5.setText(""+arrCountry.get(i));
                        }
                        else if(i==5)
                        {
                            TVp6.setText(""+arrTeam1.get(i));
                            TTVp6.setText(""+arrCountry.get(i));
                        }
                        else if(i==6)
                        {
                            TVp7.setText(""+arrTeam1.get(i));
                            TTVp7.setText(""+arrCountry.get(i));
                        }
                        else if(i==7)
                        {
                            TVp8.setText(""+arrTeam1.get(i));
                            TTVp8.setText(""+arrCountry.get(i));
                        }
                        else if(i==8)
                        {
                            TVp9.setText(""+arrTeam1.get(i));
                            TTVp9.setText(""+arrCountry.get(i));
                        }
                        else if(i==9)
                        {
                            TVp10.setText(""+arrTeam1.get(i));
                            TTVp10.setText(""+arrCountry.get(i));
                        }
                        else if(i==10)
                        {
                            TVp11.setText(""+arrTeam1.get(i));
                            TTVp11.setText(""+arrCountry.get(i));
                        }
                    }
                }
                catch (Exception e)
                {
                   // progressBar.setVisibility(View.GONE);
                    System.out.println(e);
                }
            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        System.out.println("error...");
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        MySingalton.getInstance(getApplicationContext()).addRequestQueue(jsonObjectRequest);
    }

}
