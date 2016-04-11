package com.rockgarden.myapp.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.rockgarden.myapp.R;
import com.rockgarden.myapp.adpater.PagerAdapter_Fragment;

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
    ViewPager recordViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        setupViewPager();
        if (savedInstanceState == null) {
            pendingIntroAnimation = true;
        }
    }

    /**
     * 设定ViewPager
     * 主要是setAdapter
     */
    private void setupViewPager() {
        toolbar_layout.setTitle(getString(R.string.title_activity_viewpager));
        recordViewPager.setAdapter(new PagerAdapter_Fragment(getSupportFragmentManager(),
                ViewPagerActivity.this));
        //initTabs(); //在PagerAdapter完成title设定
        recordTabs.setupWithViewPager(recordViewPager);
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
}
