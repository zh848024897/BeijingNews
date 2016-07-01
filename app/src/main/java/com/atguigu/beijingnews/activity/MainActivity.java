package com.atguigu.beijingnews.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.fragment.ContentFragment;
import com.atguigu.beijingnews.fragment.LeftMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * 作者：杨光福 on 2016/6/1 14:27
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：主界面20160702 2
 */
public class MainActivity extends SlidingFragmentActivity{

    public static final String LEFTMENU_TAG = "leftmenu_tag";
    public static final String CONTENT_TAG = "content_tag";
    private int screenWidth = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        //1.设置主页面
        setContentView(R.layout.content);
        //2.设置左侧菜单
        setBehindContentView(R.layout.leftmenu);

        //3.设置右侧菜单
        SlidingMenu slidingMenu = getSlidingMenu();
//        slidingMenu.setSecondaryMenu(R.layout.leftmenu);

        //4.设置模式：左侧菜单+主页面；主页面+右侧菜单；左侧菜单+主页面+右侧菜单
        slidingMenu.setMode(SlidingMenu.LEFT);


        //5.设置滑动区域：全屏滑动，边缘滑动，不可以滑动
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);


        //6.设置主页面占200像数
//        slidingMenu.setBehindOffset(DensityUtil.dip2px(this,200));
        slidingMenu.setBehindOffset((int) (screenWidth*0.625));


        //把两个Fragment添加到MainActivity中
        initFragment();

    }

    private void initFragment() {
        //1.得到Fragemnt管理者
        FragmentManager fm =  getSupportFragmentManager();
        //2.开启事情
        FragmentTransaction transaction = fm.beginTransaction();
        //3.替换LeftFragment和ContentFragment
        transaction.replace(R.id.fl_leftmenu,new LeftMenuFragment(), LEFTMENU_TAG);
        transaction.replace(R.id.fl_content, new ContentFragment(), CONTENT_TAG);
        //4.提交
        transaction.commit();

    }

    /**
     * 得到左侧菜单
     * @return
     */
    public LeftMenuFragment getLeftMenuFragment() {
        FragmentManager fm =  getSupportFragmentManager();
        LeftMenuFragment leftMenuFragment = (LeftMenuFragment) fm.findFragmentByTag(LEFTMENU_TAG);
        return leftMenuFragment;
    }

    /**
     * 得到正文Fragment
     * @return
     */
    public ContentFragment getContentFragment() {
        FragmentManager fm =  getSupportFragmentManager();
        ContentFragment contentFragment = (ContentFragment) fm.findFragmentByTag(CONTENT_TAG);
        return contentFragment;
    }
}
