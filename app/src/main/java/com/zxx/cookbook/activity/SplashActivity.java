package com.zxx.cookbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.zxx.cookbook.R;
import com.zxx.cookbook.bean.User;

import cn.bmob.v3.BmobUser;

/**
 * Created by dell on 2018/5/7.
 */

public class SplashActivity extends BaseActivity {
    private static Handler handler=new Handler();
    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(runnable);
        handler=null;
        super.onDestroy();
    }

    @Override
    public void initData() {
        handler.postDelayed(runnable,3000);
    }
    private Runnable runnable=new Runnable() {
        @Override
        public void run() {
            User bmobUser = BmobUser.getCurrentUser(User.class);
            if(bmobUser != null){
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
            }else{
                //缓存用户对象为空时， 可打开用户注册界面…
                startActivity(new Intent(SplashActivity.this,LoginActivity.class));
            }
        }
    };
}
