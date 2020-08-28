package com.mohannad.coupon.view.ui.help;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mohannad.coupon.R;
import com.mohannad.coupon.callback.ResponseServer;
import com.mohannad.coupon.data.local.StorageSharedPreferences;
import com.mohannad.coupon.data.model.HelpResponse;
import com.mohannad.coupon.repository.HelpRepository;
import com.mohannad.coupon.utils.BaseViewModel;

import java.util.List;

public class HelpViewModel extends BaseViewModel {
    HelpRepository helpRepository;
    MutableLiveData<List<HelpResponse.Help>> helpContents = new MutableLiveData<>();
    StorageSharedPreferences sharedPreferences;
    public HelpViewModel(@NonNull Application application) {
        super(application);
        helpRepository = HelpRepository.newInstance();
        sharedPreferences = new StorageSharedPreferences(getApplication());
        getHelpContent();
    }

    private void getHelpContent() {
        dataLoading.setValue(true);
        helpRepository.getHelpContent(sharedPreferences.getLanguage(), new ResponseServer<LiveData<HelpResponse>>() {
            @Override
            public void onSuccess(boolean status, int code, LiveData<HelpResponse> response) {
                dataLoading.setValue(false);
                if (response != null && response.getValue() != null) {
                    if (response.getValue().isStatus()) {
                        helpContents.setValue(response.getValue().getHelps());
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
