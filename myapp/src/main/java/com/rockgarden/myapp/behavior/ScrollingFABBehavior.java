package com.rockgarden.myapp.behavior;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.rockgarden.myapp.R;
import com.litesuits.android.Log;


public class ScrollingFABBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {

    private int toolbarHeight;

    public ScrollingFABBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.toolbarHeight = getToolbarHeight(context);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton fab, View dependency) {

        Log.i("layoutDependsOn: dependency=" + String.valueOf(dependency));
        Log.i("layoutDependsOn: dependency instanceof RelativeLogayout ?" + String.valueOf(dependency instanceof RelativeLayout));
        Log.i("layoutDependsOn: dependency instanceof LinearLayout ?" + String.valueOf(dependency instanceof LinearLayout));
        Log.i("layoutDependsOn: dependency instanceof NestedScrollView ?" + String.valueOf(dependency instanceof NestedScrollView));
        Log.i("layoutDependsOn: dependency instanceof ListView ?" + String.valueOf(dependency instanceof ListView));
        Log.i("layoutDependsOn: dependency instanceof AppBarLayout ?" + String.valueOf(dependency instanceof AppBarLayout));
        return dependency instanceof AppBarLayout;
//        return true;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton fab, View dependency) {
        Log.i("FAB: toolbarHeight   =" + toolbarHeight);
        Log.i("FAB: fab.getHeight() =" + fab.getHeight());
        Log.i("FAB: dependency.getY =" + dependency.getY());
        Log.i("FAB: ---             =");

        // 另一种方式check the behavior triggered,来确认dependency是AppBarLayout
        android.support.design.widget.CoordinatorLayout.Behavior behavior = ((android.support.design.widget.CoordinatorLayout.LayoutParams)dependency.getLayoutParams()).getBehavior();
        if(behavior instanceof AppBarLayout.Behavior) {

        }

        if (dependency instanceof AppBarLayout) {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
            int fabBottomMargin = lp.bottomMargin;
            int distanceToScroll = fab.getHeight() + fabBottomMargin;
            float ratio = (float) dependency.getY() / (float) toolbarHeight;
            Log.i("FAB: toolbarHeight   =" + toolbarHeight);
            Log.i("FAB: fab.getHeight() =" + fab.getHeight());
            Log.i("FAB: fabBottomMargin =" + fabBottomMargin);
            Log.i("FAB: distanceToScroll=" + distanceToScroll);
            Log.i("FAB: dependency.getY =" + dependency.getY());
            Log.i("FAB: ratio           =" + String.valueOf(ratio));
            Log.i("FAB: result          =" + String.valueOf(-distanceToScroll * ratio));
            Log.i("FAB: up?             =" + String.valueOf((-distanceToScroll * ratio) > 0));
            Log.i("FAB: ---     " );


            fab.setTranslationY(-distanceToScroll * ratio);
        }
        return true;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.i("onNestedScroll: dyConsumed         =" + dyConsumed);
        Log.i("onNestedScroll: coordinatorLayout  =" + String.valueOf(coordinatorLayout));
        Log.i("onNestedScroll: child              =" + String.valueOf(child));
        Log.i("onNestedScroll: target             =" + String.valueOf(target));
        Log.i("onNestedScroll: dxConsumed         =" + String.valueOf(dxConsumed));
        Log.i("onNestedScroll: dyConsumed         =" + String.valueOf(dyConsumed));
        Log.i("onNestedScroll: dxUnconsumed       =" + String.valueOf(dxUnconsumed));
        Log.i("onNestedScroll: dyUnconsumed       =" + String.valueOf(dyUnconsumed));
        Log.i("onNestedScroll:---  =" );
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, FloatingActionButton child, MotionEvent ev) {
        Log.i("FAB: onInterceptTouchEvent   =" + String.valueOf(parent));
        return super.onInterceptTouchEvent(parent, child, ev);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, FloatingActionButton child, int layoutDirection) {
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
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        Log.i("onStartNestedScroll: coordinatorLayout  =" + String.valueOf(coordinatorLayout));
        Log.i("onStartNestedScroll: child              =" + String.valueOf(child));
        Log.i("onStartNestedScroll: target             =" + String.valueOf(target));
        Log.i("onStartNestedScroll: nestedScrollAxes   =" + String.valueOf(nestedScrollAxes));
        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }
}