package com.rockgarden.myapp.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

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

    View toolbarTitleView;

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
        }
        return super.onOptionsItemSelected(item);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public View getToolbarTitleView() {
        return toolbarTitleView;
    }

    public void setToolbarTitleView(View view) {
        RelativeLayout.LayoutParams titleLayoutParams = new RelativeLayout.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        titleLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        titleLayoutParams.setMargins(10, 10, 10, 10); //(int left, int top, int right, int bottom).
        RelativeLayout titleLayout = (RelativeLayout) findViewById(R.id.app_title_Layout);
        titleLayout.addView(view, titleLayoutParams);
        // TODO: 15/12/22 无法去除默认加载的title,所以暂时无法直接用toolbar
        //toolbar.addView(view,titleLayoutParams);
        toolbarTitleView = view;
    }

    public MenuItem getBaseMenuItem() {
        return baseMenuItem;
    }

}
