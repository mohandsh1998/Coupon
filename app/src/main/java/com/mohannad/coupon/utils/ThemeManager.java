package com.mohannad.coupon.utils;

import android.content.Context;

import com.mohannad.coupon.R;

public class ThemeManager {
    public static void setCustomizedThemes(Context context, int theme) {
        switch (theme) {
            case Constants.MODERN_THEME:
                context.setTheme(R.style.BaseTheme_AppThemeModern);
                break;
            case Constants.LIGHT_THEME:
                context.setTheme(R.style.BaseTheme_AppThemeLight);
                break;
            case Constants.DARK_THEME:
                context.setTheme(R.style.BaseTheme_AppThemeDark);
                break;
        }
    }
}
