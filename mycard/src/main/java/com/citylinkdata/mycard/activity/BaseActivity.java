package com.citylinkdata.mycard.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.litesuits.android.log.Log;

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

    public void FullScreen_SDK16() {
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    public void jumpToMain() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        intent.setClass(this, FundsToCardActivityLayout.class);
        startActivity(intent);
        Log.i(TAG, "jump");
        finish();
    }

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
