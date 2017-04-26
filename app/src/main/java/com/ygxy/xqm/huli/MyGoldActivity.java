package com.ygxy.xqm.huli;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ygxy.xqm.huli.util.OkHttpPostUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 我的金币
 * Created by XQM on 2017/3/26.
 */

public class MyGoldActivity extends AppCompatActivity{
    @BindView(R.id.my_gold_toolbar)Toolbar toolbar;
    @BindView(R.id.my_gold_count)TextView mTvGold;
    @BindView(R.id.my_gold_non)TextView mTvNonGold;
    public static final String url = "http://139.199.220.49:8080/gold/findGold";
    private OkHttpPostUtil postUtil;
    private String header;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private UserLoginActivity userLoginActivity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_gold);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        mTvGold.setText("0");
        mTvNonGold.setVisibility(View.INVISIBLE);
        postUtil = new OkHttpPostUtil();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        userLoginActivity = new UserLoginActivity();
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        postHeaderToBackgound();
    }

    /**
     * 请求服务器
     */
    private void postHeaderToBackgound() {
        header = userLoginActivity.returnToken(preferences);
        if (header != null){
            postUtil.postHeader(url,header, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.getMessage();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MyGoldActivity.this,"访问服务器异常，请检查网络",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        final int gold = jsonObject.optInt("金币数:");
                        final String myGold = String.valueOf(gold);
//                        Log.d("金币数目：", String.valueOf(gold));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTvGold.setText(myGold);
                                if (gold <= 0){
                                    mTvGold.setText("0");
                                    mTvNonGold.setVisibility(View.VISIBLE);
                                }else {
                                    mTvNonGold.setVisibility(View.INVISIBLE);
                                }
                            }
                        });
                    } catch (JSONException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MyGoldActivity.this,"请重新登录",Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
            });
        }
    }

    public static void actionStar(Context context){
        Intent intent = new Intent(context,MyGoldActivity.class);
        context.startActivity(intent);
    }
}
