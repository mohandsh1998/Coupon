package com.mohannad.coupon.view.ui.auth.login;

import android.app.Application;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.mohannad.coupon.R;
import com.mohannad.coupon.callback.ResponseServer;
import com.mohannad.coupon.data.local.StorageSharedPreferences;
import com.mohannad.coupon.data.model.AuthResponse;
import com.mohannad.coupon.data.model.MessageResponse;
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
    public MutableLiveData<Boolean> showErrorEmailResetPassword = new MutableLiveData<>();
    public MutableLiveData<Boolean> showErrorPassword = new MutableLiveData<>();
    public MutableLiveData<Boolean> showErrorEmail = new MutableLiveData<>();
    public MutableLiveData<Boolean> successResetPassword = new MutableLiveData<>();
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

    public void onClickResetPassword() {
        if (validationResetPassword()) {
            dataLoading.setValue(true);
            resetPassword();
        }
    }

    private void resetPassword() {
        // request to resetPassword from repository
        loginRepository.resetPassword(mSharedPreferences.getLanguage(), email, new ResponseServer<MessageResponse>() {
            @Override
            public void onSuccess(boolean status, int code, MessageResponse response) {
                // hide loading
                dataLoading.setValue(false);
                // if success when connection
                if (status) {
                    // success to send email reset password
                    if (response.isStatus()) {
                        // success to send email reset password
                        toastMessageSuccess.setValue(response.getMessage());
                        successResetPassword.setValue(true);
                    } else {
                        // failed to send email reset password
                        successResetPassword.setValue(false);
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

    public void login() {
        // request to login from repository
        loginRepository.login(mSharedPreferences.getLanguage(), email, password,
                mSharedPreferences.getTokenFCM(), Constants.DEVICE_OS, mSharedPreferences.getStatusNotification(), new ResponseServer<AuthResponse>() {
                    @Override
                    public void onSuccess(boolean status, int code, AuthResponse response) {
                        // hide loading
                        dataLoading.setValue(false);
                        // if success when connection
                        if (status) {
                            // success to login
                            if (response.isStatus()) {
                                // success to login
                                mSharedPreferences.saveLogInSate(true);
                                mSharedPreferences.saveEmail(response.getUser().getEmail());
                                mSharedPreferences.saveUserName(response.getUser().getName());
                                mSharedPreferences.saveAuthToken("Bearer " + response.getUser().getToken());
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
            showErrorEmail.setValue(true);
            valid = false;
        } else if (!Utils.isEmailValid(email)) {
            errorEmail.setValue(getApplication().getString(R.string.email_invalid));
            showErrorEmail.setValue(true);
            valid = false;
        } else {
            showErrorEmail.setValue(false);
            errorEmail.setValue(null);
        }
        if (TextUtils.isEmpty(password)) {
            errorPassword.setValue(getApplication().getString(R.string.this_filed_is_empty));
            showErrorPassword.setValue(true);
            valid = false;
        } else if (password.length() < 6) {
            errorPassword.setValue(getApplication().getString(R.string.password_must_be_more_than_6));
            showErrorPassword.setValue(true);
            valid = false;
        } else {
            showErrorPassword.setValue(false);
            errorPassword.setValue(null);
        }
        return valid;
    }

    private boolean validationResetPassword() {
        boolean valid = true;
        if (TextUtils.isEmpty(email)) {
            errorEmail.setValue(getApplication().getString(R.string.this_filed_is_empty));
            showErrorEmailResetPassword.setValue(true);
            valid = false;
        } else if (!Utils.isEmailValid(email)) {
            errorEmail.setValue(getApplication().getString(R.string.email_invalid));
            valid = false;
            showErrorEmailResetPassword.setValue(true);
        } else {
            errorEmail.setValue(null);
            showErrorEmailResetPassword.setValue(false);
        }
        return valid;
    }

}
