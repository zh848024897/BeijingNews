package com.atguigu.beijingnews;

import android.app.Application;
import android.content.Context;

import com.atguigu.beijingnews.volley.VolleyManager;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.xutils.x;

import cn.jpush.android.api.JPushInterface;

/**
 * 作者：杨光福 on 2016/6/1 16:24
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：代表整个软件
 */
public class BeiingNewsApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);
        VolleyManager.init(this);
        //初始化极光推送
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
        initImageLoader(this);
        CrashHandler catchHandler = CrashHandler.getInstance();
        catchHandler.init(getApplicationContext());
    }


    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                //设置当前线程的优先级
                .threadPriority(Thread.NORM_PRIORITY - 2)
                        // 缓存显示不同大小的同一张图片
                .denyCacheImageMultipleSizesInMemory()
                        //将保存的时候的URI名称用MD5 加密
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }
}
