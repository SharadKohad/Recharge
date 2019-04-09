package com.logicaltech.rechargepannel;

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

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    SliderLayout sliderLayout;
    HashMap<String, Integer> HashMapForLocalRes ;
    LinearLayout LL_Mobile_Recharge,LL_Flight_Book,LL_refernce_social_media;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sliderLayout = (SliderLayout)findViewById(R.id.banner_slider1);
        LL_Mobile_Recharge = (LinearLayout) findViewById(R.id.linear_layout_mobile_recharge);
        LL_Flight_Book = (LinearLayout) findViewById(R.id.linear_layout_flight_book);
        LL_refernce_social_media = (LinearLayout) findViewById(R.id.linear_layout_refernce);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        AddImageUrlFormLocalRes();

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

        LL_Mobile_Recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                intent = new Intent(MainActivity.this,MobileRechargeActivity.class);
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

        LL_refernce_social_media.setOnClickListener(new View.OnClickListener() {
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

        if (id == R.id.nav_camera)
        {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

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
}
