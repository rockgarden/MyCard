package com.citylinkdata.mycard.activity;

import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.citylinkdata.mycard.R;

import butterknife.Bind;

/**
 * Created by rockgarden on 15/11/24.
 */
public class BaseDrawerActivity extends BaseLayoutActivity {

    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentViewWithoutInject(R.layout.activity_base_drawer_layout);
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.rootContentViewGroup);
        LayoutInflater.from(this).inflate(layoutResID, viewGroup, true);
        BindViews();
    }

    public void setToggle() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        if (getToolbar() != null) {
            getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawer.openDrawer(Gravity.LEFT);
                }
            });
        }
    }

    //@OnClick(R.id.drawerLayoutNavHeader) //NavigationView can't use butterknife,this is bug for Design 23
    public void navHeaderClick(final View v) {
        ViewGroup navHeader =(ViewGroup) findViewById(R.id.drawerLayoutNavHeader);
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
                        ListActivity.startListFromLocation(startingLocation, BaseDrawerActivity.this);
                        overridePendingTransition(0, 0);
                    }
                }, 200);
            }
        });
    }

}
