package com.ygxy.xqm.huli.model;

/**
 * 执行登录耗时操作
 * Created by XQM on 2017/3/17.
 */

public interface IUserBiz {
    void login(String username,String password,OnLoginListener loginListener);
}
