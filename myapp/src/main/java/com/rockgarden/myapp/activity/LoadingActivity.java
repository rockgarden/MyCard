package com.rockgarden.myapp.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.litesuits.android.view.BreathingViewHelper;
import com.rockgarden.myapp.MyApplication;
import com.rockgarden.myapp.R;
import com.rockgarden.myapp.fragment.AdvertFragment;
import com.rockgarden.myapp.fragment.GuideFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN;

/**
 * An full-screen activity
 * Use for loading Advert or Guide.
 */
public class LoadingActivity extends BaseActivity implements AdvertFragment.OnFragmentInteractionListener {
    private static final String TAG = LoadingActivity.class.getSimpleName();

    private FragmentManager fragmentManager;
    private AdvertFragment advertFragment;
    @BindView(R.id.fullscreen_content)
    View mContentView;
    @BindView(R.id.fullscreen_content_controls)
    View mControlsView;
    @BindView(R.id.skip_button)
    Button mSkipButton;

    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mShowHandler = new Handler();

    /**
     * show fullscreen content view.
     */
    private final Runnable mHideRunnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status bar and navigation bar
            View decorView = getWindow().getDecorView(); //Activity的顶级Layout
            mContentView.setSystemUiVisibility(decorView.SYSTEM_UI_FLAG_LOW_PROFILE
                    | decorView.SYSTEM_UI_FLAG_FULLSCREEN
                    | decorView.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | decorView.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | decorView.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | decorView.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    /**
     * show controls view
     */
    private final Runnable mShowRunnable = new Runnable() {
        @Override
        public void run() {
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            mControlsView.setVisibility(View.VISIBLE);
        }
    };

    /**
     * onCreate
     * don't use requestWindowFeature
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //提高全屏的兼容性
        setContentView(R.layout.activity_fullscreen);
        ButterKnife.bind(this);
        hide();
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) { //避免发生Activity重创时生成新的fragment
            showAdvert();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedShow(2000);
        BreathingViewHelper.setBreathingBackgroundColor(mSkipButton, Color.parseColor("#77f36c60"));
    }

    /**
     * show AdvertFragment
     */
    private void showAdvert() {
        advertFragment = new AdvertFragment();
        fragmentManager.beginTransaction()
                .setTransition(TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fullscreen_content, advertFragment)
                .commit();
    }

    /**
     * skip to next activity
     */
    @OnClick(R.id.skip_button)
    public void skip() {
        BreathingViewHelper.stopBreathingBackgroundColor(mSkipButton);
        if (MyApplication.getInstance().isShowGuide()) {
            showGuide();
        } else {
            bringMainActivityToTop();
        }
    }

    /**
     * show GuideFragment
     */
    private void showGuide() {
        fragmentManager.beginTransaction()
                .setTransition(TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .replace(R.id.fullscreen_content, new GuideFragment())
                .commit();
    }

    /**
     * @param delayMillis
     */
    @SuppressLint("InlinedApi")
    private void delayedShow(int delayMillis) {
        // Schedule a runnable to display UI elements after a delay
        mShowHandler.removeCallbacks(mHideRunnable);
        mShowHandler.postDelayed(mShowRunnable, delayMillis);
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar(); //获取support对应的ActionBar
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        // Schedule a runnable to remove the status and navigation bar after a delay
        mShowHandler.removeCallbacks(mShowRunnable);
        mShowHandler.postDelayed(mHideRunnable, UI_ANIMATION_DELAY);
    }

    /**
     * 实现AdvertFragment类中定义的接口
     *
     * @param uri
     */
    public void onFragmentInteraction(Uri uri) {
        Toast.makeText(this, uri.toString(), Toast.LENGTH_LONG).show();
        GuideFragment guideFragment = (GuideFragment) fragmentManager.findFragmentById(R.id.fragment_guide);
        if (guideFragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fullscreen_content, guideFragment);
            transaction.addToBackStack(null); //add the transaction to the back stack so the user can navigate back
            transaction.commit();
        } else {
            guideFragment = new GuideFragment();
            Bundle args = new Bundle();
            args.putInt(null, 4);
            guideFragment.setArguments(args);
        }
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
