package com.mohannad.coupon.view.ui.splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLanguageAndCountryBinding languageAndCountryBinding = DataBindingUtil.setContentView(this, R.layout.activity_language_and_country);
        SplashViewModel model = new ViewModelProvider(this).get(SplashViewModel.class);
        languageAndCountryBinding.setSplashViewModel(model);
        languageAndCountryBinding.setLifecycleOwner(this);
        StorageSharedPreferences sharedPreferences = new StorageSharedPreferences(this);
        // check if device language arabic select arabic button default
        if (sharedPreferences.getLanguage().equals(LocaleHelper.ARABIC_LANGUAGE)) {
            language = LocaleHelper.ARABIC_LANGUAGE;
            changeBackgroundButton(languageAndCountryBinding.btnArabicLang, languageAndCountryBinding.btnEnglishLang);
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

        languageAndCountryBinding.btnArabicLang.setOnClickListener(v -> {
            language = LocaleHelper.ARABIC_LANGUAGE;
            changeBackgroundButton(languageAndCountryBinding.btnArabicLang, languageAndCountryBinding.btnEnglishLang);
        });

        languageAndCountryBinding.btnEnglishLang.setOnClickListener(v -> {
            language = LocaleHelper.ENGLISH_LANGUAGE;
            changeBackgroundButton(languageAndCountryBinding.btnEnglishLang, languageAndCountryBinding.btnArabicLang);
        });

        languageAndCountryBinding.btnConfirm.setOnClickListener(v -> {
            if (languageAndCountryBinding.cbTermsAndConditions.isChecked()) {
                sharedPreferences.saveLanguage(language);
                sharedPreferences.saveCountryID(idCountry);
                sharedPreferences.saveCountryName(nameCountry);
                startActivity(new Intent(LanguageAndCountryActivity.this, MainActivity.class));
                finish();
            } else showAlertDialog(languageAndCountryBinding.lyContainer, getString(R.string.must_agree_on_terms_and_condition));
        });
        languageAndCountryBinding.tvTermsAndCondition.setOnClickListener(v -> {
            startActivity(new Intent(this, WebViewActivity.class).putExtra("url", Constants.TERMS_AND_CONDITIONS_URL + sharedPreferences.getLanguage()));
        });
    }

    private void changeBackgroundButton(Button buttonSelected, Button buttonUnSelected) {
        buttonSelected.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_solid_pink_radius_9dp));
        buttonSelected.setTextColor(getResources().getColor(R.color.white));
        buttonUnSelected.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_white_with_border_pink_radius_9dp));
        buttonUnSelected.setTextColor(getResources().getColor(R.color.black));
    }
}