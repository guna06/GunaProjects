package com.sqindia.autolane360mobile.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Display;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.sqindia.autolane360mobile.R;
import com.sqindia.autolane360mobile.font.FontsOverride;
import com.sqindia.autolane360mobile.fragment.AddCarPageOneFragment;
import com.sqindia.autolane360mobile.fragment.AddCarPageThreeFragment;
import com.sqindia.autolane360mobile.fragment.AddCarPageTwoFragment;
import com.sqindia.autolane360mobile.fragment.UploadCarPageOneFragment;
import com.sqindia.autolane360mobile.fragment.UploadCarPageThreeFragment;
import com.sqindia.autolane360mobile.fragment.UploadCarPageTwoFragment;
import java.util.ArrayList;
import java.util.List;

public class UploadCar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Typeface helevetical;
    SharedPreferences.Editor editor;
    private ViewPager pager;
    int currentPage;
    LinearLayout back;
    public static Button upload_part_btn;
    TextView next_fb,previous_fb,head_titletv;
    SharedPreferences.Editor edit;
    String str_mileage,str_location,str_vinstatus,str_interior,str_exterior,str_price,str_dealerinfo,str_description;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_parts);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        helevetical = Typeface.createFromAsset(getAssets(), "fonts/helevetical.ttf");
        View v1 = getWindow().getDecorView().getRootView();

        FontsOverride.overrideFonts(UploadCar.this, v1);
        //check value:
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        edit = sharedPreferences.edit();

        upload_part_btn=findViewById(R.id.upload_part_btn);
        next_fb=findViewById(R.id.next_fb);
        previous_fb=findViewById(R.id.previous_fb);
        pager=findViewById(R.id.viewpager);
        back=findViewById(R.id.back);
        head_titletv=findViewById(R.id.head_titletv);


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        pager.setOffscreenPageLimit(1);
        setupViewPager(pager);

        pager.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;
            }
        });


        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                currentPage = position;
                switch (position) {
                    case 0:
                        head_titletv.setText("Add Car Details");
                        upload_part_btn.setVisibility(View.GONE);
                        next_fb.setVisibility(View.VISIBLE);
                        previous_fb.setVisibility(View.GONE);
                        break;

                    case 1:
                        head_titletv.setText("Add Car Details");
                        upload_part_btn.setVisibility(View.GONE);
                        next_fb.setVisibility(View.VISIBLE);
                        previous_fb.setVisibility(View.VISIBLE);
                        break;

                    case 2:
                        head_titletv.setText("Add Car Details");
                        upload_part_btn.setVisibility(View.GONE);
                        next_fb.setVisibility(View.VISIBLE);
                        previous_fb.setVisibility(View.VISIBLE);
                        break;

                    case 3:
                        head_titletv.setText("Upload Car Images");
                        upload_part_btn.setVisibility(View.GONE);
                        next_fb.setVisibility(View.VISIBLE);
                        previous_fb.setVisibility(View.VISIBLE);
                        break;

                    case 4:
                        head_titletv.setText("Upload Car Images");
                        upload_part_btn.setVisibility(View.GONE);
                        next_fb.setVisibility(View.VISIBLE);
                        previous_fb.setVisibility(View.VISIBLE);
                        break;

                    case 5:
                        head_titletv.setText("Upload Car Images");
                        upload_part_btn.setVisibility(View.GONE);
                        next_fb.setVisibility(View.VISIBLE);
                        previous_fb.setVisibility(View.VISIBLE);
                        break;


                    default:
                        next_fb.setVisibility(View.VISIBLE);
                        previous_fb.setVisibility(View.GONE);
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


        previous_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentPage == 0) {

                    previous_fb.setVisibility(View.GONE);


                }
                else if (currentPage == 1) {



                    currentPage=currentPage-1;
                    pager.setCurrentItem(currentPage, true);

                }
                else if (currentPage == 2) {
                    currentPage=currentPage-1;
                    pager.setCurrentItem(currentPage, true);
                }
                else if (currentPage == 3) {


                    currentPage=currentPage-1;
                    pager.setCurrentItem(currentPage, true);
                }
                else if (currentPage == 4) {



                    currentPage=currentPage-1;
                    pager.setCurrentItem(currentPage, true);

                }
                else if (currentPage == 5) {

                    currentPage=currentPage-1;
                    pager.setCurrentItem(currentPage, true);
                }


            }
        });



        next_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
                //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% PAGE ONE %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
                if (currentPage == 0) {

                    //str_date=AddCarPageOneFragment.date_edt.getText().toString().trim();
                    str_mileage= AddCarPageOneFragment.input_mileage.getText().toString().trim();
                    str_location=AddCarPageOneFragment.input_location.getText().toString().trim();
                    str_vinstatus= AddCarPageOneFragment.input_vinstatus.getText().toString().trim();
                    //str_vehicle=AddCarPageOneFragment.input_vehicleclass.getText().toString().trim();
                    str_interior=AddCarPageOneFragment.input_interior.getText().toString().trim();
                    str_exterior= AddCarPageOneFragment.input_exterior.getText().toString().trim();
                    str_price=AddCarPageOneFragment.input_carprice.getText().toString().trim();


                 if(!TextUtils.isEmpty(AddCarPageOneFragment.input_mileage.getText()))
                 {
                     if(!TextUtils.isEmpty(AddCarPageOneFragment.input_carprice.getText()))
                     {
                         //edit.putString("store_date", str_date);
                         edit.putString("store_mileage",str_mileage);
                         edit.putString("store_location", str_location);
                         edit.putString("store_vinstatus",str_vinstatus);
                         //edit.putString("store_vehicle", str_vehicle);
                         edit.putString("store_interior",str_interior);
                         edit.putString("store_exterior", str_exterior);
                         edit.putString("store_price",str_price);
                         edit.commit();
                         upload_part_btn.setVisibility(View.GONE);
                         currentPage=currentPage+1;
                         pager.setCurrentItem(currentPage, true);
                     }
                     else
                     {
                         AddCarPageOneFragment.input_carprice.requestFocus();
                         AddCarPageOneFragment.input_carprice.setError( "Car Price is Required!" );
                     }
                 }
                 else
                 {
                     AddCarPageOneFragment.input_mileage.requestFocus();
                     AddCarPageOneFragment.input_mileage.setError( "Mileage is Required!" );
                 }


                }







                //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% PAGE TWO %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
                else if (currentPage == 1) {

                 /*currentPage=currentPage +1;
                   pager.setCurrentItem(currentPage, true);*/

                    if(!AddCarPageTwoFragment.str_year.equals("Select Year *"))
                    {
                        if(!AddCarPageTwoFragment.str_carmake.equals("Select Car Make *"))
                        {
                            if(!AddCarPageTwoFragment.str_carmodel.equals("Select Car Model *"))
                            {

                                                edit.putString("store_caryear", AddCarPageTwoFragment.str_year);
                                                edit.putString("store_carmake", AddCarPageTwoFragment.str_carmake);
                                                edit.putString("store_carmodel", AddCarPageTwoFragment.str_carmodel);
                                                edit.putString("store_cartype", AddCarPageTwoFragment.str_cartypes);
                                                edit.putString("store_carstartcode", AddCarPageTwoFragment.str_startcodes);
                                                edit.putString("store_carengine", AddCarPageTwoFragment.str_engines);
                                                edit.putString("store_cartransmision", AddCarPageTwoFragment.str_transmission);
                                                edit.commit();

                                                upload_part_btn.setVisibility(View.GONE);
                                                currentPage=currentPage+1;
                                                pager.setCurrentItem(currentPage, true);

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Please Select Car Model",Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Please Select Car Make",Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {

                        Toast.makeText(getApplicationContext(),"Please Select Year",Toast.LENGTH_LONG).show();
                    }
                }








                //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% PAGE THREE %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
                else if (currentPage == 2) {

                    str_dealerinfo=AddCarPageThreeFragment.input_dealer_info.getText().toString();
                    str_description=AddCarPageThreeFragment.input_description.getText().toString();



                                            edit.putString("store_doors", AddCarPageThreeFragment.str_doors);
                                            edit.putString("store_manufacture", AddCarPageThreeFragment.str_manufacture);
                                            edit.putString("store_AcCondition", AddCarPageThreeFragment.str_sccondition);
                                            edit.putString("store_Keys", AddCarPageThreeFragment.str_keys);
                                            edit.putString("store_dealerinfo", AddCarPageThreeFragment.input_dealer_info.getText().toString());
                                            edit.putString("store_cardescription", AddCarPageThreeFragment.input_description.getText().toString());
                                            edit.commit();
                                              upload_part_btn.setVisibility(View.GONE);
                                            currentPage=currentPage+1;
                                            pager.setCurrentItem(currentPage, true);

                }




                //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% PAGE FOUR %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
                else if (currentPage == 3) {



                    if(UploadCarPageOneFragment.front_view_iv.getDrawable()!=null)
                    {
                        upload_part_btn.setVisibility(View.GONE);
                        currentPage=currentPage+1;
                        pager.setCurrentItem(currentPage, true);

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please Capture/Select Front View Image",Toast.LENGTH_LONG).show();
                }




                }


                //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% PAGE FIVE %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
                else if (currentPage == 4) {

                                     upload_part_btn.setVisibility(View.GONE);
                                     currentPage=currentPage+1;
                                     pager.setCurrentItem(currentPage, true);

                }



                //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% PAGE SIX %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
                else if (currentPage == 5) {

                    upload_part_btn.setVisibility(View.VISIBLE);
                    next_fb.setVisibility(View.GONE);

                       /*if(UploadCarPageThreeFragment.birdeyeview_vack_iv.getDrawable()!=null)
                        {
                            if(UploadCarPageThreeFragment.front_seatview_iv.getDrawable()!=null)
                            {

                                if(UploadCarPageThreeFragment.video_img.getDrawable()!=null)
                                {
                                    upload_part_btn.setVisibility(View.VISIBLE);
                                    next_fb.setVisibility(View.GONE);
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Please Select Video",Toast.LENGTH_LONG).show();

                                }


                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Please Capture/Select Front Seat View with Open",Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Please Capture/Select Bird Eye View Back",Toast.LENGTH_LONG).show();
                        }*/
                    }
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back=new Intent(getApplicationContext(),DashboardActivity.class);
                startActivity(back);
                finish();
            }
        });
    }





    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new AddCarPageOneFragment(), "page1");
        adapter.addFrag(new AddCarPageTwoFragment(), "page2");
        adapter.addFrag(new AddCarPageThreeFragment(), "page3");
        adapter.addFrag(new UploadCarPageOneFragment(), "page4");
        adapter.addFrag(new UploadCarPageTwoFragment(), "page5");
        adapter.addFrag(new UploadCarPageThreeFragment(), "page6");

        viewPager.setAdapter(adapter);
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


    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent back=new Intent(getApplicationContext(),DashboardActivity.class);
        startActivity(back);
        finish();
    }



    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

}
