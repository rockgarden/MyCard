package com.rockgarden.myapp.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
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
 * 加载通用布局的视图控制基类
 * Created by rockgarden on 15/11/24.
 */
public class BaseLayoutDrawerActivity extends BaseLayoutActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;
    @Bind(R.id.nav_view)
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentViewNoBind(R.layout.activity_base_drawer_layout);
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.rootContentViewGroup);
        LayoutInflater.from(this).inflate(layoutResID, viewGroup, true);
        BindViews();
        navigationView.setNavigationItemSelectedListener(this);
        //若启用ActionBar的DrawerToggle则会覆盖ActionBar的HomeButton
        //setToggle();
    }

    /**
     * 实现ActionBar.home按钮与DrawerLayout联动
     * !!建议仅当drawer_layout不遮盖toolbar时使用
     */
    public void setToggle() {
        drawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState(); //NavigationIcon默认图标增加动画效果
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        // 重定义Toolbar
        /*
        if (getToolbar() != null) {
            getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //drawer.openDrawer(Gravity.LEFT); //打开DrawerLayout
                }
            });
        }
        */
    }

    /**
     * 定义navHeader的Click事件
     *
     * @param v bug:NavigationView can't use ButterKnife,this is bug for Design 23
     */
    //@OnClick(R.id.drawerLayoutNavHeader)
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
        Float elevation = getResources().getDimension(R.dimen.elevation_toolbar);
        Intent intent = null;
        int id = item.getItemId();
        if (id == R.id.show_photo) {
            intent = new Intent(this, PhotoActivity.class);
        } else if (id == R.id.nav_slideshow) {
            intent = new Intent(this, ViewPagerActivity.class);
        } else if (id == R.id.show_card) {
            intent = new Intent(this, CardActivity.class);
        } else if (id == R.id.nav_camera) {
            intent = new Intent(this, SpinnerActivity.class);
        } else if (id == R.id.nav_set) {
            intent = new Intent(this, SettingActivity.class);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            toolbar.setElevation(elevation);
        drawer.closeDrawer(GravityCompat.START);
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            this.startActivity(intent);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        ViewCompat.setElevation(getToolbar(), 0);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
