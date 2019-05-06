package com.smart.lamp.util;

/**
 * TODO  Data cache
 *
 * @author fattoliu
 * @version V 1.0
 * @date on 9/3/2019 9:30 PM
 */
public class DataCache {
    /**
     * 访问令牌
     */
    private static String accessToken;
    /**
     * 桌面与眼睛的安全距离
     */
    private static float safeEyeDistance;
    /**
     * 桌面与身体的安全距离
     */
    private static float safeBodyDistance;

    /**
     * 获取登录令牌
     * @return 令牌
     */
    public static String getAccessToken() {
        return accessToken;
    }

    /** 获取桌面与眼睛的安全距离
     * @return 桌面与眼睛的安全距离
     */
    public static float getSafeEyeDistance() {
        return safeEyeDistance;
    }

    /**
     * 获取桌面与身体的安全距离
     * @return 桌面与身体的安全距离
     */
    public static float getSafeBodyDistance() {
        return safeBodyDistance;
    }

    /** 更新令牌
     * @param value 令牌
     */
    public static void updateAccessToken(String value) {
        accessToken = value;
    }

    /** 更新桌面与眼睛的安全距离
     * @param value 桌面与眼睛的安全距离
     */
    public static void updateSafeEyeDistance(float value) {
        safeEyeDistance = value;
    }

    /** 更新桌面与身体的安全距离
     * @param value 桌面与身体的安全距离
     */
    public static void updateSafeBodyDistance(float value) {
        safeBodyDistance = value;
    }
}
