package com.logicaltech.gamerecharge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
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
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import util.Constant;
import util.MySingalton;
import util.SessionManeger;

public class ChecksumActivity extends AppCompatActivity implements PaytmPaymentTransactionCallback
{
    String custid="", orderId="", mid="",CHECKSUMHASH = "",varifyurl="",amount;
    String memberId,token="0";
    SessionManeger sessionManeger;
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
     //   setContentView(R.layout.activity_checksum);
        sessionManeger = new SessionManeger(getApplicationContext());
        HashMap<String, String> hashMap = sessionManeger.getUserDetails();
        memberId = hashMap.get(SessionManeger.MEMBER_ID);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        amount = getIntent().getExtras().getString("paytmamount");
     //   custid = intent.getExtras().getString("custid");
      //  mid = "QvgGNh07814118282130"; /// your marchant key
    //    ChecksumActivity.sendUserDetailTOServerdd dl = new ChecksumActivity.sendUserDetailTOServerdd();
    //    dl.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        Random random = new Random(System.nanoTime());
        int randomInt = random.nextInt(1000000000);
        orderId = "ORDER@"+Integer.toString(randomInt)+memberId;
        addMoney(amount,"7777777777","usertemail@provider.com",orderId,memberId);
    }
    public void addMoney(final String amt, final String mobile_no,final String email,final String unique,final String memberId) {
        String url = Constant.URL+"addPaytmMoney";
        StringRequest jsonObjRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if (status.equals("1"))
                    {
                        mid = jsonObject.getString("MID");
                        orderId = jsonObject.getString("ORDER_ID");
                        custid= jsonObject.getString("CUST_ID");
                        CHECKSUMHASH = jsonObject.getString("CHECKSUMHASH");
                        varifyurl= jsonObject.getString("CALLBACK_URL");
                        amount = jsonObject.getString("TXN_AMOUNT");
                        Toast.makeText(ChecksumActivity.this,""+msg,Toast.LENGTH_SHORT).show();
                        PaytmPGService Service = PaytmPGService.getStagingService();
                        // when app is ready to publish use production service
                        // PaytmPGService  Service = PaytmPGService.getProductionService();
                        // now call paytm service here
                        //below parameter map is required to construct PaytmOrder object, Merchant should replace below map values with his own values
                        HashMap<String, String> paramMap = new HashMap<String, String>();
                        //these are mandatory parameters
                        paramMap.put("MID", mid); //MID provided by paytm
                        paramMap.put("ORDER_ID", orderId);
                        paramMap.put("CUST_ID", custid);
                        paramMap.put("CHANNEL_ID", "WAP");
                        paramMap.put("TXN_AMOUNT", amount);
                        paramMap.put("WEBSITE", "WEBSTAGING");
                        paramMap.put("CALLBACK_URL" ,varifyurl);
                        paramMap.put( "EMAIL" , "usertemail@provider.com");   // no need
                        paramMap.put( "MOBILE_NO" , "7777777777");  // no need
                        paramMap.put("CHECKSUMHASH" , CHECKSUMHASH);
                        //paramMap.put("PAYMENT_TYPE_ID" ,"CC");    // no need
                        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
                        PaytmOrder Order = new PaytmOrder(paramMap);
                        Log.e("checksum ", "param "+ paramMap.toString());
                        Service.initialize(Order,null);
                        // start payment service call here
                        Service.startPaymentTransaction(ChecksumActivity.this, true, true, ChecksumActivity.this  );
                    }
                    else
                    {
                        Toast.makeText(ChecksumActivity.this,""+msg,Toast.LENGTH_SHORT).show();
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
                VolleyLog.d("volley", "Error: " + error.getMessage());
                error.printStackTrace();
            }
        })
        {
            @Override
            public String getBodyContentType()
            {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("TXN_AMOUNT", amt);
                params.put("MOBILE_NO",mobile_no);
                params.put("EMAIL",email);
                params.put("ORDER_ID",unique);
                params.put("CUST_ID",memberId);
                return params;
            }
        };
        MySingalton.getInstance(getApplicationContext()).addRequestQueue(jsonObjRequest);
        jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    /*public class sendUserDetailTOServerdd extends AsyncTask<ArrayList<String>, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(ChecksumActivity.this);
        //private String orderId , mid, custid, amt;
        String url ="https://www.blueappsoftware.com/payment/payment_paytm/generateChecksum.php";
      //  String varifyurl = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";
      //  String varifyurl = "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID="+orderId;
     //   String varifyurl = "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID="+orderId+"&MID="+mid;
        String CHECKSUMHASH ="";
        String varifyurl;
        @Override
        protected void onPreExecute()
        {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }
        protected String doInBackground(ArrayList<String>... alldata)
        {
            JSONParser jsonParser = new JSONParser(ChecksumActivity.this);
            String param= "MID="+mid+ "&ORDER_ID=" + orderId+ "&CUST_ID="+custid+ "&CHANNEL_ID=WAP&TXN_AMOUNT=100&WEBSITE=WEBSTAGING"+
                            "&CALLBACK_URL="+ varifyurl+"&INDUSTRY_TYPE_ID=Retail";
            JSONObject jsonObject = jsonParser.makeHttpRequest(url,"POST",param);
            // yaha per checksum ke saht order id or status receive hoga..
            Log.e("CheckSum result >>",jsonObject.toString());
            if(jsonObject != null){
                Log.e("CheckSum result >>",jsonObject.toString());
                try {
                    CHECKSUMHASH=jsonObject.has("CHECKSUMHASH")?jsonObject.getString("CHECKSUMHASH"):"";
                    Log.e("CheckSum result >>",CHECKSUMHASH);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return CHECKSUMHASH;
        }
        @Override
        protected void onPostExecute(String result) {
            Log.e(" setup acc ","  signup result  " + result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            PaytmPGService Service = PaytmPGService.getStagingService();
            // when app is ready to publish use production service
            // PaytmPGService  Service = PaytmPGService.getProductionService();
            // now call paytm service here
            //below parameter map is required to construct PaytmOrder object, Merchant should replace below map values with his own values
            HashMap<String, String> paramMap = new HashMap<String, String>();
            //these are mandatory parameters
            paramMap.put("MID", mid); //MID provided by paytm
            paramMap.put("ORDER_ID", orderId);
            paramMap.put("CUST_ID", custid);
            paramMap.put("CHANNEL_ID", "WEB");
            paramMap.put("TXN_AMOUNT", "100");
            paramMap.put("WEBSITE", "WEBSTAGING");
            paramMap.put("CALLBACK_URL" ,varifyurl);
            paramMap.put( "EMAIL" , "username@emailprovider.com");   // no need
            paramMap.put( "MOBILE_NO" , "7777777777");  // no need
            paramMap.put("CHECKSUMHASH" ,
                    "U12xeu7+U6gRdNlMNrZ850oIddvaFbuPTFQYGuea5NRxQuhJlFZ6zIlLVQPL5OqqFPzVekkDUK75Hmdfa8oJeD0rGcwTtiVZCJ9/ebjRFTw=");
            //paramMap.put("PAYMENT_TYPE_ID" ,"CC");    // no need
            paramMap.put("INDUSTRY_TYPE_ID", "Retail");
            PaytmOrder Order = new PaytmOrder(paramMap);
            Log.e("checksum ", "param "+ paramMap.toString());
            Service.initialize(Order,null);
            // start payment service call here
            Service.startPaymentTransaction(ChecksumActivity.this, true, true,
                    ChecksumActivity.this  );
        }
    }*/

    @Override
    public void onTransactionResponse(Bundle bundle)
    {
            String TXNID = "";
            String STATUS = bundle.getString("STATUS");
            String CHECKSUMHASH = bundle.getString("CHECKSUMHASH");
            String BANKNAME = bundle.getString("BANKNAME");
            String ORDERID = bundle.getString("ORDERID");
            String TXNAMOUNT = bundle.getString("TXNAMOUNT");
            String TXNDATE = bundle.getString("TXNDATE");
            String MID = bundle.getString("MID");
            TXNID = bundle.getString("TXNID");
            String RESPCODE = bundle.getString("RESPCODE");
            String PAYMENTMODE = bundle.getString("PAYMENTMODE");
            String BANKTXNID = bundle.getString("BANKTXNID");
            String CURRENCY = bundle.getString("CURRENCY");
            String GATEWAYNAME = bundle.getString("GATEWAYNAME");
            String RESPMSG = bundle.getString("RESPMSG");
            if (TXNID.equals(""))
            {

            }
            else
            {
                submitPaytmAmount(STATUS,CHECKSUMHASH,BANKNAME,ORDERID,TXNAMOUNT,TXNDATE,MID,TXNID,RESPCODE,PAYMENTMODE,BANKTXNID,CURRENCY,RESPMSG,GATEWAYNAME);
            }
    }
    @Override
    public void networkNotAvailable()
    {

    }
    @Override
    public void clientAuthenticationFailed(String s)
    {

    }
    @Override
    public void someUIErrorOccurred(String s) {
        Log.e("checksum ", " ui fail respon  "+ s );
    }
    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        Log.e("checksum ", " error loading pagerespon true "+ s + "  s1 " + s1);
    }
    @Override
    public void onBackPressedCancelTransaction() {
        Log.e("checksum ", " cancel call back respon  " );
    }
    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        Log.e("checksum ", "  transaction cancel " );
    }

    public void submitPaytmAmount(final String status, final String checksum, final String bankname, final String orderid, final String transacamt, final String txtdate, final String mid, final String txtid, final String respcode, final String pmode, final String bid, final String currency, final String rmsg,final String gname)
    {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = Constant.URL+"addFundByPaytmResp?Membercode="+memberId;
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
                        Toast.makeText(ChecksumActivity.this,""+message,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ChecksumActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(ChecksumActivity.this,""+message,Toast.LENGTH_SHORT).show();
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
        jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyRequestQueue.add(jsonObjRequest);
    }
}
