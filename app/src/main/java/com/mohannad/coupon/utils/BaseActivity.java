package com.mohannad.coupon.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.IntegerRes;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.mohannad.coupon.R;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showDefaultDialog(View view, String message) {
        showSnackbar(view, message, R.drawable.shape_snake_bar_pink).show();
    }

    public void showSuccessDialog(View view, String message) {
        showSnackbar(view, message, R.drawable.shape_snake_bar_green).show();
    }


    public void showAlertDialog(View view, String message) {
        showSnackbar(view, message, R.drawable.shape_snake_bar_red).show();
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
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    public void applyOverrideConfiguration(Configuration overrideConfiguration) {
        super.applyOverrideConfiguration(getBaseContext().getResources().getConfiguration());
    }
}
