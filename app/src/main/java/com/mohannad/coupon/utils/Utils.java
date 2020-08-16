package com.mohannad.coupon.utils;

import android.util.Patterns;

public class Utils {
    /**
     * method is used for checking valid email id format.
     *
     * @param email the email that entered by the user
     * @return boolean true for valid false for invalid
     */
    public static boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * method is used for checking valid mobile format.
     *
     * @param mobile the mobile that entered by the user
     * @return boolean true for valid false for invalid
     */
    public static boolean isMobileValid(String mobile) {
        return Patterns.PHONE.matcher(mobile).matches();
    }
}
