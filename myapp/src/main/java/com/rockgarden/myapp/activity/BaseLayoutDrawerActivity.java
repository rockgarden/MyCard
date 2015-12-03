package com.rockgarden.myapp.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.rockgarden.myapp.R;

import butterknife.Bind;

/**
 * Created by rockgarden on 15/11/24.
 */
public class BaseLayoutDrawerActivity extends BaseLayoutActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;
    @Bind(R.id.nav_view)
    NavigationView navigationView;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentViewNoBind(R.layout.activity_base_drawer_layout);
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.rootContentViewGroup);
        LayoutInflater.from(this).inflate(layoutResID, viewGroup, true);
        BindViews();
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * 实现ActionBar.home按钮与DrawerLayout联动
     * !!建议仅当drawer_layout不遮盖toolbar时使用
     */
    private void setToggle() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState(); //NavigationIcon默认图标增加动画效果
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        if (getToolbar() != null) {
            getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //drawer.openDrawer(Gravity.LEFT); //打开DrawerLayout
                }
            });
        }
    }

    //@OnClick(R.id.drawerLayoutNavHeader) //NavigationView can't use butterknife,this is bug for Design 23
    public void navHeaderClick(final View v) {
        ViewGroup navHeader = (ViewGroup) findViewById(R.id.drawerLayoutNavHeader);
        navHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(Gravity.LEFT);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int[] startingLocation = new int[2];
                        v.getLocationOnScreen(startingLocation);
                        startingLocation[0] += v.getWidth() / 2;
                        ListActivity.startListFromLocation(startingLocation, BaseLayoutDrawerActivity.this);
                        overridePendingTransition(0, 0);
                    }
                }, 200);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_camera) {

        } else if (id == R.id.show_photo) {
            startActivity(new Intent(this, PhotoActivity.class));
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.show_card) {
            startActivity(new Intent(this,CardActivity.class));
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
