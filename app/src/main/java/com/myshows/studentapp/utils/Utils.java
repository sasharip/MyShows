package com.myshows.studentapp.utils;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {

    public static String toMD5(@NonNull String input, @Nullable String salt) {
        String target = salt == null ? input : input + salt;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hashedBytes = digest.digest(target.getBytes("UTF-8"));

            StringBuilder stringBuilder = new StringBuilder();
            for (byte arrayByte : hashedBytes) {
                stringBuilder.append(Integer.toString((arrayByte & 0xff) + 0x100, 16).substring(1));
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            return null;
        }
    }

    public static void makeSnackBar(Activity activity,
                                    @StringRes int message,
                                    @Snackbar.Duration int length) {
        Snackbar.make(
                activity.findViewById(android.R.id.content),
                message,
                length
        ).show();

    }

}
