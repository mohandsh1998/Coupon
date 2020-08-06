package com.mohannad.coupon.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.IntegerRes;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.mohannad.coupon.R;

import static android.content.Context.CLIPBOARD_SERVICE;

public class BaseFragment extends Fragment {
    public void loadImage(Context context, String link, ImageView imageView) {
        Glide.with(context)
                .load(link)
                //  .placeholder(R.drawable.loading_spinner)
                .into(imageView);
    }

    // show snackbar dialog
    public Snackbar showSnackbar(View view, String msg) {
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
        snackbar.getView().setBackground(getResources().getDrawable(R.drawable.shape_snake_bar));

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
        ClipboardManager clipboard = (ClipboardManager) requireActivity().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", code);
        clipboard.setPrimaryClip(clip);
    }
}
