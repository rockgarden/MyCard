package com.citylinkdata.mycard.activity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Toast;

import com.citylinkdata.mycard.BaseActivity;
import com.citylinkdata.mycard.MyCardApplication;
import com.citylinkdata.mycard.R;
import com.citylinkdata.mycard.fragment.AdvertFragment;
import com.citylinkdata.mycard.fragment.GuideFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN;

/**
 * An full-screen activity
 * Use for loading Advert or Guide.
 */
public class FullscreenActivity extends BaseActivity {
    private static final String TAG = AddFundsToCardActivity.class.getSimpleName();

    private FragmentManager fragmentManager;
    @Bind(R.id.fullscreen_content)
    View mContentView;
    @Bind(R.id.fullscreen_content_controls)
    View mControlsView;

    private static final int UI_ANIMATION_DELAY = 200;

    private final Handler mShowHandler = new Handler();

    private final Runnable mHideRunnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status bar and navigation bar
            // View decorView = getWindow().getDecorView(); //Activity的顶级Layout
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    private final Runnable mShowRunnable = new Runnable() {
        @Override
        public void run() {
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            mControlsView.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();
        hide();
        setupInit();
    }

    @OnClick(R.id.skip_button)
    public void skip() {
        if (MyCardApplication.getInstance().isShowGuide()) {
            showGuide();
        } else {
            jumpToMain();
        }
    }

    private void setupInit() {
        fragmentManager.beginTransaction()
                .setTransition(TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fullscreen_content, new AdvertFragment())
                .commit();
    }

    private void showGuide() {
        fragmentManager.beginTransaction()
                .setTransition(TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .replace(R.id.fullscreen_content, new GuideFragment())
                .commit();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedShow(2000);
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        // Schedule a runnable to remove the status and navigation bar after a delay
        mShowHandler.removeCallbacks(mShowRunnable);
        mShowHandler.postDelayed(mHideRunnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void delayedShow(int delayMillis) {
        // Schedule a runnable to display UI elements after a delay
        mShowHandler.removeCallbacks(mHideRunnable);
        mShowHandler.postDelayed(mShowRunnable, delayMillis);
    }

    public void onFragmentInteraction(Uri uri) {
        Toast.makeText(this, "", Toast.LENGTH_LONG).show();
    }

//    private void selectDrawerItem(MenuItem menuItem) {
//        Fragment fragment = null;
//        Class fragmentClass;
//        Float elevation = getResources().getDimension(R.dimen.elevation_toolbar);
//
//        switch (menuItem.getItemId()) {
//            case R.id.drawer_home:
//                fragmentClass = MainFragment.class;
//                break;
//            case R.id.drawer_favorites:
//                fragmentClass = FundsFragment.class;
//                elevation = 0.0f;
//                break;
//            case R.id.drawer_settings:
//                fragmentClass = SettingsFragment.class;
//                break;
//            case R.id.drawer_cards:
//                fragmentClass = CardsFragment.class;
//                break;
//            default:
//                fragmentClass = MainFragment.class;
//                break;
//        }
//
//        try {
//            fragment = (Fragment) fragmentClass.newInstance();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        fragmentTransaction.replace(R.id.content_frame, fragment);
//        fragmentTransaction.addToBackStack("FRAGMENT");
//        fragmentTransaction.commit();
//
//        menuItem.setChecked(true);
//        setTitle(menuItem.getTitle());
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//            toolbar.setElevation(elevation);
//
//        drawerLayout.closeDrawers();
//    }

}
