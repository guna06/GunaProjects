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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScanner;
import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScannerBuilder;
import com.google.android.gms.vision.barcode.Barcode;
import com.nextinnovation.pt.barcodescanner.R;
import com.nextinnovation.pt.barcodescanner.Sqmobiles.utils.Config;
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

public class MainPageScanActivity extends Activity {
    LinearLayout backarrow,scan_product_lnr,stockentry,salesentry,stocktransfer,stockreceive_data;
    ImageView scan,typing_img;
    TextView okay_tv,accessdenied_tv,tv0,tv1,tv2,tv,scan_tv,head,tv_det,tv_brand,tv_product,stockentry_tv,salesentrytv,stocktf_tv,stockreceive_tv;
    Button saleentry_btn,stocktf_btn,stockentry_btn,stockreceive_btn;
    Typeface regular,bold,extraBold,light,thin,quondo;
    private final int MY_PERMISSION_REQUEST_CAMERA = 1001;
    String barcode_value,imei_id,brand_name,product_name,session_token,invoiceno,navdashboard;
    SharedPreferences.Editor editor;
    EditText edittext_invoice,tv_imeino;
    LinearLayout stockentry_data,salesentry_data,stocktf_data,access_denied,stockreceive;
    Spinner branch_spn1,branch0_spn,brand_spn,product_spn;
    ArrayList<String> product_arraylist=new ArrayList<>();
    ArrayList<String> brand_arraylist=new ArrayList<>();
    ArrayList<String> branches_arraylist=new ArrayList<>();
    ArrayList<String> branches_arraylist0=new ArrayList<>();
    Map<String, String> branches_hash_id = new HashMap<>();
    Map<String, String> branches_hash_id1 = new HashMap<>();
    Map<String, String> brand_hash_id = new HashMap<>();
    Map<String, String> product_hash_id = new HashMap<>();
    String session_email,getId,productValue,brand_id,id,branch_name,brand,spinvalue,branchId,brandId,productId,productName,str_imei_id,spinvalue1,branchId1,brandValue;
    String userPress="";
    Intent i;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpagescan);


        regular= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/ProximaNova-Regular.ttf");
        bold= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/ProximaNova-Bold.ttf");
        extraBold= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/ProximaNova-Extrabld.ttf");
        light= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/ProximaNova-Light.ttf");
        thin= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/ProximaNovaT-Thin.ttf");
        quondo= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/Realistica Demo.otf");




        //token get from Login Activity:
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();
        session_token = sharedPreferences.getString("api_key", "");
        session_email=sharedPreferences.getString("user_role","");
        extras = getIntent().getExtras();




        backarrow=findViewById(R.id.backarrow);
        scan=findViewById(R.id.scan);
        scan_product_lnr=findViewById(R.id.scan_product_lnr);
        scan_tv=findViewById(R.id.scan_tv);
        scan.setBackgroundResource(R.drawable.barcode_scanner);
        saleentry_btn=findViewById(R.id.saleentry_btn);
        head=findViewById(R.id.head);
        tv_det=findViewById(R.id.tv_det);
        tv_imeino=findViewById(R.id.tv_imeino);
        tv_brand=findViewById(R.id.tv_brand);
        tv_product=findViewById(R.id.tv_product);
        branch0_spn=findViewById(R.id.branch0_spn);
        product_spn=findViewById(R.id.product_spn);
        stockentry_btn=findViewById(R.id.stockentry_btn);
        stockreceive=findViewById(R.id.stockreceive);
        stockreceive_tv=findViewById(R.id.stockreceive_tv);
        stockreceive_data=findViewById(R.id.stockreceive_data);
        typing_img=findViewById(R.id.typing_img);
        okay_tv=findViewById(R.id.okay_tv);

        stockentry=findViewById(R.id.stockentry);
        stockentry_tv=findViewById(R.id.stockentry_tv);
        salesentry=findViewById(R.id.salesentry);
        salesentrytv=findViewById(R.id.salesentrytv);
        stocktransfer=findViewById(R.id.stocktransfer);
        stocktf_tv=findViewById(R.id.stocktf_tv);
        edittext_invoice=findViewById(R.id.edittext_invoice);
        stockentry_data=findViewById(R.id.stockentry_data);
        salesentry_data=findViewById(R.id.salesentry_data);
        stocktf_data=findViewById(R.id.stocktf_data);
        stocktf_btn=findViewById(R.id.stocktf_btn);
        branch_spn1=findViewById(R.id.branch1_spn);
        brand_spn=findViewById(R.id.brand_spn);
        access_denied=findViewById(R.id.access_denied);
        tv=findViewById(R.id.tv);
        accessdenied_tv=findViewById(R.id.accessdenied_tv);
        stockreceive_btn=findViewById(R.id.stockreceive_btn);

        tv0=findViewById(R.id.tv0);
        tv1=findViewById(R.id.tv1);
        tv2=findViewById(R.id.tv2);

        head.setTypeface(bold);
        scan_tv.setTypeface(regular);
        tv_det.setTypeface(regular);
        tv_imeino.setTypeface(regular);
        tv_brand.setTypeface(regular);
        tv_product.setTypeface(regular);
        saleentry_btn.setTypeface(regular);
        stockentry_tv.setTypeface(regular);
        salesentrytv.setTypeface(regular);
        stocktf_tv.setTypeface(regular);
        edittext_invoice.setTypeface(regular);
        stocktf_btn.setTypeface(regular);
        stockentry_btn.setTypeface(regular);
        tv.setTypeface(thin);
        tv0.setTypeface(thin);
        tv1.setTypeface(thin);
        tv2.setTypeface(thin);
        accessdenied_tv.setTypeface(regular);
        stockreceive_btn.setTypeface(regular);
        okay_tv.setTypeface(regular);

        stockentry.setBackgroundResource(R.drawable.button_bg);
        stockentry_tv.setTextColor(Color.parseColor("#FFFFFF"));
        salesentry.setBackgroundResource(R.drawable.white_curve_bg);
        salesentrytv.setTextColor(Color.parseColor("#000000"));
        stocktransfer.setBackgroundResource(R.drawable.white_curve_bg);
        stocktf_tv.setTextColor(Color.parseColor("#000000"));
        stockreceive.setBackgroundResource(R.drawable.white_curve_bg);
        stockreceive_tv.setTextColor(Color.parseColor("#000000"));
        tv_imeino.setEnabled(false);


        i=getIntent();
        String getIMEi=i.getStringExtra("Received IMEI");
        Log.e("tag","123456--------------------->"+getIMEi);

        try {

            if(!getIMEi.equals(""))
            {
                tv_imeino.setText(getIMEi);
            }


        }catch(Exception e)
        {

        }


        if(session_email.equals("stockuser"))
        {
            stockentry_data.setVisibility(View.VISIBLE);
            salesentry_data.setVisibility(View.GONE);
            stocktf_data.setVisibility(View.GONE);
            access_denied.setVisibility(View.GONE);
            stockreceive_data.setVisibility(View.GONE);
        }
        else
        {
            stockentry_data.setVisibility(View.GONE);
            salesentry_data.setVisibility(View.GONE);
            stocktf_data.setVisibility(View.GONE);
            access_denied.setVisibility(View.VISIBLE);
            stockreceive_data.setVisibility(View.GONE);
        }


        //services calling.................
        new Call_BranchesForAddStock_Async_Task1().execute();
        new Call_Branches_Async_Task().execute();

        userPress="false";



        stockentry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPress="false";

                if(!tv_imeino.getText().toString().trim().equals(""))
                {
                    new Scan_Async_Task().execute();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"IMEI No is Empty! Please scan the IMEI No",Toast.LENGTH_LONG).show();
                }



                stockentry.setBackgroundResource(R.drawable.button_bg);
                stockentry_tv.setTextColor(Color.parseColor("#FFFFFF"));
                salesentry.setBackgroundResource(R.drawable.white_curve_bg);
                salesentrytv.setTextColor(Color.parseColor("#000000"));
                stocktransfer.setBackgroundResource(R.drawable.white_curve_bg);
                stocktf_tv.setTextColor(Color.parseColor("#000000"));
                stockreceive.setBackgroundResource(R.drawable.white_curve_bg);
                stockreceive_tv.setTextColor(Color.parseColor("#000000"));


                if(session_email.equals("stockuser"))
                {
                    stockentry_data.setVisibility(View.VISIBLE);
                    salesentry_data.setVisibility(View.GONE);
                    stocktf_data.setVisibility(View.GONE);
                    access_denied.setVisibility(View.GONE);
                    stockreceive_data.setVisibility(View.GONE);
                }
                else
                {
                    stockentry_data.setVisibility(View.GONE);
                    salesentry_data.setVisibility(View.GONE);
                    stocktf_data.setVisibility(View.GONE);
                    access_denied.setVisibility(View.VISIBLE);
                    stockreceive_data.setVisibility(View.GONE);
                }
            }
        });



        tv_imeino.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // do your stuff here
                    userPress="true";
                    if(!tv_imeino.getText().toString().trim().equals(""))
                    {
                        new Scan_Async_Task().execute();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"IMEI No is Empty! Please scan the IMEI No",Toast.LENGTH_LONG).show();
                    }

                }
                return false;
            }
        });


        okay_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPress="true";
                if(!tv_imeino.getText().toString().trim().equals(""))
                {
                    new Scan_Async_Task().execute();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"IMEI No is Empty! Please scan the IMEI No",Toast.LENGTH_LONG).show();
                }

            }
        });


        typing_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_imeino.setEnabled(true);
                tv_imeino.setText("");
                tv_imeino.setHint("Type IMEI No here");



            }
        });

        salesentry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userPress="true";

                if(!tv_imeino.getText().toString().trim().equals(""))
                {
                    new Scan_Async_Task().execute();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"IMEI No is Empty! Please scan the IMEI No",Toast.LENGTH_LONG).show();
                }

                stockentry.setBackgroundResource(R.drawable.white_curve_bg);
                stockentry_tv.setTextColor(Color.parseColor("#000000"));
                salesentry.setBackgroundResource(R.drawable.button_bg);
                salesentrytv.setTextColor(Color.parseColor("#FFFFFF"));
                stocktransfer.setBackgroundResource(R.drawable.white_curve_bg);
                stocktf_tv.setTextColor(Color.parseColor("#000000"));
                stockreceive.setBackgroundResource(R.drawable.white_curve_bg);
                stockreceive_tv.setTextColor(Color.parseColor("#000000"));

                stockentry_data.setVisibility(View.GONE);
                stocktf_data.setVisibility(View.GONE);
                access_denied.setVisibility(View.GONE);
                salesentry_data.setVisibility(View.VISIBLE);
                stockreceive_data.setVisibility(View.GONE);
            }
        });


        stocktransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                userPress="true";

                if(!tv_imeino.getText().toString().trim().equals(""))
                {
                    new Scan_Async_Task().execute();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"IMEI No is Empty! Please scan the IMEI No",Toast.LENGTH_LONG).show();
                }

                stockentry.setBackgroundResource(R.drawable.white_curve_bg);
                stockentry_tv.setTextColor(Color.parseColor("#000000"));
                salesentry.setBackgroundResource(R.drawable.white_curve_bg);
                salesentrytv.setTextColor(Color.parseColor("#000000"));
                stocktransfer.setBackgroundResource(R.drawable.button_bg);
                stocktf_tv.setTextColor(Color.parseColor("#FFFFFF"));
                stockreceive.setBackgroundResource(R.drawable.white_curve_bg);
                stockreceive_tv.setTextColor(Color.parseColor("#000000"));



                access_denied.setVisibility(View.GONE);
                stockentry_data.setVisibility(View.GONE);
                salesentry_data.setVisibility(View.GONE);
                stocktf_data.setVisibility(View.VISIBLE);
                stockreceive_data.setVisibility(View.GONE);
            }
        });





        stockreceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userPress="true";

                try {
                    if(!tv_imeino.getText().toString().trim().equals(""))
                    {
                        new Scan_Async_Task().execute();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"IMEI No is Empty! Please scan the IMEI No",Toast.LENGTH_LONG).show();
                    }
                    stockentry.setBackgroundResource(R.drawable.white_curve_bg);
                    stockentry_tv.setTextColor(Color.parseColor("#000000"));
                    salesentry.setBackgroundResource(R.drawable.white_curve_bg);
                    salesentrytv.setTextColor(Color.parseColor("#000000"));
                    stocktransfer.setBackgroundResource(R.drawable.white_curve_bg);
                    stocktf_tv.setTextColor(Color.parseColor("#000000"));
                    stockreceive.setBackgroundResource(R.drawable.button_bg);
                    stockreceive_tv.setTextColor(Color.parseColor("#FFFFFF"));



                    access_denied.setVisibility(View.GONE);
                    stockentry_data.setVisibility(View.GONE);
                    salesentry_data.setVisibility(View.GONE);
                    stocktf_data.setVisibility(View.GONE);
                    stockreceive_data.setVisibility(View.VISIBLE);

                }catch(Exception e)
                {

                }




            }
        });

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (extras != null) {
                    navdashboard = extras.getString("stock_pagae");
                    if (navdashboard.equals("STOCK")) {
                        Intent back=new Intent(getApplicationContext(),ReceiveProducts.class);
                        startActivity(back);
                        finish();
                    }
                    else
                    {
                        Intent i=new Intent(getApplicationContext(),DashboardActivity1.class);
                        startActivity(i);
                        finish();
                    }
                } else {
                    Intent i=new Intent(getApplicationContext(),DashboardActivity1.class);
                    startActivity(i);
                    finish();
                }

            }
        });



        scan_product_lnr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_imeino.setText("");
                tv_imeino.setEnabled(false);
                tv_imeino.setHint("");
                scan_product_lnr.setBackgroundResource(R.drawable.fill_circle_round_bg);
                scan.setBackgroundResource(R.drawable.barcode_scanner_white);
                scan_tv.setTextColor(Color.parseColor("#FFFFFF"));
                scanBarcode();

            }
        });





        saleentry_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(tv_imeino.getText().toString())) {

                    if(!tv_imeino.getText().toString().equals("No Stock or Already Sold this IMEI No"))
                    {
                        if (!TextUtils.isEmpty(edittext_invoice.getText().toString())) {


                            if(!imei_id.equals(""))
                            {
                                invoiceno=edittext_invoice.getText().toString().trim();
                                new SalesEntry_Async_Task().execute();

                            }else
                            {
                                final Dialog dialog = new Dialog(MainPageScanActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.dialog_alert);
                                dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);

                                Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/ProximaNova-Regular.ttf");

                                Button ok_btn = (Button) dialog.findViewById(R.id.ok_btn);
                                TextView tv=(TextView)dialog.findViewById(R.id.tv_message) ;

                                tv.setText("Can't Receive. This IMEI no not Available");

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


                        } else {
                            edittext_invoice.requestFocus();
                            edittext_invoice.setError("Invoice No is Required!");
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"No Stock Available",Toast.LENGTH_LONG).show();

                    }

                } else {

                    Toast.makeText(getApplicationContext(),"Please Scan the Input", Toast.LENGTH_LONG).show();

                }
            }
        });



        stockreceive_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(tv_imeino.getText().toString())) {

                    if(!imei_id.equals(""))
                    {
                        new StockReceive().execute();

                    }else
                    {
                        final Dialog dialog = new Dialog(MainPageScanActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.dialog_alert);
                        dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);

                        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/ProximaNova-Regular.ttf");

                        Button ok_btn = (Button) dialog.findViewById(R.id.ok_btn);
                        TextView tv=(TextView)dialog.findViewById(R.id.tv_message) ;

                        tv.setText("Can't Receive. This IMEI no not Available");

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
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please Scan the Input", Toast.LENGTH_LONG).show();
                }
            }
        });




        branch_spn1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinvalue = branch_spn1.getItemAtPosition(i).toString();
                branchId=branches_hash_id.get(spinvalue);
                //branchName=branches_hash_name.get(spinvalue);
                //branchLocation=branches_hash_location.get(spinvalue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });





        branch0_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinvalue1 = branch0_spn.getItemAtPosition(i).toString();
                branchId1=branches_hash_id1.get(spinvalue1);
                //branchName=branches_hash_name.get(spinvalue);
                //branchLocation=branches_hash_location.get(spinvalue);





                if(!branch0_spn.getItemAtPosition(i).toString().equals("Select Branch"))
                {
                    new Call_Brand_Async_Task().execute();
                }
                else
                {

                }




            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });








        brand_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int j, long l) {


                brandValue=brand_spn.getItemAtPosition(j).toString();
                brandId=brand_hash_id.get(brandValue);

                Log.e("tag","AADHAV--------------->"+brandValue+brandId);


                if(!brand_spn.getItemAtPosition(j).toString().equals("Select Brand"))
                {
                    new Call_Product_Async_Task().execute();
                }
                else
                {

                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        product_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                productValue=product_spn.getItemAtPosition(i).toString();
                getId=product_hash_id.get(productValue);



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        stocktf_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!TextUtils.isEmpty(tv_imeino.getText().toString())) {

                    if(!tv_imeino.getText().toString().equals("No Stock or Already Sold this IMEI No"))
                    {
                        if (!spinvalue.equals("Select Branch")) {


                            if(!imei_id.equals(""))
                            {
                                new Stocktf_Async_Task().execute();

                            }else
                            {
                                final Dialog dialog = new Dialog(MainPageScanActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.dialog_alert);
                                dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);

                                Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/ProximaNova-Regular.ttf");

                                Button ok_btn = (Button) dialog.findViewById(R.id.ok_btn);
                                TextView tv=(TextView)dialog.findViewById(R.id.tv_message) ;

                                tv.setText("Can't Receive. This IMEI no not Available");

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



                        } else {
                            Toast.makeText(getApplicationContext(),"Please Select Branch",Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"No Stock Available",Toast.LENGTH_LONG).show();

                    }

                } else {

                    Toast.makeText(getApplicationContext(),"Please Scan the Input", Toast.LENGTH_LONG).show();

                }





            }
        });




        stockentry_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!TextUtils.isEmpty(tv_imeino.getText().toString())) {

                    if(!tv_imeino.getText().toString().equals("No Stock or Already Sold this IMEI No"))
                    {
                        if (!spinvalue1.equals("Select Branch")) {

                            if (!brandValue.equals("Select Brand")) {


                                if (!productValue.equals("Select Product")) {


                                    if (!productValue.equals("Select Product")) {


                                        if(!imei_id.equals(""))
                                        {
                                            new AddStock_Async_Task().execute();

                                        }else
                                        {
                                            final Dialog dialog = new Dialog(MainPageScanActivity.this);
                                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                            dialog.setCancelable(false);
                                            dialog.setContentView(R.layout.dialog_alert);
                                            dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);

                                            Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/ProximaNova-Regular.ttf");

                                            Button ok_btn = (Button) dialog.findViewById(R.id.ok_btn);
                                            TextView tv=(TextView)dialog.findViewById(R.id.tv_message) ;

                                            tv.setText("Can't Receive. This IMEI no not Available");

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



                                    } else {
                                        Toast.makeText(getApplicationContext(), "Please Select Product", Toast.LENGTH_LONG).show();
                                    }



                                } else {
                                    Toast.makeText(getApplicationContext(), "Please Select Product", Toast.LENGTH_LONG).show();
                                }

                            } else {
                                Toast.makeText(getApplicationContext(), "Please Select Brand", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Please Select Branch", Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"No Stock Available",Toast.LENGTH_LONG).show();

                    }


                }else
                {
                    Toast.makeText(getApplicationContext(),"Please Scan the Input", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (extras != null) {
            navdashboard = extras.getString("stock_pagae");
            if (navdashboard.equals("STOCK")) {
                Intent back=new Intent(getApplicationContext(),ReceiveProducts.class);
                startActivity(back);
                finish();
            }
            else
            {
                Intent i=new Intent(getApplicationContext(),DashboardActivity1.class);
                startActivity(i);
                finish();
            }
        } else {
            Intent i=new Intent(getApplicationContext(),DashboardActivity1.class);
            startActivity(i);
            finish();
        }
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

            ActivityCompat.requestPermissions(MainPageScanActivity.this,  new String[] {Manifest.permission.CAMERA}, MY_PERMISSION_REQUEST_CAMERA);

        } else{
            ActivityCompat.requestPermissions(MainPageScanActivity.this,new String[] {Manifest.permission.CAMERA}, MY_PERMISSION_REQUEST_CAMERA);
        }
    }

    private void startScanningBarcode() {
        /**
         * Build a new MaterialBarcodeScanner
         */
        final MaterialBarcodeScanner materialBarcodeScanner = new MaterialBarcodeScannerBuilder()
                .withActivity(MainPageScanActivity.this)
                .withEnableAutoFocus(true)
                .withBleepEnabled(true)
                .withBackfacingCamera()
                .withCenterTracker()
                .withText("Scanning...")
                .withResultListener(new MaterialBarcodeScanner.OnResultListener() {
                    @Override
                    public void onResult(Barcode barcode) {

                        imei_id="";
                        barcode_value=barcode.rawValue;
                        tv_imeino.setText(barcode_value);

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

        String imeiValue=tv_imeino.getText().toString().trim();

        ProgressDialog dialog = new ProgressDialog(MainPageScanActivity.this); // this = YourActivity


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

                RequestBody body1 = RequestBody.create(mediaType, "{\n\"imei\":\"" +imeiValue
                        + "\"\n}");
                Request request = new Request.Builder()
                        .url(Config.currentpath+"getProductDetailByImei")
                        .post(body1)
                        .addHeader("Accept", "application/json")
                        .addHeader("Authorization", "Bearer "+session_token)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Cache-Control", "no-cache")
                        .build();

                Response response = client.newCall(request).execute();
                return response.body().string();


            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            Log.e("tag","Print Value-------------------->"+userPress);

            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("success");

                if (status.equals("true")) {

                    JSONArray array=jo.getJSONArray("data");


                    if(array.length()==0)
                    {


                        Log.e("tag","01---------->");
                        if(userPress.equals("false"))
                        {
                            Log.e("tag","02---------->");
                            tv_brand.setVisibility(View.INVISIBLE);
                            tv_product.setVisibility(View.INVISIBLE);
                        }
                        else if(userPress.equals("true")){
                            String message = jo.getString("message");
                            tv_brand.setVisibility(View.VISIBLE);
                            tv_product.setVisibility(View.INVISIBLE);
                            Log.e("tag","03---------->");
                            tv_brand.setText(message);
                        }
                        else
                        {


                            String message1 = jo.getString("message");
                            tv_brand.setVisibility(View.VISIBLE);
                            tv_product.setVisibility(View.INVISIBLE);
                            tv_brand.setText(message1);

                            final Dialog dialog = new Dialog(MainPageScanActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.dialog_alert);
                                dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);

                                Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/ProximaNova-Regular.ttf");

                                Button ok_btn = (Button) dialog.findViewById(R.id.ok_btn);
                                TextView tv=(TextView)dialog.findViewById(R.id.tv_message) ;

                                tv.setText(message1);

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
                    }
                    else
                    {


                            tv_brand.setVisibility(View.VISIBLE);
                            tv_product.setVisibility(View.VISIBLE);
                            for(int i=0;i<array.length();i++)
                            {
                                JSONObject object=array.getJSONObject(i);

                                imei_id=object.getString("imei_id");
                                brand_name=object.getString("brand_name");
                                product_name=object.getString("product_name");
                            }


                            String message=jo.getString("message");
                            tv_imeino.setText(imeiValue);
                            tv_brand.setText("Brand Name: "+brand_name);
                            tv_product.setText("Product Name: "+product_name);
                    }

                } else if(status.equals("false")){


                    String message = jo.getString("message");

                    if(message.equals("User Unauthorized"))
                    {
                        final Dialog dialog = new Dialog(MainPageScanActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.dialog_alert);
                        dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);

                        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/ProximaNova-Regular.ttf");

                        Button ok_btn = (Button) dialog.findViewById(R.id.ok_btn);
                        TextView tv=(TextView)dialog.findViewById(R.id.tv_message) ;

                        tv.setText("This Product Already Sold");

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
                    else
                    {
                        final Dialog dialog = new Dialog(MainPageScanActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.dialog_alert);
                        dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);

                        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/ProximaNova-Regular.ttf");

                        Button ok_btn = (Button) dialog.findViewById(R.id.ok_btn);
                        TextView tv=(TextView)dialog.findViewById(R.id.tv_message) ;

                        tv.setText(message);
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
                }
            } catch (Exception e) {
            }

        }
    }





    //Sales Entry API CALL: ---------------------------------------------------------------------->
    public class SalesEntry_Async_Task extends AsyncTask<String, Void, String> {
        ProgressDialog dialog = new ProgressDialog(MainPageScanActivity.this); // this = YourActivity


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
                        .url(Config.currentpath+"salesEntry")
                        .post(body1)
                        .addHeader("Accept", "application/json")
                        .addHeader("Authorization", "Bearer "+session_token)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("cache-control", "no-cache")
                        .addHeader("Postman-Token", "443535f1-4bd4-44fc-ab44-32056823aca2")
                        .build();

                Response response = client.newCall(request).execute();
                return response.body().string();




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

                    final Dialog dialog = new Dialog(MainPageScanActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.dialog_alert);
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);

                    Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/ProximaNova-Regular.ttf");

                    Button ok_btn = dialog.findViewById(R.id.ok_btn);
                    TextView tv_message=dialog.findViewById(R.id.tv_message) ;
                    tv_message.setText(message);

                    ok_btn.setTypeface(tf);
                    tv_message.setTypeface(tf);



                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();



                } else {

                    String message = jo.getString("errorType");
                    Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
            }

        }
    }


    //GET Branches API CALL: ---------------------------------------------------------------------->
    public class Call_Branches_Async_Task extends AsyncTask<String, Void, String> {
        ProgressDialog dialog = new ProgressDialog(MainPageScanActivity.this); // this = YourActivity


        protected void onPreExecute() {
            super.onPreExecute();

            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setTitle("Loading Branches from Server");
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
                        .url(Config.currentpath+"getBranches")
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
                        //branch_location=object.getString("branch_location");


                        branches_arraylist.add(branch_name);



                        //get values using Hashmap:
                        branches_hash_id.put(object.getString("branch_name"),object.getString("id"));
                        //branches_hash_name.put(object.getString("branch_name"),object.getString("branch_name"));
                        //branches_hash_location.put(object.getString("branch_name"),object.getString("branch_location"));
                    }




                    CustomAdapterArrayList branchesAdapter = new CustomAdapterArrayList(MainPageScanActivity.this, R.layout.simple_dropdown_item_1line, branches_arraylist) {
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
                            tv.setTypeface(regular);
                            tv.setTextSize(20);
                            tv.setPadding(30, 55, 10, 25);
                            if (position == 0) {
                                tv.setTextColor(Color.RED);
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
                                tv.setTextColor(Color.RED);
                            } else {
                                tv.setTextColor(Color.BLACK);
                            }
                            return view;
                        }
                    };
                    branch_spn1.setAdapter(branchesAdapter);

                    String message=jo.getString("message");





                } else {



                    final Dialog dialog1 = new Dialog(MainPageScanActivity.this);
                    dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog1.setCancelable(false);
                    dialog1.setContentView(R.layout.dialog_alert);
                    dialog1.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);

                    Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/ProximaNova-Regular.ttf");

                    Button ok_btn = (Button) dialog1.findViewById(R.id.ok_btn);
                    TextView tv_message=(TextView)dialog1.findViewById(R.id.tv_message) ;
                    tv_message.setText("Session Out. Go to Login Page!");

                    ok_btn.setTypeface(tf);
                    tv_message.setTypeface(tf);



                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog1.dismiss();
                            SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor edit = shared.edit();
                            edit.putString("login_status","false");
                            edit.commit();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    });

                    dialog1.show();


                }
            } catch (Exception e) {
            }

        }
    }


    //Call Branches API CALL: ---------------------------------------------------------------------->
    public class Call_BranchesForAddStock_Async_Task1 extends AsyncTask<String, Void, String> {
        ProgressDialog dialog = new ProgressDialog(MainPageScanActivity.this); // this = YourActivity


        protected void onPreExecute() {
            super.onPreExecute();

            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setTitle("Loading Branches from Server");
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
                        .url(Config.currentpath+"getBranchesForAddStock")
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
                    branches_arraylist0 = new ArrayList<>();

                    for(int j=0;j<array.length();j++)
                    {

                        JSONObject object=array.getJSONObject(j);

                        id=object.getString("id");
                        branch_name=object.getString("branch_name");
                        //branch_location=object.getString("branch_location");


                        branches_arraylist0.add(branch_name);



                        //get values using Hashmap:
                        branches_hash_id1.put(object.getString("branch_name"),object.getString("id"));
                        //branches_hash_name.put(object.getString("branch_name"),object.getString("branch_name"));
                        //branches_hash_location.put(object.getString("branch_name"),object.getString("branch_location"));
                    }




                    CustomAdapterArrayList branchesAdapter0 = new CustomAdapterArrayList(MainPageScanActivity.this, R.layout.simple_dropdown_item_1line, branches_arraylist0) {
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
                            tv.setTypeface(regular);
                            tv.setTextSize(20);
                            tv.setPadding(30, 55, 10, 25);
                            if (position == 0) {
                                tv.setTextColor(Color.RED);
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
                                tv.setTextColor(Color.RED);
                            } else {
                                tv.setTextColor(Color.BLACK);
                            }
                            return view;
                        }
                    };
                    branch0_spn.setAdapter(branchesAdapter0);
                    String message=jo.getString("message");




                } else {



                    final Dialog dialog1 = new Dialog(MainPageScanActivity.this);
                    dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog1.setCancelable(false);
                    dialog1.setContentView(R.layout.dialog_alert);
                    dialog1.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);

                    Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/ProximaNova-Regular.ttf");

                    Button ok_btn = (Button) dialog1.findViewById(R.id.ok_btn);
                    TextView tv_message=(TextView)dialog1.findViewById(R.id.tv_message) ;
                    tv_message.setText("Session Out. Go to Login Page!");

                    ok_btn.setTypeface(tf);
                    tv_message.setTypeface(tf);



                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog1.dismiss();
                            SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor edit = shared.edit();
                            edit.putString("login_status","false");
                            edit.commit();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    });
                    dialog1.show();
                }
            } catch (Exception e) {
            }

        }
    }


    //Stock Transfer API CALL: -------------------------------------------------------------------->
    public class Stocktf_Async_Task extends AsyncTask<String, Void, String> {
        ProgressDialog dialog = new ProgressDialog(MainPageScanActivity.this); // this = YourActivity


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


            Log.e("tag","BRANCH ID---------->"+branchId);

            try {
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/json");

                RequestBody body1 = RequestBody.create(mediaType, "{\n\"imei_id\":\"" +imei_id
                        + "\",\n\"transfer_to\":\"" + branchId

                        + "\"\n}");

                Request request = new Request.Builder()
                        .url(Config.currentpath+"transferStock")
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


                    String data=jo.getString("data");
                    String msg=jo.getString("message");


                    //Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();

                    final Dialog dialog = new Dialog(MainPageScanActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.dialog_alert);
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);

                    Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/ProximaNova-Regular.ttf");

                    Button ok_btn = (Button) dialog.findViewById(R.id.ok_btn);
                    TextView tv=(TextView)dialog.findViewById(R.id.tv_message) ;

                    tv.setText(msg);

                    ok_btn.setTypeface(tf);
                    tv.setTypeface(tf);



                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();

                } else {

                    String message = jo.getString("errorType");
                    Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
            }

        }
    }



    //GET  BRAND API CALL: ---------------------------------------------------------------------->
    public class Call_Brand_Async_Task extends AsyncTask<String, Void, String> {
        ProgressDialog dialog = new ProgressDialog(MainPageScanActivity.this); // this = YourActivity


        protected void onPreExecute() {
            super.onPreExecute();

            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setTitle("Loading Brands from Server");
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
                        .url(Config.currentpath+"getBrands")
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
                    brand_arraylist = new ArrayList<>();

                    for(int j=0;j<array.length();j++)
                    {

                        JSONObject object=array.getJSONObject(j);

                        brand_id=object.getString("id");
                        brand=object.getString("brand_name");
                        //branch_location=object.getString("branch_location");


                        brand_arraylist.add(brand);



                        //get values using Hashmap:
                        brand_hash_id.put(object.getString("brand_name"),object.getString("id"));
                        //branches_hash_name.put(object.getString("branch_name"),object.getString("branch_name"));
                        //branches_hash_location.put(object.getString("branch_name"),object.getString("branch_location"));
                    }




                    CustomAdapterArrayList brandAdapter = new CustomAdapterArrayList(MainPageScanActivity.this, R.layout.simple_dropdown_item_1line, brand_arraylist) {
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
                            tv.setTypeface(regular);
                            tv.setTextSize(20);
                            tv.setPadding(30, 55, 10, 25);
                            if (position == 0) {
                                tv.setTextColor(Color.RED);
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
                                tv.setTextColor(Color.RED);
                            } else {
                                tv.setTextColor(Color.BLACK);
                            }
                            return view;
                        }
                    };
                    brand_spn.setAdapter(brandAdapter);

                    String message=jo.getString("message");





                } else {



                    final Dialog dialog1 = new Dialog(MainPageScanActivity.this);
                    dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog1.setCancelable(false);
                    dialog1.setContentView(R.layout.dialog_alert);
                    dialog1.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);

                    Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/ProximaNova-Regular.ttf");

                    Button ok_btn = (Button) dialog1.findViewById(R.id.ok_btn);
                    TextView tv_message=(TextView)dialog1.findViewById(R.id.tv_message) ;
                    tv_message.setText("Session Out. Go to Login Page!");

                    ok_btn.setTypeface(tf);
                    tv_message.setTypeface(tf);



                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog1.dismiss();
                            SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor edit = shared.edit();
                            edit.putString("login_status","false");
                            edit.commit();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    });

                    dialog1.show();


                }
            } catch (Exception e) {
            }

        }
    }


    //GET  Product API CALL: ---------------------------------------------------------------------->
    public class Call_Product_Async_Task extends AsyncTask<String, Void, String> {
        ProgressDialog dialog = new ProgressDialog(MainPageScanActivity.this); // this = YourActivity


        protected void onPreExecute() {
            super.onPreExecute();

            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setTitle("Loading Product from Server");
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
                        .url(Config.currentpath+"getProducts/"+brandId)
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
                    product_arraylist = new ArrayList<>();

                    for(int j=0;j<array.length();j++)
                    {

                        JSONObject object=array.getJSONObject(j);

                        productId=object.getString("id");
                        productName=object.getString("product_name");
                        //branch_location=object.getString("branch_location");


                        product_arraylist.add(productName);



                        //get values using Hashmap:
                        product_hash_id.put(object.getString("product_name"),object.getString("id"));
                        //branches_hash_name.put(object.getString("branch_name"),object.getString("branch_name"));
                        //branches_hash_location.put(object.getString("branch_name"),object.getString("branch_location"));
                    }




                    CustomAdapterArrayList productAdapter = new CustomAdapterArrayList(MainPageScanActivity.this, R.layout.simple_dropdown_item_1line, product_arraylist) {
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
                            tv.setTypeface(regular);
                            tv.setTextSize(20);
                            tv.setPadding(30, 55, 10, 25);
                            if (position == 0) {
                                tv.setTextColor(Color.RED);
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
                                tv.setTextColor(Color.RED);
                            } else {
                                tv.setTextColor(Color.BLACK);
                            }
                            return view;
                        }
                    };
                    product_spn.setAdapter(productAdapter);

                    String message=jo.getString("message");





                } else {
                    final Dialog dialog1 = new Dialog(MainPageScanActivity.this);
                    dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog1.setCancelable(false);
                    dialog1.setContentView(R.layout.dialog_alert);
                    dialog1.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);

                    Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/ProximaNova-Regular.ttf");

                    Button ok_btn = (Button) dialog1.findViewById(R.id.ok_btn);
                    TextView tv_message=(TextView)dialog1.findViewById(R.id.tv_message) ;
                    tv_message.setText("Session Out. Go to Login Page!");
                    ok_btn.setTypeface(tf);
                    tv_message.setTypeface(tf);


                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog1.dismiss();
                            SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor edit = shared.edit();
                            edit.putString("login_status","false");
                            edit.commit();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });

                    dialog1.show();
                }
            } catch (Exception e) {
            }

        }
    }


    //Stock Transfer API CALL: ---------------------------------------------------------------------->
    public class AddStock_Async_Task extends AsyncTask<String, Void, String> {
        ProgressDialog dialog = new ProgressDialog(MainPageScanActivity.this); // this = YourActivity


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

            Log.e("tag","IMEI NUMBER---------->"+barcode_value);
            Log.e("tag","BRANCH ID---------->"+branchId1);
            Log.e("tag","BRAND ID---------->"+brandId);
            Log.e("tag","PRODUCT ID---------->"+getId);

            try {
                OkHttpClient client = new OkHttpClient();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body1 = RequestBody.create(mediaType, "{\n\"brand_id\":\"" +brandId
                        + "\",\n\"product_id\":\"" + getId
                        + "\",\n\"branch_id\":\"" + branchId1
                        + "\",\n\"imei_number\":\"" + barcode_value
                        + "\"\n}");

                Request request = new Request.Builder()
                        .url(Config.currentpath+"addStock")
                        .post(body1)
                        .addHeader("Accept", "application/json")
                        .addHeader("Authorization", "Bearer "+session_token)
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


                String status = jo.getString("status");


                if (status.equals("success")) {

                    final Dialog dialog1 = new Dialog(MainPageScanActivity.this);
                    dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog1.setCancelable(false);
                    dialog1.setContentView(R.layout.dialog_alert);
                    dialog1.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);

                    Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/ProximaNova-Regular.ttf");

                    Button ok_btn = (Button) dialog1.findViewById(R.id.ok_btn);
                    TextView tv_message=(TextView)dialog1.findViewById(R.id.tv_message) ;
                    tv_message.setText("Added Successfully");

                    ok_btn.setTypeface(tf);
                    tv_message.setTypeface(tf);



                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog1.dismiss();

                          /*  Intent i=new Intent(getApplicationContext(),DashboardActivity.class);
                            startActivity(i);
                            finish();*/

                        }
                    });

                    dialog1.show();





                } else  if (status.equals("false")){


                    Log.e("tag","GGGGGGGGGGGGG---------------->");
                    Toast.makeText(getApplicationContext(),"You are not access to Add a Stock",Toast.LENGTH_LONG).show();


                }
                else
                {
                    Toast.makeText(getApplicationContext(),"This IMEI number has already taken.",Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
            }

        }
    }


    //Stock Transfer API CALL: ---------------------------------------------------------------------->
    public class StockReceive extends AsyncTask<String, Void, String> {
        ProgressDialog dialog = new ProgressDialog(MainPageScanActivity.this); // this = YourActivity


        protected void onPreExecute() {
            super.onPreExecute();

            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setTitle("SqMobi Alert!");
            dialog.setMessage("Loading. Please wait...");
            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            Log.e("tag","IMEI ID GGGGGGGGGGGGG---------->"+imei_id);


            try {
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/json");

                RequestBody body1 = RequestBody.create(mediaType, "{\n\"imei_id\":\"" +imei_id
                        + "\"\n}");

                Request request = new Request.Builder()
                        .url(Config.currentpath+"receiveStock")
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


                    String data=jo.getString("data");
                    String msg=jo.getString("message");


                    //Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();

                    final Dialog dialog = new Dialog(MainPageScanActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.dialog_alert);
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);

                    Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/ProximaNova-Regular.ttf");

                    Button ok_btn = (Button) dialog.findViewById(R.id.ok_btn);
                    TextView tv=(TextView)dialog.findViewById(R.id.tv_message) ;

                    tv.setText(msg);

                    ok_btn.setTypeface(tf);
                    tv.setTypeface(tf);



                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();

                } else {






                    String msg=jo.getString("errorType");
                    Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
            }

        }
    }
}
