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
import android.widget.TextView;

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

public class ContestListActivity extends AppCompatActivity
{
    ImageView Img_Back;
    ArrayList<ContestModel> arrayList =new ArrayList<>();
    RecyclerView RecyclerView_Contest_Type;
    GridLayoutManager mGridLayoutManagerBrand;
    String gtype;
    TextView TV_gametitle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest_list);
        Img_Back = (ImageView) findViewById(R.id.img_back_arrow_change_password);
        RecyclerView_Contest_Type = (RecyclerView) findViewById(R.id.rv_contest);
        mGridLayoutManagerBrand = new GridLayoutManager(ContestListActivity.this, 1);
        RecyclerView_Contest_Type.setLayoutManager(mGridLayoutManagerBrand);
        TV_gametitle = (TextView) findViewById(R.id.tv_game_title);

        gtype = getIntent().getExtras().getString("gtype");

        Img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        operatorType(gtype);

    }

    public void operatorType(final String gametype)
    {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        //  String url = Constant.URL+"addSignUp"; // <----enter your post url here
        String url = Constant.URL+"getGameSettingByType?Type="+gametype;
        JsonArrayRequest MyStringRequest = new JsonArrayRequest(Request.Method.POST, url, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                try
                {
                    arrayList.clear();
                    for (int i = 0; i < response.length(); i++)
                    {
                        JSONObject jsonObject2 = response.getJSONObject(i);
                        String total_Memb = jsonObject2.getString("total_Memb");
                        String total_time = jsonObject2.getString("max_time");
                        String winning_amt = jsonObject2.getString("winning_amt");
                        String entry_amt = jsonObject2.getString("entry_amt");
                        String flag = jsonObject2.getString("flag");
                        String ttime = jsonObject2.getString("ttime");
                        String time_left = jsonObject2.getString("time_left");
                        String game_name = jsonObject2.getString("game_name");
                        String total_joining = jsonObject2.getString("total_joining");
                        TV_gametitle.setText(""+game_name);
                        ContestModel model = new ContestModel();
                        model.setTotal_Memb(total_Memb);
                        model.setTotal_time(total_time);
                        model.setWiningprice(winning_amt);
                        model.setTime_left(time_left);
                        model.setEnteryfee(entry_amt);
                        model.setFlag(flag);
                        model.setDate(ttime);
                        arrayList.add(model);
                    }
                    CotestAdpter operator_adapter = new CotestAdpter(arrayList,getApplicationContext());
                    RecyclerView_Contest_Type.setAdapter(operator_adapter);
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

    public class CotestAdpter extends RecyclerView.Adapter<CotestAdpter.RecyclerViewHolder>
    {
        public ArrayList<ContestModel> orderList;
        public Context mContext;
        String operatorType;
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
            holder.TV_Entry_fee.setText(account_model.getEnteryfee());
            holder.TV_spot_remaing.setText("1 Spot Left ");
            holder.TV_total_spot.setText(account_model.getTotal_Memb()+" Spot");

            int total_member = Integer.parseInt(account_model.getTotal_Memb());

            int total_member_by_per = 100/total_member;
            int remain_member = 4*total_member_by_per;
            holder.progressBar.setProgress(remain_member);
            //    holder.TV_remaing_time.setText(account_model.getTotal_time());
            int sec  = Integer.parseInt(account_model.getTime_left());
            // sec = sec*60*60;
            reverseTimer(sec,holder.TV_remaing_time);
            holder.LinearLayout_Cotest.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (gtype.equals("1"))
                    {
                        Intent intent = new Intent(ContestListActivity.this,JumpFishActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                       /* Intent intent=new Intent(mContext,DTHActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        intent.putExtra("token","1");
                        intent.putExtra("code",orderList.get(position).getCode());
                        intent.putExtra("operator",orderList.get(position).getOperate());
                        mContext.getApplicationContext().startActivity(intent);*/
                    }
                }
            });
        }
        @Override
        public int getItemCount()
        {
            return orderList.size();
        }
        public class RecyclerViewHolder extends RecyclerView.ViewHolder
        {
            TextView TV_Win_Amount,TV_Entry_fee,TV_spot_remaing,TV_total_spot,TV_remaing_time;
            LinearLayout LinearLayout_Cotest;
            ProgressBar progressBar;
            public RecyclerViewHolder(View itemView)
            {
                super(itemView);
                TV_Win_Amount = (TextView) itemView.findViewById(R.id.tv_win_amount);
                TV_Entry_fee = (TextView) itemView.findViewById(R.id.text_view_entry_fee);
                TV_total_spot = (TextView) itemView.findViewById(R.id.tv_total_enrty);
                TV_spot_remaing = (TextView) itemView.findViewById(R.id.tv_remaining_left);
                TV_remaing_time = (TextView) itemView.findViewById(R.id.tv_remaing_time);
                LinearLayout_Cotest = (LinearLayout) itemView.findViewById(R.id.ll_contest_list);
                progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar_show_persentage);
            }
        }
    }

    public void reverseTimer(int Seconds,final TextView tv)
    {
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

}
