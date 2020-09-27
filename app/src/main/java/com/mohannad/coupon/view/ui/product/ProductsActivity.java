package com.mohannad.coupon.view.ui.product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.mohannad.coupon.R;
import com.mohannad.coupon.data.local.StorageSharedPreferences;
import com.mohannad.coupon.data.model.ProductsResponse;
import com.mohannad.coupon.databinding.ActivityProductsBinding;
import com.mohannad.coupon.utils.BaseActivity;
import com.mohannad.coupon.view.adapter.product.ProductAdapter;
import com.mohannad.coupon.view.ui.auth.login.LoginActivity;
import com.mohannad.coupon.view.ui.auth.login.LoginViewModel;
import com.mohannad.coupon.view.ui.image.ImageActivity;
import com.mohannad.coupon.view.ui.video.VideoActivity;
import com.mohannad.coupon.view.ui.webview.WebViewActivity;

import java.util.ArrayList;

public class ProductsActivity extends BaseActivity {
    private String type;
    private int idTitle;
    private int idCategory;
    private int idCompany;
    ArrayList<ProductsResponse.Product> products = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.products);
        ActivityProductsBinding productsBinding = DataBindingUtil.setContentView(this, R.layout.activity_products);
        StorageSharedPreferences storageSharedPreferences = new StorageSharedPreferences(this);
        ProductsViewModel model = new ViewModelProvider(this).get(ProductsViewModel.class);
        productsBinding.setProductViewModel(model);
        productsBinding.setLifecycleOwner(this);
        // remove shadow in actionbar and change arrow color
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_back_arrow));
            getSupportActionBar().setElevation(0);
        }
        // get type -> category or company
        if (getIntent().hasExtra("type"))
            type = getIntent().getStringExtra("type");
        // title id
        if (getIntent().hasExtra("idTitle"))
            idTitle = getIntent().getIntExtra("idTitle", -1);
        // category id
        if (getIntent().hasExtra("idCategory"))
            idCategory = getIntent().getIntExtra("idCategory", -1);
        // company id
        if (getIntent().hasExtra("idCompany"))
            idCompany = getIntent().getIntExtra("idCompany", -1);
        // check if type for company or category
        if (type.equals("company")) {
            // get products for company
            model.getProductsCompany(idTitle, idCompany);
        } else {
            // get products for category
            model.getProductsCategory(idTitle, idCategory);
        }
        // init adapter for products
        ProductAdapter productAdapter = new ProductAdapter(this, products, new ProductAdapter.ProductClickListener() {
            @Override
            public void shareProduct(int position, ProductsResponse.Product product) {

            }

            @Override
            public void addToFavoriteProduct(int position, ProductsResponse.Product product) {
                model.addOrRemoveProductFavorite(product.getId());
            }

            @Override
            public void copyCoupon(int position, ProductsResponse.Product product) {
                // copy code coupon
                copyText(product.getCouponCode());
                // show dialog
                showDefaultDialog(productsBinding.lyContainer, getString(R.string.coupon_was_copied));
            }

            @Override
            public void openImage(ProductsResponse.Product product) {
                startActivity(new Intent(ProductsActivity.this, ImageActivity.class).putExtra("imageUrl", product.getImage()));
            }

            @Override
            public void openVideo(ProductsResponse.Product product) {
                startActivity(new Intent(ProductsActivity.this, VideoActivity.class).putExtra("url", product.getFilePath()));
            }

            @Override
            public void onClickProductItem(ProductsResponse.Product product) {
                startActivity(new Intent(ProductsActivity.this, WebViewActivity.class).putExtra("url", product.getLink()));
            }

        });
        productsBinding.rvProductsActivityProducts.setAdapter(productAdapter);
        productsBinding.rvProductsActivityProducts.setHasFixedSize(true);
        model.products.observe(this, productAdapter::addAll);
        model.toastMessageSuccess.observe(this, msg -> {
            showDefaultDialog(productsBinding.lyContainer, msg);
        });
        model.toastMessageFailed.observe(this, msg -> {
            showAlertDialog(productsBinding.lyContainer, msg);
        });
    }
}