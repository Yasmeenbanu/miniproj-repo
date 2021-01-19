package com.example.wasteu;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class InformationActivity extends AppCompatActivity {
    ViewPager mViewPager;
    TabLayout mTabLayout;
    Toolbar mTolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        ActionBar actionBar= getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#384950")));


        mViewPager = (ViewPager) findViewById(R.id.viewpage);
       // mTolBar = (Toolbar) findViewById(R.id.toolbar);
        mTabLayout = (TabLayout) findViewById(R.id.tablayout);

    //    setSupportActionBar(mTolBar);
        MyFragmentAdpater adapter = new MyFragmentAdpater(getSupportFragmentManager());
        adapter.add(new RecycleActivity(), "Recyclable Wastes");
       // adapter.add(new NRecycleActivity(), "Non Recyclable Wastes");

        mViewPager.setAdapter(adapter);

        mTabLayout.setupWithViewPager(mViewPager);
    }

    private class MyFragmentAdpater extends FragmentPagerAdapter {

        List<Fragment> mList = new ArrayList<>();
        List<String> mTitleList = new ArrayList<>();
        public MyFragmentAdpater(FragmentManager fm) {
            super(fm);
        }
        public void add(Fragment fragement, String title){
            mList.add(fragement);
            mTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mList.get(position);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);
        }
    }
}
