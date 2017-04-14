package com.example.rxrxandroidone;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * RxAndroid的工具类
 * 步骤：1.定义被观察者；2.定义订阅者（观察者）；3.订阅者关联被观察者
 */
public class RxAndroidUtil {

    private static final String TAG = "GsonUtils";

    /**
     * 使用create方式
     */
    public static void createObservable() {
        //1.定义被观察者，
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext("hello");
                    subscriber.onNext("hi");
                    subscriber.onNext(downLoadJson());
                    subscriber.onNext("world");
                    subscriber.onCompleted();
                }
            }
        });
		//2.定义订阅者（观察者）
        Subscriber<String> showsub = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, e.getMessage());
            }

            @Override
            public void onNext(String s) {
                Log.i(TAG, "result-->>" + s);
            }
        };
		//3.订阅者关联被观察者
        observable.subscribe(showsub);

    }

    /**
     * 调用下载方法
     *
     * @return
     */
    public static String downLoadJson() {
        return "json data";
    }

    /**
     * create 第二种方式
     */
    public static void createInner() {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    for (int i = 1; i <= 5; i++) {
                        subscriber.onNext(i);
                    }
                    subscriber.onCompleted();
                }
            }
        }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, e.getMessage());
            }

            @Override
            public void onNext(Integer integer) {
                Log.i(TAG, "result-->>" + integer);
            }
        });
    }

    /***
     * 使用在被观察者，返回的对象一般都是数值类型
     */
    public static void from() {
        Integer[] items = {1, 2, 3, 4, 5};
        Observable observable = Observable.from(items);
        observable.subscribe(new Action1() {
            @Override
            public void call(Object o) {
                Log.i(TAG, o.toString());
            }
        });
    }

    /**
     * 指定某一时刻进行数据发送
     */
    public static void interval() {
        Integer[] items = {1, 2, 3, 4, 5};
		//延时1s后执行，持续时间1s
        Observable observable = Observable.interval(1, 1, TimeUnit.SECONDS);//每一个发送数据
        observable.subscribe(new Action1() {
            @Override
            public void call(Object o) {
                Log.i(TAG, o.toString());
            }
        });
    }

    /**
     * 处理数组集合
     */
    public static void just() {
        Integer[] items1 = {1, 2, 3};
        Integer[] items2 = {3, 5, 6};
		//将两个项转换为可观察的对象
		//将两个数组，合并为一个可被观察的数组对象
        Observable observable = Observable.just(items1, items2);
        observable.subscribe(new Subscriber<Integer[]>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, e.getMessage());
            }

            @Override
            public void onNext(Integer[] o) {
                for(int i=0;i<o.length;i++){
                    Log.i(TAG,"next:"+o[i]);
                }
            }
        });
    }

    /**
     * 使用范围数据，指定输出数据的范围
     */
    public  static  void  range(){
		//输出范围为1到40
        Observable observable = Observable.range(6, 10);
        observable.subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, e.getMessage());
            }

            @Override
            public void onNext(Integer o) {
                Log.i(TAG,"next:"+o);
            }
        });
    }

    /**
     * 使用过滤功能
     */
    public static  void filter(){
        Observable observable = Observable.just(1,2,3,4,5,7,8);
        observable.filter(new Func1<Integer,Boolean>() {
            @Override
            public Boolean call(Integer o) {
				//过滤掉大于5的值
                return o>5;
            }
        }).observeOn(Schedulers.io()).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer o) {
                Log.i(TAG,o.toString());
            }
        });
    }
}
