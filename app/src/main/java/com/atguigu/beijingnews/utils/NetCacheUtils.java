package com.atguigu.beijingnews.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 作者：杨光福 on 2016/6/8 10:50
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：网络缓存工具类
 */
public class NetCacheUtils {
    /**
     * 当请求图片成功的时候发这个消息
     */
    public static final int SUCCESS = 1;
    /**
     * 当请求图片失败的时候发这个消息
     */
    public static final int FAIL = 2;
    private final Handler handler;
    /**
     * 线程池
     */
    private final ExecutorService service;
    /**
     * 本地缓存工具类
     */
    private final LocalCacheUtils localCacheUtils;
    /**
     * 内存缓存工具类
     */
    private final MemoryCacheUtils momeryCacheUtils;

    public NetCacheUtils(Handler handler, LocalCacheUtils localCacheUtils, MemoryCacheUtils momeryCacheUtils) {
        this.handler = handler;
        service = Executors.newFixedThreadPool(10);
        this.localCacheUtils = localCacheUtils;
        this.momeryCacheUtils = momeryCacheUtils;
    }

    public void getBitmapFromNet(String url, int position) {
        service.execute(new MyRunnable(url, position));
//        new Thread(new MyRunnable(url,position)).start();
    }

    class MyRunnable implements Runnable {

        private final String url;
        private final int position;

        public MyRunnable(String url, int position) {
            this.url = url;
            this.position = position;

        }

        @Override
        public void run() {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("GET");//不能小写
                connection.setConnectTimeout(4000);
                connection.setReadTimeout(4000);
                connection.connect();
                int code = connection.getResponseCode();
                if (code == 200) {
                    //联网成功
                    InputStream is = connection.getInputStream();

                    Bitmap bitmap = BitmapFactory.decodeStream(is);


                    //发消息：bitmap 和postion
                    Message msg = Message.obtain();
                    msg.what = SUCCESS;
                    msg.obj = bitmap;
                    msg.arg1 = position;
                    handler.sendMessage(msg);

                    //保持一份到本地
                    localCacheUtils.putBitmap(url, bitmap);

                    //保持一份到内存中
                    momeryCacheUtils.putBitmap(url, bitmap);


                }


            } catch (IOException e) {
                e.printStackTrace();

                //发消息：bitmap 和postion
                Message msg = Message.obtain();
                msg.what = FAIL;
                msg.arg1 = position;
                handler.sendMessage(msg);
            }

        }
    }
}
