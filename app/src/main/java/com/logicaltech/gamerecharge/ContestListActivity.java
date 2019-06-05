package com.logicaltech.gamerecharge;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adpter.TopFour_Contest_List;
import adpter.TopThree_Contest_ListAdapter;
import model.ContestModel;
import model.TopScoreModel;
import util.Constant;
import util.SessionManeger;

public class ContestListActivity extends AppCompatActivity
{
    ImageView Img_Back;
    ArrayList<ContestModel> arrayList =new ArrayList<>();
    ArrayList<TopScoreModel> arrayList1 =new ArrayList<>();
    RecyclerView RecyclerView_Contest_Type,RecyclerView_Top_Three_Contest,RecyclerView_Contest_Type1;
    GridLayoutManager mGridLayoutManagerBrand,mGridLayoutManagerBrand1;
    LinearLayout LL_Current_Tournaments,LL_Current_Heroes,LL_Show_Tournaments,LL_show_heroes;
    String gtype,userId,srno,winning_amt;
    TextView TV_gametitle,TV_game_Name;
    SessionManeger sessionManeger;
    ProgressBar progressBar;
    RelativeLayout RL_Background_colour;
    JSONArray jsonArray;
    CardView CV_Contest;
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
        RecyclerView_Top_Three_Contest = (RecyclerView) findViewById(R.id.rv_heroes);
        RecyclerView_Contest_Type1 = (RecyclerView) findViewById(R.id.rv_top_four_contest);
        mGridLayoutManagerBrand1 = new GridLayoutManager(ContestListActivity.this, 1);
        RecyclerView_Contest_Type1.setLayoutManager(mGridLayoutManagerBrand1);
        mGridLayoutManagerBrand = new GridLayoutManager(ContestListActivity.this, 1);
        RecyclerView_Contest_Type.setLayoutManager(mGridLayoutManagerBrand);
        TV_gametitle = (TextView) findViewById(R.id.tv_game_title);
        TV_game_Name = (TextView) findViewById(R.id.tv_game_name);
        progressBar = (ProgressBar) findViewById(R.id.progrebar_contest);
        LL_Current_Tournaments = (LinearLayout) findViewById(R.id.ll_current_tournament);
        LL_Current_Heroes = (LinearLayout) findViewById(R.id.ll_current_heroes);
        LL_show_heroes = (LinearLayout) findViewById(R.id.ll_todays_heroes);
        RL_Background_colour = (RelativeLayout) findViewById(R.id.rl_contest_list_background);
        LL_Show_Tournaments = (LinearLayout)findViewById(R.id.ll_ballel);
        CV_Contest = (CardView) findViewById(R.id.cv_cotest_top_four_contest);
        gtype = getIntent().getExtras().getString("gtype");

        Img_Back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(ContestListActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        contestList(gtype);

        LL_Current_Tournaments.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                LL_Show_Tournaments.setVisibility(View.VISIBLE);
                LL_show_heroes.setVisibility(View.GONE);
                RL_Background_colour.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });

        LL_Current_Heroes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                LL_Show_Tournaments.setVisibility(View.GONE);
                LL_show_heroes.setVisibility(View.VISIBLE);
                RL_Background_colour.setBackgroundColor(getResources().getColor(R.color.red_600));
            }
        });
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView_Top_Three_Contest.setLayoutManager(horizontalLayoutManagaer);
    }

    public void contestList(final String gametype)
    {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        //  String url = Constant.URL+"addSignUp"; // <----enter your post url here
        String url = Constant.URL+"getGameSettingByType?Type="+gametype+"&ContestID=&Status=1";
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
                        srno = jsonObject2.getString("srno");
                        topScorePerticulerContst(srno);
                        String total_Memb = jsonObject2.getString("total_Memb");
                        String total_time = jsonObject2.getString("max_time");
                        winning_amt = jsonObject2.getString("winning_amt");
                        String entry_amt = jsonObject2.getString("entry_amt");
                        String flag = jsonObject2.getString("flag");
                        String ttime = jsonObject2.getString("ttime");
                        String time_left = jsonObject2.getString("time_left");
                        String game_name = jsonObject2.getString("game_name");
                        String total_joining = jsonObject2.getString("total_joining");
                        String payout_status = jsonObject2.getString("payout_status");
                        String game_amt_type = jsonObject2.getString("game_amt_type");
                        TV_gametitle.setText(""+game_name);
                        TV_game_Name.setText(""+game_name);
                        ContestModel model = new ContestModel();
                        model.setSrno(srno);
                        model.setTotal_Memb(total_Memb);
                        model.setTotal_time(total_time);
                        model.setWiningprice(winning_amt);
                        model.setTotal_joining(total_joining);
                        model.setTime_left(time_left);

                        model.setGame_amt_type(game_amt_type);
                        model.setEnteryfee(entry_amt);
                        model.setFlag(flag);
                        model.setDate(ttime);
                        model.setPayout_status(payout_status);
                        arrayList.add(model);
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_game_list, parent, false);
            return new CotestAdpter.RecyclerViewHolder(view);
        }
        @Override
        public void onBindViewHolder(final RecyclerViewHolder holder, final int position)
        {
            final ContestModel account_model = orderList.get(position);
            holder.TV_Win_Amount.setText(account_model.getWiningprice());
            holder.TV_Entry_fee.setText(account_model.getEnteryfee()+"0");
            final String flag = account_model.getFlag();

            if (account_model.getGame_amt_type().equals("POINTS"))
            {
                holder.ImageViewBab.setImageResource(R.drawable.coin);
                holder.ImageViewcoin.setImageResource(R.drawable.coin);
            }

            if (flag.equals("InActive"))
            {
                holder.RL_partisipet.setVisibility(View.GONE);
                holder.progressBar.setVisibility(View.GONE);
                String payout_status = account_model.getPayout_status();
                if (payout_status.equals("UnPaid"))
                {
                    //amountDistribution(account_model.getSrno());
                }
            }
            else
            {
                int total_member = Integer.parseInt(account_model.getTotal_Memb());
                int join_member = Integer.parseInt(account_model.getTotal_joining());
                int total_member_by_per = 100/total_member;
                int remain_member = Integer.parseInt(account_model.getTotal_joining())*total_member_by_per;
                holder.progressBar.setProgress(remain_member);
                holder.TV_Member_Left.setText(""+(total_member-join_member));
                holder.TV_total_spot.setText(account_model.getTotal_Memb());
            }

            int sec  = Integer.parseInt(account_model.getTime_left());
            reverseTimer(sec,holder.TV_Hr,holder.TV_Min,holder.TV_Sec);
            holder.LinearLayout_Cotest.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    srno = account_model.getSrno();
                    // if (gtype.equals("1")&&flag.equals("Active"))
                    if (gtype.equals("1"))
                    {
                //        joinContest(userId,srno);.
                        Intent intent = new Intent(ContestListActivity.this,SingleContestDetailActivity.class);
                        intent.putExtra("gametype",gtype);
                        intent.putExtra("srno",srno);
                        startActivity(intent);
                    }
                    else if(gtype.equals("2"))
                    {
                        Intent intent = new Intent(ContestListActivity.this,SingleContestDetailActivity.class);
                        intent.putExtra("gametype",gtype);
                        intent.putExtra("srno",srno);
                        startActivity(intent);
                    }
                    else if(gtype.equals("3"))
                    {
                        Intent intent = new Intent(ContestListActivity.this,SingleContestDetailActivity.class);
                        intent.putExtra("gametype",gtype);
                        intent.putExtra("srno",srno);
                        startActivity(intent);
                    }
                    else if(gtype.equals("4"))
                    {
                        Intent intent = new Intent(ContestListActivity.this,SingleContestDetailActivity.class);
                        intent.putExtra("gametype",gtype);
                        intent.putExtra("srno",srno);
                        startActivity(intent);
                    }
                    else if(gtype.equals("5"))
                    {
                        Intent intent = new Intent(ContestListActivity.this,SingleContestDetailActivity.class);
                        intent.putExtra("gametype",gtype);
                        intent.putExtra("srno",srno);
                        startActivity(intent);
                    }
                    else if(gtype.equals("7"))
                    {
                        Intent intent = new Intent(ContestListActivity.this,SingleContestDetailActivity.class);
                        intent.putExtra("gametype",gtype);
                        intent.putExtra("srno",srno);
                        startActivity(intent);
                    }
                    else if(gtype.equals("8"))
                    {
                        Intent intent = new Intent(ContestListActivity.this,SingleContestDetailActivity.class);
                        intent.putExtra("gametype",gtype);
                        intent.putExtra("srno",srno);
                        startActivity(intent);
                    }
                    else if(gtype.equals("9"))
                    {
                        Intent intent = new Intent(ContestListActivity.this,SingleContestDetailActivity.class);
                        intent.putExtra("gametype",gtype);
                        intent.putExtra("srno",srno);
                        startActivity(intent);
                    }
                    else if(gtype.equals("11"))
                    {
                        Intent intent = new Intent(ContestListActivity.this,SingleContestDetailActivity.class);
                        intent.putExtra("gametype",gtype);
                        intent.putExtra("srno",srno);
                        startActivity(intent);
                    }
                    else if(gtype.equals("12"))
                    {
                        Intent intent = new Intent(ContestListActivity.this,SingleContestDetailActivity.class);
                        intent.putExtra("gametype",gtype);
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
                    if (account_model.getTotal_joining().equals("0"))
                    {
                        Toast.makeText(getApplicationContext(),"No one can join this contest",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Intent intent = new Intent(ContestListActivity.this,TopScoreActivity.class);
                        intent.putExtra("price",account_model.getWiningprice());
                        intent.putExtra("srno",account_model.getSrno());
                        startActivity(intent);
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

    public void topScorePerticulerContst(final String srno) {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        //  String url = Constant.URL+"addSignUp"; // <----enter your post url here
        String url = Constant.URL+"getHighestScoreByContest?ContestID="+srno;
        JsonArrayRequest MyStringRequest = new JsonArrayRequest(Request.Method.POST, url, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                try
                {
                    String tempscore="0",temprank="0",tempusername = "";
                    arrayList1.clear();
                    jsonArray = response;
                    for (int i = 0; i < response.length(); i++)
                    {
                        JSONObject jsonObject2 = response.getJSONObject(i);
                        if (i==0)
                        {
                            String score = jsonObject2.getString("score");
                            String username = jsonObject2.getString("username");
                            TopScoreModel model = new TopScoreModel();
                            temprank = "1";
                            tempscore = score;
                            tempusername = username;
                        }

                        else if (i==1)
                        {
                            String score = jsonObject2.getString("score");
                            String username = jsonObject2.getString("username");
                            TopScoreModel model = new TopScoreModel();
                            model.setRank("2");
                            model.setScore(score);
                            model.setUsername(username);
                            arrayList1.add(model);

                            TopScoreModel model1 = new TopScoreModel();
                            model1.setRank(temprank);
                            model1.setScore(tempscore);
                            model1.setUsername(tempusername);
                            arrayList1.add(model1);
                        }
                        else if(i==2)
                        {
                            String score = jsonObject2.getString("score");
                            String username = jsonObject2.getString("username");
                            TopScoreModel model = new TopScoreModel();
                            model.setRank("3");
                            model.setScore(score);
                            model.setUsername(username);
                            arrayList1.add(model);
                        }
                        else
                        {
                            jsonListTopFour();
                        }
                    }
                    TopThree_Contest_ListAdapter operator_adapter = new TopThree_Contest_ListAdapter(arrayList1,getApplicationContext());
                    RecyclerView_Top_Three_Contest.setAdapter(operator_adapter);

                  //  TopFour_Contest_List operator_adapter4 = new TopFour_Contest_List(arrayList1,getApplicationContext());
                  //  RecyclerView_Contest_Type1.setAdapter(operator_adapter4);
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
        MyStringRequest.setRetryPolicy(new DefaultRetryPolicy(200000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,   DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyRequestQueue.add(MyStringRequest);
    }

    public void jsonListTopFour() {
        ArrayList<TopScoreModel> arrayList2 =new ArrayList<>();
        CV_Contest.setVisibility(View.VISIBLE);
        try
        {
            for (int i = 0; i < jsonArray.length(); i++)
            {
                if (i>=3)
                {
                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                    String score = jsonObject2.getString("score");
                    String username = jsonObject2.getString("username");
                    TopScoreModel model = new TopScoreModel();
                    model.setScore(score);
                    model.setUsername(username);
                    model.setPrice(winning_amt);
                    arrayList2.add(model);
                }
            }
            TopFour_Contest_List operator_adapter4 = new TopFour_Contest_List(arrayList2,getApplicationContext());
            RecyclerView_Contest_Type1.setAdapter(operator_adapter4);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

}
