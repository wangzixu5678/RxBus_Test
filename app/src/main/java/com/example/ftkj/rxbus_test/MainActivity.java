package com.example.ftkj.rxbus_test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import rx.Subscription;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    private Subscription mSubscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rigistBus();
    }

    public void sendMessage(View view) {
        RxBus.getInstance().post(new StudentEvent("1", "小明明"));
    }

    @Override
    protected void onDestroy() {
        if (!mSubscribe.isUnsubscribed()) {
            mSubscribe.unsubscribe();
        }
        super.onDestroy();
    }

    public void rigistBus() {
        mSubscribe = RxBus.getInstance().toObserverable(StudentEvent.class)
                .subscribe(new Action1<StudentEvent>() {
                    @Override
                    public void call(StudentEvent studentEvent) {
                        Log.d("AAA", "call: " + studentEvent);
                    }
                });
    }

    public void startServices(View view) {
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
    }


    public void goRxActivity(View view) {
        Intent intent = new Intent(this,RxbingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
