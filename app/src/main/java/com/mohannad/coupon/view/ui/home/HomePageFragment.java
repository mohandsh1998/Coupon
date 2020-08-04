package com.mohannad.coupon.view.ui.home;

import android.content.Context;
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
import com.mohannad.coupon.data.model.CompaniesResponse;
import com.mohannad.coupon.data.model.CouponHomeResponse;
import com.mohannad.coupon.databinding.FragmentHomePageBinding;
import com.mohannad.coupon.utils.PaginationListener;
import com.mohannad.coupon.view.adapter.home.CompaniesAdapter;
import com.mohannad.coupon.view.adapter.home.CouponsAdapter;
import com.mohannad.coupon.view.adapter.home.HomePagesAdapter;

import java.util.ArrayList;

public class HomePageFragment extends Fragment {
    private static final String TAG = "HomePageFragment";
    private Context mContext;
    private static final String ARG_ID_CATEGORY = "id_category";
    FragmentHomePageBinding binding;
    HomeViewModel homeViewModel;
    private CompaniesAdapter companiesAdapter;
    private CouponsAdapter couponsAdapter;
    private LinearLayoutManager linearLayoutManager;
    // id category will use to get all companies and coupons that exist inside the category
    private int idCategory;
    private int idCompany;
    private boolean mIsLastPage;
    private boolean mIsLoading;
    private boolean isNextPage = true;
    private int mCurrentPage = 1;
    private final int allCouponsCategory = 1;
    private final int couponsCompany = 2;
    private int requestType = allCouponsCategory;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_page, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding.setHomeViewModel(homeViewModel);
        binding.setLifecycleOwner(this);
        // get company for category
        homeViewModel.getCompanies(idCategory);
        // initialization an adapter for companies
        companiesAdapter = new CompaniesAdapter(requireActivity(), companies, (position, company) -> {
            requestType = couponsCompany;
            mCurrentPage = 1;
            idCompany = company.getId();
            // when selected company remove border on all companies item
            binding.imgAllCompanies.setBackground(requireActivity().getDrawable(R.drawable.shape_white_radius_9dp));
            // remove shadow
            binding.imgAllCompanies.setElevation(0);
            // clear array
            couponsAdapter.clear();
            binding.rvCoupons.removeAllViews();
            // get all coupons to company
            fetchCouponsCompany();
        });
        binding.rvCompanies.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
        binding.rvCompanies.setAdapter(companiesAdapter);

        // initialization an adapter for coupons
        couponsAdapter = new CouponsAdapter(requireActivity(), coupons, new CouponsAdapter.CouponClickListener() {
            @Override
            public void copyCoupon(int position, CouponHomeResponse.Coupon coupon) {
                showSnackbar(binding.lyNestedScrollview, 1000).show();
            }
        });
        linearLayoutManager = new LinearLayoutManager(requireContext());
        binding.rvCoupons.setLayoutManager(linearLayoutManager);
        binding.rvCoupons.setAdapter(couponsAdapter);
        binding.rvCoupons.setHasFixedSize(true);
        // get all coupons for category
        fetchAllCouponsCategory();
        // when select all company
        binding.imgAllCompanies.setOnClickListener(v -> {
            requestType = allCouponsCategory;
            mCurrentPage = 1;
            // remove border on selected company
            companiesAdapter.selected(-1);
            // add border on all coupons
            binding.imgAllCompanies.setBackground(requireActivity().getDrawable(R.drawable.shape_white_with_border_pink_radius_9dp));
            binding.imgAllCompanies.setElevation(24);
            // clear array
            couponsAdapter.clear();
            binding.rvCoupons.removeAllViews();
            // get all coupons to category
            fetchAllCouponsCategory();
        });
        homeViewModel.companies.observe(requireActivity(), companiesAdapter::addAll);
        homeViewModel.coupons.observe(requireActivity(), couponsAdapter::addAll);
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
//        binding.rvCoupons.addOnScrollListener(new PaginationListener(linearLayoutManager) {
//            @Override
//            protected void loadMoreItems() {
//                ++mCurrentPage;
//                switch (requestType) {
//                    case allCouponsCategory:
//                        fetchAllCouponsCategory();
//                        break;
//                    case couponsCompany:
//                        fetchCouponsCompany();
//                        break;
//                }
//            }
//
//            @Override
//            public boolean isLastPage() {
//                return mIsLastPage;
//            }
//
//            @Override
//            public boolean isLoading() {
//                return mIsLoading;
//            }
//        });

        binding.lyNestedScrollview.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == ( v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight() )) {
                Log.i(TAG, "BOTTOM SCROLL");
                if (!mIsLoading && !mIsLastPage) {
                    ++mCurrentPage;
                    switch (requestType) {
                        case allCouponsCategory:
                            fetchAllCouponsCategory();
                            break;
                        case couponsCompany:
                            fetchCouponsCompany();
                            break;
                    }
                }
            }
        });

    }

    // custome snackbar
    private Snackbar showSnackbar(NestedScrollView nestedScrollView, int duration) {
        // Create the Snackbar
        Snackbar snackbar = Snackbar.make(nestedScrollView, "", duration);
        // 15 is margin from all the sides for snackbar
        int marginFromSides = 15;

        //inflate view
        View snackView = getLayoutInflater().inflate(R.layout.snackbar_layout, null);

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

//    private void handleLoadMore() {
//        // initialise loading state
//        mIsLastPage = false;
////        // set up scroll listener
//        binding.rvCoupons.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                // number of visible items
//                int visibleItemCount = linearLayoutManager.getChildCount();
//                // number of items in layout
//                int totalItemCount = linearLayoutManager.getItemCount();
//                // the position of first visible item
//                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
//
//                boolean isNotLoadingAndNotLastPage = !mIsLoading && !mIsLastPage;
//                // flag if number of visible items is at the last
//                boolean isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount;
//                // validate non negative values
//                boolean isValidFirstItem = firstVisibleItemPosition >= 0;
//                // validate total items are more than possible visible items
//                boolean totalIsMoreThanVisible = isNextPage;
//                // flag to know whether to load more
//                boolean shouldLoadMore = isValidFirstItem && isAtLastItem && totalIsMoreThanVisible && isNotLoadingAndNotLastPage;
//                if (shouldLoadMore && dy > 0) {
//                    mCurrentPage = mCurrentPage + 1;
//                    switch (requestType) {
//                        case allCouponsCategory:
//                            fetchAllCouponsCategory();
//                            break;
//                        case couponsCompany:
//                            fetchCouponsCompany();
//                            break;
//                    }
//                }
//            }
//        });
//        binding.lyNestedScrollview.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                if ((scrollY >= (binding.rvCoupons.getMeasuredHeight() - v.getMeasuredHeight())) && scrollY > oldScrollY) {
//                    if (isNextPage) {
//                        mCurrentPage = mCurrentPage + 1;
//                        switch (requestType) {
//                            case allCouponsCategory:
//                                fetchAllCouponsCategory();
//                                break;
//                            case couponsCompany:
//                                fetchCouponsCompany();
//                                break;
//                        }
//                    }
//                }
//            }
//        });
//    }

    private void initPagination() {
        isNextPage = true;
        mCurrentPage = 1;
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