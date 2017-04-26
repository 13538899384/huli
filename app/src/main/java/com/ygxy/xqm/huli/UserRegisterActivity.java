package com.ygxy.xqm.huli;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ygxy.xqm.huli.bean.User;
import com.ygxy.xqm.huli.util.OkHttpPostUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.tablemanager.Connector;

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
 * Created by XQM on 2017/3/20.
 */

public class UserRegisterActivity extends AppCompatActivity{
    private ProgressDialog progressDialog;
    public static final String APP_KEY = "1c3036c5342ba";
    public static final String APP_SECRET = "1fb9aa3c046a8e83e934cb98f22ece48";
    private static final int CODE_ING = 1;                  //已发送，倒计时
    private static final int CODE_REPEAT = 2;               //重新发送
    private static final int SMSDDK_HANDLER_REGISTER = 4;            //短信回调
    public static final String PK_TABLE_URL = "http://139.199.155.77:8080/NursingAppServer/SignUpForPk?id=";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private User user;

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
    @BindView(R.id.registered_nickname)
    EditText mEtNickName;
    @BindView(R.id.registered_toolbar)
    Toolbar toolbar;
    @BindView(R.id.registered_btn_code)
    Button btn_code;
    @BindView(R.id.registered_btn)
    Button btn_registered;
    private static final int SMSDDK_HANDLER = 1;            //短信回调

    @OnClick(R.id.registered_btn_code) void registered_btn_code(){
        String phone = mEtUserPhone.getText().toString();
        if (!TextUtils.isEmpty(phone)){
            SMSSDK.getVerificationCode("86", phone, new OnSendMessageHandler() {
                @Override
                public boolean onSendMessage(String s, String s1) {
                    return false;
                }
            });
            //开始计时
            timer.start();
        }else {
            Toast.makeText(UserRegisterActivity.this,"请输入合法的手机号码",Toast.LENGTH_SHORT).show();
        }
    }
    @OnClick(R.id.registered_btn) void registered_btn(){
        String mPhone = mEtUserPhone.getText().toString();
        String mCode = mEtCode.getText().toString();
        if (judgeNull()){
            register(mPhone,mCode);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registered);
        initSDK();//短信初始化
        ButterKnife.bind(this);
        Connector.getDatabase();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
//        btn_code.setClickable(false);
        mEtNickName.setVisibility(View.VISIBLE);
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

    /**
     * 初始化sdk
     */
    private void initSDK() {
        SMSSDK.initSDK(UserRegisterActivity.this,APP_KEY,APP_SECRET);
        final EventHandler eventHandler = new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
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
                message.what = SMSDDK_HANDLER_REGISTER;
                mHandler.sendMessage(message);
            }
        };
        // 注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);
    }

    /**
     * 对用户输入进行限定
     * @return
     */
    public boolean judgeNull(){
        String mId = mEtUserId.getText().toString();
        String mPhone = mEtUserPhone.getText().toString();
        String MPassword = mEtUserPwd.getText().toString();
        String mCode = mEtCode.getText().toString();
        String mResetPassword = mEtResetUserPwd.getText().toString();
        String mNickName = mEtNickName.getText().toString();
        if (TextUtils.isEmpty(mId) && mId.length() != 10){
            Toast.makeText(MyApplication.getContext(),"请输入合法的ID",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (TextUtils.isEmpty(mNickName)){
            Toast.makeText(MyApplication.getContext(),"请输入昵称",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (TextUtils.isEmpty(mPhone) || mPhone.length() != 11){
            Toast.makeText(MyApplication.getContext(),"请输入合法的手机号码",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (TextUtils.isEmpty(MPassword) || MPassword.length() < 6){
            Toast.makeText(MyApplication.getContext(),"至少输入6位密码",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (TextUtils.isEmpty(mResetPassword)
                || MPassword.equals(mResetPassword) == false){
            Toast.makeText(MyApplication.getContext(),"请输入正确的密码",Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(mCode)){
            Toast.makeText(MyApplication.getContext(),"请输入验证码",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 反馈短信回调的结果，提示用户
     */
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SMSDDK_HANDLER_REGISTER:
                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object data = msg.obj;
                    if (result == SMSSDK.RESULT_COMPLETE){
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                            if (!postToBackground()){
                                Toast.makeText(UserRegisterActivity.this, "注册失败", Toast.LENGTH_LONG).show();
                            }
                        }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                            closeRegisterDialog();
                            Toast.makeText(UserRegisterActivity.this, "验证码已经发送", Toast.LENGTH_SHORT).show();
                        }else {
                            closeRegisterDialog();
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
                            closeRegisterDialog();
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
        final String mId = mEtUserId.getText().toString();
        final String mPhone = mEtUserPhone.getText().toString();
        final String mPassword = mEtUserPwd.getText().toString();
        final String mResetPassword = mEtResetUserPwd.getText().toString();
        final String mNickName = mEtNickName.getText().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final OkHttpPostUtil postUtil = new OkHttpPostUtil();
                String body = postUtil.bolwingJson(mId,mNickName,mPhone,mPassword,mResetPassword);
//                Log.d("main",body);
                postUtil.register("http://139.199.220.49:8080/web/register/save", body, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.getMessage();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                closeRegisterDialog();
                                Toast.makeText(UserRegisterActivity.this,"服务器异常，请检查是否联网",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
//                        Log.d("hhh", "结果:" + result);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(result);
                            final String id = jsonObject.optString("studentNumber");
                            final String message = jsonObject.optString("phoneNumber_message");
                            final  String messageId = jsonObject.optString("studentNumber_message");
//                            String nickname = jsonObject.optString("nickname");
//                            Log.d("register",nickname);
//                            Log.d("register",id);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //putEditor();
                                    Log.d("main",message);
                                    if (message.equals("手机号已被使用")){
                                        closeRegisterDialog();
                                        Toast.makeText(UserRegisterActivity.this, "手机号已被使用", Toast.LENGTH_LONG).show();
                                    }else if (messageId.equals("学号已被使用")){
                                        closeRegisterDialog();
                                        Toast.makeText(UserRegisterActivity.this, "学号已被使用", Toast.LENGTH_LONG).show();
                                    }else {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                postUtil.postPkTable(PK_TABLE_URL+id, new Callback() {
                                                    @Override
                                                    public void onFailure(Call call, IOException e) {
                                                        e.getMessage();
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                closeRegisterDialog();
                                                                Toast.makeText(UserRegisterActivity.this, "服务器异常", Toast.LENGTH_LONG).show();
                                                                return;
                                                            }
                                                        });
                                                    }
                                                    @Override
                                                    public void onResponse(Call call, Response response) throws IOException {
                                                        String data = response.body().string();
                                                        Log.d("url:",PK_TABLE_URL+id);
                                                        Log.d("data是",data);
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                closeRegisterDialog();
                                                                Toast.makeText(UserRegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                                                                toLoginActivity(mId,mPassword);
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }).start();
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
//                try {
//                    final String result = postUtil.register("http://139.199.220.49:8080/web/register/save",body);
//                    Log.d("hhh", "结果:" + result);
//                    JSONObject jsonObject = new JSONObject(result);
//                    final String id = jsonObject.optString("studentNumber");
////                    final String passwd = jsonObject.optString("password");
////                    final String phone = jsonObject.optString("phoneNumber");
//                    final String message = jsonObject.optString("phoneNumber_message");
//                    final  String messageId = jsonObject.optString("studentNumber_message");
//                    Log.d("register",id);
////                    Log.d("register",messageId);
////                    Log.d("register",message);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            //putEditor();
//                            Log.d("main",message);
//                            if (message.equals("手机号已被使用")){
//                                closeRegisterDialog();
//                                Toast.makeText(UserRegisterActivity.this, "手机号已被使用", Toast.LENGTH_LONG).show();
//                            }else if (messageId.equals("学号已被使用")){
//                                closeRegisterDialog();
//                                Toast.makeText(UserRegisterActivity.this, "学号已被使用", Toast.LENGTH_LONG).show();
//                            }else {
//                                new Thread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        postUtil.postPkTable(PK_TABLE_URL+id, new Callback() {
//                                            @Override
//                                            public void onFailure(Call call, IOException e) {
//                                                e.getMessage();
//                                                runOnUiThread(new Runnable() {
//                                                    @Override
//                                                    public void run() {
//                                                        closeRegisterDialog();
//                                                        Toast.makeText(UserRegisterActivity.this, "服务器异常", Toast.LENGTH_LONG).show();
//                                                        return;
//                                                    }
//                                                });
//                                            }
//
//                                            @Override
//                                            public void onResponse(Call call, Response response) throws IOException {
//                                                String data = response.body().string();
//                                                Log.d("url:",PK_TABLE_URL+id);
//                                                Log.d("data是",data);
//                                                runOnUiThread(new Runnable() {
//                                                    @Override
//                                                    public void run() {
//                                                        closeRegisterDialog();
//                                                        Toast.makeText(UserRegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
//                                                        toLoginActivity(mId,mPassword);
//                                                    }
//                                                });
//                                            }
//                                        });
//                                    }
//                                }).start();
//                            }
//                        }
//                    });
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        }).start();
        return true;
    }


    /**
     * 开始注册，提交验证码
     * @param phone
     * @param code
     */
    public void register(String phone,String code){
        showRegisterDialog();
        SMSSDK.submitVerificationCode("86",phone,code);
    }

    /**
     * 打开对话框提示用户
     */
    private void showRegisterDialog() {
        progressDialog.setMessage("正在注册");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    /**
     * 关闭对话框
     */
    private void closeRegisterDialog(){
        progressDialog.dismiss();
    }

    /**
     * 限定发送验证码的时间是60秒，
     * 不能重复发送
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
     * 销毁
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册，防止内存泄漏
        SMSSDK.unregisterAllEventHandler();
    }

    /**
     * 注册成功后跳转到登录界面
     */
    public void toLoginActivity(String mId,String mPassword) {
        UserLoginActivity.actionStart(UserRegisterActivity.this,mId
                ,mPassword);
        Toast.makeText(this, "register success", Toast.LENGTH_SHORT).show();
        finish();
    }
}
