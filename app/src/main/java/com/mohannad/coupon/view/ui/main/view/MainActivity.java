package com.mohannad.coupon.view.ui.main.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.mohannad.coupon.R;
import com.mohannad.coupon.data.local.StorageSharedPreferences;
import com.mohannad.coupon.data.model.CompaniesResponse;
import com.mohannad.coupon.data.model.Coupon;
import com.mohannad.coupon.data.model.StoreResponse;
import com.mohannad.coupon.databinding.ActivityMainBinding;
import com.mohannad.coupon.utils.BaseActivity;
import com.mohannad.coupon.utils.PaginationListener;
import com.mohannad.coupon.view.adapter.home.CompaniesAdapter;
import com.mohannad.coupon.view.adapter.home.CouponsAdapter;
import com.mohannad.coupon.view.adapter.main.StoreHorizontalAdapter;
import com.mohannad.coupon.view.ui.contactus.ContactUsActivity;
import com.mohannad.coupon.view.ui.main.viewmodel.MainViewModel;
import com.mohannad.coupon.view.ui.product.ProductsActivity;
import com.mohannad.coupon.view.ui.search.SearchActivity;
import com.mohannad.coupon.view.ui.store.view.StoresActivity;
import com.mohannad.coupon.view.ui.trend.TrendCouponsActivity;

import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    // current page for pagination
    private int mCurrentPage = 1;
    private final int ALL_COUPONS_STORE = 1;
    private final int COUPONS_COMPANY = 2;
    // request type to determine the type of request for (all coupons or coupons for specific company)
    private int requestType = ALL_COUPONS_STORE;
    // id store will use to get all companies and coupons that exist inside the store
    private int storeId;
    // id company will use to get coupons for company
    private int idCompany;
    private MainViewModel mainViewModel;
    private StorageSharedPreferences storageSharedPreferences;
    // adapter companies
    private CompaniesAdapter companiesAdapter;
    // adapter coupons
    private CouponsAdapter couponsAdapter;
    private LinearLayoutManager linearLayoutManager;

    private boolean mIsLastPage;
    private boolean mIsLoading;

    ArrayList<CompaniesResponse.Company> companies = new ArrayList<>();
    ArrayList<Coupon> coupons = new ArrayList<>();

    StoreHorizontalAdapter adapter;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        storageSharedPreferences = new StorageSharedPreferences(this);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding.setMainViewModel(mainViewModel);
        binding.setLifecycleOwner(this);

        List<StoreResponse.Store> stores = getIntent().getParcelableArrayListExtra(StoresActivity.STORES_KEY);
        int position = getIntent().getIntExtra(StoresActivity.POSITION_KEY, 0);
        storeId = getIntent().getIntExtra(StoresActivity.STORE_ID_KEY, 0);
        adapter = new StoreHorizontalAdapter(this, stores, storeId, sharedPreferences.getThemeMode(), storeId -> {
            adapter.setStoreIdSelected(storeId);
            this.storeId = storeId;
            this.mCurrentPage = 1;
            requestType = ALL_COUPONS_STORE;
            companiesAdapter.clear();
            couponsAdapter.clear();
            // get companies for store
            mainViewModel.getCompanies(this.storeId);
            // get all coupons for store
            fetchAllCouponsStore();
        });
        binding.rvStores.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.rvStores.setAdapter(adapter);
        binding.rvStores.scrollToPosition(position);

        // get companies for store
        mainViewModel.getCompanies(storeId);
        // initialization an adapter for companies
        companiesAdapter = new CompaniesAdapter(this, companies, sharedPreferences.getThemeMode(), new CompaniesAdapter.CompanyClickListener() {
            @Override
            public void onCompanySelected(int position, CompaniesResponse.Company company) {
                // when click to select company by user
                // set request type to coupons company
                requestType = COUPONS_COMPANY;
                // init current page
                mCurrentPage = 1;
                // set id company
                idCompany = company.getId();
                // clear array
                couponsAdapter.clear();
                // binding.rvCoupons.removeAllViews();
                // get all coupons to company
                fetchCouponsCompany();
            }

            @Override
            public void onClickAllCoupons() {
                // when click to select "All" view by user
                // set request type to all coupons
                requestType = ALL_COUPONS_STORE;
                // init current page
                mCurrentPage = 1;
                // clear array
                couponsAdapter.clear();
                // binding.rvCoupons.removeAllViews();
                // get all coupons to store
                fetchAllCouponsStore();
            }
        });

        // initialization an adapter for coupons
        couponsAdapter = new CouponsAdapter(this, coupons, sharedPreferences.getThemeMode(), companiesAdapter, new CouponsAdapter.CouponClickListener() {
            @Override
            public void copyCoupon(int position, Coupon coupon) {
                // copy code coupon
                copyText(coupon.getCouponCode());
                // show dialog
                showDefaultDialog(binding.lyContainer, getString(R.string.coupon_was_copied));
                mainViewModel.copyCoupon(coupon.getId());
            }

            @Override
            public void shopNowCoupon(int position, Coupon coupon) {
//                startActivity(new Intent(mContext, WebViewActivity.class).putExtra("url", coupon.getLink()));
                openBrowser(coupon.getLink());
            }

            @Override
            public void shopNowAds(int position, Coupon coupon) {
//                startActivity(new Intent(mContext, WebViewActivity.class).putExtra("url", coupon.getLink()));
                openBrowser(coupon.getLink());
            }


            @Override
            public void answerQuestion(int position, Coupon coupon, boolean answer) {
                /*
                  answer
                  1- yes
                  0- no
                  */
                mainViewModel.reviewCoupon(coupon.getId(), answer ? 1 : 0);
                if (!answer) {
                    startActivity(new Intent(getBaseContext(), ContactUsActivity.class));
                }
            }

            @Override
            public void openProductActivity(int position, Coupon coupon) {
                if (!TextUtils.isEmpty(coupon.getBestSelling())) {
                    openBrowser(coupon.getBestSelling());
                } else if (coupon.getCouponsCount() != 0) {
                    startActivity(new Intent(getBaseContext(), TrendCouponsActivity.class).putExtra("idTrend", coupon.getId()));
                } else {
                    // open products activity
                    Intent intent = new Intent(getBaseContext(), ProductsActivity.class);
                    // id title in items
                    intent.putExtra("idTitle", coupon.getId());
                    intent.putExtra("title", coupon.getTitle());
                    // check when click on item title to get products for store or company
                    switch (requestType) {
                        // store products
                        case ALL_COUPONS_STORE:
                            // this will send type and id store to products activity and get the products for store
                            intent.putExtra("type", "category");
                            intent.putExtra("idCategory", storeId);
                            break;
                        // company products
                        case COUPONS_COMPANY:
                            // this will send type and id company to products activity and get the products for company
                            intent.putExtra("type", "company");
                            intent.putExtra("idCompany", idCompany);
                            break;
                    }
                    startActivity(intent);
                }
            }

            @Override
            public void bestSelling(int position, Coupon coupon) {
                openBrowser(coupon.getBestSelling());
            }

            @Override
            public void shareCoupon(int position, Coupon coupon) {
                shareText("Title : " + coupon.getCompanyName() + "\n Description : " + coupon.getDesc());
            }

            @Override
            public void addToFavoriteCoupon(int position, Coupon coupon) {
                mainViewModel.addOrRemoveCouponFavorite(coupon.getId());
            }

        });
        linearLayoutManager = new LinearLayoutManager(this);
        binding.rvCoupons.setLayoutManager(linearLayoutManager);
        binding.rvCoupons.setAdapter(couponsAdapter);
        binding.rvCoupons.setHasFixedSize(true);
        // get all coupons for store
        fetchAllCouponsStore();

        mainViewModel.companies.observe(this, companiesAdapter::addAll);
        mainViewModel.coupons.observe(this, couponsAdapter::addAll);
        // display success msg
        mainViewModel.toastMessageSuccess.observe(this, msg -> {
            showDefaultDialog(binding.lyContainer, msg);
        });
        // display error msg
        mainViewModel.toastMessageFailed.observe(this, msg -> {
            showAlertDialog(binding.lyContainer, msg);
        });
        mainViewModel.isLastPage.observe(this, lastPage -> {
            this.mIsLastPage = lastPage;
        });
        mainViewModel.dataLoadingCoupons.observe(this, loading -> {
            this.mIsLoading = loading;
        });
        binding.rvCoupons.addOnScrollListener(new PaginationListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                ++mCurrentPage;
                switch (requestType) {
                    case ALL_COUPONS_STORE:
                        fetchAllCouponsStore();
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

        binding.imgBackArrow.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                startActivity(new Intent(getBaseContext(), SearchActivity.class).putExtra("word", query).putExtra("type", "search"));
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void fetchAllCouponsStore() {
        // get all coupons for store
        mainViewModel.getAllCouponsStore(storeId, mCurrentPage);
    }

    private void fetchCouponsCompany() {
        // get all coupons to company
        mainViewModel.getCouponsCompany(idCompany, mCurrentPage);
    }
}