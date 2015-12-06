package com.rockgarden.myapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by rockgarden on 2015/12/6.
 */
public class JPushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(JPushInterface.ACTION_MESSAGE_RECEIVED)) {
            Bundle bundle = intent.getExtras();
            String title = bundle.getString(JPushInterface.EXTRA_TITLE);
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            Toast.makeText(context, "Message title:" + title + "  content:" + message, Toast.LENGTH_LONG).show();
        }
    }
}
