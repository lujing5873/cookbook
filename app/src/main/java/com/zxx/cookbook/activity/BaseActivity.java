package com.zxx.cookbook.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zxx.cookbook.utils.ToastUtils;

import butterknife.ButterKnife;

/**
 * Created by dell on 2018/5/7.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        initView(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ToastUtils.cancelToast();
    }

    public abstract int getLayoutId();
    public abstract void initView(Bundle savedInstanceState);
    public abstract void initData();
    public  void showShortToast(String toast){
        ToastUtils.showShort(this,toast);
    }
}
