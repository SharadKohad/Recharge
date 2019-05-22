package com.logicaltech.gamerecharge;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import util.Constant;
import util.SessionManeger;

public class AccountActivity extends AppCompatActivity
{
    Button Btn_logout;
    SessionManeger sessionManeger;
    ImageView Img_back_arrow;
    RelativeLayout LL_Point_History;
    LinearLayout LL_ProfileDetail,LL_Paytm_Getway,LL_Main_Bal_TermAndCondition,LL_Deposit_Cash,LL_Bounce_cash,LL_Share,LL_Add_Paytm,LL_Trancation_History;
    Dialog dialog;
    WindowManager.LayoutParams lp;
    TextView TV_Coin,TV_Total_Cash,TV_Bounce_Cash,TV_Deposit_Cash;
    String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        sessionManeger = new SessionManeger(getApplicationContext());
        HashMap<String, String> hashMap = sessionManeger.getUserDetails();
        userName = hashMap.get(SessionManeger.KEY_NAME);
        init();
    }

    public void init() {
        Btn_logout = (Button) findViewById(R.id.button_logout);
        Img_back_arrow = (ImageView) findViewById(R.id.img_back_arrow_profile);
        LL_ProfileDetail = (LinearLayout) findViewById(R.id.linear_layout_view_profile);
        LL_Paytm_Getway = (LinearLayout) findViewById(R.id.linear_layout_Account_list);
        LL_Point_History = (RelativeLayout) findViewById(R.id.linear_layout_point_history);
        LL_Main_Bal_TermAndCondition = (LinearLayout) findViewById(R.id.ll_main_balance);
        LL_Deposit_Cash = (LinearLayout) findViewById(R.id.ll_deposit);
        LL_Bounce_cash = (LinearLayout) findViewById(R.id.ll_bounce_cash);
        LL_Share = (LinearLayout) findViewById(R.id.ll_earn_more);
        TV_Coin = (TextView) findViewById(R.id.textview_coin);
        TV_Total_Cash = (TextView) findViewById(R.id.tv_total_Cash);
        TV_Bounce_Cash = (TextView) findViewById(R.id.tv_bounce_cash);
        TV_Deposit_Cash = (TextView) findViewById(R.id.textview_deposit_cash);
        LL_Add_Paytm = (LinearLayout) findViewById(R.id.ll_add_money_paytm);
        LL_Trancation_History = (LinearLayout) findViewById(R.id.ll_total_amount_transaction_history);
        TV_Total_Cash.setText(""+Constant.TOTAL_BALANCE);
        TV_Bounce_Cash.setText(""+Constant.BOUNCE_CASH);
        TV_Deposit_Cash.setText(""+Constant.TOTAL_DEPOSIT_CASH);

        Img_back_arrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        LL_ProfileDetail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AccountActivity.this,ProfileDetailActivity.class);
                startActivity(intent);
            }
        });

        LL_Point_History.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AccountActivity.this,ContextParticipetionActivity.class);
                intent.putExtra("token","1");
                startActivity(intent);
            }
        });

        LL_Trancation_History.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AccountActivity.this,ContextParticipetionActivity.class);
                intent.putExtra("token","2");
                startActivity(intent);
            }
        });

        LL_Main_Bal_TermAndCondition.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showTermAndCoundition();
            }
        });

        LL_Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "Here get 100 Tokens to play with me to GOC. Click the link "+"http://www.arenaitech.com/"+ " to download the App and use my invite code "+userName+ " to register.");
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out this site!");
                startActivity(Intent.createChooser(intent, "Share"));
            }
        });

        LL_Deposit_Cash.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showDepositCash();
            }
        });

        LL_Bounce_cash.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showBounceCash();
            }
        });

        LL_Paytm_Getway.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
               showCustomDialog();
            }
        });

        LL_Add_Paytm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showCustomDialog();
            }
        });

        Btn_logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
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

        TV_Coin.setText(""+Constant.TOTAL_COIN);
        paytmpermition();
    }

    public void paytmpermition() {
        if (ContextCompat.checkSelfPermission(AccountActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(AccountActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
        }
    }

    private void showCustomDialog() {
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

    private void showTermAndCoundition() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_termandcondition);
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

    private void showDepositCash() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_deposit_cash);
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
