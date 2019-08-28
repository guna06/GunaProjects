package com.sqindia.srinarapp.ElectroplatingModule;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sqindia.srinarapp.R;
import com.sqindia.srinarapp.Utils.Config;
import com.sqindia.srinarapp.Utils.HttpUtils;
import com.sqindia.srinarapp.Utils.Util;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DialogSignatureElectroplate extends Dialog {

    Activity activity;
    TextView partname_tv,l1;
    EditText qty_edt,edt_pincode;
    TextView approve_tv;
    LinearLayout approve;
    Typeface segoeuil;
    String set_batchid, str_qty, token, set_completeqty,str_pincode,set_empid,set_approved_qty;
    Dialog loader_dialog;

    public DialogSignatureElectroplate(Activity act) {
        super(act);
        this.activity = act;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_electro);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        segoeuil = Typeface.createFromAsset(getContext().getAssets(), "fonts/segoeuil.ttf");

        partname_tv=findViewById(R.id.partname_tv);
        qty_edt=findViewById(R.id.qty_edt);
        edt_pincode=findViewById(R.id.edt_pincode);
        approve_tv = (TextView) findViewById(R.id.approve_tv);
        approve = (LinearLayout) findViewById(R.id.approve_lv);
        l1=findViewById(R.id.l1);
        partname_tv.setTypeface(segoeuil);
        qty_edt.setTypeface(segoeuil);
        approve_tv.setTypeface(segoeuil);
        edt_pincode.setTypeface(segoeuil);
        l1.setTypeface(segoeuil);
        approve.setEnabled(true);

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getContext());
        token = sharedPreferences.getString("str_sessiontoken", "");
        set_batchid = sharedPreferences.getString("dia_ele_part_id", "");
        set_completeqty =sharedPreferences.getString("dia_ele_complete_qty_ele","");
        set_empid=sharedPreferences.getString("emp_id","");
        set_approved_qty=sharedPreferences.getString("dia_ele_approved_qty_ele","");

        //set default Loader:
        loader_dialog = new Dialog(getContext());
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loader_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader_dialog.setCancelable(false);
        loader_dialog.setContentView(R.layout.test_loader);

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                str_qty=qty_edt.getText().toString().trim();
                str_pincode=edt_pincode.getText().toString();

                if (Util.Operations.isOnline(getContext())) {
                    if (!str_qty.equals("")) {
                        int type_val = Integer.parseInt(str_qty);
                        int complete_val = Integer.parseInt(set_completeqty);
                        int approve_val= Integer.parseInt(set_approved_qty);
                        int to_approve=complete_val-approve_val;
                        if (type_val > to_approve) {
                            Toast.makeText(getContext(), "Please enter minimum value for Quantity", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            if(!str_pincode.equals(""))
                            {

                                new plateQuantityApprovalAsync().execute();
                            }
                            else
                            {
                                Toast.makeText(getContext(), "Please enter Pass Code", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Please enter complete Qty", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "No Internet Connectivity...", Toast.LENGTH_LONG).show();
                }
            }


        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        dismiss();
    }





    //@@@@@@@@@@@@@@@@@@@@@@@@ CALL VIEW MACHINE API
    public class plateQuantityApprovalAsync extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            loader_dialog.show();
            approve.setEnabled(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("qty",str_qty);
                jsonObject.accumulate("partid",set_batchid);
                jsonObject.accumulate("qcstatus","approved");
                jsonObject.accumulate("employeeid",set_empid);
                jsonObject.accumulate("qcauthcode",str_pincode);


                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest1(Config.WEB_URL_ELECTROPLATE_APPROVAL,json,token);

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");
                String msg = jo.getString("message");


                if (status.equals("true")) {
                    loader_dialog.dismiss();
                    new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("SUCCESS MESSAGE!!!")
                            .setContentText(msg)

                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    Intent i=new Intent(getContext(),QcElectroplateJob.class);
                                    getContext().startActivity(i);
                                    approve.setEnabled(true);

                                }
                            })
                            .show();
                } else {
                    loader_dialog.dismiss();
                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("WARNING MESSAGE!!!")
                            .setContentText(msg)

                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();

                                }
                            })
                            .show();
                }
            } catch (Exception e) {

            }
        }

    }
}
