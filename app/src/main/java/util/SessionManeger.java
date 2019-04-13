package util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.logicaltech.rechargepannel.LoginActivity;
import java.util.HashMap;

public class SessionManeger
{
    public static SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE=0;
    // Sharedpref file name
    private static final String PREF_NAME = "ShopinationUser";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    public static final String KEY_ID = "userid";
    public static final String KEY_COMPANY_ID = "companyid";

    public static final String KEY_PHONE = "phoneno";

    public static final String ORDER_ID = "current_orderId";

    public static final String FIRST_LAUNCH = "first_launch";
    public static final String MEMBER_ID = "member_id";

    public static final String FLY_FISH_SCORE = "0";

    private static SessionManeger instance;

    public static SessionManeger getInstance(Context context){
        if (instance==null){
            instance=new SessionManeger(context);
        }
        return instance;
    }

    public SessionManeger(Context context) {
        this.context = context;
        preferences=context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor=preferences.edit();
        // open SQLite when login user.
    }

    public  void createSession(String userId, String name, String email, String phone, String member_id)
    {
        editor.putBoolean(IS_LOGIN,true);
        editor.putString(KEY_ID, userId);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PHONE, phone);
        editor.putString(MEMBER_ID,member_id);
        editor.commit();
        //  getWishListVolley(userId);
    }

    public void createSessionFlyFish(String HighScore)
    {
        editor.putBoolean(IS_LOGIN,true);
        editor.putString(FLY_FISH_SCORE,HighScore);
        editor.commit();
    }

    public boolean checkLoginAtHome(){
        if (!this.isLoggedIn()){
//            Intent intent=new Intent(context, SigninActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
            return true;
        }
        return false;
    }
    public boolean checkLogin()
    {
        if (!this.isLoggedIn())
        {
            Intent intent=new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    public boolean isLoggedIn()
    {
        return preferences.getBoolean(IS_LOGIN, false);
    }

    public HashMap<String,String> getUserDetails() {
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put(KEY_ID,preferences.getString(KEY_ID,null));
        hashMap.put(KEY_NAME,preferences.getString(KEY_NAME,null));
        hashMap.put(KEY_EMAIL,preferences.getString(KEY_EMAIL,null));
        hashMap.put(KEY_PHONE,preferences.getString(KEY_PHONE,null));
        hashMap.put(MEMBER_ID,preferences.getString(MEMBER_ID,null));
        return hashMap;
    }

    // Get mobile number to store in session

    public HashMap<String,String> getFlyFishHighScore()
    {
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put(FLY_FISH_SCORE,preferences.getString(FLY_FISH_SCORE,"0"));
        return hashMap;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        context.startActivity(i);

    }

    public void editLoginSession2(String name){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref

        editor.putString(KEY_NAME, name);

        // commit changes
        editor.commit();
    }
    // this three method are use for odder id.
    public  void setOrderIdToSession(String orderID)
    {
        editor.putString(ORDER_ID, orderID);
        editor.commit();
    }

    public String getCurrentOrderId()
    {
        String orderId = preferences.getString(ORDER_ID,"");
        return orderId;
    }

    public void deleteCurrentOrderId()
    {
        editor.putString(ORDER_ID, "");
        editor.commit();
    }


    public void remoteLogout()
    {
        editor.clear();
        editor.commit();
    }

    public void setCurrentLaunch()
    {
        editor.putBoolean(FIRST_LAUNCH, false);
        editor.commit();
    }


    public boolean getFirstLaunch()
    {
        boolean firstL = preferences.getBoolean(FIRST_LAUNCH,true);
        return firstL;
    }
}
