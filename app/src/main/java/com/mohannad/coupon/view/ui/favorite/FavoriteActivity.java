package com.mohannad.coupon.view.ui.favorite;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;


import com.mohannad.coupon.R;
import com.mohannad.coupon.data.model.FavoriteResponse;
import com.mohannad.coupon.databinding.ActivityFavoriteBinding;
import com.mohannad.coupon.utils.BaseActivity;
import com.mohannad.coupon.view.adapter.favorite.FavoriteAdapter;
import com.mohannad.coupon.view.ui.contactus.ContactUsActivity;
import com.mohannad.coupon.view.ui.image.ImageActivity;
import com.mohannad.coupon.view.ui.video.VideoActivity;

import java.util.ArrayList;

public class FavoriteActivity extends BaseActivity {

    private ActivityFavoriteBinding activityFavoriteBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityFavoriteBinding = DataBindingUtil.setContentView(this, R.layout.activity_favorite);
        FavoriteViewModel favoriteViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);
        activityFavoriteBinding.setFavoriteViewModel(favoriteViewModel);
        activityFavoriteBinding.setLifecycleOwner(this);
        FavoriteAdapter favoriteAdapter = new FavoriteAdapter(this, new ArrayList<>(),sharedPreferences.getThemeMode(),
                new FavoriteAdapter.FavoriteClickListener() {
                    @Override
                    public void shareProduct(int position, FavoriteResponse.Favorite favorite) {
                        shareText("Title : " + favorite.getNameProduct() + "\n Description : " + favorite.getDesc());
                    }

                    @Override
                    public void shareCoupon(int position, FavoriteResponse.Favorite favorite) {
                        shareText("Title : " + favorite.getCompanyName() + "\n Description : " + favorite.getDesc());

                    }

                    @Override
                    public void deleteCouponFromFavorite(int position, FavoriteResponse.Favorite favorite) {
                        favoriteViewModel.addOrRemoveCouponFavorite(favorite.getId());
                    }

                    @Override
                    public void deleteProductFromFavorite(int position, FavoriteResponse.Favorite favorite) {
                        favoriteViewModel.addOrRemoveProductFavorite(favorite.getId());
                    }

                    @Override
                    public void copyCoupon(int position, FavoriteResponse.Favorite favorite) {
                        // copy code coupon
                        copyText(favorite.getCouponCode());
                        // show dialog
                        showDefaultDialog(activityFavoriteBinding.lyContainer, getString(R.string.coupon_was_copied));
                        favoriteViewModel.copyCoupon(favorite.getId());
                    }

                    @Override
                    public void openImage(FavoriteResponse.Favorite favorite) {
                        startActivity(new Intent(getBaseContext(), ImageActivity.class).putExtra("imageUrl", favorite.getImage()));
                    }

                    @Override
                    public void openVideo(FavoriteResponse.Favorite favorite) {
                        startActivity(new Intent(getBaseContext(), VideoActivity.class).putExtra("url", favorite.getFilePath()));
                    }

                    @Override
                    public void onClickItem(FavoriteResponse.Favorite favorite) {
//                        startActivity(new Intent(requireContext(), WebViewActivity.class).putExtra("url", favorite.getLink()));
                        openBrowser(favorite.getLink());
                    }

                    @Override
                    public void answerQuestion(int position, FavoriteResponse.Favorite favorite, boolean answer) {
                        /*
                           answer
                             1- yes
                             0- no
                        */
                        favoriteViewModel.reviewCoupon(favorite.getId(), answer ? 1 : 0);
                        if (!answer) {
                            startActivity(new Intent(getBaseContext(), ContactUsActivity.class));
                        }
                    }

                    @Override
                    public void bestSelling(int position, FavoriteResponse.Favorite favorite) {
                        openBrowser(favorite.getBestSelling());
                    }
                });

        activityFavoriteBinding.rvCouponsFragmentFavorite.setAdapter(favoriteAdapter);
        favoriteViewModel.favorites.observe(this, favoriteAdapter::addAll);
        favoriteViewModel.toastMessageSuccess.observe(this, msg -> {
            showDefaultDialog(activityFavoriteBinding.lyContainer, msg);
        });
        // display error msg
        favoriteViewModel.toastMessageFailed.observe(this, msg -> {
            showAlertDialog(activityFavoriteBinding.lyContainer, msg);
        });

        activityFavoriteBinding.imgBackArrow.setOnClickListener(v -> {
            onBackPressed();
        });
    }

}