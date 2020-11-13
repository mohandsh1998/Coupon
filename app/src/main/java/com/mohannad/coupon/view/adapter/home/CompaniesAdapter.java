package com.mohannad.coupon.view.adapter.home;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.mohannad.coupon.R;
import com.mohannad.coupon.data.model.CompaniesResponse;
import com.mohannad.coupon.data.model.DealResponse;
import com.mohannad.coupon.databinding.ItemCompanyRvBinding;
import com.mohannad.coupon.databinding.ItemDealRvBinding;

import java.util.ArrayList;
import java.util.List;

public class CompaniesAdapter extends RecyclerView.Adapter<CompaniesAdapter.CompanyViewHolder> {
    private List<CompaniesResponse.Company> companies;
    Context mContext;
    private CompanyClickListener companyClickListener;
    private int selectedItem;

    public CompaniesAdapter(Context context, ArrayList<CompaniesResponse.Company> companies,
                            CompanyClickListener companyClickListener) {
        this.companies = companies;
        this.mContext = context;
        this.companyClickListener = companyClickListener;
        selectedItem = -1;
    }

    @NonNull
    @Override
    public CompanyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCompanyRvBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_company_rv, parent, false);
        return new CompanyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyViewHolder holder, int position) {
        holder.bind(companies.get(position), position);
    }

    public void addAll(List<CompaniesResponse.Company> companies) {
        this.companies.addAll(companies);
        notifyDataSetChanged();
    }

    public void selected(int position) {
        selectedItem = position;
        notifyDataSetChanged();
    }

    public void clear() {
        this.companies.clear();
    }

    @Override
    public int getItemCount() {
        return companies.size();
    }

    class CompanyViewHolder extends RecyclerView.ViewHolder {
        private ItemCompanyRvBinding itemView;

        public CompanyViewHolder(ItemCompanyRvBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;
        }

        void bind(CompaniesResponse.Company company, int position) {
            // check if position equal selected item -> add border about selected company
            if (position == selectedItem) {
                itemView.imgCompanyItemCompanyRv.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_pink_light_radius_9dp));
                itemView.tvCompanyName.setTextColor(ContextCompat.getColor(mContext, R.color.pink));
            } else {
                itemView.imgCompanyItemCompanyRv.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_white_radius_9dp));
                itemView.tvCompanyName.setTextColor(ContextCompat.getColor(mContext, R.color.black));
            }

            this.itemView.tvCompanyName.setText(company.getName());
            // loading image company
            Glide.with(mContext)
                    .load(company.getImage())
                    //  .placeholder(R.drawable.loading_spinner)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(25)))
                    .into(this.itemView.imgCompanyItemCompanyRv);

            // click listener when select company
            itemView.getRoot().setOnClickListener(v -> {
                if (selectedItem != getAdapterPosition()) {
                    // change position selectedItem
                    selectedItem = getAdapterPosition();
                    // add border on selected company
                    itemView.imgCompanyItemCompanyRv.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_pink_light_radius_9dp));
                    itemView.imgCompanyItemCompanyRv.setElevation(24);
                    companyClickListener.onCompanySelected(position, company);
                }
            });
        }
    }

    public interface CompanyClickListener {
        void onCompanySelected(int position, CompaniesResponse.Company company);
    }
}
