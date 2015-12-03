package com.citylinkdata.mycard.behavior;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.rockgarden.myapp.R;
import com.litesuits.android.log.Log;


public class ScrollingViewPagerBehavior extends CoordinatorLayout.Behavior<ViewPager> {

    private int toolbarHeight;

    public ScrollingViewPagerBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.toolbarHeight = getToolbarHeight(context);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, ViewPager fab, View dependency) {

        Log.i("layoutDependsOn: dependency=" + String.valueOf(dependency));
//        return dependency instanceof AppBarLayout;
        return true;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ViewPager fab, View dependency) {
        Log.i("FAB: toolbarHeight   =" + toolbarHeight);
        Log.i("FAB: fab.getHeight() =" + fab.getHeight());
        Log.i("FAB: dependency.getY =" + dependency.getY());
        Log.i("FAB: ---             =");
        if (dependency instanceof AppBarLayout) {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
            int fabBottomMargin = lp.bottomMargin;
            int distanceToScroll = fab.getHeight() + fabBottomMargin;
            float ratio = (float) dependency.getY() / (float) toolbarHeight;
            fab.setTranslationY(-distanceToScroll * ratio);
        }
        return true;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, ViewPager child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, ViewPager child, MotionEvent ev) {
        return super.onInterceptTouchEvent(parent, child, ev);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, ViewPager child, int layoutDirection) {
        Log.i("FAB: layoutDirection   =" + layoutDirection);
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context
                .getTheme().obtainStyledAttributes(new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, ViewPager child, View directTargetChild, View target, int nestedScrollAxes) {
        Log.i("onStartNestedScroll: coordinatorLayout  =" + String.valueOf(coordinatorLayout));
        Log.i("onStartNestedScroll: child              =" + String.valueOf(child));
        Log.i("onStartNestedScroll: target             =" + String.valueOf(target));
        Log.i("onStartNestedScroll: nestedScrollAxes   =" + String.valueOf(nestedScrollAxes));
        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, ViewPager child, View target, float velocityX, float velocityY, boolean consumed) {
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }
}