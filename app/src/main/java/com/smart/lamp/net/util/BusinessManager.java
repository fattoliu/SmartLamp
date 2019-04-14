package com.smart.lamp.net.util;

import android.text.TextUtils;

import com.smart.lamp.net.request.SignIn;
import com.smart.lamp.net.response.DeviceInfo;
import com.smart.lamp.net.response.UserInfo;
import com.smart.lamp.net.response.base.BaseResponseEntity;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by marco on 2017/8/21.
 * api调用逻辑
 */

public class BusinessManager {
    private ApiService apiService;
    private String accessToken;

    public BusinessManager(String accessToken) {
        this.apiService = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://api.nlecloud.com:80/")
                .client(initOkHttpClient())
                .build()
                .create(ApiService.class);
        this.accessToken = accessToken;
    }

    private static OkHttpClient initOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //添加日志拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(loggingInterceptor);

        Interceptor cacheInterceptor = chain -> {
            Request request = chain.request();
            Response response = chain.proceed(request);
            return response.newBuilder()
                    .header("Cache-Control", "no-store")
                    .removeHeader("Pragma")
                    .build();
        };

        builder.addInterceptor(cacheInterceptor);
        return builder.build();
    }


    /**
     * 登录
     *
     * @param signIn   登录信息
     * @param callback 回调
     */
    public void signIn(SignIn signIn, RetrofitCallBack<BaseResponseEntity<UserInfo>> callback) {
        apiService.signIn(signIn).enqueue(callback);
    }

    /**
     * 查询单个设备
     *
     * @param deviceId 设备ID
     */
    public void getDeviceInfo(String deviceId, RetrofitCallBack<BaseResponseEntity<DeviceInfo>> callback) {
        apiService.getDeviceInfo(deviceId, accessToken).enqueue(callback);
    }

    /**
     * 发送命令/控制设备
     *
     * @param deviceId 设备ID（必填）
     * @param apiTag   传感标识名（必须但值可为空）
     * @param data     开关类：开=1，关=0，暂停=2
     *                 家居类：调光灯亮度=0~254，RGB灯色度=2~239，窗帘、卷闸门、幕布打开百分比=3%~100%，红外指令=1(on)2(off)
     *                 其它：integer/float/Json/String类型值
     */
    public void control(String deviceId, String apiTag, Object data, RetrofitCallBack<BaseResponseEntity> callback) {
        apiService.control(deviceId, apiTag, data, accessToken).enqueue(callback);
    }
}
