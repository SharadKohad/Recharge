package adpter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonObject;
import com.logicaltech.gamerecharge.R;
import com.logicaltech.gamerecharge.SelectPlayerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import model.Matches_Model;
import util.Constant;
import util.MySingalton;

public class LiveMatchAdpter extends RecyclerView.Adapter<LiveMatchAdpter.RecyclerViewHolder>
{
    public ArrayList<Matches_Model> orderList;
    public Context mContext;
    public LiveMatchAdpter(ArrayList<Matches_Model> orderList , Context context)
    {
        this.orderList = orderList;
        mContext = context;
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_matches_list, parent, false);
        return new LiveMatchAdpter.RecyclerViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position)
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
       //         getLiveScore();
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


    public void getLiveScore()
    {
        final String URL = "http://cricapi.com/api/cricketScore";
        HashMap<String, String> params = new HashMap<String, String>();
       // params.put("unique_id", match_unique_id);
        params.put("unique_id", "1173354");
        params.put("apikey", Constant.APIKEY);
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

    private void showBounceCash(final String score,final String disc,final String team1,final String team2)
    {
        final Dialog dialog;
        int layout_parms;

        WindowManager.LayoutParams lp;
        dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.setContentView(R.layout.dialog_live_score);
        dialog.setCancelable(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            layout_parms = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }
        else
        {
            layout_parms = WindowManager.LayoutParams.TYPE_PHONE;
        }
        lp = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, layout_parms,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        TextView TV_Teams,TV_Score,TV_Dis;

        TV_Teams = (TextView) dialog.findViewById(R.id.textview_teams);
        TV_Score = (TextView) dialog.findViewById(R.id.textviewscore);
        TV_Dis = (TextView) dialog.findViewById(R.id.textviewscorediscription);

        TV_Score.setText(""+score);
        TV_Teams.setText(""+team1+" Vs "+team2);
        TV_Dis.setText(""+disc);
       /* ((Button) dialog.findViewById(R.id.btn_close)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });*/
        dialog.show();
        dialog.getWindow().setAttributes(lp);

    }
}
