package autohubb.vinture.com.autohubb.VehicleModule;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import autohubb.vinture.com.autohubb.R;
import autohubb.vinture.com.autohubb.activity.MainActivity;
import autohubb.vinture.com.autohubb.fonts.FontsOverride;
import autohubb.vinture.com.autohubb.utils.Config;
import autohubb.vinture.com.autohubb.utils.HttpUtils;
import autohubb.vinture.com.autohubb.utils.Util;
import es.dmoral.toasty.Toasty;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrderServicePack extends Activity {
    TextView order_rugularkit, order_tuneupkit;
    Intent intent;
    String str_carid, str_carname, str_vin, str_mileage, str_act_mileage, str_image, str_type;
    public static TextView head_tv, carname, vinno_tv, act_mileageval_tv, mileageval_tv, cartype_tv, location_val, addcart_tv, previous_tv;
    ImageView carimg_iv;
    RecyclerView pack_recyclerView;
    LinearLayout back_lv;
    protected static final int TAKE_CAR_PIC1 = 1;
    protected static final int SELECT_CAR_PIC1 = 3;
    public static ImageView image1_iv;
    Dialog cameraDialog;
    LinearLayout lnr_takepic, lnr_takegallery;
    Bitmap bitmap;
    String selectedImagePath, path_carimg1, path_carimg2, str_product_id;
    SharedPreferences sharedlogin;
    SharedPreferences.Editor editlogin;
    String get_mail_from_reg, get_otp_from_reg, get_apikey_from_login, get_email_from_login, get_userid_from_login, get_username_from_login;
    TextView okay, message;
    ImageView close_icon, icon_message;
    EditText comment_et, mileage_run_et;
    AVLoadingIndicatorView av_loader,av1;
    List<List<ServicePackListPojo>> packsPartsList;
    ServicePackListPojo servicePackListPojo;
    MyPackAdapter myPackAdapter;
    private List<ServicePackListPojo> servicePackListPojos = new ArrayList<>();
    public static LinearLayout layout_one, layout_two;
    public static String packname,getProductId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_service_pack1);
        intent = getIntent();


        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(OrderServicePack.this, v1);
        av_loader = findViewById(R.id.av);
        av1=findViewById(R.id.av1);
        back_lv=findViewById(R.id.back_lv);
        pack_recyclerView=findViewById(R.id.recyclerView);

        packsPartsList = new ArrayList<>();
        layout_one=findViewById(R.id.layout_one);
        layout_two=findViewById(R.id.layout_two);
        cartype_tv=findViewById(R.id.cartype_tv);
        carname=findViewById(R.id.carname);
        vinno_tv=findViewById(R.id.vinno_tv);
        act_mileageval_tv=findViewById(R.id.act_mileageval_tv);
        mileageval_tv=findViewById(R.id.mileageval_tv);
        location_val=findViewById(R.id.location_val);
        head_tv=findViewById(R.id.head_tv);
        head_tv=findViewById(R.id.head_tv);
        addcart_tv=findViewById(R.id.addcart_tv);
        previous_tv=findViewById(R.id.previous_tv);
        image1_iv = findViewById(R.id.image1_iv);
        comment_et=findViewById(R.id.comment_et);
        mileage_run_et=findViewById(R.id.mileage_run_et);
        carimg_iv=findViewById(R.id.carimg_iv);





        cameraDialog = new Dialog(OrderServicePack.this);
        cameraDialog.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);


        layout_one.setVisibility(View.VISIBLE);
        layout_two.setVisibility(View.GONE);

        str_carid=intent.getStringExtra("car_id_for_servicepack");
        str_carname=intent.getStringExtra("car_name_for_servicepack");
        str_vin=intent.getStringExtra("car_vinno_for_servicepack");
        str_mileage=intent.getStringExtra("car_mileagerange_for_servicepack");
        str_act_mileage=intent.getStringExtra("car_actualmilrage_for_servicepack");
        str_type=intent.getStringExtra("car_type_for_servicepack");
        str_image=intent.getStringExtra("car_image_for_servicepack");


        Glide.with(getApplicationContext())
                .load(str_image)
                .into(carimg_iv);



        sharedlogin = PreferenceManager.getDefaultSharedPreferences(OrderServicePack.this);
        editlogin = sharedlogin.edit();
        get_mail_from_reg = sharedlogin.getString("get_mail_from_reg", "");
        get_otp_from_reg=sharedlogin.getString("get_otp_from_reg","");
        get_apikey_from_login = sharedlogin.getString("get_apikey_from_login", "");
        get_email_from_login=sharedlogin.getString("get_email_from_login","");
        get_userid_from_login = sharedlogin.getString("get_userid_from_login", "");
        get_username_from_login=sharedlogin.getString("get_username_from_login","");


        if (str_act_mileage.equals("")) {
            act_mileageval_tv.setText("Not Mentioned");
        }


        carname.setText(str_carname);
        vinno_tv.setText(str_vin);
        act_mileageval_tv.setText(str_act_mileage);
        mileageval_tv.setText(str_mileage);
        location_val.setText("Ghana");

       /*Glide.with(getApplicationContext()).load(singleCarDetails.getCar_image())
                .into(carimg_iv);*/

/*
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        pack_recyclerView.setLayoutManager(manager);
        pack_recyclerView.setHasFixedSize(true);
        myPackAdapter = new MyPackAdapter(getApplicationContext(), packsPartsList);
        pack_recyclerView.setAdapter(myPackAdapter);*/


        if (Util.Operations.isOnline(OrderServicePack.this)) {
            //Cart List API calling
            new OrderParts_Async().execute();
        } else {
            Toasty.warning(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT, true).show();
        }





        back_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (layout_one.getVisibility() == View.VISIBLE) {
                    layout_two.setVisibility(View.INVISIBLE);

                    finish();
                    // Its visible
                } else if (layout_two.getVisibility() == View.VISIBLE) {
                    layout_two.setVisibility(View.GONE);
                    layout_one.setVisibility(View.VISIBLE);
                }


            }
        });


        previous_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_two.setVisibility(View.GONE);
                layout_one.setVisibility(View.VISIBLE);
            }
        });


        addcart_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("tag","11111111111--->"+getProductId);
                Log.e("tag","22222222222--->"+packname);
                Log.e("tag","33333333333--->"+str_carid);

                if(!TextUtils.isEmpty(mileage_run_et.getText()))
                {
                    if(!TextUtils.isEmpty(comment_et.getText()))
                    {
                        new Addrequest_AsyncTask().execute();
                    }
                    else
                    {
                        comment_et.requestFocus();
                        comment_et.setError( "Please Enter your Comment!" );
                    }
                }
                else
                {
                    mileage_run_et.requestFocus();
                    mileage_run_et.setError( "Enter Current Mileage" );
                }

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
            ActivityCompat.requestPermissions(OrderServicePack.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent back = new Intent(getApplicationContext(), OrderServicePack.class);
        startActivity(back);
        finish();
    }




    //Call Order Service Pack API CALL: ---------------------------------------------------------------------->

    public class OrderParts_Async extends AsyncTask<String, Void, String> {



        protected void onPreExecute() {
            super.onPreExecute();
            av_loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {

                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(Config.WEB_URL_GET_PACKS)
                        .get()
                        .addHeader("x-api-key", get_apikey_from_login)
                        .addHeader("cache-control", "no-cache")
                        .build();

                Response response = client.newCall(request).execute();

                JSONObject jsonObject = new JSONObject(response.body().string());

                String status = jsonObject.getString("status");

                if (status.equals("true"))
                {

                    JSONObject obj=jsonObject.getJSONObject("data");

                    JSONArray dataArray = obj.getJSONArray("servicePacks");
                    JSONObject dataObject = dataArray.getJSONObject(0);
                    JSONArray keys = dataObject.names();
                    packsPartsList = new ArrayList<>();

                    for (int i = 0; i < keys.length(); i++){
                        JSONArray elementArray = dataObject.getJSONArray(keys.getString(i));
                        List<ServicePackListPojo> servicePackSubList = new ArrayList<>();
                        for (int j = 0; j < elementArray.length(); j++){

                            JSONObject jsonObject1 = elementArray.getJSONObject(j);

                            Log.e("Output","value of i:"+i+" j:"+j+" - "+jsonObject1.toString());

                            String servicePackName = jsonObject1.getString("servicePackName");
                            String productId = jsonObject1.getString("productId");
                            String id = jsonObject1.getString("id");
                            String name = jsonObject1.getString("name");
                            String description = jsonObject1.getString("description");
                            String categoryId = jsonObject1.getString("categoryId");
                            String typeId = jsonObject1.getString("typeId");
                            String currentStock = jsonObject1.getString("currentStock");
                            String image = jsonObject1.getString("image");
                            String price = jsonObject1.getString("price");
                            String CategoryName = keys.getString(i);
                            ServicePackListPojo vehicleParts = new ServicePackListPojo(servicePackName,productId,id,name,description,categoryId,typeId,currentStock,image,price,CategoryName);
                            servicePackSubList.add(vehicleParts);

                        }

                        packsPartsList.add(servicePackSubList);

                    }


                }else {
                    return "false";
                }





            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
                return "error";
            }
            return "true";

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            av_loader.setVisibility(View.GONE);


            if (s.equals("true")){

                LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                pack_recyclerView.setLayoutManager(manager);
                pack_recyclerView.setHasFixedSize(true);
                myPackAdapter = new MyPackAdapter(getApplicationContext(), packsPartsList);
                pack_recyclerView.setAdapter(myPackAdapter);

            }
        }
    }

    //Login API CALL: ---------------------------------------------------------------------->
    public class Addrequest_AsyncTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            av1.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("vehicleId",str_carid);
                jsonObject.accumulate("productId",getProductId);
                jsonObject.accumulate("quantity","1");
                jsonObject.accumulate("currentMileage",mileage_run_et.getText().toString());
                jsonObject.accumulate("comment",comment_et.getText().toString());

                JSONArray array=new JSONArray();
                array.put(jsonObject);



                json = array.toString();
                return jsonStr = HttpUtils.makeRequestToken(Config.WEB_URL_ADD_REQUEST_ITEM+get_userid_from_login,get_apikey_from_login,json);



            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;



        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            av1.setVisibility(View.GONE);

            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");

                if (status.equals("true")) {
                    String msg=jo.getString("message");

                    final Dialog dialog = new Dialog(OrderServicePack.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.dialog_price_notification);
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);

                    Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/helevetical.ttf");

                    TextView ok_btn = dialog.findViewById(R.id.ok_tv);
                    TextView order_more_parts=(TextView)dialog.findViewById(R.id.order_more_parts) ;

                    ok_btn.setTypeface(tf);
                    order_more_parts.setTypeface(tf);

                    order_more_parts.setVisibility(View.GONE);



                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                            dialog.dismiss();
                        }
                    });
                    dialog.show();




                } else {
                    String msg=jo.getString("message");
                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
            }

        }
    }
}



