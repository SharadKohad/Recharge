package com.logicaltech.gamerecharge;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.squareup.picasso.Picasso;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import util.Constant;
import util.MySingalton;
import util.SessionManeger;

public class ProfileDetailActivity extends AppCompatActivity
{
    SessionManeger sessionManeger;
    EditText ET_Email,ET_MobileNo,ET_MemberName;
    TextView TV_Name;
    Button Btn_Profile_Save,Btn_Change_Password;
    private String membercode,userMobile,userName,userEmail,userMemberName;
    ImageView IV_Back_Arrow;
    CircleImageView Cimg;
    private String imgPath = "";
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 7;
    private Bitmap bitmap;
    private File destination = null;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);
        sessionManeger = new SessionManeger(getApplicationContext());
        init();
    }

    public void init()
    {
        TV_Name = (TextView) findViewById(R.id.txt_ProfileName);
        ET_Email = (EditText) findViewById(R.id.EditText_ProfileEmailId);
        ET_MobileNo = (EditText) findViewById(R.id.EditText_PhoneNumber);
        ET_MemberName = (EditText) findViewById(R.id.EditText_MemberName);
        Btn_Profile_Save = (Button) findViewById(R.id.button_profile_save);
        Btn_Change_Password = (Button) findViewById(R.id.button_change_password);
        IV_Back_Arrow = (ImageView) findViewById(R.id.img_back_arrow_profile_detail);
        Cimg = (CircleImageView) findViewById(R.id.image_view_profile_edit);
        progressBar = (ProgressBar) findViewById(R.id.progress_profile);

        HashMap<String, String> hashMap = sessionManeger.getUserDetails();
        userName = hashMap.get(SessionManeger.KEY_ID);
        userMobile = hashMap.get(SessionManeger.KEY_PHONE);
        userMemberName = hashMap.get(SessionManeger.KEY_NAME);
        userEmail = hashMap.get(SessionManeger.KEY_EMAIL);
        membercode = hashMap.get(SessionManeger.MEMBER_ID);
     //   imgPath = hashMap.get(SessionManeger.KEY_PHOTO);

        TV_Name.setText(userName);
        ET_Email.setText(userEmail);
        ET_MobileNo.setText(userMobile);
        ET_MemberName.setText(userMemberName);

        String photo = hashMap.get(SessionManeger.KEY_PHOTO);
        if (photo.equals(""))
        {

        }
        else
        {
            Picasso.with(getApplicationContext()).load(photo).into(Cimg);
        }
        Cimg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                checkAndRequestPermissions();
            }
        });

        Btn_Profile_Save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                uploadCheckInMultiFile();
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
                Intent intent = new Intent(ProfileDetailActivity.this,ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
    }

   /* public void putEditProfile(final String memberId,final String Member_name,final String emailId,final String mobileNo,final String userfile,final String payTmno,final String phonePay,final String googlePay) {
        //progressBar.setVisibility(View.VISIBLE);
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = Constant.URL+"editProfile";
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
                        Toast.makeText(ProfileDetailActivity.this,"Profile Update",Toast.LENGTH_SHORT).show();
                        //  sessionManeger.createSession(userId,userName,userEmail,userMobile,userMemberName);
                    }
                    else
                    {
                        Toast.makeText(ProfileDetailActivity.this," "+message,Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e)
                {
                    //           progressBar.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        //      progressBar.setVisibility(View.INVISIBLE);
                        VolleyLog.d("volley", "Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext(),""+error.getMessage(),Toast.LENGTH_LONG).show();
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
                params.put("Memb_Name", Member_name);
                params.put("EmailID", emailId);
                params.put("Mobile_No",mobileNo);
                params.put("userFile",userfile);
                params.put("Paytm_no",payTmno);
                params.put("Phonepe_no", phonePay);
                params.put("Googlepe_no", googlePay);
                return params;
            }
        };
        MySingalton.getInstance(getApplicationContext()).addRequestQueue(jsonObjRequest);
        jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(200000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyRequestQueue.add(jsonObjRequest);
    }*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private boolean checkAndRequestPermissions() {
        int camera = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (camera != PackageManager.PERMISSION_GRANTED)
        {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (camera == PackageManager.PERMISSION_GRANTED)
        {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
            selectImage();
        }
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            // Permission is not granted
        }
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
          //  selectImage();
            // Permission is not granted
        }


        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(ProfileDetailActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
    private void selectImage() {
        try
        {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED)
            {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery","Cancel"};
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ProfileDetailActivity.this);
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo"))
                        {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, PICK_IMAGE_CAMERA);
                        }
                        else if (options[item].equals("Choose From Gallery"))
                        {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                        }
                        else if (options[item].equals("Cancel"))
                        {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_CAMERA)
        {
            try
            {
                Uri selectedImage = data.getData();
                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                Log.e("Activity", "Pick from Camera::>>> ");
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                destination = new File(Environment.getExternalStorageDirectory() + "/" +
                        getString(R.string.app_name), "IMG_" + timeStamp + ".jpg");
                FileOutputStream fo;
                try
                {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                imgPath = destination.getAbsolutePath();
                Cimg.setImageBitmap(bitmap);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else if (requestCode == PICK_IMAGE_GALLERY)
        {
            Uri selectedImage = data.getData();
            try
            {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                Log.e("Activity", "Pick from Gallery::>>> ");
                imgPath = getRealPathFromURI(selectedImage);
                destination = new File(imgPath.toString());
                Cimg.setImageBitmap(bitmap);
               // getBase64(imgPath);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public interface WebServicesCheckInAPI {
        @PUT("editProfile")
        Call<ResponseBody> uploadMultiFile(@Body RequestBody file);
    }

    private void uploadCheckInMultiFile()
    {
        progressBar.setVisibility(View.VISIBLE);
      //  final JSONObject jsonObject = new JSONObject();
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        try
        {
            builder.addFormDataPart("membercode", membercode);
            builder.addFormDataPart("Mobile_No", ET_MobileNo.getText().toString());
            builder.addFormDataPart("Memb_Name", ET_MemberName.getText().toString());
            builder.addFormDataPart("EmailID", ET_Email.getText().toString());

            if (imgPath.equals(""))
            {

            }
            else
            {
                File file = new File(imgPath.toString());
                builder.addFormDataPart("userFile", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
            }

            // Map is used to multipart the file using okhttp3.RequestBody
            // Multiple Images
           /* try {
               // String[] filePaths = jsonObject.getString("photo_list").split(",");

             *//*   for (int i = 0; i < filePaths.length; i++) {
                    File file = new File(filePaths[i]);
                    builder.addFormDataPart("package_images[]", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
                }*//*
            } catch (Exception e) {
                e.printStackTrace();
            }*/

            Retrofit retrofit = new Retrofit.Builder().baseUrl(Constant.URL).build();
            WebServicesCheckInAPI uploadService = retrofit.create(WebServicesCheckInAPI.class);
            MultipartBody requestBody = builder.build();
            Call<ResponseBody> call = uploadService.uploadMultiFile(requestBody);
            call.enqueue(new Callback<ResponseBody>()
            {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try
                    {
                        progressBar.setVisibility(View.GONE);
                        JSONObject json = new JSONObject(response.body().string());
                        if (json.getString("status").equals("1"))
                        {
                            Toast.makeText(getApplicationContext(), json.getString("msg"), Toast.LENGTH_LONG).show();
                            sessionManeger.createSession(json.getString("Memb_Name"),json.getString("username"),json.getString("EmailID"),json.getString("Mobile_No"),membercode,json.getString("userFile"));
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), json.getString("msg"), Toast.LENGTH_LONG).show();
                        }
                    }
                    catch (Exception e)
                    {
                        progressBar.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t)
                {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ProfileDetailActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
