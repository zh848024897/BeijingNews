package com.atguigu.beijingnews.base;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.activity.MainActivity;

/**
 * 作者：杨光福 on 2016/6/3 09:18
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：基类
 * <p/>
 * 标题栏-RelativeLayout
 * 内容 - FrameLayout
 * 它是首页，新闻，智慧，政要，设置的基类
 */
public class BasePager {

    /**
     * 上下文
     */
    public Context context;

    /**
     * 用来显示页面
     */
    public View rootView;

    /**
     * 标题
     */
    public TextView tv_title;

    /**
     * 菜单按钮
     */
    public ImageButton ib_menu;

    /**
     * 孩子的视图，添加到该布局
     */
    public FrameLayout fl_content;

    public ImageButton ib_switch_gird_list;

    public Button btn_cart;

    /**
     * 构造方法
     * @param context
     */
    public BasePager(Context context) {
        this.context = context;
        rootView = initView();

    }

    private View initView() {
        View view = View.inflate(context, R.layout.basepager,null);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        ib_menu = (ImageButton) view.findViewById(R.id.ib_menu);
        fl_content = (FrameLayout) view.findViewById(R.id.fl_content);
        ib_switch_gird_list = (ImageButton) view.findViewById(R.id.ib_switch_gird_list);
        btn_cart = (Button) view.findViewById(R.id.btn_cart);
        ib_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) context;
                mainActivity.getSlidingMenu().toggle();
            }
        });
        return view;
    }

    /**
     * 当子页面需要初始化自己的数据的时候，重写方法
     */
    public void initData(){

    }
}
