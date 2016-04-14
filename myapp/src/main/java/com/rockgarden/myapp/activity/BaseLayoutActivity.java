package com.rockgarden.myapp.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
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
 * <p>引入ButterKnife并注入全局的Toolbar</p>
 * Created by rockgarden on 15/11/23.
 */
public class BaseLayoutActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    View toolbarTitleView;

    private MenuItem baseMenuItem;

    /**
     * 调用ButterKnife注入View
     * 加载全局的toolbar
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

    /**
     * AppToolBar主要功能
     * 应用程序图标
     * 逻辑父的“向上”导航
     * 特定的应用程序或活动的名称
     * 活动的基本动作图标
     * 一致导航（包括导航抽屉）
     */
    protected void setupToolbar() {
        // 注入toolbar才可有效执行setupToolbar
        if (toolbar != null) {
            setSupportActionBar(toolbar); //toolbar->ActionBar by getDelegate()
            ActionBar actionBar = getSupportActionBar();
            getSupportActionBar().setTitle(R.string.app_name); //设置title
            //String title = actionBar.getTitle().toString();
            //getSupportActionBar().setLogo(R.mipmap.ic_launcher); //设置logo
            //actionBar.show(); //.hide= hide the actionbar
            // 默认启用ActionBar的HomeButton
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); //可改变toolbar的homeIcon
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    /**
     * 定义ToolBar的OptionsMenu
     * Adding Action Items
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_base_options, menu);
        // FIXME：自定义ItemView布局异常
        //baseMenuItem = menu.findItem(R.id.action_settings);
        //baseMenuItem.setActionView(R.layout.menu_item_view);
        return true;
    }

    /**
     * 定义OptionsMenuItem的Selected事件
     * <p>Activity's options menu and the navigation button will be wired through the standard
     * {@link android.R.id#home home} menu select action.</p>
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // ActionBar原生的home
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_home:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // 单一的click事件方法
    public void onComposeAction(MenuItem mi) {
        // handle click here
    }

    /**
     * 不调用ButterKnife不注入View
     * 加载全局的toolbar
     *
     * @param layoutResId
     */
    public void setContentViewNoBind(int layoutResId) {
        super.setContentView(layoutResId);
        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        setupToolbar();
    }


    /*---其它的get或set方法---*/

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
