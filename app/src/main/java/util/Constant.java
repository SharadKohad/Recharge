package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Constant
{
    public static String FISHSCORE = "";
    public static String TOTAL_BALANCE = "";
    public static String DIRECT_INCOME = "";
    public static String TOTAL_REF = "";

    public static final String URL = " http://site17.bidbch.com/api/";
    public static boolean isValidEmail(String email)
    {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
