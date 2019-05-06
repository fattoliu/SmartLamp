package com.smart.lamp.net.util;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * TODO  retrofit callback
 *
 * @author fattoliu
 * @version V 1.0
 * @date on 5/3/2019 19:24 PM
 */
public abstract class RetrofitCallBack<T> implements Callback<T> {
    private Context context;
    private Handler handler;

    protected RetrofitCallBack(Context context) {
        this.context = context;
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onResponse(final Call<T> call, final Response<T> response) {
        handler.post(() -> {
            if (response.body() == null) {
                onFailure(call, new Throwable("服务器异常..."));
            } else {
                T t = response.body();
                onResponse(t);
            }
        });
    }

    protected abstract void onResponse(T response);

    @Override
    public void onFailure(Call<T> call, final Throwable t) {
        handler.post(() -> {
            if (t.getClass() == SocketTimeoutException.class) {
                Toast.makeText(context.getApplicationContext(),
                        "连接超时，请检查网络!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context.getApplicationContext(),
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
