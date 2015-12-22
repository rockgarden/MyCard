package com.rockgarden.myapp.observer;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import com.litesuits.android.Log;
import com.rockgarden.myapp.activity.LoginActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rockgarden on 15/12/2.
 */
public class SmsObserver extends ContentObserver{
    private static final String TAG = SmsObserver.class.getSimpleName();
    private Context mContext;
    private Handler mHandler;

    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public SmsObserver(Context context, Handler handler) {
        super(handler);
        mContext = context;
        mHandler = handler;
    }

    /**
     * This method is called when a content change occurs.
     * Includes the changed content Uri when available.
     * @param selfChange True if this is a self-change notification.
     * @param uri        The Uri of the changed content, or null if unknown.
     */
    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        Log.i(TAG,"SMS has changed");
        Log.i(TAG,uri.toString());
        String code = "";
        if(uri.toString().equals("content://sms/raw")){
            return;
        }
        Uri inboxUri = Uri.parse("content://sms/inbox");
        Cursor c = mContext.getContentResolver().query(inboxUri, null, null, null, "date desc");
        if (c != null) {
            if (c.moveToFirst()) {
                String address = c.getString(c.getColumnIndex("address"));
                String body = c.getString(c.getColumnIndex("body"));
                if (!address.equals("15555215556")) {
                    return;
                }
                Pattern pattern = Pattern.compile("(\\d{6})"); //正则表达式
                Matcher matcher = pattern.matcher(body);
                if (matcher.find()) {
                    code = matcher.group(0);
                    Log.i(TAG, "code is " + code);
                    mHandler.obtainMessage(LoginActivity.MSG_RECEIVED_CODE, code).sendToTarget();
                }
            }
            c.close();
        }
    }
}

