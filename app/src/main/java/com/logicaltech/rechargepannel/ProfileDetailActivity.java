package com.logicaltech.rechargepannel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.HashMap;

import util.SessionManeger;

public class ProfileDetailActivity extends AppCompatActivity
{
    SessionManeger sessionManeger;
    EditText ET_Name,ET_Email,ET_MobileNo,ET_MemberName;
    Button Btn_Profile_Save,Btn_Change_Password;
    private String userId,userMobile,userName,userEmail,userMemberName;
    ImageView IV_Back_Arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);
        sessionManeger = new SessionManeger(getApplicationContext());
        init();
    }

    public void init() {
        ET_Name = (EditText) findViewById(R.id.EditText_ProfileName);
        ET_Email = (EditText) findViewById(R.id.EditText_ProfileEmailId);
        ET_MobileNo = (EditText) findViewById(R.id.EditText_PhoneNumber);
        ET_MemberName = (EditText) findViewById(R.id.EditText_MemberName);
        Btn_Profile_Save = (Button) findViewById(R.id.button_profile_save);
        Btn_Change_Password = (Button) findViewById(R.id.button_change_password);
        IV_Back_Arrow = (ImageView) findViewById(R.id.img_back_arrow_profile_detail);

        HashMap<String, String> hashMap = sessionManeger.getUserDetails();
        userMemberName = hashMap.get(SessionManeger.KEY_ID);
        userMobile = hashMap.get(SessionManeger.KEY_PHONE);
        userName = hashMap.get(SessionManeger.KEY_NAME);
        userEmail = hashMap.get(SessionManeger.KEY_EMAIL);
        userId = hashMap.get(SessionManeger.MEMBER_ID);

        ET_Name.setText(userName);
        ET_Email.setText(userEmail);
        ET_MobileNo.setText(userMobile);
        ET_MemberName.setText(userMemberName);

        Btn_Profile_Save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
         //       putEditProfile(memberId,ET_Name.getText().toString(),gender,"India",ET_Email.getText().toString(),ET_City.getText().toString(),"Y",ET_Address.getText().toString(),ET_fatherName.getText().toString());
            }
        });

        IV_Back_Arrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        Btn_Change_Password.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               // Intent intent = new Intent(ProfileDetailActivity.this,ChangePasswordActivity.class);
               // startActivity(intent);
            }
        });
    }
}
