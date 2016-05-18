package com.rockgarden.myapp.uitl;

import android.app.Activity;
import android.content.Intent;

import com.rockgarden.myapp.R;

/**
 * 仅实现重载模式的更换主题
 * Created by rockgarden on 16/5/18.
 */
public class Utils {
    private static int sTheme;

    public final static int THEME_LIGHT = 0;
    public final static int THEME_DAY_NIGHT = 1;

    public static void changeToTheme(Activity activity, int theme) {
        sTheme = theme;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
        activity.overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
    }

    public static void onActivityCreateSetTheme(Activity activity) {
        switch (sTheme) {
            default:
            case THEME_LIGHT:
                activity.setTheme(R.style.Theme_Light);
                break;
            case THEME_DAY_NIGHT:
                activity.setTheme(R.style.Theme_DayNight);
                break;
        }
    }

}
