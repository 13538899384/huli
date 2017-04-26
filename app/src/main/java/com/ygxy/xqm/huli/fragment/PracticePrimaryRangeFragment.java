package com.ygxy.xqm.huli.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ygxy.xqm.huli.Pharmaceutical_Preparations;
import com.ygxy.xqm.huli.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 思维导图
 * Created by XQM on 2017/3/23.
 */

public class PracticePrimaryRangeFragment extends BackHandledFragment {
    @BindView(R.id.practice_tv1)TextView mTv1;
    @BindView(R.id.practice_tv2)TextView mTv2;
    @BindView(R.id.practice_tv3)TextView mTv3;
    @BindView(R.id.practice_tv4)TextView mTv4;
    @BindView(R.id.practice_tv5)TextView mTv5;
    @BindView(R.id.practice_tv6)TextView mTv6;
    @BindView(R.id.practice_tv7)TextView mTv7;
    @BindView(R.id.practice_tv8)TextView mTv8;
    @BindView(R.id.practice_tv9)TextView mTv9;
    @BindView(R.id.practice_ll1)Button mBtn1;
    @BindView(R.id.practice_ll2)Button mBtn2;
    @BindView(R.id.practice_ll3)Button mBtn3;
    @BindView(R.id.practice_ll4)Button mBtn4;
    @BindView(R.id.practice_ll5)Button mBtn5;
    @BindView(R.id.practice_ll6)Button mBtn6;
    @BindView(R.id.practice_ll7)Button mBtn7;
    @BindView(R.id.practice_ll8)Button mBtn8;
    @BindView(R.id.practice_ll9)Button mBtn9;
    @BindView(R.id.practice_primary_right)LinearLayout mllRight;
    @BindView(R.id.practice_primary_scroll)ScrollView mScrollView;

    @OnClick(R.id.practice_tv1)void practice_tv1(){
        if (TextUtils.isEmpty(mBtn1.getText()))
        {
            mBtn1.setText(mTv1.getText());
        }
        else if (TextUtils.isEmpty(mBtn2.getText())){
            mBtn2.setText(mTv1.getText());
        }
        else if (TextUtils.isEmpty(mBtn3.getText())){
            mBtn3.setText(mTv1.getText());
        }
        else if (TextUtils.isEmpty(mBtn4.getText())){
            mBtn4.setText(mTv1.getText());
        }
        else if (TextUtils.isEmpty(mBtn5.getText())){
            mBtn5.setText(mTv1.getText());
        }
        else if (TextUtils.isEmpty(mBtn6.getText())){
            mBtn6.setText(mTv1.getText());
        }
        else if (TextUtils.isEmpty(mBtn7.getText())){
            mBtn7.setText(mTv1.getText());
        }
        else if (TextUtils.isEmpty(mBtn8.getText())){
            mBtn8.setText(mTv1.getText());
        }
        else if (TextUtils.isEmpty(mBtn9.getText())){
            mBtn9.setText(mTv1.getText());
        }

    }
    @OnClick(R.id.practice_tv2)void practice_tv2(){
        if (TextUtils.isEmpty(mBtn1.getText()))
        {
            mBtn1.setText(mTv2.getText());
        }
        else if (TextUtils.isEmpty(mBtn2.getText())){
            mBtn2.setText(mTv2.getText());
        }
        else if (TextUtils.isEmpty(mBtn3.getText())){
            mBtn3.setText(mTv2.getText());
        }
        else if (TextUtils.isEmpty(mBtn4.getText())){
            mBtn4.setText(mTv2.getText());
        }
        else if (TextUtils.isEmpty(mBtn5.getText())){
            mBtn5.setText(mTv2.getText());
        }
        else if (TextUtils.isEmpty(mBtn6.getText())){
            mBtn6.setText(mTv2.getText());
        }
        else if (TextUtils.isEmpty(mBtn7.getText())){
            mBtn7.setText(mTv2.getText());
        }
        else if (TextUtils.isEmpty(mBtn8.getText())){
            mBtn8.setText(mTv2.getText());
        }
        else if (TextUtils.isEmpty(mBtn9.getText())){
            mBtn9.setText(mTv2.getText());
        }
    }
    @OnClick(R.id.practice_tv3)void practice_tv3(){
        if (TextUtils.isEmpty(mBtn1.getText()))
        {
            mBtn1.setText(mTv3.getText());
        }
        else if (TextUtils.isEmpty(mBtn2.getText())){
            mBtn2.setText(mTv3.getText());
        }
        else if (TextUtils.isEmpty(mBtn3.getText())){
            mBtn3.setText(mTv3.getText());
        }
        else if (TextUtils.isEmpty(mBtn4.getText())){
            mBtn4.setText(mTv3.getText());
        }
        else if (TextUtils.isEmpty(mBtn5.getText())){
            mBtn5.setText(mTv3.getText());
        }
        else if (TextUtils.isEmpty(mBtn6.getText())){
            mBtn6.setText(mTv3.getText());
        }
        else if (TextUtils.isEmpty(mBtn7.getText())){
            mBtn7.setText(mTv3.getText());
        }
        else if (TextUtils.isEmpty(mBtn8.getText())){
            mBtn8.setText(mTv3.getText());
        }
        else if (TextUtils.isEmpty(mBtn9.getText())){
            mBtn9.setText(mTv3.getText());
        }
    }
    @OnClick(R.id.practice_tv4)void practice_tv4(){
        if (TextUtils.isEmpty(mBtn1.getText()))
        {
            mBtn1.setText(mTv4.getText());
        }
        else if (TextUtils.isEmpty(mBtn2.getText())){
            mBtn2.setText(mTv4.getText());
        }
        else if (TextUtils.isEmpty(mBtn3.getText())){
            mBtn3.setText(mTv4.getText());
        }
        else if (TextUtils.isEmpty(mBtn4.getText())){
            mBtn4.setText(mTv4.getText());
        }
        else if (TextUtils.isEmpty(mBtn5.getText())){
            mBtn5.setText(mTv4.getText());
        }
        else if (TextUtils.isEmpty(mBtn6.getText())){
            mBtn6.setText(mTv4.getText());
        }
        else if (TextUtils.isEmpty(mBtn7.getText())){
            mBtn7.setText(mTv4.getText());
        }
        else if (TextUtils.isEmpty(mBtn8.getText())){
            mBtn8.setText(mTv4.getText());
        }
        else if (TextUtils.isEmpty(mBtn9.getText())){
            mBtn9.setText(mTv4.getText());
        }
    }
    @OnClick(R.id.practice_tv5)void practice_tv5(){
        if (TextUtils.isEmpty(mBtn1.getText()))
        {
            mBtn1.setText(mTv5.getText());
        }
        else if (TextUtils.isEmpty(mBtn2.getText())){
            mBtn2.setText(mTv5.getText());
        }
        else if (TextUtils.isEmpty(mBtn3.getText())){
            mBtn3.setText(mTv5.getText());
        }
        else if (TextUtils.isEmpty(mBtn4.getText())){
            mBtn4.setText(mTv5.getText());
        }
        else if (TextUtils.isEmpty(mBtn5.getText())){
            mBtn5.setText(mTv5.getText());
        }
        else if (TextUtils.isEmpty(mBtn6.getText())){
            mBtn6.setText(mTv5.getText());
        }
        else if (TextUtils.isEmpty(mBtn7.getText())){
            mBtn7.setText(mTv5.getText());
        }
        else if (TextUtils.isEmpty(mBtn8.getText())){
            mBtn8.setText(mTv5.getText());
        }
        else if (TextUtils.isEmpty(mBtn9.getText())){
            mBtn9.setText(mTv5.getText());
        }
    }
    @OnClick(R.id.practice_tv6)void practice_tv6(){
        if (TextUtils.isEmpty(mBtn1.getText()))
        {
            mBtn1.setText(mTv6.getText());
        }
        else if (TextUtils.isEmpty(mBtn2.getText())){
            mBtn2.setText(mTv6.getText());
        }
        else if (TextUtils.isEmpty(mBtn3.getText())){
            mBtn3.setText(mTv6.getText());
        }
        else if (TextUtils.isEmpty(mBtn4.getText())){
            mBtn4.setText(mTv6.getText());
        }
        else if (TextUtils.isEmpty(mBtn5.getText())){
            mBtn5.setText(mTv6.getText());
        }
        else if (TextUtils.isEmpty(mBtn6.getText())){
            mBtn6.setText(mTv6.getText());
        }
        else if (TextUtils.isEmpty(mBtn7.getText())){
            mBtn7.setText(mTv6.getText());
        }
        else if (TextUtils.isEmpty(mBtn8.getText())){
            mBtn8.setText(mTv6.getText());
        }
        else if (TextUtils.isEmpty(mBtn9.getText())){
            mBtn9.setText(mTv6.getText());
        }
    }
    @OnClick(R.id.practice_tv7)void practice_tv7(){
        if (TextUtils.isEmpty(mBtn1.getText()))
        {
            mBtn1.setText(mTv7.getText());
        }
        else if (TextUtils.isEmpty(mBtn2.getText())){
            mBtn2.setText(mTv7.getText());
        }
        else if (TextUtils.isEmpty(mBtn3.getText())){
            mBtn3.setText(mTv7.getText());
        }
        else if (TextUtils.isEmpty(mBtn4.getText())){
            mBtn4.setText(mTv7.getText());
        }
        else if (TextUtils.isEmpty(mBtn5.getText())){
            mBtn5.setText(mTv7.getText());
        }
        else if (TextUtils.isEmpty(mBtn6.getText())){
            mBtn6.setText(mTv7.getText());
        }
        else if (TextUtils.isEmpty(mBtn7.getText())){
            mBtn7.setText(mTv7.getText());
        }
        else if (TextUtils.isEmpty(mBtn8.getText())){
            mBtn8.setText(mTv7.getText());
        }
        else if (TextUtils.isEmpty(mBtn9.getText())){
            mBtn9.setText(mTv7.getText());
        }
    }
    @OnClick(R.id.practice_tv8)void practice_tv8(){
        if (TextUtils.isEmpty(mBtn1.getText()))
        {
            mBtn1.setText(mTv8.getText());
        }
        else if (TextUtils.isEmpty(mBtn2.getText())){
            mBtn2.setText(mTv8.getText());
        }
        else if (TextUtils.isEmpty(mBtn3.getText())){
            mBtn3.setText(mTv8.getText());
        }
        else if (TextUtils.isEmpty(mBtn4.getText())){
            mBtn4.setText(mTv8.getText());
        }
        else if (TextUtils.isEmpty(mBtn5.getText())){
            mBtn5.setText(mTv8.getText());
        }
        else if (TextUtils.isEmpty(mBtn6.getText())){
            mBtn6.setText(mTv8.getText());
        }
        else if (TextUtils.isEmpty(mBtn7.getText())){
            mBtn7.setText(mTv8.getText());
        }
        else if (TextUtils.isEmpty(mBtn8.getText())){
            mBtn8.setText(mTv8.getText());
        }
        else if (!TextUtils.isEmpty(mBtn9.getText())){
            mBtn9.setText(mTv8.getText());
        }
    }
    @OnClick(R.id.practice_tv9)void practice_tv9(){
        if (TextUtils.isEmpty(mBtn1.getText()))
        {
            mBtn1.setText(mTv9.getText());
        }
        else if (TextUtils.isEmpty(mBtn2.getText())){
            mBtn2.setText(mTv9.getText());
        }
        else if (TextUtils.isEmpty(mBtn3.getText())){
            mBtn3.setText(mTv9.getText());
        }
        else if (TextUtils.isEmpty(mBtn4.getText())){
            mBtn4.setText(mTv9.getText());
        }
        else if (TextUtils.isEmpty(mBtn5.getText())){
            mBtn5.setText(mTv9.getText());
        }
        else if (TextUtils.isEmpty(mBtn6.getText())){
            mBtn6.setText(mTv9.getText());
        }
        else if (TextUtils.isEmpty(mBtn7.getText())){
            mBtn7.setText(mTv9.getText());
        }
        else if (TextUtils.isEmpty(mBtn8.getText())){
            mBtn8.setText(mTv9.getText());
        }
        else if (TextUtils.isEmpty(mBtn9.getText())){
            mBtn9.setText(mTv9.getText());
        }
    }
    @OnClick(R.id.practice_ll1)void practice_ll1(){
        mBtn1.setText("");
    }
    @OnClick(R.id.practice_ll2)void practice_ll2(){
        mBtn2.setText("");
    }
    @OnClick(R.id.practice_ll3)void practice_ll3(){
        mBtn3.setText("");
    }
    @OnClick(R.id.practice_ll4)void practice_ll4(){
        mBtn4.setText("");
    }
    @OnClick(R.id.practice_ll5)void practice_ll5(){
        mBtn5.setText("");
    }
    @OnClick(R.id.practice_ll6)void practice_ll6(){
        mBtn6.setText("");
    }
    @OnClick(R.id.practice_ll7)void practice_ll7(){
        mBtn7.setText("");
    }
    @OnClick(R.id.practice_ll8)void practice_ll8(){
        mBtn8.setText("");
    }
    @OnClick(R.id.practice_ll9)void practice_ll9(){
        mBtn9.setText("");
    }
    @OnClick(R.id.practice_primary_next)void practice_primary_next(){
        Intent intent = new Intent(getActivity(), Pharmaceutical_Preparations.class);
        getActivity().finish();
        startActivity(intent);
    }

    @BindView(R.id.practice_primary_range_back)
    Button mBtn_range_next;
    @BindView(R.id.practice_primary_range_out)
    Button mBtn_range_out;

    @OnClick(R.id.practice_primary_range_back) void practice_primary_range_back(){
        getActivity().onBackPressed();
    }
    @OnClick(R.id.practice_primary_range_out) void practice_primary_range_out(){
        startRange();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.practice_primary_range,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();


    }

    private void initData() {
        mllRight.setVisibility(View.GONE);
        mScrollView.setVisibility(View.VISIBLE);
        mBtn1.setText("");
        mBtn2.setText("");
        mBtn3.setText("");
        mBtn4.setText("");
        mBtn5.setText("");
        mBtn6.setText("");
        mBtn7.setText("");
        mBtn8.setText("");
        mBtn9.setText("");
    }

    private Boolean startRange() {
        if (mBtn1.getText().equals(mTv1.getText())){
            mllRight.setVisibility(View.VISIBLE);
            mScrollView.setVisibility(View.GONE);
        }
        return null;
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
