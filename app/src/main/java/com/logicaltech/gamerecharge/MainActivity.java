package com.logicaltech.gamerecharge;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import adpter.GameAdapter;
import de.hdodenhof.circleimageview.CircleImageView;
import model.GameModel;
import util.Constant;
import util.SessionManeger;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    SliderLayout sliderLayout;
    HashMap<String, String> HashMapForURL ;
    LinearLayout LL_Refernce,LL_Super_team;
    Intent intent;
    SessionManeger sessionManeger;
    TextView TextViewUserName,TextViewUserEmail,TextViewTotalBalance,TextViewDirectIncome,TextViewRefernce,TextViewTotalIncome,TextView_Total_Coin;
    CircleImageView ImgPhoto;
    RecyclerView recyclerView_Game_type;
    GridLayoutManager mGridLayoutManagerBrand;
    ArrayList<GameModel> arrayList =new ArrayList<>();
    String membercode,userName;
    RelativeLayout RL_Game_Info;
    Dialog dialog;
    WindowManager.LayoutParams lp;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sessionManeger = new SessionManeger(getApplicationContext());
        sliderLayout = (SliderLayout)findViewById(R.id.banner_slider1);

        TextViewTotalIncome = (TextView) findViewById(R.id.tv_total_income);
        TextView_Total_Coin = (TextView) findViewById(R.id.tv_total_coin);
        RL_Game_Info = (RelativeLayout) findViewById(R.id.rl_game_info);

        LL_Refernce = (LinearLayout) findViewById(R.id.linear_layout_refer_and_earn);
        LL_Super_team = (LinearLayout)findViewById(R.id.linear_layout_super_team);
        TextViewTotalBalance = (TextView) findViewById(R.id.tv_total_income);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        recyclerView_Game_type = (RecyclerView) findViewById(R.id.rv_gametype);
        mGridLayoutManagerBrand = new GridLayoutManager(MainActivity.this, 3);

        recyclerView_Game_type.setLayoutManager(mGridLayoutManagerBrand);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);
        Menu nav_Menu = navigationView.getMenu();

        TextViewUserName = (TextView)  hView.findViewById(R.id.tv_profile_name);
        TextViewUserEmail = (TextView)  hView.findViewById(R.id.tv_email_id);
        ImgPhoto = (CircleImageView) hView.findViewById(R.id.imgPhoto);

        HashMap<String, String> hashMap = sessionManeger.getUserDetails();
        userName = hashMap.get(SessionManeger.KEY_NAME);
        String userEmail = hashMap.get(SessionManeger.KEY_EMAIL);
        membercode = hashMap.get(SessionManeger.MEMBER_ID);

        TextViewUserName.setText(userName);
        TextViewUserEmail.setText(userEmail);

        String photo = hashMap.get(SessionManeger.KEY_PHOTO);
        if (photo.equals(""))
        {

        }
        else
        {
            Picasso.with(getApplicationContext()).load(photo).into(ImgPhoto);
        }
        getBanner();
        dashBoardData(membercode);
        //contestList("1");
        gameList();

        RL_Game_Info.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showBounceCash();
            }
        });

        TextViewTotalBalance.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                intent = new Intent(MainActivity.this,WalletHistoryActivity.class);
                startActivity(intent);
            }
        });

        TextViewTotalIncome.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                intent = new Intent(MainActivity.this,AccountActivity.class);
                startActivity(intent);
            }
        });

        LL_Super_team.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                intent = new Intent(MainActivity.this,UpcomeingMatchesActivity.class);
                intent.putExtra("gtype","10");
                startActivity(intent);
            }
        });

        TextView_Total_Coin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this,ContextParticipetionActivity.class);
                intent.putExtra("token","1");
                startActivity(intent);
            }
        });

        LL_Refernce.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                /*intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "http://www.arenaitech.com/");
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out this site!");
                startActivity(Intent.createChooser(intent, "Share"));*/
                intent = new Intent(MainActivity.this,ShareAppActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_game_zone)
        {
            intent = new Intent(MainActivity.this,GameZoneActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_account)
        {
            intent = new Intent(MainActivity.this,AccountActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_participetion_list)
        {
            intent = new Intent(MainActivity.this,ContextParticipetionActivity.class);
            intent.putExtra("token","0");
            startActivity(intent);
        }
        else if (id == R.id.nav_share)
        {
            intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "Here get 50 Tokens And 50 Rs to play with me to Elite Play. Click the link "+"http://www.arenaitech.com/"+ " to download the App and use my invite code "+userName+ " to register.");
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out this site!");
            startActivity(Intent.createChooser(intent, "Share"));
            intent = new Intent(MainActivity.this,ShareAppActivity.class);
            startActivity(intent);
            /*intent = new Intent(MainActivity.this,Battle_ResultActivity.class);
            startActivity(intent);*/
        }
        else if (id == R.id.nav_logout)
        {
            new AlertDialog.Builder(MainActivity.this)
                    .setMessage("Are you sure you want to logout?").setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            sessionManeger.logoutUser();
                            finish();
                        }
                    }).setNegativeButton("No", null).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void dashBoardData(final String memberId) {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = Constant.URL+"getDashboard?membercode="+memberId;
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    int total_Balance = jsonObject.getInt("Total_Balance");
                    String Total_Direct_Income = jsonObject.getString("Total_Direct_Income");
                    String Total_Direct_Referral = jsonObject.getString("Total_Direct_Referral");
                    String spinnerdate = jsonObject.getString("last_spin_Dt");
                    int Bonus_Cash = jsonObject.getInt("Bonus_Cash");
                    int Bonus_Point = jsonObject.getInt("Bonus_Point");
                    if (spinnerdate.equals(""))
                    {
                        spinnerdate = "14/05/2019";
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Date currentDate = new Date();
                    Date endDate = null;
                    endDate=    sdf.parse(spinnerdate);
                    long duration  = currentDate.getTime() - endDate.getTime();
                    long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
                    System.out.println("hr: "+diffInHours);
                    if (diffInHours<24)
                    {

                    }
                    else
                    {
                        intent = new Intent(MainActivity.this,SpinnerWebActivity.class);
                        startActivity(intent);
                    }
                    Constant.TOTAL_BALANCE = total_Balance+Bonus_Cash;
                    Constant.TOTAL_DEPOSIT_CASH = total_Balance;
                    Constant.DIRECT_INCOME = Total_Direct_Income;
                    Constant.TOTAL_REF = Total_Direct_Referral;
                    Constant.BOUNCE_CASH = Bonus_Cash;
                    Constant.TOTAL_COIN = Bonus_Point;
                    TextView_Total_Coin.setText(""+Bonus_Point);
                    TextViewTotalBalance.setText(""+(total_Balance+Bonus_Cash));
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                } catch (ParseException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
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

    /*public void contestList(final String gametype) {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        //  String url = Constant.URL+"addSignUp"; // <----enter your post url here
        String url = Constant.URL+"getGameSettingByType?Type="+gametype+"&ContestID=&Status=";
        JsonArrayRequest MyStringRequest = new JsonArrayRequest(Request.Method.POST, url, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                try
                {
                    for (int i = 0; i < response.length(); i++)
                    {
                        JSONObject jsonObject2 = response.getJSONObject(i);
                        String flag = jsonObject2.getString("flag");
                        if (flag.equals("InActive"))
                        {
                            String payout_status = jsonObject2.getString("payout_status");
                            if (payout_status.equals("UnPaid"))
                            {
                                String srno = jsonObject2.getString("srno");
                                System.out.println("My Context"+i+ " "+srno);
                                //amountDistribution(srno);
                            }
                        }
                    }
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
    }*/

    public void gameList() {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        //  String url = Constant.URL+"addSignUp"; // <----enter your post url here
        final String url = Constant.URL+"getGameType";
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
                        String srno = jsonObject2.getString("srno");
                        String game_name = jsonObject2.getString("game_name");
                        String flag = jsonObject2.getString("flag");
                        String logo = jsonObject2.getString("logo");
                        GameModel gameModel = new GameModel();
                        gameModel.setSrno(srno);
                        gameModel.setGame_type(game_name);
                        gameModel.setFlag(flag);
                        gameModel.setLogo("http://site17.bidbch.com/"+logo);
                        arrayList.add(gameModel);
                    }
                    GameAdapter gameAdapter = new GameAdapter(arrayList,getApplicationContext());
                    recyclerView_Game_type.setAdapter(gameAdapter);
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

    public void getBanner() {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        final String url = Constant.URL+"getBannerImg";
        JsonArrayRequest MyStringRequest = new JsonArrayRequest(Request.Method.POST, url, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                try
                {
                    HashMapForURL = new HashMap<String, String>();
                    for (int i = 0; i < response.length(); i++)
                    {
                        JSONObject jsonObject2 = response.getJSONObject(i);
                        String BID = jsonObject2.getString("BID");
                        String banner_img = jsonObject2.getString("banner_img");
                        String flag = jsonObject2.getString("flag");
                        HashMapForURL.put(BID, banner_img);
                    }
                    for(String name : HashMapForURL.keySet())
                    {
                        TextSliderView textSliderView = new TextSliderView(MainActivity.this);
                        textSliderView.description(name).image(HashMapForURL.get(name)).setScaleType(BaseSliderView.ScaleType.Fit);
                        textSliderView.bundle(new Bundle());
                        textSliderView.getBundle().putString("extra",name);
                        sliderLayout.addSlider(textSliderView);
                    }
                    sliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
                    sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                    sliderLayout.setCustomAnimation(new DescriptionAnimation());
                    sliderLayout.setDuration(5000);
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

   /* public void amountDistribution(final String Srno) {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = Constant.URL+"addFinalAmtByContest";
        StringRequest jsonObjRequest = new StringRequest(Request.Method.PUT,url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("msg");
                    if (status.equals("1"))
                    {
                        Toast.makeText(MainActivity.this,"point Distribution: "+message,Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this," "+message,Toast.LENGTH_SHORT).show();
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
                params.put("srno",Srno);
                return params;
            }
        };
        MySingalton.getInstance(getApplicationContext()).addRequestQueue(jsonObjRequest);
        jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(200000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyRequestQueue.add(jsonObjRequest);
    }
*/
    private void showBounceCash() {
        dialog = new Dialog(this);
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
    }
}
