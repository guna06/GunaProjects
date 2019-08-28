package com.sqindia.srinarapp.SuperAdmin;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sqindia.srinarapp.AssemblingModule.QcAssemblingJob;
import com.sqindia.srinarapp.AssemblingModule.ViewAssemblingJob;
import com.sqindia.srinarapp.ElectroplatingModule.QcElectroplateJob;

import com.sqindia.srinarapp.Fonts.FontsOverride;

import com.sqindia.srinarapp.MachiningModule.MachiningEntryActivity;
import com.sqindia.srinarapp.MachiningModule.QcMachineJob;
import com.sqindia.srinarapp.R;
import com.sqindia.srinarapp.Utils.Config;
import com.sqindia.srinarapp.Utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class QADirection extends Activity{
    SharedPreferences.Editor editor;
    String session_token,str_dept,str_name,str_role;
    LinearLayout back;
    Button qamac,qaele,qaass,upload_export;
    DownloadManager downloadManager;
    private static final int PICK_FROM_GALLERY = 101;
    Dialog loader_dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qadirection);


        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(QADirection.this, v1);


        //set default Loader:
        loader_dialog = new Dialog(QADirection.this);
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loader_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader_dialog.setCancelable(false);
        loader_dialog.setContentView(R.layout.test_loader);

        //token get from Login Activity:
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();
        session_token = sharedPreferences.getString("str_sessiontoken", "");
        str_dept=sharedPreferences.getString("userpermission","");
        str_name=sharedPreferences.getString("emp_name","");
        str_role=sharedPreferences.getString("str_role","");


        qamac=findViewById(R.id.qamac);
        qaele=findViewById(R.id.qaele);
        qaass=findViewById(R.id.qaass);
        back=findViewById(R.id.back);
        upload_export=findViewById(R.id.upload_export);



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back=new Intent(getApplicationContext(),DashboardActivity.class);
                startActivity(back);
                finish();
            }
        });


        upload_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new exportExcelAsync().execute();

            }
        });




        qamac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(str_dept.equals("MC"))
                {
                    Intent machine=new Intent(getApplicationContext(),QcMachineJob.class);
                    machine.putExtra("navdashboard", "true");
                    startActivity(machine);
                    finish();

                }
                else if(str_dept.equals("ALL"))
                {

                    Intent machine=new Intent(getApplicationContext(),QcMachineJob.class);
                    machine.putExtra("navdashboard", "true");
                    startActivity(machine);
                    finish();
                }


                else if(str_dept.equals("MCECAC"))
                {

                    Intent machine=new Intent(getApplicationContext(),QcMachineJob.class);
                    machine.putExtra("navdashboard", "true");
                    startActivity(machine);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Not accessed for Machining.....",Toast.LENGTH_LONG).show();
                }
            }
        });





        qaele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(str_dept.equals("PEC"))
                {
                    Intent ele=new Intent(getApplicationContext(),QcElectroplateJob.class);
                    ele.putExtra("navdashboard", "true");
                    startActivity(ele);
                    finish();

                }
                else if(str_dept.equals("ALL"))
                {
                    Intent ele=new Intent(getApplicationContext(),QcElectroplateJob.class);
                    ele.putExtra("navdashboard", "true");
                    startActivity(ele);
                    finish();
                }

                else if(str_dept.equals("MCECAC"))
                {

                    Intent ele=new Intent(getApplicationContext(),QcElectroplateJob.class);
                    ele.putExtra("navdashboard", "true");
                    startActivity(ele);
                    finish();
                }

                else
                {
                    Toast.makeText(getApplicationContext(),"Not accessed for Platting.....",Toast.LENGTH_LONG).show();
                }
            }
        });


        qaass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(str_dept.equals("AEC"))
                {


                    Intent ass=new Intent(getApplicationContext(),QcAssemblingJob.class);
                    ass.putExtra("navdashboard", "true");
                    startActivity(ass);
                    finish();

                }
                else if(str_dept.equals("ALL"))
                {

                    Intent ass=new Intent(getApplicationContext(),QcAssemblingJob.class);
                    ass.putExtra("navdashboard", "true");
                    startActivity(ass);
                    finish();
                }


                else if(str_dept.equals("MCECAC"))
                {

                    Intent ass=new Intent(getApplicationContext(),QcAssemblingJob.class);
                    ass.putExtra("navdashboard", "true");
                    startActivity(ass);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Not accessed for Assembling.....",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent back=new Intent(getApplicationContext(),DashboardActivity.class);
        startActivity(back);
        finish();
    }

    //@@@@@@@@@@@@@@@@@@@@@@@@ CALL MAIN PART API
    public class exportExcelAsync extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            loader_dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";


            try {
                JSONObject jsonObject = new JSONObject();
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequestToken(Config.WEB_URL_GET_EXCEL_UPLOAD, session_token);

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loader_dialog.dismiss();

            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");

                if (status.equals("true")) {
                    String filrname=jo.getString("filename");


                    downloadManager=(DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);

                    Uri uri=Uri.parse("http://192.168.1.2:4030/reports/"+filrname);
                    DownloadManager.Request request=new DownloadManager.Request(uri);
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    Long reference=downloadManager.enqueue(request);

                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"mis@srinarelectronics.com","vnp@srinarelectronics.com","vijayakumar@srinarelectronics.com","sales@srinarelectronics.com",""});
                    //i.putExtra(Intent.EXTRA_EMAIL, new String[]{"gunapandian06@gmail.com","guna@sqindia.net"});
                    i.putExtra(Intent.EXTRA_SUBJECT, "Srinar Report");
                    i.putExtra(Intent.EXTRA_TEXT   , "Hi All!  Report for Working Status:");
                    try {
                        startActivity(Intent.createChooser(i, "Send mail..."));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(QADirection.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    new SweetAlertDialog(QADirection.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("WARNING MESSAGE!!!")
                            .setContentText("Server Error")

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
