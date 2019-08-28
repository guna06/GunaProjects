package com.sqindia.www.auto360parts_M.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sqindia.www.auto360parts_M.Fragment.upload.BodyFragment;
import com.sqindia.www.auto360parts_M.Fragment.upload.ElectricalsFragment;
import com.sqindia.www.auto360parts_M.Fragment.upload.EngineFragment;
import com.sqindia.www.auto360parts_M.Fragment.upload.InteriorFragment;
import com.sqindia.www.auto360parts_M.Fragment.upload.LightsFragment;
import com.sqindia.www.auto360parts_M.Fragment.upload.RimWheelFragment;
import com.sqindia.www.auto360parts_M.Fragment.upload.TransmissionFragment;
import com.sqindia.www.auto360parts_M.Font.FontsOverride;
import com.sqindia.www.auto360parts_M.Model.PhotoItem;
import com.sqindia.www.auto360parts_M.R;


import java.util.ArrayList;
import java.util.List;

public class UploadCarParts extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Typeface helevetical;
    SharedPreferences.Editor editor;
    private TabLayout tabLayout;
    private ViewPager pager;
    LinearLayout back;
    int currentPage;
    String str_path_eng_engine1, str_path_eng_engine2,imageUri;
    PhotoItem getItem=new PhotoItem();
    public static Button upload_part_btn;
    public PhotoItem photoItem;
    TextView next_fb,previous_fb;
    SharedPreferences sharedPrefces;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_parts);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        helevetical = Typeface.createFromAsset(getAssets(), "fonts/helevetical.ttf");
        View v1 = getWindow().getDecorView().getRootView();

        FontsOverride.overrideFonts(UploadCarParts.this, v1);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(UploadCarParts.this);
        editor = sharedPreferences.edit();

        upload_part_btn=findViewById(R.id.upload_part_btn);
        next_fb=findViewById(R.id.next_fb);
        previous_fb=findViewById(R.id.previous_fb);
        pager=findViewById(R.id.viewpager);
        back=findViewById(R.id.back);
        setupViewPager(pager);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);




        setupTabIcons();

        sharedPrefces = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        edit = sharedPrefces.edit();

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                currentPage = position;
                switch (position) {
                    case 0:
                        String str_enginebox1= EngineFragment.engine_box1.getText().toString().trim();
                        String str_enginebox2=EngineFragment.engine_box2.getText().toString().trim();
                        String str_enginebox3=EngineFragment.engine_box3.getText().toString().trim();
                        String str_enginebox4=EngineFragment.engine_box4.getText().toString().trim();
                        String str_enginebox5=EngineFragment.engine_others.getText().toString().trim();

                        if(str_enginebox1.equals("Engine Pics"))
                        {
                            str_enginebox1="engine_pics";
                        }

                        if(str_enginebox2.equals("A/C Pics"))
                        {
                            str_enginebox2="ac_pics";
                        }

                        if(str_enginebox3.equals("Emmission/Exhausts Pics"))
                        {
                            str_enginebox3="emmission_exhausts_pics";
                        }

                        if(str_enginebox4.equals("Fuel Delivery Pics"))
                        {
                            str_enginebox4="fuel_delivery_pics";
                        }


                        String merge_engine=str_enginebox1+","+str_enginebox2+","+str_enginebox3+","+str_enginebox4;
                        edit.putString("engine_heads", merge_engine);
                        edit.commit();
                        upload_part_btn.setVisibility(View.GONE);
                        next_fb.setVisibility(View.VISIBLE);
                        previous_fb.setVisibility(View.GONE);
                        break;
                    case 1:


                        String str_bodybox1= BodyFragment.body_box1.getText().toString().trim();
                        String str_bodybox2=BodyFragment.body_box2.getText().toString().trim();
                        String str_bodybox3=BodyFragment.body_box3.getText().toString().trim();

                        String str_bodybox5=BodyFragment.body_others.getText().toString().trim();

                        if(str_bodybox1.equals("Doors"))
                        {
                            str_bodybox1="doors";
                        }

                        if(str_bodybox2.equals("Bumpers"))
                        {
                            str_bodybox2="bumpers";
                        }


                        if(str_bodybox3.equals("Hoods"))
                        {
                            str_bodybox3="hoods";
                        }


                        String merge_body=str_bodybox1+","+str_bodybox2+","+str_bodybox3;
                        edit.putString("body_heads", merge_body);
                        edit.commit();
                        upload_part_btn.setVisibility(View.GONE);
                        next_fb.setVisibility(View.VISIBLE);
                        previous_fb.setVisibility(View.VISIBLE);
                        break;
                    case 2:


                        String str_transbox1= TransmissionFragment.transmission_box1.getText().toString().trim();
                        String str_transbox2=TransmissionFragment.transmission_box2.getText().toString().trim();


                        String str_transbox5=TransmissionFragment.transmission_others.getText().toString().trim();

                        if(str_transbox1.equals("Automatic"))
                        {
                            str_transbox1="automatic";
                        }

                        if(str_transbox2.equals("Manual"))
                        {
                            str_transbox2="manual";
                        }





                        String merge_transmission=str_transbox1+","+str_transbox2;
                        edit.putString("transmission_heads", merge_transmission);
                        edit.commit();
                        upload_part_btn.setVisibility(View.GONE);
                        next_fb.setVisibility(View.VISIBLE);
                        previous_fb.setVisibility(View.VISIBLE);
                        break;
                    case 3:




                        String str_interiorbox1= InteriorFragment.interior_box1.getText().toString().trim();
                        String str_interiorbox2=InteriorFragment.interior_box2.getText().toString().trim();
                        String str_interiorbox3=InteriorFragment.interior_box3.getText().toString().trim();
                        String str_interiorbox4=InteriorFragment.interior_box4.getText().toString().trim();
                        String str_interiorbox5=InteriorFragment.interior_others.getText().toString().trim();

                        if(str_interiorbox1.equals("Seats"))
                        {
                            str_interiorbox1="seats";
                        }

                        if(str_interiorbox2.equals("Steering"))
                        {
                            str_interiorbox2="steering";
                        }


                        if(str_interiorbox3.equals("Dashboards"))
                        {
                            str_interiorbox3="dashboard";
                        }

                        if(str_interiorbox4.equals("Radios"))
                        {
                            str_interiorbox4="radios";
                        }


                        String merge_interior=str_interiorbox1+","+str_interiorbox2+","+str_interiorbox3+","+str_interiorbox4;
                        edit.putString("interior_heads", merge_interior);
                        edit.commit();
                        upload_part_btn.setVisibility(View.GONE);
                        next_fb.setVisibility(View.VISIBLE);
                        previous_fb.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        String str_rimbox1= RimWheelFragment.rim_box1.getText().toString().trim();
                        String str_rimbox2=RimWheelFragment.rim_box2.getText().toString().trim();
                        String str_rimbox3=RimWheelFragment.rim_box3.getText().toString().trim();
                        String str_rimbox4=RimWheelFragment.rim_box4.getText().toString().trim();
                        String str_rimbox5=RimWheelFragment.rim_others.getText().toString().trim();

                        if(str_rimbox1.equals("Brake"))
                        {
                            str_rimbox1="brake";
                        }

                        if(str_rimbox2.equals("Suspensions"))
                        {
                            str_rimbox2="suspensions";
                        }


                        if(str_rimbox3.equals("Rims"))
                        {
                            str_rimbox3="rims";
                        }

                        if(str_rimbox4.equals("Wheels"))
                        {
                            str_rimbox4="wheel";
                        }


                        String merge_rimwheel=str_rimbox1+","+str_rimbox2+","+str_rimbox3+","+str_rimbox4;
                        edit.putString("rimwheel_heads", merge_rimwheel);
                        edit.commit();
                        upload_part_btn.setVisibility(View.GONE);
                        next_fb.setVisibility(View.VISIBLE);
                        previous_fb.setVisibility(View.VISIBLE);
                        break;
                    case 5:



                        String str_electricalbox1= ElectricalsFragment.electrical_box1.getText().toString().trim();
                        String str_electricalbox2=ElectricalsFragment.electrical_box2.getText().toString().trim();
                        String str_electricalbox3=ElectricalsFragment.electrical_box3.getText().toString().trim();
                        String electricalbox4=ElectricalsFragment.electrical_box4.getText().toString().trim();
                        String str_electricalbox5=ElectricalsFragment.electrical_others.getText().toString().trim();

                        if(str_electricalbox1.equals("Oil Pressure Switch"))
                        {
                            str_electricalbox1="oil_pressure_switch";
                        }

                        if(str_electricalbox2.equals("Ignition Lock Cylinder"))
                        {
                            str_electricalbox2="ignition_lock_cylinder";
                        }

                        if(str_electricalbox3.equals("Ignition Switch"))
                        {
                            str_electricalbox3="ignition_switch";
                        }

                        if(electricalbox4.equals("Window Lift Motor"))
                        {
                            electricalbox4="window_lift_motor";
                        }


                        String merge_electrical=str_electricalbox1+","+str_electricalbox2+","+str_electricalbox3+","+electricalbox4;
                        edit.putString("electrical_heads", merge_electrical);
                        edit.commit();
                        upload_part_btn.setVisibility(View.GONE);
                        next_fb.setVisibility(View.VISIBLE);
                        previous_fb.setVisibility(View.VISIBLE);

                        break;

                    case 6:
                        String str_lightsbox1= LightsFragment.lights_box1.getText().toString().trim();
                        String str_lightsbox2=LightsFragment.lights_box2.getText().toString().trim();
                        String str_lightsbox3=LightsFragment.lights_box3.getText().toString().trim();
                        String str_lightsbox5=LightsFragment.lights_others.getText().toString().trim();

                        if(str_lightsbox1.equals("Head Lamps"))
                        {
                            str_lightsbox1="head_lamps";
                        }

                        if(str_lightsbox2.equals("Brake Lights"))
                        {
                            str_lightsbox2="brake_lights";
                        }


                        if(str_lightsbox3.equals("Headlight Assembly"))
                        {
                            str_lightsbox3="headlight_assembly";
                        }




                        String merge_lights=str_lightsbox1+","+str_lightsbox2+","+str_lightsbox3;
                        edit.putString("lights_heads", merge_lights);
                        edit.commit();
                        upload_part_btn.setVisibility(View.VISIBLE);
                        next_fb.setVisibility(View.GONE);
                        previous_fb.setVisibility(View.VISIBLE);
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

                else if (currentPage == 6) {


                    currentPage=currentPage-1;
                    pager.setCurrentItem(currentPage, true);
                }
            }
        });




        next_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentPage == 0) {

                    currentPage=currentPage+1;
                    pager.setCurrentItem(currentPage, true);


                }
                else if (currentPage == 1) {



                    currentPage=currentPage+1;
                    pager.setCurrentItem(currentPage, true);

                }
                else if (currentPage == 2) {





                    currentPage=currentPage+1;
                    pager.setCurrentItem(currentPage, true);
                }
                else if (currentPage == 3) {


                    currentPage=currentPage+1;
                    pager.setCurrentItem(currentPage, true);
                }
                else if (currentPage == 4) {



                    currentPage=currentPage+1;
                    pager.setCurrentItem(currentPage, true);

                }
                else if (currentPage == 5) {


                    currentPage=currentPage+1;
                    pager.setCurrentItem(currentPage, true);
                }

                else if (currentPage == 6) {

                next_fb.setVisibility(View.VISIBLE);
                }


            }
        });



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back=new Intent(getApplicationContext(),PartsEntryActivity.class);
                startActivity(back);
                finish();
            }
        });


    }


    private void setupTabIcons() {
        View v2 = tabLayout.getRootView();
        FontsOverride.overrideFonts(UploadCarParts.this, v2);

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.customtab_title, null);
        tabOne.setText("Engine");
        tabOne.setTextSize(19);
        tabOne.setTextColor(getResources().getColor(R.color.blackcolor));
        tabOne.setTypeface(helevetical);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.customtab_title, null);
        tabTwo.setText("Body");
        tabOne.setTextColor(getResources().getColor(R.color.blackcolor));
        tabTwo.setTypeface(helevetical);
        tabTwo.setTextSize(19);
        tabTwo.setGravity(View.TEXT_ALIGNMENT_TEXT_START);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.customtab_title, null);
        tabThree.setText("Transmission");
        tabThree.setTypeface(helevetical);
        tabOne.setTextColor(getResources().getColor(R.color.blackcolor));
        tabThree.setTextSize(19);
        tabThree.setGravity(View.TEXT_ALIGNMENT_TEXT_START);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        TextView tabfour = (TextView) LayoutInflater.from(this).inflate(R.layout.customtab_title, null);
        tabfour.setText("Interior");
        tabfour.setSingleLine();
        tabfour.setTypeface(helevetical);
        tabfour.setTextSize(19);
        tabOne.setTextColor(getResources().getColor(R.color.blackcolor));
        tabfour.setGravity(View.TEXT_ALIGNMENT_TEXT_START);
        tabLayout.getTabAt(3).setCustomView(tabfour);


        TextView tabfive = (TextView) LayoutInflater.from(this).inflate(R.layout.customtab_title, null);
        tabfive.setText("Rim/Wheel/Brakes");
        tabfive.setSingleLine();
        tabfive.setTypeface(helevetical);
        tabfive.setTextSize(19);
        tabOne.setTextColor(getResources().getColor(R.color.blackcolor));
        tabfive.setGravity(View.TEXT_ALIGNMENT_TEXT_START);
        tabLayout.getTabAt(4).setCustomView(tabfive);


        TextView tabsix = (TextView) LayoutInflater.from(this).inflate(R.layout.customtab_title, null);
        tabsix.setText("Electricals");
        tabsix.setSingleLine();
        tabOne.setTextColor(getResources().getColor(R.color.blackcolor));
        tabsix.setTypeface(helevetical);
        tabsix.setTextSize(19);
        tabsix.setGravity(View.TEXT_ALIGNMENT_TEXT_START);
        tabLayout.getTabAt(5).setCustomView(tabsix);

        TextView tabseven = (TextView) LayoutInflater.from(this).inflate(R.layout.customtab_title, null);
        tabseven.setText("Lights");
        tabseven.setSingleLine();
        tabOne.setTextColor(getResources().getColor(R.color.blackcolor));
        tabseven.setTypeface(helevetical);
        tabseven.setTextSize(19);
        tabseven.setGravity(View.TEXT_ALIGNMENT_TEXT_START);
        tabLayout.getTabAt(6).setCustomView(tabseven);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new EngineFragment(), "Engine");
        adapter.addFrag(new BodyFragment(), "Body");
        adapter.addFrag(new TransmissionFragment(), "Transmission");
        adapter.addFrag(new InteriorFragment(), "Interior");
        adapter.addFrag(new RimWheelFragment(), "Rim");
        adapter.addFrag(new ElectricalsFragment(), "Electrical");
        adapter.addFrag(new LightsFragment(), "Lights");
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
    public void onBackPressed() {
        super.onBackPressed();
        Intent back=new Intent(getApplicationContext(),PartsEntryActivity.class);
        startActivity(back);
        finish();
    }

}
