package adpter;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.logicaltech.gamerecharge.R;
import com.logicaltech.gamerecharge.SelectPlayerActivity;
import com.logicaltech.gamerecharge.ViewTeamActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.ContestModel;
import util.Constant;
import util.MySingalton;
import util.SessionManeger;

public class CricketContestAdpter extends RecyclerView.Adapter<CricketContestAdpter.RecyclerViewHolder>
{
    public ArrayList<ContestModel> orderList;
    public Context mContext;
    String membercode,mid;
    SessionManeger sessionManeger;
    public CricketContestAdpter(ArrayList<ContestModel> orderList , Context context)
    {
        this.orderList = orderList;
        mContext = context;
        sessionManeger = new SessionManeger(mContext);
        HashMap<String, String> hashMap = sessionManeger.getUserDetails();
        membercode = hashMap.get(SessionManeger.MEMBER_ID);
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_game_list, parent, false);
        return new CricketContestAdpter.RecyclerViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position)
    {
        final ContestModel account_model = orderList.get(position);
        holder.TV_Win_Amount.setText(account_model.getWiningprice());
        holder.TV_Entry_fee.setText(account_model.getEnteryfee()+"0");
        final String flag = account_model.getFlag();
        holder.TV_top_score.setText("View Team");

        if (account_model.getGame_amt_type().equals("POINTS"))
        {
            holder.ImageViewBab.setImageResource(R.drawable.coin);
            holder.ImageViewcoin.setImageResource(R.drawable.coin);
        }
         int total_member = Integer.parseInt(account_model.getTotal_Memb());
        int join_member = Integer.parseInt(account_model.getTotal_joining());
        int total_member_by_per = 100/total_member;
        int remain_member = Integer.parseInt(account_model.getTotal_joining())*total_member_by_per;
        holder.progressBar.setProgress(remain_member);
        holder.TV_Member_Left.setText(""+(total_member-join_member));
        holder.TV_total_spot.setText(account_model.getTotal_Memb());

        int sec  = Integer.parseInt(account_model.getTime_left());
        reverseTimer(sec,holder.TV_Hr,holder.TV_Min,holder.TV_Sec);
        holder.LinearLayout_Cotest.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                /*Intent intent = new Intent(mContext, SelectPlayerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("mid",account_model.getMatch_unique_id());
                mContext.startActivity(intent);*/
        mid = account_model.getMatch_unique_id();
        joinContestStatus(membercode,account_model.getSrno());
            }
        });

        holder.TV_top_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ViewTeamActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("mid",account_model.getMatch_unique_id());
                intent.putExtra("srno",account_model.getSrno());
                mContext.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount()
    {
        return orderList.size();
    }
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView TV_Win_Amount,TV_Entry_fee,TV_total_spot,TV_Hr,TV_Min,TV_Sec,TV_top_score,TV_Member_Left;
        RelativeLayout RL_partisipet,LinearLayout_Cotest;
        ProgressBar progressBar;
        ImageView ImageViewcoin,ImageViewBab;
        public RecyclerViewHolder(View itemView)
        {
            super(itemView);
            TV_Hr = (TextView) itemView.findViewById(R.id.tv_remaing_hr);
            TV_Min = (TextView) itemView.findViewById(R.id.tv_remaing_min);
            TV_Sec = (TextView) itemView.findViewById(R.id.tv_remaing_sec);
            TV_Member_Left = (TextView)itemView.findViewById(R.id.tv_member_left);
            TV_Win_Amount = (TextView) itemView.findViewById(R.id.tv_win_amount);
            TV_Entry_fee = (TextView) itemView.findViewById(R.id.text_view_entry_fee);
            TV_total_spot = (TextView) itemView.findViewById(R.id.tv_total_player_list);
            LinearLayout_Cotest = (RelativeLayout) itemView.findViewById(R.id.rr_contest_list);
            TV_top_score = (TextView) itemView.findViewById(R.id.tv_top_score);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar_show_persentage);
            RL_partisipet = (RelativeLayout) itemView.findViewById(R.id.rl_time_value);
            ImageViewcoin = (ImageView) itemView.findViewById(R.id.imageViewcontest);
            ImageViewBab = (ImageView) itemView.findViewById(R.id.imageViewcontest1);
        }
    }

    public void reverseTimer(int Seconds,final TextView hr,final TextView min,final TextView sec) {
        new CountDownTimer(Seconds* 1000+1000, 1000)
        {
            public void onTick(long millisUntilFinished)
            {
                int seconds = (int) (millisUntilFinished / 1000);
                int hours = seconds / (60 * 60);
                int tempMint = (seconds - (hours * 60 * 60));
                int minutes = tempMint / 60;
                seconds = tempMint - (minutes * 60);
                //    hr.setText(String.format("%02d", hours) + "hr " + String.format("%02d", minutes) + "m " + String.format("%02d", seconds)+"s");
                hr.setText(String.format("%02d",hours));
                min.setText(String.format("%02d", minutes));
                sec.setText(String.format("%02d", seconds));
            }
            public void onFinish()
            {
                hr.setText("Completed");
            }
        }.start();
    }

    public void joinContestStatus(final String MemberCode, final String Srno) {
        //progressBar.setVisibility(View.VISIBLE);
        RequestQueue MyRequestQueue = Volley.newRequestQueue(mContext);
        String url = Constant.URL+"checkplayerExists?membercode="+MemberCode+"&srno="+Srno;
        StringRequest jsonObjRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                     String playerstatus = jsonObject.getString("status");
                    String  message = jsonObject.getString("msg");
                    if (playerstatus.equals("1"))
                    {
                        Intent intent = new Intent(mContext, SelectPlayerActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("srno",Srno);
                        intent.putExtra("mid",mid);
                        mContext.startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(mContext,""+message,Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        VolleyLog.d("volley", "Error: " + error.getMessage());
                        error.printStackTrace();
                    }
                }) {
            @Override
            public String getBodyContentType()
            {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        MySingalton.getInstance(mContext).addRequestQueue(jsonObjRequest);
        jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(200000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyRequestQueue.add(jsonObjRequest);
    }
}
