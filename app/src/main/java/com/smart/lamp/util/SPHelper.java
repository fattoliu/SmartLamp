package com.smart.lamp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SPHelper {

    private static SPHelper spHelperInstant;

    private SPHelper() {

    }

    static SPHelper getInstant() {
        if (spHelperInstant == null) {
            synchronized (SPHelper.class) {
                if (spHelperInstant == null) {
                    spHelperInstant = new SPHelper();
                }
            }
        }
        return spHelperInstant;
    }

    /**
     * SharePreference方式存储
     *
     * @param context   context
     * @param key       存储在XML中的key
     * @param value     值
     */
    void putData2SP(Context context, String key, Object value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sharedPreferences.edit();
        if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else {
            return;
        }
        editor.apply();
    }

    String getStringFromSP(Context context, String key) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getString(key, "");
    }

    float getFloatFromSP(Context context, String key) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getFloat(key, 0f);
    }

    public Boolean getBooleanFromSP(Context context, String key) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getBoolean(key, true);
    }
}
