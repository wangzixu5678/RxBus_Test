package com.example.ftkj.rxbus_test;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;

public class MyService extends Service {
    static{
        Log.d("AAA", "static initializer: ");
    }
    public MyService() {
        Log.d("AAA", "MyService: ");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("AAA", "onCreate: ");
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        openLooper();
        return START_NOT_STICKY;
    }

    public void openLooper(){
        Observable.interval(0,2,TimeUnit.SECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Log.d("AAA", "call: " + "轮询搞起来" + aLong.toString());

                    }
                });
    }

    @Override
    public void onDestroy() {
        Log.d("AAA", "onDestroy: ");
        super.onDestroy();
    }
}
