package com.mohannad.coupon.view.adapter.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.mohannad.coupon.R;
import com.mohannad.coupon.data.model.CompaniesResponse;
import com.mohannad.coupon.databinding.ItemCompanyRvBinding;
import com.mohannad.coupon.databinding.ItemFilterRvBinding;

import java.util.ArrayList;
import java.util.List;

public class CompaniesFilterAdapter extends RecyclerView.Adapter<CompaniesFilterAdapter.CompanyViewHolder> {
    private List<CompaniesResponse.Company> companies;
    Context mContext;
    private CompanyClickListener companyClickListener;
    private int selectedItem;

    public CompaniesFilterAdapter(Context context, ArrayList<CompaniesResponse.Company> companies,
                                  CompanyClickListener companyClickListener) {
        this.companies = companies;
        this.mContext = context;
        this.companyClickListener = companyClickListener;
        selectedItem = -1;
    }

    @NonNull
    @Override
    public CompanyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFilterRvBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_filter_rv, parent, false);
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
        private ItemFilterRvBinding itemView;

        public CompanyViewHolder(ItemFilterRvBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;
        }

        void bind(CompaniesResponse.Company company, int position) {
            // check if position equal selected item -> change border color about selected company
            if (position == selectedItem) {
                itemView.getRoot().setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_stroke_pink_raduis_15dp));
            } else {
                itemView.getRoot().setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_stroke_black_15dp));
            }

            ((TextView) itemView.getRoot()).setText(company.getName());

            // click listener when select company
            itemView.getRoot().setOnClickListener(v -> {
                if (selectedItem != getAdapterPosition()) {
                    // change position selectedItem
                    selected(position);
                    // change border color to selected company
                    itemView.getRoot().setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_stroke_pink_raduis_15dp));
                    companyClickListener.onCompanySelected(position, company);
                }
            });
        }
    }

    public interface CompanyClickListener {
        void onCompanySelected(int position, CompaniesResponse.Company company);
    }
}
