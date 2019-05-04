package com.logicaltech.gamerecharge;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

public class SignUpActivity extends AppCompatActivity
{
    TextInputEditText TIET_MemberName,TIET_name,TIET_email_id,TIET_mobileNo,TIET_sponsorId,TIET_password;
    Button Btn_SignUp;
    CheckBox checkBox_SponserId;
    ProgressBar progressBar;
    ImageView Img_SignUp_Close;
    String fname,lname,email,token="0";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
        token = getIntent().getExtras().getString("token");

        if (token.equals("1"))
        {
            fname = getIntent().getExtras().getString("first_name");
            lname = getIntent().getExtras().getString("last_name");
            email = getIntent().getExtras().getString("email");

            TIET_MemberName.setText(fname+" "+lname);
            TIET_email_id.setText(""+email);
            TIET_name.setText(""+fname);
        }
    }

    public void init()
    {
        TIET_MemberName = (TextInputEditText) findViewById(R.id.et_member_name);
        TIET_name =(TextInputEditText)findViewById(R.id.et_user_name);
        TIET_email_id = (TextInputEditText)findViewById(R.id.et_email_id);
        TIET_mobileNo = (TextInputEditText)findViewById(R.id.et_mobile_no);
        TIET_sponsorId = (TextInputEditText) findViewById(R.id.et_sponser_id);
        TIET_password = (TextInputEditText) findViewById(R.id.et_user_password);
        Img_SignUp_Close = (ImageView) findViewById(R.id.img_signup_close);
        Btn_SignUp = (Button) findViewById(R.id.btn_signup);
        checkBox_SponserId = (CheckBox) findViewById(R.id.checkboxspoinerId);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar_signup);
        checkBox_SponserId.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    TIET_sponsorId.setText("TEST@123");
                }
                else
                {
                    TIET_sponsorId.setText("");
                }
            }
        });

        Img_SignUp_Close.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        Btn_SignUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                signUp();
            }
        });
    }

    public void signUp()
    {
        String memberName = TIET_MemberName.getText().toString();
        if (memberName.equals(""))
        {
            Toast.makeText(this,"Please enter Member Name",Toast.LENGTH_SHORT).show();
        }
        else
        {
            String userName = TIET_name.getText().toString();
            if (userName.equals(""))
            {
                Toast.makeText(this,"Please enter User Name",Toast.LENGTH_SHORT).show();
            }
            else
            {
                String email_id = TIET_email_id.getText().toString();
                if (!Constant.isValidEmail(email_id))
                {
                    Toast.makeText(this,"Please enter valid email",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String mobileNo = TIET_mobileNo.getText().toString();
                    if (mobileNo.equals("")|| mobileNo.length()<10)
                    {
                        Toast.makeText(this,"Please enter valid Mobile Number",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String sponsorId = TIET_sponsorId.getText().toString();
                        if (sponsorId.equals(""))
                        {
                            Toast.makeText(this,"please Enter Sponsor Id",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String password = TIET_password.getText().toString();
                            if (password.equals(""))
                            {
                                Toast.makeText(this,"please Enter Password",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                progressBar.setVisibility(View.VISIBLE);
                                Btn_SignUp.setAlpha(0f);
                                new Handler().postDelayed(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        progressBar.setVisibility(View.GONE);
                                        Btn_SignUp.setAlpha(1f);
                                    }
                                }, 2000);

                                 registration1(mobileNo,email_id,memberName,userName,sponsorId,password,"255.255.255.0");
                            }
                        }
                    }
                }
            }
        }
    }

    public void registration1(final String Mobileno, final String EmailId,final String membername,final String username, final String sponserid,final String password, final String ip_address) {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = Constant.URL+"addRegistration";
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
                        Toast.makeText(SignUpActivity.this," "+message,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(SignUpActivity.this," "+message,Toast.LENGTH_SHORT).show();
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
                params.put("Mobile_No", Mobileno);
                params.put("Email",EmailId);
                params.put("Memb_Name", membername);
                params.put("username",username);
                params.put("sp_user", sponserid);
                params.put("mpwd", password);
                params.put("client_ip", ip_address);
                return params;
            }
        };
        MySingalton.getInstance(getApplicationContext()).addRequestQueue(jsonObjRequest);
        jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(200000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyRequestQueue.add(jsonObjRequest);
    }
}
