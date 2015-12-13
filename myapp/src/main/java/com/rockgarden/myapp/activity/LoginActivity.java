package com.rockgarden.myapp.activity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rockgarden.myapp.R;
import com.rockgarden.myapp.observer.SmsObserver;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

public class LoginActivity extends BaseActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    @Bind(R.id.editText_Alias)
    EditText inputAlias;
    @Bind(R.id.button_sendAlias)
    Button sendAlias;

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
        ButterKnife.bind(this);
        et_ValidateCode = (EditText) findViewById(R.id.et_validateCode);
        mObserver = new SmsObserver(LoginActivity.this, mHandler);
        Uri uri = Uri.parse("content://sms");
        getContentResolver().registerContentObserver(uri, true, mObserver);
    }

    @OnClick(R.id.button_sendAlias)
    public void sendAlias() {
        try {
            JPushInterface.setAlias(this, inputAlias.getText().toString(), null);
            Toast.makeText(this, "success", Toast.LENGTH_LONG).show();
        }catch (Exception e){
            Toast.makeText(this, "failure", Toast.LENGTH_LONG).show();
        }
    }

}
