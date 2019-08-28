package com.sqindia.www.auto360parts_M.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sqindia.www.auto360parts_M.Font.FontsOverride;
import com.sqindia.www.auto360parts_M.R;


public class DashboardActivity extends Activity {


    LinearLayout upload_parts,view_parts;
    ImageView logout_iv;
    TextView version_tv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(DashboardActivity.this, v1);


        upload_parts=findViewById(R.id.upload_parts);
        view_parts=findViewById(R.id.view_parts);
        logout_iv=findViewById(R.id.logout_iv);
        version_tv=findViewById(R.id.version_tv);

        try {
            String versionName = getApplicationContext().getPackageManager()
                    .getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;
            version_tv.setText("Released Version : "+versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        upload_parts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),PartsEntryActivity.class);
                startActivity(i);
                finish();
            }
        });

        view_parts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),ViewCarPartsActivity.class);
                startActivity(i);
                finish();
            }
        });


        logout_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(DashboardActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_logout);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);

                Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/helevetical.ttf");

                Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);
                Button ok_btn = (Button) dialog.findViewById(R.id.ok_btn);
                TextView tv=(TextView)dialog.findViewById(R.id.tv) ;

                cancel_btn.setTypeface(tf);
                ok_btn.setTypeface(tf);
                tv.setTypeface(tf);


                cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                ok_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor edit = shared.edit();
                        edit.putString("login_status","false");
                        edit.commit();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();


                        edit.clear();
                        edit.commit();
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });



    }
}
