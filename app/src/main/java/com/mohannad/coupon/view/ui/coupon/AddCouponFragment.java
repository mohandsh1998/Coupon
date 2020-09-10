package com.mohannad.coupon.view.ui.coupon;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.mohannad.coupon.R;
import com.mohannad.coupon.data.model.CategoriesResponse;
import com.mohannad.coupon.data.model.CompaniesResponse;
import com.mohannad.coupon.data.model.CountryResponse;
import com.mohannad.coupon.databinding.FragmentAddCouponBinding;
import com.mohannad.coupon.utils.BaseFragment;
import com.mohannad.coupon.view.adapter.spinner.SpinnerAdapter;

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
        SpinnerAdapter adapterCountries = new SpinnerAdapter(requireContext(), new ArrayList<>());
        addCouponBinding.spinnerCountries.setAdapter(adapterCountries);
        mViewModel.countries.observe(requireActivity(), adapterCountries::addAll);
        addCouponBinding.spinnerCountries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // item selected by user from spinner
                CountryResponse.Country country = (CountryResponse.Country) parent.getItemAtPosition(position);
                mViewModel.country(country.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // array categories
        ArrayList<CategoriesResponse.Category> categories = new ArrayList<>();
        // init categories adapter
        ArrayAdapter<CategoriesResponse.Category> adapterCategories = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, categories);
        addCouponBinding.spinnerCategories.setAdapter(adapterCategories);
        // layout dropdown menu in spinner
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mViewModel.categories.observe(requireActivity(), adapterCategories::addAll);
        // action listener when change item in dropdown
        addCouponBinding.spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // item selected by user from spinner
                CategoriesResponse.Category category = (CategoriesResponse.Category) parent.getItemAtPosition(position);
                mViewModel.category(category.getId());
                // get companies for the category selected by the user in the spinner
                mViewModel.getCompanies(category.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // array companies for specific category
        ArrayList<CompaniesResponse.Company> companies = new ArrayList<>();
        // init companies adapter
        ArrayAdapter<CompaniesResponse.Company> adapterCompanies = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, companies);
        // layout dropdown menu in spinner
        adapterCompanies.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mViewModel.companies.observe(requireActivity(), companyList -> {
            // clear previous companies
            companies.clear();
            // add the new companies
            companies.addAll(companyList);
            addCouponBinding.spinnerCompanies.setAdapter(adapterCompanies);
        });
        // action listener when change item in dropdown
        addCouponBinding.spinnerCompanies.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // item selected by user from spinner
                CompaniesResponse.Company company = (CompaniesResponse.Company) parent.getItemAtPosition(position);
                mViewModel.company(company.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // show alert dialog when send coupon
        mViewModel.toastMessageSuccess.observe(requireActivity(), this::showDialog);
        mViewModel.toastMessageFailed.observe(requireActivity(), msg -> {
            showAlertDialog(addCouponBinding.lyContainer, msg);
        });
    }

}