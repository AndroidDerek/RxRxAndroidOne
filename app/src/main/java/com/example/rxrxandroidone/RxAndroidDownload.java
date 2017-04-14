package com.example.rxrxandroidone;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/4/13 0013.
 *
 * 步骤：1.定义被观察者；2.定义订阅者（观察者）；3.订阅者关联被观察者
 */

public class RxAndroidDownload extends AppCompatActivity implements View.OnClickListener {
    private Button download_image;
    private ImageView imageView;
    private static final String TAG = "GsonUtils";
    private Button post;
    private Button get;
    private TextView return_message;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rxandroiddownload);
        initView();

    }

    private void initView() {
        download_image = (Button) findViewById(R.id.download_image);
        imageView = (ImageView) findViewById(R.id.imageView);

        download_image.setOnClickListener(this);
        post = (Button) findViewById(R.id.post);
        post.setOnClickListener(this);
        get = (Button) findViewById(R.id.get);
        get.setOnClickListener(this);
        return_message = (TextView) findViewById(R.id.return_message);
        return_message.setOnClickListener(this);
    }

    String postPath = "http://fanyi.youdao.com/openapi.do";
    /**
     * 有道词典提供的api查询接口,q=后面拼接的是需要查询的单词
     * 参考网站
     * 例如：查询words，对应的http://fanyi.youdao.com/openapi.do?keyfrom=imoocdict123456&key=324273592
     * &type=data&doctype=json&version=1.1&q=words
     */
    String getPath = "http://fanyi.youdao.com/openapi.do?keyfrom=imoocdict123456&key=324273592&" +
            "type=data&doctype=json&version=1.1&q=blue";

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.download_image:
                Log.d(TAG, "download_image");
                //下载图片
                RxAndroidOkhttp rxAndroidOkhttp1 = RxAndroidOkhttp.getInstance();
                /**
                 * 1.observable定义被观察者
                 */
                Observable<byte[]> observable = rxAndroidOkhttp1.downLoadImage("https://www.baidu.com/img/bd_logo1.png");
                /**
                 * 2.定义订阅者（观察者）
                 */
                Subscriber<byte[]> subscriber = new Subscriber<byte[]>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(byte[] bytes) {
                        /**
                         * 订阅者收到变化的字节信息
                         */
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        //在主线程中更新ui
                        /*RxAndroidDownload.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(bitmap);
                            }
                        });*/
                        //展示图片
                        imageView.setImageBitmap(bitmap);
                    }
                };
                /**
                 * 3.订阅者关联被观察者
                 */
                /**
                 * Schedulers.io()说明是输入输出的计划任务
                 * AndroidSchedulers.mainThread()说明订阅者是中ui主线程中执行
                 */
                observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
                break;
            case R.id.post:
                Log.d(TAG, "post");
                //使用post请求，返回服务器传递过来的字符串
                RxAndroidOkhttp rxAndroidOkhttp2 = RxAndroidOkhttp.getInstance();
                Map<String, String> params = new HashMap<>();
                params.put("keyfrom", "imoocdict123456");
                params.put("key", "324273592");
                params.put("type", "data");
                params.put("doctype", "json");
                params.put("version", "1.1");
                params.put("q", "red");
                Observable<String> observable1 = rxAndroidOkhttp2.post(postPath, params);
                Subscriber<String> subscriber1 = new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(TAG, "s=" + s);
                        return_message.setTextColor(Color.RED);
                        return_message.setText("服务器post返回的数据:" + s);
                    }
                };
                observable1.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber1);

                break;
            case R.id.get:
                Log.d(TAG, "get");
                RxAndroidOkhttp rxAndroidOkhttp3 = RxAndroidOkhttp.getInstance();
                // 使用get进行请求
                Observable<String> observable3 = rxAndroidOkhttp3.get(getPath);
                Subscriber<String> subscriber3 = new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(TAG, "s=" + s);
                        return_message.setTextColor(Color.BLUE);
                        return_message.setText("服务器get返回的数据:" + s);
                    }
                };
                observable3.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber3);

                break;
        }
    }

}
