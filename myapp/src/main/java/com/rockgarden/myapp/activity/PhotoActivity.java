package com.rockgarden.myapp.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.litesuits.common.utils.AndroidUtil;
import com.rockgarden.myapp.R;
import com.rockgarden.myapp.adpater.RecyclerViewAdapter_Photo;
import com.rockgarden.myapp.uitl.FeedContextMenu;
import com.rockgarden.myapp.uitl.FeedContextMenuManager;

import butterknife.BindView;
import butterknife.OnClick;

public class PhotoActivity extends BaseLayoutDrawerActivity implements RecyclerViewAdapter_Photo.OnFeedItemClickListener, FeedContextMenu.OnFeedContextMenuItemClickListener {
    public static final String TAG = PhotoActivity.class.getName();
    public static final String ACTION_SHOW_LOADING_ITEM = "action_show_loading_item";

    private static final int ANIM_DURATION_TOOLBAR = 300;
    private static final int ANIM_DURATION_FAB = 400;

    private RecyclerViewAdapter_Photo recyclerViewAdapterPhoto;

    @BindView(R.id.rvFeed)
    RecyclerView rvFeed;
    @BindView(R.id.btnCreate)
    FloatingActionButton fabCreate;
    @BindView(R.id.content)
    CoordinatorLayout clContent;
    private ImageView imageTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        processExtraData();

        imageTitle = new ImageView(this);
        imageTitle.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
        setToolbarTitleView(imageTitle);

        setupPhotos();
        //instance为空才启动切换动画
        if (savedInstanceState == null) {
            pendingIntroAnimation = true;
        } else {
            recyclerViewAdapterPhoto.updateItems(false);
        }
    }


    /**
     * 统一处理intent的方法
     */
    private void processExtraData() {
        Intent intent = getIntent();
        if (ACTION_SHOW_LOADING_ITEM.equals(intent.getAction())) {
            showFeedLoadingItemDelayed();
        }
    }

    private void setupPhotos() {
        toolbar.setTitle(getString(R.string.title_activity_photo));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };
        rvFeed.setLayoutManager(linearLayoutManager);
        recyclerViewAdapterPhoto = new RecyclerViewAdapter_Photo(this);
        recyclerViewAdapterPhoto.setOnFeedItemClickListener(this);
        rvFeed.setAdapter(recyclerViewAdapterPhoto);
        rvFeed.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                FeedContextMenuManager.getInstance().onScrolled(recyclerView, dx, dy);
            }
        });
    }

    /**
     * Activity处于任务栈的顶端,也就是说之前打开过的Activity现在处于onPause、onStop状态的话;
     * 其他应用或Activity再发送Intent的话,执行顺序为:onNewIntent-onRestart-onStart-onResume；
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // Note that getIntent() still returns the original Intent. You can use setIntent(Intent) to update it to this new Intent.
        setIntent(intent); //must store the new intent unless getIntent() will return the old one
//        if (ACTION_SHOW_LOADING_ITEM.equals(intent.getAction())) {
//            showFeedLoadingItemDelayed();
//        }
    }

    private void showFeedLoadingItemDelayed() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rvFeed.smoothScrollToPosition(0);
                recyclerViewAdapterPhoto.showLoadingView();
            }
        }, 500);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (pendingIntroAnimation) {
            pendingIntroAnimation = false;
            startIntroAnimation();
        }
        return true;
    }

    private void startIntroAnimation() {
        fabCreate.setTranslationY(2 * getResources().getDimensionPixelOffset(R.dimen.btn_fab_size));
        int actionbarSize = AndroidUtil.dpToPx(56);
        getToolbar().setTranslationY(-actionbarSize);
        getToolbarTitleView().setTranslationY(-actionbarSize);
        // MenuItem加动画
        //getAnimationMenuItem().getActionView().setTranslationY(-actionbarSize);
        getToolbar().animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(300);
        getToolbarTitleView().animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(400)
        /*getAnimationMenuItem().getActionView().animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(500)*/
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        startContentAnimation();
                    }
                })
                .start();
    }

    private void startContentAnimation() {
        fabCreate.animate()
                .translationY(0)
                .setInterpolator(new OvershootInterpolator(1.f))
                .setStartDelay(300)
                .setDuration(ANIM_DURATION_FAB)
                .start();
        recyclerViewAdapterPhoto.updateItems(true);
    }

    @Override
    public void onCommentsClick(View v, int position) {
        final Intent intent = new Intent(this, CommentsActivity.class);
        int[] startingLocation = new int[2];
        v.getLocationOnScreen(startingLocation);
        intent.putExtra(CommentsActivity.ARG_DRAWING_START_LOCATION, startingLocation[1]);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    public void onMoreClick(View v, int itemPosition) {
        FeedContextMenuManager.getInstance().toggleContextMenuFromView(v, itemPosition, this);
    }

    @Override
    public void onProfileClick(View v) {
//        int[] startingLocation = new int[2];
//        v.getLocationOnScreen(startingLocation);
//        startingLocation[0] += v.getWidth() / 2;
//        UserProfileActivity.startUserProfileFromLocation(startingLocation, this);
//        overridePendingTransition(0, 0);
    }

    @Override
    public void onReportClick(int feedItem) {
        FeedContextMenuManager.getInstance().hideContextMenu();
    }

    @Override
    public void onSharePhotoClick(int feedItem) {
        FeedContextMenuManager.getInstance().hideContextMenu();
    }

    @Override
    public void onCopyShareUrlClick(int feedItem) {
        FeedContextMenuManager.getInstance().hideContextMenu();
    }

    @Override
    public void onCancelClick(int feedItem) {
        FeedContextMenuManager.getInstance().hideContextMenu();
    }

    @OnClick(R.id.btnCreate)
    public void onTakePhotoClick() {
//        int[] startingLocation = new int[2];
//        fabCreate.getLocationOnScreen(startingLocation);
//        startingLocation[0] += fabCreate.getWidth() / 2;
//        TakePhotoActivity.startCameraFromLocation(startingLocation, this);
//        overridePendingTransition(0, 0);
    }

    public void showLikedSnackBar() {
        Snackbar.make(clContent, "Liked!", Snackbar.LENGTH_SHORT).show();
    }
}
