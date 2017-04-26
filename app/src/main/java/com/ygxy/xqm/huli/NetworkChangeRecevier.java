package com.ygxy.xqm.huli;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

/**
 * 监听网络变化
 * Created by XQM on 2017/2/25.
 */

public class NetworkChangeRecevier extends BroadcastReceiver{
    //定义回调显示对话框的接口
    public interface OnNetworkChangeListener{
        void onChange();
    }
    private OnNetworkChangeListener listener;

    private ConnectivityManager connectivityManager;//网络连接服务对象
    private AlertDialog.Builder builder;
    private Boolean success = false;
    public static final String ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    public void setOnNetworkChangeListener(OnNetworkChangeListener listener){
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION.equals(intent.getAction())) {
            //获取网络连接服务
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            //获取GPRS网络和WiFi网络连接状态
            NetworkInfo.State mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
            NetworkInfo.State wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();

            if (wifi != null && mobile != null && NetworkInfo.State.CONNECTED != wifi && NetworkInfo.State.CONNECTED == mobile) {
//                Toast.makeText(context, "手机网络连接成功", Toast.LENGTH_SHORT).show();
                return;
            } else if (wifi != null && mobile != null && NetworkInfo.State.CONNECTED == wifi && NetworkInfo.State.CONNECTED != mobile) {
//                context.startService(intent2);
//                Toast.makeText(context, "无线网络连接成功", Toast.LENGTH_SHORT).show();
                return;
            } else if (wifi != null && mobile != null && NetworkInfo.State.CONNECTED != wifi && NetworkInfo.State.CONNECTED != mobile) {
//                if (listener!=null){
//                    listener.onChange();
//                }
                Toast.makeText(context,"网络连接异常",Toast.LENGTH_SHORT).show();
            }
        }
    }

}
