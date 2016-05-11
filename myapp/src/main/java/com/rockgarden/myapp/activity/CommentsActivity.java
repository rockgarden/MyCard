package com.rockgarden.myapp.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.litesuits.common.utils.AndroidUtil;
import com.rockgarden.myapp.R;
import com.rockgarden.myapp.adpater.RecyclerViewAdapter_Comment;
import com.rockgarden.myapp.widget.SendCommentButton;

import butterknife.BindView;

/**
 * Created by rockgarden on 15/11/19.
 */
public class CommentsActivity extends BaseLayoutActivity implements SendCommentButton.OnSendClickListener {
    public static final String ARG_DRAWING_START_LOCATION = "arg_drawing_start_location";

    @BindView(R.id.comments_content)
    LinearLayout contentComments;
    @BindView(R.id.comment_list)
    RecyclerView commentList;
    @BindView(R.id.add_comment)
    LinearLayout addComment;
    @BindView(R.id.et_comment)
    EditText etComment;
    @BindView(R.id.btn_send_comment)
    SendCommentButton btnSendComment;

    private RecyclerViewAdapter_Comment recyclerViewAdapterComment;
    private int drawingStartLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        setupComments();
        setupSendCommentButton();
        drawingStartLocation = getIntent().getIntExtra(ARG_DRAWING_START_LOCATION, 0);
        if (savedInstanceState == null) {
            contentComments.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    contentComments.getViewTreeObserver().removeOnPreDrawListener(this);
                    startIntroAnimation();
                    return true;
                }
            });
        }
    }

    private void setupComments() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        commentList.setLayoutManager(linearLayoutManager);
        commentList.setHasFixedSize(true);
        recyclerViewAdapterComment = new RecyclerViewAdapter_Comment(this);
        commentList.setAdapter(recyclerViewAdapterComment);
        commentList.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        commentList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    recyclerViewAdapterComment.setAnimationsLocked(true);
                }
            }
        });
    }

    private void setupSendCommentButton() {
        btnSendComment.setOnSendClickListener(this);
    }

    private void startIntroAnimation() {
        ViewCompat.setElevation(getToolbar(), 0);
        contentComments.setScaleY(0.1f);
        contentComments.setPivotY(drawingStartLocation);
        addComment.setTranslationY(200);
        contentComments.animate()
                .scaleY(1)
                .setDuration(200)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ViewCompat.setElevation(getToolbar(), AndroidUtil.dpToPx(8));
                        animateContent();
                    }
                })
                .start();
    }

    private void animateContent() {
        recyclerViewAdapterComment.updateItems();
        addComment.animate().translationY(0)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(200)
                .start();
    }

    @Override
    public void onBackPressed() {
        ViewCompat.setElevation(getToolbar(), 0);
        contentComments.animate()
                .translationY(AndroidUtil.getScreenHeight(this))
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        CommentsActivity.super.onBackPressed();
                        overridePendingTransition(0, 0);
                    }
                })
                .start();
    }

    @Override
    public void onSendClickListener(View v) {
        if (validateComment()) {
            recyclerViewAdapterComment.addItem();
            recyclerViewAdapterComment.setAnimationsLocked(false);
            recyclerViewAdapterComment.setDelayEnterAnimation(false);
            commentList.smoothScrollBy(0, commentList.getChildAt(0).getHeight() * recyclerViewAdapterComment.getItemCount());
            etComment.setText(null);
            btnSendComment.setCurrentState(SendCommentButton.STATE_DONE);
        }
    }

    private boolean validateComment() {
        if (TextUtils.isEmpty(etComment.getText())) {
            btnSendComment.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake_error));
            return false;
        }
        return true;
    }
}
