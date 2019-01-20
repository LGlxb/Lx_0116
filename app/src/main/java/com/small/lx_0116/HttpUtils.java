package com.small.lx_0116;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/***
 * ok网络请求工具类
 */
public class HttpUtils {
    //提供一个本地HttpUtils的引用
    public static HttpUtils httpUtils;
    private final OkHttpClient okHttpClient;
    private final Handler handler;

    //私有化构造函数
    private HttpUtils() {
        //主线程handler
        handler = new Handler(Looper.getMainLooper());
        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(5000, TimeUnit.MILLISECONDS)
                .writeTimeout(5000, TimeUnit.MILLISECONDS)
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                .connectionPool(new ConnectionPool(5, 1, TimeUnit.SECONDS))
                .build();
    }

    //提供公有方法供外部类访问
    public static HttpUtils getHttpUtils() {
        //Dcl模式的懒汉式
        if (httpUtils == null) {
            synchronized (HttpUtils.class) {//线程锁---保证获取对象唯一
                if (httpUtils == null) {
                    return httpUtils = new HttpUtils();
                }
            }
        }
        return httpUtils;
    }

    //异步get请求--doGet
    public void doGet(String url, final IOKHttpUtilsCallBack iokHttpUtilsCallBack) {
        Request request = new Request.Builder().url(url).get().build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (iokHttpUtilsCallBack != null) {
                    iokHttpUtilsCallBack.onFailure(e.getMessage());
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    final String json = response.body().string();
                    if (iokHttpUtilsCallBack != null) {
                        //切换到主线程
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                iokHttpUtilsCallBack.onResponse(json);
                            }
                        });
                    }
                } else {
                    if (iokHttpUtilsCallBack != null) {
                        //切换到主线程
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                iokHttpUtilsCallBack.onFailure("网络异常");
                            }
                        });
                    }
                }
            }
        });
    }

    //接口回调
    public interface IOKHttpUtilsCallBack {
        void onFailure(String error);

        void onResponse(String json);
    }
}
