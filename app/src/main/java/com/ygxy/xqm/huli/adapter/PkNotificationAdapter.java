package com.ygxy.xqm.huli.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ygxy.xqm.huli.MyApplication;
import com.ygxy.xqm.huli.R;
import com.ygxy.xqm.huli.UserLoginActivity;
import com.ygxy.xqm.huli.bean.MyNotification;
import com.ygxy.xqm.huli.bean.NotiHistory;
import com.ygxy.xqm.huli.util.OkHttpPostUtil;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by XQM on 2017/4/10.
 */

public class PkNotificationAdapter extends BaseAdapter{
    private List<MyNotification> list;
    private LayoutInflater inflater;
    private UserLoginActivity userLoginActivity;
    private SharedPreferences preferences;
    private String TAG = "PkNotification";
    private Dialog dialog;
    private AlertDialog.Builder builder;
    private Context mContext;
    private ViewHolder viewHolder = null;
    private NotiHistory history;
    private String HANDLER_MESSAGE_URL = "http://139.199.155.77:8080/NursingAppServer/HandleMessage?choice=";
//    private final String PK_GET_MESSAGE_RUL = "http://139.199.155.77:8080/NursingAppServer/GetMessage?id=";
    private final int ERROR = 0;
    private final int SUCCESS = 1;
    private final int REFUSE = 2;
    public PkNotificationAdapter(Context context,List<MyNotification> list){
        mContext = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
    }
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ERROR:
                    Toast.makeText(mContext,"访问服务器失败",Toast.LENGTH_SHORT).show();
                    break;
                case SUCCESS:
                    builder = new AlertDialog.Builder(mContext);
                    builder.setIcon(R.mipmap.ic_launcher);
                    builder.setTitle("新消息");
                    View view2 = LayoutInflater.from(mContext).inflate(R.layout.non_referee_view,null);
                    TextView hit2 = (TextView) view2.findViewById(R.id.non_referee_hit);
                    Button cancel2 = (Button) view2.findViewById(R.id.non_referee);
                    hit2.setText("大侠，您已经接受挑战，请与他进行联系，进行线下比拼吧");
                    cancel2.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            viewHolder.ll_button.setVisibility(View.GONE);
                            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            textParams.setMarginStart(5);
                            viewHolder.content.setLayoutParams(textParams);
                        }
                    });
                    builder.setView(view2);
                    builder.setCancelable(false);
                    dialog = builder.create();
                    dialog.show();
                    break;
                case REFUSE:
                    builder = new AlertDialog.Builder(mContext);
                    builder.setIcon(R.mipmap.ic_launcher);
                    builder.setTitle("新消息");
                    View view1 = LayoutInflater.from(mContext).inflate(R.layout.non_referee_view,null);
                    TextView hit1 = (TextView) view1.findViewById(R.id.non_referee_hit);
                    Button cancel1 = (Button) view1.findViewById(R.id.non_referee);
                    hit1.setText("大侠，挑战只可以拒绝一次，您已经拒绝了对方的挑战");
                    cancel1.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            viewHolder.ll_button.setVisibility(View.GONE);
                            LinearLayout.LayoutParams textParams1 = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            textParams1.setMarginStart(5);
                            viewHolder.content.setLayoutParams(textParams1);
                        }
                    });
                    builder.setView(view1);
                    builder.setCancelable(false);
                    dialog = builder.create();
                    dialog.show();
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final MyNotification myNotification = list.get(position);
        userLoginActivity = new UserLoginActivity();
        history = new NotiHistory();
        preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        if (viewHolder == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.my_notification_item,null);//layout文件转化为convertView
            viewHolder.content = (TextView) convertView.findViewById(R.id.notification_content);
            viewHolder.mBtnAccept = (Button) convertView.findViewById(R.id.notification_accept);
            viewHolder.mBtnRefuse = (Button) convertView.findViewById(R.id.notification_refuse);
            viewHolder.ll_notification = (LinearLayout) convertView.findViewById(R.id.ll_notification);
            viewHolder.ll_button = (LinearLayout) convertView.findViewById(R.id.ll_button);
            if (myNotification.getType() == -1) {
                viewHolder.ll_button.setVisibility(View.GONE);
                LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                textParams.setMarginStart(5);
                viewHolder.content.setLayoutParams(textParams);
//                Log.e("您的PK申请被",myNotification.getCompetitor());
                viewHolder.content.setText("您的PK申请被"+myNotification.getCompetitor()+"拒绝了");
                history.setContent("您的PK申请被"+myNotification.getCompetitor()+"拒绝了");
                history.save();
            }
            else if (myNotification.getType() == 0){
                viewHolder.mBtnAccept.setVisibility(View.VISIBLE);
                viewHolder.mBtnRefuse.setVisibility(View.VISIBLE);
                viewHolder.content.setText(myNotification.getPkStudent1()+"和"+myNotification.getPkStudent2()
                        +"发起了挑战");
                history.setContent(myNotification.getPkStudent1()+"和"+myNotification.getPkStudent2()
                        +"发起了挑战");
                history.save();
                //拒绝
                viewHolder.mBtnRefuse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        OkHttpPostUtil.postPkTable(HANDLER_MESSAGE_URL + "0" + "&id=" + userLoginActivity.returnId(preferences), new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.getMessage();
                                Message message = Message.obtain();
                                message.what = ERROR;
                                mHandler.sendMessage(message);
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Message message = Message.obtain();
                                message.what = REFUSE;
                                mHandler.sendMessage(message);
//                                Log.e("您的PK申请被","拒绝挑战");
                            }
                        });
                    }
                });
                //接受
                viewHolder.mBtnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        OkHttpPostUtil.postPkTable(HANDLER_MESSAGE_URL + "1" + "&id=" + userLoginActivity.returnId(preferences), new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.getMessage();
                                Message message = Message.obtain();
                                message.what = ERROR;
                                mHandler.sendMessage(message);
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Message message = Message.obtain();
                                history.setContent("您的PK申请被接受了");
                                history.save();
                                message.what = SUCCESS;
                                mHandler.sendMessage(message);
                            }
                        });
                    }
                });
            }
            else if (myNotification.getType() == 1){
                if (myNotification.getResult() == -1){
                    viewHolder.ll_button.setVisibility(View.GONE);
                    LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    textParams.setMarginStart(5);
                    viewHolder.content.setLayoutParams(textParams);
                    viewHolder.content.setText("您和"+myNotification.getCompetitor()+"PK后的分数是："
                            +myNotification.getScore()+","+"结果败了");
                    history.setContent("您和"+myNotification.getCompetitor()+"PK后的分数是："
                            +myNotification.getScore()+","+"结果败了");
                    history.save();
                }else if (myNotification.getResult() == 0){
                    viewHolder.ll_button.setVisibility(View.GONE);
                    LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    textParams.setMarginStart(5);
                    viewHolder.content.setLayoutParams(textParams);
                    viewHolder.content.setText("您和"+myNotification.getCompetitor()+"PK后的分数是："
                            +myNotification.getScore()+","+"结果打平了");
                    history.setContent("您和"+myNotification.getCompetitor()+"PK后的分数是："
                            +myNotification.getScore()+","+"结果打平了");
                    history.save();
                }else {
                    viewHolder.ll_button.setVisibility(View.GONE);
                    LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    textParams.setMarginStart(5);
                    viewHolder.content.setLayoutParams(textParams);
                    viewHolder.content.setText("您和"+myNotification.getCompetitor()+"PK后的分数是："
                            +myNotification.getScore()+","+"结果赢了");
                    history.setContent("您和"+myNotification.getCompetitor()+"PK后的分数是："
                            +myNotification.getScore()+","+"结果赢了");
                    history.save();
                }
            }
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    static class ViewHolder{
        public TextView content;
        public Button mBtnAccept,mBtnRefuse;
        public LinearLayout ll_notification,ll_button;
    }
}
