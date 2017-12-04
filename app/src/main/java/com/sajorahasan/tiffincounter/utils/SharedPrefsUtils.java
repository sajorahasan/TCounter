package com.sajorahasan.tiffincounter.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by Sajora on 29-05-2017.
 */

public class SharedPrefsUtils {
    private static SharedPrefsUtils store;
    private SharedPreferences SP;
    private static String filename = "TiffinCounter";

    private SharedPrefsUtils(Context context) {
        SP = context.getApplicationContext().getSharedPreferences(filename, 0);
    }

    public static SharedPrefsUtils getInstance(Context context) {
        if (store == null) {
            store = new SharedPrefsUtils(context);
        }
        return store;
    }

    public void put(String key, String value) {//Log.v("Keystore","PUT "+key+" "+value);
        Editor editor;

        editor = SP.edit();
        editor.putString(key, value);
        editor.apply(); // Stop everything and do an immediate save!
        // editor.apply();//Keep going and save when you are not busy - Available only in APIs 9 and above.  This is the preferred way of saving.
    }

    public void putBoolean(String key, boolean value) {
        Editor editor;

        editor = SP.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public String get(String key) {//Log.v("Keystore","GET from "+key);
        return SP.getString(key, null);
    }

    public int getInt(String key) {//Log.v("Keystore","GET INT from "+key);
        return SP.getInt(key, 0);
    }

    public boolean getBoolean(String key) {//Log.v("Keystore","GET from "+key);
        return SP.getBoolean(key, false);
    }

    public void putInt(String key, int num) {//Log.v("Keystore","PUT INT "+key+" "+String.valueOf(num));
        Editor editor;
        editor = SP.edit();

        editor.putInt(key, num);
        editor.apply();
    }


    public void clear() {
        Editor editor;
        editor = SP.edit();

        editor.clear();
        editor.apply();
    }

    public void remove() {
        Editor editor;
        editor = SP.edit();

        editor.remove(filename);
        editor.apply();
    }
}