package com.ygxy.xqm.huli.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.ygxy.xqm.huli.interf.BackHandledInterface;

/**
 * fragment退出栈的基类
 * Created by XQM on 2017/3/16.
 */

public abstract class BackHandledFragment extends Fragment{
    protected BackHandledInterface mBackHandledInterface;
    public abstract boolean onBackPressed();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //判断碎片所在activity是否实现BackHandledInterface接口
        if (!(getActivity() instanceof BackHandledInterface)){
            throw new ClassCastException("Hosting Activity must implement BackHandledInterface");
        }else {
            this.mBackHandledInterface = (BackHandledInterface) getActivity();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //告诉FragmentActivity，当前Fragment在栈顶
        mBackHandledInterface.setSelectedFragment(this);
    }
}
