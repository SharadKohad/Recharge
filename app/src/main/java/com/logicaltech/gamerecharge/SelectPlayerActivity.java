package com.logicaltech.gamerecharge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import adpter.TeamAdapter;
import model.PlayerModel;
import model.TeamModel;
import util.Constant;
import util.SessionManeger;

public class SelectPlayerActivity extends AppCompatActivity
{
    private Context context;
    private TextView txtTeam1, txtTeam2, txt_selectTeam;
    private TeamAdapter mAdapter;
    private List<PlayerModel> team1List, team2List, finallist;
    private int selectedTeam = 0;
    ProgressBar progressBar;
    String match_unique_id, membercode, srno;
    SessionManeger sessionManeger;
    ImageView IV_Back_Arrow;
    TextView TV_Count, txtViewT1, txtViewT2;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_player);
        match_unique_id = getIntent().getExtras().getString("mid");
        srno = getIntent().getExtras().getString("srno");
        sessionManeger = new SessionManeger(getApplicationContext());
        HashMap<String, String> hashMap = sessionManeger.getUserDetails();
        membercode = hashMap.get(SessionManeger.MEMBER_ID);
        init();
    }

    private void init() {
        context = this;
        txtTeam1 = findViewById(R.id.tv_team1);
        txtTeam2 = findViewById(R.id.tv_team2);
        progressBar = findViewById(R.id.progrebar_matches);
        IV_Back_Arrow = findViewById(R.id.img_back_upcomeing_matches);
        txt_selectTeam = findViewById(R.id.textview_create_team);
        TV_Count = findViewById(R.id.tvselectplayer);
        txtViewT1 = (TextView) findViewById(R.id.tvteamcount1);
        txtViewT2 = (TextView) findViewById(R.id.tvteamcount2);
        team1List = new ArrayList<>();
        team2List = new ArrayList<>();
        finallist = new ArrayList<>();
        setListeners();
        setRecyclerView();
        getMatchTeamListAPI();
    }

    private void setListeners() {
        txtTeam1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTeam = 0;
                mAdapter.updateAdapter(team1List);
            }
        });

        txtTeam2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTeam = 1;
                mAdapter.updateAdapter(team2List);
            }
        });

        IV_Back_Arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txt_selectTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // String new_id = pid.substring(0,pid.length()-1);
                //     selectPlayer(membercode,getSelectedPlayerId(),match_unique_id);

                Intent intent = new Intent(SelectPlayerActivity.this, CreateTeamActivity.class);
                intent.putExtra("playerlist", getSelectedPlayerList());
                intent.putExtra("mid", match_unique_id);
                intent.putExtra("srno", srno);
                startActivity(intent);
            }
        });
    }

    private void setRecyclerView() {
        mAdapter = new TeamAdapter(context, new ArrayList<PlayerModel>(), new TeamAdapter.TeamAdapterInterface() {
            @Override
            public void getPosition(int position) {
                //modify list item to reflect changed data
                if (selectedTeam == 0) {
                    manipulateListData(team1List, position);
                } else {
                    manipulateListData(team2List, position);
                }
            }
        });
        RecyclerView mRecyclerView = findViewById(R.id.rv_select_player);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(mAdapter);
    }

    //modify list with selected players also checking if 11 players are selected
    private void manipulateListData(List<PlayerModel> list, int position) {
        if (getSelectedPlayerCount() < 11)
        {
            list.get(position).setIs_selected(!list.get(position).isIs_selected());
        }
        else
        {
            if (list.get(position).isIs_selected())
            {
                list.get(position).setIs_selected(!list.get(position).isIs_selected());
            }
            else
            {
                Toast.makeText(context, "Max 11 players allowed", Toast.LENGTH_SHORT).show();
            }
        }
        TV_Count.setText(""+getSelectedPlayerCount()+"/11");
        mAdapter.updateAdapter(list);
    }
    //runs a loop with both team list getting the selected count.
    private int getSelectedPlayerCount() {
        int count = 0;
        for (PlayerModel playerModel : team1List)
        {
            if (playerModel.isIs_selected())
            {
                count++;
            }
        }
        txtViewT1.setText(""+count);
        int c1 = 0;
        for (PlayerModel playerModel : team2List)
        {
            if (playerModel.isIs_selected())
            {
                count++;
                c1++;
            }
        }
        txtViewT2.setText(""+c1);
        return count;
    }

  /*  //modify list with selected players also checking if 11 players are selected
    private void manipulateListData(List<PlayerModel> list, int position)
    {
        Integer[] selectedArr = getSelectedPlayerCount();
        int count = selectedArr[0] + selectedArr[1];
        if (count < 11)
        {
            int currentTeamCount = 0;
            if (selectedTeam == 0)
            {
                currentTeamCount = selectedArr[0];
            }
            else
            {
                currentTeamCount = selectedArr[1];
            }
            if (currentTeamCount < 6)
            {
                list.get(position).setIs_selected(!list.get(position).isIs_selected());
            }
            else
            {
                Toast.makeText(context, "Max only 6 players allowed :|", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            if (list.get(position).isIs_selected())
            {
                list.get(position).setIs_selected(!list.get(position).isIs_selected());
            }
            else
            {
                Toast.makeText(context, "Max 11 players allowed", Toast.LENGTH_SHORT).show();
            }
        }
        TV_Count.setText("" + count + "/11");
        mAdapter.updateAdapter(list);
    }*/
   /* //runs a loop with both team list getting the selected count.
    private Integer[] getSelectedPlayerCount()
    {
        Integer[] selectedCount = new Integer[2];
        int count_team_1 = 0, count_team_2 = 0;
        for (PlayerModel playerModel : team1List)
        {
            if (playerModel.isIs_selected())
            {
                count_team_1++;
            }
        }
        txtViewT1.setText("" + count_team_1);
        for (PlayerModel playerModel : team2List)
        {
            if (playerModel.isIs_selected())
            {
                count_team_2++;
            }
        }
        txtViewT2.setText("" + count_team_2);
        selectedCount[0] = count_team_1;
        selectedCount[1] = count_team_2;
        return selectedCount;
    }*/
   /* private String getSelectedPlayerId() {
        String pid = "";
        for (PlayerModel playerModel : team1List)
        {
            if (playerModel.isIs_selected())
            {
                pid = pid+playerModel.getPid()+"~";
            }
        }
        for (PlayerModel playerModel : team2List)
        {
            if (playerModel.isIs_selected())
            {
                pid = pid+playerModel.getPid()+"~";
            }
        }
        if (pid.substring(pid.length()-1).equalsIgnoreCase("~"))
        {
            pid = pid.substring(0,pid.length()-1);
        }

        return pid;
    }*/

    private ArrayList<PlayerModel> getSelectedPlayerList() {
        ArrayList<PlayerModel> templist = new ArrayList<>();
        for (PlayerModel playerModel : team1List) {
            if (playerModel.isIs_selected()) {
                templist.add(playerModel);
            }
        }
        for (PlayerModel playerModel : team2List) {
            if (playerModel.isIs_selected()) {
                templist.add(playerModel);
            }
        }
       /* if (pid.substring(pid.length()-1).equalsIgnoreCase("~"))
        {
            pid = pid.substring(0,pid.length()-1);
        }*/

        return templist;
    }

    public void getMatchTeamListAPI()
    {
        final String URL = "http://cricapi.com/api/fantasySquad";
        JSONObject params = new JSONObject();
        try {
            params.put("unique_id", match_unique_id);
            params.put("apikey", Constant.APIKEY);
            // APIKEY="TmQf9rBDhAcsi2IRaCnzSwKJGeH2";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //  params.put("unique_id", "1173354");
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", "RESPONSE  = " + response);
                try {
                    Gson gson = new Gson();
                    Object json = null;
                    String jsonString = null;
                    try {
                        jsonString = response.getString("squad");
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    JSONArray jsonArray = new JSONArray(jsonString);
                    ArrayList<TeamModel> arrList = new ArrayList();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        TeamModel model = gson.fromJson(object.toString(), TeamModel.class);
                        arrList.add(model);
                    }
                    txtTeam1.setText(arrList.get(0).getName());
                    txtTeam2.setText(arrList.get(1).getName());
                    team1List = Arrays.asList(arrList.get(0).getPlayers());
                    team2List = Arrays.asList(arrList.get(1).getPlayers());
                    //update team 1 data
                    mAdapter.updateAdapter(team1List);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(mJsonObjectRequest);
        mJsonObjectRequest.setShouldCache(false);
        mJsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

}
