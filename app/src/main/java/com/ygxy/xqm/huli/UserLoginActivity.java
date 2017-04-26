package com.ygxy.xqm.huli;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ygxy.xqm.huli.util.OkHttpPostUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 用户登录
 * Created by XQM on 2017/3/25.
 */

public class UserLoginActivity extends AppCompatActivity implements NetworkChangeRecevier.OnNetworkChangeListener{
    @BindView(R.id.login_account)
    EditText mEtUsername;
    @BindView(R.id.login_pwd)
    EditText mEtPassword;
    @BindView(R.id.login_ll)
    LinearLayout mLogin_ll;
    @BindView(R.id.login_splash)
    LinearLayout mLogin_splash;
    private ProgressDialog progressDialog;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private ExitReceiver exitReceiver = new ExitReceiver();
    private IntentFilter intentFilter;
    public final static String LOGIN_URL = "http://139.199.220.49:8080/web/login";
    public final static String EXITACTION = "action.exit";
    private final static int STUDENT_PASSWORD_MESSAGE = 0;
    private final static int STUDENT_ID_MESSAGE = 1;
    private final static int STUDENT_LOGIN_SUCCESS = 2;
    private NetworkChangeRecevier networkChangeRecevier;
    private AlertDialog.Builder builder;
    private Boolean login_status;
    private String mToken;
    private String mId;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case STUDENT_ID_MESSAGE:
                    Toast.makeText(UserLoginActivity.this,"学号不存在",Toast.LENGTH_SHORT).show();
                    hideProgressDialog();
                    break;
                case STUDENT_PASSWORD_MESSAGE:
                    Toast.makeText(UserLoginActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
                    hideProgressDialog();
                    break;
                case STUDENT_LOGIN_SUCCESS:
                    Toast.makeText(UserLoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                    MainActivity.actionStart(UserLoginActivity.this);
                    hideProgressDialog();
                default:break;
            }
        }
    };

    @OnClick(R.id.btn_login)
    void btn_login() {
        if (judgeNull()){
            showProgressDialog();
            login();
        }
    }

    private boolean judgeNull() {
        if (TextUtils.isEmpty(mEtUsername.getText().toString())){
            Toast.makeText(UserLoginActivity.this,"请输入的学号",Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(mEtPassword.getText().toString())){
            Toast.makeText(UserLoginActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void showProgressDialog() {
        progressDialog.setMessage("正在登录...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    private void login() {
        final String mUsername = mEtUsername.getText().toString();
        final String mUserPassword = mEtPassword.getText().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
//                    queryFromBackGround(mUsername, mUserPassword);
//                    先从本地查找是否有用户登录的信息
                    Boolean login_status = preferences.getBoolean("login_status",false);
                    if (login_status == true){
                        MainActivity.actionStart(UserLoginActivity.this);
                        Log.d("login", String.valueOf(login_status));
                        finish();
                    }else {
                        //没有就从服务器访问服务器
                        queryFromBackGround(mUsername, mUserPassword);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    /**
     * 访问服务器
     * @param mUsername
     * @param mUserPassword
     */
    private void queryFromBackGround(final String mUsername, final String mUserPassword) {

        OkHttpPostUtil postUtil = new OkHttpPostUtil();
        String body = postUtil.loginJson(mUsername,mUserPassword);
        postUtil.register(LOGIN_URL, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.getMessage();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressDialog();
                        Toast.makeText(UserLoginActivity.this,"服务器异常，请检查是否联网",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                //解析数据
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("user");
                    final String mId = jsonObject1.optString("studentNumber");
                    final JSONObject finalJsonObject = jsonObject;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mId.equals(mUsername)){
                                //preference = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
                                mToken = finalJsonObject.optString("token");
                                editor.putBoolean("login_status",true);
                                editor.putString("login_token",mToken);
                                editor.putString("login_id",mId);
                                editor.apply();

                                Log.d("token",mToken);
                                Log.d("id",mId);
                                hideProgressDialog();
                                //Log.d("login","成功");
                                //Toast.makeText(UserLoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                                MainActivity.actionStart(UserLoginActivity.this);
                                finish();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    final String messagePassword = jsonObject.optString("Login_message");
                    final String messageId = jsonObject.optString("studentNumber_message");
                    Log.d("error",messageId);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideProgressDialog();
                            if (messageId.equals("学号不存在")){
                                Toast.makeText(UserLoginActivity.this,"学号不存在",Toast.LENGTH_SHORT).show();
                            }else if (messagePassword.equals("密码错误")){
                                Toast.makeText(UserLoginActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
    }

    /**
     * 隐藏对话框
     */
    private void hideProgressDialog() {
        progressDialog.dismiss();
    }

    @OnClick(R.id.registered)
    void registered() {
        Intent intent = new Intent(UserLoginActivity.this, UserRegisterActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.forget_password)
    void forget_password() {
        Intent intent = new Intent(UserLoginActivity.this, ForgetPwdActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.bind(this);
        mLogin_ll.setVisibility(View.GONE);
        mLogin_splash.setVisibility(View.VISIBLE);
        progressDialog = new ProgressDialog(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        intentFilter = new IntentFilter();
        networkChangeRecevier = new NetworkChangeRecevier();
        networkChangeRecevier.setOnNetworkChangeListener(this);
        autoLogin();
    }

    @Override
    protected void onResume() {
        super.onResume();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(networkChangeRecevier,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (networkChangeRecevier != null){
            this.unregisterReceiver(networkChangeRecevier);
        }
    }

    /**
     *  检测用户是否登录过，如果登录过一次，
     * 则不需要再次进行请求服务器登录
     */
    private void autoLogin() {
        login_status = preferences.getBoolean("login_status",false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    if (login_status == true){
                        MainActivity.actionStart(UserLoginActivity.this);
//                        Log.d("自动登录", String.valueOf(login_status));
                        finish();
                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mLogin_ll.setVisibility(View.VISIBLE);
                                mLogin_splash.setVisibility(View.GONE);
//                                Log.d("自动登录failed", String.valueOf(login_status));
                            }
                        });

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    /**
     *如果为新用户需要跳转到登录界面进行登录
     * @param context
     * @param mId
     * @param mPassword
     */
    public static void actionStart(Context context, String mId, String mPassword) {
        Intent intent = new Intent(context, UserLoginActivity.class);
        intent.putExtra("id", mId);
        intent.putExtra("mPassword", mPassword);
        context.startActivity(intent);
    }

    /**
     * 返回用户登录的token
     * @param preferences
     * @return
     */
    public String returnToken(SharedPreferences preferences){
        mToken = preferences.getString("login_token","");
//        Log.d("backToken",mToken);
        return mToken;
    }

    /**
     * 返回用户登录的学号
     * @param preferences
     * @return
     */
    public String returnId(SharedPreferences preferences){
        mId = preferences.getString("login_id","");
//        Log.d("login_id",mId);
        return mId;
    }

    /**
     * 处理网络变化的回调
     */
    @Override
    public void onChange() {
        builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("网络异常");
        builder.setMessage("网络连接异常");
        builder.setCancelable(true);
        builder.setNegativeButton("我知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        // 需要设置 AlertDialog的类型，保证在广播接收器中可以正常弹出
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private class ExitReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            UserLoginActivity.this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(exitReceiver);
    }
}