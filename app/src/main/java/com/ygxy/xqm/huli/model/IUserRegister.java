package com.ygxy.xqm.huli.model;

/**
 * 用户注册接口
 * Created by XQM on 2017/3/17.
 */

public interface IUserRegister {
    void register(String userid, String userphone, String userpwd, OnRegisterListener listener);
}
