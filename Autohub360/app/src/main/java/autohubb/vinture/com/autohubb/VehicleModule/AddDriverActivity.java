package autohubb.vinture.com.autohubb.VehicleModule;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import autohubb.vinture.com.autohubb.R;
import autohubb.vinture.com.autohubb.activity.GetFilePathFromDevice;
import autohubb.vinture.com.autohubb.fonts.FontsOverride;
import autohubb.vinture.com.autohubb.utils.Config;
import autohubb.vinture.com.autohubb.utils.HttpUtils;

public class AddDriverActivity extends Activity {
    EditText fname,lname,phone,email,city,state;
    TextView submit_driver,head_tv;
    AVLoadingIndicatorView av_loader,dialog_avi;
    SharedPreferences sharedlogin;
    SharedPreferences.Editor editlogin;
    String companyId,get_mail_from_reg,get_otp_from_reg,get_apikey_from_login,get_email_from_login,get_userid_from_login,get_username_from_login;
    LinearLayout back_lv;
    Typeface lato_head;
    Bundle bundle;
    ImageView capture,driver_profile;
    protected static final int TAKE_CAR_PIC1 = 1;
    protected static final int SELECT_CAR_PIC1 = 2;
    Dialog cameraDialog;
    LinearLayout lnr_takepic, lnr_takegallery;
    Bitmap bitmap;
    String selectedImagePath,path_carimg1,cloudImageUrl;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_driver);


        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(AddDriverActivity.this, v1);

        cameraDialog = new Dialog(AddDriverActivity.this);
        cameraDialog.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);

        lato_head = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/heleveticalBold.TTF");
        fname=findViewById(R.id.fname);
        lname=findViewById(R.id.lname);
        phone=findViewById(R.id.phone);
        email=findViewById(R.id.email);
        city=findViewById(R.id.city);
        state=findViewById(R.id.state);
        capture=findViewById(R.id.capture);
        driver_profile=findViewById(R.id.driver_profile);

        submit_driver=findViewById(R.id.submit_driver);
        dialog_avi=findViewById(R.id.dialog_avi);
        back_lv=findViewById(R.id.back_lv);

        bundle= getIntent().getExtras();

        sharedlogin = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editlogin = sharedlogin.edit();
        get_mail_from_reg = sharedlogin.getString("get_mail_from_reg", "");
        get_otp_from_reg=sharedlogin.getString("get_otp_from_reg","");
        get_apikey_from_login = sharedlogin.getString("get_apikey_from_login", "");
        get_email_from_login=sharedlogin.getString("get_email_from_login","");
        get_userid_from_login = sharedlogin.getString("get_userid_from_login", "");
        get_username_from_login=sharedlogin.getString("get_username_from_login","");
        companyId = sharedlogin.getString("get_companyid_from_compannylist","");



        submit_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(fname.getText())) {
                    if(!TextUtils.isEmpty(lname.getText())) {
                        if(!TextUtils.isEmpty(phone.getText())) {
                            if(!TextUtils.isEmpty(email.getText())) {
                                if(!TextUtils.isEmpty(city.getText())) {
                                    if(!TextUtils.isEmpty(state.getText())) {
                                        if (driver_profile.getDrawable() != null) {
                                            new upload_image().execute();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Driver Image is Required", Toast.LENGTH_LONG).show();
                                        }

                                    }else {
                                        state.requestFocus();
                                        state.setError( "State is Required!" );
                                    }

                                }else {
                                    city.requestFocus();
                                    city.setError( "City is Required!" );
                                }

                            }else {
                                email.requestFocus();
                                email.setError( "Email is Required!" );
                            }

                        }else {
                            phone.requestFocus();
                            phone.setError( "Phone is Required!" );
                        }

                    }else {
                        lname.requestFocus();
                        lname.setError( "Last Name is Required!" );
                    }

                }else {
                    fname.requestFocus();
                    fname.setError( "First Name is Required!" );
                }




            }
        });




        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraDialog.setContentView(R.layout.camera_dialog);
                lnr_takepic = cameraDialog.findViewById(R.id.lnr_takepic);
                lnr_takegallery = cameraDialog.findViewById(R.id.lnr_takegallery);


                // if button is clicked, close the custom dialog
                lnr_takepic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cameraDialog.dismiss();
                        if (checkPermission()) {
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, TAKE_CAR_PIC1);
                        }
                    }
                });


                lnr_takegallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cameraDialog.dismiss();
                        if (checkPermission()) {
                            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            pickIntent.setType("image/*");
                            startActivityForResult(pickIntent, SELECT_CAR_PIC1);
                        }
                    }
                });
                cameraDialog.show();
            }
        });



        back_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddDriverActivity.this, CommercialCarListActivity.class));
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                finish();
            }
        });
    }




    //UPLAOD DRIVER API CALL: ---------------------------------------------------------------------->
    private class upload_driver extends AsyncTask<String,String,String> {

        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";

            try {

                JSONObject driver_data = new JSONObject();
                driver_data.put("companyId", companyId);
                driver_data.put("firstName", fname.getText().toString());
                driver_data.put("lastName", lname.getText().toString());
                driver_data.put("email", email.getText().toString());
                driver_data.put("phone", phone.getText().toString());
                driver_data.put("city", city.getText().toString());
                driver_data.put("state", state.getText().toString());
                driver_data.put("image",cloudImageUrl);


                json = driver_data.toString();
                Log.e("tag","json response---------->"+json);


                return jsonStr = HttpUtils.makeRequestToken(Config.WEB_URL_ADD_DRIVER_DETAILS,get_apikey_from_login,json);
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog_avi.setVisibility(View.GONE);

            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");


                if (status.equals("true")) {
                    String message=jo.getString("message");



                    JSONObject object=jo.getJSONObject("data");
                    String companyId=object.getString("driverId");
                    Intent i=new Intent(getApplicationContext(),DriversPageActivity.class);
                    startActivity(i);
                    /*SharedPreferences putCompany = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor putVehicleEditor = putCompany.edit();
                    putVehicleEditor.putString("get_companyid_from_pRegister",companyId);
                    putVehicleEditor.commit();
*/



                } else {
                    Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
            }






        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        if ((result == PackageManager.PERMISSION_GRANTED) && (result1 == PackageManager.PERMISSION_GRANTED) && (result2 == PackageManager.PERMISSION_GRANTED)) {
            return true;
        } else {
            ActivityCompat.requestPermissions(AddDriverActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
            return false;
        }
    }


    //Upload Image Url Instead Cloud:
    private class upload_image extends AsyncTask<String, String, String> {


        protected void onPreExecute() {

            super.onPreExecute();

            dialog_avi.setVisibility(View.VISIBLE);
        }



        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            try {
                String responseString = null;
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Config.WEB_URL_IMAGE_UPLOAD);
                //HttpPost httppost = new HttpPost("http://104.197.80.225/autoparts360/api/add_car_details");
                httppost.setHeader("x-api-key",get_apikey_from_login);



                HttpResponse response = null;
                HttpEntity r_entity = null;


                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                //upload Images     *************************************  1

                if(!path_carimg1.equals(""))
                {
                    Log.e("tag","inside1"+path_carimg1);

                    entity.addPart("cloudImage", new FileBody(new File(path_carimg1), "image/jpeg"));
                }


                httppost.setEntity(entity);
                try {
                    try {
                        response = httpclient.execute(httppost);
                    } catch (Exception e) {
                        Log.e("tag", "ds:" + e.toString());
                    }
                    try {
                        r_entity = response.getEntity();
                    } catch (Exception e) {
                        Log.e("tag", "dsa:" + e.toString());
                    }

                    int statusCode = response.getStatusLine().getStatusCode();
                    Log.e("tag", response.getStatusLine().toString());
                    if (statusCode == 200) {
                        responseString = EntityUtils.toString(r_entity);
                        Log.e("tag", "rssss" + responseString);
                        // return success;

                    } else {
                        responseString = "Error occurred! Http Status Code: "
                                + statusCode;
                        Log.e("tag3", responseString);
                    }
                } catch (ClientProtocolException e) {
                    responseString = e.toString();
                    Log.e("tag44", responseString);
                } catch (IOException e) {
                    responseString = e.toString();
                    Log.e("tag45", responseString);
                }
                return responseString;
            } catch (Exception e) {
                Log.e("tag_InputStream0", e.getLocalizedMessage());
            }
            return null;

        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);



            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");


                if (status.equals("true")) {

                    String message = jo.getString("message");
                    //Toasty.success(getApplicationContext(),message,Toasty.LENGTH_LONG).show();

                    JSONObject object=jo.getJSONObject("data");
                    cloudImageUrl=object.getString("imageUrl");

                    new upload_driver().execute();


                } else {
                    dialog_avi.setVisibility(View.VISIBLE);
                    new upload_driver().execute();
                    String message = jo.getString("message");
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
            }



        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        bitmap = null;
        selectedImagePath = null;

        //********************CAR PIC 5 -------TAKE
        if (resultCode == RESULT_OK && requestCode == TAKE_CAR_PIC1) {

            bitmap = (Bitmap) data.getExtras().get("data");
            File sd = Environment.getExternalStorageDirectory();
            File imagepath = new File(sd.getAbsolutePath() + File.separator +
                    "AutoHubb360");

            if (!imagepath.isDirectory()) {
                imagepath.mkdirs();
            }

            File mediaFile4 = new File(imagepath + File.separator + "img_" +
                    System.currentTimeMillis() + ".jpg");

            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(mediaFile4);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


            driver_profile.setImageBitmap(bitmap);
            //upload1.setVisibility(View.GONE);
            path_carimg1 = mediaFile4.getAbsolutePath();
            Log.e("tag", "Take Car Path1" + path_carimg1);



            /*edit.putString("store_birdeyeview_path",path_birdeye_view);
            edit.commit();*/
        }


        //********************CAR PIC 9 -------SELECT
        else if (resultCode == RESULT_OK && requestCode == SELECT_CAR_PIC1) {
            try {
                Uri selectedMediaUri = data.getData();
                path_carimg1 = GetFilePathFromDevice.getPath(getApplicationContext(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), selectedMediaUri);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageBytes = baos.toByteArray();
                    String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                    Log.e("tag", "printing BITMAP------------>" + imageString);
                    driver_profile.setImageBitmap(bitmap);
                    Log.e("tag", "Select Car Path1" + path_carimg1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {
            }
        }

    }


        @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AddDriverActivity.this, CommercialCarListActivity.class));
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
        finish();
    }
}
