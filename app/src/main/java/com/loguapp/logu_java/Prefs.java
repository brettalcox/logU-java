package com.loguapp.logu_java;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by BA042808 on 4/7/2016.
 */
public class Prefs {
    private static String USERNAME = "username";
    private static String ISLOGGEDIN = "loggedIn";
    private static String BODYWEIGHT = "bodyweight";
    private static String GENDER = "gender";
    private static String UNIT = "unit";
    private static String LATITUDE = "latitude";
    private static String LONGITUDE = "longitude";
    private static String LOG_GPS = "log_gps";

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

    public static void setBodyweight(Context context, int value) {
        getPrefs(context).edit().putInt(BODYWEIGHT, value).apply();
    }

    public static void setGender(Context context, String value) {
        getPrefs(context).edit().putString(GENDER, value).apply();
    }

    public static void setUnit(Context context, int value) {
        getPrefs(context).edit().putInt(UNIT, value).apply();
    }

    public static int getBodyweight(Context context) {
        return getPrefs(context).getInt(BODYWEIGHT, 9000);
    }

    public static String getGender(Context context) {
        return getPrefs(context).getString(GENDER, "M");
    }

    public static int getUnit(Context context) {
        return getPrefs(context).getInt(UNIT, 1);
    }

    public static void setLat(Context context, double value) {
        getPrefs(context).edit().putFloat(LATITUDE, (float) value).apply();
    }

    public static void setLon(Context context, double value) {
        getPrefs(context).edit().putFloat(LONGITUDE, (float) value).apply();
    }

    public static float getLat(Context context) {
        return getPrefs(context).getFloat(LATITUDE, 6969);
    }

    public static float getLon(Context context) {
        return getPrefs(context).getFloat(LONGITUDE, 6969);
    }

    public static void setLogGPS(Context context, boolean value) {
        getPrefs(context).edit().putBoolean(LOG_GPS, value).apply();
    }

    public static boolean getLogGPS(Context context) {
        return getPrefs(context).getBoolean(LOG_GPS, false);
    }
}
