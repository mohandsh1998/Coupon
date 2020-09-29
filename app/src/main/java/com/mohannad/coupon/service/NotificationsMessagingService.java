package com.mohannad.coupon.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mohannad.coupon.R;
import com.mohannad.coupon.data.local.StorageSharedPreferences;
import com.mohannad.coupon.utils.LocaleHelper;

public class NotificationsMessagingService extends FirebaseMessagingService {
    private static final String CHANNEL_ID = "com.mohannad.coupon.notification";
    private static final String CHANNEL_NAME = "Notification app";
    private static final int NOTIFICATION_ID = 183;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null) {
            createNotificationChannel();
            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String description = "This is notify from admin channel!";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            channel.setDescription(description);
            // Register the channel with the system;
            // You can't change the importance or other notification behaviors later!
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showNotification(String title, String content) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSmallIcon(R.drawable.img_splash);
        mBuilder.setVibrate(new long[]{0, 500, 700, 900});
        mBuilder.setSound(defaultSoundUri);
        mBuilder.setAutoCancel(true);
        mBuilder.setContentTitle(title);
        mBuilder.setColor(Color.BLACK);
        mBuilder.setContentText(content);
        mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        showNotification(mBuilder);
    }

    private void showNotification(NotificationCompat.Builder builder) {
        // Get a reference of NotificationManagerCompat
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // NotificationId is a unique int for each notification that you must define
        Notification notification = builder.build();
        notificationManager.notify(NotificationsMessagingService.NOTIFICATION_ID, notification);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        StorageSharedPreferences storageSharedPreferences = new StorageSharedPreferences(this);
        storageSharedPreferences.saveTokenFCM(s);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        StorageSharedPreferences storageSharedPreferences = new StorageSharedPreferences(newBase);
        super.attachBaseContext(LocaleHelper.setLocale(newBase, storageSharedPreferences.getLanguage()));
    }
}