package com.example.ftkj.rxbus_test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.rxbinding.support.v4.widget.RxDrawerLayout;
import com.jakewharton.rxbinding.support.v7.widget.RxPopupMenu;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextSwitcher;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewAfterTextChangeEvent;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func2;

public class FormActivity extends AppCompatActivity {

    private EditText mEt_name;
    private EditText mEt_password;
    private Button mBtn_commit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        mEt_name = (EditText) findViewById(R.id.et_username);
        mEt_password = (EditText) findViewById(R.id.et_password);
        mBtn_commit = (Button) findViewById(R.id.btn_commit);
        RxView.enabled(mBtn_commit).call(false);

        /**
         *  throttleFirst 防止手抖
         */
        RxView.clicks(mBtn_commit)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Toast.makeText(FormActivity.this, ""+"提交!!", Toast.LENGTH_SHORT).show();
                    }
                });

        Observable<CharSequence> observableName = RxTextView.textChanges(mEt_name);
        Observable<CharSequence> observablePassword = RxTextView.textChanges(mEt_password);
        Observable.combineLatest(observableName, observablePassword, new Func2<CharSequence, CharSequence, Boolean>() {
            @Override
            public Boolean call(CharSequence charSequence, CharSequence charSequence2) {
                if (isName(charSequence)&&isPassword(charSequence2)){
                   return true;
                }
                return false;
            }
        }).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                 if (aBoolean){
                     RxView.enabled(mBtn_commit).call(true);
                 }else {
                     RxView.enabled(mBtn_commit).call(false);
                 }
            }
        });
    }

    private boolean isName(CharSequence name) {
        if (name.length()>=3){
            return true;
        }
        return false;
    }

    private boolean isPassword(CharSequence password) {
        if (password.length()>=6){
            return true;
        }
        return false;
    }


}
