package com.rockgarden.myapp.uitl;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.webkit.JavascriptInterface;

import com.rockgarden.myapp.R;
import com.rockgarden.myapp.demo.JSWebViewDemo;
import com.rockgarden.myapp.fragment.WebViewFragment;

public class NotificationBindObject {

    private Context mContext;
    private String mTitle;

    public NotificationBindObject(Context context, String title) {
        mContext = context;
        mTitle = title;
    }

    /**
     * The '@JavascriptInterface is required to make the method accessible from the Javascript
     * layer
     * <p/>
     * The code in this method is based on the documentation here:
     * <p/>
     * http://developer.android.com/training/notify-user/build-notification.html
     *
     * @param message The message displayed in the notification
     */
    @JavascriptInterface
    public void showNotification(String message) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle(mTitle)
                        .setContentText(message)
                        .setAutoCancel(true);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(mContext, JSWebViewDemo.class);
        resultIntent.putExtra(WebViewFragment.EXTRA_FROM_NOTIFICATION, true);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(JSWebViewDemo.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(-1, mBuilder.build());
    }

}
