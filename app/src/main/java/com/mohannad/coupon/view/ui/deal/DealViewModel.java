package com.mohannad.coupon.view.ui.deal;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DealViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DealViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is deal fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}