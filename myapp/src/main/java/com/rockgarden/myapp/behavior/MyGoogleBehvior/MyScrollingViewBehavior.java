//package com.rockgarden.myapp.behavior.MyGoogleBehvior;
//
///**
// * Created by wangkan on 16/6/8.
// */
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.support.design.widget.AppBarLayout;
//import android.support.design.widget.CoordinatorLayout;
//import android.support.design.widget.CoordinatorLayout.Behavior;
//import android.util.AttributeSet;
//import android.view.View;
//
///**
// * Behavior which should be used by {@link View}s which can scroll vertically and support
// * nested scrolling to automatically scroll any {@link AppBarLayout} siblings.
// */
//public static class MyScrollingViewBehavior extends MyHeaderScrollingViewBehavior {
//
//    public MyScrollingViewBehavior() {}
//
//    public MyScrollingViewBehavior(Context context, AttributeSet attrs) {
//        super(context, attrs);
//
//        final TypedArray a = context.obtainStyledAttributes(attrs,
//                android.support.design.R.styleable.ScrollingViewBehavior_Params);
//        setOverlayTop(a.getDimensionPixelSize(
//                android.support.design.R.styleable.ScrollingViewBehavior_Params_behavior_overlapTop, 0));
//        a.recycle();
//    }
//
//    @Override
//    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
//        // We depend on any AppBarLayouts
//        return dependency instanceof AppBarLayout;
//    }
//
//    @Override
//    public boolean onDependentViewChanged(CoordinatorLayout parent, View child,
//                                          View dependency) {
//        offsetChildAsNeeded(parent, child, dependency);
//        return false;
//    }
//
//    private void offsetChildAsNeeded(CoordinatorLayout parent, View child, View dependency) {
//        final CoordinatorLayout.Behavior behavior =
//                ((CoordinatorLayout.LayoutParams) dependency.getLayoutParams()).getBehavior();
//        if (behavior instanceof Behavior) {
//            // Offset the child, pinning it to the bottom the header-dependency, maintaining
//            // any vertical gap, and overlap
//            final Behavior ablBehavior = (Behavior) behavior;
//            final int offset = ablBehavior.getTopBottomOffsetForScrollingSibling();
//            child.offsetTopAndBottom((dependency.getBottom() - child.getTop())
//                    + ablBehavior.mOffsetDelta
//                    + getVerticalLayoutGap()
//                    - getOverlapPixelsForOffset(dependency));
//        }
//    }
//
//
//    @Override
//    float getOverlapRatioForOffset(final View header) {
//        if (header instanceof AppBarLayout) {
//            final AppBarLayout abl = (AppBarLayout) header;
//            final int totalScrollRange = abl.getTotalScrollRange();
//            final int preScrollDown = abl.getDownNestedPreScrollRange();
//            final int offset = getAppBarLayoutOffset(abl);
//
//            if (preScrollDown != 0 && (totalScrollRange + offset) <= preScrollDown) {
//                // If we're in a pre-scroll down. Don't use the offset at all.
//                return 0;
//            } else {
//                final int availScrollRange = totalScrollRange - preScrollDown;
//                if (availScrollRange != 0) {
//                    // Else we'll use a interpolated ratio of the overlap, depending on offset
//                    return 1f + (offset / (float) availScrollRange);
//                }
//            }
//        }
//        return 0f;
//    }
//
//    private static int getAppBarLayoutOffset(AppBarLayout abl) {
//        final CoordinatorLayout.Behavior behavior =
//                ((CoordinatorLayout.LayoutParams) abl.getLayoutParams()).getBehavior();
//        if (behavior instanceof Behavior) {
//            return ((Behavior) behavior).getTopBottomOffsetForScrollingSibling();
//        }
//        return 0;
//    }
//
//    @Override
//    View findFirstDependency(List<View> views) {
//        for (int i = 0, z = views.size(); i < z; i++) {
//            View view = views.get(i);
//            if (view instanceof AppBarLayout) {
//                return view;
//            }
//        }
//        return null;
//    }
//
//    @Override
//    int getScrollRange(View v) {
//        if (v instanceof AppBarLayout) {
//            return ((AppBarLayout) v).getTotalScrollRange();
//        } else {
//            return super.getScrollRange(v);
//        }
//    }
//}