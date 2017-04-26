package com.ygxy.xqm.huli.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.ygxy.xqm.huli.AccountManageActivity;
import com.ygxy.xqm.huli.R;
import com.ygxy.xqm.huli.UserLoginActivity;
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
 * Created by XQM on 2017/3/25.
 */

public class UserChangePasswordFragment extends Fragment{
    @BindView(R.id.old_password)EditText mEtOldPassword;
    @BindView(R.id.new_password)EditText mEtNewPassword;
    @BindView(R.id.admit_password)EditText mEtAdmitPassword;
    private ProgressDialog progressDialog;
    private OkHttpPostUtil postUtil;
    private UserLoginActivity loginActivity;
    private SharedPreferences preferences;
    private Message message;
    private AccountManageActivity activity;
    public static final String CHANGE_PASSWORD_URL = "http://139.199.220.49:8080/user/modifyPassword/";
    private final int FAILTRUE = 0;
    private final int RESPONSE = 1;
    private final int OLD_PASSWORD_ERROR = 2;
    private AlertDialog.Builder builder;
    @OnClick(R.id.forget_password_btn)void forget_password_btn(){
        if (judgeNull()){
            submit();
        }
    }

    /**
     * 处理用户交互
     */
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case FAILTRUE:
                    hideProgressDialog();
                    Toast.makeText(getActivity(),"访问服务器出现异常",Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                    break;
                case OLD_PASSWORD_ERROR:
                    hideProgressDialog();
                    Toast.makeText(getActivity(),"原密码输入错误",Toast.LENGTH_SHORT).show();
                    break;
                case RESPONSE:
                    hideProgressDialog();
                    builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("密码修改成功，需要重新登录");
                    builder.setCancelable(false);
                    builder.setPositiveButton("重新登陆", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(getActivity(),UserLoginActivity.class);
                            startActivity(intent);
                        }
                    });
                    builder.show();
                    getActivity().onBackPressed();
                    break;
            }
        }
    };


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    /**
     * 提交
     */
    private void submit() {
        showProgressDialog();
        String oldPassword = mEtOldPassword.getText().toString();
        String newPassword = mEtNewPassword.getText().toString();
        String admitPassword = mEtAdmitPassword.getText().toString();
        queryFromBackGround(oldPassword,newPassword,admitPassword);
    }


    private void queryFromBackGround(final String oldPassword, final String newPassword, String admitPassword) {
        String token = loginActivity.returnToken(preferences);
        String json = postUtil.changePasswordJson(newPassword,admitPassword);
//        String id = loginActivity.returnId(preferences);
        Log.d("changepassword",json);
        postUtil.postChangePassword(CHANGE_PASSWORD_URL + oldPassword, token, json, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.getMessage();
                message = Message.obtain();
                message.what = FAILTRUE;
                mHandler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
//                    String mess = jsonObject.optString("message");
                    String password_message = jsonObject.optString("password_message");
                    Log.d("result",result);
                    Log.d("changepassword",CHANGE_PASSWORD_URL + oldPassword);
                    if (password_message.equals("密码修改成功")){
                        Log.d("result",password_message);
                        message = Message.obtain();
                        message.what = RESPONSE;
                        mHandler.sendMessage(message);
                    }
                    else if (password_message.equals("旧密码输入错误")){
                        message = Message.obtain();
                        message.what = OLD_PASSWORD_ERROR;
                        mHandler.sendMessage(message);
                    }
                    else {
                        message = Message.obtain();
                        message.what = FAILTRUE;
                        mHandler.sendMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    message = Message.obtain();
                    message.what = FAILTRUE;
                    mHandler.sendMessage(message);
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.change_pwd,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressDialog = new ProgressDialog(getActivity());
        postUtil = new OkHttpPostUtil();
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        loginActivity = new UserLoginActivity();
        activity = new AccountManageActivity();

    }

    /**
     * 判断用户的输入
     * @return
     */
    private boolean judgeNull() {
        if (TextUtils.isEmpty(mEtOldPassword.getText().toString())){
            Toast.makeText(getActivity(),"请输入原密码",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (TextUtils.isEmpty(mEtNewPassword.getText().toString()) || mEtNewPassword.length() < 6){
            Toast.makeText(getActivity(),"请输入合法的密码",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (mEtAdmitPassword.getText().toString().equals(mEtNewPassword.getText().toString()) == false){
            Toast.makeText(getActivity(),"请输入正确的密码",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 显示提交对话框
     */
    private void showProgressDialog() {
        progressDialog.setMessage("正在提交...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    /**
     * 隐藏对话框
     */
    private void hideProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
