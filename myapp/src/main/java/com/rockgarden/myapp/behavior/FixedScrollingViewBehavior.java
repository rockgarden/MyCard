package com.rockgarden.myapp.behavior;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.nostra13.universalimageloader.utils.L;

import java.util.List;

//http://stackoverflow.com/a/30807149/2678584
//
//<android.support.v4.widget.NestedScrollView
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        app:layout_behavior="com.evs.demo.layout.FixedScrollingViewBehavior">
//
//        .....
//</android.support.v4.widget.NestedScrollView>
//
//To watch the result : https://www.youtube.com/watch?v=B0BCZNmFgWg
public class FixedScrollingViewBehavior extends AppBarLayout.ScrollingViewBehavior {
    public FixedScrollingViewBehavior() {
    }

    public FixedScrollingViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean onMeasureChild(CoordinatorLayout parent, View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {

        L.i("onMeasureChild: parentWidthMeasureSpec =" + parentWidthMeasureSpec);
        L.i("onMeasureChild: widthUsed              =" + String.valueOf(widthUsed));
        L.i("onMeasureChild: parentHeightMeasureSpec=" + String.valueOf(parentHeightMeasureSpec));
        L.i("onMeasureChild: heightUsed             =" + String.valueOf(heightUsed));
        L.i("onMeasureChild: child                  =" + String.valueOf(child));
        L.i("onMeasureChild: parent                 =" + String.valueOf(parent));
        L.i("onMeasureChild:---                     =");
        if (child.getLayoutParams().height == -1) {
            List dependencies = parent.getDependencies(child);
            if (dependencies.isEmpty()) {
                return false;
            }

            AppBarLayout appBar = findFirstAppBarLayout(dependencies);
            if (appBar != null && ViewCompat.isLaidOut(appBar)) {
                if (ViewCompat.getFitsSystemWindows(appBar)) {
                    ViewCompat.setFitsSystemWindows(child, true);
                }

                int scrollRange = appBar.getTotalScrollRange();
                int height = parent.getHeight() - appBar.getMeasuredHeight() + Math.min(scrollRange, parent.getHeight() - heightUsed);
                int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
                parent.onMeasureChild(child, parentWidthMeasureSpec, widthUsed, heightMeasureSpec, heightUsed);
                return true;
            }
        }

        return false;
    }


    private static AppBarLayout findFirstAppBarLayout(List<View> views) {
        int i = 0;

        for (int z = views.size(); i < z; ++i) {
            View view = (View) views.get(i);
            if (view instanceof AppBarLayout) {
                return (AppBarLayout) view;
            }
        }

        return null;
    }


    private float xDistance, yDistance, lastX, lastY;

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
