package com.mohannad.coupon.view.ui.contactus;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.mohannad.coupon.R;
import com.mohannad.coupon.callback.ResponseServer;
import com.mohannad.coupon.data.local.StorageSharedPreferences;
import com.mohannad.coupon.data.model.MessageResponse;
import com.mohannad.coupon.repository.ChangePasswordRepository;
import com.mohannad.coupon.repository.ContactUsRepository;
import com.mohannad.coupon.utils.BaseViewModel;
import com.mohannad.coupon.utils.Utils;

import java.util.Objects;

public class ContactUsViewModel extends BaseViewModel {
    // text will write in edit text
    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> title = new MutableLiveData<>();
    public MutableLiveData<String> content = new MutableLiveData<>();

    // error messages that will show in contact us layout
    public MutableLiveData<String> errorEmail = new MutableLiveData<>();
    // visibility error
    public MutableLiveData<Boolean> errorEmailVisibility = new MutableLiveData<>();
    public MutableLiveData<Boolean> errorTitleVisibility = new MutableLiveData<>();
    public MutableLiveData<Boolean> errorContentVisibility = new MutableLiveData<>();

    private ContactUsRepository contactUsRepository;
    private StorageSharedPreferences sharedPreferences;
    public ContactUsViewModel(@NonNull Application application) {
        super(application);
        contactUsRepository = ContactUsRepository.newInstance();
        sharedPreferences = new StorageSharedPreferences(getApplication());
    }

    public void onClick() {
        if (validation()) {
            dataLoading.setValue(true);
            contactUs();
        }
    }

    public void contactUs() {
        // request to contactUs from repository
        contactUsRepository.contactUs(sharedPreferences.getLanguage(), title.getValue(), email.getValue(), content.getValue(), new ResponseServer<MessageResponse>() {
            @Override
            public void onSuccess(boolean status, int code, MessageResponse response) {
                // hide loading
                dataLoading.setValue(false);
                // if success when connection
                if (status) {
                    // success to login
                    if (response.isStatus()) {
                        title.setValue(null);
                        email.setValue(null);
                        content.setValue(null);
                        // success in send msg
                        success.setValue(true);
                        toastMessageSuccess.setValue(response.getMessage());
                    } else {
                        // failed in send msg
                        success.setValue(false);
                        toastMessageFailed.setValue(response.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(String message) {
                // hide loading
                dataLoading.setValue(false);
                success.setValue(false);
                // show error msg
                toastMessageFailed.setValue(getApplication().getString(R.string.problem_when_try_to_connect));
            }
        });

    }

    private boolean validation() {
        boolean valid = true;
        if (TextUtils.isEmpty(email.getValue())) {
            errorEmail.setValue(getApplication().getString(R.string.this_filed_is_empty));
            errorEmailVisibility.setValue(true);
            valid = false;
        } else if (!Utils.isEmailValid(email.getValue())) {
            errorEmail.setValue(getApplication().getString(R.string.email_invalid));
            errorEmailVisibility.setValue(true);
            valid = false;
        } else {
            errorEmail.setValue(null);
            errorEmailVisibility.setValue(false);
        }
        if (TextUtils.isEmpty(title.getValue())) {
            errorTitleVisibility.setValue(true);
            valid = false;
        } else {
            errorTitleVisibility.setValue(false);
        }
        if (TextUtils.isEmpty(content.getValue())) {
            errorContentVisibility.setValue(true);
            valid = false;
        } else {
            errorContentVisibility.setValue(false);
        }
        return valid;
    }

}
