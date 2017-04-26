package com.ygxy.xqm.huli.model;

import android.text.TextUtils;

import com.ygxy.xqm.huli.bean.User;

/**
 * 实现注册接口
 * Created by XQM on 2017/3/17.
 */

public class UserRegister implements IUserRegister {
    private User user = new User();
    @Override
    public void register(final String userid, final String userphone, final String userpwd, final OnRegisterListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (!TextUtils.isEmpty(userid) && !TextUtils.isEmpty(userphone) && !TextUtils.isEmpty(userpwd)){
                    //user.setId(userid);
                    user.setPhone(userphone);
                    user.setPassword(userpwd);
//                    user.save();
                    listener.registerSuccess(user);
                }else {
                    listener.registerFailed();
                }
            }
        }).start();
    }
}
