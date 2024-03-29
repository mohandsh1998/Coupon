package com.mohannad.coupon.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntegerRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.mohannad.coupon.R;
import com.mohannad.coupon.data.local.StorageSharedPreferences;

public class BaseActivity extends AppCompatActivity {
    public StorageSharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeManager.setCustomizedThemes(this, sharedPreferences.getThemeMode());
    }

    public void shareText(String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public void showDialog(String msg) {
        new AlertDialog.Builder(this)
                .setMessage(msg)
                .setPositiveButton(Html.fromHtml("<font color='#000000'>" + getString(R.string.yes) + "</font>"), (dialog, which) -> {
                    dialog.cancel();
                }).show();
    }

    public void showDefaultDialog(View view, String message) {
        showToast(view, message, R.drawable.shape_solid_green2_radius_9dp).show();
    }

    public void showSuccessDialog(View view, String message) {
        showToast(view, message, R.drawable.shape_solid_green_radius_9dp).show();
    }


    public void showAlertDialog(View view, String message) {
        showToast(view, message, R.drawable.shape_solid_red_radius_9dp).show();
    }

    public Toast showToast(View view, String msg, @DrawableRes int background) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout,
                (ViewGroup) findViewById(R.id.toast_layout_root));
        layout.setBackground(ContextCompat.getDrawable(this, background));
        TextView text = (TextView) layout.findViewById(R.id.tv_msg);
        text.setText(msg);
        if (background == R.drawable.shape_solid_red_radius_9dp)
            text.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 80);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        return toast;
    }

    // show snackbar dialog
    public Snackbar showSnackbar(View view, String msg, @DrawableRes int background) {
        // Create the Snackbar
        Snackbar snackbar = Snackbar.make(view, "", Snackbar.LENGTH_SHORT);
        // 15 is margin from all the sides for snackbar
        int marginFromSides = 15;

        //inflate view
        View snackView = getLayoutInflater().inflate(R.layout.snackbar_layout, null);
        TextView tvMsg = snackView.findViewById(R.id.tv_msg_snack_bar);
        tvMsg.setText(msg);
        // White background
        snackbar.getView().setBackgroundColor(Color.WHITE);
        // for rounded edges
        snackbar.getView().setBackground(getResources().getDrawable(background));

        Snackbar.SnackbarLayout snackBarView = (Snackbar.SnackbarLayout) snackbar.getView();
        FrameLayout.LayoutParams parentParams = (FrameLayout.LayoutParams) snackBarView.getLayoutParams();
        parentParams.setMargins(marginFromSides, 0, marginFromSides, marginFromSides);
        parentParams.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        parentParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
        snackBarView.setLayoutParams(parentParams);

        snackBarView.addView(snackView, 0);
        return snackbar;
    }

    public void copyText(String code) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", code);
        clipboard.setPrimaryClip(clip);
    }

    public void loadImage(Context context, String link, ImageView imageView) {
        Glide.with(context)
                .load(link)
                //  .placeholder(R.drawable.loading_spinner)
                .into(imageView);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        sharedPreferences = new StorageSharedPreferences(newBase);
        super.attachBaseContext(LocaleHelper.setLocale(newBase, sharedPreferences.getLanguage()));
    }

    @Override
    public void applyOverrideConfiguration(Configuration overrideConfiguration) {
        super.applyOverrideConfiguration(getBaseContext().getResources().getConfiguration());
    }

    public void openBrowser(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    public void changeToolbarAndStatusBar(@ColorRes int color, Toolbar toolbar) {
        if (toolbar != null)
            toolbar.setBackgroundColor(getResources().getColor(color));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(color));
        }
    }

    public void changeStatusBar(@ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(color));
        }
    }
}
