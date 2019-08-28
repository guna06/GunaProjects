package com.nextinnovation.pt.barcodescanner.Sqmobiles;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.nextinnovation.pt.barcodescanner.R;


public class SplashActivity1 extends Activity {

    private static int SPLASH_TIME_OUT = 3000;
    String login_status;
    Typeface quondo;
    TextView appname_tv;

    public void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash1);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SplashActivity1.this);
        login_status = sharedPreferences.getString("login_status", "");

        quondo= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/Realistica Demo.otf");
        appname_tv=findViewById(R.id.appname_tv);
        appname_tv.setTypeface(quondo);




        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {

                if(login_status.equals("true"))
                {
                    Intent intent=new Intent(SplashActivity1.this,DashboardActivity1.class);
                    startActivity(intent);
                    finish();
                }
                else if(login_status.equals("false"))
                {
                    Intent intent=new Intent(SplashActivity1.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Intent intent=new Intent(SplashActivity1.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

             }
        }, SPLASH_TIME_OUT);
    }    }

