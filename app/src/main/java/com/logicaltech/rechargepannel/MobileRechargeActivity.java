package com.logicaltech.rechargepannel;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MobileRechargeActivity extends AppCompatActivity
{
    TextView TV_Opertor;
    TextInputEditText TIET_Mobile_no,TIET_Amount;
    Button Btn_Recharge;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_recharge);
        init();
    }
    private void init() {
        TV_Opertor = (TextView) findViewById(R.id.TextView_dth_operator);
        TIET_Mobile_no = (TextInputEditText) findViewById(R.id.et_mobilenumber);
        TIET_Amount = (TextInputEditText) findViewById(R.id.textinputedittext_amount);
        Btn_Recharge = (Button) findViewById(R.id.btn_processrecharge);

        Btn_Recharge.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String operator = TV_Opertor.getText().toString();
                if (operator.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please select operator",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String mobileno = TIET_Mobile_no.getText().toString();
                    if (mobileno.equals(""))
                    {
                        Toast.makeText(getApplicationContext(),"Please enter mobile no",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String amount = TIET_Amount.getText().toString();
                        if (amount.equals(""))
                        {
                            Toast.makeText(getApplicationContext(),"Please enter amount",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {

                        }
                    }
                }
            }
        });
    }
}
