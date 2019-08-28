package com.nextinnovation.pt.barcodescanner.Sqmobiles;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StockTransferActivity extends Activity {

    LinearLayout backarrow;
    Spinner branch_spn1;
    LinearLayout scan;
    Button salestf_btn;
    ArrayList<String> branches_arraylist=new ArrayList<>();
    Map<String, String> branches_hash_id = new HashMap<>();
    Map<String, String> branches_hash_name = new HashMap<>();
    Map<String, String> branches_hash_location = new HashMap<>();
    Typeface regular,bold,extraBold,light,thin;
    CustomAdapterArrayList branchesAdapter;
    String id,branch_name,branch_location;
    SharedPreferences.Editor editor;
    String session_token,spinvalue,branchId,branchName,branchLocation,barcode_value;
    private final int MY_PERMISSION_REQUEST_CAMERA = 1001;
    String str_imei_id,brand_name,product_name,imei_id;
    TextView tv_details,head,tv_det;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stocktransfer);

        regular= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/ProximaNova-Regular.ttf");
        bold= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/ProximaNova-Bold.ttf");
        extraBold= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/ProximaNova-Extrabld.ttf");
        light= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/ProximaNova-Light.ttf");
        thin= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/ProximaNovaT-Thin.ttf");


        //token get from Login Activity:
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();
        session_token = sharedPreferences.getString("api_key", "");


        backarrow=findViewById(R.id.backarrow);
        branch_spn1=findViewById(R.id.branch_spn);
        scan=findViewById(R.id.scan);
        salestf_btn=findViewById(R.id.salestf_btn);
        tv_details=findViewById(R.id.tv_details);
        head=findViewById(R.id.head);
        tv_det=findViewById(R.id.tv_det);

        //new Call_Branches_Async_Task().execute();


        head.setTypeface(bold);
        tv_det.setTypeface(regular);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanBarcode();
            }
        });


        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),DashboardActivity.class);
                startActivity(i);
                finish();
            }
        });


        salestf_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new Stocktf_Async_Task().execute();




            }
        });

// Spinner Adapter:

            branch_spn1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    spinvalue = branch_spn1.getItemAtPosition(i).toString();
                    branchId=branches_hash_id.get(spinvalue);
                    branchName=branches_hash_name.get(spinvalue);
                    branchLocation=branches_hash_location.get(spinvalue);

                    Log.e("tag","Print---------"+ branchId+"  "+branchName+"  "+branchLocation);

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

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

            ActivityCompat.requestPermissions(StockTransferActivity.this,  new String[] {Manifest.permission.CAMERA}, MY_PERMISSION_REQUEST_CAMERA);

        } else{
            ActivityCompat.requestPermissions(StockTransferActivity.this,new String[] {Manifest.permission.CAMERA}, MY_PERMISSION_REQUEST_CAMERA);
        }
    }

    private void startScanningBarcode() {
        /**
         * Build a new MaterialBarcodeScanner
         */
        final MaterialBarcodeScanner materialBarcodeScanner = new MaterialBarcodeScannerBuilder()
                .withActivity(StockTransferActivity.this)
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




    //Scan API CALL: ---------------------------------------------------------------------->
    public class Scan_Async_Task extends AsyncTask<String, Void, String> {
        ProgressDialog dialog = new ProgressDialog(StockTransferActivity.this); // this = YourActivity


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
                        tv_details.setText("No Stock or Already Sold this IMEI No");
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




                        }


                        String message=jo.getString("message");
                        tv_details.setText("/n/n"+"Brand Name : "+brand_name+"/n/n"+"Product Name : "+product_name);
                        Log.e("tag","print message"+message);
                    }

                } else {


                    final Dialog dialog = new Dialog(StockTransferActivity.this);
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




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent j=new Intent(getApplicationContext(),DashboardActivity.class);
        startActivity(j);
        finish();
    }



    //Call Branches API CALL: ---------------------------------------------------------------------->
    public class Call_Branches_Async_Task extends AsyncTask<String, Void, String> {
        ProgressDialog dialog = new ProgressDialog(StockTransferActivity.this); // this = YourActivity


        protected void onPreExecute() {
            super.onPreExecute();

            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setTitle("Loading");
            dialog.setMessage("Please wait...");
            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("http://104.197.80.225/sqmobiles/public/api/mobile/auth/getBranches")
                        .get()
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
                    branches_arraylist = new ArrayList<>();

                    for(int j=0;j<array.length();j++)
                    {

                         JSONObject object=array.getJSONObject(j);

                         id=object.getString("id");
                         branch_name=object.getString("branch_name");
                         branch_location=object.getString("branch_location");


                         branches_arraylist.add(branch_name);



                        //get values using Hashmap:
                        branches_hash_id.put(object.getString("branch_name"),object.getString("id"));
                        branches_hash_name.put(object.getString("branch_name"),object.getString("branch_name"));
                        branches_hash_location.put(object.getString("branch_name"),object.getString("branch_location"));
                    }




                    branchesAdapter = new CustomAdapterArrayList(StockTransferActivity.this, R.layout.simple_dropdown_item_1line, branches_arraylist) {
                        @Override
                        public boolean isEnabled(int position) {
                            if (position == 0) {
                                return true;
                            } else {
                                return true;
                            }
                        }

                        @Override
                        public View getDropDownView(int position, View convertView,
                                                    ViewGroup parent) {
                            View view = super.getDropDownView(position, convertView, parent);
                            TextView tv = (TextView) view;
                            tv.setTypeface(regular);
                            tv.setTextSize(20);
                            tv.setPadding(30, 55, 10, 25);
                            if (position == 0) {
                                tv.setTextColor(Color.BLACK);
                            } else {
                                tv.setTextColor(Color.BLACK);
                            }
                            return view;
                        }


                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getDropDownView(position, convertView, parent);
                            TextView tv = (TextView) view;
                            tv.setTextSize(20);
                            tv.setPadding(10, 20, 0, 20);
                            tv.setTypeface(regular);
                            if (position == 0) {
                                tv.setTextColor(Color.BLACK);
                            } else {
                                tv.setTextColor(Color.BLACK);
                            }
                            return view;
                        }
                    };
                    branch_spn1.setAdapter(branchesAdapter);

                    String message=jo.getString("message");





                } else {

                    //String message = jo.getString("message");
                    Toast.makeText(getApplicationContext(),"message", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
            }

        }
    }





    //Stock Transfer API CALL: ---------------------------------------------------------------------->
    public class Stocktf_Async_Task extends AsyncTask<String, Void, String> {
        ProgressDialog dialog = new ProgressDialog(StockTransferActivity.this); // this = YourActivity


        protected void onPreExecute() {
            super.onPreExecute();

            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setTitle("Loading");
            dialog.setMessage("Loading. Please wait...");
            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            Log.e("tag","IMEI ID---------->"+str_imei_id);
            Log.e("tag","BRANCH ID---------->"+branchId);

            try {
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/json");

                RequestBody body1 = RequestBody.create(mediaType, "{\n\"imei_id\":\"" +str_imei_id
                        + "\",\n\"transfer_to\":\"" + branchId

                        + "\"\n}");

                Request request = new Request.Builder()
                        .url("http://104.197.80.225/sqmobiles/public/api/mobile/auth/transferStock")
                        .post(body1)
                        .addHeader("Accept", "application/json")
                        .addHeader("Authorization", "Bearer "+session_token)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("User-Agent", "PostmanRuntime/7.15.2")
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


                    JSONObject object=jo.getJSONObject("data");
                    String msg=object.getString("message");


                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();

                    Intent verify=new Intent(getApplicationContext(),DashboardActivity.class);
                    startActivity(verify);
                    finish();

                } else {

                    //String message = jo.getString("message");
                    Toast.makeText(getApplicationContext(),"message", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
            }

        }
    }



}