package com.rockgarden.myapp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
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
    public static final String TAG = BaseLayoutActivity.class.getName();

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    View toolbarTitleView;

    private MenuItem AnimationMenuItem;
    int selectedMenuId = 0; //TODO:可用TAG动态配置menu

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
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_base_options, menu);
        final int menuItemCount = menu.size();
        MenuItem searchItem = menu.findItem(R.id.action_search);
        AnimationMenuItem = menu.findItem(R.id.action_refresh);
        MenuItem shareItem = menu.findItem(R.id.action_share);

        ShareActionProvider myShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        myShareActionProvider.setShareIntent(getDefaultIntent());

        /* collapseActionView展开和合并的时候显示不同的界面
           Bug:用MenuItem.OnActionExpandListener()会引起UnsupportedOperationException*/
        MenuItemCompat.setOnActionExpandListener(searchItem,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem menuItem) {
                        // Return true to allow the action view to expand
                        AnimationMenuItem.setVisible(false);
                        ;
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                        // When the action view is collapsed, reset the query
                        AnimationMenuItem.setVisible(true);
                        // Return true to allow the action view to collapse
                        return true;
                    }
                });

        // TODO:控制需配置SearchView的属性
        /*
        if (searchItem != null) {
            SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
            // use this method for search process
            searchView.setOnSearchClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Search view is expanded
                    AnimationMenuItem.setVisible(false);
                }
            });
            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    //Search View is collapsed
                    AnimationMenuItem.setVisible(true);
                    return false;
                }
            });
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String query) {
                    // use this method when query submitted
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    // use this method for auto complete search process
                    Log.e("SearchValueIs", ":" + newText);
                    return false;
                }
            });
        }
        */

        // FIXME：setActionView自定义ItemView布局异常,点击响应异常
        //AnimationMenuItem.setActionView(R.layout.menu_item_view);

        switch (selectedMenuId) {
            case 0:
                return true;
            case 1:
                // 不显示MenuItem
                for (int i = 0; i < menuItemCount; i++) menu.getItem(i).setVisible(false);
                this.invalidateOptionsMenu();
            case 2:
                AnimationMenuItem.setVisible(false);
                this.invalidateOptionsMenu();
                return true;
            default:
                return true;
        }
    }

    private Intent getDefaultIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        return intent;
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
                //TODO:返回模式待确认PictureDetailActivity没返回MainActivity
                // 规范要求
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else {
                    upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
            case R.id.action_home:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.action_refresh:
                //item.setActionView(getAnimation());
                // FIXME:执行后没有setActionView(null);
                showRefreshAnimation(item);
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * 刷新ITEM动画
     *
     * @param item
     */
    @SuppressLint("NewApi")
    private void showRefreshAnimation(MenuItem item) {
        View view = item.getActionView();
        if (view != null) {
            view.clearAnimation();
            item.setActionView(null);
        } else {
            //用一个ImageView设置成MenuItem的ActionView,再用这个ImageView显示旋转动画;
            ImageView refreshActionView = (ImageView) getLayoutInflater().inflate(R.layout.action_refresh_view, null);
            refreshActionView.setImageResource(R.mipmap.ic_launcher);
            item.setActionView(refreshActionView);
            //显示刷新动画
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.shake_error);
            animation.setRepeatMode(Animation.RESTART);
            animation.setRepeatCount(Animation.RELATIVE_TO_PARENT);
            refreshActionView.startAnimation(animation);
        }
    }

    @SuppressLint("NewApi")
    private void hideRefreshAnimationView(MenuItem item) {
        if (item != null) {
            View view = item.getActionView();
            if (view != null) {
                view.clearAnimation();
                item.setActionView(null);
            }
        }
    }

    //FIXME:用此方法icon消失了
    public View getAnimation() {
        LayoutInflater inflater1 = (LayoutInflater) this.getApplication()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ImageView iv = (ImageView) inflater1.inflate(R.layout.action_refresh_view,
                null);
        Animation rotation = AnimationUtils.loadAnimation(this.getApplicationContext(),
                R.anim.shake_error);
        rotation.setRepeatCount(Animation.INFINITE);
        iv.startAnimation(rotation);
        return iv;
    }

    // 单一的click事件方法
    public void onComposeAction(MenuItem mi) {
        // handle click here
    }


    /*---不调用ButterKnife---*/

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

    public MenuItem getAnimationMenuItem() {
        return AnimationMenuItem;
    }


    /*---不建议使用的方法---*/

    /**
     * 显示每一个Action按钮对应的图标
     * 调用MenuBuilder这个类的setOptionalIconsVisible方法
     * 不建议重写,默认的设置是合理的;
     * @param featureId 108 it is what?
     * @param menu
     * @return
     */
    /*
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == 108 && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    Log.e("TAG", e);
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }
    */
}
