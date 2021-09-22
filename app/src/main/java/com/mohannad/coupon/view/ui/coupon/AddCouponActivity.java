package com.mohannad.coupon.view.ui.coupon;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mohannad.coupon.R;
import com.mohannad.coupon.data.model.StoreResponse;
import com.mohannad.coupon.data.model.CompaniesResponse;
import com.mohannad.coupon.data.model.CountryResponse;
import com.mohannad.coupon.databinding.ActivityAddCouponBinding;
import com.mohannad.coupon.databinding.SpinnerBottomSheetDialogBinding;
import com.mohannad.coupon.utils.BaseActivity;
import com.mohannad.coupon.utils.Constants;
import com.mohannad.coupon.view.adapter.spinner.SpinnerBottomSheetAdapter;

import java.util.ArrayList;

public class AddCouponActivity extends BaseActivity {
    private Context mContext;
    private ActivityAddCouponBinding addCouponBinding;
    private AddCouponViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_add_coupon);
        addCouponBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_coupon);
        mViewModel = new ViewModelProvider(this).get(AddCouponViewModel.class);
        addCouponBinding.setAddCouponViewModel(mViewModel);
        addCouponBinding.setLifecycleOwner(this);
        // init countries adapter
        SpinnerBottomSheetAdapter<CountryResponse.Country> adapterCountries = new SpinnerBottomSheetAdapter<>(new ArrayList<>(), (SpinnerBottomSheetAdapter.ItemClickListener<CountryResponse.Country>) (position, item) -> {
            // action listener when change item in dropdown
            // load img flag
            loadImage(this, item.getFlag(), addCouponBinding.imgCountries);
            // item selected by user from dropdown
            mViewModel.country(item.getId());
        });

        mViewModel.countries.observe(this, countries -> {
            adapterCountries.addAll(countries);
            if (countries.size() > 0) {
                // load the first img flag when get from server
                loadImage(this, countries.get(0).getFlag(), addCouponBinding.imgCountries);
                mViewModel.country(countries.get(0).getId());
            }
        });

        // when click on country img show BottomSheet dialog that will show flags
        addCouponBinding.imgCountries.setOnClickListener(v -> {
            showFilterSheet(adapterCountries, getString(R.string.choose_country));
        });

        addCouponBinding.imgBackArrow.setOnClickListener(v -> {
            onBackPressed();
        });

        // show alert dialog when send coupon
        mViewModel.toastMessageSuccess.observe(this, this::showDialog);
        mViewModel.toastMessageFailed.observe(this, msg -> {
            showAlertDialog(addCouponBinding.lyContainer, msg);
        });
        updateTheme();
    }

    private void updateTheme() {
        switch (sharedPreferences.getThemeMode()) {
            case Constants.MODERN_THEME:
                addCouponBinding.btnSend.setBackgroundResource(R.drawable.shape_blue_15dp);
                addCouponBinding.etStoreName.setBackgroundResource(R.drawable.shape_gray_radius_15dp);
                addCouponBinding.etStoreName.setTextColor(getResources().getColor(R.color.black));
                addCouponBinding.etOffer.setBackgroundResource(R.drawable.shape_gray_radius_15dp);
                addCouponBinding.etOffer.setTextColor(getResources().getColor(R.color.black));
                addCouponBinding.etCoupon.setBackgroundResource(R.drawable.shape_gray_radius_15dp);
                addCouponBinding.etCoupon.setTextColor(getResources().getColor(R.color.black));
                addCouponBinding.etDescription.setBackgroundResource(R.drawable.shape_gray_radius_15dp);
                addCouponBinding.etDescription.setTextColor(getResources().getColor(R.color.black));
                addCouponBinding.etStoreLink.setBackgroundResource(R.drawable.shape_gray_radius_15dp);
                addCouponBinding.etStoreLink.setTextColor(getResources().getColor(R.color.black));
                addCouponBinding.etEmail.setBackgroundResource(R.drawable.shape_gray_radius_15dp);
                addCouponBinding.etEmail.setTextColor(getResources().getColor(R.color.black));
                addCouponBinding.lyMobile.setBackgroundResource(R.drawable.shape_gray_radius_15dp);
                addCouponBinding.etMobile.setTextColor(getResources().getColor(R.color.black));
                break;
            case Constants.LIGHT_THEME:
                addCouponBinding.btnSend.setBackgroundResource(R.drawable.shape_gradient_blue_15dp);
                addCouponBinding.etStoreName.setBackgroundResource(R.drawable.shape_white_radius_15dp);
                addCouponBinding.etStoreName.setTextColor(getResources().getColor(R.color.black));
                addCouponBinding.etOffer.setBackgroundResource(R.drawable.shape_white_radius_15dp);
                addCouponBinding.etOffer.setTextColor(getResources().getColor(R.color.black));
                addCouponBinding.etCoupon.setBackgroundResource(R.drawable.shape_white_radius_15dp);
                addCouponBinding.etCoupon.setTextColor(getResources().getColor(R.color.black));
                addCouponBinding.etDescription.setBackgroundResource(R.drawable.shape_white_radius_15dp);
                addCouponBinding.etDescription.setTextColor(getResources().getColor(R.color.black));
                addCouponBinding.etStoreLink.setBackgroundResource(R.drawable.shape_white_radius_15dp);
                addCouponBinding.etStoreLink.setTextColor(getResources().getColor(R.color.black));
                addCouponBinding.etEmail.setBackgroundResource(R.drawable.shape_white_radius_15dp);
                addCouponBinding.etEmail.setTextColor(getResources().getColor(R.color.black));
                addCouponBinding.lyMobile.setBackgroundResource(R.drawable.shape_white_radius_15dp);
                addCouponBinding.etMobile.setTextColor(getResources().getColor(R.color.black));
                break;
            case Constants.DARK_THEME:
                addCouponBinding.btnSend.setBackgroundResource(R.drawable.shape_gradient_blue_15dp);
                addCouponBinding.etStoreName.setBackgroundResource(R.drawable.shape_black_radius_15dp);
                addCouponBinding.etStoreName.setTextColor(getResources().getColor(R.color.white));
                addCouponBinding.etOffer.setBackgroundResource(R.drawable.shape_black_radius_15dp);
                addCouponBinding.etOffer.setTextColor(getResources().getColor(R.color.white));
                addCouponBinding.etCoupon.setBackgroundResource(R.drawable.shape_black_radius_15dp);
                addCouponBinding.etCoupon.setTextColor(getResources().getColor(R.color.white));
                addCouponBinding.etDescription.setBackgroundResource(R.drawable.shape_black_radius_15dp);
                addCouponBinding.etDescription.setTextColor(getResources().getColor(R.color.white));
                addCouponBinding.etStoreLink.setBackgroundResource(R.drawable.shape_black_radius_15dp);
                addCouponBinding.etStoreLink.setTextColor(getResources().getColor(R.color.white));
                addCouponBinding.etEmail.setBackgroundResource(R.drawable.shape_black_radius_15dp);
                addCouponBinding.etEmail.setTextColor(getResources().getColor(R.color.white));
                addCouponBinding.lyMobile.setBackgroundResource(R.drawable.shape_black_radius_15dp);
                addCouponBinding.etMobile.setTextColor(getResources().getColor(R.color.white));
                break;
        }
    }

    private <T> void showFilterSheet(SpinnerBottomSheetAdapter<T> adapterItems, String title) {
        BottomSheetDialog bottomSheet = new BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme);
        SpinnerBottomSheetDialogBinding sheetView = SpinnerBottomSheetDialogBinding.inflate(LayoutInflater.from(this));
        bottomSheet.setContentView(sheetView.getRoot());
        sheetView.setLifecycleOwner(this);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(((View) sheetView.getRoot().getParent()));
        behavior.setHalfExpandedRatio(0.5f);
        behavior.setSkipCollapsed(false);
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        sheetView.tvTitle.setText(title);
        sheetView.rvItems.setAdapter(adapterItems);
        sheetView.tvHide.setOnClickListener(v -> {
            bottomSheet.cancel();
        });
        bottomSheet.show();
    }

}