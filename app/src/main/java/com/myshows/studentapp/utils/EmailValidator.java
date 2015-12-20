package com.myshows.studentapp.utils;

import android.util.Patterns;

public class EmailValidator {

    public static boolean isValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
