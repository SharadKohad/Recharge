package com.logicaltech.rechargepannel;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

public class ChangePasswordActivity extends AppCompatActivity
{
    ImageView IV_Back_Arrow;
    Button btn_save_password;
    TextInputEditText ET_Old_Password,ET_NewPassword,ET_ConformPaaword;
    SessionManeger sessionManeger;
    String memberId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        sessionManeger =new SessionManeger(getApplicationContext());
        HashMap<String, String> hashMap = sessionManeger.getUserDetails();
        memberId = hashMap.get(SessionManeger.MEMBER_ID);

        IV_Back_Arrow = (ImageView) findViewById(R.id.img_back_arrow_change_password);
        btn_save_password = (Button) findViewById(R.id.button_forgotpassword_save);
        ET_Old_Password = (TextInputEditText) findViewById(R.id.edit_text_old_password);
        ET_NewPassword = (TextInputEditText) findViewById(R.id.edit_text_new_password);
        ET_ConformPaaword = (TextInputEditText) findViewById(R.id.edit_text_new_password_conform);

        IV_Back_Arrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        btn_save_password.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                changePassword();
            }
        });
    }

    public void changePassword()
    {
        String oldPassword = ET_Old_Password.getText().toString();
        if (oldPassword.equals(""))
        {
            Toast.makeText(ChangePasswordActivity.this,"Please enter the Old Password",Toast.LENGTH_SHORT).show();
        }
        else
        {
            String newPassword = ET_NewPassword.getText().toString();
            if (newPassword.equals(""))
            {
                Toast.makeText(ChangePasswordActivity.this,"Please enter the New Password",Toast.LENGTH_SHORT).show();
            }
            else
            {
                String conformPassword = ET_ConformPaaword.getText().toString();
                if (conformPassword.equals(""))
                {
                    Toast.makeText(ChangePasswordActivity.this,"Password don,t match",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (newPassword.equals(conformPassword))
                    {
                        changePassword(memberId,oldPassword,newPassword);
                    }
                    else
                    {
                        Toast.makeText(ChangePasswordActivity.this,"Password don,t match",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    public void changePassword(final String memberId,final String oldPassword,final String newPawwrod)
    {
        String url = Constant.URL+"changePassword";
        StringRequest jsonObjRequest = new StringRequest(Request.Method.PUT,url, new Response.Listener<String>()
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
                        Toast.makeText(ChangePasswordActivity.this," "+msg,Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        Toast.makeText(ChangePasswordActivity.this," "+msg,Toast.LENGTH_SHORT).show();
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
                params.put("membercode", memberId);
                params.put("OldPass",oldPassword);
                params.put("NewPass", newPawwrod);
                return params;
            }
        };
        MySingalton.getInstance(getApplicationContext()).addRequestQueue(jsonObjRequest);
    }
}
