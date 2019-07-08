package util;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.logicaltech.gamerecharge.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Constant
{
    Dialog dialog;
    public Context context;
    WindowManager.LayoutParams lp;

    public Constant(Context context) {
        this.context = context;
    }

    public static String FISHSCORE = "";
    public static int TOTAL_BALANCE = 0;
    public static int BOUNCE_CASH = 0;
    public static int TOTAL_COIN = 0;
    public static int TOTAL_DEPOSIT_CASH = 0;
    public static JSONArray jsonObjectmatch ;
    public static JSONArray jsonArrayBattleList ;
    public static JSONArray jsonArrayTopThreePlayer ;

    public static JSONArray jsonArrayPlayerList ;
    public static JSONObject jsonObjectmatchSummery;
    public static String DIRECT_INCOME = "";
    public static String TOTAL_REF = "";
    public static int HIGH_SCORE=0;
    public static String APIKEY="sO54esYqnjhWuDoYlrNhjC67Gt93";//"JCr0JOu28XgyHTuHz5BJ6TWWuVV2";//"TmQf9rBDhAcsi2IRaCnzSwKJGeH2";
    public static String HashId = "";
    public static final String URL = " http://site17.bidbch.com/api/";


    public static boolean isValidEmail(String email)
    {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static String IMG = "";

    public void showBounceCash() {
        dialog = new Dialog(context);
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
}
