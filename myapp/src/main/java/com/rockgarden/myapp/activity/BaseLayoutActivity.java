package com.rockgarden.myapp.activity;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.rockgarden.myapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 加载通用布局的视图控制基类
 * Created by rockgarden on 15/11/23.
 */
public class BaseLayoutActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private MenuItem settingMenuItem;

    /**
     * 调用ButterKnife注入View
     * @param layoutResID
     */
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        BindViews();
    }

    protected void BindViews() {
        ButterKnife.bind(this);
        setupToolbar();
    }

    protected void setupToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        }
    }

    /**
     * 不调用ButterKnife不注入View
     * @param layoutResId
     */
    public void setContentViewNoBind(int layoutResId) {
        super.setContentView(layoutResId);
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
