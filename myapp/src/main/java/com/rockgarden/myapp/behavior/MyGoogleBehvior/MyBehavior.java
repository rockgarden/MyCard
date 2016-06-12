//package com.rockgarden.myapp.behavior.MyGoogleBehvior;
//
///**
// * Created by wangkan on 16/6/8.
// */
//
//import android.content.Context;
//import android.os.Parcel;
//import android.os.Parcelable;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.design.widget.AnimationUtils;
//import android.support.design.widget.AppBarLayout;
//import android.support.design.widget.CoordinatorLayout;
//import android.support.design.widget.HeaderBehavior;
//import android.support.design.widget.MathUtils;
//import android.support.design.widget.ValueAnimatorCompat;
//import android.support.design.widget.ViewUtils;
//import android.support.v4.os.ParcelableCompat;
//import android.support.v4.os.ParcelableCompatCreatorCallbacks;
//import android.support.v4.view.ViewCompat;
//import android.util.AttributeSet;
//import android.view.View;
//import android.view.animation.Interpolator;
//
//import java.lang.ref.WeakReference;
//import java.util.List;
//
///**
// * The default {MyBehavior} for {@link AppBarLayout}. Implements the necessary nested
// * scroll handling with offsetting.
// */
//public class MyBehavior extends MyHeaderBehavior<AppBarLayout> {
//    private static final int ANIMATE_OFFSET_DIPS_PER_SECOND = 300;
//    private static final int INVALID_POSITION = -1;
//
//    /**
//     * Callback to allow control over any {@link AppBarLayout} dragging.
//     */
//    public static abstract class DragCallback {
//        /**
//         * Allows control over whether the given {@link AppBarLayout} can be dragged or not.
//         *
//         * <p>Dragging is defined as a direct touch on the AppBarLayout with movement. This
//         * call does not affect any nested scrolling.</p>
//         *
//         * @return true if we are in a position to scroll the AppBarLayout via a drag, false
//         *         if not.
//         */
//        public abstract boolean canDrag(@NonNull AppBarLayout appBarLayout);
//    }
//
//    private int mOffsetDelta;
//
//    private boolean mSkipNestedPreScroll;
//    private boolean mWasNestedFlung;
//
//    private ValueAnimatorCompat mAnimator;
//
//    private int mOffsetToChildIndexOnLayout = INVALID_POSITION;
//    private boolean mOffsetToChildIndexOnLayoutIsMinHeight;
//    private float mOffsetToChildIndexOnLayoutPerc;
//
//    private WeakReference<View> mLastNestedScrollingChildRef;
//    private DragCallback mOnDragCallback;
//
//    public MyBehavior() {}
//
//    public MyBehavior(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    @Override
//    public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child,
//                                       View directTargetChild, View target, int nestedScrollAxes) {
//        // Return true if we're nested scrolling vertically, and we have scrollable children
//        // and the scrolling view is big enough to scroll
//        final boolean started = (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0
//                && child.hasScrollableChildren()
//                && parent.getHeight() - directTargetChild.getHeight() <= child.getHeight();
//
//        if (started && mAnimator != null) {
//            // Cancel any offset animation
//            mAnimator.cancel();
//        }
//
//        // A new nested scroll has started so clear out the previous ref
//        mLastNestedScrollingChildRef = null;
//
//        return started;
//    }
//
//    @Override
//    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child,
//                                  View target, int dx, int dy, int[] consumed) {
//        if (dy != 0 && !mSkipNestedPreScroll) {
//            int min, max;
//            if (dy < 0) {
//                // We're scrolling down
//                min = -child.getTotalScrollRange();
//                max = min + child.getDownNestedPreScrollRange();
//            } else {
//                // We're scrolling up
//                min = -child.getUpNestedPreScrollRange();
//                max = 0;
//            }
//            consumed[1] = scroll(coordinatorLayout, child, dy, min, max);
//        }
//    }
//
//    @Override
//    public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child,
//                               View target, int dxConsumed, int dyConsumed,
//                               int dxUnconsumed, int dyUnconsumed) {
//        if (dyUnconsumed < 0) {
//            // If the scrolling view is scrolling down but not consuming, it's probably be at
//            // the top of it's content
//            scroll(coordinatorLayout, child, dyUnconsumed,
//                    -child.getDownNestedScrollRange(), 0);
//            // Set the expanding flag so that onNestedPreScroll doesn't handle any events
//            mSkipNestedPreScroll = true;
//        } else {
//            // As we're no longer handling nested scrolls, reset the skip flag
//            mSkipNestedPreScroll = false;
//        }
//    }
//
//    @Override
//    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout abl,
//                                   View target) {
//        if (!mWasNestedFlung) {
//            // If we haven't been flung then let's see if the current view has been set to snap
//            snapToChildIfNeeded(coordinatorLayout, abl);
//        }
//
//        // Reset the flags
//        mSkipNestedPreScroll = false;
//        mWasNestedFlung = false;
//        // Keep a reference to the previous nested scrolling child
//        mLastNestedScrollingChildRef = new WeakReference<>(target);
//    }
//
//    @Override
//    public boolean onNestedFling(final CoordinatorLayout coordinatorLayout,
//                                 final AppBarLayout child, View target, float velocityX, float velocityY,
//                                 boolean consumed) {
//        boolean flung = false;
//
//        if (!consumed) {
//            // It has been consumed so let's fling ourselves
//            flung = fling(coordinatorLayout, child, -child.getTotalScrollRange(),
//                    0, -velocityY);
//        } else {
//            // If we're scrolling up and the child also consumed the fling. We'll fake scroll
//            // upto our 'collapsed' offset
//            if (velocityY < 0) {
//                // We're scrolling down
//                final int targetScroll = -child.getTotalScrollRange()
//                        + child.getDownNestedPreScrollRange();
//                if (getTopBottomOffsetForScrollingSibling() < targetScroll) {
//                    // If we're currently not expanded more than the target scroll, we'll
//                    // animate a fling
//                    animateOffsetTo(coordinatorLayout, child, targetScroll);
//                    flung = true;
//                }
//            } else {
//                // We're scrolling up
//                final int targetScroll = -child.getUpNestedPreScrollRange();
//                if (getTopBottomOffsetForScrollingSibling() > targetScroll) {
//                    // If we're currently not expanded less than the target scroll, we'll
//                    // animate a fling
//                    animateOffsetTo(coordinatorLayout, child, targetScroll);
//                    flung = true;
//                }
//            }
//        }
//
//        mWasNestedFlung = flung;
//        return flung;
//    }
//
//    /**
//     * Set a callback to control any {@link AppBarLayout} dragging.
//     *
//     * @param callback the callback to use, or {@code null} to use the default behavior.
//     */
//    public void setDragCallback(@Nullable DragCallback callback) {
//        mOnDragCallback = callback;
//    }
//
//    private void animateOffsetTo(final CoordinatorLayout coordinatorLayout,
//                                 final AppBarLayout child, final int offset) {
//        final int currentOffset = getTopBottomOffsetForScrollingSibling();
//        if (currentOffset == offset) {
//            if (mAnimator != null && mAnimator.isRunning()) {
//                mAnimator.cancel();
//            }
//            return;
//        }
//
//        if (mAnimator == null) {
//            mAnimator = ViewUtils.createAnimator();
//            mAnimator.setInterpolator(AnimationUtils.DECELERATE_INTERPOLATOR);
//            mAnimator.setUpdateListener(new ValueAnimatorCompat.AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimatorCompat animator) {
//                    setHeaderTopBottomOffset(coordinatorLayout, child,
//                            animator.getAnimatedIntValue());
//                }
//            });
//        } else {
//            mAnimator.cancel();
//        }
//
//        // Set the duration based on the amount of dips we're travelling in
//        final float distanceDp = Math.abs(currentOffset - offset) /
//                coordinatorLayout.getResources().getDisplayMetrics().density;
//        mAnimator.setDuration(Math.round(distanceDp * 1000 / ANIMATE_OFFSET_DIPS_PER_SECOND));
//
//        mAnimator.setIntValues(currentOffset, offset);
//        mAnimator.start();
//    }
//
//    private View getChildOnOffset(AppBarLayout abl, final int offset) {
//        for (int i = 0, count = abl.getChildCount(); i < count; i++) {
//            View child = abl.getChildAt(i);
//            if (child.getTop() <= -offset && child.getBottom() >= -offset) {
//                return child;
//            }
//        }
//        return null;
//    }
//
//    private void snapToChildIfNeeded(CoordinatorLayout coordinatorLayout, AppBarLayout abl) {
//        final int offset = getTopBottomOffsetForScrollingSibling();
//        final View offsetChild = getChildOnOffset(abl, offset);
//        if (offsetChild != null) {
//            final LayoutParams lp = (LayoutParams) offsetChild.getLayoutParams();
//            if ((lp.getScrollFlags() & LayoutParams.FLAG_SNAP) == LayoutParams.FLAG_SNAP) {
//                // We're set the snap, so animate the offset to the nearest edge
//                int childTop = -offsetChild.getTop();
//                int childBottom = -offsetChild.getBottom();
//
//                // If the view is set only exit until it is collapsed, we'll abide by that
//                if ((lp.getScrollFlags() & LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED)
//                        == LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED) {
//                    childBottom += ViewCompat.getMinimumHeight(offsetChild);
//                }
//
//                final int newOffset = offset < (childBottom + childTop) / 2
//                        ? childBottom : childTop;
//                animateOffsetTo(coordinatorLayout, abl,
//                        MathUtils.constrain(newOffset, -abl.getTotalScrollRange(), 0));
//            }
//        }
//    }
//
//    @Override
//    public boolean onLayoutChild(CoordinatorLayout parent, AppBarLayout abl,
//                                 int layoutDirection) {
//        boolean handled = super.onLayoutChild(parent, abl, layoutDirection);
//
//        final int pendingAction = abl.getPendingAction();
//        if (pendingAction != PENDING_ACTION_NONE) {
//            final boolean animate = (pendingAction & PENDING_ACTION_ANIMATE_ENABLED) != 0;
//            if ((pendingAction & PENDING_ACTION_COLLAPSED) != 0) {
//                final int offset = -abl.getUpNestedPreScrollRange();
//                if (animate) {
//                    animateOffsetTo(parent, abl, offset);
//                } else {
//                    setHeaderTopBottomOffset(parent, abl, offset);
//                }
//            } else if ((pendingAction & PENDING_ACTION_EXPANDED) != 0) {
//                if (animate) {
//                    animateOffsetTo(parent, abl, 0);
//                } else {
//                    setHeaderTopBottomOffset(parent, abl, 0);
//                }
//            }
//        } else if (mOffsetToChildIndexOnLayout >= 0) {
//            View child = abl.getChildAt(mOffsetToChildIndexOnLayout);
//            int offset = -child.getBottom();
//            if (mOffsetToChildIndexOnLayoutIsMinHeight) {
//                offset += ViewCompat.getMinimumHeight(child);
//            } else {
//                offset += Math.round(child.getHeight() * mOffsetToChildIndexOnLayoutPerc);
//            }
//            setTopAndBottomOffset(offset);
//        }
//
//        // Finally reset any pending states
//        abl.resetPendingAction();
//        mOffsetToChildIndexOnLayout = INVALID_POSITION;
//
//        // We may have changed size, so let's constrain the top and bottom offset correctly,
//        // just in case we're out of the bounds
//        setTopAndBottomOffset(
//                MathUtils.constrain(getTopAndBottomOffset(), -abl.getTotalScrollRange(), 0));
//
//        // Make sure we update the elevation
//        dispatchOffsetUpdates(abl);
//
//        return handled;
//    }
//
//    @Override
//    boolean canDragView(AppBarLayout view) {
//        if (mOnDragCallback != null) {
//            // If there is a drag callback set, it's in control
//            return mOnDragCallback.canDrag(view);
//        }
//
//        // Else we'll use the default behaviour of seeing if it can scroll down
//        if (mLastNestedScrollingChildRef != null) {
//            // If we have a reference to a scrolling view, check it
//            final View scrollingView = mLastNestedScrollingChildRef.get();
//            return scrollingView != null && scrollingView.isShown()
//                    && !ViewCompat.canScrollVertically(scrollingView, -1);
//        } else {
//            // Otherwise we assume that the scrolling view hasn't been scrolled and can drag.
//            return true;
//        }
//    }
//
//    @Override
//    void onFlingFinished(CoordinatorLayout parent, AppBarLayout layout) {
//        // At the end of a manual fling, check to see if we need to snap to the edge-child
//        snapToChildIfNeeded(parent, layout);
//    }
//
//    @Override
//    int getMaxDragOffset(AppBarLayout view) {
//        return -view.getDownNestedScrollRange();
//    }
//
//    @Override
//    int getScrollRangeForDragFling(AppBarLayout view) {
//        return view.getTotalScrollRange();
//    }
//
//    @Override
//    int setHeaderTopBottomOffset(CoordinatorLayout coordinatorLayout,
//                                 AppBarLayout header, int newOffset, int minOffset, int maxOffset) {
//        final int curOffset = getTopBottomOffsetForScrollingSibling();
//        int consumed = 0;
//
//        if (minOffset != 0 && curOffset >= minOffset && curOffset <= maxOffset) {
//            // If we have some scrolling range, and we're currently within the min and max
//            // offsets, calculate a new offset
//            newOffset = MathUtils.constrain(newOffset, minOffset, maxOffset);
//            AppBarLayout appBarLayout = (AppBarLayout) header;
//            if (curOffset != newOffset) {
//                final int interpolatedOffset = appBarLayout.hasChildWithInterpolator()
//                        ? interpolateOffset(appBarLayout, newOffset)
//                        : newOffset;
//
//                boolean offsetChanged = setTopAndBottomOffset(interpolatedOffset);
//
//                // Update how much dy we have consumed
//                consumed = curOffset - newOffset;
//                // Update the stored sibling offset
//                mOffsetDelta = newOffset - interpolatedOffset;
//
//                if (!offsetChanged && appBarLayout.hasChildWithInterpolator()) {
//                    // If the offset hasn't changed and we're using an interpolated scroll
//                    // then we need to keep any dependent views updated. CoL will do this for
//                    // us when we move, but we need to do it manually when we don't (as an
//                    // interpolated scroll may finish early).
//                    coordinatorLayout.dispatchDependentViewsChanged(appBarLayout);
//                }
//
//                // Dispatch the updates to any listeners
//                dispatchOffsetUpdates(appBarLayout);
//            }
//        } else {
//            // Reset the offset delta
//            mOffsetDelta = 0;
//        }
//
//        return consumed;
//    }
//
//    private void dispatchOffsetUpdates(AppBarLayout layout) {
//        final List<OnOffsetChangedListener> listeners = layout.mListeners;
//
//        // Iterate backwards through the list so that most recently added listeners
//        // get the first chance to decide
//        for (int i = 0, z = listeners.size(); i < z; i++) {
//            final OnOffsetChangedListener listener = listeners.get(i);
//            if (listener != null) {
//                listener.onOffsetChanged(layout, getTopAndBottomOffset());
//            }
//        }
//    }
//
//    private int interpolateOffset(AppBarLayout layout, final int offset) {
//        final int absOffset = Math.abs(offset);
//
//        for (int i = 0, z = layout.getChildCount(); i < z; i++) {
//            final View child = layout.getChildAt(i);
//            final AppBarLayout.LayoutParams childLp = (LayoutParams) child.getLayoutParams();
//            final Interpolator interpolator = childLp.getScrollInterpolator();
//
//            if (absOffset >= child.getTop() && absOffset <= child.getBottom()) {
//                if (interpolator != null) {
//                    int childScrollableHeight = 0;
//                    final int flags = childLp.getScrollFlags();
//                    if ((flags & LayoutParams.SCROLL_FLAG_SCROLL) != 0) {
//                        // We're set to scroll so add the child's height plus margin
//                        childScrollableHeight += child.getHeight() + childLp.topMargin
//                                + childLp.bottomMargin;
//
//                        if ((flags & LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED) != 0) {
//                            // For a collapsing scroll, we to take the collapsed height
//                            // into account.
//                            childScrollableHeight -= ViewCompat.getMinimumHeight(child);
//                        }
//                    }
//
//                    if (ViewCompat.getFitsSystemWindows(child)) {
//                        childScrollableHeight -= layout.getTopInset();
//                    }
//
//                    if (childScrollableHeight > 0) {
//                        final int offsetForView = absOffset - child.getTop();
//                        final int interpolatedDiff = Math.round(childScrollableHeight *
//                                interpolator.getInterpolation(
//                                        offsetForView / (float) childScrollableHeight));
//
//                        return Integer.signum(offset) * (child.getTop() + interpolatedDiff);
//                    }
//                }
//
//                // If we get to here then the view on the offset isn't suitable for interpolated
//                // scrolling. So break out of the loop
//                break;
//            }
//        }
//
//        return offset;
//    }
//
//    @Override
//    int getTopBottomOffsetForScrollingSibling() {
//        return getTopAndBottomOffset() + mOffsetDelta;
//    }
//
//    @Override
//    public Parcelable onSaveInstanceState(CoordinatorLayout parent, AppBarLayout appBarLayout) {
//        final Parcelable superState = super.onSaveInstanceState(parent, appBarLayout);
//        final int offset = getTopAndBottomOffset();
//
//        // Try and find the first visible child...
//        for (int i = 0, count = appBarLayout.getChildCount(); i < count; i++) {
//            View child = appBarLayout.getChildAt(i);
//            final int visBottom = child.getBottom() + offset;
//
//            if (child.getTop() + offset <= 0 && visBottom >= 0) {
//                final SavedState ss = new SavedState(superState);
//                ss.firstVisibleChildIndex = i;
//                ss.firstVisibileChildAtMinimumHeight =
//                        visBottom == ViewCompat.getMinimumHeight(child);
//                ss.firstVisibileChildPercentageShown = visBottom / (float) child.getHeight();
//                return ss;
//            }
//        }
//
//        // Else we'll just return the super state
//        return superState;
//    }
//
//    @Override
//    public void onRestoreInstanceState(CoordinatorLayout parent, AppBarLayout appBarLayout,
//                                       Parcelable state) {
//        if (state instanceof SavedState) {
//            final SavedState ss = (SavedState) state;
//            super.onRestoreInstanceState(parent, appBarLayout, ss.getSuperState());
//            mOffsetToChildIndexOnLayout = ss.firstVisibleChildIndex;
//            mOffsetToChildIndexOnLayoutPerc = ss.firstVisibileChildPercentageShown;
//            mOffsetToChildIndexOnLayoutIsMinHeight = ss.firstVisibileChildAtMinimumHeight;
//        } else {
//            super.onRestoreInstanceState(parent, appBarLayout, state);
//            mOffsetToChildIndexOnLayout = INVALID_POSITION;
//        }
//    }
//
//    protected static class SavedState extends View.BaseSavedState {
//        int firstVisibleChildIndex;
//        float firstVisibileChildPercentageShown;
//        boolean firstVisibileChildAtMinimumHeight;
//
//        public SavedState(Parcel source, ClassLoader loader) {
//            super(source);
//            firstVisibleChildIndex = source.readInt();
//            firstVisibileChildPercentageShown = source.readFloat();
//            firstVisibileChildAtMinimumHeight = source.readByte() != 0;
//        }
//
//        public SavedState(Parcelable superState) {
//            super(superState);
//        }
//
//        @Override
//        public void writeToParcel(Parcel dest, int flags) {
//            super.writeToParcel(dest, flags);
//            dest.writeInt(firstVisibleChildIndex);
//            dest.writeFloat(firstVisibileChildPercentageShown);
//            dest.writeByte((byte) (firstVisibileChildAtMinimumHeight ? 1 : 0));
//        }
//
//        public static final Parcelable.Creator<SavedState> CREATOR =
//                ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks<SavedState>() {
//                    @Override
//                    public SavedState createFromParcel(Parcel source, ClassLoader loader) {
//                        return new SavedState(source, loader);
//                    }
//
//                    @Override
//                    public SavedState[] newArray(int size) {
//                        return new SavedState[size];
//                    }
//                });
//    }
//}
//
