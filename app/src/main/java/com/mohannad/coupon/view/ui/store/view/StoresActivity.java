package com.mohannad.coupon.view.ui.store.view;

import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.mohannad.coupon.R;
import com.mohannad.coupon.databinding.ActivityStoresBinding;
import com.mohannad.coupon.utils.BaseActivity;
import com.mohannad.coupon.utils.Constants;
import com.mohannad.coupon.view.adapter.store.StoreAdapter;
import com.mohannad.coupon.view.ui.main.view.MainActivity;
import com.mohannad.coupon.view.ui.more.MoreActivity;
import com.mohannad.coupon.view.ui.search.SearchActivity;
import com.mohannad.coupon.view.ui.store.viewmodel.StoresViewModel;

import java.util.ArrayList;
import java.util.List;

public class StoresActivity extends BaseActivity {
    public static final String STORES_KEY = "stores";
    public static final String POSITION_KEY = "position";
    public static final String STORE_ID_KEY = "store_id";
    private ActivityStoresBinding binding;
    private StoresViewModel mViewModel;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_stores);
        mViewModel = new ViewModelProvider(this).get(StoresViewModel.class);
        binding.setStoresViewModel(mViewModel);
        binding.setLifecycleOwner(this);

        StoreAdapter storeAdapter = new StoreAdapter(this, new ArrayList<>(),
                sharedPreferences.getThemeMode(),
                (position, storeId) -> startActivity(
                        new Intent(StoresActivity.this, MainActivity.class)
                                .putParcelableArrayListExtra(STORES_KEY, mViewModel.stores.getValue())
                                .putExtra(POSITION_KEY, position)
                                .putExtra(STORE_ID_KEY, storeId)));

        if (sharedPreferences.getThemeMode() == Constants.MODERN_THEME)
            layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        else layoutManager = new GridLayoutManager(this, 2);

        binding.rvStores.setLayoutManager(layoutManager);
        binding.rvStores.setHasFixedSize(true);
        binding.rvStores.setAdapter(storeAdapter);

        mViewModel.stores.observe(this, storeAdapter::addAll);
        mViewModel.toastMessageFailed.observe(this, msg -> {
            showAlertDialog(binding.lyContainer, msg);
        });

        binding.imgMenu.setOnClickListener(v -> {
            startActivity(new Intent(getBaseContext(), MoreActivity.class));
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
}