/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rockgarden.myapp.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.litesuits.common.utils.AndroidUtil;
import com.rockgarden.myapp.R;
import com.rockgarden.myapp.model.Picture;

import butterknife.BindView;

public class PictureDetailActivity extends BaseLayoutActivity {
    public static final String TAG = PictureDetailActivity.class.getName();

    @BindView(R.id.picture_detail_NestedScrollView)
    NestedScrollView picture_detail_NestedScrollView;
    @BindView(R.id.collapse_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbar;

    public static final String EXTRA_NAME = "picture_name";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picturedetail);

        Intent intent = getIntent();
        final String pictureName = intent.getStringExtra(EXTRA_NAME);

        collapsingToolbar.setTitle(pictureName);

        loadBackdrop();
    }

    private void loadBackdrop() {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(Picture.getRandomCheeseDrawable()).centerCrop().into(imageView);
    }

    @Override
    public void onBackPressed() {
        ViewCompat.setElevation(getToolbar(), 0);
        picture_detail_NestedScrollView.animate()
                .translationY(AndroidUtil.getScreenHeight(this))
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        //TODO:怎么后退是最优的super.onBackPressed()or finish
                        PictureDetailActivity.super.onBackPressed();
                        //finish();
                        overridePendingTransition(0, 0);
                    }
                })
                .start();
    }

}
