package com.example.rxrxandroidone;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * RxAndroid结合OkHttp实现图片下载、get、post请求
 */
public class RxAndroidOkhttp {
    private final String TAG = "GsonUtils";
    private OkHttpClient client;

    private RxAndroidOkhttp() {
        //创建okhttp对象
        client = new OkHttpClient();
        //设置连接超时时间为10s
        client.newBuilder().connectTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
    }

    //创建私有的本类对象
    private static RxAndroidOkhttp rxAndroidOkhttp;

    //创建 单例模式（OkHttp官方建议如此操作）
    public static RxAndroidOkhttp getInstance() {
        //对象问空才创建
        if (null == rxAndroidOkhttp) {
            //同步锁解决多线程问题
            synchronized (RxAndroidOkhttp.class) {
                rxAndroidOkhttp = new RxAndroidOkhttp();
            }
        }
        return rxAndroidOkhttp;
    }

    /**
     * 使用get进行请求,返回服务器传递过来的字符串
     *
     * @param url
     * @return
     */
    public Observable<String> get(String url) {
        //创建被观察者
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                //在没有取消订阅的时候
                if (!subscriber.isUnsubscribed()) {
                    //构建get请求
                    Request request = new Request.Builder().url(url).get().build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            //通知订阅者的错误信息
                            subscriber.onError(e);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (null != response && response.isSuccessful()) {
                                ///通知订阅者的成功信息
                                subscriber.onNext(response.body().string());
                            }
                            //通知完毕
                            subscriber.onCompleted();
                        }
                    });
                }
            }
        });
        return observable;
    }


    /**
     * 使用post请求，返回服务器传递过来的字符串
     *
     * @param url
     * @param params
     * @return
     */
    public Observable<String> post(String url, Map<String, String> params) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    FormBody.Builder builder = new FormBody.Builder();
                    if (params != null && !params.isEmpty()) {
                        for (Map.Entry<String, String> entry : params.entrySet()) {
                            builder.add(entry.getKey(), entry.getValue());
                        }
                    }
                    RequestBody requestBody = builder.build();
                    //构建post请求
                    Request request = new Request.Builder().url(url).post(requestBody).build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            subscriber.onError(e);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (null != response && response.isSuccessful()) {
                                subscriber.onNext(response.body().string());
                            }
                            subscriber.onCompleted();//访问结束
                        }
                    });
                }
            }
        });
    }

    /**
     * 声明一个被观察者对象，作为结果返回
     * 返回图形的字节数组
     *
     * @param path
     * @return
     */
    public Observable<byte[]> downLoadImage(String path) {
        return Observable.create(new Observable.OnSubscribe<byte[]>() {
            @Override
            public void call(Subscriber<? super byte[]> subscriber) {
                //如果没有订阅
                if (!subscriber.isUnsubscribed()) {
                    //访问网络操作
                    Request request = new Request.Builder().url(path).build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            subscriber.onError(e);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (null != response && response.isSuccessful()) {
                                byte[] data = response.body().bytes();
                                if (data != null) {
                                    subscriber.onNext(data);
                                }
                            }
                            subscriber.onCompleted();
                        }
                    });

                }
            }
        });
    }

}
