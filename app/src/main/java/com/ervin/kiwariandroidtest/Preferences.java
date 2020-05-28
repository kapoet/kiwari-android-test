package com.ervin.kiwariandroidtest;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {
    private static SharedPreferences getInstance(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setLoggedIn(Context context, boolean isLoggedIn, String uid) {
        SharedPreferences.Editor editor = getInstance(context).edit();
        editor.putBoolean(Global.IS_LOGGED_IN, isLoggedIn);
        editor.putString(Global.UID, uid);
        editor.apply();
    }

    public static boolean isLoggedIn(Context context) {
        return getInstance(context).getBoolean(Global.IS_LOGGED_IN,false);
    }

    public static String getCurrentUID(Context context) {
        return getInstance(context).getString(Global.UID,"");
    }
}
