package com.citylinkdata.mycard.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.citylinkdata.mycard.R;
import com.citylinkdata.mycard.adapter.PagerAdapter_Fragment;

import butterknife.Bind;
import butterknife.OnClick;

public class PayToCardActivity extends BaseLayoutDrawerActivity {
    @Bind(R.id.toolbar_layout)
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
        setContentView(R.layout.activity_pay_to_card);
        toolbar_layout.setTitle(getString(R.string.title_activity_funds_to_card));
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 可改变toolbar的homeIcon
        recordViewPager.setAdapter(new PagerAdapter_Fragment(getSupportFragmentManager(),
                PayToCardActivity.this));
//        initTabs();
        recordTabs.setupWithViewPager(recordViewPager);
    }

    private void initTabs() {
        recordTabs.addTab(recordTabs.newTab().setText("One"));
        recordTabs.addTab(recordTabs.newTab().setText("Two"));
        recordTabs.addTab(recordTabs.newTab().setText("Three"));
    }

    @OnClick(R.id.recordFab)
    public void clickRecordFab(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onBackPressed() {
        ViewCompat.setElevation(getToolbar(), 0);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.base_options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
