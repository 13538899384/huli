package com.ygxy.xqm.huli.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ygxy.xqm.huli.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 高级训练场
 * Created by XQM on 2017/4/29.
 */

public class AdvancedFragment extends Fragment{
    @OnClick(R.id.advanced_once_exit)void advanced_once_exit(){
        getActivity().finish();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.advanced_once_pass,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
