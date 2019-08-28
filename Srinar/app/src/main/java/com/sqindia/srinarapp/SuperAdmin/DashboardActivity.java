package com.sqindia.srinarapp.SuperAdmin;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.sqindia.srinarapp.MachiningModule.MachiningEntryActivity;
import com.sqindia.srinarapp.AssemblingModule.AssemblingEntryActivity;
import com.sqindia.srinarapp.ElectroplatingModule.ElectroplateEntryActivity;
import com.sqindia.srinarapp.Fonts.FontsOverride;
import com.sqindia.srinarapp.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DashboardActivity extends Activity {
    LinearLayout lnr_machining,lnr_electroplating,lnr_assembling,statusreport,qcreport,back_login,dispatch;
    TextView role_tv,sub_titletv,logout_tv;
    SharedPreferences.Editor editor;
    String session_token,str_dept,show_role,str_name,str_role;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);
        hideSoftKeyboard();

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(DashboardActivity.this, v1);

        //token get from Login Activity:
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();
        session_token = sharedPreferences.getString("str_sessiontoken", "");
        str_dept=sharedPreferences.getString("userpermission","");
        str_name=sharedPreferences.getString("emp_name","");
        str_role=sharedPreferences.getString("str_role","");

        lnr_machining=findViewById(R.id.lnr_machining);
        lnr_electroplating=findViewById(R.id.lnr_electroplating);
        lnr_assembling=findViewById(R.id.lnr_assembling);
        role_tv=findViewById(R.id.role_tv);
        sub_titletv=findViewById(R.id.sub_titletv);
        statusreport=findViewById(R.id.statusreport);
        logout_tv=findViewById(R.id.logout_tv);
        qcreport=findViewById(R.id.qcreport);
        back_login=findViewById(R.id.back_login);
        dispatch=findViewById(R.id.dispatch);


        sub_titletv.setVisibility(View.GONE);
        if(str_dept.equals("ALL"))
        {
            //show_role="Super Admin.,";
            sub_titletv.setVisibility(View.VISIBLE);
        }
        else
        {
            sub_titletv.setVisibility(View.GONE);
        }

        role_tv.setText("Welcome "+str_name);




        sub_titletv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goadmin=new Intent(getApplicationContext(),AdminDashboard.class);
                startActivity(goadmin);
                finish();
            }
        });


        back_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(DashboardActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Do you want to exit the Application?")
                        .setConfirmText("Yes!")
                        .setCancelText("No")

                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor edit = shared.edit();
                                edit.putString("status","false") ;
                                edit.commit();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
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


        lnr_machining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(str_dept.equals("ME"))
                {
                    lnr_machining.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.dash_fill_border) );

                    Intent machine=new Intent(getApplicationContext(),MachiningEntryActivity.class);
                    startActivity(machine);
                    finish();

                }
                else if(str_dept.equals("ALL"))
                {
                    lnr_machining.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.dash_fill_border) );
                    Intent machine=new Intent(getApplicationContext(),MachiningEntryActivity.class);
                    startActivity(machine);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Can't accessed for Machining.....",Toast.LENGTH_LONG).show();
                }


            }
        });
        logout_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(DashboardActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Do you want to Logout?")
                        .setConfirmText("Yes!")
                        .setCancelText("No")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor edit = shared.edit();
                                edit.putString("status","false") ;
                                edit.commit();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
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



        lnr_electroplating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(str_dept.equals("PEC"))
                {
                    lnr_electroplating.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.dash_fill_border) );

                    Intent machine=new Intent(getApplicationContext(),ElectroplateEntryActivity.class);
                    startActivity(machine);
                    finish();

                }

                else if(str_dept.equals("ALL"))
                {
                    lnr_electroplating.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.dash_fill_border) );

                    Intent machine=new Intent(getApplicationContext(),ElectroplateEntryActivity.class);
                    startActivity(machine);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Can't accessed for Electroplating.....",Toast.LENGTH_LONG).show();
                }


            }
        });


        lnr_assembling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(str_dept.equals("AEC"))
                {
                    lnr_assembling.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.dash_fill_border) );
                    Intent machine=new Intent(getApplicationContext(), AssemblingEntryActivity.class);
                    startActivity(machine);
                    finish();
                }


                else if(str_dept.equals("ALL"))
                {
                    lnr_assembling.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.dash_fill_border) );
                    Intent machine=new Intent(getApplicationContext(), AssemblingEntryActivity.class);
                    startActivity(machine);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Can't accessed for Assembling.....",Toast.LENGTH_LONG).show();
                }


            }
        });



        qcreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!str_dept.equals("ME"))
                {
                    qcreport.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.dash_fill_border) );

                    Intent machine=new Intent(getApplicationContext(),QADirection.class);
                    startActivity(machine);
                    finish();

                }

                else
                {
                    Toast.makeText(getApplicationContext(),"Can't accessed for QA.....",Toast.LENGTH_LONG).show();
                }
            }
        });



        dispatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(str_dept.equals("AEC"))
                {
                    dispatch.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.dash_fill_border));

                    Intent machine=new Intent(getApplicationContext(),DispatchProduct.class);
                    startActivity(machine);
                    finish();
                }


                else if(str_dept.equals("ALL"))
                {
                    dispatch.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.dash_fill_border));

                    Intent machine=new Intent(getApplicationContext(),DispatchProduct.class);
                    startActivity(machine);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Can't accessed for Dispatch.....",Toast.LENGTH_LONG).show();
                }

            }
        });

        statusreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(str_dept.equals("ALL"))
                {
                    statusreport.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.dash_fill_border));
                    Intent report = new Intent(getApplicationContext(), NewStatusReportActivity.class);
                    startActivity(report);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Can't accessed for Status Report.....",Toast.LENGTH_LONG).show();
                }
            }
        });


    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        new SweetAlertDialog(DashboardActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Do you want to exit the Application?")
                .setConfirmText("Yes!")
                .setCancelText("No")

                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor edit = shared.edit();
                        edit.putString("status","false") ;
                        edit.commit();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
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



    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

}
