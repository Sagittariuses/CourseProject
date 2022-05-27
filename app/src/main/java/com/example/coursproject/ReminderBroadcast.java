package com.example.coursproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

public class ReminderBroadcast extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationUtils _notificationUtils = new NotificationUtils(context);
        NotificationCompat.Builder _builder = _notificationUtils.setNotification(Memory.TitleNotify, Memory.NoteNotify);
        _builder.setVibrate(new long[]{0,100,0, 100,0, 100,0, 100});
        _builder.setSound(alarmSound);
        _notificationUtils.getManager().notify(101, _builder.build());
    }
}