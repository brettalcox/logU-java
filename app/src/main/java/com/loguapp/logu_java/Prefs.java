package com.loguapp.logu_java;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by BA042808 on 4/7/2016.
 */
public class Prefs {
    private static String MY_STRING_PREF = "username";

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences("MyPreferences", 0);
    }

    public static String getMyStringPref(Context context) {
        return getPrefs(context).getString(MY_STRING_PREF, "default");
    }

    public static void setMyStringPref(Context context, String value) {
        // perform validation etc..
        getPrefs(context).edit().putString(MY_STRING_PREF, value).apply();
    }
}
