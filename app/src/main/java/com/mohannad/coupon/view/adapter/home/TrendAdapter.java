package com.mohannad.coupon.view.adapter.home;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mohannad.coupon.R;
import com.mohannad.coupon.data.model.Coupon;
import com.mohannad.coupon.data.model.HelpResponse;
import com.mohannad.coupon.databinding.ItemTitleRvBinding;
import com.mohannad.coupon.utils.BaseViewHolder;
import com.mohannad.coupon.utils.Constants;

import java.util.List;

public class TrendAdapter extends RecyclerView.Adapter<TrendAdapter.TitleViewHolder> {
    private Context mContext;
    private List<Coupon> trendList;
    private TrendClickListener trendClickListener;
    private int theme;

    public TrendAdapter(Context mContext, List<Coupon> trendList, int theme, TrendClickListener trendClickListener) {
        this.mContext = mContext;
        this.trendList = trendList;
        this.theme = theme;
        this.trendClickListener = trendClickListener;
    }

    @NonNull
    @Override
    public TitleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TitleViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_title_rv, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TitleViewHolder holder, int position) {
        holder.onBind(position);
    }


    public void addAll(List<Coupon> coupons) {
        this.trendList.clear();
        this.trendList.addAll(coupons);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return trendList.size();
    }

    // view holder for title top products
    class TitleViewHolder extends BaseViewHolder {
        ItemTitleRvBinding itemTitleRvBinding;

        public TitleViewHolder(ItemTitleRvBinding itemTitleRvBinding) {
            super(itemTitleRvBinding);
            this.itemTitleRvBinding = itemTitleRvBinding;
            switch (theme) {
                case Constants.MODERN_THEME:
                    this.itemTitleRvBinding.lyTrend.setBackgroundResource(R.color.gray1);
                    break;
                case Constants.LIGHT_THEME:
                    this.itemTitleRvBinding.lyTrend.setBackgroundResource(R.color.white);
                    break;
                case Constants.DARK_THEME:
                    this.itemTitleRvBinding.lyTrend.setBackgroundResource(R.color.black2);
                    break;
            }
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            Coupon trend = trendList.get(position);
            itemTitleRvBinding.tvTitleTopProduct.setText(trend.getTitle());

            if (!TextUtils.isEmpty(trend.getImage()))
                // load img ads
                Glide.with(mContext)
                        .load(trend.getImage())
                        //  .placeholder(R.drawable.loading_spinner)
                        .into(this.itemTitleRvBinding.imgTitle);

            this.itemTitleRvBinding.getRoot().setOnClickListener(v -> trendClickListener.openProductActivity(position, trend));
        }

        @Override
        public int getCurrentPosition() {
            return super.getCurrentPosition();
        }
    }

    public interface TrendClickListener {
        void openProductActivity(int position, Coupon trend);
    }
}
