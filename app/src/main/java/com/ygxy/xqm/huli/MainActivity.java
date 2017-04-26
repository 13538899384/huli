package com.ygxy.xqm.huli;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ygxy.xqm.huli.fragment.MineFragment;
import com.ygxy.xqm.huli.fragment.PkFragment;
import com.ygxy.xqm.huli.fragment.PracticeRangeFragment;
import com.ygxy.xqm.huli.servier.AutoUpdateService;
import com.ygxy.xqm.huli.util.OkHttpPostUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener{
    private MineFragment mMineFragment;
    private PkFragment mPkFragment;
    private PracticeRangeFragment mPracticeRangeFragment;
    private FragmentPagerAdapter pagerAdapter;//容纳fragment的适配器
    private List<Fragment> fragmentList;//fragment数据源
    private ViewPager mViewPager;//加载FragmentPagerAdapter
    private static final String EXITACTION = "action.exit";
    private String TAG = "MainActivity";

    private LinearLayout mTabPractice,mTabPk,mTabMine;
    private ExitReceiver exitReceiver = new ExitReceiver();
    private IntentFilter intentFilter;
    private OkHttpPostUtil postUtil;
    private UserLoginActivity loginActivity;
    private String id;
    private SharedPreferences preferences;
    private Dialog dialog;
    private AlertDialog.Builder builder;
    // 点击 Back 键间隔时间
    private long clickTime = 0;

    public static void actionStart(Context context){
        Intent intent = new Intent(context,MainActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_main);
        intentFilter = new IntentFilter();
        intentFilter.addAction(EXITACTION);
        postUtil = new OkHttpPostUtil();
        loginActivity = new UserLoginActivity();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        builder = new AlertDialog.Builder(this);
        registerReceiver(exitReceiver,intentFilter);
        initViews();
        initData();
//        initMessage();
        initListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void initViews() {
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mTabMine = (LinearLayout) findViewById(R.id.TabMine);
        mTabPk = (LinearLayout) findViewById(R.id.TabPk);
        mTabPractice = (LinearLayout) findViewById(R.id.TabPractice);
    }

    public void initListeners() {
        mTabPk.setOnClickListener(this);
        mTabPractice.setOnClickListener(this);
        mTabMine.setOnClickListener(this);
    }

    public void initData() {
        fragmentList = new ArrayList<Fragment>();//初始化数据源
        mPracticeRangeFragment = new PracticeRangeFragment();
        mPkFragment = new PkFragment();
        mMineFragment = new MineFragment();

        fragmentList.add(mPracticeRangeFragment);
        fragmentList.add(mPkFragment);
        fragmentList.add(mMineFragment);

        pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        };
        mViewPager.setAdapter(pagerAdapter);
        setSelect(0);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                position = mViewPager.getCurrentItem();//获取当前所属的fragment位置
                setTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }

    private void setSelect(int position) {
        setTab(position);
        mViewPager.setCurrentItem(position,false);//,返回fragment所属的位置，取消viewpager切换默认效果
    }

    private void setTab(int position) {
        //设置切换对应fragment样式
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.TabPractice:
                setSelect(0);
                break;
            case R.id.TabPk:
                setSelect(1);
                break;
            case R.id.TabMine:
                setSelect(2);
                break;
            default:
                break;
        }
    }

    private class ExitReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            MainActivity.this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(exitReceiver);
    }
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
        {
            // 判断两次点击的时间间隔（默认设置为2秒）
            if ((System.currentTimeMillis() - clickTime) > 2000)
            {
                //Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "再按一次退出程序",Toast.LENGTH_SHORT).show();
                clickTime = System.currentTimeMillis();
            }
            else
            {
                // 退出。为了确保不出现问题，两种退出都写上
                finish();
                System.exit(0);
                android.os.Process.killProcess(android.os.Process.myPid());
                super.onBackPressed();
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
