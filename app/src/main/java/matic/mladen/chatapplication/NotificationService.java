package matic.mladen.chatapplication;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.io.IOException;

public class NotificationService extends Service {
    private NotificationBinder notificationExample = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        if(notificationExample == null)
            notificationExample = new NotificationBinder();

        return (IBinder) notificationExample;
    }

    public boolean onUnbind(Intent intent)
    {
        notificationExample.stop();
        return super.onUnbind(intent);
    }
    /*

    */

}
