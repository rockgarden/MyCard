package com.rockgarden.myapp.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.TextView;

import com.rockgarden.myapp.R;
import com.rockgarden.myapp.fragment.MainFragment;

import static android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN;

public class MainActivity extends BaseLayoutDrawerActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    private FragmentManager fragmentManager;
    private MainFragment mainFragment;
    int selectedMainContentView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle();
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) { //避免发生Activity重创时生成新的fragment
            showMain();
        }
        // 在ActionBar上注入DrawerToggle
        setToggle();
    }

    private void showMain() {
        selectedMainContentView = 4;
        mainFragment = MainFragment.newInstance(selectedMainContentView);
        fragmentManager.beginTransaction()
                .setTransition(TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.main_content, mainFragment)
                .commit();
    }

    /**
     * 自定义Toolbar
     */
    private void initToolbar() {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // 禁用HomeButton
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_launcher);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    // 在titleView中加入textView
    private void setTitle() {
        TextView title = new TextView(this);
        title.setText(TAG);
        title.setTextSize(28);
        title.setTextColor(getResources().getColor(R.color.white));
        setToolbarTitleView(title);
    }


}
