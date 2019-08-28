package com.sqindia.www.auto360parts_M.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.sqindia.www.auto360parts_M.Fragment.upload.FirstFragment;
import com.sqindia.www.auto360parts_M.Fragment.upload.FourthFragment;
import com.sqindia.www.auto360parts_M.Fragment.upload.SecondFragment;
import com.sqindia.www.auto360parts_M.Fragment.upload.ThirdFragment;
import com.sqindia.www.auto360parts_M.R;
import com.sqindia.www.auto360parts_M.Transformations.GateTransformation;
import com.sqindia.www.auto360parts_M.Adapter.MyPagerAdapter;



public class TransformationActivity extends AppCompatActivity {

    ViewPager viewPager;
    MyPagerAdapter pagerAdapter;
    Intent intent;
    public static String get_car1,get_car2,get_vin1,get_vin2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transformation);

        intent=getIntent();
        get_car1=intent.getStringExtra("pull_car1");
        get_car2=intent.getStringExtra("pull_car2");
        get_vin1=intent.getStringExtra("pull_vin1");
        get_vin2=intent.getStringExtra("pull_vin2");

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        addingFragmentsTOpagerAdapter();
        viewPager.setAdapter(pagerAdapter);
        GateTransformation gateTransformation = new GateTransformation();
        viewPager.setPageTransformer(true, gateTransformation);
    }


    private void addingFragmentsTOpagerAdapter() {
        pagerAdapter.addFragments(new FirstFragment());
        pagerAdapter.addFragments(new SecondFragment());
        pagerAdapter.addFragments(new ThirdFragment());
        pagerAdapter.addFragments(new FourthFragment());
    }

}