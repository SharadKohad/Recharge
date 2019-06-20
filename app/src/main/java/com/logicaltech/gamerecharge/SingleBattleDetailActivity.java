package com.logicaltech.gamerecharge;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
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

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import util.Constant;
import util.MySingalton;
import util.SessionManeger;

public class SingleBattleDetailActivity extends AppCompatActivity
{
    ImageView Img_Back_Arrow;
    ProgressBar progressBar;
    private Context context;
    private String game_amt_type,srno,gametype,userId,userName,p1_Image;
    private int join_contest_amt;
    private TextView TV_Remaing_Time,TV_Game_Name,TV_Price,TV_Total_Player;
    private Button Btn_play;
    Dialog dialog,dialogBattle;
    WindowManager.LayoutParams lp;
    SessionManeger sessionManeger;
    private String p2_Name,p2_Image,ded_mainbal_amt,ded_boncash_amt;
    private RelativeLayout RL_Video_Game,RL_Game_List;
    int findplayerFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_battle_detail);

        init();
    }

    private void init() {
        context = this;
        sessionManeger = new SessionManeger(getApplicationContext());
        HashMap<String, String> hashMap = sessionManeger.getUserDetails();
        userId = hashMap.get(SessionManeger.MEMBER_ID);
        userName = hashMap.get(SessionManeger.KEY_NAME);
        p1_Image = hashMap.get(SessionManeger.KEY_PHOTO);
        Img_Back_Arrow = (ImageView) findViewById(R.id.img_back_arrow_battle_detail);
        progressBar = (ProgressBar) findViewById(R.id.progrebar_single_contest);
        TV_Remaing_Time = (TextView) findViewById(R.id.tv_remaing_time_contest_detail);
        TV_Game_Name = (TextView) findViewById(R.id.tv_cotest_name);
        TV_Price = (TextView) findViewById(R.id.tv_wining_price);
        TV_Total_Player  = (TextView) findViewById(R.id.tv_total_player);
        Btn_play = (Button) findViewById(R.id.rl_play_game);
        RL_Game_List = findViewById(R.id.rl_all_game);
        RL_Video_Game = findViewById(R.id.rl_all_how_to_play);
        srno = getIntent().getExtras().getString("srno");
        gametype = getIntent().getExtras().getString("gametype");
        battleList(srno);
        singleContestDetail(gametype,srno,"1");
        clicklisner();
    }

    public void clicklisner() {
        Img_Back_Arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (game_amt_type.equals("POINTS"))
                {
                    showTokenDialogBox();
                }
                else
                {
                    showCustomDialog();
                }
            }
        });

        RL_Game_List.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SingleBattleDetailActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        RL_Video_Game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SingleBattleDetailActivity.this,GameVideoActivity.class);
                intent.putExtra("gametype",gametype);
                startActivity(intent);
            }
        });
    }

    public void singleContestDetail(final String gametype,final String srno,final String status) {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        //  String url = Constant.URL+"addSignUp"; // <----enter your post url here
        String url = Constant.URL+"getBattleSettingByType?Type="+gametype+"&BattleID="+srno+"&Status="+status;
        JsonArrayRequest MyStringRequest = new JsonArrayRequest(Request.Method.POST, url, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                try
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    for (int i = 0; i < response.length(); i++)
                    {
                        JSONObject jsonObject2 = response.getJSONObject(i);
                        String winning_amt = jsonObject2.getString("winning_amt");
                        game_amt_type = jsonObject2.getString("game_amt_type");
                        join_contest_amt = jsonObject2.getInt("entry_amt");
                        Btn_play.setText("PLAY WITH "+join_contest_amt);
                       // String flag = jsonObject2.getString("flag");
                      //  String ttime = jsonObject2.getString("ttime");
                        String time_left = jsonObject2.getString("time_left");
                        String game_name = jsonObject2.getString("game_name");
                        String total_joining = jsonObject2.getString("total_joining");
                        ded_mainbal_amt = jsonObject2.getString("ded_mainbal_amt");
                        ded_boncash_amt = jsonObject2.getString("ded_boncash_amt");
                        TV_Game_Name.setText(""+game_name);
                        TV_Price.setText( winning_amt);
                      //  TV_Dis_Price.setText("\u20B9 "+winning_amt);

                        if (total_joining.equals("0"))
                        {
                            TV_Total_Player.setText("1");
                        }
                        else
                        {
                            TV_Total_Player.setText(""+total_joining);
                        }

                       /* if (game_amt_type.equals("RUPEES"))
                        {
                            TV_Bonus_Cash.setText("\u20B9 "+ded_boncash_amt+" Use in bounes cash");
                        }
                        else
                        {
                            LL_Bonus_Cash.setVisibility(View.GONE);
                        }*/
                        //    join_contest_amt = Integer.parseInt(entry_amt);
                        int sec  = Integer.parseInt(time_left);
                        reverseTimer(sec,TV_Remaing_Time);
                    }
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
                tv.setText(String.format("%02d", hours) + "H " + String.format("%02d", minutes) + "M " + String.format("%02d", seconds)+"S");
            }
            public void onFinish()
            {
                tv.setText("Completed");
            }
        }.start();
    }

    private void showTokenDialogBox() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_token_contest_conform);
        dialog.setCancelable(true);
        final  TextView TV_Total_Token,TV_Cotest_Token,TV_Remaing_Amount;
        final  ImageView img_close;
        final Button btn;
        String tokenmy="0";

        lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        TV_Total_Token = (TextView) dialog.findViewById(R.id.tv_total_amount);
        TV_Cotest_Token = (TextView) dialog.findViewById(R.id.tv_contest_amount);
        TV_Remaing_Amount = (TextView) dialog.findViewById(R.id.tv_remaining_left_amount);
        img_close = (ImageView) dialog.findViewById(R.id.img_tournament_close);
        btn = (Button) dialog.findViewById(R.id.btn_join_contest);

        TV_Total_Token.setText(""+Constant.TOTAL_COIN);
        TV_Cotest_Token.setText(""+join_contest_amt);

        int RemaingAmt = Constant.TOTAL_COIN - join_contest_amt;

        TV_Remaing_Amount.setText(""+RemaingAmt);
        if (Constant.TOTAL_COIN<join_contest_amt)
        {
            Toast.makeText(SingleBattleDetailActivity.this,"insufficient Balance,Please add fund through paytm",Toast.LENGTH_SHORT).show();
            btn.setText("ADD FUND");
            tokenmy = "1";
        }

        img_close.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });
        final String finalTokenmy = tokenmy;
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (finalTokenmy.equals("1"))
                {
                    Intent intent = new Intent(SingleBattleDetailActivity.this,AccountActivity.class);
                    startActivity(intent);
                }
                else
                {
                    if (findplayerFlag==1)
                    {
                        showProgress(context);
                    }
                    else
                    {
                        joinBattle(userId,srno);
                    }
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void joinBattle(final String MemberCode, final String Srno) {
        //progressBar.setVisibility(View.VISIBLE);
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = Constant.URL+"addToBattle";
        StringRequest jsonObjRequest = new StringRequest(Request.Method.PUT,url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    String  pstatus = jsonObject.getString("status");
                    String  msg = jsonObject.getString("msg");
                    if (pstatus.equals("1"))
                    {
                        showProgress(context);
                    }
                    else
                    {
                        Toast.makeText(SingleBattleDetailActivity.this,msg+"",Toast.LENGTH_SHORT).show();
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
                params.put("membercode", MemberCode);
                params.put("srno",Srno);
                return params;
            }
        };
        MySingalton.getInstance(getApplicationContext()).addRequestQueue(jsonObjRequest);
        jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void showBattleDetail() {
        Handler handler;
        dialogBattle = new Dialog(this);
        dialogBattle.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialogBattle.setContentView(R.layout.dialog_battle_headetoheade);
        dialogBattle.setCancelable(true);
        final  TextView TV_p1_Name,TV_p2_Name,TV_P1_Point,TV_P2_Point;
        final CircleImageView img_p1,img_p2;
        final RelativeLayout rlPlayer1,rlPlayer2;
        final Animation left,right;

        lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogBattle.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        img_p1 = (CircleImageView) dialogBattle.findViewById(R.id.img_player1);
        img_p2 = (CircleImageView) dialogBattle.findViewById(R.id.img_player2);

        TV_p1_Name = (TextView) dialogBattle.findViewById(R.id.txt_player1name);
        TV_p2_Name = (TextView) dialogBattle.findViewById(R.id.txt_player2name);
        TV_P1_Point = (TextView) dialogBattle.findViewById(R.id.txt_playerpoint1);
        TV_P2_Point = (TextView) dialogBattle.findViewById(R.id.txt_playerpoint2);

        rlPlayer1 = (RelativeLayout) dialogBattle.findViewById(R.id.rl_player1);
        rlPlayer2 = (RelativeLayout) dialogBattle.findViewById(R.id.rl_player2);


        //showProgress(context);
        TV_p1_Name.setText(""+userName);
        TV_p2_Name.setText(""+p2_Name);

        TV_P1_Point.setText(""+join_contest_amt);
        TV_P2_Point.setText(""+join_contest_amt);

        if (p1_Image.equals(""))
        {
            Picasso.with(context).load(R.drawable.icon_profile).into(img_p1);
        }
        else
        {
            Picasso.with(context).load(p1_Image).into(img_p1);
        }
        Picasso.with(context).load(p2_Image).into(img_p2);

        dialogBattle.show();
        dialogBattle.getWindow().setAttributes(lp);

        left = AnimationUtils.loadAnimation(this,R.anim.left);
        right = AnimationUtils.loadAnimation(this,R.anim.right);
        rlPlayer1.setAnimation(left);
        rlPlayer2.setAnimation(right);

        handler=new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if (gametype.equals("1"))
                {
                    Intent intent = new Intent(SingleBattleDetailActivity.this,JumpFishActivity.class);
                    intent.putExtra("srno",srno);
                    intent.putExtra("gtype","1");
                    intent.putExtra("cob","1");
                    startActivity(intent);
                }
                 else if (gametype.equals("2"))
                        {
                            Intent intent = new Intent(SingleBattleDetailActivity.this,WebView2048Activity.class);
                            intent.putExtra("url","http://site0.bidbch.com/games/2048/index.html");
                            intent.putExtra("gtype",gametype);
                            intent.putExtra("srno",srno);
                            intent.putExtra("cob","1");
                            startActivity(intent);
                        }
                        else if(gametype.equals("3"))
                        {
                            Intent intent = new Intent(SingleBattleDetailActivity.this,WebView2048Activity.class);
                            intent.putExtra("url","http://site0.bidbch.com/games/catchdots/index.html");
                            intent.putExtra("gtype",gametype);
                            intent.putExtra("srno",srno);
                            intent.putExtra("cob","1");
                            startActivity(intent);
                        }
                       else if(gametype.equals("4"))
                        {
                            Intent intent = new Intent(SingleBattleDetailActivity.this,WebView2048Activity.class);
                            intent.putExtra("url","http://site0.bidbch.com/games/fastarrow/index.html");
                            intent.putExtra("gtype",gametype);
                            intent.putExtra("srno",srno);
                            intent.putExtra("cob","1");
                            startActivity(intent);
                        }
                         else if(gametype.equals("5"))
                        {
                            Intent intent = new Intent(SingleBattleDetailActivity.this,WebView2048Activity.class);
                            intent.putExtra("url","http://site0.bidbch.com/games/pingpong/index.html");
                            intent.putExtra("gtype",gametype);
                            intent.putExtra("srno",srno);
                            intent.putExtra("cob","1");
                            startActivity(intent);
                        }
                        else if(gametype.equals("7"))
                        {
                            Intent intent = new Intent(SingleBattleDetailActivity.this,WebView2048Activity.class);
                            intent.putExtra("url","http://site0.bidbch.com/games/DotsPong/index.html");
                            intent.putExtra("gtype",gametype);
                            intent.putExtra("srno",srno);
                            intent.putExtra("cob","1");
                            startActivity(intent);
                        }

                        else if(gametype.equals("8"))
                        {
                            Intent intent = new Intent(SingleBattleDetailActivity.this,WebView2048Activity.class);
                            intent.putExtra("url","http://site0.bidbch.com/games/DotsAttack/index.html");
                            intent.putExtra("gtype",gametype);
                            intent.putExtra("srno",srno);
                            intent.putExtra("cob","1");
                            startActivity(intent);
                        }
                        else if(gametype.equals("9"))
                        {
                            Intent intent = new Intent(SingleBattleDetailActivity.this,WebView2048Activity.class);
                            intent.putExtra("url","http://site0.bidbch.com/games/JumpNinjaHero/index.html");
                            intent.putExtra("gtype",gametype);
                            intent.putExtra("srno",srno);
                            intent.putExtra("cob","1");
                            startActivity(intent);
                        }
                        else if(gametype.equals("11"))
                        {
                            Intent intent = new Intent(SingleBattleDetailActivity.this,WebView2048Activity.class);
                            intent.putExtra("url","http://site0.bidbch.com/games/ShotPong/index.html");
                            intent.putExtra("gtype",gametype);
                            intent.putExtra("srno",srno);
                            intent.putExtra("cob","1");
                            startActivity(intent);
                        }
                        else if(gametype.equals("12"))
                        {
                            Intent intent = new Intent(SingleBattleDetailActivity.this,WebView2048Activity.class);
                            intent.putExtra("url","http://site0.bidbch.com/games/FlyingTriangle/index.html");
                            intent.putExtra("gtype",gametype);
                            intent.putExtra("srno",srno);
                            intent.putExtra("cob","1");
                            startActivity(intent);
                        }
                        else if(gametype.equals("13"))
                        {
                            Intent intent = new Intent(SingleBattleDetailActivity.this,WebView2048Activity.class);
                            intent.putExtra("url","http://site0.bidbch.com/games/CrazyChicks/index.html");
                            intent.putExtra("gtype",gametype);
                            intent.putExtra("srno",srno);
                            intent.putExtra("cob","1");
                            startActivity(intent);
                        }
                        else if(gametype.equals("15"))
                        {
                            Intent intent = new Intent(context,WebView2048Activity.class);
                            intent.putExtra("url","http://site0.bidbch.com/games/penaltykick/index.html");
                            intent.putExtra("gtype",gametype);
                            intent.putExtra("srno",srno);
                            intent.putExtra("cob","1");
                            startActivity(intent);
                        }

            }
        },5000);
        dialogBattle.show();
    }

    public void battleList(final String bId ) {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        //  String url = Constant.URL+"addSignUp"; // <----enter your post url here
        String url = Constant.URL+"getBattlePlayerDetails?BattleID="+bId;
        JsonArrayRequest MyStringRequest = new JsonArrayRequest(Request.Method.POST, url, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                try
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    Constant.jsonArrayBattleList = response;
                    String res = response.toString();
                    if (res.equals("[]"))
                    {
                        battleList("0");
                    }
                    else
                    {
                        for (int i = 0; i < response.length(); i++)
                        {
                            JSONObject jsonObject2 = response.getJSONObject(i);
                            String p2_id = jsonObject2.getString("membercode");

                            if (response.length()==1&&userId.equals(p2_id))
                            {
                            //    Toast.makeText(context,"Other user Not Found",Toast.LENGTH_SHORT).show();
                              //  Btn_play.setEnabled(false);
                                findplayerFlag = 1;
                                break;
                            }
                            if (userId.equals(p2_id))
                            {
                                if (i==2)
                                {
                                  //  Toast.makeText(context,"Other user Not Found",Toast.LENGTH_SHORT).show();
                               //     Btn_play.setEnabled(false);
                                    findplayerFlag=1;
                                    break;
                                }
                            }
                            else
                            {
                                p2_Name =jsonObject2.getString("memb_name");
                                p2_Image= jsonObject2.getString("userFile");
                                System.out.println("P2 Name "+p2_Name);
                                break;
                            }
                        }
                    }
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

    public void showProgress(Context view) {
        final int THREE_SECONDS = 1*5000;
        final ProgressDialog dlg = new ProgressDialog(this);
        dlg.setMessage("Find Players...");
        dlg.setCancelable(false);
        dlg.show();
        new Handler().postDelayed(new Runnable()
        {
            public void run()
            {
                if (findplayerFlag==1)
                {
                    dlg.dismiss();
                    Toast.makeText(context,"Other user Not Found",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    dlg.dismiss();
                    showBattleDetail();
                }
            }
        }, THREE_SECONDS);
    }

    private void showCustomDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_contest_conform);
        dialog.setCancelable(true);
        final  TextView TV_Total_Amount,TV_Cotest_Amount,TV_Bounce_Cash,TV_Main_Wallet;
        final  ImageView img_close;
        final Button btn;
        final RelativeLayout RL_info;
        String tokenmy="0";

        lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        TV_Total_Amount = (TextView) dialog.findViewById(R.id.tv_total_amount);
        TV_Cotest_Amount = (TextView) dialog.findViewById(R.id.tv_contest_amount);
        TV_Bounce_Cash = (TextView) dialog.findViewById(R.id.tv_use_bounce_cash);
        TV_Main_Wallet = (TextView) dialog.findViewById(R.id.tv_deposit_and_cash);
        RL_info = (RelativeLayout) dialog.findViewById(R.id.rl_bounce_cash);
        img_close = (ImageView) dialog.findViewById(R.id.img_tournament_close);
        btn = (Button) dialog.findViewById(R.id.btn_join_contest);

        TV_Total_Amount.setText(""+Constant.TOTAL_BALANCE);
        TV_Cotest_Amount.setText(""+join_contest_amt);
        TV_Bounce_Cash.setText(""+ded_boncash_amt);
        TV_Main_Wallet.setText(""+ded_mainbal_amt);

        RL_info.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showBounceCash();
            }
        });
        if (Constant.TOTAL_BALANCE<join_contest_amt)
        {
            Toast.makeText(SingleBattleDetailActivity.this,"insufficient balance ",Toast.LENGTH_SHORT).show();
            btn.setText("ADD FUND");
            tokenmy = "1";
        }
        else
        {
            if (Constant.TOTAL_DEPOSIT_CASH<Float.parseFloat(ded_mainbal_amt))
            {
                Toast.makeText(SingleBattleDetailActivity.this,"insufficient Main balance ",Toast.LENGTH_SHORT).show();
                btn.setText("ADD FUND");
                tokenmy = "1";
            }
        }

        img_close.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });
        final String finalTokenmy = tokenmy;
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (finalTokenmy.equals("1"))
                {
                    Intent intent = new Intent(SingleBattleDetailActivity.this,AccountActivity.class);
                    startActivity(intent);
                }
                else
                {
                    if (findplayerFlag==1)
                    {
                        showProgress(context);
                    }
                    else
                    {
                        joinBattle(userId,srno);
                    }
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void showBounceCash() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_bounce_cash);
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
    }
}
