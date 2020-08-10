package com.mohannad.coupon.view.ui.home;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayoutMediator;
import com.mohannad.coupon.R;
import com.mohannad.coupon.data.local.StorageSharedPreferences;
import com.mohannad.coupon.data.model.CompaniesResponse;
import com.mohannad.coupon.data.model.CouponHomeResponse;
import com.mohannad.coupon.databinding.FragmentHomePageBinding;
import com.mohannad.coupon.utils.BaseFragment;
import com.mohannad.coupon.utils.PaginationListener;
import com.mohannad.coupon.view.adapter.home.CompaniesAdapter;
import com.mohannad.coupon.view.adapter.home.CouponsAdapter;
import com.mohannad.coupon.view.adapter.home.HomePagesAdapter;
import com.mohannad.coupon.view.ui.auth.login.LoginActivity;
import com.mohannad.coupon.view.ui.webview.WebViewActivity;

import java.util.ArrayList;

import static android.content.Context.CLIPBOARD_SERVICE;

public class HomePageFragment extends BaseFragment {
    private static final String TAG = "HomePageFragment";
    private static final String ARG_ID_CATEGORY = "id_category";
    // current page for pagination
    private int mCurrentPage = 1;
    private final int ALL_COUPONS_CATEGORY = 1;
    private final int COUPONS_COMPANY = 2;
    // request type to determine the type of request for (all coupons or coupons to company)
    private int requestType = ALL_COUPONS_CATEGORY;
    // id category will use to get all companies and coupons that exist inside the category
    private int idCategory;
    // id company will use to get coupons to company
    private int idCompany;
    private Context mContext;
    FragmentHomePageBinding binding;
    HomeViewModel homeViewModel;
    private StorageSharedPreferences storageSharedPreferences;
    // adapter companies
    private CompaniesAdapter companiesAdapter;
    // adapter coupons
    private CouponsAdapter couponsAdapter;
    private LinearLayoutManager linearLayoutManager;

    private boolean mIsLastPage;
    private boolean mIsLoading;

    ArrayList<CompaniesResponse.Company> companies = new ArrayList<>();
    ArrayList<CouponHomeResponse.Coupon> coupons = new ArrayList<>();

    public HomePageFragment() {
        // Required empty public constructor
    }

    public static HomePageFragment newInstance(int idCategoryTab) {
        HomePageFragment fragment = new HomePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID_CATEGORY, idCategoryTab);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idCategory = getArguments().getInt(ARG_ID_CATEGORY);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_page, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        storageSharedPreferences = new StorageSharedPreferences(mContext);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding.setHomeViewModel(homeViewModel);
        binding.setLifecycleOwner(this);
        // get companies for category
        homeViewModel.getCompanies(idCategory);
        // initialization an adapter for companies
        companiesAdapter = new CompaniesAdapter(requireActivity(), companies, (position, company) -> {
            // when click to select company by user
            // set request type to coupons company
            requestType = COUPONS_COMPANY;
            // init current page
            mCurrentPage = 1;
            // set id company
            idCompany = company.getId();
            // when select company remove border and shadow on "All" View
            couponsAdapter.selectAllView(false);
            // clear array
            couponsAdapter.clear();
            binding.rvCoupons.removeAllViews();
            // get all coupons to company
            fetchCouponsCompany();
        });

        // initialization an adapter for coupons
        couponsAdapter = new CouponsAdapter(requireActivity(), coupons, companiesAdapter, new CouponsAdapter.CouponClickListener() {
            @Override
            public void copyCoupon(int position, CouponHomeResponse.Coupon coupon) {
                // copy code coupon
                copyText(coupon.getCouponCode());
                // show dialog
                showSnackbar(binding.lyContainer, getString(R.string.coupon_was_copied)).show();
                homeViewModel.copyCoupon(coupon.getId());
            }

            @Override
            public void shopNowCoupon(int position, CouponHomeResponse.Coupon coupon) {
                startActivity(new Intent(mContext, WebViewActivity.class).putExtra("url", coupon.getLink()));
            }

            @Override
            public void shopNowAds(int position, CouponHomeResponse.Coupon coupon) {
                startActivity(new Intent(mContext, WebViewActivity.class).putExtra("url", coupon.getLink()));
            }


            @Override
            public void answerQuestion(int position, CouponHomeResponse.Coupon coupon, boolean answer) {

            }

            @Override
            public void onClickAllCoupons() {
                // when click to select "All" view by user
                // set request type to all coupons
                requestType = ALL_COUPONS_CATEGORY;
                // init current page
                mCurrentPage = 1;
                // remove border on selected company
                companiesAdapter.selected(-1);
                // clear array
                couponsAdapter.clear();
                binding.rvCoupons.removeAllViews();
                // get all coupons to category
                fetchAllCouponsCategory();
            }

            @Override
            public void shareCoupon(int position, CouponHomeResponse.Coupon coupon) {

            }

            @Override
            public void addToFavoriteCoupon(int position, CouponHomeResponse.Coupon coupon) {
                if (storageSharedPreferences.getLogInState())
                    homeViewModel.addOrRemoveCouponFavorite(coupon.getId());
                else startActivity(new Intent(mContext, LoginActivity.class));
            }

        });
        linearLayoutManager = new LinearLayoutManager(requireContext());
        binding.rvCoupons.setLayoutManager(linearLayoutManager);
        binding.rvCoupons.setAdapter(couponsAdapter);
        binding.rvCoupons.setHasFixedSize(true);
        // get all coupons for category
        fetchAllCouponsCategory();

        homeViewModel.companies.observe(requireActivity(), companiesAdapter::addAll);
        homeViewModel.coupons.observe(requireActivity(), couponsAdapter::addAll);
        // display success msg
        homeViewModel.toastMessageSuccess.observe(requireActivity(), msg -> {
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        });
        // display error msg
        homeViewModel.toastMessageFailed.observe(requireActivity(), msg -> {
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        });
        homeViewModel.isLastPage.observe(requireActivity(), lastPage -> {
            this.mIsLastPage = lastPage;
        });
        homeViewModel.dataLoadingCoupons.observe(requireActivity(), loading -> {
            this.mIsLoading = loading;
        });
        binding.rvCoupons.addOnScrollListener(new PaginationListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                ++mCurrentPage;
                switch (requestType) {
                    case ALL_COUPONS_CATEGORY:
                        fetchAllCouponsCategory();
                        break;
                    case COUPONS_COMPANY:
                        fetchCouponsCompany();
                        break;
                }
            }

            @Override
            public boolean isLastPage() {
                return mIsLastPage;
            }

            @Override
            public boolean isLoading() {
                return mIsLoading;
            }
        });


    }

    private void fetchAllCouponsCategory() {
        // get all coupons for category
        homeViewModel.getAllCouponsCategory(idCategory, mCurrentPage);
    }

    private void fetchCouponsCompany() {
        // get all coupons to company
        homeViewModel.getCouponsCompany(idCompany, mCurrentPage);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }
}