package autohubb.vinture.com.autohubb.MyOrdersModule;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import autohubb.vinture.com.autohubb.R;
import autohubb.vinture.com.autohubb.fonts.FontsOverride;



public class MyOrderMainPageFragment extends Fragment{
    private ViewPager viewPager;
    private TabLayout tabLayout;
    Typeface helvetica;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.a_trackorder, container, false);
        View v1 = getActivity().getWindow().getDecorView().getRootView();

        Intent intent = getActivity().getIntent();
        /*if (intent != null) {
            String value = intent.getStringExtra("PAGE");

            if(value.equals("ORDER_PAGE"))
            {
                viewPager.setCurrentItem(2);
            }
            else
            {
                viewPager.setCurrentItem(0);
            }

        }*/



        FontsOverride.overrideFonts(getActivity(), v1);
        helvetica = Typeface.createFromAsset(getActivity().getAssets(), "fonts/helevetical.ttf");
        viewPager = (ViewPager)view. findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setTabTextColors(Color.parseColor("#000000") ,Color.parseColor("#FFFFFF"));
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        return view;
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFrag(new MyRequestFragment(), "My Requests");
        adapter.addFrag(new MyQuotesFragment(), "My Quotes");
        adapter.addFrag(new MyOrdersFragment(), "My Orders");
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        View v2 = tabLayout.getRootView();
        FontsOverride.overrideFonts(getActivity(), v2);

        TextView tabOne = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.customtabgroup_title, null);
        tabOne.setText("My Requests");
        tabOne.setMinimumWidth(0);
        tabOne.setTextSize(20);
        tabOne.setTypeface(helvetica);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.customtabgroup_title, null);
        tabTwo.setText("My Quotes");
        tabTwo.setMinimumWidth(0);
        tabTwo.setTypeface(helvetica);
        tabTwo.setTextSize(20);
        tabTwo.setGravity(View.TEXT_ALIGNMENT_CENTER);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.customtabgroup_title, null);
        tabThree.setText("My Orders");
        tabThree.setMinimumWidth(0);
        tabThree.setTypeface(helvetica);
        tabThree.setTextSize(20);
        tabThree.setGravity(View.TEXT_ALIGNMENT_CENTER);
        tabLayout.getTabAt(2).setCustomView(tabThree);
        tabLayout.setTabTextColors(
                getResources().getColor(R.color.textcolor),
                getResources().getColor(R.color.background_white_color)
        );

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






}
