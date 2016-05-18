package com.rockgarden.myapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.rockgarden.StatusBarUtil;
import com.rockgarden.myapp.MyApplication;
import com.rockgarden.myapp.R;
import com.rockgarden.myapp.uitl.Utils;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设置DEMO
 */
public class SettingActivity extends BaseLayoutDrawerActivity {
    public static final String TAG = SettingActivity.class.getName();

    public static final String EXTRA_IS_TRANSPARENT = "is_transparent";

    @BindView(R.id.root_layout)
    RelativeLayout mRootLayout;
    @BindView(R.id.tv_status_alpha)
    TextView mTvStatusAlpha;
    @BindView(R.id.btn_change_color)
    Button mBtnChangeColor;
    @BindView(R.id.btn_change_background)
    Button mBtnChangeBackground;
    @BindView(R.id.sb_change_alpha)
    SeekBar mSbChangeAlpha;
    @BindView(R.id.chb_translucent)
    CheckBox mChbTranslucent;
    @BindView(R.id.spThemes)
    Spinner spThemes;

    private boolean isBgChanged;
    private boolean isTransparent;
    private int mAlpha;
    private int mColor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        isTransparent = getIntent().getBooleanExtra(EXTRA_IS_TRANSPARENT, false);
        super.onCreate(savedInstanceState);
        // MUST BE SET BEFORE setContentView
        Utils.onActivityCreateSetTheme(this);
        // AFTER SETTING THEME
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        setupSpinnerItemSelection();

        if (!isTransparent) {
            mSbChangeAlpha.setVisibility(View.VISIBLE);
            setSeekBar();
        } else {
            mSbChangeAlpha.setVisibility(View.GONE);
        }
    }

    private void setupSpinnerItemSelection() {
        spThemes.setSelection(MyApplication.currentPosition);
        MyApplication.currentPosition = spThemes.getSelectedItemPosition();
        spThemes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (MyApplication.currentPosition != position) {
                    Utils.changeToTheme(SettingActivity.this, position);
                }
                MyApplication.currentPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @OnClick(R.id.btn_change_background)
    public void changeBackground() {
        isBgChanged = !isBgChanged;
        if (isBgChanged) {
            mRootLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.guide1));
        } else {
            mRootLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.guide2));
        }
    }

    @OnClick(R.id.btn_change_color)
    public void changeColor() {
        Random random = new Random();
        mColor = 0xff000000 | random.nextInt(0xffffff);
        toolbar.setBackgroundColor(mColor);
        StatusBarUtil.setColor(SettingActivity.this, mColor, mAlpha);
    }

    @OnClick(R.id.chb_translucent)
    public void setTranslucent() {
        if (mChbTranslucent.isChecked()) {
            mRootLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.guide3));
            StatusBarUtil.setTranslucentForDrawerLayout(SettingActivity.this, drawer, mAlpha);
            toolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        } else {
            mRootLayout.setBackgroundDrawable(null);
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            StatusBarUtil.setColorForDrawerLayout(SettingActivity.this, drawer, getResources().getColor(R
                    .color.colorPrimary), mAlpha);
        }
    }

    protected void setStatusBar() {
        if (isTransparent) {
            StatusBarUtil.setTransparent(this);
        } else {
            StatusBarUtil.setTranslucent(this, StatusBarUtil.DEFAULT_STATUS_BAR_ALPHA);
        }
    }

    private void setSeekBar() {
        mSbChangeAlpha.setMax(255);
        mSbChangeAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mAlpha = progress;
                StatusBarUtil.setTranslucent(SettingActivity.this, mAlpha);
                mTvStatusAlpha.setText(String.valueOf(mAlpha));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mSbChangeAlpha.setProgress(StatusBarUtil.DEFAULT_STATUS_BAR_ALPHA);
    }
}
