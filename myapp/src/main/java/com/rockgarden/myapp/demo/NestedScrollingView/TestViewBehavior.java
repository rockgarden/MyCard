package com.rockgarden.myapp.demo.NestedScrollingView;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by rockgarden on 16/6/7.
 */
public class TestViewBehavior extends CoordinatorLayout.Behavior<TestView> {

    private float mCustomFinalHeight;

    private float mAvatarMaxSize;
    private float mFinalLeftAvatarPadding;
    private float mStartPosition;
    private int mStartXPosition;
    private float mStartToolbarPosition;
    private int mStartYPosition;
    private int mFinalYPosition;
    private int mStartHeight;
    private int mFinalXPosition;
    private float mChangeBehaviorPoint;

    public TestViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Determine whether the supplied child view has another specific sibling view as a
     * layout dependency.
     *
     * <p>This method will be called at least once in response to a layout request. If it
     * returns true for a given child and dependency view pair, the parent CoordinatorLayout
     * will:</p>
     * <ol>
     *     <li>Always lay out this child after the dependent child is laid out, regardless
     *     of child order.</li>
     *     <li>Call {@link #onDependentViewChanged} when the dependency view's layout or
     *     position changes.</li>
     * </ol>
     *
     * @param parent the parent view of the given child
     * @param child the child view to test
     * @param dependency the proposed dependency of child
     * @return true if child's layout depends on the proposed dependency's layout,
     *         false otherwise
     *
     * @see #onDependentViewChanged(CoordinatorLayout, android.view.View, android.view.View)
     */
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, TestView child, View dependency) {
        return dependency instanceof NestedScrollView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, TestView child, View dependency) {

        final int maxScrollDistance = (int) (mStartToolbarPosition);
        float expandedPercentageFactor = dependency.getY() / maxScrollDistance;

        if (expandedPercentageFactor < mChangeBehaviorPoint) {
            float heightFactor = (mChangeBehaviorPoint - expandedPercentageFactor) / mChangeBehaviorPoint;

            float distanceXToSubtract = ((mStartXPosition - mFinalXPosition)
                    * heightFactor) + (child.getHeight()/2);
            float distanceYToSubtract = ((mStartYPosition - mFinalYPosition)
                    * (1f - expandedPercentageFactor)) + (child.getHeight()/2);

            child.setX(mStartXPosition - distanceXToSubtract);
            child.setY(mStartYPosition - distanceYToSubtract);

            float heightToSubtract = ((mStartHeight - mCustomFinalHeight) * heightFactor);

            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            lp.width = (int) (mStartHeight - heightToSubtract);
            lp.height = (int) (mStartHeight - heightToSubtract);
            child.setLayoutParams(lp);
        } else {
            float distanceYToSubtract = ((mStartYPosition - mFinalYPosition)
                    * (1f - expandedPercentageFactor)) + (mStartHeight/2);

            child.setX(mStartXPosition - child.getWidth()/2);
            child.setY(mStartYPosition - distanceYToSubtract);

            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            lp.width = (int) (mStartHeight);
            lp.height = (int) (mStartHeight);
            child.setLayoutParams(lp);
        }
        return true;
    }

}
