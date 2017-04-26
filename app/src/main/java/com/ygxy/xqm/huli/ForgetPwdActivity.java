package com.ygxy.xqm.huli;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ygxy.xqm.huli.util.OkHttpPostUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by XQM on 2017/3/17.
 */

public class ForgetPwdActivity extends AppCompatActivity{
    @BindView(R.id.forget_password_toolbar)
    Toolbar toolbar;
    @BindView(R.id.forget_id)
    EditText mEtForgetId;
    @BindView(R.id.forget_password)
    EditText mEtForgetPassword;
    @BindView(R.id.reset_forget_password)
    EditText mEtResetForgetPassword;
    @BindView(R.id.forget_password_phone)
    EditText mEtForgetPhone;
    @BindView(R.id.forget_password_code)
    EditText mEtForget_code;
    @BindView(R.id.forget_password_btn_code)
    Button btn_code;
    private ProgressDialog dialog;
    public static final int SMSDDK_HANDLER_MESSAGE = 1;//信息回调
    public static final String STUDENT_FORGET_PASSWORD = "http://139.199.220.49:8080/web/modifyPassword";
    @OnClick(R.id.forget_password_btn_code) void forget_password_btn_code(){
        String phone = mEtForgetPhone.getText().toString();
        if (!TextUtils.isEmpty(phone)){
            SMSSDK.getVerificationCode("86", phone, new OnSendMessageHandler() {
                @Override
                public boolean onSendMessage(String s, String s1) {
                    return false;
                }
            });
            timer.start();
        }else {
            Toast.makeText(ForgetPwdActivity.this,"请输入合法的手机号码",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 判断用户输入的信息
     * @return
     */
    private boolean judgeNull() {
        String mId = mEtForgetId.getText().toString();
        String mPhone = mEtForgetPhone.getText().toString();
        String mPassword = mEtForgetPassword.getText().toString();
        String mCode = mEtForget_code.getText().toString();
        String mResetPassword = mEtResetForgetPassword.getText().toString();
        if (TextUtils.isEmpty(mId) || mId.length() != 10){
            Toast.makeText(ForgetPwdActivity.this,"请输入合法的学号",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (TextUtils.isEmpty(mPassword) || mPassword.length() < 6){
            Toast.makeText(MyApplication.getContext(),"至少输入6位密码",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (TextUtils.isEmpty(mResetPassword)
                || mResetPassword.equals(mPassword) == false){
            Toast.makeText(MyApplication.getContext(),"请输入正确的密码",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (TextUtils.isEmpty(mPhone) && mPhone.length() != 11){
            Toast.makeText(ForgetPwdActivity.this,"请输入合法的手机号码",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (TextUtils.isEmpty(mCode)){
            Toast.makeText(MyApplication.getContext(),"请输入验证码",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @OnClick(R.id.forget_password_btn) void forget_password_btn(){
        String mPhone = mEtForgetPhone.getText().toString();
        String mCode = mEtForget_code.getText().toString();
        if (judgeNull()){
            submit(mPhone,mCode);
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_pwd);
        ButterKnife.bind(this);
        initSDK();//短信初始化
        dialog = new ProgressDialog(this);
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

    /**
     * 更新UI
     */
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SMSDDK_HANDLER_MESSAGE:
                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object data = msg.obj;
                    if (result == SMSSDK.RESULT_COMPLETE){
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                            if (!postToBackground()){
                                Toast.makeText(ForgetPwdActivity.this, "更改密码失败", Toast.LENGTH_LONG).show();
                            }
                        }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                            closeDialog();
                            Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();
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
                            closeDialog();
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

    /**
     * post服务器，写入用户信息
     * @return
     */
    private boolean postToBackground() {
        final String mId = mEtForgetId.getText().toString();
        final String mPhone = mEtForgetPhone.getText().toString();
        final String mPassword = mEtForgetPassword.getText().toString();
        String mCode = mEtForget_code.getText().toString();
        final String mResetPassword = mEtResetForgetPassword.getText().toString();
        OkHttpPostUtil postUtil = new OkHttpPostUtil();
        String body = postUtil.forgetwordJson(mId,mPhone,mPassword,mResetPassword);
        postUtil.register(STUDENT_FORGET_PASSWORD, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.getMessage();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeDialog();
                        Toast.makeText(ForgetPwdActivity.this,"服务器异常，请检查是否联网",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d("hhh", "结果:" + result);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    final String id = jsonObject.optString("studentNumber");
                    final String passwd = jsonObject.optString("password");
                    final String phone = jsonObject.optString("phoneNumber");
                    final String message = jsonObject.optString("error_message");
                    final String success_message = jsonObject.optString("success_message");
                    final String password_message = jsonObject.optString("password_message");
                    Log.d("forget",id);
                    Log.d("forget",passwd);
                    Log.d("forget",phone);
                    Log.d("forget",message);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //putEditor();
                            Log.d("main",message);
                            if (message.equals("学号或手机号输入错误")){
                                closeDialog();
                                Toast.makeText(ForgetPwdActivity.this, "学号或手机号输入错误", Toast.LENGTH_LONG).show();
                            }else if (password_message.equals("两次输入的密码不相同")){
                                closeDialog();
                                Toast.makeText(ForgetPwdActivity.this, password_message, Toast.LENGTH_LONG).show();
                            }else if (success_message.equals("密码修改成功")){
                                closeDialog();
                                Toast.makeText(ForgetPwdActivity.this, "密码修改成功", Toast.LENGTH_LONG).show();
                                toLoginActivity(mId,mPassword);
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                OkHttpPostUtil postUtil = new OkHttpPostUtil();
//                String body = postUtil.bolwingJson(mId,mPhone,mPassword,mResetPassword);
////                Log.d("main",body);
//                try {
//                    final String result = postUtil.register(STUDENT_FORGET_PASSWORD,body);
//                    Log.d("hhh", "结果:" + result);
//                    JSONObject jsonObject = new JSONObject(result);
//                    final String id = jsonObject.optString("studentNumber");
//                    final String passwd = jsonObject.optString("password");
//                    final String phone = jsonObject.optString("phoneNumber");
//                    final String message = jsonObject.optString("error_message");
//                    final String success_message = jsonObject.optString("success_message");
//                    final String password_message = jsonObject.optString("password_message");
//                    Log.d("forget",id);
//                    Log.d("forget",passwd);
//                    Log.d("forget",phone);
//                    Log.d("forget",message);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            //putEditor();
//                            Log.d("main",message);
//                            if (message.equals("学号或手机号输入错误")){
//                                closeDialog();
//                                Toast.makeText(ForgetPwdActivity.this, "学号或手机号输入错误", Toast.LENGTH_LONG).show();
//                            }else if (password_message.equals("两次输入的密码不相同")){
//                                closeDialog();
//                                Toast.makeText(ForgetPwdActivity.this, password_message, Toast.LENGTH_LONG).show();
//                            }else if (success_message.equals("密码修改成功")){
//                                closeDialog();
//                                Toast.makeText(ForgetPwdActivity.this, "密码修改成功", Toast.LENGTH_LONG).show();
//                                toLoginActivity(mId,mPassword);
//                            }
//                        }
//                    });
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
        return true;
    }


    /**
     * 重新登录
     * @param mId
     * @param mPassword
     */
    private void toLoginActivity(String mId, String mPassword) {
        Intent intent = new Intent(ForgetPwdActivity.this,UserLoginActivity.class);
        intent.putExtra("id",mId);
        intent.putExtra("password",mPassword);
        startActivity(intent);
    }

    /**
     * 初始化SMSSDK
     */
    private void initSDK() {
        SMSSDK.initSDK(this,UserRegisterActivity.APP_KEY,UserRegisterActivity.APP_SECRET);
        EventHandler handler = new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                super.afterEvent(event, result, data);
                Message message = new Message();
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        Log.e("user","提交验证码成功");

                    }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                        //获取验证码成功
                        Log.e("user","获取验证码成功");
                    }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                        //返回支持发送验证码的国家列表
                    }
                }else{
                    ((Throwable)data).printStackTrace();
                }
                message.arg1 = event;
                message.arg2 = result;
                message.obj = data;
                message.what = SMSDDK_HANDLER_MESSAGE;
                mHandler.sendMessage(message);
            }
        };
        //注册回调
        SMSSDK.registerEventHandler(handler);
    }

    /**
     * 提交验证码
     * @param phone
     * @param code
     */
    public void submit(String phone,String code){
        showDialog();
        SMSSDK.submitVerificationCode("86",phone,code);
    }

    /**
     * 显示提交对话框
     */
    public void showDialog(){
        dialog.setMessage("正在提交");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /**
     * 60秒计时器
     */
    private CountDownTimer timer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            btn_code.setText((millisUntilFinished / 1000) + "秒后可重发");
            btn_code.setEnabled(false);
        }

        @Override
        public void onFinish() {
            btn_code.setEnabled(true);
            btn_code.setText("获取验证码");
        }
    };

    /**
     * 关闭对话框
     */
    public void closeDialog(){
        dialog.dismiss();
    }

    /**
     * 销毁活动
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();//解除回调，防止内存泄漏
    }
}
