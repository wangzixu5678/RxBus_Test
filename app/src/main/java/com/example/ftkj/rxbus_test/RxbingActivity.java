package com.example.ftkj.rxbus_test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

public class RxbingActivity extends AppCompatActivity {

    private Button mButton;
    private Subscription mSubscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxbing);
        mButton = (Button) findViewById(R.id.btn_getcode);

        /**
         * throttleFirst 第一次点击之后的20S点击都无效
         * interval（第一次响应时间，间隔响应时间，相应时间单位）
         * take 取前20次响应 然后结束
         */
        mSubscribe = RxView.clicks(mButton)
                .throttleFirst(20, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        RxView.enabled(mButton).call(false);
                    }
                }).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Observable.interval(0, 1, TimeUnit.SECONDS)
                        .take(20)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Long>() {
                            @Override
                            public void onCompleted() {
                                RxView.enabled(mButton).call(true);
                                RxTextView.text(mButton).call("获取验证码");
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(Long aLong) {
                                Log.d("AAA", "onNext: "+aLong);
                                RxTextView.text(mButton).call(String.format(Locale.CHINA, "剩余%d秒", 20 - aLong));
                            }
                        });
            }

        });


/**
 * 注释掉的这种方法不会调用onComplete() 因为主Rx事件并没有完结
 * 需要在onNext()中判断Button的计时结束
 */

//        RxView.clicks(mButton)
//                .throttleFirst(5, TimeUnit.SECONDS)
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .doOnNext(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//                        RxView.enabled(mButton).call(false);
//                    }
//                }).flatMap(new Func1<Void, Observable<?>>() {
//            @Override
//            public Observable<?> call(Void aVoid) {
//                return Observable.interval(0,1,TimeUnit.SECONDS)
//                        .take(5)
//                        .observeOn(AndroidSchedulers.mainThread());
//            }
//        }).subscribe(new Observer<Object>() {
//            @Override
//            public void onCompleted() {
//                RxView.enabled(mButton).call(true);
//                RxTextView.text(mButton).call("获取验证码");
//                Log.d("AAA", "onCompleted: ");
//            }
//            @Override
//            public void onError(Throwable e) {
//                Log.d("AAA", "onError: "+e.toString());
//            }
//            @Override
//            public void onNext(Object o) {
//                Log.d("AAA", "onNext: "+  o);
//                RxTextView.text(mButton).call(String.format(Locale.CHINA, "剩余%d秒", 5-((Long) o)));
//            }
//        });



    }

    public void btnConcatTest(View view) {
        Observable<String> observable = Observable.just("1", "2", "3");
        Observable<String> observable1 = Observable.just("A", "B", "C");
        Observable.concat(observable, observable1)
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.d("AAA", "onNext: " + s);

                    }
                });
    }

    public void btnConcatConbinelatest(View view) {

        Observable<String> observable = Observable.just("1", "2", "3");
        Observable<String> observable1 = Observable.just("A", "B", "C");
        Observable.combineLatest(observable, observable1, new Func2<String, String, Object>() {
            @Override
            public Object call(String s, String s2) {

                return s + s2;
            }
        }).subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                if (o instanceof String) {
                    Log.d("AAA", "call: " + ((String) o));
                }
            }
        });

    }

    public void btnFlatMapTest(View view) {
        ArrayList<School> schools = new ArrayList<>();
        ArrayList<School.Student> s1l = new ArrayList<>();
        ArrayList<School.Student> s2l = new ArrayList<>();
        ArrayList<School.Student> s3l = new ArrayList<>();

        s1l.add(new School.Student(1L,"王子旭",23));
        s1l.add(new School.Student(2L,"王子九日",23));
        s1l.add(new School.Student(3L,"王子八日",23));
        School s1 = new School(1L, "清华大学", "北京");
        s1.setStudents(s1l);

        s2l.add(new School.Student(4L,"赵迪音",21));
        s2l.add(new School.Student(5L,"赵笛音",21));
        s2l.add(new School.Student(6L,"赵低音",21));
        School s2 = new School(1L, "复旦大学", "上海");
        s2.setStudents(s2l);

        s3l.add(new School.Student(7L,"王小狗",20));
        s3l.add(new School.Student(8L,"王二狗",19));
        s3l.add(new School.Student(9L,"王三狗",18));
        School s3 = new School(1L, "南京大学", "南京");
        s3.setStudents(s3l);

        schools.add(s1);
        schools.add(s2);
        schools.add(s3);

        Observable.from(schools)
                .flatMap(new Func1<School, Observable<School.Student>>() {
                    @Override
                    public Observable<School.Student> call(School school) {
                        return Observable.from(school.getStudents());
                    }
                }).filter(new Func1<School.Student, Boolean>() {
            @Override
            public Boolean call(School.Student student) {
                return student.getName().startsWith("王");
            }
        }).subscribe(new Action1<School.Student>() {
            @Override
            public void call(School.Student student) {
                Log.d("AAA", "call: "+student.getName());
            }
        });

    }

    public void btnFormCommitTest(View view) {
        Intent intent = new Intent();
        intent.setClass(this,com.example.ftkj.rxbus_test.FormActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscribe.unsubscribe();
    }
}
