package com.logicaltech.gamerecharge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import util.Constant;
import util.MySingalton;

public class PlayerInfoActivity extends AppCompatActivity
{
    TextView TV_P_Name,TV_Country;
    CircleImageView CIV_Player;
    TextView TV_OM,TV_OInn,TV_ORuns,TV_Ohs,TV_Oavs,TV_Osr,TV_O100,TV_O50;
    TextView TV_TM,TV_TInn,TV_TRuns,TV_Ths,TV_Tavs,TV_Tsr,TV_T100,TV_T50;
    TextView TV_TtM,TV_TtInn,TV_TtRuns,TV_Tths,TV_Ttavs,TV_Ttsr,TV_Tt100,TV_Tt50;

    TextView TV_OtMb,TV_OtBBi,TV_OtRunsb,TV_OtWkts,TV_Oteconimy,TV_Otavg,TV_Ot5w;
    TextView TV_TtMb,TV_TtBBi,TV_TtRunsb,TV_TtWkts,TV_Tteconimy,TV_Ttavg,TV_Tt5w;
    TextView TV_TMb,TV_TBBi,TV_TRunsb,TV_TWkts,TV_Teconimy,TV_Tavg,TV_T5w;

    ImageView IV_Back_Arrow;
    ProgressBar progressBar;
    String player_unique_id;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_info);
        init();
        player_unique_id = getIntent().getExtras().getString("pid");

        getPlayerDetail(player_unique_id);
    }

    public void init() {
        progressBar = (ProgressBar) findViewById(R.id.progrebar_player_detail);
        TV_P_Name = (TextView) findViewById(R.id.textview_player_name);
        TV_Country = (TextView) findViewById(R.id.textview_country);
        CIV_Player = (CircleImageView) findViewById(R.id.imageView_player_image);
        IV_Back_Arrow = (ImageView) findViewById(R.id.img_back_player_info);
        TV_TM = (TextView) findViewById(R.id.tmatchs);
        TV_TInn = (TextView) findViewById(R.id.tinn);
        TV_TRuns = (TextView) findViewById(R.id.truns);
        TV_Ths = (TextView) findViewById(R.id.ths);
        TV_Tavs = (TextView) findViewById(R.id.tavs);
        TV_Tsr = (TextView) findViewById(R.id.tsr);
        TV_T100 = (TextView) findViewById(R.id.t100);
        TV_T50 = (TextView) findViewById(R.id.t50);

        TV_OM = (TextView) findViewById(R.id.omatches);
        TV_OInn = (TextView) findViewById(R.id.oinn);
        TV_ORuns = (TextView) findViewById(R.id.oruns);
        TV_Ohs = (TextView) findViewById(R.id.ohs);
        TV_Oavs = (TextView) findViewById(R.id.oavg);
        TV_Osr = (TextView) findViewById(R.id.osr);
        TV_O100 = (TextView) findViewById(R.id.o100);
        TV_O50 = (TextView) findViewById(R.id.o50);

        TV_TtM = (TextView) findViewById(R.id.textview_tmatches);
        TV_TtInn = (TextView) findViewById(R.id.textview_tinn);
        TV_TtRuns = (TextView) findViewById(R.id.textview_truns);
        TV_Tths = (TextView) findViewById(R.id.textview_ths);
        TV_Ttavs = (TextView) findViewById(R.id.textview_tavs);
        TV_Ttsr = (TextView) findViewById(R.id.textview_tsr);
        TV_Tt100 = (TextView) findViewById(R.id.textview_t100);
        TV_Tt50 = (TextView) findViewById(R.id.textview_t50);


        TV_TtMb = (TextView) findViewById(R.id.btmatchs);
        TV_TtBBi = (TextView) findViewById(R.id.btbbi);
        TV_TtRunsb = (TextView) findViewById(R.id.btruns);
        TV_TtWkts = (TextView) findViewById(R.id.bthswkts);
        TV_Tteconimy = (TextView) findViewById(R.id.bteconimy);
        TV_Ttavg = (TextView) findViewById(R.id.btavg);
        TV_Tt5w = (TextView) findViewById(R.id.bt5wt);


        TV_OtMb = (TextView) findViewById(R.id.bomatches);
        TV_OtBBi = (TextView) findViewById(R.id.bobbi);
        TV_OtRunsb = (TextView) findViewById(R.id.boruns);
        TV_OtWkts = (TextView) findViewById(R.id.bowks);
        TV_Oteconimy = (TextView) findViewById(R.id.boeconimy);
        TV_Otavg = (TextView) findViewById(R.id.boavg);
        TV_Ot5w = (TextView) findViewById(R.id.bo5w);


        TV_TMb = (TextView) findViewById(R.id.btextview_tmatches);
        TV_TBBi = (TextView) findViewById(R.id.btextview_bbi);
        TV_TRunsb = (TextView) findViewById(R.id.btextview_truns);
        TV_TWkts = (TextView) findViewById(R.id.btextview_wks);
        TV_Teconimy = (TextView) findViewById(R.id.btextview_teconomi);
        TV_Tavg = (TextView) findViewById(R.id.btextview_tavg);
        TV_T5w = (TextView) findViewById(R.id.btextview_t5wks);


        IV_Back_Arrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    public void getPlayerDetail(final String pid)
    {
        progressBar.setVisibility(View.VISIBLE);
        final String URL = "http://cricapi.com/api/playerStats?pid="+pid;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("apikey", Constant.APIKEY);
        JsonObjectRequest request_json = new JsonObjectRequest(URL, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    progressBar.setVisibility(View.GONE);
                    //Process os success response
                    String name  = response.getString("name");
                    TV_P_Name.setText(""+name);
                    TV_Country.setText(""+response.getString("country"));
                    Picasso.with(getApplicationContext()).load(response.getString("imageURL")).into(CIV_Player);
                    JSONObject jsonObjectdata = response.getJSONObject("data");
                    JSONObject jsonObjectbowing = jsonObjectdata.getJSONObject("bowling");

                    JSONObject jsonObjecttest = jsonObjectbowing.getJSONObject("T20Is");
                    TV_TtMb.setText(""+jsonObjecttest.getString("Mat"));
                    TV_TtBBi.setText(""+jsonObjecttest.getString("BBI"));
                    TV_TtRunsb.setText(""+jsonObjecttest.getString("Runs"));
                    TV_TtWkts.setText(""+jsonObjecttest.getString("Wkts"));
                    TV_Tteconimy.setText(""+jsonObjecttest.getString("Econ"));
                    TV_Ttavg.setText(""+jsonObjecttest.getString("Ave"));
                    TV_Tt5w.setText(""+jsonObjecttest.getString("5w"));

                    JSONObject jsonObjectoday = jsonObjectbowing.getJSONObject("ODIs");
                    TV_OtMb.setText(""+jsonObjectoday.getString("Mat"));
                    TV_OtBBi.setText(""+jsonObjectoday.getString("BBI"));
                    TV_OtRunsb.setText(""+jsonObjectoday.getString("Runs"));
                    TV_OtWkts.setText(""+jsonObjectoday.getString("Wkts"));
                    TV_Oteconimy.setText(""+jsonObjectoday.getString("Econ"));
                    TV_Otavg.setText(""+jsonObjectoday.getString("Ave"));
                    TV_Ot5w.setText(""+jsonObjectoday.getString("5w"));

                    JSONObject tests = jsonObjectbowing.getJSONObject("tests");
                    TV_TMb.setText(""+tests.getString("Mat"));
                    TV_TBBi.setText(""+tests.getString("BBI"));
                    TV_TRunsb.setText(""+tests.getString("Runs"));
                    TV_TWkts.setText(""+tests.getString("Wkts"));
                    TV_Teconimy.setText(""+tests.getString("Econ"));
                    TV_Tavg.setText(""+tests.getString("Ave"));
                    TV_T5w.setText(""+tests.getString("5w"));


                    JSONObject jsonObjectbatting = jsonObjectdata.getJSONObject("batting");
                    JSONObject jsonObjectt20 = jsonObjectbatting.getJSONObject("T20Is");
                    TV_TM.setText(""+jsonObjectt20.getString("Mat"));
                    TV_TInn.setText(""+jsonObjectt20.getString("Inns"));
                    TV_TRuns.setText(""+jsonObjectt20.getString("Runs"));
                    TV_Ths.setText(""+jsonObjectt20.getString("HS"));
                    TV_Tavs.setText(""+jsonObjectt20.getString("Ave"));
                    TV_Tsr.setText(""+jsonObjectt20.getString("SR"));
                    TV_T100.setText(""+jsonObjectt20.getString("100"));
                    TV_T50.setText(""+jsonObjectt20.getString("50"));

                    JSONObject jsonObjectodi = jsonObjectbatting.getJSONObject("ODIs");
                    TV_OM.setText(""+jsonObjectodi.getString("Mat"));
                    TV_OInn.setText(""+jsonObjectodi.getString("Inns"));
                    TV_ORuns.setText(""+jsonObjectodi.getString("Runs"));
                    TV_Ohs.setText(""+jsonObjectodi.getString("HS"));
                    TV_Oavs.setText(""+jsonObjectodi.getString("Ave"));
                    TV_Osr.setText(""+jsonObjectodi.getString("SR"));
                    TV_O100.setText(""+jsonObjectodi.getString("100"));
                    TV_O50.setText(""+jsonObjectodi.getString("50"));


                    JSONObject jsonObjecttestbown = jsonObjectbatting.getJSONObject("tests");
                    TV_TtM.setText(""+jsonObjecttestbown.getString("Mat"));
                    TV_TtInn.setText(""+jsonObjecttestbown.getString("Inns"));
                    TV_TtRuns.setText(""+jsonObjecttestbown.getString("Runs"));
                    TV_Tths.setText(""+jsonObjecttestbown.getString("HS"));
                    TV_Ttavs.setText(""+jsonObjecttestbown.getString("Ave"));
                    TV_Ttsr.setText(""+jsonObjecttestbown.getString("SR"));
                    TV_Tt100.setText(""+jsonObjecttestbown.getString("100"));
                    TV_Tt50.setText(""+jsonObjecttestbown.getString("50"));

                }
                catch (JSONException e)
                {
                    progressBar.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        MySingalton.getInstance(getApplicationContext()).addRequestQueue(request_json);
    }
}
