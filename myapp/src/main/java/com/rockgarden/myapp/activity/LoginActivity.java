package com.rockgarden.myapp.activity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.EditText;

import com.rockgarden.myapp.R;
import com.rockgarden.myapp.observer.SmsObserver;

public class LoginActivity extends BaseLayoutActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    public static final int MSG_RECEIVED_CODE = 1; //message的标签
    private EditText et_ValidateCode = null;
    private SmsObserver mObserver;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_RECEIVED_CODE) {
                String code = (String) msg.obj;
                //update the UI
                et_ValidateCode.setText(code);
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        getContentResolver().unregisterContentObserver(mObserver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_ValidateCode = (EditText) findViewById(R.id.et_validateCode);
        mObserver = new SmsObserver(LoginActivity.this, mHandler);
        Uri uri = Uri.parse("content://sms");
        getContentResolver().registerContentObserver(uri, true, mObserver);
    }

}
