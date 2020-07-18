package com.mohannad.coupon.view.ui.auth;

import android.app.Application;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mohannad.coupon.R;
import com.mohannad.coupon.callback.ResponseServer;
import com.mohannad.coupon.data.local.StorageSharedPreferences;
import com.mohannad.coupon.data.model.AuthResponse;
import com.mohannad.coupon.repository.SignUpRepository;
import com.mohannad.coupon.utils.BaseViewModel;
import com.mohannad.coupon.utils.Constants;
import com.mohannad.coupon.utils.Utils;

public class SignUpViewModel extends BaseViewModel {
    public String fullName;
    public String email;
    public String password;
    // error messages that will show in sign up layout
    public MutableLiveData<Boolean> errorFullName = new MutableLiveData<>();
    public MutableLiveData<String> errorEmail = new MutableLiveData<>();
    public MutableLiveData<String> errorPassword = new MutableLiveData<>();
    private SignUpRepository signUpRepository;
    private StorageSharedPreferences mSharedPreferences;

    public SignUpViewModel(@NonNull Application application) {
        super(application);
        signUpRepository = SignUpRepository.newInstance();
        mSharedPreferences = new StorageSharedPreferences(application);
    }

    public void onClick() {
        if (validation()) {
            dataLoading.setValue(true);
            signUp();
        }
    }

    public void signUp() {
        // request to sign up from repository
        signUpRepository.signUp(getApplication().getString(R.string.lang), fullName, email, password,
                "gg", Constants.DEVICE_OS, 1, new ResponseServer<AuthResponse>() {
                    @Override
                    public void onSuccess(boolean status, int code, AuthResponse response) {
                        // hide loading
                        dataLoading.setValue(false);
                        // if success when connection
                        if (status) {
                            // success to sign up
                            if (response.isStatus()) {
                                // success to sign up
                                mSharedPreferences.saveLogInSate(true);
                                mSharedPreferences.saveEmail(response.getUser().getEmail());
                                mSharedPreferences.saveUserName(response.getUser().getName());
                                mSharedPreferences.saveAuthToken(response.getUser().getToken());
                                toastMessageSuccess.setValue(response.getMsg());
                                success.setValue(true);
                            } else {
                                // failed to sign up
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
        if (TextUtils.isEmpty(fullName)) {
            errorFullName.setValue(true);
            valid = false;
        } else {
            errorFullName.setValue(false);
        }
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
