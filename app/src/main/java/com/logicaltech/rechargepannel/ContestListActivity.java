package com.logicaltech.rechargepannel;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import model.ContestModel;
import util.Constant;
import util.MySingalton;
import util.SessionManeger;

public class ContestListActivity extends AppCompatActivity
{
    ImageView Img_Back;
    ArrayList<ContestModel> arrayList =new ArrayList<>();
    RecyclerView RecyclerView_Contest_Type;
    GridLayoutManager mGridLayoutManagerBrand;
    String gtype,userId,srno;
    TextView TV_gametitle;
    SessionManeger sessionManeger;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest_list);
        sessionManeger = new SessionManeger(getApplicationContext());
        HashMap<String, String> hashMap = sessionManeger.getUserDetails();
        userId = hashMap.get(SessionManeger.MEMBER_ID);
        Img_Back = (ImageView) findViewById(R.id.img_back_arrow_change_password);
        RecyclerView_Contest_Type = (RecyclerView) findViewById(R.id.rv_contest);
        mGridLayoutManagerBrand = new GridLayoutManager(ContestListActivity.this, 1);
        RecyclerView_Contest_Type.setLayoutManager(mGridLayoutManagerBrand);
        TV_gametitle = (TextView) findViewById(R.id.tv_game_title);
        progressBar = (ProgressBar) findViewById(R.id.progrebar_contest);
        gtype = getIntent().getExtras().getString("gtype");
        Img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
        contestList(gtype);
    }

    public void contestList(final String gametype) {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        //  String url = Constant.URL+"addSignUp"; // <----enter your post url here
        String url = Constant.URL+"getGameSettingByType?Type="+gametype+"&ContestID=";
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
                        String total_Memb = jsonObject2.getString("total_Memb");
                        String total_time = jsonObject2.getString("max_time");
                        String winning_amt = jsonObject2.getString("winning_amt");
                        String entry_amt = jsonObject2.getString("entry_amt");
                        String flag = jsonObject2.getString("flag");
                        String ttime = jsonObject2.getString("ttime");
                        String time_left = jsonObject2.getString("time_left");
                        String game_name = jsonObject2.getString("game_name");
                        String total_joining = jsonObject2.getString("total_joining");
                        String payout_status = jsonObject2.getString("payout_status");
                        TV_gametitle.setText(""+game_name);

                        if (flag.equals("Active"))
                        {
                            ContestModel model = new ContestModel();
                            model.setSrno(srno);
                            model.setTotal_Memb(total_Memb);
                            model.setTotal_time(total_time);
                            model.setWiningprice(winning_amt);
                            model.setTotal_joining(total_joining);
                            model.setTime_left(time_left);
                            model.setEnteryfee(entry_amt);
                            model.setFlag(flag);
                            model.setDate(ttime);
                            model.setPayout_status(payout_status);
                            arrayList.add(model);
                        }
                    }
                    CotestAdpter operator_adapter = new CotestAdpter(arrayList,getApplicationContext());
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

    public class CotestAdpter extends RecyclerView.Adapter<CotestAdpter.RecyclerViewHolder> {
        public ArrayList<ContestModel> orderList;
        public Context mContext;
        public CotestAdpter(ArrayList<ContestModel> orderList , Context context)
        {
            this.orderList = orderList;
            mContext = context;
        }
        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_contest_list, parent, false);
            return new CotestAdpter.RecyclerViewHolder(view);
        }
        @Override
        public void onBindViewHolder(final RecyclerViewHolder holder, final int position)
        {

            final ContestModel account_model = orderList.get(position);
            holder.TV_Win_Amount.setText("\u20B9 "+account_model.getWiningprice());
            holder.TV_Entry_fee.setText(account_model.getEnteryfee()+"0");
            final String flag = account_model.getFlag();

            if (flag.equals("InActive"))
            {
                holder.RL_partisipet.setVisibility(View.GONE);
                holder.progressBar.setVisibility(View.GONE);
                String payout_status = account_model.getPayout_status();
                if (payout_status.equals("UnPaid"))
                {
                    amountDistribution(account_model.getSrno());
                }
            }
            else
            {
                int total_member = Integer.parseInt(account_model.getTotal_Memb());
                int total_member_by_per = 100/total_member;
                int remain_member = Integer.parseInt(account_model.getTotal_joining())*total_member_by_per;
                holder.progressBar.setProgress(remain_member);
              //  holder.TV_total_spot.setText(account_model.getTotal_Memb()+" spot");
                holder.TV_total_spot.setText(account_model.getTotal_joining()+"/"+account_model.getTotal_Memb());
            }

            int sec  = Integer.parseInt(account_model.getTime_left());
            reverseTimer(sec,holder.TV_remaing_time);
            holder.LinearLayout_Cotest.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                   // if (gtype.equals("1")&&flag.equals("Active"))
                    if (gtype.equals("1"))
                    {
                        srno = account_model.getSrno();
                //        joinContest(userId,srno);.
                        Intent intent = new Intent(ContestListActivity.this,SingleContestDetailActivity.class);
                        intent.putExtra("srno",srno);
                        startActivity(intent);

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Select Active Contest",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            holder.TV_top_score.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent intent = new Intent(ContestListActivity.this,TopScoreActivity.class);
                    intent.putExtra("srno",account_model.getSrno());
                    startActivity(intent);
                }
            });
        }
        @Override
        public int getItemCount()
        {
            return orderList.size();
        }
        public class RecyclerViewHolder extends RecyclerView.ViewHolder {
            TextView TV_Win_Amount,TV_Entry_fee,TV_total_spot,TV_remaing_time,TV_top_score;
            LinearLayout LinearLayout_Cotest;
            RelativeLayout RL_partisipet;
            ProgressBar progressBar;
            public RecyclerViewHolder(View itemView)
            {
                super(itemView);
                TV_remaing_time = (TextView) itemView.findViewById(R.id.tv_remaing_time);
                TV_Win_Amount = (TextView) itemView.findViewById(R.id.tv_win_amount);
                TV_Entry_fee = (TextView) itemView.findViewById(R.id.text_view_entry_fee);
                TV_total_spot = (TextView) itemView.findViewById(R.id.tv_total_player_list);
                LinearLayout_Cotest = (LinearLayout) itemView.findViewById(R.id.ll_contest_list);
                TV_top_score = (TextView) itemView.findViewById(R.id.tv_top_score);
                progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar_show_persentage);
                RL_partisipet = (RelativeLayout) itemView.findViewById(R.id.rl_partisipet);
            }
        }
    }

    public void reverseTimer(int Seconds,final TextView tv) {
        new CountDownTimer(Seconds* 1000+1000, 1000)
        {
            public void onTick(long millisUntilFinished)
            {
                int seconds = (int) (millisUntilFinished / 1000);
                int hours = seconds / (60 * 60);
                int tempMint = (seconds - (hours * 60 * 60));
                int minutes = tempMint / 60;
                seconds = tempMint - (minutes * 60);
                tv.setText(String.format("%02d", hours) + "hr " + String.format("%02d", minutes) + "m " + String.format("%02d", seconds)+"s");
            }
            public void onFinish()
            {
                tv.setText("Completed");
            }
        }.start();
    }

    public void joinContest(final String MemberCode, final String Srno) {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = Constant.URL+"addContest";
        StringRequest jsonObjRequest = new StringRequest(Request.Method.PUT,url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("msg");
                    if (status.equals("1"))
                    {
                        Toast.makeText(ContestListActivity.this," "+message,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ContestListActivity.this,JumpFishActivity.class);
                        intent.putExtra("srno",srno);
                        startActivity(intent);
                    }
                    else if(status.equals("2"))
                    {
                        Intent intent = new Intent(ContestListActivity.this,JumpFishActivity.class);
                        intent.putExtra("srno",srno);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(ContestListActivity.this," "+message,Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e)
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        progressBar.setVisibility(View.INVISIBLE);
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
                params.put("membercode", MemberCode);
                params.put("srno",Srno);
                return params;
            }
        };
        MySingalton.getInstance(getApplicationContext()).addRequestQueue(jsonObjRequest);
        jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(200000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyRequestQueue.add(jsonObjRequest);
    }

    public void amountDistribution(final String Srno)
    {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = Constant.URL+"addFinalAmtByContest";
        StringRequest jsonObjRequest = new StringRequest(Request.Method.PUT,url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("msg");
                    if (status.equals("1"))
                    {
                        Toast.makeText(ContestListActivity.this,"point Distribution: "+message,Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(ContestListActivity.this," "+message,Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e)
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        progressBar.setVisibility(View.INVISIBLE);
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
                params.put("srno",Srno);
                return params;
            }
        };
        MySingalton.getInstance(getApplicationContext()).addRequestQueue(jsonObjRequest);
        jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(200000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyRequestQueue.add(jsonObjRequest);
    }
}
