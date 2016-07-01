package com.atguigu.beijingnews.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * 作者：杨光福 on 2016/6/8 13:58
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：内存缓存
 */
public class MemoryCacheUtils {
    private final LruCache<String, Bitmap> lruCache;

    public MemoryCacheUtils() {
        //用最大虚拟机内存的8分之1用于存储图片，kb
        int maxSize = (int) ((Runtime.getRuntime().maxMemory() / 1024) / 8);
        lruCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return (value.getRowBytes() * value.getHeight()) / 1024;
            }
        };
    }

    /**
     * 把图片放入到集合中LruCache
     * @param url
     * @param bitmap
     */
    public void putBitmap(String url, Bitmap bitmap) {
        lruCache.put(url, bitmap);
    }

    /**
     * 根据Url从内存获取图片
     * @param url
     * @return
     */
    public Bitmap getBitmapFromUrl(String url) {
        return lruCache.get(url);
    }
}
