package com.ygxy.xqm.huli;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qiniu.android.common.AutoZone;
import com.qiniu.android.common.Zone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.ygxy.xqm.huli.myview.CustomDialog;
import com.ygxy.xqm.huli.util.ImagePathUtils;
import com.ygxy.xqm.huli.util.OkHttpPostUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by XQM on 2017/4/9.
 */

public class FeedbackActivity extends AppCompatActivity {
    @BindView(R.id.feedback_toolbar)Toolbar toolbar;
    @BindView(R.id.mine_feedback_add)ImageView mImgAdd;
    @BindView(R.id.mine_feedback_publish)TextView mTvPublish;
    private static final String CAMERA_DIR = "/dcim/";
    private static final String albumName ="CameraSample";
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    private String MINE_FEEDBACK_URL = "http://139.199.220.49:8080/suggestion/submit";
    private SharedPreferences preferences;
    private UserLoginActivity loginActivity;
    private Bitmap photo;//头像
    private String token = null;
    private String qiniuToken = "";
    private Boolean flag = true;
    private CustomDialog customDialog;
    // 图片名
    public String name;
    private String url;//上传七牛云返回的
    private String key = null;

    //添加照片
    @OnClick(R.id.mine_feedback_add)void mine_feedback_add(){
        showTypeDialog();
    }
    //上传照片
    @OnClick(R.id.feedback_upload_photos)void feedback_upload_photos(){
        loadImageToQiNiu();
    }
    //写入数据库
    @OnClick(R.id.feedback_submit)void feedback_submit(){
        postToBackground();
    }

    /**
     * 上传照片到七牛云
     * @return
     */
    private Boolean loadImageToQiNiu() {
        showDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //国内https上传
                boolean https = true;
                Zone z1 = new AutoZone(https, null);
                Configuration config = new Configuration.Builder().zone(z1).build();
                String datatime = String.valueOf(new Date().getTime());
                key= "http://onohffsqv.bkt.clouddn.com/" + datatime + "springboot-app.png";
                Log.e("qiniu",getUploadToken());
                UploadManager uploadManager = new UploadManager(config);
                final String imagePath = ImagePathUtils.savePhoto(photo, Environment
                        .getExternalStorageDirectory().getAbsolutePath(), String
                        .valueOf(System.currentTimeMillis()));
                if (imagePath != null){
                    qiniuToken = getUploadToken();
                    uploadManager.put(imagePath, key, qiniuToken, new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject response) {
//                            Log.e("qiniu", qiniuToken);
                            if(info.isOK())
                            {
                                Log.i("qiniu", "Upload Success");
                                flag = true;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        closeDialog();
                                    }
                                });
                            }
                            else{
                                Log.i("qiniu", "Upload Fail");
                                //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                                flag = false;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        closeDialog();
                                    }
                                });
                            }
//                            Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + response);
                        }
                    },null);
                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeDialog();
                        }
                    });
                }
            }
        }).start();
        return flag;
    }

    /**
     *  获得七牛上传凭证uploadtoken
     * @return
     */
    private String getUploadToken(){
        OkHttpPostUtil.postPkTable(PersonalInfoActivity.UPLOAD_IMAGE_URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.getMessage();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
//                JSONObject jsonObject = null;
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    qiniuToken = jsonObject.optString("七牛云上传token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return qiniuToken;
    }

    /**
     * 将反馈信息写入数据库
     */
    private void postToBackground() {
        if (loadImageToQiNiu() == true){
            showDialog();
            token = loginActivity.returnToken(preferences);
            String content = mTvPublish.getText().toString();
            if (!TextUtils.isEmpty(content)){
                OkHttpPostUtil.postChangePassword(MINE_FEEDBACK_URL, token, OkHttpPostUtil.submitSuggestionJson(content,key)
                        , new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.getMessage();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(FeedbackActivity.this,"访问服务器出现异常",Toast.LENGTH_SHORT).show();
                                        closeDialog();
                                    }
                                });
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String result = response.body().string();
                                Log.e("feedback",result);
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    final String message = jsonObject.optString("message");
                                    final String suggestion_message = jsonObject.optString("suggestion_message");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (suggestion_message != null ){
                                                Toast.makeText(FeedbackActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
                                                closeDialog();
                                                finish();
                                            }
                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }else {
                Toast.makeText(FeedbackActivity.this,"请输入内容",Toast.LENGTH_SHORT).show();
                closeDialog();
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 选择上传照片的方式
     */
    private void showTypeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_select_photo, null);
        TextView tv_select_gallery = (TextView) view.findViewById(R.id.tv_select_gallery);
        TextView tv_select_camera = (TextView) view.findViewById(R.id.tv_select_camera);
        TextView tv_cancel_gallery = (TextView) view.findViewById(R.id.tv_cancel_gallery);
        tv_select_gallery.setOnClickListener(new View.OnClickListener() {// 在相册中选取
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1, 1);
                dialog.dismiss();
            }
        });
        tv_select_gallery.setOnClickListener(new View.OnClickListener() {// 在相册中选取
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1, 1);
                dialog.dismiss();
            }
        });
        tv_select_camera.setOnClickListener(new View.OnClickListener() {// 调用照相机
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //下面这句指定调用相机拍照后的照片存储的路径,head.jpg为一个临时文件，每次拍照后这个图片都会被替换
                intent2.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "head.jpg")));
                startActivityForResult(intent2, 2);// 采用ForResult打开
                dialog.dismiss();
            }
        });
        tv_cancel_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    /**
     * 初始化数据
     */
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
        loginActivity = new UserLoginActivity();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    private void showDialog(){
        if(customDialog == null){
            customDialog = new CustomDialog(this,R.style.CustomDialog);
//            progressDialog.setMessage("正在上传，请稍后...");
//            progressDialog.setCanceledOnTouchOutside(false);
            customDialog.show();
        }
    }

    protected void closeDialog(){
        if (customDialog != null){
            customDialog.dismiss();
        }
    }

    /**
     * 返回的结果
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK && data != null){
                    cropPhoto(data.getData());// 裁剪图片
                }else {
                    return;
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory() + "/head.jpg");
                    cropPhoto(Uri.fromFile(temp));// 裁剪图片
                }
                break;
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    photo = extras.getParcelable("data");
                    if (photo != null) {
                        /**
                         * 上传服务器代码
                         */
                        // 新线程后台上传服务端
//                        progressDialog.show(this, null, "正在上传图片，请稍候...");
                        String imagePath = ImagePathUtils.savePhoto(photo, Environment
                                .getExternalStorageDirectory().getAbsolutePath(), String
                                .valueOf(System.currentTimeMillis()));
//                        String path = OkHttpPostUtil.loadImageJson(imagePath);
                        Glide.with(this).load(imagePath).into(mImgAdd);
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 调用系统的裁剪功能
     *
     * @param uri
     */
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        // outputX outputY 是裁剪图片宽高
//        intent.putExtra("outputX", 150);
//        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    /**
     * 实现跳转
     * @param context
     */
    static public void starAction(Context context){
        Intent intent = new Intent(context,FeedbackActivity.class);
        context.startActivity(intent);
    }
}
