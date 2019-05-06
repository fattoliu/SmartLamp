package com.smart.lamp.net.util;

import com.smart.lamp.net.request.SignIn;
import com.smart.lamp.net.response.DeviceInfo;
import com.smart.lamp.net.response.SensorDataRecord;
import com.smart.lamp.net.response.UserInfo;
import com.smart.lamp.net.response.base.BaseResponseEntity;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * TODO api service interface
 *
 * @author fattoliu
 * @version V 1.0
 * @date on 6/3/2019 5:17 PM
 */
public interface ApiService {

    /** 登录
     */
    @POST("Users/Login")
    Call<BaseResponseEntity<UserInfo>> signIn(@Body SignIn signIn);

    /**
     * 获取设备信息
     */
    @GET("Devices/{deviceId}")
    Call<BaseResponseEntity<DeviceInfo>> getDeviceInfo(@Path("deviceId") String deviceId,
                                                       @Header("AccessToken") String accessToken);
    /**
     * 发起指令
     */
    @POST("Cmds")
    Call<BaseResponseEntity> control(@Query("deviceId") String deviceId,
                                     @Query("apiTag") String apiTag,
                                     @Body Object data,
                                     @Header("AccessToken") String accessToken);

    /**
     * 聚合查询
     */
    @GET("devices/{deviceId}/datas/grouping")
    Call<BaseResponseEntity<SensorDataRecord>> getSensorDataGrouping(@Path("deviceId") String deviceId,
                                                                     @Query("ApiTags") String apiTag,
                                                                     @Query("GroupBy") String groupBy,
                                                                     @Query("Func") String func,
                                                                     @Query("StartDate") String startDate,
                                                                     @Query("EndDate") String endDate,
                                                                     @Header("AccessToken") String accessToken);

    /**
     * 模糊查询
     */
    @GET("devices/{deviceId}/Datas")
    Call<BaseResponseEntity<SensorDataRecord>> getSensorData(@Path("deviceId") String deviceId,
                                                             @Query("ApiTags") String ApiTags,
                                                             @Query("Method") String Method,
                                                             @Query("TimeAgo") String TimeAgo,
                                                             @Query("StartDate") String StartDate,
                                                             @Query("EndDate") String EndDate,
                                                             @Query("Sort") String Sort,
                                                             @Query("PageSize") String PageSize,
                                                             @Query("PageIndex") String PageIndex,
                                                             @Header("AccessToken") String accessToken);

}
