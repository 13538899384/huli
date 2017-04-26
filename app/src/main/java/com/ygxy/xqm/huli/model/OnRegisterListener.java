package com.ygxy.xqm.huli.model;

import com.ygxy.xqm.huli.bean.User;

/**
 * Created by XQM on 2017/3/17.
 */

public interface OnRegisterListener {
    void registerSuccess(User user);

    void registerFailed();
}
