package com.rockgarden.myapp.behavior;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.ActionBar;
import android.util.AttributeSet;
import android.view.animation.Interpolator;

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
public class AnewsAppBarBehavior extends AppBarLayout.Behavior {
    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();
    private Context mContext;
    private int mStatusHeight;
    private ActionBar actionBar;
    private int actionBarColor;

    public AnewsAppBarBehavior() {
    }

    public AnewsAppBarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
//        mStatusHeight = getStatusBarHeight();
    }

//    @Override
//    public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
//        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
//        L.d("onNestedScroll dyConsumed: " + dyConsumed + " , " + dyUnconsumed);
//    }

//    @Override
//    public boolean layoutDependsOn(CoordinatorLayout parent, AppBarLayout child, View dependency) {
//        L.d("layoutDependsOn : "+dependency);
//        return (dependency instanceof ViewPager) || super.layoutDependsOn(parent, child, dependency);
//    }
//
//    @Override
//    public boolean onDependentViewChanged(CoordinatorLayout parent, AppBarLayout child, View dependency) {
//        final float y = child.getY();
//        final int height = child.getHeight();
//        L.d("onDependentViewChanged y: " + y + " , height: " + height);
//
////        final ColorDrawable background = new ColorDrawable(color);
////        child.setBackground(background);
////        final CollapsingToolbarLayout firstToolbar = findFirstToolbar(child);
////        firstToolbar.setContentScrimColor(color);
//
////        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
//
//            if (y < height) {
//                setColorForActionBar((y+mStatusHeight) / height);
//                //       animateAppAndStatusBar(child, (y+mStatusHeight) / height);
//            } else {
//            }
////        }
//        return super.onDependentViewChanged(parent, child, dependency);
//    }

//    @Override
//    public boolean onInterceptTouchEvent(CoordinatorLayout parent, AppBarLayout child, MotionEvent ev) {
//
//
//
////        final ColorDrawable background = new ColorDrawable(color);
////        child.setBackground(background);
////        final CollapsingToolbarLayout firstToolbar = findFirstToolbar(child);
////        firstToolbar.setContentScrimColor(color);
//
//        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
//            final float y = child.getY();
//            final int height = child.getHeight();
//            L.d("onInterceptTouchEvent y: " + y + " , height: " + height);
//            if (y < height) {
//                setColorForActionBar((y+mStatusHeight) / height);
//                //       animateAppAndStatusBar(child, (y+mStatusHeight) / height);
//            } else {
//            }
//        }
//        L.d("onInterceptTouchEvent ev: " + ev);
//        return super.onInterceptTouchEvent(parent, child, ev);
//    }

//    public void setColorForActionBar(float percentage) {
//        android.support.v7.app.ActionBar ab = actionBar;
//        if (ab != null) {
//            final int color = actionBarColor;//Color.parseColor("#ffffff");
//                final int evaluate = (Integer) new ArgbEvaluator().evaluate(percentage, 0, 255);
//                final int argb = Color.argb(evaluate, Color.red(color), Color.green(color), Color.blue(color));
//
//            ab.setBackgroundDrawable(new ColorDrawable(argb));
//
////                collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
////                collapsingToolbar.setContentScrimColor(mutedColor);
//
////              mToolbarView.setBackgroundColor(color);
//
////            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////                Window window = getActivity().getWindow();
////                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
////                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
////                window.setStatusBarColor(headerBgColor);
////            }
////
////            isActionBarColored = true;
//        }
//    }




//    @Override
//    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target) {
//        super.onStopNestedScroll(coordinatorLayout, child, target);
//
//        L.d("onStopNestedScroll child.getY(): " + child.getY());
////
//        if ((child.getY() + mStatusHeight) < child.getHeight() / 2) {
//            super.onNestedFling(coordinatorLayout, child, target, 0, 0, true);
//        } else {
//            super.onNestedFling(coordinatorLayout, child, target, 0, -1, true);
//        }
//    }
//
//
//    // A method to find height of the status bar
//    public int getStatusBarHeight() {
//        int result = 0;
//
//        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
//        if (resourceId > 0) {
//            result = mContext.getResources().getDimensionPixelSize(resourceId);
//        }
//        return result;
//    }
//
//    private void animateIn(LinearLayout appBarLayout, float y) {
//        ViewCompat.animate(appBarLayout).translationY(0).y(y).alpha(1.0F)
//                .setInterpolator(INTERPOLATOR).withLayer().setListener(null)
//                .start();
//    }
//
//    private void animateAppAndStatusBar(final View toolbar, float percentage) {
//        final int color = ((ColorDrawable) toolbar.getBackground()).getColor();
//        final int evaluate = (Integer) new ArgbEvaluator().evaluate(percentage, 0, 255);
//        final int argb = Color.argb(evaluate, Color.red(color), Color.green(color), Color.blue(color));
//        final ColorDrawable background = new ColorDrawable(argb);
//        toolbar.setBackground(background);
//    }
//
//    private static CollapsingToolbarLayout findFirstToolbar(ViewGroup views) {
//        int i = 0;
//
//        for (int z = views.getChildCount(); i < z; ++i) {
//            View view = (View) views.getChildAt(i);
//            if (view instanceof CollapsingToolbarLayout) {
//                return (CollapsingToolbarLayout) view;
//            }
//        }
//
//        return null;
//    }
//
//    public void setActionBar(ActionBar actionBar) {
//        this.actionBar = actionBar;
//    }
//
//    public void setActionBarColor(int actionBarColor) {
//        this.actionBarColor = actionBarColor;
//    }
}
