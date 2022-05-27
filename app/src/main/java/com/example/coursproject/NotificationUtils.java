package com.example.coursproject;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;

public class NotificationUtils extends ContextWrapper
{
    private NotificationManager notificationManager;
    String CHANNEL_ID = "notification channel";
    String TIMELINE_CHANNEL_NAME = "Timeline notification";
    private NotificationManager _notificationManager;
    private final Context _context;

    public NotificationUtils(Context base)
    {
        super(base);
        _context = base;
        createChannel();
    }

    @SuppressLint("ResourceAsColor")
    public NotificationCompat.Builder setNotification(String title, String content)
    {
        Intent notificationIntent = new Intent(this, NoteActivity.class);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setAutoCancel(false)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setContentTitle("Напоминание: " + title)
                .setContentText("Записка: " + content)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setColor(R.color.mycolor)
                .setAutoCancel(true)
                .setColor(16088680)
                .addAction(R.drawable.ic_add_btn, "Открыть", pendingIntent);

    }

    @SuppressLint("ObsoleteSdkInt")
    private void createChannel()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, TIMELINE_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            getManager().createNotificationChannel(channel);
        }
    }

    public NotificationManager getManager()
    {
        if(_notificationManager == null)
        {
            _notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return _notificationManager;
    }

    public void setReminder(Calendar calendar)
    {
        Intent _intent = new Intent(_context, ReminderBroadcast.class);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent _pendingIntent = PendingIntent.getBroadcast(_context, 0, _intent, 0);

        AlarmManager _alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        _alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), _pendingIntent);
    }

}