package com.logicaltech.gamerecharge;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adpter.LiveMatchAdpter;
import adpter.MatchResultAdapter;
import adpter.MatchesAdapter;
import model.Matches_Model;
import util.Constant;
import util.MySingalton;

public class UpcomeingMatchesActivity extends AppCompatActivity
{
    RecyclerView recyclerView_Matches;
    ArrayList<Matches_Model> arrayList_matches = new ArrayList<>();
    LinearLayout LL_Upcoming_matches,LL_Live_Matches,LL_Result;
    ProgressBar progressBar;
    ImageView IV_BackArrow;
    Dialog dialog;
    WindowManager.LayoutParams lp;
    String gtype;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcomeing_matches);
        init();
        getMatchesList();
    }

    public void init() {
        gtype = getIntent().getExtras().getString("gtype");
        recyclerView_Matches = (RecyclerView) findViewById(R.id.rv_upcomeing_matches);
        LL_Live_Matches = (LinearLayout) findViewById(R.id.ll_live_match);
        LL_Upcoming_matches = (LinearLayout) findViewById(R.id.ll_matches);
        LL_Result = (LinearLayout) findViewById(R.id.ll_match_result);
        progressBar = (ProgressBar) findViewById(R.id.progrebar_matches);
        IV_BackArrow = (ImageView) findViewById(R.id.img_back_upcomeing_matches);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView_Matches.setLayoutManager(horizontalLayoutManagaer);

        LL_Upcoming_matches.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getUpcomeing();
            }
        });

        LL_Live_Matches.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getLiveMatchesList();
            }
        });

        LL_Result.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                geteMatchesResult();
            }
        });

        IV_BackArrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

    }

    public void getMatchesList()
    {
        progressBar.setVisibility(View.VISIBLE);
     //   String strurl = "http://cricapi.com/api/matches/?apikey="+ Constant.APIKEY;
        String strurl = "http://site17.bidbch.com/api/getUpcomingMatches";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, strurl, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    progressBar.setVisibility(View.GONE);
                    arrayList_matches.clear();
                    JSONArray jsonArray = response.getJSONArray("matches");
                    Constant.jsonObjectmatch = jsonArray;
                    for (int i = 0; i < jsonArray.length(); i++)
                        {
                            Matches_Model model = new Matches_Model();
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String matchStarted = jsonObject1.getString("matchStarted");
                            String squad = jsonObject1.getString("squad");
                            if (matchStarted.equals("false")&&squad.equals("true"))
                            {
                                String unique_id = jsonObject1.getString("unique_id");
                                String date = jsonObject1.getString("date");
                                String dateTimeGMT = jsonObject1.getString("dateTimeGMT");
                                String team1 = jsonObject1.getString("team-1");
                                String team2 = jsonObject1.getString("team-2");
                                if (jsonObject1.has("type"))
                                {
                                    String type = jsonObject1.getString("type");
                                    model.setType(type);
                                }

                                model.setUnique_id(unique_id);
                                model.setDate(date);
                                model.setDateTimeGMT(dateTimeGMT);
                                model.setTeam1(team1);
                                model.setTeam2(team2);
                                model.setSquad(squad);
                                model.setMatchStarted(matchStarted);
                                arrayList_matches.add(model);
                            }
                        }
                    MatchesAdapter subCategoryAdapter = new MatchesAdapter(arrayList_matches,getApplicationContext());
                    recyclerView_Matches.setAdapter(subCategoryAdapter);
                }
                catch (Exception e)
                {
                    progressBar.setVisibility(View.GONE);
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        MySingalton.getInstance(getApplicationContext()).addRequestQueue(jsonObjectRequest);
    }

    public void getLiveMatchesList() {
        try
        {
            arrayList_matches.clear();
            JSONArray jsonArray = Constant.jsonObjectmatch;
            for (int i = 0; i < jsonArray.length(); i++)
            {
                Matches_Model model = new Matches_Model();
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String matchStarted = jsonObject1.getString("matchStarted");
                String squad = jsonObject1.getString("squad");
                if (matchStarted.equals("true")&&squad.equals("true"))
                {
                    if (jsonObject1.has("winner_team"))
                    {
                        String winner_team = jsonObject1.getString("winner_team");
                        if (winner_team.equals("null"))
                        {
                            String unique_id = jsonObject1.getString("unique_id");
                            String date = jsonObject1.getString("date");
                            String dateTimeGMT = jsonObject1.getString("dateTimeGMT");
                            String team1 = jsonObject1.getString("team-1");
                            String team2 = jsonObject1.getString("team-2");
                            if (jsonObject1.has("type"))
                            {
                                String type = jsonObject1.getString("type");
                                model.setType(type);
                            }
                            if (jsonObject1.has("toss_winner_team"))
                            {
                                String toss_winner_team = jsonObject1.getString("toss_winner_team");
                                model.setToss_winner_team(toss_winner_team);
                            }
                            model.setUnique_id(unique_id);
                            model.setDate(date);
                            model.setDateTimeGMT(dateTimeGMT);
                            model.setTeam1(team1);
                            model.setTeam2(team2);
                            model.setSquad(squad);
                            model.setMatchStarted(matchStarted);
                            arrayList_matches.add(model);
                        }
                        else
                        {

                        }
                    }
                    else
                    {
                        String unique_id = jsonObject1.getString("unique_id");
                        String date = jsonObject1.getString("date");
                        String dateTimeGMT = jsonObject1.getString("dateTimeGMT");
                        String team1 = jsonObject1.getString("team-1");
                        String team2 = jsonObject1.getString("team-2");
                        if (jsonObject1.has("type"))
                        {
                            String type = jsonObject1.getString("type");
                            model.setType(type);
                        }
                        if (jsonObject1.has("toss_winner_team"))
                        {
                            String toss_winner_team = jsonObject1.getString("toss_winner_team");
                            model.setToss_winner_team(toss_winner_team);
                        }
                        model.setUnique_id(unique_id);
                        model.setDate(date);
                        model.setDateTimeGMT(dateTimeGMT);
                        model.setTeam1(team1);
                        model.setTeam2(team2);
                        model.setSquad(squad);
                        model.setMatchStarted(matchStarted);
                        arrayList_matches.add(model);
                    }
                }
            }
            LiveMatchAdpter subCategoryAdapter = new LiveMatchAdpter(arrayList_matches, getApplicationContext());
            recyclerView_Matches.setAdapter(subCategoryAdapter);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    public void getUpcomeing() {
        try
        {
            arrayList_matches.clear();
            JSONArray jsonArray = Constant.jsonObjectmatch;
            for (int i = 0; i < jsonArray.length(); i++)
            {
                Matches_Model model = new Matches_Model();
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String matchStarted = jsonObject1.getString("matchStarted");
                String squad = jsonObject1.getString("squad");
                if (matchStarted.equals("false")&&squad.equals("true"))
                {
                    String unique_id = jsonObject1.getString("unique_id");
                    String date = jsonObject1.getString("date");
                    String dateTimeGMT = jsonObject1.getString("dateTimeGMT");
                    String team1 = jsonObject1.getString("team-1");
                    String team2 = jsonObject1.getString("team-2");
                    if (jsonObject1.has("type"))
                    {
                        String type = jsonObject1.getString("type");
                        model.setType(type);
                    }
                    model.setUnique_id(unique_id);
                    model.setDate(date);
                    model.setDateTimeGMT(dateTimeGMT);
                    model.setTeam1(team1);
                    model.setTeam2(team2);
                    model.setSquad(squad);
                    model.setMatchStarted(matchStarted);
                    arrayList_matches.add(model);
                }
            }
            MatchesAdapter subCategoryAdapter = new MatchesAdapter(arrayList_matches, getApplicationContext());
            recyclerView_Matches.setAdapter(subCategoryAdapter);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void geteMatchesResult() {
        try
        {
            arrayList_matches.clear();
            JSONArray jsonArray = Constant.jsonObjectmatch;
            for (int i = 0; i < jsonArray.length(); i++)
            {
                Matches_Model model = new Matches_Model();
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String matchStarted = jsonObject1.getString("matchStarted");
                String squad = jsonObject1.getString("squad");
                if (matchStarted.equals("true")&&squad.equals("true"))
                {
                    if (jsonObject1.has("winner_team"))
                    {
                        String winner_team = jsonObject1.getString("winner_team");
                        if (winner_team.equals("null"))
                        {

                        }
                        else
                        {
                            String unique_id = jsonObject1.getString("unique_id");
                            String date = jsonObject1.getString("date");
                            String dateTimeGMT = jsonObject1.getString("dateTimeGMT");
                            String team1 = jsonObject1.getString("team-1");
                            String team2 = jsonObject1.getString("team-2");
                            if (jsonObject1.has("type"))
                            {
                                String type = jsonObject1.getString("type");
                                model.setType(type);
                            }
                            if (jsonObject1.has("toss_winner_team"))
                            {
                                String toss_winner_team = jsonObject1.getString("toss_winner_team");
                                model.setToss_winner_team(toss_winner_team);
                            }

                            model.setWinnerTeam(winner_team);
                            model.setUnique_id(unique_id);
                            model.setDate(date);
                            model.setDateTimeGMT(dateTimeGMT);
                            model.setTeam1(team1);
                            model.setTeam2(team2);
                            model.setSquad(squad);
                            model.setMatchStarted(matchStarted);
                            arrayList_matches.add(model);
                        }
                    }
                }
            }
            MatchResultAdapter matchResultAdapter = new MatchResultAdapter(arrayList_matches, getApplicationContext());
            recyclerView_Matches.setAdapter(matchResultAdapter);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    public class LiveMatchAdpter extends RecyclerView.Adapter<LiveMatchAdpter.RecyclerViewHolder> {
        public ArrayList<Matches_Model> orderList;
        public Context mContext;
        public LiveMatchAdpter(ArrayList<Matches_Model> orderList , Context context)
        {
            this.orderList = orderList;
            mContext = context;
        }
        @Override
        public LiveMatchAdpter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_matches_list, parent, false);
            return new LiveMatchAdpter.RecyclerViewHolder(view);
        }
        @Override
        public void onBindViewHolder(final LiveMatchAdpter.RecyclerViewHolder holder, final int position)
        {
            final Matches_Model account_model = orderList.get(position);
            holder.TV_Team1.setText(account_model.getTeam1());
            holder.TV_Team2.setText(account_model.getTeam2());
            holder.TV_Matches.setText(""+account_model.getTeam1()+" Vs "+account_model.getTeam2());
            holder.TV_Timing.setText(""+account_model.getDate());

            holder.LL_Select_Player.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    getLiveScore(account_model.getUnique_id());
                }
            });
        }
        @Override
        public int getItemCount()
        {
            return orderList.size();
        }

        public class RecyclerViewHolder extends RecyclerView.ViewHolder {
            TextView TV_Team1,TV_Team2,TV_Matches,TV_Timing;
            LinearLayout LL_Select_Player;
            public RecyclerViewHolder(View itemView)
            {
                super(itemView);
                TV_Team1 = (TextView) itemView.findViewById(R.id.textview_team1);
                TV_Team2 = (TextView) itemView.findViewById(R.id.textview_team2);
                TV_Matches = (TextView) itemView.findViewById(R.id.textview_current_matches);
                TV_Timing = (TextView)itemView.findViewById(R.id.textview_match_time);
                LL_Select_Player = (LinearLayout) itemView.findViewById(R.id.ll_select_player);
            }
        }

        public void getLiveScore(final String mid)
        {
         //   final String URL = "http://cricapi.com/api/cricketScore";
            final String URL = Constant.URL+"getCricketScore?unique_id="+mid;
            HashMap<String, String> params = new HashMap<String, String>();
            // params.put("unique_id", match_unique_id);
            /*params.put("unique_id", mid);
            params.put("apikey", Constant.APIKEY);*/
            JsonObjectRequest request_json = new JsonObjectRequest(URL, new JSONObject(params), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response)
                {
                    try
                    {
                        String score = response.getString("score");
                        String discription = response.getString("description");
                        String team1 = response.getString("team-1");
                        String team2 = response.getString("team-2");
                        showBounceCash(score,discription,team1,team2);
                    }
                    catch (JSONException e)
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
            MySingalton.getInstance(mContext).addRequestQueue(request_json);

        }
        private void showBounceCash(final String score,final String disc,final String team1,final String team2) {
            dialog = new Dialog(UpcomeingMatchesActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
            dialog.setContentView(R.layout.dialog_live_score);
            dialog.setCancelable(true);
            lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

            TextView TV_Teams,TV_Score,TV_Dis;

            TV_Teams = (TextView) dialog.findViewById(R.id.textview_teams);
            TV_Score = (TextView) dialog.findViewById(R.id.textviewscore);
            TV_Dis = (TextView) dialog.findViewById(R.id.textviewscorediscription);

            TV_Score.setText(""+score);
            TV_Teams.setText(""+team1+" Vs "+team2);
            TV_Dis.setText(""+disc);
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

}
