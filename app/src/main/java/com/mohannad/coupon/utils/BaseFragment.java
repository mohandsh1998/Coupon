package com.mohannad.coupon.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.mohannad.coupon.BuildConfig;
import com.mohannad.coupon.R;

import static android.content.Context.CLIPBOARD_SERVICE;

public class BaseFragment extends Fragment {

    public void loadImage(Context context, String link, ImageView imageView) {
        if (context != null)
            Glide.with(context)
                    .load(link)
                    //  .placeholder(R.drawable.loading_spinner)
                    .into(imageView);
    }

    // alert dialog will show at center of screen
    public void showDialog(String msg) {
        new AlertDialog.Builder(requireContext())
                .setMessage(msg)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    dialog.cancel();
                }).show();
    }

    public void showDefaultDialog(View view, String message) {
        showToast(view, message, R.drawable.shape_solid_green_radius_9dp).show();
    }

    public void showSuccessDialog(View view, String message) {
        showToast(view, message, R.drawable.shape_solid_green_radius_9dp).show();
    }


    public void showAlertDialog(View view, String message) {
        showToast(view, message, R.drawable.shape_solid_red_radius_9dp).show();
    }

    public Toast showToast(View view, String msg, @DrawableRes int background) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout, null);
        layout.setBackground(ContextCompat.getDrawable(getContext(), background));
        TextView text = (TextView) layout.findViewById(R.id.tv_msg);
        if (background == R.drawable.shape_solid_red_radius_9dp)
            text.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
        text.setText(msg);
        Toast toast = new Toast(getContext());
        toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL, 0, 80);
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

    public void shareText(String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public void openBrowser(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    public void copyText(String code) {
        ClipboardManager clipboard = (ClipboardManager) requireActivity().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", code);
        clipboard.setPrimaryClip(clip);
    }

    public void changeStatusBar(@ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = requireActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(color));
        }
    }
}
