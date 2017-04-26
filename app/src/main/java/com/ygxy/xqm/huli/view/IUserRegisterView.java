package com.ygxy.xqm.huli.view;

import com.ygxy.xqm.huli.bean.User;

/**
 * Created by XQM on 2017/3/17.
 */

public interface IUserRegisterView {
    String getUseId();//获取学号
    String getUseAccount();//获取账号
    String getUsePwd();//获取密码
    String getUsePhone();//获取手机号
    String getUseCode();//获取验证码
    String getUseResetPwd();//获取重新设置的密码
    void showStatus();//显示状态
    void hideStatus();
    void clearAll();
    void toLoginActivity(User user);
    void showFailed();
    void showRegister();//显示注册按钮
}
