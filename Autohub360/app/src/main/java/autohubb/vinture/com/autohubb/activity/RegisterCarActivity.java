package autohubb.vinture.com.autohubb.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import autohubb.vinture.com.autohubb.R;
import autohubb.vinture.com.autohubb.fonts.FontsOverride;
import autohubb.vinture.com.autohubb.fragment.ViewCommercialCar;


public class RegisterCarActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TabLayout tabLayout;
    private ViewPager pager;
    Typeface helevetical;
    LinearLayout back_lv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_car_activity);
        helevetical = Typeface.createFromAsset(getAssets(), "fonts/helevetical.ttf");

        View v1 = getWindow().getDecorView().getRootView();
        FontsOverride.overrideFonts(RegisterCarActivity.this, v1);


        pager=findViewById(R.id.viewpager);
        back_lv=findViewById(R.id.back_lv);

        setupViewPager(pager);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);


        back_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });




        setupTabIcons();


    }



    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
       // adapter.addFrag(new ViewPrivateCar(), "Private Car");
        adapter.addFrag(new ViewCommercialCar(), "Commercial Car");
        viewPager.setAdapter(adapter);
    }



    private void setupTabIcons() {
        View v2 = tabLayout.getRootView();
        FontsOverride.overrideFonts(RegisterCarActivity.this, v2);

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.customtab_title, null);
        tabOne.setText("  Private Car    ");
        tabOne.setTextSize(23);
        tabOne.setTextColor(getResources().getColor(R.color.blackcolor));
        tabOne.setTypeface(helevetical);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.customtab_title, null);
        tabTwo.setText("   Commercial Car");
        tabOne.setTextColor(getResources().getColor(R.color.blackcolor));
        tabTwo.setTypeface(helevetical);
        tabTwo.setTextSize(23);
        tabTwo.setGravity(View.TEXT_ALIGNMENT_TEXT_START);
        tabLayout.getTabAt(1).setCustomView(tabTwo);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}



