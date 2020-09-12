package com.mohannad.coupon.view.adapter.spinner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.mohannad.coupon.R;
import com.mohannad.coupon.data.model.FavoriteResponse;
import com.mohannad.coupon.databinding.ItemSpinnerBottomSheetRvBinding;

import java.util.ArrayList;
import java.util.List;

public class SpinnerBottomSheetAdapter<T> extends RecyclerView.Adapter<SpinnerBottomSheetAdapter.ItemViewHolder> {
    public List<T> itemContents;
    private int selectedItem;
    private ItemClickListener clickListener;

    public SpinnerBottomSheetAdapter(ArrayList<T> itemContents, ItemClickListener clickListener) {
        this.itemContents = itemContents;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSpinnerBottomSheetRvBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_spinner_bottom_sheet_rv, parent, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.itemView.tvItemName.setText(itemContents.get(position).toString());
        holder.itemView.getRoot().setOnClickListener(v -> {
            setSelectedItem(position);
            clickListener.selectedItem(position, itemContents.get(position));
        });
        if (position == selectedItem) {
            holder.itemView.imgChecked.setVisibility(View.VISIBLE);
        } else {
            holder.itemView.imgChecked.setVisibility(View.INVISIBLE);
        }
    }

    public void addAll(List<T> itemContents) {
        this.itemContents.clear();
        this.itemContents.addAll(itemContents);
        notifyDataSetChanged();
    }

    public void setSelectedItem(int position) {
        selectedItem = position;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return itemContents.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private ItemSpinnerBottomSheetRvBinding itemView;

        public ItemViewHolder(ItemSpinnerBottomSheetRvBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;
        }
    }

    public interface ItemClickListener<T> {
        void selectedItem(int position, T item);
    }
}
