package com.smart.lamp.net.util;

import com.smart.lamp.net.request.SignIn;
import com.smart.lamp.net.response.DeviceInfo;
import com.smart.lamp.net.response.UserInfo;
import com.smart.lamp.net.response.base.BaseResponseEntity;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiService {

    @POST("Users/Login")
    Call<BaseResponseEntity<UserInfo>> signIn(@Body SignIn signIn);

    @GET("Devices/{deviceId}")
    Call<BaseResponseEntity<DeviceInfo>> getDeviceInfo(@Path("deviceId") String deviceId,
                                                       @Header("AccessToken") String accessToken);

    @POST("Cmds")
    Call<BaseResponseEntity> control(@Query("deviceId") String deviceId,
                                     @Query("apiTag") String apiTag,
                                     @Body Object data,
                                     @Header("AccessToken") String accessToken);
}
