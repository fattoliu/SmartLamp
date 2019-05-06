package com.smart.lamp.net.util;

import com.smart.lamp.net.request.SignIn;
import com.smart.lamp.net.response.DeviceInfo;
import com.smart.lamp.net.response.SensorDataRecord;
import com.smart.lamp.net.response.UserInfo;
import com.smart.lamp.net.response.base.BaseResponseEntity;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * TODO Api manager class
 *
 * @author fattoliu
 * @version V 1.0
 * @date on 8/3/2019 10:23 PM
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

    /**
     * 初始化 OKHttpClient
     * @return OKHttpClient 实例
     */
    private OkHttpClient initOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(loggingInterceptor);
        builder.callTimeout(180, TimeUnit.SECONDS);
        builder.readTimeout(180, TimeUnit.SECONDS);
        builder.writeTimeout(180, TimeUnit.SECONDS);
        builder.connectTimeout(180, TimeUnit.SECONDS);

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

    /** 登录
     * @param signIn 登录信息实体
     * @param callback 交易回调
     */
    public void signIn(SignIn signIn, RetrofitCallBack<BaseResponseEntity<UserInfo>> callback) {
        apiService.signIn(signIn).enqueue(callback);
    }

    /**
     * 获取设备信息
     * @param deviceId 设备ID
     * @param callback 交易回调
     */
    public void getDeviceInfo(String deviceId, RetrofitCallBack<BaseResponseEntity<DeviceInfo>> callback) {
        apiService.getDeviceInfo(deviceId, accessToken).enqueue(callback);
    }

    public void control(String deviceId, String apiTag, Object data, RetrofitCallBack<BaseResponseEntity> callback) {
        apiService.control(deviceId, apiTag, data, accessToken).enqueue(callback);
    }

    /**
     * 聚合查询传感数据
     * @param deviceId 设备ID
     * @param apiTag 传感标识名（可选，多个用逗号分隔，最多50个）
     * @param groupBy 聚合方式（1：按分钟分组聚合 2：按小时分组聚合 3：按天分组聚合 4：按月分组聚合），默认2按小时聚合
     * @param func 聚合函数（与GroupBy配对使用，可以是MAX：按最大值聚合 MIN：按最小值聚合 COUNT：按统计条数聚合），默认MAX最大值聚合
     * @param startDate 起始时间（必填，格式YYYY-MM-DD HH:mm:ss）
     * @param endDate 结束时间（可选，格式YYYY-MM-DD HH:mm:ss）默认取当前时间
     * @param callback 交易回调
     */
    public void grouping(String deviceId, String apiTag,String groupBy, String func, String startDate, String endDate, RetrofitCallBack<BaseResponseEntity<SensorDataRecord>> callback) {
        apiService.getSensorDataGrouping(deviceId, apiTag, groupBy, func, startDate, endDate, accessToken).enqueue(callback);
    }


    /**
     * 模糊查询传感数据
     *
     * @param deviceId  设备ID
     * @param ApiTags   传感标识名（可选，多个用逗号分隔，最多50个）
     * @param Method    查询方式（1：XX分钟内 2：XX小时内 3：XX天内 4：XX周内 5：XX月内 6：按startDate与endDate指定日期查询）
     * @param TimeAgo   与Method一起配对使用（当Method=1~5时），表示以现在起"多长时间范围内"的数据，例：(Method=2,TimeAgo=30)表示现在起30小时内的历史数据
     * @param StartDate 起始时间（可选，格式YYYY-MM-DD HH:mm:ss）
     * @param EndDate   结束时间（可选，格式YYYY-MM-DD HH:mm:ss）
     * @param Sort      时间排序方式，DESC:倒序，ASC升序
     * @param PageSize  指定每次要请求的数据条数，默认20，最多3000
     * @param PageIndex 指定页码
     */
    public void getSensorData(String deviceId, String ApiTags, String Method, String TimeAgo, String StartDate, String EndDate, String Sort, String PageSize, String PageIndex,
                              RetrofitCallBack<BaseResponseEntity<SensorDataRecord>> callback) {
        apiService.getSensorData(deviceId, ApiTags, Method, TimeAgo, StartDate, EndDate, Sort, PageSize, PageIndex, accessToken).enqueue(callback);
    }
}
