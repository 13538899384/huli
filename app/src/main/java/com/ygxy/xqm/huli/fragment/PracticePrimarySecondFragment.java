package com.ygxy.xqm.huli.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ygxy.xqm.huli.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 排序
 * Created by XQM on 2017/3/26.
 */

public class PracticePrimarySecondFragment extends BackHandledFragment{
    @BindView(R.id.practice_primary_range_out)
    Button mBtn_out;

    @OnClick(R.id.practice_primary_range_out) void practice_primary_range_out(){
        getActivity().finish();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.practice_primary_second,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        startRange();
    }

    private Boolean startRange() {

        return null;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
