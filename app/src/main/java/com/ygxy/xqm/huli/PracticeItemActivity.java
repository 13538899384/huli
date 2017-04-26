package com.ygxy.xqm.huli;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RelativeLayout;

import com.ygxy.xqm.huli.fragment.BackHandledFragment;
import com.ygxy.xqm.huli.fragment.PracticeItemFragment;
import com.ygxy.xqm.huli.fragment.PracticePrimaryRangeFragment;
import com.ygxy.xqm.huli.fragment.PracticePrimarySecondFragment;
import com.ygxy.xqm.huli.interf.BackHandledInterface;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by XQM on 2017/3/22.
 */

public class PracticeItemActivity extends FragmentActivity implements BackHandledInterface{
    BackHandledFragment mBackHandledFragment;
    @BindView(R.id.practice_rl)
    RelativeLayout practice_rl;
    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentTransaction transaction;
    private PracticeItemFragment itemFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.practice_item_fragment);
        ButterKnife.bind(this);
        practice_rl.setVisibility(View.VISIBLE);
        itemFragment = new PracticeItemFragment();
        addItemFragment(itemFragment);

    }

    public void addItemFragment(PracticeItemFragment itemFragment) {
        transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.practice_rl,itemFragment,null);
        transaction.commit();
    }

    public void replaceItemFragment(PracticePrimaryRangeFragment rangeFragment){
        if (rangeFragment == null){
            rangeFragment = new PracticePrimaryRangeFragment();
        }
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.practice_rl,rangeFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void replaceRangeFragment(PracticePrimarySecondFragment practicePrimarySecondFragment){
        if (practicePrimarySecondFragment == null){
            practicePrimarySecondFragment = new PracticePrimarySecondFragment();
        }
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.practice_rl,practicePrimarySecondFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void setSelectedFragment(BackHandledFragment selectedFragment) {
        this.mBackHandledFragment = selectedFragment;
    }

    @Override
    public void onBackPressed() {
        if(mBackHandledFragment == null || !mBackHandledFragment.onBackPressed()){
            if(getSupportFragmentManager().getBackStackEntryCount() == 0){
                super.onBackPressed();
            }else{
                getSupportFragmentManager().popBackStack();
            }
        }
    }
}
