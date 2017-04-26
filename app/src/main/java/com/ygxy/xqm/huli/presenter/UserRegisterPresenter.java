package com.ygxy.xqm.huli.presenter;

import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import com.ygxy.xqm.huli.bean.User;
import com.ygxy.xqm.huli.model.IUserRegister;
import com.ygxy.xqm.huli.model.OnRegisterListener;
import com.ygxy.xqm.huli.model.UserRegister;
import com.ygxy.xqm.huli.MyApplication;
import com.ygxy.xqm.huli.view.IUserRegisterView;

/**
 * Created by XQM on 2017/3/17.
 */

public class UserRegisterPresenter {
    private IUserRegister userRegister;
    private IUserRegisterView userRegisterView;
    private Handler mHandler = new Handler();

    public UserRegisterPresenter(IUserRegisterView userRegisterView){
        this.userRegisterView = userRegisterView;
        userRegister = new UserRegister();
    }

    public void register(){
        if (TextUtils.isEmpty(userRegisterView.getUseId()) || userRegisterView.getUseId().length() != 10){
            Toast.makeText(MyApplication.getContext(),"请输入合法的ID",Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(userRegisterView.getUsePhone()) || userRegisterView.getUsePhone().length() != 11){
            Toast.makeText(MyApplication.getContext(),"请输入合法的手机号码",Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(userRegisterView.getUsePwd())){
            Toast.makeText(MyApplication.getContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(userRegisterView.getUseResetPwd())
                || userRegisterView.getUseResetPwd().equals(userRegisterView.getUsePwd()) == false){
            Toast.makeText(MyApplication.getContext(),"请输入正确的密码",Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(userRegisterView.getUseCode())){
            Toast.makeText(MyApplication.getContext(),"请输入验证码",Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            userRegisterView.showRegister();
            userRegisterView.showStatus();
            userRegister.register(userRegisterView.getUseId(), userRegisterView.getUsePhone(),
                    userRegisterView.getUsePwd(), new OnRegisterListener() {
                        @Override
                        public void registerSuccess(final User user) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    userRegisterView.toLoginActivity(user);
                                }
                            });
                        }
                        @Override
                        public void registerFailed() {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    userRegisterView.showFailed();
                                    userRegisterView.hideStatus();
                                }
                            });
                        }
                    });
        }
    }
}
