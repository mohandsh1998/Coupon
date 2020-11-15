package com.mohannad.coupon.view.ui.favorite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.mohannad.coupon.R;
import com.mohannad.coupon.callback.ICommunicateMainActivity;
import com.mohannad.coupon.data.model.FavoriteResponse;
import com.mohannad.coupon.databinding.FragmentFavoriteBinding;
import com.mohannad.coupon.utils.BaseFragment;
import com.mohannad.coupon.view.adapter.favorite.FavoriteAdapter;
import com.mohannad.coupon.view.ui.contactus.ContactUsActivity;
import com.mohannad.coupon.view.ui.deal.DealViewModel;
import com.mohannad.coupon.view.ui.home.HomeViewModel;
import com.mohannad.coupon.view.ui.image.ImageActivity;
import com.mohannad.coupon.view.ui.product.ProductsActivity;
import com.mohannad.coupon.view.ui.video.VideoActivity;
import com.mohannad.coupon.view.ui.webview.WebViewActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FavoriteFragment extends BaseFragment {

    private FragmentFavoriteBinding fragmentFavoriteBinding;
    private ICommunicateMainActivity mListener;

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        if (context instanceof ICommunicateMainActivity) {
            mListener = (ICommunicateMainActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ICommunicateHomeActivity");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListener.onInteractionFavoriteFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fragmentFavoriteBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite, container, false);
        return fragmentFavoriteBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FavoriteViewModel favoriteViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);
        fragmentFavoriteBinding.setFavoriteViewModel(favoriteViewModel);
        fragmentFavoriteBinding.setLifecycleOwner(this);
        FavoriteAdapter favoriteAdapter = new FavoriteAdapter(requireContext(), new ArrayList<>(),
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
                        showDefaultDialog(fragmentFavoriteBinding.lyContainer, getString(R.string.coupon_was_copied));
                        favoriteViewModel.copyCoupon(favorite.getId());
                    }

                    @Override
                    public void openImage(FavoriteResponse.Favorite favorite) {
                        startActivity(new Intent(requireContext(), ImageActivity.class).putExtra("imageUrl", favorite.getImage()));
                    }

                    @Override
                    public void openVideo(FavoriteResponse.Favorite favorite) {
                        startActivity(new Intent(requireContext(), VideoActivity.class).putExtra("url", favorite.getFilePath()));
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
                            startActivity(new Intent(requireContext(), ContactUsActivity.class));
                        }
                    }
                });

        fragmentFavoriteBinding.rvCouponsFragmentFavorite.setAdapter(favoriteAdapter);
        favoriteViewModel.favorites.observe(requireActivity(), favoriteAdapter::addAll);
        favoriteViewModel.toastMessageSuccess.observe(requireActivity(), msg -> {
            showDefaultDialog(fragmentFavoriteBinding.lyContainer, msg);
        });
        // display error msg
        favoriteViewModel.toastMessageFailed.observe(requireActivity(), msg -> {
            showAlertDialog(fragmentFavoriteBinding.lyContainer, msg);
        });
    }
}