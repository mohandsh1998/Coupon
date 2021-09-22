package com.mohannad.coupon.view.adapter.spinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.mohannad.coupon.R;
import com.mohannad.coupon.data.model.CountryResponse;

import java.util.List;

public class SpinnerImageWithTextAdapter extends ArrayAdapter<CountryResponse.Country> {

    private Context mContext;
    private List<CountryResponse.Country> items;

    public SpinnerImageWithTextAdapter(Context context, List<CountryResponse.Country> items) {
        super(context, 0, items);
        this.mContext = context;
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flags_with_name_spinner_large_size, parent, false);
        TextView nameCountry = view.findViewById(R.id.name_country);
        ImageView imgFlag = view.findViewById(R.id.img_flag);
        nameCountry.setText(items.get(position).getName());
        // loading image flag
        Glide.with(mContext)
                .load(items.get(position).getFlag())
                .into(imgFlag);
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flags_with_name_spinner_large_size, parent, false);
        TextView nameCountry = view.findViewById(R.id.name_country);
        nameCountry.setTextColor(mContext.getResources().getColor(R.color.black1));
        ImageView imgFlag = view.findViewById(R.id.img_flag);
        nameCountry.setText(items.get(position).getName());
        // loading image flag
        Glide.with(mContext)
                .load(items.get(position).getFlag())
                .into(imgFlag);
        return view;
    }

    public void addAll(List<CountryResponse.Country> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return super.getCount();
    }
}
