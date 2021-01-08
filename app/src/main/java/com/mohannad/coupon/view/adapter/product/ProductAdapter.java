package com.mohannad.coupon.view.adapter.product;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.mohannad.coupon.R;
import com.mohannad.coupon.data.model.ProductsResponse;
import com.mohannad.coupon.databinding.ItemProductRvBinding;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<ProductsResponse.Product> products;
    private Context mContext;
    private Animation shake;
    private ProductClickListener productClickListener;
    private int copyItem = -1;
    // tag to start animation when copy coupon only
    private boolean startAnimation = false;

    public ProductAdapter(Context context, ArrayList<ProductsResponse.Product> products, ProductClickListener productClickListener) {
        mContext = context;
        this.products = products;
        // animation when copy coupon
        shake = AnimationUtils.loadAnimation(mContext, R.anim.shake);
        this.productClickListener = productClickListener;
        copyItem = -1;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductRvBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_product_rv, parent, false);
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.bind(position);
    }

    public void addAll(List<ProductsResponse.Product> products) {
        copyItem = -1;
        this.products.clear();
        this.products.addAll(products);
        notifyDataSetChanged();
    }

    public void setCopyItem(int position) {
        copyItem = position;
        notifyDataSetChanged();
    }

    public void changeStatusFavoriteProduct(ProductsResponse.Product product) {
        // check if the product in favorite or not to change img favorite
        if (product.isInFavorite()) {
            product.setInFavorite(false);
        } else product.setInFavorite(true);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private ItemProductRvBinding itemView;

        public ProductViewHolder(ItemProductRvBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;
            this.itemView.imgFavoriteProductItemProductRv.setVisibility(View.VISIBLE);
            this.itemView.tvNumReviewsItemProductRv.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
            this.itemView.tvNumOrderItemProductRv.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        }

        void bind(int position) {
            ProductsResponse.Product product = products.get(position);
            // check if file path "" then this product not have video
            if (TextUtils.isEmpty(product.getFilePath())) {
                // hide icon video
                this.itemView.imgVideoItemProductRv.setVisibility(View.GONE);
                this.itemView.imgCompanyItemProductRv.setOnClickListener(v -> {
                    productClickListener.openImage(product);
                });
            } else {
                // show icon video
                this.itemView.imgVideoItemProductRv.setVisibility(View.VISIBLE);
                this.itemView.imgCompanyItemProductRv.setOnClickListener(v -> {
                    productClickListener.openVideo(product);
                });
            }
            // loading image company
            Glide.with(mContext)
                    .load(product.getImage())
                    //  .placeholder(R.drawable.loading_spinner)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(40)))
                    .into(this.itemView.imgCompanyItemProductRv);
            // name product
            this.itemView.tvProductNameItemProductRv.setText(product.getName());
            // desc
            this.itemView.tvDescItemProductRv.setText(product.getDesc());

            if (product.getRate() != 0) {
                this.itemView.appCompatRatingBar.setVisibility(View.VISIBLE);
                this.itemView.tvReviewsItemProductRv.setVisibility(View.VISIBLE);
                this.itemView.appCompatRatingBar.setRating(product.getRate());
                this.itemView.tvReviewsItemProductRv.setText(Float.toString(product.getRate()));
            } else {
                this.itemView.appCompatRatingBar.setVisibility(View.GONE);
                this.itemView.tvReviewsItemProductRv.setVisibility(View.GONE);
            }

            if (product.getNumberReviews() != 0) {
                this.itemView.tvNumReviewsItemProductRv.setVisibility(View.VISIBLE);
                this.itemView.tvNumReviewsItemProductRv.setText(product.getNumberReviews() + " " + mContext.getString(R.string.reviews));
            } else {
                this.itemView.tvNumReviewsItemProductRv.setVisibility(View.GONE);
            }

            if (product.getNumberOrders() != 0) {
                this.itemView.tvNumOrderItemProductRv.setVisibility(View.VISIBLE);
                this.itemView.tvNumOrderItemProductRv.setText(product.getNumberOrders() + " " + mContext.getString(R.string.orders));
            } else {
                this.itemView.tvNumOrderItemProductRv.setVisibility(View.GONE);
            }

            // check if the product in favorite or not
            if (product.isInFavorite()) {
                itemView.imgFavoriteProductItemProductRv.setImageResource(R.drawable.ic_baseline_favorite_24);
            } else {
                itemView.imgFavoriteProductItemProductRv.setImageResource(R.drawable.ic_favorite);
            }

            // when the user click to copy coupon
            this.itemView.tvCopyCouponItemProductRv.setOnClickListener(v -> {
                setCopyItem(position);
                startAnimation = true;
                productClickListener.copyCoupon(position, product);
            });

            // when the user click to add product or remove to favorite
            itemView.imgFavoriteProductItemProductRv.setOnClickListener(v -> {
                // this to change img favorite
                changeStatusFavoriteProduct(product);
                productClickListener.addToFavoriteProduct(position, product);
            });
            // when the user click to share product
            itemView.imgShareItemProductRv.setOnClickListener(v -> {
                productClickListener.shareProduct(position, product);
            });
            itemView.getRoot().setOnClickListener(v -> productClickListener.onClickProductItem(product));
            // check if product coupon code empty hide "copy code" else Show "copy code"
            if (TextUtils.isEmpty(product.getCouponCode())) {
                this.itemView.tvCopyCouponItemProductRv.setVisibility(View.GONE);
            } else {
                this.itemView.tvCopyCouponItemProductRv.setVisibility(View.VISIBLE);
            }
            // check if position == coupon has been copied -> will show code coupon
            // if not -> hide code coupon and show copy coupon text
            if (position == copyItem) {
                this.itemView.tvCopyCouponItemProductRv.setTextColor(ContextCompat.getColor(mContext, R.color.pink));
                // change text to code coupon
                this.itemView.tvCopyCouponItemProductRv.setText(product.getCouponCode());
                this.itemView.tvCopyCouponItemProductRv.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_copy, 0, 0, 0);
                this.itemView.tvCopyCouponItemProductRv.setTextColor(mContext.getResources().getColor(R.color.gray8));
                // change background
                this.itemView.tvCopyCouponItemProductRv.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_stroke_gray_raduis_9dp));
                if (startAnimation) {
                    // start animation
                    this.itemView.tvCopyCouponItemProductRv.startAnimation(shake);
                    this.itemView.getRoot().startAnimation(shake);
                    startAnimation = false;
                }
            } else {
                this.itemView.tvCopyCouponItemProductRv.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
                this.itemView.tvCopyCouponItemProductRv.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                // change code coupon to text copy coupon
                this.itemView.tvCopyCouponItemProductRv.setText(mContext.getString(R.string.get_code));
                // change background
                this.itemView.tvCopyCouponItemProductRv.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_solid_pink_radius_9dp));
            }

        }
    }

    public interface ProductClickListener {
        void shareProduct(int position, ProductsResponse.Product product);

        void addToFavoriteProduct(int position, ProductsResponse.Product product);

        void copyCoupon(int position, ProductsResponse.Product product);

        void openImage(ProductsResponse.Product product);

        void openVideo(ProductsResponse.Product product);

        void onClickProductItem(ProductsResponse.Product product);
    }
}
