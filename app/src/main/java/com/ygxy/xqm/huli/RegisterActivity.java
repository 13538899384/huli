package com.ygxy.xqm.huli;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ygxy.xqm.huli.bean.User;
import com.ygxy.xqm.huli.presenter.UserRegisterPresenter;
import com.ygxy.xqm.huli.view.IUserRegisterView;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * 注册
 * Created by XQM on 2017/3/16.
 */

public class RegisterActivity extends AppCompatActivity implements IUserRegisterView{
    private UserRegisterPresenter registerPresenter = new UserRegisterPresenter(this);
    private ProgressDialog progressDialog;
    public static final String APP_KEY = "1c3036c5342ba";
    public static final String APP_SECRET = "1fb9aa3c046a8e83e934cb98f22ece48";
    private static final int CODE_ING = 1;                  //已发送，倒计时
    private static final int CODE_REPEAT = 2;               //重新发送
    private static final int SMSDDK_HANDLER = 3;            //短信回调
    private int TIME = 60;//倒计时60s

    @BindView(R.id.registered_id)
    EditText mEtUserId;
    @BindView(R.id.registered_phone)
    EditText mEtUserPhone;
    @BindView(R.id.registered_password)
    EditText mEtUserPwd;
    @BindView(R.id.registered_reset_password)
    EditText mEtResetUserPwd;
    @BindView(R.id.registered_code)
    EditText mEtCode;
    @BindView(R.id.registered_toolbar)
    Toolbar toolbar;
    @BindView(R.id.registered_btn_code)
    Button btn_code;
    @BindView(R.id.registered_btn)
    Button btn_registered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registered);
        initSDK();//短信初始化
        ButterKnife.bind(this);
//        btn_code.setClickable(false);
        progressDialog = new ProgressDialog(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //对SMSSDK初始化
    private void initSDK() {
        SMSSDK.initSDK(RegisterActivity.this,APP_KEY,APP_SECRET);
        final EventHandler eventHandler = new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message message = new Message();
                message.arg1 = event;
                message.arg2 = result;
                message.obj = data;
                message.what = SMSDDK_HANDLER;
                mHandler.sendMessage(message);
            }
        };
        // 注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);
    }

    //获取验证码
    @OnClick(R.id.registered_btn_code) void registered_btn_code(){
        if (!TextUtils.isEmpty(mEtUserPhone.getText().toString())){
            btn_code.setClickable(true);
            SMSSDK.getVerificationCode("86",mEtUserPhone.getText().toString());
            btn_code.setClickable(false);
        }
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                for (int i = 60; i > 0; i--)
                {
                    mHandler.sendEmptyMessage(CODE_ING);
                    if (i <= 0) {
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mHandler.sendEmptyMessage(CODE_REPEAT);
            }
        }).start();
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case CODE_ING://已发送，倒计时
                  btn_code.setText("重新发送("+--TIME+"s)");
                    break;
                case CODE_REPEAT://重新发送
                    btn_code.setText("获取验证码");
                    btn_code.setClickable(true);
                    break;
                case SMSDDK_HANDLER:
                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object data = msg.obj;
                    if (result == SMSSDK.RESULT_COMPLETE){
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                            Toast.makeText(RegisterActivity.this, "验证成功", Toast.LENGTH_LONG).show();
                        }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                                Toast.makeText(getApplicationContext(), "验证码已经发送",
                                        Toast.LENGTH_SHORT).show();
                            }else {
                            ((Throwable) data).printStackTrace();
                        }
                    }
                    if(result == SMSSDK.RESULT_ERROR) //返回结果错误时
                    {
                        try {
                            Throwable throwable = (Throwable) data;
                            throwable.printStackTrace();
                            JSONObject object = new JSONObject(throwable.getMessage());
                            String des = object.optString("detail");//错误描述
                            int status = object.optInt("status");//错误代码
                            if (status > 0 && !TextUtils.isEmpty(des)) {
                                Toast.makeText(getApplicationContext(), des, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (Exception e) {
                            //do something
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @OnClick(R.id.registered_btn) void registered_btn(){
        registerPresenter.register();
    }

    @Override
    public String getUseId() {
        return mEtUserId.getText().toString();

    }

    @Override
    public String getUseAccount() {
        return mEtUserPhone.getText().toString();
    }

    @Override
    public String getUsePwd() {
        return mEtUserPwd.getText().toString();
    }

    @Override
    public String getUsePhone() {
        return mEtUserPhone.getText().toString();
    }

    @Override
    public String getUseCode() {
        return mEtCode.getText().toString();
    }

    @Override
    public String getUseResetPwd() {
        return mEtResetUserPwd.getText().toString();
    }

    @Override
    public void showStatus() {
        SMSSDK.submitVerificationCode("86", mEtUserPhone.getText().toString(), mEtCode.getText().toString());//对验证码进行验证->回调函数
        progressDialog.setMessage("正在注册...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    @Override
    public void hideStatus() {
        progressDialog.dismiss();
    }

    @Override
    public void clearAll() {
        mEtUserId.setText("");
        mEtUserPhone.setText("");
        mEtUserPwd.setText("");
    }

    @Override
    public void toLoginActivity(User user) {
        LoginActivity.actionStart(RegisterActivity.this,mEtUserId.getText().toString(),
                mEtUserPwd.getText().toString());
        Toast.makeText(this, "register success", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void showFailed() {
        Toast.makeText(this, "register failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showRegister() {
        btn_registered.setClickable(true);
    }
}
