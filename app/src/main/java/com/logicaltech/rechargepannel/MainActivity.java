package com.logicaltech.rechargepannel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import util.Constant;
import util.SessionManeger;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    SliderLayout sliderLayout;
    HashMap<String, Integer> HashMapForLocalRes;
    LinearLayout LL_Mobile_Recharge,LL_Flight_Book,LL_refernce_social_media,LL_Game,LL_Total_Balance;
    Intent intent;
    SessionManeger sessionManeger;
    TextView TextViewUserName,TextViewUserEmail,TextViewTotalBalance,TextViewDirectIncome,TextViewRefernce;
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
        LL_refernce_social_media = (LinearLayout) findViewById(R.id.linear_layout_refernce);
        LL_Game = (LinearLayout) findViewById(R.id.linear_layout_game);
        TextViewTotalBalance = (TextView) findViewById(R.id.tv_total_income);
        TextViewDirectIncome = (TextView) findViewById(R.id.tv_directt_income);
        TextViewRefernce = (TextView) findViewById(R.id.tv_total_ref);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

        for(String name : HashMapForLocalRes.keySet())
        {
            TextSliderView textSliderView = new TextSliderView(MainActivity.this);
            textSliderView.description(name).image(HashMapForLocalRes.get(name)).setScaleType(BaseSliderView.ScaleType.Fit);
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("extra",name);
            sliderLayout.addSlider(textSliderView);
        }
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.FlipHorizontal);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(3000);
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
            startActivity(intent);
        }
        else if (id == R.id.nav_manage)
        {

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
        HashMapForLocalRes.put("Product", R.drawable.slider1);
        HashMapForLocalRes.put("Sale", R.drawable.slider2);
        HashMapForLocalRes.put("Vendor", R.drawable.slider3);
        HashMapForLocalRes.put("Online Shoping", R.drawable.slider4);
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

}
