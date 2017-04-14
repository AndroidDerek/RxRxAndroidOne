package com.example.rxrxandroidone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button create;
    private LinearLayout activity_main;
    private Button createInner;
    private Button from;
    private Button interval;
    private Button just;
    private Button range;
    private Button filter;
    private TextView log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initView();

    }

    private void initView() {
        create = (Button) findViewById(R.id.create);
        activity_main = (LinearLayout) findViewById(R.id.activity_main);

        create.setOnClickListener(this);
        createInner = (Button) findViewById(R.id.createInner);
        createInner.setOnClickListener(this);
        from = (Button) findViewById(R.id.from);
        from.setOnClickListener(this);
        interval = (Button) findViewById(R.id.interval);
        interval.setOnClickListener(this);
        just = (Button) findViewById(R.id.just);
        just.setOnClickListener(this);
        range = (Button) findViewById(R.id.range);
        range.setOnClickListener(this);
        filter = (Button) findViewById(R.id.filter);
        filter.setOnClickListener(this);
        log = (TextView) findViewById(R.id.log);
        log.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create:
                RxAndroidUtil.createObservable();
                break;
            case R.id.createInner:
                RxAndroidUtil.createInner();
                break;
            case R.id.from:
                RxAndroidUtil.from();
                break;
            case R.id.interval:
                RxAndroidUtil.interval();
                break;
            case R.id.just:
                RxAndroidUtil.just();
                break;
            case R.id.range:
                RxAndroidUtil.range();
                break;
            case R.id.filter:
                RxAndroidUtil.filter();
                break;
        }
    }
}
