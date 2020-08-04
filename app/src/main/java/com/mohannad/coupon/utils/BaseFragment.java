package com.mohannad.coupon.utils;

import android.content.Context;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

public class BaseFragment extends Fragment {
    public void loadImage(Context context, String link, ImageView imageView) {
        Glide.with(context)
                .load(link)
                //  .placeholder(R.drawable.loading_spinner)
                .into(imageView);
    }
}
