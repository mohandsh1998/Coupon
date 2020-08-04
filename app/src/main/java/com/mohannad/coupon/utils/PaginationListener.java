package com.mohannad.coupon.utils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class PaginationListener extends RecyclerView.OnScrollListener {

    public static final int PAGE_START = 1;

    @NonNull
    private LinearLayoutManager mLayoutManager;

    /**
     * Set scrolling threshold here (for now i'm assuming 30 item in one page)
     */
    private static final int PAGE_SIZE = 30;


    public PaginationListener(@NonNull LinearLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int visibleItemCount = mLayoutManager.getChildCount();
        int totalItemCount = mLayoutManager.getItemCount();
        int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();
        Log.d("Mo:firstVisibl : ", firstVisibleItemPosition + "");
        Log.d("Mo:visibleItemCount : ", visibleItemCount + "");
        Log.d("Mo:totalItemCount : ", totalItemCount + "");
        Log.d("Mo: isLoading", isLoading() + "");
        Log.d("Mo: isLastPage", isLastPage() + "");
        if (!isLoading() && !isLastPage()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= PAGE_SIZE) {
                loadMoreItems();
            }
        }
    }

    protected abstract void loadMoreItems();

    public abstract boolean isLastPage();

    public abstract boolean isLoading();
}