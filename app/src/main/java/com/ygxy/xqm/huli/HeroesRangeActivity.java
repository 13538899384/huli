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
import android.widget.TextView;
import android.widget.Toast;

import com.ygxy.xqm.huli.adapter.PkHeroesAdapter;
import com.ygxy.xqm.huli.bean.Heroes;
import com.ygxy.xqm.huli.util.OkHttpPostUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by XQM on 2017/3/30.
 */

public class HeroesRangeActivity extends AppCompatActivity{
    @BindView(R.id.heroes_recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.pk_heroes_refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.pk_heroes_toolbar)
    Toolbar toolbar;
    @BindView(R.id.pk_heroes_hit)TextView mTvNot;
    private Heroes heroes;
    private PkHeroesAdapter adapter;
    private String PK_RANK_URL = "http://139.199.155.77:8080/NursingAppServer/GetRank";
    private String TAG = "heroes";
//    private Heroes[] heroes = {new Heroes(1,"张三","100分","独孤求败"),
//            new Heroes(2,"刘思思","100分","独孤求败"),
//            new Heroes(3,"杨颖","100分","独孤求败")};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.heroes_range);
        ButterKnife.bind(this);
        initData();
    }
    private void initData() {
        mTvNot.setVisibility(View.GONE);
        refreshLayout.setVisibility(View.VISIBLE);
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(HeroesRangeActivity.this);
        recyclerView.setLayoutManager(layoutManager);
//        heroesList.clear();
//        for (int i = 0;i<heroes.length;i++){
//            Random random = new Random();
//            int index = random.nextInt(heroes.length);
//            heroesList.add(heroes[index]);
//        }
//        adapter = new PkHeroesAdapter(this,getHeroesList(PK_RANK_URL));
        adapter = new PkHeroesAdapter(HeroesRangeActivity.this,getHeroesList(PK_RANK_URL));
        recyclerView.setAdapter(adapter);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshRange();
            }
        });
    }

    /**
     * 返回HeroesList集合
     * @param url
     * @return
     */
    private List<Heroes> getHeroesList(String url){
        final List<Heroes> heroesList = new ArrayList<>();
        OkHttpPostUtil.postPkTable(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.getMessage();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(HeroesRangeActivity.this,"访问服务器出现异常",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
//                Log.e(TAG,result);
                if (result == null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshLayout.setVisibility(View.GONE);
                            mTvNot.setVisibility(View.VISIBLE);
                        }
                    });
                }else {
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        JSONObject jsonObject = null;
                        for (int i = 0;i < jsonArray.length();i++){
                            jsonObject = jsonArray.getJSONObject(i);
                            heroes = new Heroes();
                            heroes.setHeroes_range(jsonObject.getInt("Rank"));
                            heroes.setHeroes_grade(jsonObject.getInt("Score"));
                            heroes.setHeroes_name(jsonObject.getString("StudentNum"));
                            heroes.setHeroes_nickname(jsonObject.getString("Label"));
//                            Log.e(TAG, String.valueOf(jsonObject.getInt("Rank")));
                            heroesList.add(heroes);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return heroesList;
    }

    /**
     * 刷新数据
     */
    private void refreshRange() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                    getHeroesList(PK_RANK_URL);
//                    Thread.sleep(2000);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(HeroesRangeActivity.this,"刷新成功",Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                        refreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
}
