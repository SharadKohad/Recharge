package com.logicaltech.gamerecharge;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import util.Constant;
import util.SessionManeger;

public class ShareAppActivity extends AppCompatActivity
{
    private Context context;
    private ImageView IV_Back_Arrow;
    RelativeLayout RL_share_whatsapp;
    String userName;
    SessionManeger sessionManeger;
    TextView TV_Ref_Code,TV_Coin,TV_Total_Balance;
    LinearLayout LL_whatsapp,LL_Invite_Contact;
    CircleImageView CimgView;
    ImageView img_copyText;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_app);
        init();
    }

    private void init() {
        sessionManeger = new SessionManeger(getApplicationContext());
        HashMap<String, String> hashMap = sessionManeger.getUserDetails();
        //   HashMap<String, String> hashMap1 = sessionManeger.getFlyFishHighScore();
        //userId = hashMap.get(SessionManeger.MEMBER_ID);
        userName = hashMap.get(SessionManeger.KEY_NAME);

        context = this;
        IV_Back_Arrow = findViewById(R.id.img_back_arrow_share_app);
        RL_share_whatsapp = findViewById(R.id.rl_share_whatsapp);
        TV_Ref_Code = findViewById(R.id.txt_refercode);
        TV_Coin = findViewById(R.id.tv_total_coin);
        TV_Total_Balance = findViewById(R.id.tv_total_income);
        img_copyText = findViewById(R.id.img_copytext);
        LL_whatsapp = findViewById(R.id.ll_whatupp);
        CimgView = findViewById(R.id.imgprofile_share);
        LL_Invite_Contact = findViewById(R.id.ll_invite_contact);
        TV_Ref_Code.setText(userName);
        TV_Coin.setText(""+ Constant.TOTAL_COIN);
        TV_Total_Balance.setText(""+ Constant.TOTAL_BALANCE);

        if (hashMap.get(SessionManeger.KEY_PHOTO).equals(""))
        {

        }
        else
        {
            Picasso.with(context).load(hashMap.get(SessionManeger.KEY_PHOTO)).into(CimgView);
        }
        cliable();
    }

    private void cliable() {
        IV_Back_Arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        RL_share_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.setPackage("com.whatsapp");           // so that only Whatsapp reacts and not the chooser
                i.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                i.putExtra(Intent.EXTRA_TEXT, "Here get 50 Tokens and 50 Rs Bonus cash to play with me to Elite play. Click the link "+"http://www.arenaitech.com/"+ " to download the App and use my invite code "+userName+ " to register.");
                startActivity(i);
            }
        });

        LL_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                i.putExtra(Intent.EXTRA_TEXT, "Here get 50 Tokens and 50 Rs Bonus cash to play with me to Elite play. Click the link "+"http://www.arenaitech.com/"+ " to download the App and use my invite code "+userName+ " to register.");
                startActivity(i);
            }
        });

        img_copyText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", TV_Ref_Code.getText().toString());
                if (clipboard == null) return;
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(),"Copy",Toast.LENGTH_LONG).show();
            }
        });


      /*  LL_Invite_Contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent("android.intent.action.MAIN");
                sendIntent.setComponent(new ComponentName("com.whatsapp","com.whatsapp.Conversation"));
                sendIntent.putExtra("College", PhoneNumberUtils.stripSeparators("9561922280")+"@s.whatsapp.net");
                startActivity(sendIntent);
            }
        });*/
    }

}
