package com.ygxy.xqm.huli.presenter;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.ygxy.xqm.huli.MyApplication;
import com.ygxy.xqm.huli.model.IUserBiz;
import com.ygxy.xqm.huli.model.OnLoginListener;
import com.ygxy.xqm.huli.model.UserBiz;
import com.ygxy.xqm.huli.view.IUserLoginView;


/**
 * 连接model和view层进行交互实现
 * Created by XQM on 2017/3/17.
 */

public class UserLoginPresenter {
    private IUserBiz iUserBiz;
    private IUserLoginView loginView;
    private Handler mhandler = new Handler();

    public UserLoginPresenter(IUserLoginView loginView){
        this.loginView = loginView;
        this.iUserBiz = new UserBiz();
    }
    private final static String LOGIN_URL = "http://139.199.220.49:8080/web/login";
    private final static int STUDENTPASSWORD_MESSAGE = 0;
    private final static int STUDENTID_MESSAGE = 1;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case STUDENTID_MESSAGE:
                    Toast.makeText(MyApplication.getContext(),"学号不存在",Toast.LENGTH_SHORT).show();
                    break;
                case STUDENTPASSWORD_MESSAGE:
                    Toast.makeText(MyApplication.getContext(),"密码不存在",Toast.LENGTH_SHORT).show();
                    break;
                default:break;
            }
        }
    };

    public void login(){
        if (TextUtils.isEmpty(loginView.getUserName())){
            Toast.makeText(MyApplication.getContext(),"用户名不能为空",Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(loginView.getPassword())){
            Toast.makeText(MyApplication.getContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
//        else if (loginView != null){
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    Message message = new Message();
//                    Bundle bundle = new Bundle();
//                    OkHttpPostUtil postUtil = new OkHttpPostUtil();
//                    String body = postUtil.loginJson(loginView.getUserName(),loginView.getPassword());
//                    String result = null;
//                    try {
//                        result = postUtil.register(LOGIN_URL,body);
//                        JSONObject jsonObject = new JSONObject(result);
//                        //解析数据
//
//                        String mId = jsonObject.optString("studentNumber");
//                        String mPassword = jsonObject.optString("password");
//                        String messageId = jsonObject.optString("studentNumber_message");
//                        String messagePassword = jsonObject.optString("password_message");
//                        bundle.putString("id",messageId);
//                        bundle.putString("password",messagePassword);
//                        message.setData(bundle);
//                        if (messageId.equals("学号不存在")){
//                            message.what = STUDENTID_MESSAGE;
//                            mhandler.sendMessage(message);
//                        }else if (messagePassword.equals("密码不存在")){
//                            message.what = STUDENTPASSWORD_MESSAGE;
//                            mhandler.sendMessage(message);
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
//        }
        else {
            loginView.showDialog();
            iUserBiz.login(loginView.getUserName(), loginView.getPassword(), new OnLoginListener() {
                @Override
                public void loginSuccess() {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            loginView.hideDialog();
                            loginView.toMainActivity();

                        }
                    });
                }

                @Override
                public void loginFailed() {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            loginView.showFailedError();
                            loginView.hideDialog();
                        }
                    });
                }
            });
        }
    }


    public void clear()
    {
        loginView.clearUserName();
        loginView.clearPassword();
    }

    public void setVisibity(){

    }
}
