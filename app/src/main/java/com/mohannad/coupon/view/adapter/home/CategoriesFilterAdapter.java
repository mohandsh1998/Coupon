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
import com.mohannad.coupon.data.model.CategoriesResponse;
import com.mohannad.coupon.data.model.CompaniesResponse;
import com.mohannad.coupon.databinding.ItemFilterRvBinding;

import java.util.ArrayList;
import java.util.List;

public class CategoriesFilterAdapter extends RecyclerView.Adapter<CategoriesFilterAdapter.CategoryViewHolder> {
    private List<CategoriesResponse.Category> categories;
    Context mContext;
    private CategoryClickListener categoryClickListener;
    private int selectedItem;

    public CategoriesFilterAdapter(Context context, ArrayList<CategoriesResponse.Category> categories,
                                   CategoryClickListener categoryClickListener) {
        this.categories = categories;
        this.mContext = context;
        this.categoryClickListener = categoryClickListener;
        selectedItem = -1;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFilterRvBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_filter_rv, parent, false);
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.bind(categories.get(position), position);
    }

    public void addAll(List<CategoriesResponse.Category> categories) {
        this.categories.addAll(categories);
        notifyDataSetChanged();
    }

    public void selected(int position) {
        selectedItem = position;
        notifyDataSetChanged();
    }

    public void clear() {
        this.categories.clear();
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private ItemFilterRvBinding itemView;

        public CategoryViewHolder(ItemFilterRvBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;
        }

        void bind(CategoriesResponse.Category category, int position) {
            // check if position equal selected item -> change bg about selected category
            if (position == selectedItem) {
                itemView.getRoot().setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_solid_black_10dp));
                ((TextView) itemView.getRoot()).setTextColor(ContextCompat.getColor(mContext, R.color.white));
            } else {
                itemView.getRoot().setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_solid_gray_10dp));
                ((TextView) itemView.getRoot()).setTextColor(ContextCompat.getColor(mContext, R.color.black));
            }

            ((TextView) itemView.getRoot()).setText(category.getName());

            // click listener when select category
            itemView.getRoot().setOnClickListener(v -> {
                if (selectedItem != getAdapterPosition()) {
                    // change position selectedItem
                    selected(position);
                    // change bg  to selected category and shadow
                    itemView.getRoot().setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_solid_black_10dp));
                    ((TextView) itemView.getRoot()).setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    categoryClickListener.onCategorySelected(position, category);
                }
            });
        }
    }

    public interface CategoryClickListener {
        void onCategorySelected(int position, CategoriesResponse.Category category);
    }
}
