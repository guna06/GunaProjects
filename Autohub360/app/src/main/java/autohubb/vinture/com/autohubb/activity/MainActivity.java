package autohubb.vinture.com.autohubb.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import java.lang.reflect.Field;

import autohubb.vinture.com.autohubb.Shop.ViewCartActivity;
import autohubb.vinture.com.autohubb.MyOrdersModule.MyOrderMainPageFragment;
import autohubb.vinture.com.autohubb.R;
import autohubb.vinture.com.autohubb.Shop.ShopListFragment;
import autohubb.vinture.com.autohubb.VehicleModule.CommercialCarListActivity;
import autohubb.vinture.com.autohubb.VehicleModule.MyCarsFragment;
import autohubb.vinture.com.autohubb.VehicleModule.PrivateCarRegisterActivity;
import autohubb.vinture.com.autohubb.fonts.FontsOverride;
import autohubb.vinture.com.autohubb.fragment.HomeFragment;
import autohubb.vinture.com.autohubb.navigation.CarLinksActivity;
import autohubb.vinture.com.autohubb.navigation.ReceiptsActivity;
import autohubb.vinture.com.autohubb.splash.SplashActivity;
import autohubb.vinture.com.autohubb.utils.Config;
import autohubb.vinture.com.autohubb.utils.HttpUtils;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    Toolbar toolbar;
    TextView toolbar_title, username_tv, mail_tv;
    SharedPreferences sharedlogin;
    SharedPreferences.Editor editlogin;
    public String get_mail_from_reg, get_otp_from_reg, get_apikey_from_login, get_email_from_login, get_userid_from_login, get_username_from_login;
    LinearLayout shop_bag;
    public static TextView cart_badge;
    BottomNavigationView navigation;
    NavigationView navigationView;
    Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar =  findViewById(R.id.toolbar_top);
        username_tv=findViewById(R.id.username_tv) ;
        mail_tv=findViewById(R.id.mail_tv);
        cart_badge=findViewById(R.id.cart_badge);
        shop_bag=findViewById(R.id.shop_bag);
        navigationView=findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(MainActivity.this, v1);
        typeface=Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/helevetical.ttf");

        sharedlogin = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        editlogin = sharedlogin.edit();
        get_mail_from_reg = sharedlogin.getString("get_mail_from_reg", "");
        get_otp_from_reg = sharedlogin.getString("get_otp_from_reg", "");
        get_apikey_from_login = sharedlogin.getString("get_apikey_from_login", "");
        get_email_from_login = sharedlogin.getString("get_email_from_login", "");
        get_userid_from_login = sharedlogin.getString("get_userid_from_login", "");
        get_username_from_login = sharedlogin.getString("get_username_from_login", "");


        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername =  headerView.findViewById(R.id.username_tv);
        TextView mail_tv=headerView.findViewById(R.id.mail_tv);
        navUsername.setText(get_username_from_login);
        mail_tv.setText(get_email_from_login);
        navUsername.setTypeface(typeface);
        mail_tv.setTypeface(typeface);



        try {
            mail_tv.setText(get_email_from_login);
        }catch (Exception e)
        {

        }

        new ViewCart_Task().execute();

        shop_bag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shop=new Intent(getApplicationContext(),ViewCartActivity.class);
                startActivity(shop);
                finish();
            }
        });


        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

       // drawer.openDrawer(Gravity.LEFT);
        toggle.syncState();
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("Landing Page");
        HomeFragment homeFragment1 = new HomeFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, homeFragment1);
        transaction.commit();


        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setNavigationItemSelectedListener(this);
        Menu m = navigationView .getMenu();

        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);

            //for applying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    SpannableString s = new SpannableString(subMenuItem.getTitle());
                    s.setSpan(new TypefaceSpan("fonts/helevetical.ttf"), 0, s.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    subMenuItem.setTitle(s);
                }
            }
        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {

                case R.id.action_home:
                    toolbar_title.setText("Landing Page");
                    HomeFragment homeFragment = new HomeFragment();
                    FragmentTransaction transaction0 = getSupportFragmentManager().beginTransaction();
                    transaction0.replace(R.id.frame, homeFragment);
                    transaction0.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up);
                    transaction0.commit();
                    return true;


                case R.id.action_mycars:
                    toolbar_title.setText("My Cars");
                    MyCarsFragment myCarsFragment = new MyCarsFragment();
                    FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
                    transaction1.replace(R.id.frame, myCarsFragment);
                    transaction1.setCustomAnimations(R.anim.slide_in_up,
                            R.anim.slide_out_up);
                    transaction1.commit();
                    return true;

                case R.id.action_track:
                    toolbar_title.setText("My Order");
                    MyOrderMainPageFragment trackOrderFragment = new MyOrderMainPageFragment();
                    FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
                    transaction2.replace(R.id.frame, trackOrderFragment);
                    transaction2.setCustomAnimations(R.anim.slide_in_up,
                            R.anim.slide_out_up);
                    transaction2.commit();
                    return true;
                case R.id.action_notification:
                    toolbar_title.setText("Notification");
                    return true;
                case R.id.action_shop:
                    toolbar_title.setText("Shop Cart");
                    ShopListFragment shopListFragment = new ShopListFragment();
                    FragmentTransaction transaction4 = getSupportFragmentManager().beginTransaction();
                    transaction4.replace(R.id.frame, shopListFragment);
                    transaction4.setCustomAnimations(R.anim.slide_in_up,
                            R.anim.slide_out_up);
                    transaction4.commit();
                    return true;
            }
            return false;
        }
    };


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Do you want to exit the Application?")
                    .setConfirmText("Yes!")
                    .setCancelText("No")

                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismiss();
                            Intent a = new Intent(Intent.ACTION_MAIN);
                            a.addCategory(Intent.CATEGORY_HOME);
                            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(a);


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        /*//noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_private) {
            Intent private_car = new Intent(MainActivity.this, PrivateCarRegisterActivity.class);
            startActivity(private_car);
        }

        else if (id == R.id.nav_my_car) {
            navigation.setSelectedItemId(R.id.action_mycars);
            MyCarsFragment myCarsFragment = new MyCarsFragment();
            FragmentTransaction transaction5 = getSupportFragmentManager().beginTransaction();
            transaction5.replace(R.id.frame, myCarsFragment);
            transaction5.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up);
            transaction5.commit();
        }


        else if (id == R.id.nav_car_links) {
            Intent car_links = new Intent(MainActivity.this, CarLinksActivity.class);
            startActivity(car_links);
        }
     else if (id == R.id.nav_commercial) {

        Intent commercial_car = new Intent(MainActivity.this, CommercialCarListActivity.class);
        startActivity(commercial_car);
    }

     else if (id == R.id.nav_receipts) {

        Intent receipts = new Intent(MainActivity.this, ReceiptsActivity.class);
        startActivity(receipts);
    }


    else if (id == R.id.drawer_item_logout) {
         final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_logout);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_curve_bg);

        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/helevetical.ttf");

        Button cancel_btn = dialog.findViewById(R.id.cancel_btn);
        Button ok_btn = dialog.findViewById(R.id.ok_btn);
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

                dialog.dismiss();
                new Logout_Task().execute();

            }
        });
        dialog.show();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private static class BottomNavigationViewHelper {
        @SuppressLint("RestrictedApi")
        public static void disableShiftMode(BottomNavigationView view) {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
            try {
                Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
                shiftingMode.setAccessible(true);
                shiftingMode.setBoolean(menuView, false);
                shiftingMode.setAccessible(false);
                for (int i = 0; i < menuView.getChildCount(); i++) {
                    BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                    //noinspection RestrictedApi
                    item.setShiftingMode(false);
                    // set once again checked value, so view will be updated
                    //noinspection RestrictedApi
                    item.setChecked(item.getItemData().isChecked());
                }
            } catch (NoSuchFieldException e) {
                Log.e("BNVHelper", "Unable to get shift mode field", e);
            } catch (IllegalAccessException e) {
                Log.e("BNVHelper", "Unable to change value of shift mode", e);
            }
        }

    }





    //ViewCart API CALL: ---------------------------------------------------------------------->
    public class ViewCart_Task extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog;


        protected void onPreExecute() {
            super.onPreExecute();
            //av_loader.setVisibility(View.VISIBLE);


        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequestToken(Config.WEB_URL_GET_LIST_CART_METHOD+get_userid_from_login,get_apikey_from_login);

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //av_loader.setVisibility(View.GONE);
            /*if (dialog.isShowing()) {
                dialog.dismiss();
            }*/
            try{

                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");
                Log.e("tag","1");
                if (status.equals("true")) {
                    String message=jo.getString("message");
                    JSONObject object=jo.getJSONObject("data");
                    JSONArray car_array=object.getJSONArray("cartItems");

                    if(car_array.length()==0)
                    {
                        Log.e("tag","5");
                        cart_badge.setText("0");
                    }
                    else {
                        //noparts_found.setVisibility(View.GONE);
                        Log.e("tag","6"+car_array.length());
                        int cart_count=car_array.length();

                        cart_badge.setText(String.valueOf(cart_count));
                    }
                }
                else
                {

                }


            }
            catch (Exception e)
            {

            }

            // Setup and Handover data to recyclerview
            //recyclerView_viewcart.setAdapter(viewCartAdapter);


        }
    }


    //Logout API CALL: ---------------------------------------------------------------------->
    public class Logout_Task extends AsyncTask<String, Void, String> {



        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();


                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequestUrlToken(Config.WEB_URL_LOGOUT,get_apikey_from_login);



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


                if (status.equals("true")) {

                    String msg=jo.getString("message");

                    if(msg.equals("Logout success"))
                    {
                        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor edit = shared.edit();
                        edit.putString("login_status","false");
                        edit.commit();

                        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                        startActivity(intent);
                        finish();
                    }


                } else {
                    Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
            }

        }
    }








}

