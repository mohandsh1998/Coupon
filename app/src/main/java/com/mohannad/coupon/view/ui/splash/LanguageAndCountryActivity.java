package com.mohannad.coupon.view.ui.splash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.mohannad.coupon.R;
import com.mohannad.coupon.data.local.StorageSharedPreferences;
import com.mohannad.coupon.data.model.CategoriesResponse;
import com.mohannad.coupon.data.model.CountryResponse;
import com.mohannad.coupon.databinding.ActivityLanguageAndCountryBinding;
import com.mohannad.coupon.utils.BaseActivity;
import com.mohannad.coupon.utils.Constants;
import com.mohannad.coupon.utils.LocaleHelper;
import com.mohannad.coupon.view.adapter.spinner.SpinnerAdapter;
import com.mohannad.coupon.view.adapter.spinner.SpinnerImageWithTextAdapter;
import com.mohannad.coupon.view.ui.main.MainActivity;
import com.mohannad.coupon.view.ui.search.SearchViewModel;
import com.mohannad.coupon.view.ui.webview.WebViewActivity;

import java.util.ArrayList;

public class LanguageAndCountryActivity extends BaseActivity {
    private String language = LocaleHelper.ENGLISH_LANGUAGE;
    private int idCountry = -1;
    private String nameCountry;
    private StorageSharedPreferences sharedPreferences;
    private SplashViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLanguageAndCountryBinding languageAndCountryBinding = DataBindingUtil.setContentView(this, R.layout.activity_language_and_country);
        changeToolbarAndStatusBar(R.color.gray7, null);
        model = new ViewModelProvider(this).get(SplashViewModel.class);
        languageAndCountryBinding.setSplashViewModel(model);
        languageAndCountryBinding.setLifecycleOwner(this);
        sharedPreferences = new StorageSharedPreferences(this);
        // check if device language arabic select arabic button default
        if (sharedPreferences.getLanguage().equals(LocaleHelper.ARABIC_LANGUAGE)) {
            language = LocaleHelper.ARABIC_LANGUAGE;
            changeBackgroundButton(languageAndCountryBinding.tvArabicLang, languageAndCountryBinding.tvEnglishLang);
        }
        model.getCountries();

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
                startActivity(new Intent(LanguageAndCountryActivity.this, MainActivity.class));
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
            changeBackgroundButton(languageAndCountryBinding.tvArabicLang, languageAndCountryBinding.tvEnglishLang);
        });

        languageAndCountryBinding.tvEnglishLang.setOnClickListener(v -> {
            language = LocaleHelper.ENGLISH_LANGUAGE;
            changeBackgroundButton(languageAndCountryBinding.tvEnglishLang, languageAndCountryBinding.tvArabicLang);
        });

        languageAndCountryBinding.btnConfirm.setOnClickListener(v -> {
            if (languageAndCountryBinding.cbTermsAndConditions.isChecked()) {
                sharedPreferences.saveLanguage(language);
                sharedPreferences.saveCountryID(idCountry);
                sharedPreferences.saveCountryName(nameCountry);
                sharedPreferences.saveStatusNotification(1);
                // check if device token empty -> get device token then add token on server
                // if exist add device token on server
                if (TextUtils.isEmpty(sharedPreferences.getTokenFCM())) {
                    getToken();
                } else {
                    model.addTokenDevice();
                }
            } else
                showAlertDialog(languageAndCountryBinding.lyContainer, getString(R.string.must_agree_on_terms_and_condition));
        });
        languageAndCountryBinding.tvTermsAndCondition.setOnClickListener(v -> {
//            startActivity(new Intent(this, WebViewActivity.class).putExtra("url", Constants.TERMS_AND_CONDITIONS_URL + sharedPreferences.getLanguage()));
            openBrowser(Constants.TERMS_AND_CONDITIONS_URL + sharedPreferences.getLanguage());
        });
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

    private void changeBackgroundButton(TextView viewSelected, TextView viewUnSelected) {
        viewSelected.setBackgroundResource(R.drawable.shape_white_radius_9dp);
        viewSelected.setElevation(2);
        viewSelected.setTextColor(ContextCompat.getColor(this, R.color.pink));
        viewUnSelected.setBackground(null);
        viewUnSelected.setElevation(0);
        viewUnSelected.setTextColor(ContextCompat.getColor(this, R.color.black));
    }
}