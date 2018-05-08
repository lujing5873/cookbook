package com.zxx.cookbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.zxx.cookbook.R;
import com.zxx.cookbook.bean.User;
import com.zxx.cookbook.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by dell on 2018/5/7.
 */

public class RegisterActivity extends BaseActivity {
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.register_et_name)
    EditText registerEtName;
    @BindView(R.id.register_et_user)
    EditText registerEtUser;
    @BindView(R.id.register_et_pw)
    EditText registerEtPw;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        titleText.setText("注册账号");
    }

    @Override
    public void initData() {

    }


    @OnClick(R.id.title_left)
    public void onTitleLeftClicked() {
        finish();
    }

    @OnClick(R.id.register_ok)
    public void onRegisterOkClicked() {
        String user=registerEtUser.getText().toString();
        String nickName=registerEtName.getText().toString();
        String password=registerEtPw.getText().toString();
        if(TextUtils.isEmpty(user)||user.length()<8||user.length()>16){
            showShortToast("请检查用户名");
            return;
        }
        if(TextUtils.isEmpty(password)||password.length()<8||password.length()>16){
            showShortToast("请检查密码");
            return;
        }
        if(TextUtils.isEmpty(nickName)){
           showShortToast("请检查昵称");
            return;
        }
        User bmobUser=new User();
        bmobUser.setUsername(user);
        bmobUser.setPassword(password);
        bmobUser.setNickName(nickName);
        bmobUser.setRegisterTime(System.currentTimeMillis());
        bmobUser.setUserLevel(2);
        bmobUser.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if(e==null){
                    showShortToast("注册成功");
                    finish();
                }else{
                    showShortToast("注册失败,请更换用户名重试");
                }
            }
        });
    }
}
