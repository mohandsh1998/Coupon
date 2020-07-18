package com.mohannad.coupon.view.ui.auth;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.mohannad.coupon.R;
import com.mohannad.coupon.callback.ResponseServer;
import com.mohannad.coupon.data.local.StorageSharedPreferences;
import com.mohannad.coupon.data.model.AuthResponse;
import com.mohannad.coupon.repository.LoginRepository;
import com.mohannad.coupon.repository.SignUpRepository;
import com.mohannad.coupon.utils.BaseViewModel;
import com.mohannad.coupon.utils.Constants;
import com.mohannad.coupon.utils.Utils;

public class LoginViewModel extends BaseViewModel {
    public String email;
    public String password;
    // error messages that will show in login layout
    public MutableLiveData<String> errorEmail = new MutableLiveData<>();
    public MutableLiveData<String> errorPassword = new MutableLiveData<>();
    private LoginRepository loginRepository;
    private StorageSharedPreferences mSharedPreferences;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        loginRepository = LoginRepository.newInstance();
        mSharedPreferences = new StorageSharedPreferences(application);
    }

    public void onClick() {
        if (validation()) {
            dataLoading.setValue(true);
            login();
        }
    }

    public void login() {
        // request to sign up from repository
        loginRepository.login(getApplication().getString(R.string.lang), email, password,
                "gg", Constants.DEVICE_OS, 1, new ResponseServer<AuthResponse>() {
                    @Override
                    public void onSuccess(boolean status, int code, AuthResponse response) {
                        // hide loading
                        dataLoading.setValue(false);
                        // if success when connection
                        if (status) {
                            // success to login
                            if (response.isStatus()) {
                                // success to sign up
                                mSharedPreferences.saveLogInSate(true);
                                mSharedPreferences.saveEmail(response.getUser().getEmail());
                                mSharedPreferences.saveUserName(response.getUser().getName());
                                mSharedPreferences.saveAuthToken(response.getUser().getToken());
                                success.setValue(true);
                            } else {
                                // failed to login
                                success.setValue(false);
                                toastMessageFailed.setValue(response.getMsg());
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
        if (TextUtils.isEmpty(email)) {
            errorEmail.setValue(getApplication().getString(R.string.this_filed_is_empty));
            valid = false;
        } else if (!Utils.isEmailValid(email)) {
            errorEmail.setValue(getApplication().getString(R.string.email_invalid));
            valid = false;
        } else {
            errorEmail.setValue(null);
        }
        if (TextUtils.isEmpty(password)) {
            errorPassword.setValue(getApplication().getString(R.string.this_filed_is_empty));
            valid = false;
        } else if (password.length() < 6) {
            errorPassword.setValue(getApplication().getString(R.string.password_must_be_more_than_6));
            valid = false;
        } else {
            errorPassword.setValue(null);
        }
        return valid;
    }

}
