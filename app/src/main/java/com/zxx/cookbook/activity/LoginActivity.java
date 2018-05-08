package com.zxx.cookbook.activity;

import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

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

public class LoginActivity extends BaseActivity {

    @BindView(R.id.login_et_user)
    EditText loginEtUser;
    @BindView(R.id.login_et_pw)
    EditText loginEtPw;
    @BindView(R.id.login_show_pw)
    CheckBox loginShowPw;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initData() {

    }
    @OnClick(R.id.login_register)
    public  void register(){
        startActivity(new Intent(this,RegisterActivity.class));
    }


    @OnClick(R.id.login_show_pw)
    public void showPassword(){
        if(loginShowPw.isChecked()){
            loginEtPw.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }else{
            loginEtPw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
        loginEtPw.setSelection(loginEtPw.getText().toString().length());
    }

    @OnClick(R.id.login_ok)
    public void onViewClicked() {
        String user=loginEtUser.getText().toString();
        String password=loginEtPw.getText().toString();
        if(TextUtils.isEmpty(user)||user.length()<8||user.length()>16){
            showShortToast("请检查用户名");
            return;
        }
        if(TextUtils.isEmpty(password)||password.length()<8||password.length()>16){
            showShortToast("请检查密码");
            return;
        }
        User bmobUser=new User();
        bmobUser.setUsername(user);
        bmobUser.setPassword(password);
        bmobUser.login(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if(e==null){
                    showShortToast("登录成功:");
                    //通过BmobUser user = BmobUser.getCurrentUser()获取登录成功后的本地用户信息
                    //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(MyUser.class)获取自定义用户信息
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                }else{
                    showShortToast("登录失败,请检查账号密码");
                    System.out.println(e.getMessage());
                }
            }
        });

    }
}
