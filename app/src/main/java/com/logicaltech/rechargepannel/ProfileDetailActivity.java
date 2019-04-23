package com.logicaltech.rechargepannel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import util.Constant;
import util.MySingalton;
import util.SessionManeger;

public class ProfileDetailActivity extends AppCompatActivity
{
    SessionManeger sessionManeger;
    EditText ET_Name,ET_Email,ET_MobileNo,ET_MemberName;
    Button Btn_Profile_Save,Btn_Change_Password;
    private String membercode,userMobile,userName,userEmail,userMemberName;
    ImageView IV_Back_Arrow;
    CircleImageView Cimg;
    private String imgPath = null,base64Sting;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 7;
    private InputStream inputStreamImg;
    private Bitmap bitmap;
    private File destination = null;

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
        ET_Name = (EditText) findViewById(R.id.EditText_ProfileName);
        ET_Email = (EditText) findViewById(R.id.EditText_ProfileEmailId);
        ET_MobileNo = (EditText) findViewById(R.id.EditText_PhoneNumber);
        ET_MemberName = (EditText) findViewById(R.id.EditText_MemberName);
        Btn_Profile_Save = (Button) findViewById(R.id.button_profile_save);
        Btn_Change_Password = (Button) findViewById(R.id.button_change_password);
        IV_Back_Arrow = (ImageView) findViewById(R.id.img_back_arrow_profile_detail);
        Cimg = (CircleImageView) findViewById(R.id.image_view_profile_edit);

        HashMap<String, String> hashMap = sessionManeger.getUserDetails();
        userMemberName = hashMap.get(SessionManeger.KEY_ID);
        userMobile = hashMap.get(SessionManeger.KEY_PHONE);
        userName = hashMap.get(SessionManeger.KEY_NAME);
        userEmail = hashMap.get(SessionManeger.KEY_EMAIL);
        membercode = hashMap.get(SessionManeger.MEMBER_ID);

        ET_Name.setText(userName);
        ET_Email.setText(userEmail);
        ET_MobileNo.setText(userMobile);
        ET_MemberName.setText(userMemberName);

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
                putEditProfile(membercode,ET_Name.getText().toString(),ET_Email.getText().toString(),ET_MobileNo.getText().toString(),base64Sting,"7276612632","7276612632","7276612632");
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

    public void putEditProfile(final String memberId,final String Member_name,final String emailId,final String mobileNo,final String userfile,final String payTmno,final String phonePay,final String googlePay) {
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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean checkAndRequestPermissions()
    {
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
        inputStreamImg = null;
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
                getBase64();
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

    private String getBase64() {
        File file = new File(imgPath);  //file Path
        byte[] b = new byte[(int) file.length()];
        try
        {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(b);
            for (int j = 0; j < b.length; j++)
            {
                System.out.print((char) b[j]);
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File Not Found.");
            e.printStackTrace();
        }
        catch (IOException e1)
        {
            System.out.println("Error Reading The File.");
            e1.printStackTrace();
        }
        byte[] byteFileArray = new byte[0];
        try
        {
            byteFileArray = FileUtils.readFileToByteArray(file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        base64Sting = "";
        if (byteFileArray.length > 0)
        {
            base64Sting = android.util.Base64.encodeToString(byteFileArray, android.util.Base64.NO_WRAP);
            Log.i("File Base64 string", "IMAGE PARSE ==>" + base64Sting);
        }
        System.out.print(" My URL"+base64Sting);
        return base64Sting;
    }

}
