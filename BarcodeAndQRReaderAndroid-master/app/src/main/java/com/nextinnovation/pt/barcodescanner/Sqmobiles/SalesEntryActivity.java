package com.nextinnovation.pt.barcodescanner.Sqmobiles;
import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScanner;
import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScannerBuilder;
import com.google.android.gms.vision.barcode.Barcode;
import com.nextinnovation.pt.barcodescanner.R;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;


import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;




public class SalesEntryActivity extends Activity {
    TextView head,scan,tv_imeino,tv_brand,tv_product;
    Typeface regular,bold,extraBold,light,thin;
    EditText edittext_invoice;
    Button salestf_btn;
    LinearLayout backarrow;
    private final int MY_PERMISSION_REQUEST_CAMERA = 1001;
    private Barcode barcodeResult;
    String barcode_value,imei_id,brand_name,product_name,invoiceno;
    SharedPreferences.Editor editor;
    String session_token;
    TextView tv_det;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salesentry);

        regular= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/ProximaNova-Regular.ttf");
        bold= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/ProximaNova-Bold.ttf");
        extraBold= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/ProximaNova-Extrabld.ttf");
        light= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/ProximaNova-Light.ttf");
        thin= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/ProximaNovaT-Thin.ttf");



        //token get from Login Activity:
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();
        session_token = sharedPreferences.getString("api_key", "");

        head=findViewById(R.id.head);
        salestf_btn=findViewById(R.id.salestf_btn);
        edittext_invoice=findViewById(R.id.edittext_invoice);
        tv_imeino=findViewById(R.id.tv_imeino);
        tv_brand=findViewById(R.id.tv_brand);
        tv_product=findViewById(R.id.tv_product);
        salestf_btn=findViewById(R.id.salestf_btn);
        scan=findViewById(R.id.scan);
        backarrow=findViewById(R.id.backarrow);
        tv_det=findViewById(R.id.tv_det);

        head.setTypeface(extraBold);
        salestf_btn.setTypeface(regular);
        edittext_invoice.setTypeface(regular);
        tv_det.setTypeface(regular);

        tv_imeino.setTypeface(regular);
        tv_brand.setTypeface(regular);
        tv_product.setTypeface(regular);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanBarcode();
            }
        });




        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back=new Intent(getApplicationContext(),DashboardActivity.class);
                startActivity(back);
                finish();
            }
        });



        salestf_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invoiceno=edittext_invoice.getText().toString().trim();

                if (!TextUtils.isEmpty(tv_imeino.getText().toString())) {
                    if (!TextUtils.isEmpty(edittext_invoice.getText().toString())) {

                        new SalesEntry_Async_Task().execute();


                    } else {
                        edittext_invoice.requestFocus();
                        edittext_invoice.setError("Invoice No is Required!");
                    }
                } else {

                    Toast.makeText(getApplicationContext(),"Please Scan the Input", Toast.LENGTH_LONG).show();

                }
            }
        });


    }

    private void scanBarcode() {
        checkPermission();
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            //Log.d(TAG , getResources().getString(R.string.camera_permission_granted));
            startScanningBarcode();
        } else {
            requestCameraPermission();

        }
    }


    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {

            ActivityCompat.requestPermissions(SalesEntryActivity.this,  new String[] {Manifest.permission.CAMERA}, MY_PERMISSION_REQUEST_CAMERA);

        } else{
            ActivityCompat.requestPermissions(SalesEntryActivity.this,new String[] {Manifest.permission.CAMERA}, MY_PERMISSION_REQUEST_CAMERA);
        }
    }

    private void startScanningBarcode() {
        /**
         * Build a new MaterialBarcodeScanner
         */
        final MaterialBarcodeScanner materialBarcodeScanner = new MaterialBarcodeScannerBuilder()
                .withActivity(SalesEntryActivity.this)
                .withEnableAutoFocus(true)
                .withBleepEnabled(true)
                .withBackfacingCamera()
                .withCenterTracker()
                .withText("Scanning...")
                .withResultListener(new MaterialBarcodeScanner.OnResultListener() {
                    @Override
                    public void onResult(Barcode barcode) {

                        barcode_value=barcode.rawValue;

                        Log.e("tag","print barcode value-------->"+barcode.rawValue);
                        new Scan_Async_Task().execute();

                        /*barcodeResult = barcode;
                        showDialog(barcode.rawValue , getScanTime(),getScanDate());*/
                    }
                })
                .build();
        materialBarcodeScanner.startScan();
    }

    private void showDialog(String scanContent, String scanTime, String scanDate) {

        AlertDialog.Builder builder = new AlertDialog.Builder(SalesEntryActivity.this);

        builder.setMessage(scanContent)
                .setTitle(R.string.dialog_title);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
               /* DatabaseHelper databaseHelper = new DatabaseHelper(context);
                databaseHelper.addProduct(new Product(scanContent,currentTime,currentDate));
                Toast.makeText(MainActivity1.this, "Saved", Toast.LENGTH_SHORT).show();
                viewPager.setCurrentItem(1);*/


            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
               // Toast.makeText(MainActivity1.this, "Not Saved", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==MY_PERMISSION_REQUEST_CAMERA && grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startScanningBarcode();
        } else {
            Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.sorry_for_not_permission), Snackbar.LENGTH_SHORT)
                    .show();
        }

    }


    public String getScanTime() {
        DateFormat timeFormat = new SimpleDateFormat("hh:mm a" , Locale.getDefault());
        return  timeFormat.format(new Date());
    }

    public String getScanDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault());
        return dateFormat.format(new Date());
    }




    //Scan API CALL: ---------------------------------------------------------------------->
    public class Scan_Async_Task extends AsyncTask<String, Void, String> {
        ProgressDialog dialog = new ProgressDialog(SalesEntryActivity.this); // this = YourActivity


        protected void onPreExecute() {
            super.onPreExecute();

            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setTitle("SQMobiles Alert!!!");
            dialog.setMessage("Scanning... Please wait...");
            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body1 = RequestBody.create(mediaType, "{\n\"imei\":\"" +barcode_value
                        + "\"\n}");
                Request request = new Request.Builder()
                        .url("http://104.197.80.225/sqmobiles/public/api/mobile/auth/getProductDetailByImei")
                        .post(body1)
                        .addHeader("Accept", "application/json")
                        .addHeader("Authorization", "Bearer "+session_token)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Cache-Control", "no-cache")
                        .build();

                Response response = client.newCall(request).execute();
                return response.body().string();


                // Log.e("tag","print -----"+response.toString());

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();


            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("success");

                if (status.equals("true")) {

                    JSONArray array=jo.getJSONArray("data");
                    Log.e("tag","print length------->"+array.length());

                    if(array.length()==0)
                    {
                        Log.e("tag","print 1------->");
                        tv_brand.setText("No Stock or Already Sold this IMEI No");
                    }
                    else
                    {
                        Log.e("tag","print 2------->");
                        for(int i=0;i<array.length();i++)
                        {
                            JSONObject object=array.getJSONObject(i);

                            imei_id=object.getString("imei_id");
                            brand_name=object.getString("brand_name");
                            product_name=object.getString("product_name");

                            Log.e("tag","Print Value IMEI ID"+imei_id);


                        }


                        String message=jo.getString("message");
                        tv_imeino.setText("IMEI No: "+barcode_value);
                        tv_brand.setText("Brand Name: "+brand_name);
                        tv_product.setText("Product Name: "+product_name);
                        //tv_details.setText("\n\n"+barcode_value+"\n\n"+"Brand Name : "+brand_name+"\n\n"+"Product Name : "+product_name);
                        Log.e("tag","print message"+message);
                    }

                } else {


                    final Dialog dialog = new Dialog(SalesEntryActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.dialog_alert);
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);

                    Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/ProximaNova-Regular.ttf");

                    Button ok_btn = (Button) dialog.findViewById(R.id.ok_btn);
                    TextView tv=(TextView)dialog.findViewById(R.id.tv_message) ;

                    tv.setText("Session out.. Please Login!!");

                    ok_btn.setTypeface(tf);
                    tv.setTypeface(tf);



                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();

                }
            } catch (Exception e) {
            }

        }
    }



    //Sales Entry API CALL: ---------------------------------------------------------------------->
    public class SalesEntry_Async_Task extends AsyncTask<String, Void, String> {
        ProgressDialog dialog = new ProgressDialog(SalesEntryActivity.this); // this = YourActivity


        protected void onPreExecute() {
            super.onPreExecute();

            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setTitle("SQMobiles Alert");
            dialog.setMessage("Loading... Please Wait...");
            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {
                OkHttpClient client = new OkHttpClient();



                MediaType mediaType = MediaType.parse("application/json");

                RequestBody body1 = RequestBody.create(mediaType, "{\n\"imei_id\":\"" +imei_id
                        + "\",\n\"invoice_number\":\"" + invoiceno

                        + "\"\n}");


                Request request = new Request.Builder()
                        .url("http://mobiles.sqindia.net/api/mobile/auth/getProductDetailByImei")
                        .post(body1)
                        .addHeader("Accept", "application/json")
                        .addHeader("Authorization", "Bearer "+session_token)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("cache-control", "no-cache")
                        .addHeader("Postman-Token", "443535f1-4bd4-44fc-ab44-32056823aca2")
                        .build();

                Response response = client.newCall(request).execute();
                return response.body().string();


                // Log.e("tag","print -----"+response.toString());

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();


            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("success");

                if (status.equals("true")) {


                    String message=jo.getString("message");

                    final Dialog dialog = new Dialog(SalesEntryActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.dialog_alert);
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);

                    Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/ProximaNova-Regular.ttf");

                    Button ok_btn = (Button) dialog.findViewById(R.id.ok_btn);
                    TextView tv_message=(TextView)dialog.findViewById(R.id.tv_message) ;
                    tv_message.setText(message);

                    ok_btn.setTypeface(tf);
                    tv_message.setTypeface(tf);



                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent verify=new Intent(getApplicationContext(),DashboardActivity.class);
                            startActivity(verify);
                            finish();
                        }
                    });

                    dialog.show();



                } else {

                    //String message = jo.getString("message");
                    Toast.makeText(getApplicationContext(),"message", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
            }

        }
    }

}
