package com.ygxy.xqm.huli.util;

import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by XQM on 2017/3/22.
 */

public class OkHttpPostUtil {
    public final static int CONNECT_TIMEOUT = 60;
    public final static int READ_TIMEOUT = 100;
    public final static int WRITE_TIMEOUT = 60;
    static OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)//设置写的超时时间
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
            .build();
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public void register(String url, String json,Callback callback){
        RequestBody body = RequestBody.create(JSON,json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void postHeader(String url, String header, Callback callback){
        RequestBody body = RequestBody.create(JSON,"{\"data\":\"000\"}");
        Request request = new Request.Builder()
                .header("token",header)
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void postChangePassword(String url,String header,String json,Callback callback){
        RequestBody body = RequestBody.create(JSON,json);
        Request request = new Request.Builder()
                .header("token",header)
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void postPkTable(String url,Callback callback){
        RequestBody body = RequestBody.create(JSON,"{\"data\":\"000\"}");
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public String loginJson(String userId,String password){
        return "{\"studentNumber\":\""+userId+"\""+",\"password\":\""+password+"\"}";
    }

    public static String loadImageJson(String path){
        return "{\"url\":\""+path+"\"}";
    }

    public static String submitSuggestionJson(String content,String path){
        return "{\"content\":\""+content+"\""+",\"url\":\""+path+"\"}";
    }
    public String bolwingJson(String userId,String nickname,String userPhone, String password,String resetPassword) {
//        JSONObject jsonObject = new JSONObject();
//        JSONArray jsonArray = new JSONArray();
//        String json = null;
//        try {
//            jsonObject.put("studentNumber",userId);
//            jsonObject.put("phoneNumber",userPhone);
//            jsonObject.put("password",password);
//            jsonObject.put("repeatPassword",resetPassword);
//            jsonArray.put(jsonObject);
//            json = "{\"\":"+jsonArray.toString()+"}";
//            return json;
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        //return json;
        return "{\"password\":\""+password+"\""+",\"phoneNumber\":\""+userPhone+"\""+"" +
                ",\"repeatPassword\":\""+resetPassword+"\""+"," +
                "\"studentNumber\":\""+userId+"\""+",\"nickname\":\""+nickname+"\"}";
    }

    public String forgetwordJson(String userId,String userPhone, String password,String resetPassword){
        return "{\"password\":\""+password+"\""+",\"phoneNumber\":\""+userPhone+"\""+"" +
                ",\"repeatPassword\":\""+resetPassword+"\""+"," +
                "\"studentNumber\":\""+userId+"\"}";
    }

    public String changePasswordJson(String newPassword, String admitPassword){
        return "{\"password\":\""+newPassword+"\""+",\"repeatPassword\":\""+admitPassword+"\"}";
    }
}
