package com.ygxy.xqm.huli.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ygxy.xqm.huli.DanNiaoPrimaryActivity;
import com.ygxy.xqm.huli.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by XQM on 2017/3/26.
 */

public class DaoniaoItemFragment extends BackHandledFragment {
    @BindView(R.id.practice_mind_out)
    Button mPractice_mind_out;
    @BindView(R.id.practice_mind_next)
    Button mPractice_mind_next;
    @BindView(R.id.practice_mind_start)TextView textView;
    private DaoniaoPrimaryRangeFragment primaryRangeFragment;

    @OnClick(R.id.practice_mind_out) void practice_mind_out(){
        getActivity().finish();
    }
    @OnClick(R.id.practice_mind_next) void practice_mind_next(){
        DanNiaoPrimaryActivity itemActivity = (DanNiaoPrimaryActivity) getActivity();
        itemActivity.replaceItemFragment(primaryRangeFragment);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.practice_mind_map,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        primaryRangeFragment = new DaoniaoPrimaryRangeFragment();
        textView.setText("导尿技术知识思维导图");
    }


    @Override
    public void onPause() {
        super.onPause();
        onBackPressed();
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
