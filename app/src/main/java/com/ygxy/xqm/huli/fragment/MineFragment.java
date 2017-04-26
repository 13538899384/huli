package com.ygxy.xqm.huli.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.ygxy.xqm.huli.AccountManageActivity;
import com.ygxy.xqm.huli.FeedbackActivity;
import com.ygxy.xqm.huli.MyGoldActivity;
import com.ygxy.xqm.huli.MyNotificationActivity;
import com.ygxy.xqm.huli.PersonalInfoActivity;
import com.ygxy.xqm.huli.R;
import com.ygxy.xqm.huli.UserLoginActivity;
import com.ygxy.xqm.huli.myview.MyTopbar;
import com.ygxy.xqm.huli.util.AppConfig;
import com.ygxy.xqm.huli.util.DataCleanManager;
import com.ygxy.xqm.huli.util.FileUtil;
import com.ygxy.xqm.huli.util.MethodsCompat;
import com.ygxy.xqm.huli.util.OkHttpPostUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.Core;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by XQM on 2017/3/16.
 */

public class MineFragment extends Fragment{
    @BindView(R.id.mine_bar)
    MyTopbar myTopbar;
    @BindView(R.id.mine_clear_num)TextView mTvClear;
    @BindView(R.id.mine_nickname_string) TextView mTvSetNickname;
    @BindView(R.id.mine_logo)CircleImageView mIvHead;
//    @BindView(R.id.mine_account_manager)
//    RelativeLayout mRlAccountManager;
    private final int CLEAN_SUC=1001;
    private final int CLEAN_FAIL=1002;
    public final int FAILTRUE = 0;
    public final int RESPONSE = 1;
    public final int LOAD = 2;
    public final int NOT_LOAD = 3;
    public static final String FIND_NICKNAME_URL = "http://139.199.220.49:8080/user/findNickname";
    private UserLoginActivity loginActivity;
    private PersonalInfoActivity personalInfoActivity;
    private SharedPreferences preferences;
    private Message message;
    private String nickname;
    @OnClick(R.id.mine_account_manager) void mine_account_manager(){
        AccountManageActivity.actionStar(getActivity());
    }

    @OnClick(R.id.mine_gold) void mine_gold(){
        MyGoldActivity.actionStar(getActivity());
    }

    @OnClick(R.id.mine_personal_info)void mine_personal_info(){
        PersonalInfoActivity.actionStar(getActivity());
    }

    @OnClick(R.id.mine_clear_num)void mine_clear_num(){
        onClickCleanCache();
    }

    @OnClick(R.id.mine_feedback)void mine_feedback(){
        FeedbackActivity.starAction(getActivity());
    }

    @OnClick(R.id.mine_notification)void mine_notification(){
        MyNotificationActivity.starAction(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mine,container,false);
        ButterKnife.bind(this,view);
        caculateCacheSize();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myTopbar.setMainTitle("我的");
        loginActivity = new UserLoginActivity();
        personalInfoActivity = new PersonalInfoActivity();
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        caculateCacheSize();
    }

    @Override
    public void onResume() {
        super.onResume();
        setNickname();
        String imageUrl = personalInfoActivity.returnImageUrl(preferences);
        if (imageUrl != null){
            Glide.with(getActivity()).load(imageUrl).into(mIvHead);
            Log.e("image",imageUrl);
        }else {
            Glide.with(getActivity()).load(R.mipmap.icon_logo).into(mIvHead);
        }
    }

    private void setNickname() {
        String token = loginActivity.returnToken(preferences);
//        Log.d("result",token);
        OkHttpPostUtil.postHeader(FIND_NICKNAME_URL, token, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.getMessage();
                message = Message.obtain();
                message.what = FAILTRUE;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    String mess = jsonObject.optString("message");
                    nickname = jsonObject.optString("nickname");
//                    Log.d("nickname",nickname);
                    if (mess.equals("token无效，请重新登录") == false){
                        message = Message.obtain();
                        Bundle bundle = new Bundle();
                        bundle.putString("nickname",nickname);
                        message.setData(bundle);
                        message.what = RESPONSE;
                        handler.sendMessage(message);
//                        message = Message.obtain();
//                        message.what = CHANGE;
//                        handler.sendMessage(message);
                    }else {
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    message = Message.obtain();
                    message.what = FAILTRUE;
                    handler.sendMessage(message);
                }
            }
        });
    }

    /**
     * 提示用户是否清理缓存
     */
    private void onClickCleanCache() {
        getConfirmDialog(getActivity(), "是否清空缓存?", new DialogInterface.OnClickListener
                () {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                clearAppCache();
                mTvClear.setText("0KB");
            }
        }).show();
    }

    /**
     * 计算缓存的大小
     */
    private void caculateCacheSize() {
        long fileSize = 0;
        String cacheSize = "0KB";
        File filesDir = getActivity().getFilesDir();
        File cacheDir = getActivity().getCacheDir();

        fileSize += FileUtil.getDirSize(filesDir);
        fileSize += FileUtil.getDirSize(cacheDir);
        // 2.2版本才有将应用缓存转移到sd卡的功能
        if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
            File externalCacheDir = MethodsCompat
                    .getExternalCacheDir(getActivity());
            fileSize += FileUtil.getDirSize(externalCacheDir);
            fileSize += FileUtil.getDirSize(new File(
                    org.kymjs.kjframe.utils.FileUtils.getSDCardPath()
                            + File.separator + "KJLibrary/cache"));
        }
        if (fileSize > 0)
            cacheSize = FileUtil.formatFileSize(fileSize);
        mTvClear.setText(cacheSize);
    }

    public static boolean isMethodsCompat(int VersionCode) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }
    /**
     * 清除app缓存
     */
    public void myclearaAppCache() {
        DataCleanManager.cleanDatabases(getActivity());
        // 清除数据缓存
        DataCleanManager.cleanInternalCache(getActivity());
        // 2.2版本才有将应用缓存转移到sd卡的功能
//        if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
//            DataCleanManager.cleanCustomCache(MethodsCompat
//                    .getExternalCacheDir(getActivity()));
//        }
        // 清除编辑器保存的临时内容
        Properties props = getProperties();
        for (Object key : props.keySet()) {
            String _key = key.toString();
            if (_key.startsWith("temp"))
                removeProperty(_key);
        }
        Core.getKJBitmap().cleanCache();
    }

    /**
     * 清除保存的缓存
     */
    public Properties getProperties() {
        return AppConfig.getAppConfig(getActivity()).get();
    }
    public void removeProperty(String... key) {
        AppConfig.getAppConfig(getActivity()).remove(key);
    }

    /**
     * 清除app缓存
     *
     * @param
     */
    public void clearAppCache() {

        new Thread() {
            @Override
            public void run() {
                Message msg = new Message();
                try {
                    myclearaAppCache();
                    msg.what = CLEAN_SUC;
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = CLEAN_FAIL;
                }
                handler.sendMessage(msg);
            }
        }.start();
    }
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case CLEAN_FAIL:
                    Toast.makeText(getActivity(),"清除失败",Toast.LENGTH_SHORT).show();
                    break;
                case CLEAN_SUC:
                    Toast.makeText(getActivity(),"清除成功",Toast.LENGTH_SHORT).show();
                    break;
                case FAILTRUE:
                    Toast.makeText(getActivity(),"访问服务器出现异常",Toast.LENGTH_SHORT).show();
                    break;
                case RESPONSE:
                    Bundle bundle = msg.getData();
//                    Log.d("bundle",bundle.getString("nickname"));
                    mTvSetNickname.setText(bundle.getString("nickname"));
                    break;
                case LOAD:
                    Bundle bundle1 = msg.getData();
                    Log.e("url",bundle1.getString("url"));
                    Picasso.with(getActivity()).load("http://onohffsqv.bkt.clouddn.com/"+bundle1.getString("url")).into(mIvHead);
                    break;
                case NOT_LOAD:
                    Picasso.with(getActivity()).load(R.mipmap.ic_launcher).into(mIvHead);
                    break;
                default:
                    break;

            }
        }
    };

    /**
     * 创建对话框对象
     * @param context
     * @param message
     * @param onClickListener
     * @return
     */
    public static AlertDialog.Builder getConfirmDialog(Context context, String message, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setMessage(Html.fromHtml(message));
        builder.setPositiveButton("确定", onClickListener);
        builder.setNegativeButton("取消", null);
        return builder;
    }

    /**
     * 获取对话框
     * @param context
     * @return
     */
    public static AlertDialog.Builder getDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        return builder;
    }
}
