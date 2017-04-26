package com.ygxy.xqm.huli.servier;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.ygxy.xqm.huli.UserLoginActivity;

/**
 * Created by XQM on 2017/4/6.
 */

public class AutoUpdateService extends Service{
    private final String PK_GET_MESSAGE_RUL = "http://139.199.155.77:8080/NursingAppServer/GetMessage?id=";
    private UserLoginActivity userLoginActivity;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String TAG = "AutoUpdateService";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        userLoginActivity = new UserLoginActivity();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateMessage();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anMis = 5 * 60 * 1000;//5分钟更新一次
        long triggerAttime = SystemClock.elapsedRealtime() + anMis;
        Intent intent1 = new Intent(this,AutoUpdateService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this,0,intent1,0);
        manager.cancel(pendingIntent);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAttime,pendingIntent);
        return super.onStartCommand(intent, flags, startId);
    }

    public void updateMessage(){
        preferences = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this);
        editor = preferences.edit();
//        OkHttpPostUtil.postPkTable(PK_GET_MESSAGE_RUL + userLoginActivity.returnId(preferences), new okhttp3.Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.getMessage();
//            }
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String data = response.body().string();
//                Log.e(TAG,PK_GET_MESSAGE_RUL + userLoginActivity.returnId(preferences));
//                Log.e(TAG,data);
//                Log.e(TAG,userLoginActivity.returnId(preferences));
//                if (data.equals("0")){
//                    Log.e(TAG,"没有消息");
//                }else if (data.equals("Reject")){
//                    Log.e(TAG,"PK申请被拒绝");
//                }
//                JSONObject jsonObject = null;
//                try {
//                    jsonObject = new JSONObject(data);
//                    String role = jsonObject.optString("Role");
//                    String pkStudent1 = jsonObject.optString("PkStudent1");
//                    String pkStudent2 = jsonObject.optString("PkStudent2");
//                    String referee1 = jsonObject.optString("Referee1");
//                    String referee2 = jsonObject.optString("Referee2");
//                    Log.e(TAG,role);
//                    editor.putString("role",role);
//                    editor.putString("pkStudent1",pkStudent1);
//                    editor.putString("pkStudent2",pkStudent2);
//                    editor.putString("referee1",referee1);
//                    editor.putString("referee2",referee2);
//                    editor.putString("data",data);
//                    editor.apply();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//
//                }
//            }
//        });
    }
}
