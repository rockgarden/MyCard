package com.rockgarden.myapp.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.rockgarden.myapp.R;
import com.rockgarden.sign.jni.JniSignHolder;

public class JniDemo extends AppCompatActivity {
    JniSignHolder jniSignHolder = new JniSignHolder();
    private static int si;

    private static void callback() {
        si = 123;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jni_demo);
        /* Create a TextView and set its content.
         * the text is retrieved by calling a native
         * function.
         */
        TextView tv = new TextView(this);
        tv.setText( jniSignHolder.stringFromJNI() );
        setContentView(tv);

    }

}
