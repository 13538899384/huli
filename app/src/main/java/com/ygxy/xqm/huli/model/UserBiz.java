package com.ygxy.xqm.huli.model;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.ygxy.xqm.huli.MyApplication;
import com.ygxy.xqm.huli.bean.User;
import com.ygxy.xqm.huli.util.OkHttpPostUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 实现IUserBiz登录耗时操作
 * Created by XQM on 2017/3/17.
 */

public class UserBiz implements IUserBiz {
    private List<User> userList;
    private User user = new User();
    private final static String LOGIN_URL = "http://139.199.220.49:8080/web/login";
    private final static int STUDENTPASSWORD_MESSAGE = 0;
    private final static int STUDENTID_MESSAGE = 1;
    private SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
    private SharedPreferences.Editor editor;

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
    @Override
    public void login(final String username, final String password, final OnLoginListener loginListener) {
        new Thread(new Runnable() {
            //模拟登录耗时操作
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    String editorUsername = preference.getString("username","");
                    String editorPassword = preference.getString("password","");
//                    if (editorUsername.equals(username) && editorPassword.equals(password)){
//                        MainActivity.actionStart(MyApplication.getContext());
//                        Log.d("login",editorPassword);
//                    }else {
                        queryFromBackGround(username, password, loginListener);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void queryFromBackGround(String username,String password,final OnLoginListener loginListener) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        OkHttpPostUtil postUtil = new OkHttpPostUtil();
        String body = postUtil.loginJson(username,password);
        String result = null;
        try {
//            result = postUtil.register(LOGIN_URL,body);
            //解析数据
            JSONObject jsonObject = new JSONObject(result);
            String mId = jsonObject.optString("studentNumber");
            String mPassword = jsonObject.optString("password");
            String messageId = jsonObject.optString("studentNumber_message");
            String messagePassword = jsonObject.optString("password_message");

            Log.d("login",messageId);
            bundle.putString("id",messageId);
            bundle.putString("password",messagePassword);
            message.setData(bundle);
//                    if (messageId.equals("学号不存在")){
//                        message.what = STUDENTID_MESSAGE;
//                        mHandler.sendMessage(message);
//                    }else if (messagePassword.equals("密码不存在")){
//                        message.what = STUDENTPASSWORD_MESSAGE;
//                        mHandler.sendMessage(message);
//                    }else {
//
//                    }
            if (mId.equals(username) && mPassword.equals(password)){
                //preference = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
                editor = preference.edit();
                editor.putString("username",username);
                editor.putString("password",password);
                editor.apply();
                loginListener.loginSuccess();
            }else {
                loginListener.loginFailed();
            }
        }  catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void query(){

    }
}
