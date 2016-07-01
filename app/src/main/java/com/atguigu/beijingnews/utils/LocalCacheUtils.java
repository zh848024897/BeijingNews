package com.atguigu.beijingnews.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 作者：杨光福 on 2016/6/8 11:35
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：本地缓存工具类
 */
public class LocalCacheUtils {

    private final MemoryCacheUtils momeryCacheUtils;

    public LocalCacheUtils(MemoryCacheUtils momeryCacheUtils) {
        this.momeryCacheUtils = momeryCacheUtils;

    }

    /**
     * 根据Uri缓存图片
     *
     * @param url
     * @param bitmap
     */
    public void putBitmap(String url, Bitmap bitmap) {
        //http://lbsyun.baidu.com/static/img/imgeditor/logo.gif--MD5--lsklkslkllklkskllkslkkls
        //mnt/sdcard/beijingnews/lsklkslkllklkskllkslkkls
        try {
            String fileName = MD5Encoder.encode(url);
            ////mnt/sdcard/
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/beijingnews";

            // //mnt/sdcard/beijingnews/lsklkslkllklkskllkslkkls
            File file = new File(path, fileName);

            File parentfile = file.getParentFile();
            if (!parentfile.exists()) {
                parentfile.mkdirs();
            }

            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /*
     根据Uril获取图片
     */
    public Bitmap getBitmapFromUrl(String url) {
        //http://lbsyun.baidu.com/static/img/imgeditor/logo.gif--MD5--lsklkslkllklkskllkslkkls
        //mnt/sdcard/beijingnews/lsklkslkllklkskllkslkkls
        try {
            String fileName = MD5Encoder.encode(url);
            ////mnt/sdcard/
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/beijingnews";

            // //mnt/sdcard/beijingnews/lsklkslkllklkskllkslkkls
            File file = new File(path, fileName);

            //文件已经保持过
            if (file.exists()) {

                FileInputStream fis = new FileInputStream(file);

                Bitmap bitmap = BitmapFactory.decodeStream(fis);

                if (bitmap != null) {
                    //保持到内存中
                    momeryCacheUtils.putBitmap(url,bitmap);
                }

                return bitmap;


            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }
}
