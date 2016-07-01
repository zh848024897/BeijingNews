package com.atguigu.beijingnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.atguigu.beijingnews.base.BasePager;
import com.atguigu.beijingnews.utils.LogUtil;

/**
 * 作者：杨光福 on 2016/6/3 09:34
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：主页面
 */
public class HomePager extends BasePager {
    /**
     * 构造方法
     *
     * @param context
     */
    public HomePager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("首页的数据被初始化了");
        //1.设置标题
        tv_title.setText("首页");

        //2.创建视图
        TextView textView = new TextView(context);
        textView.setText("首页的内容");
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(25);

        //3.把视图添加到FrameLayout
        fl_content.addView(textView);
    }
}
