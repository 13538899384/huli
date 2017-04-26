package com.ygxy.xqm.huli.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ygxy.xqm.huli.HeroesRangeActivity;
import com.ygxy.xqm.huli.PkHistoryActivity;
import com.ygxy.xqm.huli.R;
import com.ygxy.xqm.huli.UserLoginActivity;
import com.ygxy.xqm.huli.servier.NotificationService;
import com.ygxy.xqm.huli.util.OkHttpPostUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by XQM on 2017/3/16.
 */

public class PkFragment extends Fragment {
    @BindView(R.id.pk_heroes)
    Button mBtnHeroes;
    @BindView(R.id.pk_pk)
    Button mBtnPk;
    private OkHttpPostUtil okHttpPostUtil;
    private String header;
    private SharedPreferences sharedPreferences;
    private UserLoginActivity userLoginActivity;
    public static final String REDUCE_URL = "http://139.199.220.49:8080/gold/reduce/";//减少金币
    public static final String ADD_URL = "http://139.199.220.49:8080/gold/add/";//增加金币
    public static final String PK_URL = "http://139.199.155.77:8080/NursingAppServer/PK?id=";//pk匹配
    public static final String PK_REFEREE_URL = "http://139.199.155.77:8080/NursingAppServer/AsReferee?id=";//是否是评委
    public static final String PK_REFEREE_GRADE_URL = "http://139.199.155.77:8080/NursingAppServer/RecordResult?id=";//评委录入分数
    private final int PK_ERROR = 0;//没有匹配成功
    private final int PK_SUCCESS = 1;//匹配等待处理
    private final int PK_REFEREE_NON = 2;//不是评委
    private final int PK_REFEREE = 3;//评委
    private final int PK_REFEREE_GRADE = 4;//录入分数返回
    private final int PK_HANDLER = 5;//匹配对象得到消息
    private Boolean PK_STATUS = false;
    private String id = null;
    private String firstId = null,secondId = null;
    private Dialog dialog;
    private AlertDialog.Builder builder;
    private HeroesRangeActivity rangeActivity;
    private PkHistoryFragment historyFragment;
    private NotificationService.NotificationBinder notificationBinder;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            notificationBinder = (NotificationService.NotificationBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    /**
     * 处理服务器返回的结果
     */
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case PK_ERROR://服务器返回出错
                    Toast.makeText(getActivity(),"访问服务器异常",Toast.LENGTH_SHORT).show();
                    break;
                case PK_SUCCESS://pk匹配成功
                    PK_STATUS = true;
                    Bundle bundle1 = msg.getData();
                    String pk_back_id = bundle1.getString("resultId");//返回pk对象的id
                    Log.d("backId",pk_back_id);
                    if (pk_back_id.equals("0")){
                        Toast.makeText(getActivity(),"拒绝请求",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getActivity(),"匹配的对手学号是："+pk_back_id+","+"正在等待对方处理",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case PK_REFEREE://评委录入分数
                    Bundle bundle = msg.getData();
                    String result = bundle.getString("result");
                    Log.d("返回的result",result);
                    //判断是不是评委，只有评委才能录入分数
                    if (!result.equals("0")){
                        showGradeViewDialog();
//                        Toast.makeText(getActivity(),"两个PK选手的学号是："+result,Toast.LENGTH_SHORT).show();
                    }else {
                        showNonRefereeDialog();
                    }
                    break;
                case PK_REFEREE_GRADE://录入分数是否成功
                    Bundle bundle2 = msg.getData();
                    String result2 = bundle2.getString("result");
                    Log.d("返回的resul2",result2);
                    if (result2.equals("1")){
                        Toast.makeText(getActivity(),"录入成绩成功",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }else {
                        Toast.makeText(getActivity(),"没有录入成绩",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                    break;
                case PK_HANDLER://匹配对象通知处理
//                    Bundle bundle3 = msg.getData();
//                    String role = bundle3.getString("role");
//                    String pkStudent1 = bundle3.getString("PkStudent1");
//                    String pkStudent2 = bundle3.getString("pkStudent2");
//                    String referee1 = bundle3.getString("Referee1");
//                    String referee2 = bundle3.getString("Referee2");
//                    Log.d("接到",role);
//                    Intent intent = new Intent(getActivity(), NotificationService.class);
//                    getActivity().startService(intent);
//                    getActivity().bindService(intent,connection,BIND_AUTO_CREATE);//绑定服务
//                    notificationBinder.showNotification("haoaho");
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 查看英雄榜
     */
    @OnClick(R.id.pk_heroes) void pk_heroes(){
//        addGold();
        Intent intent = new Intent(getActivity(), HeroesRangeActivity.class);
        startActivity(intent);
    }

    /**
     * pk
     */
    @OnClick(R.id.pk_pk) void pk_pk(){
        okHttpPostUtil.postPkTable(PK_URL + id, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.getMessage();
                Message message = Message.obtain();
                message.arg1 = 0;
                message.what = PK_ERROR;
                mHandler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d("返回的结果",result);
                Message message = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putString("resultId",result);
                message.setData(bundle);
                message.what = PK_SUCCESS;
                mHandler.sendMessage(message);

            }
        });
    }

    /**
     *pk记录
     */
    @OnClick(R.id.pk_history)void pk_history(){
//        reduceGold();
        Intent intent = new Intent(getActivity(), PkHistoryActivity.class);
        startActivity(intent);
    }

    /**
     * 评委录入分数
     */
    @OnClick(R.id.pk_grade)void pk_grade(){
        Log.d("测试返回的id",id);
        Log.d("测试返回的id",PK_REFEREE_URL + id);
        //提示用户匹配结果
        okHttpPostUtil.postPkTable(PK_REFEREE_URL + id, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.getMessage();
                Message message = Message.obtain();
                message.arg1 = 0;
                message.what = PK_ERROR;
                mHandler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d("返回的结果",result);
                if (!result.equals("0")){
                    firstId = result.substring(0,10);
                    secondId = result.substring(11,result.length());
                    Log.d("学生1",firstId);
                    Log.d("学生2",secondId);
                }
//                showStudentNumber(firstId,secondId);
                Message message = Message.obtain();
                Bundle data = new Bundle();
                data.putString("result",result);
                message.setData(data);
                message.what = PK_REFEREE;
                mHandler.sendMessage(message);
            }
        });
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pk,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        okHttpPostUtil = new OkHttpPostUtil();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userLoginActivity = new UserLoginActivity();
        rangeActivity = new HeroesRangeActivity();
        historyFragment = new PkHistoryFragment();
        builder = new AlertDialog.Builder(getActivity());
        id = userLoginActivity.returnId(sharedPreferences);
    }

    /**
     * 减少金币数量
     */
    private void reduceGold() {
        header = userLoginActivity.returnToken(sharedPreferences);
        final String reduceGoldCount = "90";
//        Log.d("header",header);
        if (header != null){
            okHttpPostUtil.postHeader(REDUCE_URL+reduceGoldCount, header, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.getMessage();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    Log.d("result",result);
                    Log.d("请求头",REDUCE_URL+reduceGoldCount);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String reduceGold = jsonObject.optString("reduceGold");
//                    Log.d("addGold",reduceGold);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }else {
            Toast.makeText(getActivity(),"操作失败，请重新登录",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 增加金币数量
     */
    private void addGold() {
        header = userLoginActivity.returnToken(sharedPreferences);
        final String addGoldCount = "1000";
//        Log.d("header",header);
        if (header != null){
            okHttpPostUtil.postHeader(ADD_URL+addGoldCount, header, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.getMessage();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
//                Log.d("result",result);
//                Log.d("请求头",ADD_URL+addGoldCount);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String addGold = jsonObject.optString("addGold");
                        Log.d("addGold",addGold);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }else {
            Toast.makeText(getActivity(),"添加金币失败，请重新登录",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 交互评委录入学分
     * @param
     */
    public void showGradeViewDialog() {
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("评委录入学分");
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.referee_view, null);
        TextView mTvStudent1 = (TextView) view.findViewById(R.id.referee_student1);
        TextView mTvStudent2 = (TextView) view.findViewById(R.id.referee_student2);
        final EditText mEtScore1 = (EditText) view.findViewById(R.id.referee_score1);
        final EditText mEtScore2 = (EditText) view.findViewById(R.id.referee_score2);
        Button mBtnCancel = (Button) view.findViewById(R.id.referee_cancel);
        Button mBtnSubmit = (Button) view.findViewById(R.id.referee_submit);
        mTvStudent1.setText(firstId);
        mTvStudent2.setText(secondId);
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String score1 = mEtScore1.getText().toString();
                final String score2 = mEtScore2.getText().toString();
                if (!score1.equals("") || !score2.equals("")) {
                    okHttpPostUtil.postPkTable(PK_REFEREE_GRADE_URL + id + "&score1=" + score1 + "&score2=" + score2, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.getMessage();
                            Message message = Message.obtain();
                            message.arg1 = 0;
                            message.what = PK_ERROR;
                            mHandler.sendMessage(message);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String result = response.body().string();
//                            Log.d("result", result);
//                            Log.d("body", PK_REFEREE_GRADE_URL + id + "&score1=" + score1 + "&score2=" + score2);
                            Message message = Message.obtain();
                            Bundle bundle = new Bundle();
                            bundle.putString("result",result);
                            message.setData(bundle);
                            message.what = PK_REFEREE_GRADE;
                            mHandler.sendMessage(message);
                        }
                    });
                }
                else {
                    Toast.makeText(getActivity(), "请录入分数", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setView(view);
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();
    }


    /**
     * 不是评委提示用户
     */
    public void showNonRefereeDialog(){
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("提示");
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.non_referee_view,null);
        @SuppressLint("WrongViewCast")
        Button mBtn_nonreferee = (Button) view.findViewById(R.id.non_referee);
        mBtn_nonreferee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        builder.setView(view);
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();
    }
}
