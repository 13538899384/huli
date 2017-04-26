package com.ygxy.xqm.huli.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ygxy.xqm.huli.ChangPasswordActivity;
import com.ygxy.xqm.huli.R;
import com.ygxy.xqm.huli.UserLoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by XQM on 2017/3/19.
 */

public class AccountingManagementFragment extends Fragment {
    @BindView(R.id.account_change_pwd)
    TextView mTv_change_pwd;
    private AlertDialog.Builder builder;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @OnClick(R.id.account_change_pwd) void account_change_pwd(){
        Intent intent = new Intent(getActivity(), ChangPasswordActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.account_sign_out) void account_sign_out(){
        builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("退出登录");
        builder.setMessage("确定要退出登录？");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                editor.putBoolean("login_status",false);
                editor.apply();
                Boolean status = preferences.getBoolean("login_status",false);
                Intent intent = new Intent(getActivity(), UserLoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.accountingfragment,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = preferences.edit();
    }


    @Override
    public void onPause() {
        super.onPause();
    }
}
