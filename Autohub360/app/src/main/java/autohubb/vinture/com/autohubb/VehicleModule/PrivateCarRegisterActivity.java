package autohubb.vinture.com.autohubb.VehicleModule;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
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
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import autohubb.vinture.com.autohubb.Adapter.CustomAdapterArrayList;
import autohubb.vinture.com.autohubb.R;
import autohubb.vinture.com.autohubb.activity.GetFilePathFromDevice;
import autohubb.vinture.com.autohubb.activity.MainActivity;
import autohubb.vinture.com.autohubb.fonts.FontsOverride;
import autohubb.vinture.com.autohubb.utils.Config;
import autohubb.vinture.com.autohubb.utils.HttpUtils;
import autohubb.vinture.com.autohubb.utils.Util;

public class PrivateCarRegisterActivity extends Activity {

    EditText vin_et, mileage_et;
    Spinner caryear_spn, carmake_spn, carmodel_spn, trim_spn;
    Typeface helvetica;
    ArrayList<String> caryear_arraylist = new ArrayList<>();
    ArrayList<String> carmake_arraylist = new ArrayList<>();
    ArrayList<String> carmodel_arraylist = new ArrayList<>();
    ArrayList<String> cartrim_arraylist = new ArrayList<>();
    CustomAdapterArrayList carYearAdapter, carMakeAdapter, carModelAdapter, carTrimAdapter;
    String str_year, str_carmake, str_carmodel, str_trim;
    public static ImageView image1_iv, image2_iv, upload1, upload2;
    Dialog cameraDialog;
    LinearLayout lnr_takepic, lnr_takegallery;
    Bitmap bitmap;
    String status_msg, selectedImagePath, path_carimg1, path_carimg2;
    TextView submit_tv, add_carimg_tv;
    LinearLayout back_lv;
    HorizontalScrollView hsscroll;
    TextView okay, message;
    protected static final int TAKE_CAR_PIC1 = 1;
    protected static final int SELECT_CAR_PIC1 = 3;
    SharedPreferences sharedlogin;
    SharedPreferences.Editor editlogin;
    String cloudImageUrl,get_mail_from_reg, get_otp_from_reg, get_apikey_from_login, get_email_from_login, get_userid_from_login, get_username_from_login;
    AVLoadingIndicatorView av_loader;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_privatecar);

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(PrivateCarRegisterActivity.this, v1);

        helvetica = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/helevetical.ttf");
        caryear_spn = findViewById(R.id.year_spn);
        carmake_spn = findViewById(R.id.make_spn);
        carmodel_spn = findViewById(R.id.model_spn);
        trim_spn = findViewById(R.id.trim_spn);

        vin_et = findViewById(R.id.vin_et);
        mileage_et = findViewById(R.id.mileage_et);
        image1_iv = findViewById(R.id.image1_iv);
        image2_iv = findViewById(R.id.image2_iv);
        submit_tv = findViewById(R.id.submit_tv);
        back_lv = findViewById(R.id.back_lv);
        add_carimg_tv = findViewById(R.id.add_carimg_tv);
        av_loader = (AVLoadingIndicatorView) findViewById(R.id.avi);

        Map config = new HashMap();
        config.put("cloud_name", "sqindia");
        // MediaManager.init(this, config);


        Typeface lato_head = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/heleveticalBold.TTF");
        add_carimg_tv.setTypeface(lato_head);
        upload1 = findViewById(R.id.upload1);
        upload2 = findViewById(R.id.upload2);

        sharedlogin = PreferenceManager.getDefaultSharedPreferences(PrivateCarRegisterActivity.this);
        editlogin = sharedlogin.edit();
        get_mail_from_reg = sharedlogin.getString("get_mail_from_reg", "");
        get_otp_from_reg = sharedlogin.getString("get_otp_from_reg", "");

        get_apikey_from_login = sharedlogin.getString("get_apikey_from_login", "");
        get_email_from_login = sharedlogin.getString("get_email_from_login", "");
        get_userid_from_login = sharedlogin.getString("get_userid_from_login", "");
        get_username_from_login = sharedlogin.getString("get_username_from_login", "");

        cameraDialog = new Dialog(PrivateCarRegisterActivity.this);
        cameraDialog.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);


        back_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();

            }
        });


        carmake_arraylist.add("Select Car Make");
        carMakeAdapter = new CustomAdapterArrayList(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, carmake_arraylist) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {

                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTypeface(helvetica);
                tv.setTextSize(16);
                if (position == 0) {
                    tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.spin_text));
                }
                return view;
            }


            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextSize(19);
                tv.setTypeface(helvetica);
                if (position == 0) {
                    tv.setTextColor(getResources().getColor(R.color.gray_text));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.textcolor));
                }
                return view;
            }
        };
        carmake_spn.setAdapter(carMakeAdapter);


        carmodel_arraylist.add("Select Car Model");
        carModelAdapter = new CustomAdapterArrayList(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, carmodel_arraylist) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {

                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTypeface(helvetica);
                tv.setTextSize(16);
                if (position == 0) {
                    tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.spin_text));
                }
                return view;
            }


            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextSize(19);
                tv.setTypeface(helvetica);
                if (position == 0) {
                    tv.setTextColor(getResources().getColor(R.color.gray_text));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.textcolor));
                }
                return view;
            }
        };
        carmodel_spn.setAdapter(carModelAdapter);

        cartrim_arraylist.add("Select Car Trim");
        carTrimAdapter = new CustomAdapterArrayList(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, cartrim_arraylist) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {

                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTypeface(helvetica);
                tv.setTextSize(16);
                if (position == 0) {
                    tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.spin_text));
                }
                return view;
            }


            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextSize(19);
                tv.setTypeface(helvetica);
                if (position == 0) {
                    tv.setTextColor(getResources().getColor(R.color.gray_text));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.textcolor));
                }
                return view;
            }
        };
        trim_spn.setAdapter(carTrimAdapter);

        if (caryear_arraylist.isEmpty()) {
            if (Util.Operations.isOnline(getApplicationContext())) {
                new getCarYear().execute();
            } else {
                Toast.makeText(getApplicationContext(), "Please Check Internet Connectivity", Toast.LENGTH_LONG).show();
            }
        }


        caryear_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_year = (String) adapterView.getItemAtPosition(i);

                if (Util.Operations.isOnline(getApplicationContext())) {
                    new getCarMake(str_year).execute();
                } else {
                    Toast.makeText(getApplicationContext(), "Please check Internet Connectivity", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        carmake_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_carmake = (String) adapterView.getItemAtPosition(i);
                Log.e("tag", "print make------------>" + str_carmake);
                if (Util.Operations.isOnline(getApplicationContext())) {
                    new getCarModel(str_year, str_carmake).execute();
                } else {
                    Toast.makeText(getApplicationContext(), "Please check Internet Connectivity", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        carmodel_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_carmodel = (String) adapterView.getItemAtPosition(i);
                Log.e("tag", "print Model------------>" + str_carmodel);

                if (Util.Operations.isOnline(getApplicationContext())) {
                    new getCarTrim(str_year, str_carmake, str_carmodel).execute();
                } else {
                    Toast.makeText(getApplicationContext(), "Please check Internet Connectivity", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        trim_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_trim = (String) adapterView.getItemAtPosition(i);
                Log.e("tag", "print Trim------------>" + str_trim);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        submit_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!TextUtils.isEmpty(vin_et.getText())) {
                    if (!str_year.equals("Select Year")) {
                        if (!str_carmake.equals("Select Car Make")) {

                           /* if(!str_carmodel.equals("Select Car Model"))
                            {*/

                               /* if(!str_trim.equals("Select Car Trim"))
                                {*/
                            if (!TextUtils.isEmpty(mileage_et.getText())) {

                                if (image1_iv.getDrawable() != null) {
                                    new upload_image().execute();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Car Image is Required", Toast.LENGTH_LONG).show();
                                }


                            } else {
                                mileage_et.requestFocus();
                                mileage_et.setError("Mileage is Required!");
                            }


                                /*}
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Please Select Car Trim",Toast.LENGTH_LONG).show();
                                }*/

                           /* }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Please Select Car Model",Toast.LENGTH_LONG).show();
                            }*/
                        } else {
                            Toast.makeText(getApplicationContext(), "Please Select Car Make", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Please Select Car Year", Toast.LENGTH_LONG).show();
                    }
                } else {
                    vin_et.requestFocus();
                    vin_et.setError("Vin No is Required!");
                }

            }
        });


        image1_iv.setOnClickListener(new View.OnClickListener() {
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

    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        if ((result == PackageManager.PERMISSION_GRANTED) && (result1 == PackageManager.PERMISSION_GRANTED) && (result2 == PackageManager.PERMISSION_GRANTED)) {
            return true;
        } else {
            ActivityCompat.requestPermissions(PrivateCarRegisterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
            return false;
        }
    }

    //GET YEAR API CALL: ---------------------------------------------------------------------->
    public class getCarYear extends AsyncTask<String, String, String> {
        private ProgressDialog dialog;

        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(PrivateCarRegisterActivity.this);
            dialog.setMessage("Loading Year..., please wait.");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://carqueryapi.com/api/0.3/?cmd=getYears")
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .addHeader("Postman-Token", "9a479868-31a0-4760-a9d8-7d9007955ad5")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            try {
                JSONObject jo = new JSONObject(s);
                caryear_arraylist.clear();
                JSONObject object = jo.getJSONObject("Years");
                String min_year = object.getString("min_year");
                String max_year = object.getString("max_year");


                int minyear = Integer.parseInt(min_year);
                int maxyaer = Integer.parseInt(max_year);

                caryear_arraylist.add(0, "Select Year");
                for (int i = maxyaer; i >= minyear; i--)

                {

                    caryear_arraylist.add(String.valueOf(i));
                }

                caryear_spn.setVisibility(View.VISIBLE);

// Spinner Adapter:
                carYearAdapter = new CustomAdapterArrayList(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, caryear_arraylist) {
                    @Override
                    public boolean isEnabled(int position) {
                        if (position == 0) {

                            return false;
                        } else {
                            return true;
                        }
                    }

                    @Override
                    public View getDropDownView(int position, View convertView,
                                                ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        tv.setTypeface(helvetica);
                        tv.setTextSize(16);
                        if (position == 0) {
                            tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                        } else {
                            tv.setTextColor(getResources().getColor(R.color.spin_text));
                        }
                        return view;
                    }


                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        tv.setTextSize(19);
                        tv.setTypeface(helvetica);
                        if (position == 0) {
                            tv.setTextColor(getResources().getColor(R.color.gray_text));
                        } else {
                            tv.setTextColor(getResources().getColor(R.color.textcolor));
                        }
                        return view;
                    }
                };
                caryear_spn.setAdapter(carYearAdapter);


                if (dialog.isShowing()) {
                    dialog.dismiss();
                }


            } catch (Exception e) {

            }
        }
    }


    //GET MAKE API CALL: ---------------------------------------------------------------------->
    public class getCarMake extends AsyncTask<String, String, String> {
        private ProgressDialog dialog1;
        String year_str;

        public getCarMake(String str_caryear) {
            this.year_str = str_caryear;

        }

        protected void onPreExecute() {
            super.onPreExecute();
            dialog1 = new ProgressDialog(PrivateCarRegisterActivity.this);
            dialog1.setMessage("Loading Car Make..., please wait.");
            dialog1.show();
        }

        @Override
        protected String doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://www.carqueryapi.com/api/0.3/?cmd=getMakes&year=" + year_str)
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .addHeader("Postman-Token", "9a479868-31a0-4760-a9d8-7d9007955ad5")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (dialog1.isShowing()) {
                dialog1.dismiss();
            }


            try {
                JSONObject jo = new JSONObject(s);
                carmake_arraylist.clear();
                JSONArray jsonArray = jo.getJSONArray("Makes");
                carmake_arraylist.add(0, "Select Car Make");
                for (int l = 0; l < jsonArray.length(); l++) {
                    JSONObject object = jsonArray.getJSONObject(l);
                    String make_id = object.getString("make_id");
                    String make_display = object.getString("make_display");
                    carmake_arraylist.add(make_display);
                }

                carMakeAdapter = new CustomAdapterArrayList(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, carmake_arraylist) {
                    @Override
                    public boolean isEnabled(int position) {
                        if (position == 0) {

                            return false;
                        } else {
                            return true;
                        }
                    }

                    @Override
                    public View getDropDownView(int position, View convertView,
                                                ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        tv.setTypeface(helvetica);
                        tv.setTextSize(16);
                        if (position == 0) {
                            tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                        } else {
                            tv.setTextColor(getResources().getColor(R.color.spin_text));
                        }
                        return view;
                    }


                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        tv.setTextSize(19);
                        tv.setTypeface(helvetica);
                        if (position == 0) {
                            tv.setTextColor(getResources().getColor(R.color.gray_text));
                        } else {
                            tv.setTextColor(getResources().getColor(R.color.textcolor));
                        }
                        return view;
                    }
                };
                carmake_spn.setAdapter(carMakeAdapter);


            } catch (Exception e) {

            }
        }
    }


    //GET MODEL API CALL: ---------------------------------------------------------------------->
    public class getCarModel extends AsyncTask<String, String, String> {
        private ProgressDialog dialog2;
        String getyear_str;
        String make_str;

        public getCarModel(String str_caryear, String str_carmake) {
            this.getyear_str = str_caryear;
            this.make_str = str_carmake;

        }


        protected void onPreExecute() {
            super.onPreExecute();
            dialog2 = new ProgressDialog(PrivateCarRegisterActivity.this);
            dialog2.setMessage("Loading Car Model..., please wait.");
            dialog2.show();
        }

        @Override
        protected String doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://www.carqueryapi.com/api/0.3/?cmd=getModels&year=" + getyear_str + "&make=" + make_str)
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .addHeader("Postman-Token", "9a479868-31a0-4760-a9d8-7d9007955ad5")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (dialog2.isShowing()) {
                dialog2.dismiss();
            }

            try {
                JSONObject jo = new JSONObject(s);

                JSONArray jsonArray = jo.getJSONArray("Models");
                carmodel_arraylist.clear();

                carmodel_arraylist.add(0, "Select Car Model");

                for (int l = 0; l < jsonArray.length(); l++) {
                    JSONObject object = jsonArray.getJSONObject(l);
                    String model_name = object.getString("model_name");
                    carmodel_arraylist.add(model_name);
                }


                carModelAdapter = new CustomAdapterArrayList(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, carmodel_arraylist) {
                    @Override
                    public boolean isEnabled(int position) {
                        if (position == 0) {

                            return false;
                        } else {
                            return true;
                        }
                    }

                    @Override
                    public View getDropDownView(int position, View convertView,
                                                ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        tv.setTypeface(helvetica);
                        tv.setTextSize(16);
                        if (position == 0) {
                            tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                        } else {
                            tv.setTextColor(getResources().getColor(R.color.spin_text));
                        }
                        return view;
                    }


                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        tv.setTextSize(19);
                        tv.setTypeface(helvetica);
                        if (position == 0) {
                            tv.setTextColor(getResources().getColor(R.color.hintcolor));
                        } else {
                            tv.setTextColor(getResources().getColor(R.color.textcolor));
                        }
                        return view;
                    }
                };
                carmodel_spn.setAdapter(carModelAdapter);


            } catch (Exception e) {

            }
        }
    }


    //GET TRIM API CALL: ---------------------------------------------------------------------->
    public class getCarTrim extends AsyncTask<String, String, String> {
        private ProgressDialog dialog2;
        String getyear_str;
        String make_str;
        String model_str;

        public getCarTrim(String str_year, String str_carmake, String str_carmodel) {

            this.getyear_str = str_year;
            this.make_str = str_carmake;
            this.model_str = str_carmodel;
        }


        protected void onPreExecute() {
            super.onPreExecute();
            dialog2 = new ProgressDialog(PrivateCarRegisterActivity.this);
            dialog2.setMessage("Loading Car Trim..., please wait.");
            dialog2.show();
        }

        @Override
        protected String doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://www.carqueryapi.com/api/0.3/?cmd=getTrims&year=" + getyear_str + "&make=" + make_str + "&model=" + model_str)
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .addHeader("Postman-Token", "9a479868-31a0-4760-a9d8-7d9007955ad5")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (dialog2.isShowing()) {
                dialog2.dismiss();
            }

            try {
                JSONObject jo = new JSONObject(s);

                JSONArray jsonArray = jo.getJSONArray("Trims");
                cartrim_arraylist.clear();

                cartrim_arraylist.add(0, "Select Car Trim");

                for (int l = 0; l < jsonArray.length(); l++) {
                    JSONObject object = jsonArray.getJSONObject(l);
                    String model_trim = object.getString("model_trim");
                    cartrim_arraylist.add(model_trim);
                }


                carTrimAdapter = new CustomAdapterArrayList(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, cartrim_arraylist) {
                    @Override
                    public boolean isEnabled(int position) {
                        if (position == 0) {

                            return false;
                        } else {
                            return true;
                        }
                    }

                    @Override
                    public View getDropDownView(int position, View convertView,
                                                ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        tv.setTypeface(helvetica);
                        tv.setTextSize(16);
                        if (position == 0) {
                            tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                        } else {
                            tv.setTextColor(getResources().getColor(R.color.spin_text));
                        }
                        return view;
                    }


                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        tv.setTextSize(19);
                        tv.setTypeface(helvetica);
                        if (position == 0) {
                            tv.setTextColor(getResources().getColor(R.color.hintcolor));
                        } else {
                            tv.setTextColor(getResources().getColor(R.color.textcolor));
                        }
                        return view;
                    }
                };
                trim_spn.setAdapter(carTrimAdapter);


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

            image1_iv.setImageBitmap(bitmap);

            path_carimg1 = mediaFile4.getAbsolutePath();




        }


        //********************CAR PIC 9 -------SELECT
        else if (resultCode == RESULT_OK && requestCode == SELECT_CAR_PIC1) {
            try {
                Uri selectedMediaUri = data.getData();
                path_carimg1 = GetFilePathFromDevice.getPath(getApplicationContext(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), selectedMediaUri);
                    image1_iv.setImageBitmap(bitmap);


                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {

            }
        }
    }


    //Uplaod Private Car:
    private class upload_car extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

        }


        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";

            try {

                JSONObject commercial_data = new JSONObject();
                commercial_data.put("userId", get_userid_from_login);
                commercial_data.put("vin", vin_et.getText().toString());
                commercial_data.put("vehicleTypeId", "");
                commercial_data.put("year", str_year);
                commercial_data.put("make", str_carmake);

                commercial_data.put("model", str_carmodel);
                commercial_data.put("trim", str_trim);
                commercial_data.put("image", "string");
                commercial_data.put("businessTypeId", "1");
                commercial_data.put("companyId", "");
                commercial_data.put("driverId", "");
                commercial_data.put("mileageRange", "");
                commercial_data.put("actualMileage", mileage_et.getText().toString());
                commercial_data.put("image",cloudImageUrl);


                json = commercial_data.toString();
                Log.e("tag", "json response---------->" + json);


                return jsonStr = HttpUtils.makeRequestToken(Config.WEB_URL_ADD_CAR, get_apikey_from_login, json);
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            av_loader.setVisibility(View.GONE);

            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");


                if (status.equals("true")) {


                    final Dialog dialog = new Dialog(PrivateCarRegisterActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.dialog_confirmation);
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);
                    okay = dialog.findViewById(R.id.okay);
                    message = dialog.findViewById(R.id.message);
                    Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/helevetical.ttf");

                    okay.setTypeface(tf);
                    message.setTypeface(tf);
                    message.setText("Private Car registered Successfully!!");


                    okay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    });

                    dialog.show();

                    String message = jo.getString("message");

                    JSONObject object = jo.getJSONObject("data");
                    String vehicle_id = object.getString("vehicleId");
                    SharedPreferences putVehicle = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor putVehicleEditor = putVehicle.edit();
                    putVehicleEditor.putString("get_vehicleid_from_pRegister", vehicle_id);
                    putVehicleEditor.commit();


                } else {
                    String message = jo.getString("message");
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
            }


        }
    }


  //Upload Image Url Instead Cloud:
    private class upload_image extends AsyncTask<String, String, String> {


        protected void onPreExecute() {
            super.onPreExecute();
            av_loader.setVisibility(View.VISIBLE);
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
                  Log.e("tag","inside1");
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
                   // Toasty.success(getApplicationContext(),message,Toasty.LENGTH_LONG).show();

                    JSONObject object=jo.getJSONObject("data");
                    cloudImageUrl=object.getString("imageUrl");

                    new upload_car().execute();


                } else {
                    av_loader.setVisibility(View.VISIBLE);
                    new upload_car().execute();
                    String message = jo.getString("message");
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
            }



        }
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

}
