package com.rockgarden.myapp.uitl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.rockgarden.myapp.R;

/**
 * 仅实现重载模式的主题更换
 * Created by rockgarden on 16/5/18.
 * TODO:如何让已经加载的Activity切换Theme?
 */
public class ThemeUtil {
    public final static int THEME_LIGHT = 0;
    public final static int THEME_DAY_NIGHT = 1;
    private static int sTheme;

    /**
     * 重新加载activity以应用新的Theme
     * @param activity
     * @param theme
     */
    public static void changeToTheme(Activity activity, int theme) {
        sTheme = theme;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
        activity.overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
    }

    /**
     * 通过Context传递Application/Activity对象
     * 完成SetTheme
     * @param context
     *
     * -SetTheme必须在setContentView前执行
     */
    public static void onCreateSetTheme(Context context) {
        switch (sTheme) {
            default: context.setTheme(R.style.Theme_Light);
            case THEME_LIGHT:
                context.setTheme(R.style.Theme_Light);
                break;
            case THEME_DAY_NIGHT:
                context.setTheme(R.style.Theme_DayNight);
                break;
        }
    }

}
