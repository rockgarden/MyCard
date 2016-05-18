package com.rockgarden.myapp.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;

import com.litesuits.android.Log;
import com.rockgarden.myapp.R;

import java.lang.reflect.Field;

import butterknife.ButterKnife;

/**
 * 加载全局通用方法的基类
 * Created by rockgarden on 15/11/20.
 */
public class BaseActivity extends AppCompatActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();

    public boolean isJumpToGuide = false;
    public LoadingListener listener = null;
    private Context currentContext = null;
    public boolean NeedsLoop = true;
    public static boolean isLogin = false;

    public boolean pendingIntroAnimation;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setOverflowShowingAlways();
        setTranslucentStatusBar(this.getWindow());
        ButterKnife.setDebug(true);
    }

    public void FullScreen_SDK16() {
        if (Build.VERSION.SDK_INT < 16) {
            requestWindowFeature(Window.FEATURE_NO_TITLE); //与ButterKnife对冲突
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    public void jumpToMain() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        Log.d(TAG, "jump to main");
        finish();
    }

    public void bringMainActivityToTop() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    public void bringPhotoActivityToTop() {
        Intent intent = new Intent(this, PhotoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setAction(PhotoActivity.ACTION_SHOW_LOADING_ITEM);
        startActivity(intent);
    }

    // 解决物理Menu键和overflow按钮互斥
    private void setOverflowShowingAlways() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置透明效果的StatusBar
     * @param window
     */
    public static void setTranslucentStatusBar(Window window) {
        if (window == null) return;
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= Build.VERSION_CODES.LOLLIPOP) {
            setTranslucentStatusBarLollipop(window);
        } else if (sdkInt >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatusBarKiKat(window);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void setTranslucentStatusBarLollipop(Window window) {
        window.setStatusBarColor(
                window.getContext()
                        .getResources()
                        .getColor(R.color.transparent));
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void setTranslucentStatusBarKiKat(Window window) {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

//    /**
//     * 显示每一个Action按钮对应的图标
//     * 调用MenuBuilder这个类的setOptionalIconsVisible方法
//     * @param featureId
//     * @param menu
//     * @return
//     */
//    @Override
//    public boolean onMenuOpened(int featureId, Menu menu) {
//        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
//            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
//                try {
//                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
//                    m.setAccessible(true);
//                    m.invoke(menu, true);
//                } catch (Exception e) {
//                }
//            }
//        }
//        return super.onMenuOpened(featureId, menu);
//    }

//    private void AlertNotAvailable() {
//        if (NeedsLoop)
//            Looper.prepare();
//        new AlertDialog.Builder(currentContext).setIcon(android.R.drawable.ic_dialog_alert).setTitle("请确认").setCancelable(false).setMessage("程序将退出,请检查外部或内部存储器是否可用,并确保至少有" + baselibConst.GetFreeSize() + "M可用空间.").setPositiveButton("确认", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                if (kListener != null)
//                    kListener.killMyProgress();
//            }
//        }).show();
//        if (NeedsLoop)
//            Looper.loop();
//    }
//
//    private void AlertRetry(String message) {
//        if (NeedsLoop)
//            Looper.prepare();
//        new AlertDialog.Builder(currentContext).setIcon(android.R.drawable.ic_dialog_alert).setTitle("请确认").setCancelable(false).setMessage(message).setPositiveButton("重试", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                CheckUpdate();
//            }
//        }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                baselibConst.killMyProgress();
//            }
//        }).show();
//        if (NeedsLoop)
//            Looper.loop();
//    }

//    public void ReadPrefence() {
//        try {
//            String app_ver = currentContext.getPackageManager().getPackageInfo(currentContext.getPackageName(), 0).versionName;
//            SharedPreferences settings = currentContext.getSharedPreferences(baselibConst.GetPrefsName(), 0);
//            guideVersion = settings.getString("vGuideVersion", "");// 点击引导页进入时的版本
//            isJumpToGuide = !app_ver.equalsIgnoreCase(guideVersion);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void WritePrefence() {
//        try {
//            String app_ver = currentContext.getPackageManager().getPackageInfo(currentContext.getPackageName(), 0).versionName;
//            SharedPreferences settings = currentContext.getSharedPreferences(baselibConst.GetPrefsName(), 0);
//            SharedPreferences.Editor editor = settings.edit();
//            editor.putString("vGuideVersion", app_ver);
//            editor.commit();
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

    // private void JumpToLogin() {
    // Intent intent = new Intent();
    // Bundle bundle = new Bundle();
    // bundle.putBoolean("autoLogin", true);
    // intent.putExtras(bundle);
    // intent.putExtra("guideVersion", guideVersion);
    // intent.setClass(getApplicationContext(), LoginActivity.class);
    // startActivity(intent);
    // finish();
    // }

    // private void JumpToGuide() {
    // Intent intent = new Intent();
    // Bundle bundle = new Bundle();
    // intent.putExtras(bundle);
    // intent.setClass(getApplicationContext(), GuideActivity.class);
    // startActivity(intent);
    // finish();
    // }

    public interface LoadingListener {
        void JumpToMain();
        void JumpToGuide();
    }

}
