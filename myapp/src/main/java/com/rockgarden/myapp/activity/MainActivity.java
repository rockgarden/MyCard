package com.rockgarden.myapp.activity;

import android.os.Bundle;

import com.litesuits.android.view.GifView;
import com.rockgarden.myapp.R;

import java.io.IOException;
import java.io.InputStream;

import butterknife.Bind;

public class MainActivity extends BaseLayoutDrawerActivity {

    @Bind(R.id.test_GifView)
    GifView testGifViw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            InputStream inputStream = getAssets().open("test.gif");
            testGifViw.setGifStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
