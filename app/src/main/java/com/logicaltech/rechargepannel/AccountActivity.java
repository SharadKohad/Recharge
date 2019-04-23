package com.logicaltech.rechargepannel;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.paytm.pgsdk.PaytmPGService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import util.Constant;
import util.MySingalton;
import util.SessionManeger;

public class AccountActivity extends AppCompatActivity
{
    Button Btn_logout;
    SessionManeger sessionManeger;
    ImageView Img_back_arrow;
    LinearLayout LL_ProfileDetail,LL_Paytm_Getway;
    Dialog dialog;
    WindowManager.LayoutParams lp;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        sessionManeger = new SessionManeger(getApplicationContext());
        init();
    }

    public void init()
    {
        Btn_logout = (Button) findViewById(R.id.button_logout);
        Img_back_arrow = (ImageView) findViewById(R.id.img_back_arrow_profile);
        LL_ProfileDetail = (LinearLayout) findViewById(R.id.linear_layout_view_profile);
        LL_Paytm_Getway = (LinearLayout) findViewById(R.id.linear_layout_Account_list);
        Img_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        LL_ProfileDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountActivity.this,ProfileDetailActivity.class);
                startActivity(intent);
            }
        });

        LL_Paytm_Getway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showCustomDialog();

            }
        });

        Btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(AccountActivity.this)
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
        });

        paytmpermition();
    }

    public void paytmpermition()
    {
        if (ContextCompat.checkSelfPermission(AccountActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AccountActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
        }
    }

    private void showCustomDialog()
    {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_paytm_layout);
        dialog.setCancelable(true);
        final TextView TV_Total_Amount;
        final  ImageView img_close;
        final EditText ET_Add_Amount;

        lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        TV_Total_Amount = (TextView) dialog.findViewById(R.id.tv_total_amount);
        img_close = (ImageView) dialog.findViewById(R.id.img_tournament_close);
        ET_Add_Amount = (EditText) dialog.findViewById(R.id.et_add_paytmamount);
        TV_Total_Amount.setText(""+ Constant.TOTAL_BALANCE);

        img_close.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });
       /* submitPaytmAmount("TXN_SUCCESS","cW3/7W4kipLxClguib9RPYXiP8HaGBgHLoOnA0JOOp8Bv28FhQQ/vaIfB8E+rc3Kp/egwLUY1Esn9ZxX+frI4fLvDMfBsPIx8tgX5Dr7QTU=","ANDB","ORDER_@2772588","50","2019-04-23 16:01:50.0","QvgGNh07814118282130","201904221112128001101654553004319078","01","NB","40905","INR","Txn Success","ANDB");*/

        ((Button) dialog.findViewById(R.id.btn_add_paytm)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String paytm_amount = ET_Add_Amount.getText().toString();
                if (paytm_amount.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please Enter Amount",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent(AccountActivity.this, ChecksumActivity.class);
                    intent.putExtra("paytmamount", paytm_amount);
                    startActivity(intent);
                }
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


   /* public void submitPaytmAmount(final String status, final String checksum, final String bankname, final String orderid, final String transacamt, final String txtdate, final String mid, final String txtid, final String respcode, final String pmode, final String bid, final String currency, final String rmsg,final String gname)
    {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = Constant.URL+"addFundByPaytmResp?Membercode=2568477";
        StringRequest jsonObjRequest = new StringRequest(Request.Method.PUT,url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("mystatus");
                    String message = jsonObject.getString("msg");
                    if (status.equals("1"))
                    {
                        Toast.makeText(AccountActivity.this,""+message,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AccountActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(AccountActivity.this,""+message,Toast.LENGTH_SHORT).show();
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
                params.put("STATUS", status);
                params.put("CHECKSUMHASH",checksum);
                params.put("BANKNAME", bankname);
                params.put("ORDERID",orderid);
                params.put("TXNAMOUNT", transacamt);
                params.put("TXNDATE",txtdate);
                params.put("MID", mid);
                params.put("TXNID",txtid);
                params.put("RESPCODE", respcode);
                params.put("PAYMENTMODE",pmode);
                params.put("BANKTXNID", bid);
                params.put("CURRENCY",currency);
                params.put("GATEWAYNAME", gname);
                params.put("RESPMSG",rmsg);
                return params;
            }
        };
        MySingalton.getInstance(getApplicationContext()).addRequestQueue(jsonObjRequest);
        jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(200000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyRequestQueue.add(jsonObjRequest);
    }*/

}
