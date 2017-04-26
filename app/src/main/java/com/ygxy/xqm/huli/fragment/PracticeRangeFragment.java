package com.ygxy.xqm.huli.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ygxy.xqm.huli.R;
import com.ygxy.xqm.huli.adapter.PracticeAdapter;
import com.ygxy.xqm.huli.bean.Practice;
import com.ygxy.xqm.huli.MyApplication;
import com.ygxy.xqm.huli.myview.MyTopbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by XQM on 2017/3/16.
 */

public class PracticeRangeFragment extends Fragment{
    @BindView(R.id.practice_recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.practice_search)
    EditText mEtSearch;
    private List<Practice> practiceList = new ArrayList<>();
    private PracticeAdapter adapter;
    private Practice[] practices = {new Practice("我的天","中",R.drawable.ic_launcher),
            new Practice("我的天","难",R.drawable.ic_launcher),
            new Practice("我的天","难",R.drawable.ic_launcher)};
    @BindView(R.id.practice_bar) MyTopbar myTopbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.practice_range,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myTopbar.setMainTitle("练习场");
        onTouchSearch();
        GridLayoutManager layoutManager = new GridLayoutManager(MyApplication.getContext(),3);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PracticeAdapter(MyApplication.getContext(),practiceList);
        initPractices();
        recyclerView.setAdapter(adapter);


    }

    /**
     * 监听搜索输入框
     */
    private void onTouchSearch() {
        mEtSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mEtSearch.setFocusable(true);
                mEtSearch.setFocusableInTouchMode(true);
                mEtSearch.requestFocus();
                return false;
            }
        });
    }

    private void initPractices() {
        practiceList.clear();
        for (int i = 0;i < 50;i++){
            Random random = new Random();
            int index = random.nextInt(practices.length);
            practiceList.add(practices[index]);
        }

    }
}
