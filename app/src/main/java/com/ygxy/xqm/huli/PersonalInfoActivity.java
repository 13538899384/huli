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
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.squareup.picasso.Picasso;
import com.ygxy.xqm.huli.fragment.MineFragment;
import com.ygxy.xqm.huli.myview.CustomDialog;
import com.ygxy.xqm.huli.util.ImagePathUtils;
import com.ygxy.xqm.huli.util.OkHttpPostUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by XQM on 2017/3/27.
 */

public class PersonalInfoActivity extends AppCompatActivity{
    @BindView(R.id.mine_personal_toolbar)Toolbar mTbInfo;
    @BindView(R.id.per_btn_head)ImageView mIvHead;
    @BindView(R.id.per_tv_nickname)EditText mTvNickname;
    @BindView(R.id.per_btn_nickname)TextView mTvChange;
    private UserLoginActivity loginActivity;
    private SharedPreferences preferences;
    private Bitmap head;//头像
    private CustomDialog customDialog;
    private static String path = "/sdcard/myHead";//sd
    private final String CHANGE_NICKNAME_URL = "http://139.199.220.49:8080/user/modifyNickname/";
    public static final String UPLOAD_IMAGE_URL = "http://139.199.220.49:8080/qiniu/getToken";
    private String WRITE_TO_URL = "http://139.199.220.49:8080/user/uploadImage";
    private static String LOAD_IMAGE_URL = "http://139.199.220.49:8080/user/findUrl";
    private SharedPreferences.Editor editor;
    private String qiniuToken = "";
    private String token = null;
    private String TAG = "personal";
    private Boolean flag = true;
    @OnClick(R.id.per_head_ll)void per_head_ll(){
        showTypeDialog();
    }
    @OnClick(R.id.per_nickname_ll)void per_nickname_ll(){
        changeNickname();
    }

    /**
     * 更改昵称
     */
    private void changeNickname() {
        mTvChange.setText("保存");
        mTvNickname.setSelection(0);
        mTvNickname.setCursorVisible(true);
        mTvNickname.setFocusable(true);
        mTvNickname.requestFocus();
        mTvNickname.setFocusableInTouchMode(true);
        mTvChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String token = loginActivity.returnToken(preferences);
                final String nickname = mTvNickname.getText().toString();
//                Log.d("change",nickname);
                OkHttpPostUtil.postHeader(CHANGE_NICKNAME_URL + nickname, token, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(PersonalInfoActivity.this,"访问服务器出现异常",Toast.LENGTH_SHORT).show();
                                mTvNickname.setFocusable(false);
                                mTvNickname.setFocusableInTouchMode(false);
                                mTvChange.setText("修改");
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            final String nickname_message = jsonObject.optString("nickname_message");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (nickname_message.equals("修改成功")){
                                        Toast.makeText(PersonalInfoActivity.this,nickname_message,Toast.LENGTH_SHORT).show();
                                        queryFromBackground();
                                        mTvNickname.setFocusable(false);
                                        mTvNickname.setFocusableInTouchMode(false);
                                        mTvChange.setText("修改");
                                    }else {
                                        Toast.makeText(PersonalInfoActivity.this,"修改昵称失败",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                            mTvNickname.setFocusable(false);
                            mTvNickname.setFocusableInTouchMode(false);
                            mTvChange.setText("修改");
                        }
                    }
                });
            }
        });

    }

    /**
     * 弹出选择的对话框
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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_info);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setNickname();
//        if (imageUrl != null){
//            Glide.with(this).load(imageUrl).into(mIvHead);
////            Log.e(TAG,imageUrl);
//        }
//        else {
//            queryQiNiu();
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        setNickname();
    }

    /**
     * 处理返回的结果
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK){
                    cropPhoto(data.getData());// 裁剪图片
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
                    head = extras.getParcelable("data");
                    if (head != null) {
                        /**
                         * 上传服务器代码
                         */
                        // 新线程后台上传服务端
//                        customDialog.setTitle("正在上传图片，请稍候...");
                        customDialog.setMessage("正在上传图片，请稍候...");
                        customDialog.show();
                        String imagePath = ImagePathUtils.savePhoto(head, Environment
                                .getExternalStorageDirectory().getAbsolutePath(), String
                                .valueOf(System.currentTimeMillis()));
                        if (uploadImg()){
                            setPicToView(head);// 保存在SD卡中
//                            mIvHead.setImageBitmap(head);// 用ImageView显示出来
                            editor.putString("imageUrl",imagePath);
                            editor.apply();
                            Glide.with(this).load(imagePath).into(mIvHead);
                        }else {
                            return;
//                            customDialog.show();
//                            customDialog.dismiss();
                        }
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 上传图片到七牛
     */
    private Boolean uploadImg() {
        new Thread(new Runnable() {
            @Override
            public void run() {
               //国内https上传
                boolean https = true;
                Zone z1 = new AutoZone(https, null);
                Configuration config = new Configuration.Builder().zone(z1).build();
                String datatime = String.valueOf(new Date().getTime());
                String key= "http://onohffsqv.bkt.clouddn.com/" + datatime + "springboot-app.png";
                Log.e(TAG,getUploadToken());
//                Log.e(TAG,key);
                UploadManager uploadManager = new UploadManager(config);
                final String imagePath = ImagePathUtils.savePhoto(head, Environment
                        .getExternalStorageDirectory().getAbsolutePath(), String
                        .valueOf(System.currentTimeMillis()));
                qiniuToken = getUploadToken();
                uploadManager.put(imagePath, key, getUploadToken(), new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        if(info.isOK())
                        {
                            Log.i("qiniu", "Upload Success");
                            OkHttpPostUtil.postChangePassword(WRITE_TO_URL, token,
                                    OkHttpPostUtil.loadImageJson(key), new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            e.getMessage();
                                            flag = false;
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    customDialog.dismiss();
                                                }
                                            });
                                        }
                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            String result = response.body().string();
                                            JSONObject jsonObject = null;
                                            try {
                                                jsonObject = new JSONObject(result);
                                                String message = jsonObject.optString("upload_message");
                                                if (message.equals("上传成功")){
                                                    flag = true;
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    customDialog.dismiss();
                                                }
                                            });
                                        }
                                    });
                        }
                        else{
                            flag = false;
                            Log.i("qiniu", "Upload Fail");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(PersonalInfoActivity.this,"上传头像失败",Toast.LENGTH_SHORT).show();
                                    customDialog.dismiss();
                                }
                            });
                            //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                        }
                        Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + response);
                    }
                },null);
            }
        }).start();
        return flag;
    }

    /**
     * 获取图片路径
     * @return
     */
    public static String getImagePath(SharedPreferences preferences){
        String imagePath = preferences.getString("imagePath",null);
        if (imagePath != null) {
            return imagePath;
        }
//        Log.d("imagePath",imagePath);
        return imagePath;
    }

    /**
     *  获得七牛上传凭证uploadtoken
     * @return
     */
    private String getUploadToken(){
        OkHttpPostUtil.postPkTable(UPLOAD_IMAGE_URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.getMessage();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    qiniuToken = jsonObject.optString("七牛云上传token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return qiniuToken;
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
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    /**
     * 保存图片到sd卡
     * @param mBitmap
     */
    private void setPicToView(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹
        String fileName = path + "head.jpg";// 图片名字
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 初始化
     */
    private void initData() {
        setSupportActionBar(mTbInfo);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        mTbInfo.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        loginActivity = new UserLoginActivity();
        customDialog = new CustomDialog(this,R.style.CustomDialog);
        mTvChange.setText("修改");
        mTvNickname.setCursorVisible(false);
        mTvNickname.setFocusable(false);
        mTvNickname.setFocusableInTouchMode(false);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        token = loginActivity.returnToken(preferences);
        queryQiNiu();
    }

    /**
     * 如果SD里面没有则需要从服务器取头像，取回来的头像再保存在SD中
     * @return
     */
    private void queryQiNiu() {
        OkHttpPostUtil.postHeader(LOAD_IMAGE_URL, loginActivity.returnToken(preferences), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.getMessage();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PersonalInfoActivity.this,"访问服务器出现异常",Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    final String url = jsonObject.optString("url");
//                    Log.e(TAG,url);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (url != null){
                                    Picasso.with(PersonalInfoActivity.this).load("http://onohffsqv.bkt.clouddn.com/"+url).into(mIvHead);
                                    editor.putString("imageUrl","http://onohffsqv.bkt.clouddn.com/"+url);
                                    editor.apply();
                                }else {
                                    Picasso.with(PersonalInfoActivity.this).load(R.mipmap.icon_logo).into(mIvHead);
                                }
//                                Bitmap bitmap = ImageUtil.getImage("http://onohffsqv.bkt.clouddn.com/"+url);
//                                Drawable drawable = new BitmapDrawable(bitmap);// 转换成drawable
//                                mIvHead.setImageDrawable(drawable);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 查询昵称，先从本地查，没有再从服务器查
     */
    private void setNickname() {
        queryFromBackground();
//        if (queryFromLocate()){
//            mTvNickname.setText(preferences.getString("nickname",null));
//            Log.d("nick",preferences.getString("nickname",null));
//        }else {
//            queryFromBackground();
//        }
    }

    /**
     * 从本地查询昵称
     * @return
     */
    private boolean queryFromLocate() {
        Boolean status = preferences.getBoolean("ok",false);
        if (status == true){
            Log.d("status", String.valueOf(status));
            return true;
        }
        return false;
    }

    /**
     * 从服务器查询昵称
     */
    private void queryFromBackground() {
//        String token = loginActivity.returnToken(preferences);
        OkHttpPostUtil.postHeader(MineFragment.FIND_NICKNAME_URL, token, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PersonalInfoActivity.this,"访问服务器出现异常",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    final String mess = jsonObject.optString("message");
                    final String nickname = jsonObject.optString("nickname");
//                    Log.d("nickname",nickname);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mess.equals("token无效，请重新登录") == false){
                                mTvNickname.setText(nickname);
                                editor.putBoolean("ok",true);
                                editor.putString("nickname",nickname);
                                editor.apply();
                            }else {
                                return;
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 实现跳转
     * @param context
     */
    public static void actionStar(Context context){
        Intent intent = new Intent(context,PersonalInfoActivity.class);
        context.startActivity(intent);
    }

    public String returnImageUrl(SharedPreferences preferences){
        return preferences.getString("imageUrl",null);
    }

}
