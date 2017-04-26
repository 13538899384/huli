package com.ygxy.xqm.huli.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by XQM on 2017/4/6.
 */

public class ImageUtil {
    /**
     * 获取网络address地址对应的图片
     * @param address
     * @return bitmap的类型
     */
    public static Bitmap getImage(String address) throws Exception{
        //通过代码 模拟器浏览器访问图片的流程
        URL url = new URL(address);
        HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        //获取服务器返回回来的流
        InputStream is = conn.getInputStream();
        byte[] imagebytes = StreamTool.getBytes(is);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imagebytes, 0, imagebytes.length);
        return bitmap;
    }
}
