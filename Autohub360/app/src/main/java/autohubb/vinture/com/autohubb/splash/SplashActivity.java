package autohubb.vinture.com.autohubb.splash;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import autohubb.vinture.com.autohubb.R;
import autohubb.vinture.com.autohubb.activity.MainActivity;
import autohubb.vinture.com.autohubb.fonts.FontsOverride;
import autohubb.vinture.com.autohubb.UserModule.LoginActivity;
import autohubb.vinture.com.autohubb.UserModule.RegisterActivity;

public class SplashActivity extends AppCompatActivity {
    final long DELAY_MS = 300;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.
    int currentPage = 0;
    Timer timer;
    TextView splashcontent_tv;
    int i;
    TextView registertv, logintv;
    int NUM_PAGES = 4;
    String status;
    String currentVersion, playstoreVersion;
    Dialog dg_show_update;
    TextView tv_dg_txt, tv_dg_txt2;
    Button btn_dg_download;
    private ViewPager mPager;
    private SimpleViewPagerIndicator pageIndicator;
    private int resourceList[] = {R.drawable.splash,
            R.drawable.splash, R.drawable.splash
    };
    String login_status;
    private int List[] = {R.string.splashone, R.string.splashone, R.string.splashone};





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(SplashActivity.this, v1);

        Typeface lato_head = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/heleveticalBold.TTF");
        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            Log.e("tag", "version:" + currentVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.e("tag", "err:" + e.toString());
        }

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
        status = sharedPreferences.getString("login_status", "");
        registertv = findViewById(R.id.registertv);
        logintv = findViewById(R.id.logintv);

        logintv.setTypeface(lato_head);
        registertv.setTypeface(lato_head);
        initView();




        if (isNetworkConnected()) {
        }else
        {
            try {
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();

                alertDialog.setTitle("Info");
                alertDialog.setMessage("Internet not available, Cross check your internet connectivity and try again");
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();

                    }
                });

                alertDialog.show();
            } catch (Exception e) {
                // Log.d(Constants.TAG, "Show Dialog: " + e.getMessage());
            }
        }

        registertv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    startActivity(new Intent(SplashActivity.this, RegisterActivity.class));
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                    finish();

            }
        });

        logintv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                finish();
            }
        });


        if (status == "" || status == null) {
            logintv.setVisibility(View.VISIBLE);
            registertv.setVisibility(View.VISIBLE);
            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {

                    if (currentPage == 1) {
                        String splashone = getString(R.string.splashone);
                        /*splashcontent_tv.setText("");
                        splashcontent_tv.setText(splashone);*/

                    } else if (currentPage == 2) {
                        String splashtwo = getString(R.string.splashone);
                        /*splashcontent_tv.setText("");
                        splashcontent_tv.setText(splashtwo);*/
                    } else if (currentPage == 3) {
                        String splashthree = getString(R.string.splashone);
                        /*splashcontent_tv.setText("");
                        splashcontent_tv.setText(splashthree);*/

                    }


                    if (currentPage == NUM_PAGES - 1) {
                        currentPage = 0;
                    }
                    mPager.setCurrentItem(currentPage++, true);
                }
            };

            timer = new Timer(); // This will create a new Thread
            timer.schedule(new TimerTask() { // task to be scheduled

                @Override
                public void run() {

                    handler.post(Update);

                }
            }, DELAY_MS, PERIOD_MS);


        } else if (status.equals("false")) {

            logintv.setVisibility(View.VISIBLE);
            registertv.setVisibility(View.VISIBLE);
            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {

                    if (currentPage == 1) {

                        Log.e("tag", "page1---------->" + currentPage);
                        String splashone = getString(R.string.splashone);
                       /* splashcontent_tv.setText("");
                        splashcontent_tv.setText(splashone);*/


                    } else if (currentPage == 2) {

                        Log.e("tag", "page2---------->" + currentPage);
                        String splashtwo = getString(R.string.splashone);
                        /*splashcontent_tv.setText("");
                        splashcontent_tv.setText(splashtwo);*/

                    } else if (currentPage == 3) {

                        Log.e("tag", "page3---------->" + currentPage);
                        String splashthree = getString(R.string.splashone);
                        /*splashcontent_tv.setText("");
                        splashcontent_tv.setText(splashthree);*/


                    }


                    if (currentPage == NUM_PAGES - 1) {
                        currentPage = 0;
                    }
                    mPager.setCurrentItem(currentPage++, true);
                }
            };

            timer = new Timer(); // This will create a new Thread
            timer.schedule(new TimerTask() { // task to be scheduled

                @Override
                public void run() {

                    handler.post(Update);

                }
            }, DELAY_MS, PERIOD_MS);


        } else {
            logintv.setVisibility(View.INVISIBLE);
            registertv.setVisibility(View.INVISIBLE);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                    finish();

                }
            }, 2000);

        }
    }

    public void initView() {
        // TODO Auto-generated method stub
        mPager = (ViewPager) findViewById(R.id.pager);
        pageIndicator = (SimpleViewPagerIndicator) findViewById(R.id.page_indicator);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        ViewPagerAdapter mPagerAdapter = new ViewPagerAdapter(this,
                resourceList, List, width, height);
        mPager.setOffscreenPageLimit(1);
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(0); // current item number
        pageIndicator.setViewPager(mPager);
        pageIndicator.notifyDataSetChanged();
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    private boolean isNetworkConnected() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE); // 1
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo(); // 2
        return networkInfo != null && networkInfo.isConnected(); // 3
    }

    }

