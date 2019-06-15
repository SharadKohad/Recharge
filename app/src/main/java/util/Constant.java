package util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Constant
{
    public static String FISHSCORE = "";
    public static int TOTAL_BALANCE = 0;
    public static int BOUNCE_CASH = 0;
    public static int TOTAL_COIN = 0;
    public static int TOTAL_DEPOSIT_CASH = 0;
    public static JSONArray jsonObjectmatch ;
    public static JSONArray jsonArrayPlayerList ;
    public static JSONObject jsonObjectmatchSummery;
    public static String DIRECT_INCOME = "";
    public static String TOTAL_REF = "";
    public static int HIGH_SCORE=0;
    public static String APIKEY="sO54esYqnjhWuDoYlrNhjC67Gt93";//"JCr0JOu28XgyHTuHz5BJ6TWWuVV2";//"TmQf9rBDhAcsi2IRaCnzSwKJGeH2";
    public static final String URL = " http://site17.bidbch.com/api/";
    public static boolean isValidEmail(String email)
    {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static String IMG = "";
}
