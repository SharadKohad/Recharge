package com.logicaltech.gamerecharge;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import util.Constant;
import util.MySingalton;
import util.SessionManeger;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class SpinnerWebActivity extends AppCompatActivity
{
    WebView mywebview;
    String current_url =  "http://logicalsolutiontech.com/wheel/",coin,membercode;
    SessionManeger sessionManeger;
    Dialog dialog;
    WindowManager.LayoutParams lp;
    TextView TV_Income;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner_web);
        sessionManeger = new SessionManeger(getApplicationContext());
        TV_Income = (TextView) findViewById(R.id.tv_total_income_spinner);
        TV_Income.setText(""+Constant.TOTAL_BALANCE);
        HashMap<String, String> hashMap = sessionManeger.getUserDetails();
        membercode = hashMap.get(SessionManeger.MEMBER_ID);
        mywebview  = new WebView(this);
        mywebview.getSettings().setJavaScriptEnabled(true); // enable javascript
        mywebview .loadUrl(current_url);
        setContentView(mywebview);
        mywebview.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                coin= url.replaceAll("[^0-9]", "");
                addPoint(membercode,coin,"POINTS");
                showCustomDialog();
                return super.shouldOverrideUrlLoading(view,url);
            }
        });
    }
    public void addPoint(final String memberCode, final String point, final String type) {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = Constant.URL+"addSpinnerAmt";
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
                        Toast.makeText(SpinnerWebActivity.this," "+message, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(SpinnerWebActivity.this," "+message, Toast.LENGTH_SHORT).show();
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
                params.put("membercode", memberCode);
                params.put("amount",point);
                params.put("amount_type",type);
                return params;
            }
        };
        MySingalton.getInstance(getApplicationContext()).addRequestQueue(jsonObjRequest);

      /*  jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MyRequestQueue.add(jsonObjRequest);*/
        jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void showCustomDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_spin_add);
        dialog.setCancelable(true);
        final TextView TV_Total_coin,TV_Message;
        final Button btn;

        lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        TV_Total_coin = (TextView) dialog.findViewById(R.id.tv_recied_coin);
        TV_Message  = (TextView) dialog.findViewById(R.id.txtmessage);

        btn = (Button) dialog.findViewById(R.id.btn_coin_add);

        if (coin.equals("0"))
        {
            TV_Total_coin.setText("0");
            TV_Message.setText("BETTER LUCK NEXT TIME");
        }
        else
        {
            TV_Total_coin.setText(""+coin+" "+"Point");
        }

        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
               // addPoint(membercode,coin);
                 Intent intent = new Intent(SpinnerWebActivity.this,MainActivity.class);
                 startActivity(intent);
                 finish();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
}
