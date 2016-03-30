package com.rockgarden.myapp.behavior;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by rockgarden on 16/3/29.
 */
public class NoScrollingBehavior extends AppBarLayout.ScrollingViewBehavior {

    public NoScrollingBehavior() {
    }

    public NoScrollingBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    @Override
//    public boolean onInterceptTouchEvent(CoordinatorLayout parent, AppBarLayout child, MotionEvent ev) {
//
//        if (ev.getAction() == MotionEvent.ACTION_UP) {
//            return true;
//        }
//        L.d("onInterceptTouchEvent ev: " + ev);
//        return super.onInterceptTouchEvent(parent, child, ev);
//    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, View child, MotionEvent ev) {
        boolean response = super.onInterceptTouchEvent(parent, child, ev);
        float x = ev.getY();
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mTouchX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                float dX = Math.abs(x - mTouchX);
                if (dX > minSwipeDistance)
                    return true;
                break;
        }
        return response;
    }

    private static final int minSwipeDistance = 300;
    private float mTouchX;
}
