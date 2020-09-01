package com.mohannad.coupon.view.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayoutMediator;
import com.mohannad.coupon.R;
import com.mohannad.coupon.data.local.StorageSharedPreferences;
import com.mohannad.coupon.data.model.CategoriesResponse;
import com.mohannad.coupon.data.model.CompaniesResponse;
import com.mohannad.coupon.databinding.FilterBottomSheetDialogBinding;
import com.mohannad.coupon.databinding.FragmentHomeBinding;
import com.mohannad.coupon.utils.BaseFragment;
import com.mohannad.coupon.view.adapter.deal.SlideAdsAdapter;
import com.mohannad.coupon.view.adapter.home.CategoriesFilterAdapter;
import com.mohannad.coupon.view.adapter.home.CompaniesFilterAdapter;
import com.mohannad.coupon.view.adapter.home.HomePagesAdapter;
import com.mohannad.coupon.view.ui.deal.DealViewModel;
import com.mohannad.coupon.view.ui.search.SearchActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment {
    private Context mContext;
    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    ArrayList<CategoriesResponse.Category> categoriesTabs = new ArrayList<>();
    private int idCategoryFilter = -1;
    private int idCompanyFilter = -1;
    private String filterSpecific;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        StorageSharedPreferences sharedPreferences = new StorageSharedPreferences(requireContext());
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding.setHomeViewModel(homeViewModel);
        binding.setLifecycleOwner(this);
        binding.topText.setText(sharedPreferences.getAdsTitle());
        // start marquee
        binding.topText.setSelected(true);
        // initialization an adapter for categories pages in the home
        HomePagesAdapter homePagesAdapter = new HomePagesAdapter(requireActivity(), categoriesTabs);
        binding.vpCouponsPages.setAdapter(homePagesAdapter);
        // A mediator to link a TabLayout with a ViewPager2
        new TabLayoutMediator(binding.tabsCategories, binding.vpCouponsPages,
                (tab, position) -> tab.setText(categoriesTabs.get(position).getName())
        ).attach();

        // display error msg
        homeViewModel.toastMessageFailed.observe(requireActivity(), msg -> {
            showAlertDialog(binding.lyContainer, msg);
        });
        homeViewModel.categoriesTabs.observe(requireActivity(), homePagesAdapter::addAll);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.home_menu, menu);
        // SearchView
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        // Hint SearchView
        searchView.setQueryHint(getString(R.string.what_would_you_like_to_find));
        // Background SearchView
        searchView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_gray2_raduis_9dp));
        // close icon in SearchView
        ImageView searchViewCloseIcon = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        searchViewCloseIcon.setEnabled(false);
        // hide back arrow
        searchView.setIconifiedByDefault(true);
        // disable focusable
        searchView.setFocusable(false);
        // hide search icon in search view
        searchView.setIconified(false);
        searchView.clearFocus();
        searchView.requestFocusFromTouch();

        // EditText for SearchView
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_search_pink, 0, 0, 0);
        searchEditText.setCompoundDrawablePadding(16);
        searchEditText.setTextSize(14);
        searchEditText.setTextColor(Color.BLACK);
        searchEditText.setHintTextColor(mContext.getResources().getColor(R.color.gray0));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                startActivity(new Intent(requireContext(), SearchActivity.class).putExtra("word", query).putExtra("type", "search"));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void showFilterSheet() {
        BottomSheetDialog bottomSheet = new BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme);
        FilterBottomSheetDialogBinding sheetView = FilterBottomSheetDialogBinding.inflate(LayoutInflater.from(requireContext()));
        bottomSheet.setContentView(sheetView.getRoot());
        sheetView.setHomeViewModel(homeViewModel);
        sheetView.setLifecycleOwner(this);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(((View) sheetView.getRoot().getParent()));
        behavior.setSkipCollapsed(true);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        ArrayList<CompaniesResponse.Company> companies = new ArrayList<>();
        CompaniesFilterAdapter companiesFilterAdapter = new CompaniesFilterAdapter(requireContext(), companies, (position, company) -> idCompanyFilter = companies.get(position).getId());
        CategoriesFilterAdapter categoriesFilterAdapter = new CategoriesFilterAdapter(requireContext(), categoriesTabs, (position, category) -> {
            // set id category selected
            idCategoryFilter = category.getId();
            idCompanyFilter = -1;
            // clear companies
            companiesFilterAdapter.clear();
            companiesFilterAdapter.selected(-1);
            sheetView.rvCompaniesFilter.removeAllViews();
            // get companies for selected category
            homeViewModel.getCompanies(categoriesTabs.get(position).getId());
        });
        sheetView.rvCategoryFilter.setAdapter(categoriesFilterAdapter);
        sheetView.tvLastCoupons.setOnClickListener(v -> {
            filterSpecific = "latest";
            sheetView.tvLastCoupons.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_stroke_pink_raduis_15dp));
            sheetView.tvMostUsed.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_stroke_black_15dp));
        });
        sheetView.tvMostUsed.setOnClickListener(v -> {
            filterSpecific = "most_used";
            sheetView.tvMostUsed.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_stroke_pink_raduis_15dp));
            sheetView.tvLastCoupons.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_stroke_black_15dp));
        });
        // clear call item
        sheetView.btnReset.setOnClickListener(v -> {
            idCategoryFilter = -1;
            idCompanyFilter = -1;
            filterSpecific = null;
            categoriesFilterAdapter.selected(-1);
            companiesFilterAdapter.selected(-1);
            sheetView.rvCategoryFilter.scrollToPosition(0);
            sheetView.rvCompaniesFilter.scrollToPosition(0);
            sheetView.tvMostUsed.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_stroke_black_15dp));
            sheetView.tvLastCoupons.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_stroke_black_15dp));
        });
        sheetView.btnApply.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), SearchActivity.class)
                    .putExtra("idCategory", idCategoryFilter)
                    .putExtra("idCompany", idCompanyFilter)
                    .putExtra("filterSpecific", filterSpecific)
                    .putExtra("type", "filter"));
        });
        sheetView.rvCompaniesFilter.setAdapter(companiesFilterAdapter);
        // get companies for first category
        homeViewModel.getCompanies(categoriesTabs.get(0).getId());
        homeViewModel.companies.observe(requireActivity(), companiesFilterAdapter::addAll);
        bottomSheet.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.menu_filter) {
            if (categoriesTabs.size() != 0)
                showFilterSheet();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }
}