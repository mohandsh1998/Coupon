package com.mohannad.coupon.view.ui.auth.changepassword;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.mohannad.coupon.R;
import com.mohannad.coupon.callback.ResponseServer;
import com.mohannad.coupon.data.local.StorageSharedPreferences;
import com.mohannad.coupon.data.model.AuthResponse;
import com.mohannad.coupon.data.model.MessageResponse;
import com.mohannad.coupon.repository.ChangePasswordRepository;
import com.mohannad.coupon.repository.LoginRepository;
import com.mohannad.coupon.utils.BaseViewModel;
import com.mohannad.coupon.utils.Constants;
import com.mohannad.coupon.utils.Utils;

import java.util.Objects;

public class ChangePasswordViewModel extends BaseViewModel {
    // text will write in edittext
    public MutableLiveData<String> oldPassword = new MutableLiveData<>();
    public MutableLiveData<String> newPassword = new MutableLiveData<>();
    public MutableLiveData<String> confirmNewPassword = new MutableLiveData<>();

    // error messages that will show in change password layout
    public MutableLiveData<String> errorOldPassword = new MutableLiveData<>();
    public MutableLiveData<String> errorConfirmNewPassword = new MutableLiveData<>();
    // visibility error
    public MutableLiveData<Boolean> errorOldPasswordVisibility = new MutableLiveData<>();
    public MutableLiveData<Boolean> errorNewPasswordVisibility = new MutableLiveData<>();
    public MutableLiveData<Boolean> errorNewPasswordTextVisibility = new MutableLiveData<>();
    public MutableLiveData<Boolean> errorConfirmNewPasswordVisibility = new MutableLiveData<>();

    private ChangePasswordRepository changePasswordRepository;
    private StorageSharedPreferences mSharedPreferences;

    public ChangePasswordViewModel(@NonNull Application application) {
        super(application);
        changePasswordRepository = ChangePasswordRepository.newInstance();
        mSharedPreferences = new StorageSharedPreferences(application);
    }

    public void onClick() {
        if (validation()) {
            dataLoading.setValue(true);
            changePassword();
        }
    }

    public void changePassword() {
        // request to changePassword from repository
        changePasswordRepository.changePassword(getApplication().getString(R.string.lang), mSharedPreferences.getAuthToken(), oldPassword.getValue(),
                newPassword.getValue(), confirmNewPassword.getValue(), new ResponseServer<MessageResponse>() {
                    @Override
                    public void onSuccess(boolean status, int code, MessageResponse response) {
                        // hide loading
                        dataLoading.setValue(false);
                        // if success when connection
                        if (status) {
                            // success to login
                            if (response.isStatus()) {
                                oldPassword.setValue(null);
                                newPassword.setValue(null);
                                confirmNewPassword.setValue(null);
                                // success in change password
                                success.setValue(true);
                                toastMessageSuccess.setValue(response.getMessage());
                            } else {
                                // failed in change password
                                success.setValue(false);
                                errorOldPassword.setValue(response.getMessage());
                                errorOldPasswordVisibility.setValue(true);
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
        if (TextUtils.isEmpty(oldPassword.getValue())) {
            errorOldPassword.setValue(getApplication().getString(R.string.this_filed_is_empty));
            errorOldPasswordVisibility.setValue(true);
            valid = false;
        } else {
            errorOldPassword.setValue(null);
            errorOldPasswordVisibility.setValue(false);
        }
        if (TextUtils.isEmpty(newPassword.getValue())) {
            errorNewPasswordVisibility.setValue(true);
            errorNewPasswordTextVisibility.setValue(true);
            valid = false;
        } else {
            errorNewPasswordTextVisibility.setValue(false);
            errorNewPasswordVisibility.setValue(false);
        }
        if (TextUtils.isEmpty(confirmNewPassword.getValue())) {
            errorConfirmNewPassword.setValue(getApplication().getString(R.string.this_filed_is_empty));
            errorConfirmNewPasswordVisibility.setValue(true);
            valid = false;
        } else {
            errorConfirmNewPassword.setValue(null);
            errorConfirmNewPasswordVisibility.setValue(false);
        }
        if (!TextUtils.isEmpty(oldPassword.getValue()) && !TextUtils.isEmpty(newPassword.getValue()) && !TextUtils.isEmpty(confirmNewPassword.getValue())) {
            if (!Objects.equals(newPassword.getValue(), confirmNewPassword.getValue())) {
                errorConfirmNewPassword.setValue(getApplication().getString(R.string.password_not_match));
                errorConfirmNewPasswordVisibility.setValue(true);
                errorNewPasswordVisibility.setValue(true);
                valid = false;
            } else if (Objects.requireNonNull(newPassword.getValue()).length() < 6) {
                errorConfirmNewPassword.setValue(getApplication().getString(R.string.password_must_be_more_than_6));
                errorConfirmNewPasswordVisibility.setValue(true);
                errorNewPasswordVisibility.setValue(true);
                valid = false;
            } else {
                errorConfirmNewPassword.setValue(null);
                errorConfirmNewPasswordVisibility.setValue(false);
                errorNewPasswordVisibility.setValue(false);
            }
        }
        return valid;
    }

}
