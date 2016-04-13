package com.rockgarden.myapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.rockgarden.myapp.R;
import com.rockgarden.myapp.fragment.PayFragment;
import com.rockgarden.myapp.fragment.PicturesFragment;
import com.rockgarden.myapp.fragment.RecordsFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class ViewPagerActivity extends BaseLayoutDrawerActivity {

    @Bind(R.id.collapse_toolbar_layout)
    CollapsingToolbarLayout toolbar_layout;
    @Bind(R.id.record_tablayout)
    TabLayout recordTabs;
    @Bind(R.id.recordFab)
    FloatingActionButton recordFab;
    @Bind(R.id.record_viewpager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        toolbar_layout.setTitle(getString(R.string.title_activity_viewpager));
        if (viewPager != null) {
            setupViewPager();
        }
        //initTabs(); //一般在PagerAdapter中完成title设定
        recordTabs.setupWithViewPager(viewPager);
        if (savedInstanceState == null) {
            pendingIntroAnimation = true;
        }
    }

    /**
     * 设定ViewPager
     * setAdapter by FragmentStatePagerAdapter
     */
    private void setupViewPager() {
        viewPager.setAdapter(new PagerAdapter_Fragment(getSupportFragmentManager(),
                ViewPagerActivity.this));
    }

    //setAdapter by FragmentPagerAdapter
    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new RecordsFragment(), "Tab 1");
        adapter.addFragment(new PicturesFragment(), "Tab 2");
        viewPager.setAdapter(adapter);
    }

    private void initTabs() {
        recordTabs.addTab(recordTabs.newTab().setText("One"));
        recordTabs.addTab(recordTabs.newTab().setText("Two"));
    }

    @OnClick(R.id.recordFab)
    public void clickRecordFab(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    static class PagerAdapter_Fragment extends FragmentStatePagerAdapter {

        CharSequence Titles[];
        private String tabTitles[] = new String[]{"Tab1", "Tab2", "Tab3"};
        int PAGE_COUNT = 3;
        private Context context;

        public PagerAdapter_Fragment(FragmentManager fm, CharSequence mTitles[],
                                     int mNumbOfTabsumb) {
            super(fm);
            this.Titles = mTitles;
            this.PAGE_COUNT = mNumbOfTabsumb;
        }

        public PagerAdapter_Fragment(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return PayFragment.newInstance(position + 1);
                case 1:
                    return RecordsFragment.newInstance(position + 1);
                case 2:
                    return PicturesFragment.newInstance(position + 1);
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

    }
}
