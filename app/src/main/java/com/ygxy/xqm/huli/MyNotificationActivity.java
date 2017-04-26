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
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ygxy.xqm.huli.adapter.PkNotificationAdapter;
import com.ygxy.xqm.huli.bean.MyNotification;
import com.ygxy.xqm.huli.bean.NotiHistory;
import com.ygxy.xqm.huli.util.OkHttpPostUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by XQM on 2017/4/3.
 */

public class MyNotificationActivity extends AppCompatActivity {
    @BindView(R.id.notification_toolbar)Toolbar toolbar;
    @BindView(R.id.notification_null_text)LinearLayout ll_null;
    @BindView(R.id.myListView)ListView listView;
    @BindView(R.id.listView)ListView listView1;
    @BindView(R.id.tv_no)TextView mTv_no;
    private PkNotificationAdapter adapter;
    private ArrayList<String> datalist = new ArrayList<String>();
    private List<NotiHistory> historyList;
    private final String PK_GET_MESSAGE_RUL = "http://139.199.155.77:8080/NursingAppServer/GetMessage?id=";
    private UserLoginActivity userLoginActivity;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String TAG = "myNotification";
    private String role = null;
    private String pkStudent1 = null;
    private String pkStudent2 = null;
    private String referee1 = null;
    private String referee2 = null;
    private int type;
    private int score;
    private int result;
    private String competitor = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_notification);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        userLoginActivity = new UserLoginActivity();
        preferences = PreferenceManager.getDefaultSharedPreferences(MyNotificationActivity.this);
        editor = preferences.edit();
        ll_null.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
        mTv_no.setVisibility(View.GONE);
        adapter = new PkNotificationAdapter(this,getList());
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryNotification();
    }

    private void queryNotification() {
//        list = DataSupport.findAll(MyNotification.class);
    }

    /**
     * 返回list集合
     * @return
     */
    private List<MyNotification> getList(){
        final List<MyNotification> notificationList = new ArrayList<>();
        final MyNotification myNotification = new MyNotification();
        OkHttpPostUtil.postPkTable(PK_GET_MESSAGE_RUL + userLoginActivity.returnId(preferences), new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.getMessage();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {Toast.makeText(MyNotificationActivity.this,"访问网络异常",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                Log.e("data",data);
                if (data.equals("0")){
                    Log.e(TAG,"没有消息");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ll_null.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MyNotificationActivity.this,
                                    R.layout.simple_list_item,R.id.text_item,datalist);
                            listView1.setAdapter(arrayAdapter);
                            queryList();
                        }
                    });
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        type = jsonObject.optInt("Type");
                        if (type == -1){
                            competitor = jsonObject.getString("Competitor");
                            myNotification.setType(type);
                            myNotification.setCompetitor(competitor);
                            notificationList.add(myNotification);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ll_null.setVisibility(View.GONE);
                                    listView.setVisibility(View.VISIBLE);
                                    mTv_no.setVisibility(View.GONE);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }else if (type == 0){
                            role = jsonObject.optString("Role");
                            pkStudent1 = jsonObject.optString("PkStudent1");
                            pkStudent2 = jsonObject.optString("PkStudent2");
                            referee1 = jsonObject.optString("Referee1");
                            referee2 = jsonObject.optString("Referee2");

                            myNotification.setType(type);
                            myNotification.setRole(role);
                            myNotification.setPkStudent1(pkStudent1);
                            myNotification.setPkStudent2(pkStudent2);
                            myNotification.setReferee1(referee1);
                            myNotification.setReferee2(referee2);
                            notificationList.add(myNotification);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ll_null.setVisibility(View.GONE);
                                    listView.setVisibility(View.VISIBLE);
                                    mTv_no.setVisibility(View.GONE);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                        else {
                            score = jsonObject.getInt("Score");
                            competitor = jsonObject.getString("Competitor");
                            result = jsonObject.getInt("Result");
                            myNotification.setType(type);
                            myNotification.setScore(score);
                            myNotification.setCompetitor(competitor);
                            myNotification.setResult(result);
                            notificationList.add(myNotification);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ll_null.setVisibility(View.GONE);
                                    listView.setVisibility(View.VISIBLE);
                                    mTv_no.setVisibility(View.GONE);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return notificationList;
    }

    /**
     * 从数据库中查询
     */
    private void queryList() {
        historyList = DataSupport.findAll(NotiHistory.class);
        if(historyList.size() > 0){
            datalist.clear();
            for (NotiHistory notiHistory:historyList){
                datalist.add(notiHistory.getContent());
                adapter.notifyDataSetChanged();
            }
        }else {
            Log.e("list", String.valueOf(historyList.size()));
            mTv_no.setVisibility(View.VISIBLE);
            listView1.setVisibility(View.GONE);
        }
    }

    public static void starAction(Context context){
        Intent intent = new Intent(context,MyNotificationActivity.class);
        context.startActivity(intent);
    }
}
