package com.loguapp.logu_java;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by BA042808 on 4/7/2016.
 */
public class Prefs {
    private static String USERNAME = "username";
    private static String ISLOGGEDIN = "loggedIn";

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences("MyPreferences", 0);
    }

    public static String getUsername(Context context) {
        return getPrefs(context).getString(USERNAME, "default");
    }

    public static void setUsername(Context context, String value) {
        // perform validation etc..
        getPrefs(context).edit().putString(USERNAME, value).apply();
    }

    public static int getSessionStatus(Context context) {
        return getPrefs(context).getInt(ISLOGGEDIN, 69);
    }

    public static void userLoggedIn(Context context) {
        getPrefs(context).edit().putInt(ISLOGGEDIN, 1).apply();
    }

    public static void userLoggedOut(Context context) {
        getPrefs(context).edit().putInt(ISLOGGEDIN, 0).apply();
    }
}
