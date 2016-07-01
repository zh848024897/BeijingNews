package com.atguigu.beijingnews.base;

import android.content.Context;
import android.view.View;

/**
 * 作者：杨光福 on 2016/6/3 14:29
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：菜单详情页面的基类
 */
public abstract class MenuDetailBasePager {

    /**
     * 上下文
     */
    public Context context;

    /**
     * 用来显示页面
     */
    public View rootView;


    /**
     * 构造方法
     * @param context
     */
    public MenuDetailBasePager(Context context) {
        this.context = context;
        rootView = initView();

    }

    /**
     * 由孩子强制实现该方法，实现自己特有的效果
     * @return
     */
    public abstract View initView() ;

    /**
     * 当子页面需要初始化自己的数据的时候，重写方法
     */
    public void initData(){

    }
}
