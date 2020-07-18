package com.mohannad.coupon.data.local;

import android.content.Context;
import android.content.SharedPreferences;

public class StorageSharedPreferences {

    private static String MY_DATA_KEY = "MyData";
    private static String LOGGED_IN_KEY = "logged_in";
    private static String TOKEN_KEY = "token";
    private static String TOKEN_FCM_KEY = "token_fcm";
    private static String USER_NAME_KEY = "user_name";
    private static String EMAIL_KEY = "email";
    private static String PASSWORD_KEY = "password";
    private static final boolean LOG_IN_STATE = false;
    private static final String AUTH_TOKEN = null;
    private static final String TOKEN_FCM = null;
    private static final String USER_NAME = null;
    private static final String EMAIL = null;
    private static final String PASSWORD = null;

    private Context mContext;


    public StorageSharedPreferences(Context context) {
        this.mContext = context;
    }

    private SharedPreferences.Editor getPreferencesEditor() {
        return getSharedPreferences().edit();
    }

    private SharedPreferences getSharedPreferences() {
        return mContext.getSharedPreferences(MY_DATA_KEY, Context.MODE_PRIVATE);
    }

    public void saveLogInSate(boolean state) {
        getPreferencesEditor().putBoolean(LOGGED_IN_KEY, state).commit();
    }

    public boolean getLogInState() {
        return getSharedPreferences().getBoolean(LOGGED_IN_KEY, LOG_IN_STATE);
    }

    public void saveAuthToken(String token) {
        getPreferencesEditor().putString(TOKEN_KEY, token).commit();
    }

    public String getAuthToken() {
        return getSharedPreferences().getString(TOKEN_KEY, AUTH_TOKEN);
    }

    public void saveUserName(String name) {
        getPreferencesEditor().putString(USER_NAME_KEY, name).commit();
    }

    public String getUserName() {
        return getSharedPreferences().getString(USER_NAME_KEY, USER_NAME);
    }

    public void saveEmail(String email) {
        getPreferencesEditor().putString(EMAIL_KEY, email).commit();
    }

    public String getEmail() {
        return getSharedPreferences().getString(EMAIL_KEY, EMAIL);
    }


    public void savePassword(String password) {
        getPreferencesEditor().putString(PASSWORD_KEY, password).commit();
    }

    public String getPassword() {
        return getSharedPreferences().getString(PASSWORD_KEY, PASSWORD);
    }

    public void saveTokenFCM(String token) {
        getPreferencesEditor().putString(TOKEN_FCM_KEY, token).commit();
    }

    public String getTokenFCM() {
        return getSharedPreferences().getString(TOKEN_FCM_KEY, TOKEN_FCM);
    }

    public void logout(){
        saveLogInSate(false);
        saveAuthToken(null);
        saveUserName(null);
        saveEmail(null);
        savePassword(null);
        saveTokenFCM(null);
    }

}
