package com.mohannad.coupon.service;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mohannad.coupon.data.local.StorageSharedPreferences;
import com.mohannad.coupon.utils.LocaleHelper;

public class NotificationsMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
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