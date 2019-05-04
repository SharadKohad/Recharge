package com.logicaltech.gamerecharge;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import util.Constant;
import util.MySingalton;
import util.SessionManeger;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener
{
    Button btn_signin;
    TextInputEditText TIET_user_name,TIET_password;
    TextView TV_Signup;
    ProgressBar progressBar;
    String user_name,password,first_name,last_name,email;
    SessionManeger sessionManeger;
    TextView TVforgotpassword;
    Dialog dialog;
    WindowManager.LayoutParams lp;
    ImageView IV_Gmail_LogIn;
    LoginButton loginButton;
    CallbackManager callbackManager;
    CircleImageView img_facebook,img_gmail;
    private GoogleApiClient googleApiClient;
    private static final int RED_CODE= 9001;

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
        loginButton = findViewById(R.id.login_button);
        img_facebook = (CircleImageView) findViewById(R.id.fb);
        img_gmail = (CircleImageView) findViewById(R.id.gmail);
        btn_signin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                signIn();
            }
        });




        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions).build();


        img_gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInGmail();
            }
        });


        img_facebook.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                loginButton.performClick();
            }
        });

        TV_Signup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                intent.putExtra("token","0");
                startActivity(intent);
            }
        });

        TVforgotpassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showCustomDialog();
            }
        });

       /* boolean loggedOut = AccessToken.getCurrentAccessToken() == null;

        if (!loggedOut)
        {
            Toast.makeText(getApplicationContext(),"false",Toast.LENGTH_SHORT).show();
        }
        else
        {
            getUserProfile(AccessToken.getCurrentAccessToken());
        }*/

        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));

        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                boolean loggedIn = AccessToken.getCurrentAccessToken() == null;
                Log.d("API123", loggedIn + " ??");
                getUserProfile(AccessToken.getCurrentAccessToken());
            }

            @Override
            public void onCancel()
            {
                // App code
                Toast.makeText(getApplicationContext(),"On Cancel",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception)
            {
                // App code
                Toast.makeText(getApplicationContext(),"On Error"+exception,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==RED_CODE)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }
    public void signIn() {
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
    public void signInVolly(final String userId, final String Password) {
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
    private void showCustomDialog() {
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
    public void forgotPassword(final String userId) {
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
    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(currentAccessToken, new GraphRequest.GraphJSONObjectCallback()
                {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response)
                    {
                        Log.d("TAG", object.toString());
                        try
                        {
                            first_name = object.getString("first_name");
                            last_name = object.getString("last_name");
                            email = object.getString("email");
                            signInVollySocialLogin(email);
                        //    String id = object.getString("id");
                          //  String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }
    public void signInVollySocialLogin(final String email) {
        progressBar.setVisibility(View.VISIBLE);
        btn_signin.setAlpha(0f);
        String url = Constant.URL+"getLoginDtlByEmailID";
        StringRequest jsonObjRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    progressBar.setVisibility(View.GONE);
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
                        Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                        intent.putExtra("token","1");
                        intent.putExtra("first_name",first_name);
                        intent.putExtra("last_name",last_name);
                        intent.putExtra("email",email);
                        startActivity(intent);
                    }
                }
                catch (JSONException e)
                {
                    progressBar.setVisibility(View.GONE);
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
                params.put("Email", email);
                return params;
            }
        };
        MySingalton.getInstance(getApplicationContext()).addRequestQueue(jsonObjRequest);
        jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signInGmail() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,RED_CODE);
    }

    private void handleResult(GoogleSignInResult result) {
        if (result.isSuccess())
        {
            GoogleSignInAccount account = result.getSignInAccount();
            first_name = account.getDisplayName();
            last_name = account.getFamilyName();
            email = account.getEmail();
            signInVollySocialLogin(email);
        }
        else
        {

        }
    }
}
