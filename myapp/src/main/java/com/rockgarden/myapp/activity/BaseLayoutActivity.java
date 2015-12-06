package com.rockgarden.myapp.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

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

    ImageView toolbarLogo;

    private MenuItem baseMenuItem;

    /**
     * 调用ButterKnife注入View
     *
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
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 可改变toolbar的homeIcon
        }
    }

    /**
     * 不调用ButterKnife不注入View
     *
     * @param layoutResId
     */
    public void setContentViewNoBind(int layoutResId) {
        super.setContentView(layoutResId);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_base_options, menu);
        baseMenuItem = menu.findItem(R.id.action_settings);
        baseMenuItem.setActionView(R.layout.menu_item_view);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, LoginActivity.class));
            ;
        }
        return super.onOptionsItemSelected(item);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public ImageView getToolbarLogo() {
        toolbarLogo = (ImageView) findViewById(R.id.app_logo_ImageView);
        return toolbarLogo;
    }

    public MenuItem getBaseMenuItem() {
        return baseMenuItem;
    }

}
