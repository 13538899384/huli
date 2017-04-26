package com.ygxy.xqm.huli;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.ygxy.xqm.huli.adapter.PkHistoryAdapter;
import com.ygxy.xqm.huli.bean.History;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by XQM on 2017/3/30.
 */

public class PkHistoryActivity extends AppCompatActivity{
    @BindView(R.id.pk_heroes_toolbar)Toolbar toolbar;
    @BindView(R.id.pk_history_refresh)SwipeRefreshLayout refreshLayout;
    @BindView(R.id.history_recyclerView)RecyclerView recyclerView;

    private List<History> historyList = new ArrayList<>();
    private PkHistoryAdapter historyAdapter;
    private History[] histories = {new History("李思思","张三","李四","100分","98分","赢"),
            new History("李思思","张三","李四","100分","98分","赢"),
            new History("李思思","张三","李四","100分","98分","赢")};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pk_history);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        historyList.clear();
        for (int i = 0;i<histories.length;i++){
            Random random = new Random();
            int index = random.nextInt(histories.length);
            historyList.add(histories[index]);
        }
        historyAdapter = new PkHistoryAdapter(this,historyList);
        recyclerView.setAdapter(historyAdapter);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshRange();
            }
        });
    }

    private void refreshRange() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PkHistoryActivity.this,"刷新成功",Toast.LENGTH_SHORT).show();
                        historyAdapter.notifyDataSetChanged();
                        refreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
}
