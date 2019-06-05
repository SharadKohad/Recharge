package adpter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

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
import com.logicaltech.gamerecharge.MatchResultActivity;
import com.logicaltech.gamerecharge.R;
import com.logicaltech.gamerecharge.SelectPlayerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.Matches_Model;
import util.Constant;
import util.SessionManeger;

public class MatchResultAdapter extends RecyclerView.Adapter<MatchResultAdapter.RecyclerViewHolder>
{
    public ArrayList<Matches_Model> orderList;
    public Context mContext;
    public int score,totalscore;
    SessionManeger sessionManeger;
    String membercode;
    public MatchResultAdapter(ArrayList<Matches_Model> orderList , Context context)
    {
        this.orderList = orderList;
        mContext = context;
        sessionManeger = new SessionManeger(mContext);
        HashMap<String, String> hashMap = sessionManeger.getUserDetails();
        membercode = hashMap.get(SessionManeger.MEMBER_ID);
    }
    
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_matches_result_list, parent, false);
        return new MatchResultAdapter.RecyclerViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position)
    {
        final Matches_Model account_model = orderList.get(position);
        holder.TV_Team1.setText(account_model.getTeam1());
        holder.TV_Team2.setText(account_model.getTeam2());
        holder.TV_Matches.setText(""+account_model.getTeam1()+" Vs "+account_model.getTeam2());
        holder.TV_Match_Result.setText(account_model.getWinnerTeam()+" Win");
        holder.TV_View_Score.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(mContext, MatchResultActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("mid",account_model.getUnique_id());
                mContext.startActivity(intent);
            }
        });

        holder.TV_Submit_Score.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                cricketHighScore(membercode,account_model.getUnique_id());
               // showBounceCash();
            }
        });
    }
    @Override
    public int getItemCount()
    {
        return orderList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView TV_Team1,TV_Team2,TV_Matches,TV_Match_Result,TV_View_Score,TV_Submit_Score;
        LinearLayout LL_Select_Player;
        public RecyclerViewHolder(View itemView)
        {
            super(itemView);
            TV_Team1 = (TextView) itemView.findViewById(R.id.textview_team1);
            TV_Team2 = (TextView) itemView.findViewById(R.id.textview_team2);
            TV_Matches = (TextView) itemView.findViewById(R.id.textview_current_matches);
            TV_View_Score = (TextView) itemView.findViewById(R.id.textview_scoreboard);
            TV_Submit_Score = (TextView)itemView.findViewById(R.id.textview_submitscore);
            TV_Match_Result = (TextView)itemView.findViewById(R.id.textview_match_wining_team);
            LL_Select_Player = (LinearLayout) itemView.findViewById(R.id.ll_select_player);
        }
    }

    public void cricketHighScore(final String membercode,final String unique_id) {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(mContext);
        String url = Constant.URL+"getCricketPlayerPointsByUID?membercode="+membercode+"&Unique_ID="+unique_id;
        JsonArrayRequest MyStringRequest = new JsonArrayRequest(Request.Method.POST, url, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                try
                {
                    String respo = response.toString();
                    if (respo.equals("[]"))
                    {
                        Toast.makeText(mContext,"Sorry You can not join contest on this Match:",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        for (int i = 0; i < response.length(); i++)
                        {
                            JSONObject jsonObject1 = response.getJSONObject(i);
                            score = jsonObject1.getInt("total_score");
                            totalscore = totalscore+score;
                        }
                        // System.out.println("High Score: "+totalscore);
                        Toast.makeText(mContext,"High Score Submited:"+totalscore,Toast.LENGTH_SHORT).show();
                        totalscore = 0;
                    }

                }
                catch (JSONException e)
                {
                  //  progressBar.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error)
            {
              //  progressBar.setVisibility(View.INVISIBLE);
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

   /* private void showBounceCash()
    {
        final Dialog dialog;
        WindowManager.LayoutParams lp;
        dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_tournament_info);
        dialog.setCancelable(true);
        lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


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
    }*/

}
