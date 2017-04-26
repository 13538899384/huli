package com.ygxy.xqm.huli;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePalApplication;

/**
 * 全局获取Context
 * Created by XQM on 2017/3/15.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
        LitePalApplication.initialize(context);
    }
     public static Context getContext(){
         return context;
     }
}
