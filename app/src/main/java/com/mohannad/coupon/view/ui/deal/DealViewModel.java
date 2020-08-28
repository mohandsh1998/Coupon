package com.mohannad.coupon.view.ui.deal;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mohannad.coupon.R;
import com.mohannad.coupon.callback.ResponseServer;
import com.mohannad.coupon.data.local.StorageSharedPreferences;
import com.mohannad.coupon.data.model.DealResponse;
import com.mohannad.coupon.data.model.HelpResponse;
import com.mohannad.coupon.repository.DealRepository;
import com.mohannad.coupon.repository.HelpRepository;
import com.mohannad.coupon.utils.BaseViewModel;

import java.util.List;

public class DealViewModel extends BaseViewModel {
    DealRepository dealRepository;
    MutableLiveData<List<DealResponse.DealItem>> deals = new MutableLiveData<>();
    MutableLiveData<List<DealResponse.DealsAds>> adsDeal = new MutableLiveData<>();
    StorageSharedPreferences sharedPreferences;
    public DealViewModel(@NonNull Application application) {
        super(application);
        dealRepository = DealRepository.newInstance();
        sharedPreferences = new StorageSharedPreferences(getApplication());
        getDeals();
    }

    private void getDeals() {
        dataLoading.setValue(true);
        dealRepository.getDeals(sharedPreferences.getLanguage(), 1, 1, new ResponseServer<LiveData<DealResponse>>() {
            @Override
            public void onSuccess(boolean status, int code, LiveData<DealResponse> response) {
                dataLoading.setValue(false);
                if (response != null && response.getValue() != null) {
                    if (response.getValue().isStatus()) {
                        deals.setValue(response.getValue().getDeal().getDealItems());
                        adsDeal.setValue(response.getValue().getDealsAds());
                    }
                }
            }

            @Override
            public void onFailure(String message) {
                dataLoading.setValue(false);
                // show error msg
                toastMessageFailed.setValue(getApplication().getString(R.string.problem_when_try_to_connect));
            }
        });
    }
}
