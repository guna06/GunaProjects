package autohubb.vinture.com.autohubb.MyOrdersModule;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import autohubb.vinture.com.autohubb.R;
import autohubb.vinture.com.autohubb.Shop.ViewCartActivity;
import autohubb.vinture.com.autohubb.fonts.FontsOverride;
import autohubb.vinture.com.autohubb.model.ShippingAddressPojo;
import autohubb.vinture.com.autohubb.orders.CustomerInfoFragment;
import autohubb.vinture.com.autohubb.orders.ProductImfoFragment;
import autohubb.vinture.com.autohubb.orders.ReturnPolicyFragment;
import autohubb.vinture.com.autohubb.orders.ShippingInfoFragment;
import autohubb.vinture.com.autohubb.user.ShippingAddress;
import autohubb.vinture.com.autohubb.utils.Config;
import autohubb.vinture.com.autohubb.utils.HttpUtils;

public class ShippingAddressActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView recyclerView_shippingaddres;
    ShippingAddressAdapter shippingAddressAdapter;
    private List<ShippingAddressPojo> shippingAddressPojoArraylist=new ArrayList<>();
    public static AVLoadingIndicatorView avi;
    ShippingAddressPojo shippingAddressPojo;
    SharedPreferences sharedlogin;
    SharedPreferences.Editor editlogin;
    String get_mail_from_reg,get_otp_from_reg,get_apikey_from_login,get_email_from_login,get_userid_from_login,get_username_from_login;
    private ViewPager pager;
    int currentPage;
    private TabLayout tabLayout;
    LinearLayout back_lv,noparts_found;
    TextView add_shipping;
    Typeface helvetica;
    Bundle bundle;
    public static String navigation_role;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shipping_list);

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(ShippingAddressActivity.this, v1);
        helvetica = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/helevetical.ttf");
        sharedlogin = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editlogin = sharedlogin.edit();
        get_mail_from_reg = sharedlogin.getString("get_mail_from_reg", "");
        get_otp_from_reg=sharedlogin.getString("get_otp_from_reg","");
        get_apikey_from_login = sharedlogin.getString("get_apikey_from_login", "");
        get_email_from_login=sharedlogin.getString("get_email_from_login","");
        get_userid_from_login = sharedlogin.getString("get_userid_from_login", "");
        get_username_from_login=sharedlogin.getString("get_username_from_login","");


        recyclerView_shippingaddres=findViewById(R.id.recyclerView_shippingaddres);
        pager=findViewById(R.id.viewpager);
        avi=findViewById(R.id.avi);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);
        back_lv=findViewById(R.id.back_lv);
        add_shipping=findViewById(R.id.add_shipping);
        pager.setOffscreenPageLimit(1);
        setupViewPager(pager);
        noparts_found=findViewById(R.id.noparts_found);
        noparts_found.setVisibility(View.GONE);

        setupTabIcons();


        pager.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;
            }
        });

        LinearLayoutManager manager1 = new LinearLayoutManager(getApplicationContext());
        recyclerView_shippingaddres.setLayoutManager(manager1);
        recyclerView_shippingaddres.setHasFixedSize(true);
        shippingAddressAdapter = new ShippingAddressAdapter(this, shippingAddressPojoArraylist);
        recyclerView_shippingaddres.setAdapter(shippingAddressAdapter);
        bundle= getIntent().getExtras();
        //Extract the data…

        navigation_role = bundle.getString("ADDRESS_PAGE_BACKNAV_NAME");
        Log.e("tag","printing navigation"+navigation_role);


        add_shipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),ShippingAddress.class);
                startActivity(i);
                finish();
            }
        });

        back_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor edit = shared.edit();
                edit.putString("status_condition","address_false");
                edit.commit();






                if(navigation_role.equals("viewcart"))
                {
                    Intent back=new Intent(getApplicationContext(),ViewCartActivity.class);
                    startActivity(back);
                    finish();
                }
                else if(navigation_role.equals("pricequoted"))
                {
                    Intent back=new Intent(getApplicationContext(),PriceQuotedPageActivity.class);
                    startActivity(back);
                    finish();
                }

            }
        });


        new CallShippingAddress_Task().execute();


        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                currentPage = position;
                switch (position) {
                    case 0:

                        break;

                    case 1:

                        break;

                    case 2:

                        break;

                    case 3:

                        break;

                    case 4:

                        break;

                    case 5:

                        break;


                    default:

                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new ProductImfoFragment(), "Product Info");
        adapter.addFrag(new ShippingInfoFragment(), "Shipping Info");
        adapter.addFrag(new CustomerInfoFragment(), "Customer Info");
        adapter.addFrag(new ReturnPolicyFragment(), "Return Policy");
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        View v2 = tabLayout.getRootView();
        //FontsOverride.overrideFonts(getApplicationContext(), v2);

        TextView tabOne = (TextView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.customtabgroup_title, null);
        tabOne.setText("Product Info");
        tabOne.setMinimumWidth(0);
        tabOne.setTextSize(17);
        tabOne.setTypeface(helvetica);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.customtabgroup_title, null);
        tabTwo.setText("Shipping Info");
        tabTwo.setMinimumWidth(0);
        tabTwo.setTypeface(helvetica);
        tabTwo.setTextSize(17);
        tabTwo.setGravity(View.TEXT_ALIGNMENT_CENTER);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.customtabgroup_title, null);
        tabThree.setText("Customer Info");
        tabThree.setMinimumWidth(0);
        tabThree.setTypeface(helvetica);
        tabThree.setTextSize(17);
        tabThree.setGravity(View.TEXT_ALIGNMENT_CENTER);
        tabLayout.getTabAt(2).setCustomView(tabThree);


        TextView tabFour = (TextView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.customtabgroup_title, null);
        tabFour.setText("Return Policy");
        tabFour.setMinimumWidth(0);
        tabFour.setTypeface(helvetica);
        tabFour.setTextSize(17);
        tabFour.setGravity(View.TEXT_ALIGNMENT_CENTER);
        tabLayout.getTabAt(3).setCustomView(tabFour);


        tabLayout.setTabTextColors(
                getResources().getColor(R.color.textcolor),
                getResources().getColor(R.color.background_white_color)
        );

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    //Shiping Address API CALL: ---------------------------------------------------------------------->
    public class CallShippingAddress_Task extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog;


        protected void onPreExecute() {
            super.onPreExecute();
            avi.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                json = jsonObject.toString();

                return jsonStr = HttpUtils.makeRequestToken(Config.WEB_URL_LIST_SHIPPING_DETAILS+get_userid_from_login,get_apikey_from_login);

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            avi.setVisibility(View.GONE);

            try{
                JSONObject jo = new JSONObject(s);
                String status = jo.getString("status");
                Log.e("tag","1");

                if (status.equals("true")) {
                    Log.e("tag","2");
                    String message=jo.getString("message");
                    Log.e("tag","3");

                    JSONObject data_object=jo.getJSONObject("data");
                    JSONArray address_array=data_object.getJSONArray("shippingAddress");
                    Log.e("tag","4");
                    if(address_array.length()==0)
                    {
                        Log.e("tag","5");
                        noparts_found.setVisibility(View.VISIBLE);
                    }
                    else {
                         noparts_found.setVisibility(View.GONE);

                        for(int i=0;i<address_array.length();i++)
                        {
                            try {
                                Log.e("tag","6");
                                shippingAddressPojo = new ShippingAddressPojo();
                                JSONObject jsonObject = address_array.getJSONObject(i);
                                shippingAddressPojo.id=jsonObject.getString("id");
                                Log.e("tag","7");
                                shippingAddressPojo.userId=jsonObject.getString("userId");
                                Log.e("tag","8");
                                shippingAddressPojo.firstName=jsonObject.getString("firstName");
                                Log.e("tag","9");
                                shippingAddressPojo.lastName=jsonObject.getString("lastName");
                                Log.e("tag","10");
                                shippingAddressPojo.addressLine1=jsonObject.getString("addressLine1");
                                Log.e("tag","11");
                                shippingAddressPojo.addressLine2=jsonObject.getString("addressLine2");
                                Log.e("tag","12");
                                shippingAddressPojo.city=jsonObject.getString("city");
                                Log.e("tag","13");
                                shippingAddressPojo.state=jsonObject.getString("state");
                                Log.e("tag","14");
                                shippingAddressPojo.country=jsonObject.getString("country");
                                Log.e("tag","15");
                                shippingAddressPojo.phone=jsonObject.getString("phone");
                                shippingAddressPojo.email=jsonObject.getString("email");

                                shippingAddressPojoArraylist.add(shippingAddressPojo);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }


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
            recyclerView_shippingaddres.setAdapter(shippingAddressAdapter);


        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();


        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor edit = shared.edit();
        edit.putString("status_condition","address_false");
        edit.commit();





        if(navigation_role.equals("viewcart"))
        {
            Intent back=new Intent(getApplicationContext(),ViewCartActivity.class);
            startActivity(back);
            finish();
        }
        else if(navigation_role.equals("pricequoted"))
        {
            Intent back=new Intent(getApplicationContext(),PriceQuotedPageActivity.class);
            startActivity(back);
            finish();
        }

    }
}
