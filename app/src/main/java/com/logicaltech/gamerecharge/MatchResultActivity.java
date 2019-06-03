package com.logicaltech.gamerecharge;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import adpter.BattingAdapter;
import adpter.BowingAdpter;
import model.Batting_Model;
import util.Constant;
import util.MySingalton;

public class MatchResultActivity extends AppCompatActivity
{
    ImageView IV_Back_Arrow;
    TextView TV_Toss,TV_Wining_Team,TV_Team1,TV_Team2;
    RecyclerView RV_Batting_LineUp,RV_Bowler;
    ArrayList<Batting_Model> arrayList_matches_sammary = new ArrayList<>();
    ArrayList<Batting_Model> arrayList_matches_sammary1 = new ArrayList<>();
    LinearLayout LL_Score_Cart;
    String mid;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_result);
        IV_Back_Arrow = (ImageView) findViewById(R.id.img_back_match_result);
        TV_Toss = (TextView) findViewById(R.id.textview_toss);
        TV_Wining_Team = (TextView) findViewById(R.id.textview_wining);
        TV_Team1 = (TextView) findViewById(R.id.textview_team1);
        TV_Team2 = (TextView) findViewById(R.id.textview_team2);
        RV_Batting_LineUp = (RecyclerView) findViewById(R.id.RV_Batting_lineup);
        RV_Bowler = (RecyclerView) findViewById(R.id.RV_Bowing_lineup);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        RV_Batting_LineUp.setLayoutManager(horizontalLayoutManagaer);
        LinearLayoutManager horizontalLayoutManagaer1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        RV_Bowler.setLayoutManager(horizontalLayoutManagaer1);
        LL_Score_Cart = (LinearLayout) findViewById(R.id.LL_ScoreCard);

        mid = getIntent().getExtras().getString("mid");

        IV_Back_Arrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        getMatchesSummery(mid);

        TV_Team1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LL_Score_Cart.setVisibility(View.VISIBLE);
                getBattingLineUp(TV_Team1.getText().toString());
                getBoweling(0);
            }
        });

        TV_Team2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LL_Score_Cart.setVisibility(View.VISIBLE);
                getBattingLineUp(TV_Team2.getText().toString());
                getBoweling(1);
            }
        });

    }
    public void getMatchesSummery(final String match_id) {
        final String URL = "http://cricapi.com/api/fantasySummary";
     //   final String URL = "http://site17.bidbch.com/api/getFantasySummary?unique_id="+match_id;
        HashMap<String, String> params = new HashMap<String, String>();
      //  params.put("unique_id", match_unique_id);
        params.put("unique_id", match_id);
        params.put("apikey", Constant.APIKEY);
        JsonObjectRequest request_json = new JsonObjectRequest(URL, new JSONObject(params), new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    JSONObject data = response.getJSONObject("data");
                    Constant.jsonObjectmatchSummery = data;
                    TV_Toss.setText(data.getString("toss_winner_team")+" Win The Toss");
                    TV_Wining_Team.setText(data.getString("winner_team")+" Won");

                    JSONArray jsonArray = data.getJSONArray("batting");
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject batting = jsonArray.getJSONObject(i);
                        String team1_name = batting.getString("title");
                        if (i==0)
                        {
                            TV_Team1.setText(""+team1_name);
                        }
                        else
                        {
                            TV_Team2.setText(""+team1_name);
                        }
                    }
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        MySingalton.getInstance(getApplicationContext()).addRequestQueue(request_json);
    }

    public void getBattingLineUp(final String teamname) {
        try
        {
            arrayList_matches_sammary.clear();
            JSONObject data = Constant.jsonObjectmatchSummery;
            JSONArray jsonArray = data.getJSONArray("batting");
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject batting = jsonArray.getJSONObject(i);
                String team1_name = batting.getString("title");
                if (team1_name.equals(teamname))
                {
                    JSONArray scores = batting.getJSONArray("scores");
                    for (int j = 0; j < scores.length(); j++)
                    {
                        Batting_Model batting_model = new Batting_Model();
                        JSONObject jsonObjectScore = scores.getJSONObject(j);
                        if (jsonObjectScore.has("dismissal"))
                        {
                            // JSONObject jsonObjectdismissalby  = jsonObjectScore.getJSONObject("dismissal-by");
                            // String name = jsonObjectdismissalby.getString("name");
                            //  batting_model.setDismissal_by(name);
                            String dismissal = jsonObjectScore.getString("dismissal");
                            batting_model.setDismissal(dismissal);
                        }
                        else
                        {
                            String dismissal = jsonObjectScore.getString("detail");
                            batting_model.setDismissal(dismissal);
                        }
                        //     String dismissal = jsonObjectScore.getString("dismissal");
                        String SR = jsonObjectScore.getString("SR");
                        String s6 = jsonObjectScore.getString("6s");
                        String s4 = jsonObjectScore.getString("4s");
                        String B = jsonObjectScore.getString("B");
                        String R = jsonObjectScore.getString("R");
                        String dismissal_info = jsonObjectScore.getString("dismissal-info");
                        String batsman = jsonObjectScore.getString("batsman");

                        batting_model.setSR(SR);
                        batting_model.setS6(s6);
                        batting_model.setS4(s4);
                        batting_model.setB(B);
                        batting_model.setR(R);
                        batting_model.setDismissal_info(dismissal_info);
                        batting_model.setBatsman(batsman);
                        arrayList_matches_sammary.add(batting_model);
                    }
                }
            }
            BattingAdapter battingAdapter = new BattingAdapter(arrayList_matches_sammary, getApplicationContext());
            RV_Batting_LineUp.setAdapter(battingAdapter);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            System.out.println("My Error" + e);
        }
    }

    public void getBoweling(final int token) {
        try
        {
            arrayList_matches_sammary1.clear();
            JSONObject data = Constant.jsonObjectmatchSummery;
            JSONArray jsonArray = data.getJSONArray("bowling");
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject bowling = jsonArray.getJSONObject(i);
                if (i==token)
                {
                    JSONArray scores = bowling.getJSONArray("scores");
                    for (int j = 0; j < scores.length(); j++)
                    {
                        Batting_Model batting_model = new Batting_Model();
                        JSONObject jsonObjectScore = scores.getJSONObject(j);
                        String Econ = jsonObjectScore.getString("Econ");
                        String W = jsonObjectScore.getString("W");
                        String R = jsonObjectScore.getString("R");
                        String M = jsonObjectScore.getString("M");
                        String O = jsonObjectScore.getString("O");
                        String bowler = jsonObjectScore.getString("bowler");

                        batting_model.setEconimy(Econ);
                        batting_model.setW(W);
                        batting_model.setR(R);
                        batting_model.setM(M);
                        batting_model.setO(O);
                        batting_model.setBatsman(bowler);
                        arrayList_matches_sammary1.add(batting_model);
                    }
                }
            }
            BowingAdpter battingAdapter = new BowingAdpter(arrayList_matches_sammary1, getApplicationContext());
            RV_Bowler.setAdapter(battingAdapter);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            System.out.println("My Error" + e);
        }
    }
    
}
