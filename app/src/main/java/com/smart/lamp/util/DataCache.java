package com.smart.lamp.util;

import android.content.Context;
import android.text.TextUtils;

/**
 * Created by marco on 2017/8/23.
 * 数据缓存
 */

public class DataCache {
    private static String accessToken;
    private static float safeEyeDistance;
    private static float safeBodyDistance;

    public static String getAccessToken(Context context) {
        if (TextUtils.isEmpty(accessToken)) {
            accessToken = SPHelper.getInstant().getStringFromSP(context, Constants.ACCESS_TOKEN);
        }
        return accessToken;
    }

    public static float getSafeEyeDistance(Context context) {
        if (0f == safeEyeDistance) {
            safeEyeDistance = SPHelper.getInstant().getFloatFromSP(context,
                    Constants.SAFE_EYE_DISTANCE);
        }
        return safeEyeDistance;
    }

    public static float getSafeBodyDistance(Context context) {
        if (0f == safeBodyDistance) {
            safeBodyDistance = SPHelper.getInstant().getFloatFromSP(context,
                    Constants.SAFE_BODY_DISTANCE);
        }
        return safeBodyDistance;
    }

    public static void updateAccessToken(Context context, String value) {
        accessToken = value;
        SPHelper.getInstant().putData2SP(context, Constants.ACCESS_TOKEN, value);
    }

    public static void updateSafeEyeDistance(Context context, float value) {
        safeEyeDistance = value;
        SPHelper.getInstant().putData2SP(context, Constants.SAFE_EYE_DISTANCE, value);
    }

    public static void updateSafeBodyDistance(Context context, float value) {
        safeBodyDistance = value;
        SPHelper.getInstant().putData2SP(context, Constants.SAFE_BODY_DISTANCE, value);
    }
}
