package com.citylinkdata.mycard.activity;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.citylinkdata.mycard.BaseActivity;
import com.citylinkdata.mycard.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rockgarden on 15/11/23.
 */
public class BaseLayoutActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private MenuItem settingMenuItem;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        BindViews();
    }

    protected void BindViews() {
        ButterKnife.bind(this);
        setupToolbar();
    }

    public void setContentViewWithoutInject(int layoutResId) {
        super.setContentView(layoutResId);
    }

    protected void setupToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.base_options_menu, menu);
        settingMenuItem = menu.findItem(R.id.action_settings);
        settingMenuItem.setActionView(R.layout.menu_item_view);
        return true;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public MenuItem getSettingMenuItem() {
        return settingMenuItem;
    }

}
