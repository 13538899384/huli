package com.ygxy.xqm.huli.view;


/**
 * Created by XQM on 2017/3/10.
 */

public interface IUserLoginView {
    String getUserName();

    String getPassword();

    void clearUserName();

    void clearPassword();

    void showDialog();

    void hideDialog();

    void toMainActivity();

    void showFailedError();
}
