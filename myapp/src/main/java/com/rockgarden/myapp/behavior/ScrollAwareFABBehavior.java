package com.rockgarden.myapp.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.litesuits.android.Log;

import java.util.List;

public class ScrollAwareFABBehavior extends FloatingActionButton.Behavior {
    private static final String TAG = ScrollAwareFABBehavior.class.getSimpleName();

    public ScrollAwareFABBehavior() {
    }

    public ScrollAwareFABBehavior(Context context, AttributeSet attrs) {
        super();
    }

    @Override
    public boolean onStartNestedScroll(final CoordinatorLayout coordinatorLayout, final FloatingActionButton child,
                                       final View directTargetChild, final View target, final int nestedScrollAxes) {
        Log.d("onStartNestedScroll: directTargetChild = " + directTargetChild + " target = " + target.getClass().getSimpleName());
        // Ensure we react to vertical scrolling
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
                ||
                super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

//    @Override
//    public boolean onTouchEvent(CoordinatorLayout parent, FloatingActionButton child, MotionEvent ev) {
//        return super.onTouchEvent(parent, child, ev);
//    }

    @Override
    public void onNestedScroll(final CoordinatorLayout coordinatorLayout, final FloatingActionButton child,
                               final View target, final int dxConsumed, final int dyConsumed,
                               final int dxUnconsumed, final int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        Log.d("onNestedScroll: target = " + target.getClass().getSimpleName() + " dyConsumed = " + dyConsumed + " dyUnconsumed = " + dyUnconsumed + " ");

    }

//    @Override
//    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
//        Log.d("layoutDependsOn: dependency = " + dependency.getClass().getSimpleName());
//        return super.layoutDependsOn(parent, child, dependency);
//    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        Log.d("onDependentViewChanged: dependency = " + dependency.getClass().getSimpleName());
        /* 旋转FAB
        float translationY = getFabTranslationYForSnackbar(parent, child);
        float percentComplete = -translationY / dependency.getHeight();
        child.setRotation(-90 * percentComplete);
        child.setTranslationY(translationY);
        return false;
        */
        return super.onDependentViewChanged(parent, child, dependency);
    }

    private float getFabTranslationYForSnackbar(CoordinatorLayout parent, FloatingActionButton fab) {
        float minOffset = 0;
        final List<View> dependencies = parent.getDependencies(fab);
        for (int i = 0, z = dependencies.size(); i < z; i++) {
            final View view = dependencies.get(i);
            if (view instanceof Snackbar.SnackbarLayout && parent.doViewsOverlap(fab, view)) {
                minOffset = Math.min(minOffset,
                        ViewCompat.getTranslationY(view) - view.getHeight());
            }
        }
        return minOffset;
    }

//    @Override
//    public void onDependentViewRemoved(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
//        Log.d("onDependentViewRemoved: dependency = " + dependency.getClass().getSimpleName());
//        super.onDependentViewRemoved(parent, child, dependency);
//    }
//
//    @Override
//    public boolean onLayoutChild(CoordinatorLayout parent, FloatingActionButton child, int layoutDirection) {
//        Log.d("onLayoutChild: layoutDirection = " + layoutDirection);
//        return super.onLayoutChild(parent, child, layoutDirection);
//    }
//
//    @Override
//    public boolean onInterceptTouchEvent(CoordinatorLayout parent, FloatingActionButton child, MotionEvent ev) {
//        L.d("onInterceptTouchEvent: ev = " + ev);
//        return super.onInterceptTouchEvent(parent, child, ev);
//    }
//
//    @Override
//    public boolean blocksInteractionBelow(CoordinatorLayout parent, FloatingActionButton child) {
//        L.d("blocksInteractionBelow");
//        return super.blocksInteractionBelow(parent, child);
//    }
//
//    @Override
//    public boolean isDirty(CoordinatorLayout parent, FloatingActionButton child) {
//        Log.d("isDirty");
//        return super.isDirty(parent, child);
//    }
//
//    @Override
//    public boolean onMeasureChild(CoordinatorLayout parent, FloatingActionButton child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
//        Log.d("onMeasureChild: widthUsed = " + widthUsed + " heightUsed = " + heightUsed);
//        return super.onMeasureChild(parent, child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
//    }
//
//    @Override
//    public void onNestedScrollAccepted(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
//        Log.d("onNestedScrollAccepted: directTargetChild = " + directTargetChild.getClass().getSimpleName() + " target = " + target.getClass().getSimpleName());
//        super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
//    }
//
//    @Override
//    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target) {
//        Log.d("onStopNestedScroll: target = " + target.getClass().getSimpleName());
//        super.onStopNestedScroll(coordinatorLayout, child, target);
//    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dx, int dy, int[] consumed) {
        Log.d("onNestedPreScroll: target = " + target.getClass().getSimpleName() + " dx = "+dx + " dy = "+ dy + " consumed = "+ (consumed != null && consumed.length > 1 ? consumed[1] : null));
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        consumed[0] = 0;
        consumed[1] = 0;
        if (dy > 0 && child.getVisibility() == View.VISIBLE) {
            // User scrolled down and the FAB is currently visible -> hide the FAB
            child.hide();
        } else if (dy < 0 && child.getVisibility() != View.VISIBLE) {
            // User scrolled up and the FAB is currently not visible -> show the FAB
            child.show();
        }
    }

//    @Override
//    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, float velocityX, float velocityY, boolean consumed) {
//        Log.d("onNestedFling: target = " + target.getClass().getSimpleName() + " velocityX = " + velocityX + " velocityY = " + velocityY);
//        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
//    }
//
//    @Override
//    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, float velocityX, float velocityY) {
//        Log.d("onNestedPreFling: target = " + target.getClass().getSimpleName() + " velocityX = "+velocityX + " velocityY = "+ velocityY);
//        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
//    }

}