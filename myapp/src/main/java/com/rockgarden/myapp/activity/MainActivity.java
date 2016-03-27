package com.rockgarden.myapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.litesuits.android.view.GifView;
import com.rockgarden.myapp.R;

import java.io.IOException;
import java.io.InputStream;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseLayoutDrawerActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    @Bind(R.id.test_GifView)
    GifView testGifViw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle();
        try {
            InputStream inputStream = getAssets().open("test.gif");
            testGifViw.setGifStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setTitle() {
        TextView title = new TextView(this);
        title.setText(TAG);
        title.setTextSize(32);
        setToolbarTitleView(title);
    }

    @OnClick(R.id.test_GifView)
    public void onGifClick(View v) {
        GifView gif = (GifView) v;
        gif.setPaused(!gif.isPaused());
    }
}
