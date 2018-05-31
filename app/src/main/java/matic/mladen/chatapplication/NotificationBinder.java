package matic.mladen.chatapplication;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.support.v4.app.INotificationSideChannel;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Mladen on 5/31/2018.
 */

public class NotificationBinder extends INotificationExample.Stub {
    private ICallbackExample mICallbackExample;
    private Notification mNotification;

    @Override
    public void setCallback(ICallbackExample callback) {
        mICallbackExample = callback;
        mNotification = new Notification();
        mNotification.start();
    }

    public void stop() {
        mNotification.stop();
    }

    private class Notification implements Runnable {
        private Handler mHandler = null;
        private boolean mRun = true;

        public void start() {
            mHandler = new Handler(Looper.getMainLooper());
            mHandler.postDelayed(this, 5000L);
        }

        public void stop() {
            mRun = false;
            mHandler.removeCallbacks(this);
        }

        @Override
        public void run() {
            if(!mRun) {
                return;
            }

            try {
                mICallbackExample.onCallbackCall();
            } catch (NullPointerException e) {

            } catch (RemoteException e) {
                Log.i("MSG", "onCallbackCall()");
            }
            mHandler.postDelayed(this, 5000L);
        }
    }
}
/*
NotificationCompat.Builder mNotification;
    private HttpHelper mHttpHelper;
    private Context mContext;

    private Notification mNotificationNotBuilder;
 */

/*
SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
            String sessionId = sharedPreferences.getString("sessionId", "ERROR!");

            boolean response = false;

            try {
                response = mHttpHelper.getServiceFromURL("http://18.205.194.168:80/getfromservice", sessionId);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (!response) {
                Log.d("Service", "Error!");
            }

            Intent intent = new Intent(getApplicationContext(), ContactsActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

            mNotification = new NotificationCompat.Builder(getApplicationContext());
            mNotification.setSmallIcon(R.drawable.pepe);
            mNotification.setContentTitle("Alert: ");
            mNotification.setContentText("New message!");
            mNotification.setContentIntent(pendingIntent);
            mNotification.setPriority(NotificationCompat.PRIORITY_DEFAULT);
            mNotification.setAutoCancel(true);

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
            notificationManagerCompat.notify(777, mNotification.build());

            mHandler.postDelayed(this, 5000L);
 */