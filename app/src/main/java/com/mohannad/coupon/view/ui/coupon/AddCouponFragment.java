package com.mohannad.coupon.view.ui.coupon;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mohannad.coupon.R;
import com.mohannad.coupon.data.model.CategoriesResponse;
import com.mohannad.coupon.data.model.CompaniesResponse;
import com.mohannad.coupon.data.model.CountryResponse;
import com.mohannad.coupon.databinding.FragmentAddCouponBinding;
import com.mohannad.coupon.databinding.SpinnerBottomSheetDialogBinding;
import com.mohannad.coupon.utils.BaseFragment;
import com.mohannad.coupon.view.adapter.spinner.SpinnerBottomSheetAdapter;

import java.util.ArrayList;

public class AddCouponFragment extends BaseFragment {
    private FragmentAddCouponBinding addCouponBinding;
    private AddCouponViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        addCouponBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_coupon, container, false);
        return addCouponBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AddCouponViewModel.class);
        addCouponBinding.setAddCouponViewModel(mViewModel);
        addCouponBinding.setLifecycleOwner(this);
        // init countries adapter
        SpinnerBottomSheetAdapter<CountryResponse.Country> adapterCountries = new SpinnerBottomSheetAdapter<>(new ArrayList<>(), (SpinnerBottomSheetAdapter.ItemClickListener<CountryResponse.Country>) (position, item) -> {
            // action listener when change item in dropdown
            // load img flag
            loadImage(getContext(), item.getFlag(), addCouponBinding.imgCountries);
            // item selected by user from dropdown
            mViewModel.country(item.getId());
        });

        mViewModel.countries.observe(requireActivity(), countries -> {
            adapterCountries.addAll(countries);
            if (countries.size() > 0) {
                // load the first img flag when get from server
                loadImage(getContext(), countries.get(0).getFlag(), addCouponBinding.imgCountries);
                mViewModel.country(countries.get(0).getId());
            }
        });

        // when click on country img show BottomSheet dialog that will show flags
        addCouponBinding.imgCountries.setOnClickListener(v -> {
            showFilterSheet(adapterCountries, getString(R.string.choose_country));
        });

        // array categories
        ArrayList<CategoriesResponse.Category> categories = new ArrayList<>();
        // init categories adapter
        SpinnerBottomSheetAdapter<CategoriesResponse.Category> adapterCategories =
                new SpinnerBottomSheetAdapter<>(categories, (SpinnerBottomSheetAdapter.ItemClickListener<CategoriesResponse.Category>) (position, item) -> {
                    // action listener when change item in dropdown
                    // display category name in edit text
                    addCouponBinding.etCategories.setText(item.getName());
                    // item selected by user from dropdown
                    mViewModel.category(item.getId());
                    // get companies for the category selected by the user in the dropdown
                    mViewModel.getCompanies(item.getId());
                });
        // when click on editText categories show BottomSheet dialog that will show categories
        addCouponBinding.etCategories.setOnClickListener(v -> {
            showFilterSheet(adapterCategories, getString(R.string.choose_category));
        });

        mViewModel.categories.observe(requireActivity(), categoriesList -> {
            adapterCategories.addAll(categoriesList);
            if (categoriesList.size() > 0) {
                // display the first category name in editText when get from server
                addCouponBinding.etCategories.setText(categoriesList.get(0).getName());
                mViewModel.category(categoriesList.get(0).getId());
                // get companies for the first category
                mViewModel.getCompanies(categoriesList.get(0).getId());
            }
        });

        // array companies for specific category
        ArrayList<CompaniesResponse.Company> companies = new ArrayList<>();
        // init companies adapter
        SpinnerBottomSheetAdapter<CompaniesResponse.Company> adapterCompanies =
                new SpinnerBottomSheetAdapter<>(companies, (SpinnerBottomSheetAdapter.ItemClickListener<CompaniesResponse.Company>) (position, item) -> {
                    // action listener when change item in dropdown
                    // display company name in edit text
                    addCouponBinding.etCompanies.setText(item.getName());
                    // item selected by user from dropdown
                    mViewModel.company(item.getId());
                });

        // when click on editText companies show BottomSheet dialog that will show companies for specific category
        addCouponBinding.etCompanies.setOnClickListener(v -> {
            showFilterSheet(adapterCompanies, getString(R.string.choose_company));
        });

        mViewModel.companies.observe(requireActivity(), companyList -> {
            adapterCompanies.addAll(companyList);
            if (companyList.size() > 0) {
                // display the first company name in editText when get from server
                addCouponBinding.etCompanies.setText(companyList.get(0).getName());
                mViewModel.company(companyList.get(0).getId());
            }
        });

        // show alert dialog when send coupon
        mViewModel.toastMessageSuccess.observe(requireActivity(), this::showDialog);
        mViewModel.toastMessageFailed.observe(requireActivity(), msg -> {
            showAlertDialog(addCouponBinding.lyContainer, msg);
        });
    }

    private <T> void showFilterSheet(SpinnerBottomSheetAdapter<T> adapterItems, String title) {
        BottomSheetDialog bottomSheet = new BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme);
        SpinnerBottomSheetDialogBinding sheetView = SpinnerBottomSheetDialogBinding.inflate(LayoutInflater.from(requireContext()));
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