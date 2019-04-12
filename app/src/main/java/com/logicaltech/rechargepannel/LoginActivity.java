package com.logicaltech.rechargepannel;

import android.app.Dialog;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.format.Formatter;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import util.Constant;
import util.MySingalton;
import util.SessionManeger;

public class LoginActivity extends AppCompatActivity
{
    Button btn_signin;
    TextInputEditText TIET_user_name,TIET_password;
    TextView TV_Signup;
    ProgressBar progressBar;
    String user_name,password;
    SessionManeger sessionManeger;
    TextView TVforgotpassword;
    Dialog dialog;
    WindowManager.LayoutParams lp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sessionManeger = new SessionManeger(getApplicationContext());
        init();
    }

    private void init()
    {
        btn_signin = (Button) findViewById(R.id.btn_signin);
        TIET_user_name = (TextInputEditText) findViewById(R.id.et_user_name);
        TIET_password = (TextInputEditText) findViewById(R.id.et_user_password);
        TV_Signup = (TextView) findViewById(R.id.tv_sign_up);
        progressBar = (ProgressBar) findViewById(R.id.progress_barsignin);
        TVforgotpassword = (TextView) findViewById(R.id.forgotpassword);

        btn_signin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                signIn();
            }
        });

        TV_Signup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

        TVforgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
        });


    }

    public void signIn()
    {
        user_name = TIET_user_name.getText().toString();
        if (user_name.equals(""))
        {
            Toast.makeText(this,"Please enter valid email",Toast.LENGTH_SHORT).show();
        }
        else
        {
            password = TIET_password.getText().toString();
            if (password.equals(""))
            {
                Toast.makeText(this,"Please enter password",Toast.LENGTH_SHORT).show();
            }
            else
            {
                signInVolly(user_name,password);
                //   postOTP_Genrate(user_id);
               /* progressBar.setVisibility(View.VISIBLE);
                btn_signin.setAlpha(0f);
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        progressBar.setVisibility(View.GONE);
                        btn_signin.setAlpha(1f);
                    }
                }, 2000);*/
            }
        }
    }

    public void signInVolly(final String userId, final String Password)
    {
        progressBar.setVisibility(View.VISIBLE);
        btn_signin.setAlpha(0f);
        String url = Constant.URL+"getLogin";
        StringRequest jsonObjRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    progressBar.setVisibility(View.GONE);
                    btn_signin.setAlpha(1f);
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if (status.equals("1"))
                    {
                        String username = jsonObject.getString("username");
                        String email = jsonObject.getString("Email");
                        String mobileNo = jsonObject.getString("Mobile_No");
                        String userName = jsonObject.getString("Memb_Name");
                        String memberId = jsonObject.getString("membercode");
                        sessionManeger.createSession(username,userName,email,mobileNo,memberId);
                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this,""+msg,Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e)
                {
                    progressBar.setVisibility(View.GONE);
                    btn_signin.setAlpha(1f);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error)
            {
                progressBar.setVisibility(View.GONE);
                btn_signin.setAlpha(1f);
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
                params.put("username", userId);
                params.put("Password",Password);
                return params;
            }
        };
        MySingalton.getInstance(getApplicationContext()).addRequestQueue(jsonObjRequest);
        jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void showCustomDialog()
    {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.forgot_password);
        dialog.setCancelable(true);
        final TextInputEditText textInputEditTextEmail;

        lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        textInputEditTextEmail = (TextInputEditText) dialog.findViewById(R.id.tiet_password_forgot);

        ((AppCompatButton) dialog.findViewById(R.id.btn_forgot_password)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String forgotEmail = textInputEditTextEmail.getText().toString();
                if (forgotEmail.equals(""))
                {
                    Toast.makeText(LoginActivity.this,"Please enter valid email",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    forgotPassword(forgotEmail);
                }
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void forgotPassword(final String userId)
    {
        String url = Constant.URL+"forgotPassword";
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
                        Toast.makeText(LoginActivity.this," "+msg,Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this,""+msg,Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
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
                params.put("username", userId);
                return params;
            }
        };
        MySingalton.getInstance(getApplicationContext()).addRequestQueue(jsonObjRequest);
    }


}
