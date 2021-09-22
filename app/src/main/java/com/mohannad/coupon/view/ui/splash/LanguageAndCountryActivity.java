package com.mohannad.coupon.view.ui.splash;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.mohannad.coupon.R;
import com.mohannad.coupon.data.model.CountryResponse;
import com.mohannad.coupon.databinding.ActivityLanguageAndCountryBinding;
import com.mohannad.coupon.utils.BaseActivity;
import com.mohannad.coupon.utils.Constants;
import com.mohannad.coupon.utils.LocaleHelper;
import com.mohannad.coupon.utils.ThemeManager;
import com.mohannad.coupon.view.adapter.spinner.SpinnerImageWithTextAdapter;
import com.mohannad.coupon.view.ui.main.view.MainActivity;
import com.mohannad.coupon.view.ui.store.view.StoresActivity;

import java.util.ArrayList;

public class LanguageAndCountryActivity extends BaseActivity {
    private final String LANGUAGE_KEY = "Language";
    private final String THEME_KEY = "Theme";
    private final String COUNTRY_ID_KEY = "country_id";
    private final String COUNTRY_NAME_KEY = "country_name";
    private final String FIRST_TIME_KEY = "first_time";
    private String language = LocaleHelper.ENGLISH_LANGUAGE;
    private int theme = Constants.LIGHT_THEME;
    private int idCountry = -1;
    private String nameCountry;
    private boolean firstTime = true;
    private SplashViewModel model;
    private ActivityLanguageAndCountryBinding languageAndCountryBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // will used when update theme
        if (savedInstanceState != null) {
            language = savedInstanceState.getString(LANGUAGE_KEY);
            idCountry = savedInstanceState.getInt(COUNTRY_ID_KEY);
            nameCountry = savedInstanceState.getString(COUNTRY_NAME_KEY);
            theme = savedInstanceState.getInt(THEME_KEY);
            firstTime = savedInstanceState.getBoolean(FIRST_TIME_KEY);
            ThemeManager.setCustomizedThemes(this, theme);
        }

        languageAndCountryBinding = DataBindingUtil.setContentView(this, R.layout.activity_language_and_country);
        model = new ViewModelProvider(this).get(SplashViewModel.class);
        languageAndCountryBinding.setSplashViewModel(model);
        languageAndCountryBinding.setLifecycleOwner(this);
        if (firstTime)
            // check if device language arabic select arabic button default
            if (sharedPreferences.getLanguage().equals(LocaleHelper.ARABIC_LANGUAGE)) {
                language = LocaleHelper.ARABIC_LANGUAGE;
            }
        // update layout to select language
        updateLanguage();
        // update layout to select theme
        updateTheme();
        // call countries when open activity
        if (firstTime) model.getCountries();

        // array countries
        ArrayList<CountryResponse.Country> countries = new ArrayList<>();
        // init countries adapter
        SpinnerImageWithTextAdapter adapterCountries = new SpinnerImageWithTextAdapter(this, countries);
        languageAndCountryBinding.spinnerCountries.setAdapter(adapterCountries);
        // layout dropdown menu in spinner
        adapterCountries.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        model.countries.observe(this, adapterCountries::addAll);

        model.successTokenDevice.observe(this, success -> {
            if (success) {
                sharedPreferences.setFirstTimeLaunch(false);
                startActivity(new Intent(LanguageAndCountryActivity.this, StoresActivity.class));
                finish();
            }
        });

        languageAndCountryBinding.spinnerCountries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // item selected by user from spinner
                CountryResponse.Country country = (CountryResponse.Country) parent.getItemAtPosition(position);
                idCountry = country.getId();
                nameCountry = country.getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        languageAndCountryBinding.tvArabicLang.setOnClickListener(v -> {
            language = LocaleHelper.ARABIC_LANGUAGE;
            updateLanguage();
        });

        languageAndCountryBinding.tvEnglishLang.setOnClickListener(v -> {
            language = LocaleHelper.ENGLISH_LANGUAGE;
            updateLanguage();
        });

        languageAndCountryBinding.tvThemeModern.setOnClickListener(v -> {
            theme = Constants.MODERN_THEME;
            updateTheme();
            recreate();
        });

        languageAndCountryBinding.tvThemeLight.setOnClickListener(v -> {
            theme = Constants.LIGHT_THEME;
            updateTheme();
            recreate();
        });

        languageAndCountryBinding.tvThemeDark.setOnClickListener(v -> {
            theme = Constants.DARK_THEME;
            updateTheme();
            recreate();
        });
        languageAndCountryBinding.btnConfirm.setBackgroundResource(theme == Constants.MODERN_THEME ? R.drawable.shape_blue_15dp : R.drawable.shape_gradient_blue_15dp);
        languageAndCountryBinding.btnConfirm.setOnClickListener(v -> {
                    sharedPreferences.saveLanguage(language);
                    sharedPreferences.saveCountryID(idCountry);
                    sharedPreferences.saveCountryName(nameCountry);
                    sharedPreferences.saveStatusNotification(1);
                    sharedPreferences.saveThemeMode(theme);
                    // check if device token empty -> get device token then add token on server
                    // if exist add device token on server
                    if (TextUtils.isEmpty(sharedPreferences.getTokenFCM())) {
                        getToken();
                    } else {
                        model.addTokenDevice();
                    }
                }
        );

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(LANGUAGE_KEY, language);
        savedInstanceState.putInt(COUNTRY_ID_KEY, idCountry);
        savedInstanceState.putString(COUNTRY_NAME_KEY, nameCountry);
        savedInstanceState.putInt(THEME_KEY, theme);
        savedInstanceState.putBoolean(FIRST_TIME_KEY, false);
    }

    public void getToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("FCM", "getInstanceId failed", task.getException());
                        return;
                    }
                    // Get new Instance ID token
                    if (task.getResult() != null) {
                        sharedPreferences.saveTokenFCM(task.getResult().getToken());
                        model.addTokenDevice();
                    }
                });
    }

    private void changeBackgroundButtonLanguage(TextView viewSelected, TextView viewUnSelected) {
        viewSelected.setBackgroundResource(theme == Constants.MODERN_THEME ? R.drawable.shape_blue_light_radius_15dp : R.drawable.shape_gradient_green);
        viewSelected.setTextColor(ContextCompat.getColor(this, theme == Constants.MODERN_THEME ? R.color.black : R.color.white));
        viewUnSelected.setBackground(null);
        viewUnSelected.setTextColor(ContextCompat.getColor(this, R.color.gray4));
    }

    private void updateLanguage() {
        switch (language) {
            case LocaleHelper.ARABIC_LANGUAGE:
                changeBackgroundButtonLanguage(languageAndCountryBinding.tvArabicLang, languageAndCountryBinding.tvEnglishLang);
                break;

            case LocaleHelper.ENGLISH_LANGUAGE:
                changeBackgroundButtonLanguage(languageAndCountryBinding.tvEnglishLang, languageAndCountryBinding.tvArabicLang);
                break;
        }
    }

    private void updateTheme() {
        switch (theme) {
            case Constants.MODERN_THEME:
                languageAndCountryBinding.viewLanguage.setBackgroundResource(R.drawable.shape_stroke_gray_15dp);
                languageAndCountryBinding.spinnerCountries.setBackgroundResource(R.drawable.shape_gray_radius_15dp);
                languageAndCountryBinding.viewTheme.setBackgroundResource(R.drawable.shape_stroke_gray_15dp);
                changeBackgroundButtonTheme(languageAndCountryBinding.tvThemeModern, languageAndCountryBinding.tvThemeLight, languageAndCountryBinding.tvThemeDark);
                break;
            case Constants.LIGHT_THEME:
                languageAndCountryBinding.viewLanguage.setBackgroundResource(R.drawable.shape_white_radius_15dp);
                languageAndCountryBinding.spinnerCountries.setBackgroundResource(R.drawable.shape_white_radius_15dp);
                languageAndCountryBinding.viewTheme.setBackgroundResource(R.drawable.shape_white_radius_15dp);
                changeBackgroundButtonTheme(languageAndCountryBinding.tvThemeLight, languageAndCountryBinding.tvThemeModern, languageAndCountryBinding.tvThemeDark);
                break;
            case Constants.DARK_THEME:
                languageAndCountryBinding.viewLanguage.setBackgroundResource(R.drawable.shape_black_radius_15dp);
                languageAndCountryBinding.spinnerCountries.setBackgroundResource(R.drawable.shape_black_radius_15dp);
                languageAndCountryBinding.viewTheme.setBackgroundResource(R.drawable.shape_black_radius_15dp);
                changeBackgroundButtonTheme(languageAndCountryBinding.tvThemeDark, languageAndCountryBinding.tvThemeModern, languageAndCountryBinding.tvThemeLight);
                break;
        }
    }

    private void changeBackgroundButtonTheme(TextView viewSelected, TextView viewUnSelected1, TextView viewUnSelected2) {
        viewSelected.setBackgroundResource(theme == Constants.MODERN_THEME ? R.drawable.shape_blue_light_radius_15dp : R.drawable.shape_gradient_green);
        viewSelected.setTextColor(ContextCompat.getColor(this, theme == Constants.MODERN_THEME ? R.color.black : R.color.white));
        viewUnSelected1.setBackground(null);
        viewUnSelected1.setTextColor(ContextCompat.getColor(this, R.color.gray4));
        viewUnSelected2.setBackground(null);
        viewUnSelected2.setTextColor(ContextCompat.getColor(this, R.color.gray4));
    }
}