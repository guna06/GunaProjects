package com.sqindia.srinarapp.SuperAdmin;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sqindia.srinarapp.Adapter.CustomAdapter;
import com.sqindia.srinarapp.Fonts.FontsOverride;
import com.sqindia.srinarapp.R;
import com.sqindia.srinarapp.Utils.Config;
import com.sqindia.srinarapp.Utils.HttpUtils;
import com.sqindia.srinarapp.Utils.Util;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class LoginActivity extends Activity {
    LinearLayout back_login;
    ImageView login_img;
    MaterialBetterSpinner users_spn;
    String str_typeusers;
    Typeface segoeui;
    String str_uname,str_pwd,str_role;
    EditText user_edt,pwd_edt;
    Dialog loader_dialog;


    String[] typeofusers_data = {"Super Admin", "Admin", "Supervisor",
            "QA", "Final Inspection"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(LoginActivity.this, v1);

        back_login=findViewById(R.id.back_login);
        login_img=findViewById(R.id.login_img);
        users_spn=findViewById(R.id.users_spn);
        user_edt=findViewById(R.id.user_edt);
        pwd_edt=findViewById(R.id.pwd_edt);

        //set default Loader:
        loader_dialog = new Dialog(LoginActivity.this);
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loader_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loader_dialog.setCancelable(false);
        loader_dialog.setContentView(R.layout.test_loader);



        segoeui=Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/segoeuil.ttf");
        pwd_edt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        // Shift Spinner Adapter:
        final CustomAdapter shiftAdapter = new CustomAdapter(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, typeofusers_data) {
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
                tv.setTypeface(segoeui);
                tv.setTextSize(9);
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
                tv.setTextSize(15);
                tv.setPadding(10, 20, 0, 20);
                tv.setTypeface(segoeui);
                if (position == 0) {
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        users_spn.setAdapter(shiftAdapter);


        users_spn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FontsOverride.overrideFonts(LoginActivity.this, view);
                str_typeusers = adapterView.getItemAtPosition(i).toString();
                Log.e("tag", "str_typeusers------>" + str_typeusers);

                if(str_typeusers.equals("Super Admin"))
                {
                    str_role="SUPER ADMIN";
                }
                else if(str_typeusers.equals("Admin"))
                {
                    str_role="ADMIN";
                }
                else if(str_typeusers.equals("Supervisor"))
                {
                    str_role="SUPERVISOR";
                }
                else if(str_typeusers.equals("QA"))
                {
                    str_role="QA";
                }
                else if(str_typeusers.equals("Final Inspection"))
                {
                    str_role="Final Insecption";
                }
            }
        });



        back_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Do you want to exit the Application?")
                        .setConfirmText("Yes!")
                        .setCancelText("No")

                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                Intent i1 = new Intent(Intent.ACTION_MAIN);
                                i1.setAction(Intent.ACTION_MAIN);
                                i1.addCategory(Intent.CATEGORY_HOME);
                                i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i1);
                                finish();
                                sDialog.dismiss();

                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();

                            }
                        })
                        .show();
            }
        });



        login_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              str_uname = user_edt.getText().toString();
                str_pwd = pwd_edt.getText().toString();
                if (Util.Operations.isOnline(LoginActivity.this)) {

                    if (!str_uname.isEmpty() && !str_pwd.isEmpty()) {
                        new staffLogin_Task().execute();
                   } else {

                        new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("WARNING MESSAGE!!!")
                                .setContentText("Enter All Details..")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                    }
                                })
                                .show();
                    }
                } else {
                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("No Internet Connectivity")
                            .show();
                }

            }
        });
    }



    public class staffLogin_Task extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            loader_dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";


            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("employeeid", str_uname);
                jsonObject.accumulate("password", str_pwd);
                jsonObject.accumulate("role",str_role);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest(Config.WEB_URL_LOGIN, json);

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loader_dialog.hide();

            try {
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");
                String msg = jo.getString("message");




                if (status.equals("true")) {

                    String username = jo.getString("username");
                    String id = jo.getString("employeeid");
                    String role = jo.getString("role");
                    String dept=jo.getString("userpermission");
                    String token = jo.getString("token");


                    SharedPreferences sharedPrefces = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor edit = sharedPrefces.edit();
                    edit.putString("str_sessiontoken", token);
                    edit.putString("emp_name",username);
                    edit.putString("emp_id",id);
                    edit.putString("str_role",role);
                    edit.putString("userpermission",dept);
                    edit.putString("status","true") ;
                    edit.commit();


                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("MESSAGE!!!")
                            .setContentText("Login successfully..")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {

                                    sDialog.dismissWithAnimation();
                                    Intent i = new Intent(getApplicationContext(), DashboardActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            })
                            .show();

                } else {

                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("MESSAGE!!!")
                            .setContentText(msg)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {

                                    sDialog.dismissWithAnimation();
                                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            })
                            .show();


                }
            } catch (Exception e) {

            }
        }
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Do you want to exit the Application?")
                .setConfirmText("Yes!")
                .setCancelText("No")

                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        Intent i1 = new Intent(Intent.ACTION_MAIN);
                        i1.setAction(Intent.ACTION_MAIN);
                        i1.addCategory(Intent.CATEGORY_HOME);
                        i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i1);
                        finish();
                        sDialog.dismiss();

                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();

                    }
                })
                .show();





    }
}
