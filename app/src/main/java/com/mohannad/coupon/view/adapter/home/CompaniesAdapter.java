package com.mohannad.coupon.view.adapter.home;

import android.content.Context;
import android.view.LayoutInflater;
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
import com.mohannad.coupon.databinding.ItemCompanyRvBinding;
import com.mohannad.coupon.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class CompaniesAdapter extends RecyclerView.Adapter<CompaniesAdapter.CompanyViewHolder> {
    private List<CompaniesResponse.Company> companies;
    Context mContext;
    private CompanyClickListener companyClickListener;
    private int selectedItem;
    private int theme;

    public CompaniesAdapter(Context context, ArrayList<CompaniesResponse.Company> companies, int theme,
                            CompanyClickListener companyClickListener) {
        this.companies = companies;
        this.mContext = context;
        this.companyClickListener = companyClickListener;
        this.theme = theme;
        selectedItem = 0;
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
        this.companies.add(new CompaniesResponse.Company());
        this.companies.addAll(companies);
        notifyDataSetChanged();
    }

    public void clear() {
        selectedItem = 0;
        this.companies.clear();
        notifyDataSetChanged();
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
                itemView.imgCompanyItemCompanyRv.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_circle_solid_white_and_stroke_blue));
                if (theme == Constants.DARK_THEME)
                    itemView.tvCompanyName.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                else
                    itemView.tvCompanyName.setTextColor(ContextCompat.getColor(mContext, R.color.black));
            } else {
                itemView.imgCompanyItemCompanyRv.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_circle_solid_white_and_stroke_gray));
                itemView.tvCompanyName.setTextColor(ContextCompat.getColor(mContext, R.color.gray));
            }
            // companies from api
            if (position != 0) {
                this.itemView.tvCompanyName.setText(company.getName());
                // loading image company
                Glide.with(mContext)
                        .load(company.getImage())
                        //  .placeholder(R.drawable.loading_spinner)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(15)))
                        .into(this.itemView.imgCompanyItemCompanyRv);
            } else {
                // ALL companies
                this.itemView.tvCompanyName.setText(mContext.getString(R.string.all));
                // loading image company
                Glide.with(mContext)
                        .load(ContextCompat.getDrawable(mContext, R.drawable.ic_all_company))
                        //  .placeholder(R.drawable.loading_spinner)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(15)))
                        .into(this.itemView.imgCompanyItemCompanyRv);
            }

            // click listener when select company
            itemView.getRoot().setOnClickListener(v -> {
                if (selectedItem != getAdapterPosition()) {
                    // change position selectedItem
                    selectedItem = getAdapterPosition();
                    notifyDataSetChanged();
                    if (position != 0)
                        companyClickListener.onCompanySelected(position, company);
                    else companyClickListener.onClickAllCoupons();
                }
            });
        }
    }

    public interface CompanyClickListener {
        void onCompanySelected(int position, CompaniesResponse.Company company);

        void onClickAllCoupons();
    }
}
