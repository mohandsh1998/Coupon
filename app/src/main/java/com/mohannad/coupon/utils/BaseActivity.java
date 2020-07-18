package com.mohannad.coupon.utils;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.IntegerRes;
import androidx.appcompat.app.AppCompatActivity;

import com.mohannad.coupon.R;
import com.tapadoo.alerter.Alerter;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showSuccessDialog(String message) {
        showDialog(this, message, R.color.green);
    }


    public void showAlertDialog(String message) {
        showDialog(this, message, R.color.red);

    }

    public static void showDialog(AppCompatActivity activity, String message, int color) {
        Alerter.create(activity)
                .setText(message)
                .setDuration(2000)
                .enableVibration(true)
                .enableSwipeToDismiss()
                .setBackgroundColorRes(color)
                .show();
    }
}
