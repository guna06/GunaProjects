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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import autohubb.vinture.com.autohubb.R;
import autohubb.vinture.com.autohubb.activity.GetFilePathFromDevice;
import autohubb.vinture.com.autohubb.activity.MainActivity;
import autohubb.vinture.com.autohubb.fonts.FontsOverride;
import autohubb.vinture.com.autohubb.model.ProductConditionPojo;
import autohubb.vinture.com.autohubb.model.VehicleParts;
import autohubb.vinture.com.autohubb.orders.OrderPartsAdapter;
import autohubb.vinture.com.autohubb.utils.Config;
import autohubb.vinture.com.autohubb.utils.HttpUtils;
import es.dmoral.toasty.Toasty;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrderPartsOne extends Activity {

    ProductConditionPojo productConditionPojo;
    List<ProductConditionPojo> productConditionPojoList=new ArrayList<>();
    RadioGroup product_condition_group;
    LinearLayout back_lv,info_page;
    TextView carname,vinno_tv,act_mileageval_tv,mileageval_tv,cartype_tv,addcart_tv;
    EditText comment_tv,mileage_run_et;
    RecyclerView order_parts_recyclerview;
    OrderPartsAdapter orderPartsAdapter;
    List<List<VehicleParts>> vehiclePartsList;
    SharedPreferences sharedlogin;
    SharedPreferences.Editor editlogin;
    public String get_mail_from_reg, get_otp_from_reg, get_apikey_from_login, get_email_from_login, get_userid_from_login, get_username_from_login;
    public static LinearLayout linear_content1,linear_content2;
    AVLoadingIndicatorView av_loader,avi_3;
    TextView car_type;
    Intent intent;
    String selected_p_condition_id,str_carid,str_carname,str_vin,str_mileage,str_act_mileage,str_type;
    protected static final int TAKE_CAR_PIC1 = 1;
    protected static final int SELECT_CAR_PIC1 = 3;
    public static ImageView image1_iv;
    Dialog cameraDialog;
    LinearLayout lnr_takepic, lnr_takegallery;
    Bitmap bitmap;
    String selectedImagePath,path_carimg1;
    public static String getProductId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_parts_one);

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(OrderPartsOne.this, v1);

        intent=getIntent();
        str_carid=intent.getStringExtra("car_id_for_parts");
        str_carname=intent.getStringExtra("car_name_for_parts");
        str_vin=intent.getStringExtra("car_vinno_for_parts");
        str_mileage=intent.getStringExtra("car_mileagerange_for_parts");
        str_act_mileage=intent.getStringExtra("car_actualmilrage_for_parts");
        str_type=intent.getStringExtra("car_type_for_parts");


        sharedlogin = PreferenceManager.getDefaultSharedPreferences(OrderPartsOne.this);
        editlogin = sharedlogin.edit();
        get_mail_from_reg = sharedlogin.getString("get_mail_from_reg", "");
        get_otp_from_reg = sharedlogin.getString("get_otp_from_reg", "");
        get_apikey_from_login = sharedlogin.getString("get_apikey_from_login", "");
        get_email_from_login = sharedlogin.getString("get_email_from_login", "");
        get_userid_from_login = sharedlogin.getString("get_userid_from_login", "");
        get_username_from_login = sharedlogin.getString("get_username_from_login", "");


        back_lv=findViewById(R.id.back_lv);
        cartype_tv=findViewById(R.id.cartype_tv);
        carname=findViewById(R.id.carname);
        vinno_tv=findViewById(R.id.vinno_tv);
        act_mileageval_tv=findViewById(R.id.act_mileageval_tv);
        mileageval_tv=findViewById(R.id.mileageval_tv);
        order_parts_recyclerview=findViewById(R.id.order_parts_recyclerview);
        addcart_tv=findViewById(R.id.addcart_tv);
        car_type=findViewById(R.id.car_type);
        comment_tv=findViewById(R.id.comment_tv);
        mileage_run_et=findViewById(R.id.mileage_run_et);
        av_loader = findViewById(R.id.avi_2);
        info_page=findViewById(R.id.info_page);
        image1_iv = findViewById(R.id.image1_iv);
        linear_content1=findViewById(R.id.linear_content1);
        linear_content2=findViewById(R.id.linear_content2);
        product_condition_group=findViewById(R.id.product_condition_group);
        avi_3=findViewById(R.id.avi_3);

        cameraDialog = new Dialog(OrderPartsOne.this);
        cameraDialog.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);

        carname.setText(str_carname);
        vinno_tv.setText(str_vin);
        act_mileageval_tv.setText(str_act_mileage);
        mileageval_tv.setText(str_mileage);
        car_type.setText(str_type);

        vehiclePartsList = new ArrayList<>();

        linear_content1.setVisibility(View.VISIBLE);
        linear_content2.setVisibility(View.GONE);

        new OrderParts_Async().execute();
        new ProductCondition_Task().execute();

        info_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toasty.info(getApplicationContext(), "Under Development", Toast.LENGTH_SHORT, true).show();
            }
        });

        product_condition_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                selected_p_condition_id=productConditionPojoList.get(i).id;
            }
        });



        addcart_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selected_p_condition_id!=null)
                {
                    if(!TextUtils.isEmpty(mileage_run_et.getText()))
                    {
                        if(!TextUtils.isEmpty(comment_tv.getText()))
                        {
                            new Addrequest_AsyncTask().execute();
                        }
                        else
                        {
                            comment_tv.requestFocus();
                            comment_tv.setError( "Please Enter your Comment!");
                        }
                    }
                    else
                    {
                        mileage_run_et.requestFocus();
                        mileage_run_et.setError( "Enter Current Mileage" );
                    }
                }
                else {
                    Toasty.warning(getApplicationContext(),"Please Select Product Condition Id",Toasty.LENGTH_SHORT).show();
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


        back_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (linear_content1.getVisibility() == View.VISIBLE) {
                    linear_content2.setVisibility(View.INVISIBLE);
                    finish();
                    // Its visible
                } else if (linear_content2.getVisibility() == View.VISIBLE) {
                    linear_content2.setVisibility(View.GONE);
                    linear_content1.setVisibility(View.VISIBLE);
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
            ActivityCompat.requestPermissions(OrderPartsOne.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
            return false;
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

            if (!imagepath.isDirectory())
            {
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
            //upload1.setVisibility(View.GONE);
            path_carimg1=mediaFile4.getAbsolutePath();
            Log.e("tag","Take Car Path1"+path_carimg1);


            /*edit.putString("store_birdeyeview_path",path_birdeye_view);
            edit.commit();*/
        }




        //********************CAR PIC 9 -------SELECT
        else if (resultCode == RESULT_OK && requestCode == SELECT_CAR_PIC1){
            try {
                Uri selectedMediaUri = data.getData();
                path_carimg1 = GetFilePathFromDevice.getPath(getApplicationContext(), selectedMediaUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), selectedMediaUri);

                    image1_iv.setImageBitmap(bitmap);
                    Log.e("tag","Select Car Path1"+path_carimg1);

                    //upload1.setVisibility(View.GONE);
                    /*edit.putString("store_birdeyeview_path",path_birdeye_view);
                    edit.commit();*/
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {

            }
        }







    }

    //Order Parts API CALL: ---------------------------------------------------------------------->
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
                        .url(Config.WEB_URL_GET_ORDER_PARTS)
                        .get()
                        .addHeader("x-api-key", get_apikey_from_login)
                        .addHeader("cache-control", "no-cache")
                        .addHeader("Postman-Token", "f28bbaa1-a08a-4a02-affd-483fb7ac5891")
                        .build();

                Response response = client.newCall(request).execute();

                JSONObject jsonObject = new JSONObject(response.body().string());
                String status = jsonObject.getString("status");

                if (status.equals("true"))
                {

                    JSONObject obj=jsonObject.getJSONObject("data");

                    JSONArray dataArray = obj.getJSONArray("vehicleParts");
                    JSONObject dataObject = dataArray.getJSONObject(0);
                    JSONArray keys = dataObject.names();
                    vehiclePartsList = new ArrayList<>();

                    for (int i = 0; i < keys.length(); i++){
                        JSONArray elementArray = dataObject.getJSONArray(keys.getString(i));
                        List<VehicleParts> vehiclePartsSubList = new ArrayList<>();

                        for (int j = 0; j < elementArray.length(); j++){

                            JSONObject jsonObject1 = elementArray.getJSONObject(j);

                            Log.e("Output","value of i:"+i+" j:"+j+" - "+jsonObject1.toString());
                            String id = jsonObject1.getString("id");
                            String name = jsonObject1.getString("name");
                            String description = jsonObject1.getString("description");
                            String categoryId = jsonObject1.getString("categoryId");
                            String typeId = jsonObject1.getString("typeId");
                            String currentStock = jsonObject1.getString("currentStock");
                            String image = jsonObject1.getString("image");
                            String price = jsonObject1.getString("price");
                            String productCategory = jsonObject1.getString("productCategory");
                            String productType = jsonObject1.getString("productType");
                            String CategoryName = keys.getString(i);

                            VehicleParts vehicleParts = new VehicleParts(id, name,description,categoryId,typeId,currentStock,image,price,productCategory,productType, CategoryName);
                            vehiclePartsSubList.add(vehicleParts);
                        }
                        vehiclePartsList.add(vehiclePartsSubList);
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


            if (s.equals("true")){
                av_loader.setVisibility(View.GONE);
                LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                order_parts_recyclerview.setLayoutManager(manager);
                order_parts_recyclerview.setHasFixedSize(true);
                orderPartsAdapter = new OrderPartsAdapter(getApplicationContext(), vehiclePartsList);
                order_parts_recyclerview.setAdapter(orderPartsAdapter);

                orderPartsAdapter.notifyDataSetChanged();
            }
        }
    }

    public class Addrequest_AsyncTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            avi_3.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("vehicleId",str_carid);
                jsonObject.accumulate("productId",getProductId);
                jsonObject.accumulate("productConditionId",selected_p_condition_id);
                jsonObject.accumulate("quantity","1");
                jsonObject.accumulate("currentMileage",mileage_run_et.getText().toString());
                jsonObject.accumulate("comment",comment_tv.getText().toString());

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
            avi_3.setVisibility(View.GONE);

            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");

                if (status.equals("true")) {
                    String msg=jo.getString("message");

                    final Dialog dialog = new Dialog(OrderPartsOne.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.dialog_price_notification);
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);

                    Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/helevetical.ttf");

                    TextView ok_btn = dialog.findViewById(R.id.ok_tv);
                    TextView order_more_parts=(TextView)dialog.findViewById(R.id.order_more_parts) ;

                    ok_btn.setTypeface(tf);
                    order_more_parts.setTypeface(tf);


                    order_more_parts.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            Toasty.info(getApplicationContext(),"Under Development",Toasty.LENGTH_SHORT).show();

                        }
                    });



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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (linear_content1.getVisibility() == View.VISIBLE) {
            linear_content2.setVisibility(View.INVISIBLE);
            finish();
            // Its visible
        } else if (linear_content2.getVisibility() == View.VISIBLE) {
            linear_content2.setVisibility(View.GONE);
            linear_content1.setVisibility(View.VISIBLE);
        }
    }

    //Get Product Condition Id API CALL: ---------------------------------------------------------------------->
    public class ProductCondition_Task extends AsyncTask<String, Void, String> {


        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequestToken(Config.WEB_URL_GET_PRODUCT_CONDITION_ID,get_apikey_from_login);

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
           // avi.setVisibility(View.GONE);

            try{
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");

                if (status.equals("true")) {
                    String message=jo.getString("message");

                    JSONObject object=jo.getJSONObject("data");
                    JSONArray car_array=object.getJSONArray("productConditions");
                    if(car_array.length()==0)
                    {
                        // noparts_found.setVisibility(View.VISIBLE);
                    }
                    else {
                        // noparts_found.setVisibility(View.GONE);

                        for(int i=0;i<car_array.length();i++)
                        {
                            try {
                                productConditionPojo = new ProductConditionPojo();
                                JSONObject jsonObject = car_array.getJSONObject(i);
                                productConditionPojo.id=jsonObject.getString("id");
                                productConditionPojo.name=jsonObject.getString("name");

                                productConditionPojoList.add(productConditionPojo);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        for (int k = 0; k < productConditionPojoList.size();k++)
                        {

                            RadioButton btn = new RadioButton(getApplicationContext());
                            btn.setId(k);
                            final String itemName = productConditionPojoList.get(k).name;


                            btn.setText(itemName);
                            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                            product_condition_group.addView(btn, params);
                        }
                    }
                }
                else
                {

                }


            }
            catch (Exception e)
            {

            }

            // Setup and Handover data to recyclerview
            // shipping_recyclerview.setAdapter(shippingMethodAdapter);


        }
    }

}
