package com.ygxy.xqm.huli;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.ygxy.xqm.huli.presenter.UserLoginPresenter;
import com.ygxy.xqm.huli.view.IUserLoginView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登录
 * Created by XQM on 2017/3/16.
 */

public class LoginActivity extends AppCompatActivity implements IUserLoginView {
    @BindView(R.id.login_account)
    EditText mEtUsername;
    @BindView(R.id.login_pwd)
    EditText mEtPassword;
    private ProgressDialog progressDialog;
    private UserLoginPresenter mUserLoginPresenter = new UserLoginPresenter(this);

    @OnClick(R.id.btn_login) void btn_login(){
        mUserLoginPresenter.login();
    }

    @OnClick(R.id.registered) void registered(){
        Intent intent = new Intent(LoginActivity.this,UserRegisterActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.forget_password) void forget_password(){
        Intent intent = new Intent(LoginActivity.this,ForgetPwdActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   //设置屏幕常亮
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.login);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
    }

    public static void actionStart(Context context,String mId,String mPassword){
        Intent intent = new Intent(context,LoginActivity.class);
        intent.putExtra("id",mId);
        intent.putExtra("mPassword",mPassword);
        context.startActivity(intent);
    }

    @Override
    public String getUserName() {
        return mEtUsername.getText().toString();
    }

    @Override
    public String getPassword() {
        return mEtPassword.getText().toString();
    }

    @Override
    public void clearUserName() {
        mEtUsername.setText("");
    }

    @Override
    public void clearPassword() {
        mEtPassword.setText("");
    }

    @Override
    public void showDialog() {
        progressDialog.setMessage("正在登录...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    @Override
    public void hideDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void toMainActivity() {
        MainActivity.actionStart(LoginActivity.this);
        Toast.makeText(this, "login success", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void showFailedError() {
        Toast.makeText(this, "用户名或密码有误", Toast.LENGTH_SHORT).show();
    }
}
