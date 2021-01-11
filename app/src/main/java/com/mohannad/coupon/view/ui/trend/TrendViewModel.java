package com.mohannad.coupon.view.ui.trend;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mohannad.coupon.R;
import com.mohannad.coupon.callback.ResponseServer;
import com.mohannad.coupon.data.local.StorageSharedPreferences;
import com.mohannad.coupon.data.model.Coupon;
import com.mohannad.coupon.data.model.HelpResponse;
import com.mohannad.coupon.data.model.TrendResponse;
import com.mohannad.coupon.repository.HelpRepository;
import com.mohannad.coupon.repository.TrendRepository;
import com.mohannad.coupon.utils.BaseViewModel;

import java.util.List;

public class TrendViewModel extends BaseViewModel {
    TrendRepository trendRepository;
    MutableLiveData<List<Coupon>> trendsLiveData = new MutableLiveData<>();
    StorageSharedPreferences sharedPreferences;
    public TrendViewModel(@NonNull Application application) {
        super(application);
        trendRepository = TrendRepository.newInstance();
        sharedPreferences = new StorageSharedPreferences(getApplication());
        getTrends();
    }

    private void getTrends() {
        dataLoading.setValue(true);
        trendRepository.getTrends(sharedPreferences.getLanguage(), sharedPreferences.getTokenFCM(), new ResponseServer<LiveData<TrendResponse>>() {
            @Override
            public void onSuccess(boolean status, int code, LiveData<TrendResponse> response) {
                dataLoading.setValue(false);
                if (response != null && response.getValue() != null) {
                    if (response.getValue().isStatus()) {
                        trendsLiveData.setValue(response.getValue().getTitles());
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
