package com.ygxy.xqm.huli.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * 控制activity的类
 * Created by XQM on 2017/3/16.
 */

public class ActivityCollector {
    public static List<Activity> activityList = new ArrayList<Activity>();

    public static void addActivity(Activity activity){
        activityList.add(activity);
    }

    public static void removeActivity(Activity activity){
        activityList.remove(activity);
    }

    public static void finishAll(){
        for (Activity activity:activityList){
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
