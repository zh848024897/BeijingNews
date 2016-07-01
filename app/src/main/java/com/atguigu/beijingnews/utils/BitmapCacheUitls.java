package com.atguigu.beijingnews.utils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

/**
 * 作者：杨光福 on 2016/6/8 10:46
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：图片三级缓存工具类
 */
public class BitmapCacheUitls {

    /**
     * 网络缓存工具类
     */
    private NetCacheUtils netCacheUtils;

    /**
     * 本地缓存工具类
     */
    private LocalCacheUtils localCacheUtils;

    /**
     * 内存缓存
     */
    private MemoryCacheUtils momeryCacheUtils;

    public BitmapCacheUitls(Handler handler) {
        momeryCacheUtils = new MemoryCacheUtils();
        localCacheUtils = new LocalCacheUtils(momeryCacheUtils);
        netCacheUtils = new NetCacheUtils(handler, localCacheUtils, momeryCacheUtils);
    }

    /**
     * *   从内存中取图片
     *   * 从本地文件中取图片
     *        向内存中保持一份
     *   * 请求网络图片，获取图片，显示到控件上
     *      * 向内存存一份
     *      * 向本地文件中存一份
     *
     * @param url
     * @param position
     * @return
     */

    public Bitmap getBitmapFromUrl(String url, int position) {
        //从内存中取图片
        if (momeryCacheUtils != null) {
            Bitmap bitmap = momeryCacheUtils.getBitmapFromUrl(url);
            if (bitmap != null) {
                Log.e("test", "从内存中加载图片==" + position);
                return bitmap;
            }
        }

        //从本地文件中取图片
        if (localCacheUtils != null) {

            Bitmap bitmap = localCacheUtils.getBitmapFromUrl(url);
            if (bitmap != null) {
                Log.e("test", "从本地加载图片==" + position);
                return bitmap;
            }
        }

        //请求网络图片，获取图片，显示到控件上
        if (netCacheUtils != null) {
            netCacheUtils.getBitmapFromNet(url, position);
        }

        return null;
    }
}
