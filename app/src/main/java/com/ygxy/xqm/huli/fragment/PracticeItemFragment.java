package com.ygxy.xqm.huli.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ygxy.xqm.huli.PracticeItemActivity;
import com.ygxy.xqm.huli.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by XQM on 2017/3/26.
 */

public class PracticeItemFragment extends BackHandledFragment {
    @BindView(R.id.practice_mind_out)
    Button mPractice_mind_out;
    @BindView(R.id.practice_mind_next)
    Button mPractice_mind_next;
    private PracticePrimaryRangeFragment primaryRangeFragment;

    @OnClick(R.id.practice_mind_out) void practice_mind_out(){
        getActivity().finish();
    }
    @OnClick(R.id.practice_mind_next) void practice_mind_next(){
        PracticeItemActivity itemActivity = (PracticeItemActivity) getActivity();
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
        primaryRangeFragment = new PracticePrimaryRangeFragment();
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
