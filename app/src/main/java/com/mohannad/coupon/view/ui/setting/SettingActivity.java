package com.mohannad.coupon.view.ui.setting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.mohannad.coupon.R;
import com.mohannad.coupon.data.local.StorageSharedPreferences;
import com.mohannad.coupon.data.model.CountryResponse;
import com.mohannad.coupon.databinding.ActivitySettingBinding;
import com.mohannad.coupon.utils.BaseActivity;
import com.mohannad.coupon.utils.LocaleHelper;
import com.mohannad.coupon.view.adapter.spinner.SpinnerImageWithTextAdapter;
import com.mohannad.coupon.view.ui.splash.SplashActivity;
import com.mohannad.coupon.view.ui.splash.SplashViewModel;

import java.util.ArrayList;

public class SettingActivity extends BaseActivity {
    private String language;
    private int idCountry = -1;
    private String nameCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.setting_app);
        ActivitySettingBinding settingBinding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        SettingViewModel model = new ViewModelProvider(this).get(SettingViewModel.class);
        settingBinding.setSettingViewModel(model);
        settingBinding.setLifecycleOwner(this);
        StorageSharedPreferences sharedPreferences = new StorageSharedPreferences(this);
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
            restartApp();
        });
        // remove shadow in actionbar and change arrow color
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_back_arrow));
            getSupportActionBar().setElevation(0);
        }
    }

    private void restartApp() {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(intent);
    }
}