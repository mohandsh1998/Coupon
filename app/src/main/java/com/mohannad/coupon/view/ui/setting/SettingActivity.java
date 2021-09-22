package com.mohannad.coupon.view.ui.setting;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.mohannad.coupon.R;
import com.mohannad.coupon.data.local.StorageSharedPreferences;
import com.mohannad.coupon.data.model.CountryResponse;
import com.mohannad.coupon.databinding.ActivitySettingBinding;
import com.mohannad.coupon.utils.BaseActivity;
import com.mohannad.coupon.utils.Constants;
import com.mohannad.coupon.utils.LocaleHelper;
import com.mohannad.coupon.utils.ThemeManager;
import com.mohannad.coupon.view.adapter.spinner.SpinnerImageWithTextAdapter;
import com.mohannad.coupon.view.ui.splash.SplashActivity;

import java.util.ArrayList;

public class SettingActivity extends BaseActivity {
    private final String LANGUAGE_KEY = "Language";
    private final String THEME_KEY = "Theme";
    private final String COUNTRY_ID_KEY = "country_id";
    private final String COUNTRY_NAME_KEY = "country_name";
    private final String FIRST_TIME_KEY = "first_time";
    private String language;
    private int idCountry = -1;
    private String nameCountry;
    private boolean firstTime = true;
    private int theme = Constants.LIGHT_THEME;
    private ActivitySettingBinding settingBinding;

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
        setTitle(R.string.setting_app);
        settingBinding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        SettingViewModel model = new ViewModelProvider(this).get(SettingViewModel.class);
        settingBinding.setSettingViewModel(model);
        settingBinding.setLifecycleOwner(this);
        if (firstTime) model.getCountries();
        settingBinding.switchNotification.setChecked(sharedPreferences.getStatusNotification() == 1);
        settingBinding.spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    language = LocaleHelper.ENGLISH_LANGUAGE;
                else
                    language = LocaleHelper.ARABIC_LANGUAGE;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        settingBinding.spinnerLanguage.setSelection((sharedPreferences.getLanguage().equals(LocaleHelper.ENGLISH_LANGUAGE)) ? 0 : 1);

        if (firstTime) {
            language = sharedPreferences.getLanguage();
            theme = sharedPreferences.getThemeMode();
            idCountry = sharedPreferences.getCountryID();
            nameCountry = sharedPreferences.getCountryName();
        }
        updateTheme();
        // array countries
        ArrayList<CountryResponse.Country> countries = new ArrayList<>();
        // init countries adapter
        SpinnerImageWithTextAdapter adapterCountries = new SpinnerImageWithTextAdapter(this, countries);
        settingBinding.spinnerCountries.setAdapter(adapterCountries);
        model.countries.observe(this, countryList -> {
            adapterCountries.addAll(countryList);
            for (int i = 0; i < countryList.size(); ++i) {
                if (sharedPreferences.getCountryID() == countryList.get(i).getId()) {
                    settingBinding.spinnerCountries.setSelection(i);
                }
            }
        });
        settingBinding.spinnerCountries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        settingBinding.btnSave.setOnClickListener(v -> {
            sharedPreferences.saveLanguage(language);
            sharedPreferences.saveCountryID(idCountry);
            sharedPreferences.saveCountryName(nameCountry);
            sharedPreferences.saveThemeMode(theme);
            sharedPreferences.saveStatusNotification(settingBinding.switchNotification.isChecked() ? 1 : 0);
            model.addTokenDevice();
        });

        settingBinding.tvThemeModern.setOnClickListener(v -> {
            theme = Constants.MODERN_THEME;
            updateTheme();
            recreate();
        });

        settingBinding.tvThemeLight.setOnClickListener(v -> {
            theme = Constants.LIGHT_THEME;
            updateTheme();
            recreate();
        });

        settingBinding.tvThemeDark.setOnClickListener(v -> {
            theme = Constants.DARK_THEME;
            updateTheme();
            recreate();
        });

        model.successTokenDevice.observe(this, success -> {
            if (success) {
                restartApp();
            }
        });

        settingBinding.imgBackArrow.setOnClickListener(v -> {
            onBackPressed();
        });
        updateTheme();
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

    private void updateTheme() {
        switch (theme) {
            case Constants.MODERN_THEME:
                settingBinding.btnSave.setBackgroundResource(R.drawable.shape_blue_15dp);
                settingBinding.lyLanguage.setBackgroundResource(R.drawable.shape_gray_radius_15dp);
                settingBinding.spinnerLanguage.setPopupBackgroundDrawable(new ColorDrawable(Color.WHITE));
                settingBinding.spinnerLanguage.setBackgroundResource(R.drawable.shape_white_radius_15dp);
                settingBinding.lyCountry.setBackgroundResource(R.drawable.shape_gray_radius_15dp);
                settingBinding.spinnerCountries.setBackgroundResource(R.drawable.shape_white_radius_15dp);
                settingBinding.lyNotification.setBackgroundResource(R.drawable.shape_gray_radius_15dp);
                settingBinding.viewTheme.setBackgroundResource(R.drawable.shape_gray_radius_15dp);
                changeBackgroundButtonTheme(settingBinding.tvThemeModern, settingBinding.tvThemeLight, settingBinding.tvThemeDark);
                break;
            case Constants.LIGHT_THEME:
                settingBinding.btnSave.setBackgroundResource(R.drawable.shape_gradient_blue_15dp);
                settingBinding.spinnerLanguage.setBackgroundResource(R.drawable.shape_gray_radius_15dp);
                settingBinding.lyLanguage.setBackgroundResource(R.drawable.shape_white_radius_15dp);
                settingBinding.spinnerLanguage.setPopupBackgroundDrawable(new ColorDrawable(Color.WHITE));
                settingBinding.spinnerCountries.setBackgroundResource(R.drawable.shape_gray_radius_15dp);
                settingBinding.lyCountry.setBackgroundResource(R.drawable.shape_white_radius_15dp);
                settingBinding.lyNotification.setBackgroundResource(R.drawable.shape_white_radius_15dp);
                settingBinding.viewTheme.setBackgroundResource(R.drawable.shape_white_radius_15dp);
                changeBackgroundButtonTheme(settingBinding.tvThemeLight, settingBinding.tvThemeModern, settingBinding.tvThemeDark);
                break;
            case Constants.DARK_THEME:
                settingBinding.btnSave.setBackgroundResource(R.drawable.shape_gradient_blue_15dp);
                settingBinding.spinnerLanguage.setBackgroundResource(R.drawable.shape_black1_radius_15dp);
                settingBinding.spinnerLanguage.setPopupBackgroundDrawable(new ColorDrawable(Color.BLACK));
                settingBinding.lyLanguage.setBackgroundResource(R.drawable.shape_black_radius_15dp);
                settingBinding.spinnerCountries.setBackgroundResource(R.drawable.shape_black1_radius_15dp);
                settingBinding.lyCountry.setBackgroundResource(R.drawable.shape_black_radius_15dp);
                settingBinding.lyNotification.setBackgroundResource(R.drawable.shape_black_radius_15dp);
                settingBinding.viewTheme.setBackgroundResource(R.drawable.shape_black_radius_15dp);
                changeBackgroundButtonTheme(settingBinding.tvThemeDark, settingBinding.tvThemeModern, settingBinding.tvThemeLight);
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

    private void restartApp() {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(intent);
    }
}