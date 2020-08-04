package com.mohannad.coupon.view.adapter.help;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.mohannad.coupon.R;
import com.mohannad.coupon.data.model.HelpResponse;
import com.mohannad.coupon.databinding.ItemHelpRvBinding;

import java.util.ArrayList;
import java.util.List;

public class HelpAdapter extends RecyclerView.Adapter<HelpAdapter.HelpViewHolder> {
    private List<HelpResponse.Help> helpContents;

    public HelpAdapter(ArrayList<HelpResponse.Help> helpContents) {
        this.helpContents = helpContents;
    }

    @NonNull
    @Override
    public HelpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHelpRvBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_help_rv, parent, false);
        return new HelpViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HelpViewHolder holder, int position) {
        holder.bind(helpContents.get(position).getContent());
    }

    public void addAll(List<HelpResponse.Help> helpContents) {
        this.helpContents.clear();
        this.helpContents.addAll(helpContents);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return helpContents.size();
    }

    static class HelpViewHolder extends RecyclerView.ViewHolder {
        private ItemHelpRvBinding itemView;

        public HelpViewHolder(ItemHelpRvBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;
        }

        void bind(String text) {
            this.itemView.textHelpItem.setText(text);
        }
    }
}
