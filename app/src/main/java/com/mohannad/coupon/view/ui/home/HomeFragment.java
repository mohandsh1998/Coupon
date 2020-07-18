package com.mohannad.coupon.view.ui.home;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.mohannad.coupon.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.home_menu, menu);
        // SearchView
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        // Hint SearchView
        searchView.setQueryHint(getString(R.string.what_would_you_like_to_find));
        // Background SearchView
        searchView.setBackground(mContext.getDrawable(R.drawable.shape_gray2_raduis_9dp));
        // close icon in SearchView
        ImageView searchViewCloseIcon = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        searchViewCloseIcon.setEnabled(false);
        // hide back arrow
        searchView.setIconifiedByDefault(true);
        // disable focusable
        searchView.setFocusable(false);
        // hide search icon in search view
        searchView.setIconified(false);
        searchView.clearFocus();
        searchView.requestFocusFromTouch();

        // EditText for SearchView
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_search_pink, 0, 0, 0);
        searchEditText.setCompoundDrawablePadding(16);
        searchEditText.setTextSize(14);
        searchEditText.setTextColor(Color.BLACK);
        searchEditText.setHintTextColor(mContext.getResources().getColor(R.color.gray0));
    }
}