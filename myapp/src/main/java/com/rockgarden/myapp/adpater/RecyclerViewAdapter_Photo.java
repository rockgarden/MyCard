package com.rockgarden.myapp.adpater;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.litesuits.common.utils.AndroidUtil;
import com.rockgarden.myapp.R;
import com.rockgarden.myapp.activity.PhotoActivity;
import com.rockgarden.myapp.widget.SendingProgressView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 图片列表的示例
 * Created by wk on 05.11.16.
 */
public class RecyclerViewAdapter_Photo extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private static final int VIEW_TYPE_DEFAULT = 1;
    private static final int VIEW_TYPE_LOADER = 2;

    private static final DecelerateInterpolator DECCELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);

    private static final int ANIMATED_ITEMS_COUNT = 2;

    private Context context;
    private int lastAnimatedPosition = -1;
    private int itemsCount = 0;
    private boolean animateItems = false;

    private final Map<Integer, Integer> likesCount = new HashMap<>();
    private final Map<RecyclerView.ViewHolder, AnimatorSet> likeAnimations = new HashMap<>();
    private final ArrayList<Integer> likedPositions = new ArrayList<>();

    private OnFeedItemClickListener onFeedItemClickListener;

    private boolean showLoadingView = false;
    private int loadingViewSize = AndroidUtil.dpToPx(200);

    public RecyclerViewAdapter_Photo(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false);
        final PhotoItemViewHolder photoItemViewHolder = new PhotoItemViewHolder(view);
        if (viewType == VIEW_TYPE_DEFAULT) {
            photoItemViewHolder.btnComments.setOnClickListener(this);
            photoItemViewHolder.btnMore.setOnClickListener(this);
            photoItemViewHolder.ivFeedCenter.setOnClickListener(this);
            photoItemViewHolder.btnLike.setOnClickListener(this);
            photoItemViewHolder.ivUserProfile.setOnClickListener(this);
        } else if (viewType == VIEW_TYPE_LOADER) {
            View bgView = new View(context);
            bgView.setLayoutParams(new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            ));
            bgView.setBackgroundColor(0x77ffffff);
            photoItemViewHolder.vImageRoot.addView(bgView);
            photoItemViewHolder.vProgressBg = bgView;

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(loadingViewSize, loadingViewSize);
            params.gravity = Gravity.CENTER;
            SendingProgressView sendingProgressView = new SendingProgressView(context);
            sendingProgressView.setLayoutParams(params);
            photoItemViewHolder.vImageRoot.addView(sendingProgressView);
            photoItemViewHolder.vSendingProgress = sendingProgressView;
        }

        return photoItemViewHolder;
    }

    private void runEnterAnimation(View view, int position) {
        if (!animateItems || position >= ANIMATED_ITEMS_COUNT - 1) {
            return;
        }

        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(AndroidUtil.getScreenHeight(context));
            view.animate()
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator(3.f))
                    .setDuration(700)
                    .start();
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        runEnterAnimation(viewHolder.itemView, position);
        final PhotoItemViewHolder holder = (PhotoItemViewHolder) viewHolder;
        if (getItemViewType(position) == VIEW_TYPE_DEFAULT) {
            bindDefaultFeedItem(position, holder);
        } else if (getItemViewType(position) == VIEW_TYPE_LOADER) {
            bindLoadingFeedItem(holder);
        }
    }

    private void bindDefaultFeedItem(int position, PhotoItemViewHolder holder) {
        if (position % 2 == 0) {
            holder.ivFeedCenter.setImageResource(R.mipmap.ic_launcher);
            holder.ivFeedBottom.setText(R.string.good_photo);
        } else {
            holder.ivFeedCenter.setImageResource(R.mipmap.ic_launcher);
            holder.ivFeedBottom.setText(R.string.bad_photo);
        }
        updateLikesCounter(holder, false);
        updateHeartButton(holder, false);

        holder.btnComments.setTag(position);
        holder.btnMore.setTag(position);
        holder.ivFeedCenter.setTag(holder);
        holder.btnLike.setTag(holder);

        if (likeAnimations.containsKey(holder)) {
            likeAnimations.get(holder).cancel();
        }
        resetLikeAnimationState(holder);
    }

    private void bindLoadingFeedItem(final PhotoItemViewHolder holder) {
        holder.ivFeedCenter.setImageResource(R.mipmap.ic_launcher);
        holder.ivFeedBottom.setText(R.string.good_photo);
        holder.vSendingProgress.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                holder.vSendingProgress.getViewTreeObserver().removeOnPreDrawListener(this);
                holder.vSendingProgress.simulateProgress();
                return true;
            }
        });
        holder.vSendingProgress.setOnLoadingFinishedListener(new SendingProgressView.OnLoadingFinishedListener() {
            @Override
            public void onLoadingFinished() {
                holder.vSendingProgress.animate().scaleY(0).scaleX(0).setDuration(200).setStartDelay(100);
                holder.vProgressBg.animate().alpha(0.f).setDuration(200).setStartDelay(100)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                holder.vSendingProgress.setScaleX(1);
                                holder.vSendingProgress.setScaleY(1);
                                holder.vProgressBg.setAlpha(1);
                                showLoadingView = false;
                                notifyItemChanged(0);
                            }
                        })
                        .start();
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if (showLoadingView && position == 0) {
            return VIEW_TYPE_LOADER;
        } else {
            return VIEW_TYPE_DEFAULT;
        }
    }

    @Override
    public int getItemCount() {
        return itemsCount;
    }

    private void updateLikesCounter(PhotoItemViewHolder holder, boolean animated) {
        int currentLikesCount = likesCount.get(holder.getPosition()) + 1;
        String likesCountText = context.getResources().getQuantityString(
                R.plurals.likes_count, currentLikesCount, currentLikesCount
        );
        if (animated) {
            holder.tsLikesCounter.setText(likesCountText);
        } else {
            holder.tsLikesCounter.setCurrentText(likesCountText);
        }
        likesCount.put(holder.getPosition(), currentLikesCount);
    }

    private void updateHeartButton(final PhotoItemViewHolder holder, boolean animated) {
        if (animated) {
            if (!likeAnimations.containsKey(holder)) {
                AnimatorSet animatorSet = new AnimatorSet();
                likeAnimations.put(holder, animatorSet);

                ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(holder.btnLike, "rotation", 0f, 360f);
                rotationAnim.setDuration(300);
                rotationAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

                ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(holder.btnLike, "scaleX", 0.2f, 1f);
                bounceAnimX.setDuration(300);
                bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

                ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(holder.btnLike, "scaleY", 0.2f, 1f);
                bounceAnimY.setDuration(300);
                bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);
                bounceAnimY.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        holder.btnLike.setImageResource(R.drawable.ic_heart_red);
                    }
                });

                animatorSet.play(rotationAnim);
                animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);

                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        resetLikeAnimationState(holder);
                    }
                });

                animatorSet.start();
            }
        } else {
            if (likedPositions.contains(holder.getPosition())) {
                holder.btnLike.setImageResource(R.drawable.ic_heart_red);
            } else {
                holder.btnLike.setImageResource(R.drawable.ic_heart_outline_grey);
            }
        }
    }

    @Override
    public void onClick(View view) {
        final int viewId = view.getId();
        if (viewId == R.id.btnComments) {
            if (onFeedItemClickListener != null) {
                onFeedItemClickListener.onCommentsClick(view, (Integer) view.getTag());
            }
        } else if (viewId == R.id.btnMore) {
            if (onFeedItemClickListener != null) {
                onFeedItemClickListener.onMoreClick(view, (Integer) view.getTag());
            }
        } else if (viewId == R.id.btnLike) {
            PhotoItemViewHolder holder = (PhotoItemViewHolder) view.getTag();
            if (!likedPositions.contains(holder.getPosition())) {
                likedPositions.add(holder.getPosition());
                updateLikesCounter(holder, true);
                updateHeartButton(holder, true);
                if (context instanceof PhotoActivity) {
                    ((PhotoActivity) context).showLikedSnackBar();
                }
            }
        } else if (viewId == R.id.ivFeedCenter) {
            PhotoItemViewHolder holder = (PhotoItemViewHolder) view.getTag();
            if (!likedPositions.contains(holder.getPosition())) {
                likedPositions.add(holder.getPosition());
                updateLikesCounter(holder, true);
                animatePhotoLike(holder);
                updateHeartButton(holder, false);
                if (context instanceof PhotoActivity) {
                    ((PhotoActivity) context).showLikedSnackBar();
                }
            }
        } else if (viewId == R.id.ivUserProfile) {
            if (onFeedItemClickListener != null) {
                onFeedItemClickListener.onProfileClick(view);
            }
        }
    }

    private void animatePhotoLike(final PhotoItemViewHolder holder) {
        if (!likeAnimations.containsKey(holder)) {
            holder.vBgLike.setVisibility(View.VISIBLE);
            holder.ivLike.setVisibility(View.VISIBLE);
            holder.vBgLike.setScaleY(0.1f);
            holder.vBgLike.setScaleX(0.1f);
            holder.vBgLike.setAlpha(1f);
            holder.ivLike.setScaleY(0.1f);
            holder.ivLike.setScaleX(0.1f);
            AnimatorSet animatorSet = new AnimatorSet();
            likeAnimations.put(holder, animatorSet);
            ObjectAnimator bgScaleYAnim = ObjectAnimator.ofFloat(holder.vBgLike, "scaleY", 0.1f, 1f);
            bgScaleYAnim.setDuration(200);
            bgScaleYAnim.setInterpolator(DECCELERATE_INTERPOLATOR);
            ObjectAnimator bgScaleXAnim = ObjectAnimator.ofFloat(holder.vBgLike, "scaleX", 0.1f, 1f);
            bgScaleXAnim.setDuration(200);
            bgScaleXAnim.setInterpolator(DECCELERATE_INTERPOLATOR);
            ObjectAnimator bgAlphaAnim = ObjectAnimator.ofFloat(holder.vBgLike, "alpha", 1f, 0f);
            bgAlphaAnim.setDuration(200);
            bgAlphaAnim.setStartDelay(150);
            bgAlphaAnim.setInterpolator(DECCELERATE_INTERPOLATOR);
            ObjectAnimator imgScaleUpYAnim = ObjectAnimator.ofFloat(holder.ivLike, "scaleY", 0.1f, 1f);
            imgScaleUpYAnim.setDuration(300);
            imgScaleUpYAnim.setInterpolator(DECCELERATE_INTERPOLATOR);
            ObjectAnimator imgScaleUpXAnim = ObjectAnimator.ofFloat(holder.ivLike, "scaleX", 0.1f, 1f);
            imgScaleUpXAnim.setDuration(300);
            imgScaleUpXAnim.setInterpolator(DECCELERATE_INTERPOLATOR);
            ObjectAnimator imgScaleDownYAnim = ObjectAnimator.ofFloat(holder.ivLike, "scaleY", 1f, 0f);
            imgScaleDownYAnim.setDuration(300);
            imgScaleDownYAnim.setInterpolator(ACCELERATE_INTERPOLATOR);
            ObjectAnimator imgScaleDownXAnim = ObjectAnimator.ofFloat(holder.ivLike, "scaleX", 1f, 0f);
            imgScaleDownXAnim.setDuration(300);
            imgScaleDownXAnim.setInterpolator(ACCELERATE_INTERPOLATOR);
            animatorSet.playTogether(bgScaleYAnim, bgScaleXAnim, bgAlphaAnim, imgScaleUpYAnim, imgScaleUpXAnim);
            animatorSet.play(imgScaleDownYAnim).with(imgScaleDownXAnim).after(imgScaleUpYAnim);
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    resetLikeAnimationState(holder);
                }
            });
            animatorSet.start();
        }
    }

    private void resetLikeAnimationState(PhotoItemViewHolder holder) {
        likeAnimations.remove(holder);
        holder.vBgLike.setVisibility(View.GONE);
        holder.ivLike.setVisibility(View.GONE);
    }

    public void updateItems(boolean animated) {
        itemsCount = 1000;
        animateItems = animated;
        fillLikesWithRandomValues();
        notifyDataSetChanged();
    }

    private void fillLikesWithRandomValues() {
        for (int i = 0; i < getItemCount(); i++) {
            likesCount.put(i, new Random().nextInt(100));
        }
    }

    public void setOnFeedItemClickListener(OnFeedItemClickListener onFeedItemClickListener) {
        this.onFeedItemClickListener = onFeedItemClickListener;
    }

    public void showLoadingView() {
        showLoadingView = true;
        notifyItemChanged(0);
    }

    public static class PhotoItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ivFeedCenter)
        ImageView ivFeedCenter;
        @Bind(R.id.ivFeedBottom)
        TextView ivFeedBottom;
        @Bind(R.id.btnComments)
        ImageButton btnComments;
        @Bind(R.id.btnLike)
        ImageButton btnLike;
        @Bind(R.id.btnMore)
        ImageButton btnMore;
        @Bind(R.id.vBgLike)
        View vBgLike;
        @Bind(R.id.ivLike)
        ImageView ivLike;
        @Bind(R.id.tsLikesCounter)
        TextSwitcher tsLikesCounter;
        @Bind(R.id.ivUserProfile)
        ImageView ivUserProfile;
        @Bind(R.id.vImageRoot)
        FrameLayout vImageRoot;

        SendingProgressView vSendingProgress;
        View vProgressBg;

        public PhotoItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

    public interface OnFeedItemClickListener {
        public void onCommentsClick(View v, int position);

        public void onMoreClick(View v, int position);

        public void onProfileClick(View v);
    }
}
