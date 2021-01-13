package com.mohannad.coupon.view.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

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
import com.mohannad.coupon.view.adapter.home.CategoriesFilterAdapter;
import com.mohannad.coupon.view.adapter.home.CompaniesFilterAdapter;
import com.mohannad.coupon.view.adapter.home.HomePagesAdapter;
import com.mohannad.coupon.view.ui.search.SearchActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HomeFragment extends BaseFragment {
    private Context mContext;
    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    ArrayList<CategoriesResponse.Category> categoriesTabs = new ArrayList<>();
    private int idCategoryFilter = -1;
    private int idCompanyFilter = -1;
    private String filterSpecific;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        changeStatusBar(R.color.gray7);
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

        binding.imgFilter.setOnClickListener(v -> {
            showFilterSheet();
        });

        binding.toolbar.inflateMenu(R.menu.home_menu);
        binding.toolbar.setOnMenuItemClickListener(item -> {
            // SearchView
            SearchView searchView =
                    (SearchView) item.getActionView();
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
            return false;
        });
    }

    public void showFilterSheet() {
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
            sheetView.tvLastCoupons.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_solid_black_10dp));
            sheetView.tvLastCoupons.setTextColor(ContextCompat.getColor(mContext, R.color.white));

            sheetView.tvMostUsed.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_solid_gray_10dp));
            sheetView.tvMostUsed.setTextColor(ContextCompat.getColor(mContext, R.color.black));

        });
        sheetView.tvMostUsed.setOnClickListener(v -> {
            filterSpecific = "most_used";
            sheetView.tvMostUsed.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_solid_black_10dp));
            sheetView.tvMostUsed.setTextColor(ContextCompat.getColor(mContext, R.color.white));

            sheetView.tvLastCoupons.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_solid_gray_10dp));
            sheetView.tvLastCoupons.setTextColor(ContextCompat.getColor(mContext, R.color.black));

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
            sheetView.tvMostUsed.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_solid_gray_10dp));
            sheetView.tvMostUsed.setTextColor(ContextCompat.getColor(mContext, R.color.black));
            sheetView.tvLastCoupons.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_solid_gray_10dp));
            sheetView.tvLastCoupons.setTextColor(ContextCompat.getColor(mContext, R.color.black));
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
        if (categoriesTabs.size() != 0)
            bottomSheet.show();
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }
}