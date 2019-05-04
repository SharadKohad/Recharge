package com.logicaltech.gamerecharge;

import android.app.AlertDialog;
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
import android.widget.LinearLayout;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adpter.GameAdapter;
import model.GameModel;
import util.Constant;
import util.SessionManeger;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    SliderLayout sliderLayout;
    HashMap<String, Integer> HashMapForLocalRes;
    LinearLayout LL_Mobile_Recharge,LL_Flight_Book,LL_refernce_social_media,LL_Game,LL_Total_Balance,LL_Refernce;/*,LL_FishGame_Home,LL_2048_Game,LL_Fist_Arrow,LL_Table_Tenic,LL_Catch_Dot,LL_Book;*/
    Intent intent;
    SessionManeger sessionManeger;
    TextView TextViewUserName,TextViewUserEmail,TextViewTotalBalance,TextViewDirectIncome,TextViewRefernce;
    RecyclerView recyclerView_Game_type;
    GridLayoutManager mGridLayoutManagerBrand;
    ArrayList<GameModel> arrayList =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sessionManeger = new SessionManeger(getApplicationContext());
        sliderLayout = (SliderLayout)findViewById(R.id.banner_slider1);
        LL_Mobile_Recharge = (LinearLayout) findViewById(R.id.linear_layout_mobile_recharge);
        LL_Flight_Book = (LinearLayout) findViewById(R.id.linear_layout_flight_book);
        LL_Total_Balance = (LinearLayout) findViewById(R.id.linear_layout_total_balance);
    //    LL_Book = (LinearLayout) findViewById(R.id.linear_layout_three_game);
        LL_refernce_social_media = (LinearLayout) findViewById(R.id.linear_layout_refernce);
        LL_Game = (LinearLayout) findViewById(R.id.linear_layout_game);
      //  LL_FishGame_Home = (LinearLayout) findViewById(R.id.linear_layout_jump_fish_home);
     //   LL_2048_Game = (LinearLayout) findViewById(R.id.linear_layout_2048);
    //    LL_Fist_Arrow = (LinearLayout) findViewById(R.id.linear_layout_jump_game_four);
     //   LL_Catch_Dot = (LinearLayout) findViewById(R.id.linear_layout_six);
        LL_Refernce = (LinearLayout) findViewById(R.id.linear_layout_refer_and_earn);
        TextViewTotalBalance = (TextView) findViewById(R.id.tv_total_income);
        TextViewDirectIncome = (TextView) findViewById(R.id.tv_directt_income);
        TextViewRefernce = (TextView) findViewById(R.id.tv_total_ref);
     //   LL_Table_Tenic = (LinearLayout) findViewById(R.id.linear_layout_game_five);
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

        HashMap<String, String> hashMap = sessionManeger.getUserDetails();
        String userName = hashMap.get(SessionManeger.KEY_NAME);
        String userEmail = hashMap.get(SessionManeger.KEY_EMAIL);
        String membercode = hashMap.get(SessionManeger.MEMBER_ID);

        TextViewUserName.setText(userName);
        TextViewUserEmail.setText(userEmail);

        AddImageUrlFormLocalRes();
        dashBoardData(membercode);
        contestList("1");
        gameList();

        for(String name : HashMapForLocalRes.keySet())
        {
            TextSliderView textSliderView = new TextSliderView(MainActivity.this);
            textSliderView.description(name).image(HashMapForLocalRes.get(name)).setScaleType(BaseSliderView.ScaleType.Fit);
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("extra",name);
            sliderLayout.addSlider(textSliderView);
        }
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(5000);
        //  sliderLayout.addOnPageChangeListener(MainActivity.this);

        LL_Mobile_Recharge.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                intent = new Intent(MainActivity.this,MobileRechargeActivity.class);
                startActivity(intent);
            }
        });

        LL_Total_Balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this,WalletHistoryActivity.class);
                startActivity(intent);
            }
        });

        LL_Game.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                intent = new Intent(MainActivity.this,GameZoneActivity.class);
                startActivity(intent);
            }
        });

        LL_Flight_Book.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                intent = new Intent(MainActivity.this,FlightBookActivity.class);
                startActivity(intent);
            }
        });

      /*  LL_Catch_Dot.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                intent = new Intent(MainActivity.this,ContestListActivity.class);
                intent.putExtra("gtype","3");
                startActivity(intent);
            }
        });

        LL_Fist_Arrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                intent = new Intent(MainActivity.this,ContestListActivity.class);
                intent.putExtra("gtype","4");
                startActivity(intent);
            }
        });
        LL_Book.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                intent = new Intent(MainActivity.this,WebView2048Activity.class);
                intent.putExtra("url","https://buy-instant-html5games.com/books-tower");
                intent.putExtra("srno","3");
                startActivity(intent);
            }
        });

        LL_Table_Tenic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                intent = new Intent(MainActivity.this,ContestListActivity.class);
                intent.putExtra("gtype","5");
                startActivity(intent);
            }
        });*/


        LL_Refernce.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "http://www.arenaitech.com/");
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out this site!");
                startActivity(Intent.createChooser(intent, "Share"));
            }
        });

/*
        LL_FishGame_Home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //  intent = new Intent(GameZoneActivity.this,JumpFishActivity.class);
                intent = new Intent(MainActivity.this,ContestListActivity.class);
                intent.putExtra("gtype","1");
                startActivity(intent);
            }
        });

        LL_2048_Game.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //  intent = new Intent(GameZoneActivity.this,JumpFishActivity.class);
                intent = new Intent(MainActivity.this,ContestListActivity.class);
                intent.putExtra("gtype","2");
                startActivity(intent);
            }
        });*/

        LL_refernce_social_media.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "http://www.arenaitech.com/");
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out this site!");
                startActivity(Intent.createChooser(intent, "Share"));
            }
        });
    }
    @Override
    public void onBackPressed()
    {
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
            startActivity(intent);
        }
        else if (id == R.id.nav_share)
        {
            intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "http://www.arenaitech.com/");
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out this site!");
            startActivity(Intent.createChooser(intent, "Share"));
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

    public void AddImageUrlFormLocalRes() {
        HashMapForLocalRes = new HashMap<String, Integer>();
        HashMapForLocalRes.put("Friengs Refernce", R.drawable.sharad2);
        HashMapForLocalRes.put("Game", R.drawable.slider2);
        HashMapForLocalRes.put("Point Earn", R.drawable.slider3);
        HashMapForLocalRes.put("paytm Cashback", R.drawable.slide4);
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
                    Constant.TOTAL_BALANCE = total_Balance;
                    Constant.DIRECT_INCOME = Total_Direct_Income;
                    Constant.TOTAL_REF = Total_Direct_Referral;
                    TextViewTotalBalance.setText(""+total_Balance);
                    TextViewDirectIncome.setText(Total_Direct_Income);
                    TextViewRefernce.setText(Total_Direct_Referral);
                    Toast.makeText(MainActivity.this,"DashBoard Successfull",Toast.LENGTH_SHORT).show();
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

    public void contestList(final String gametype) {
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
    }

    public void gameList()
    {
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

}
