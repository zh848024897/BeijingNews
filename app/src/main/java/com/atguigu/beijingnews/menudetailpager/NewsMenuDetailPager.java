package com.atguigu.beijingnews.menudetailpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.activity.MainActivity;
import com.atguigu.beijingnews.adapter.NewsMenuDetailPagerAdapter;
import com.atguigu.beijingnews.base.MenuDetailBasePager;
import com.atguigu.beijingnews.domain.NewsCenterBean2;
import com.atguigu.beijingnews.utils.LogUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：杨光福 on 2016/6/3 14:33
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：新闻菜单详情页面
 */
public class NewsMenuDetailPager extends MenuDetailBasePager {


    /**
     * 页签页面的数据集合
     */
    private final List<NewsCenterBean2.NewsCenterData.ChildrenData> childrens;
    /**
     * 左侧菜单对应的新闻详情页面的数据
     */
    @ViewInject(R.id.vp_newsmenu_detailpager)
    private ViewPager mVpNewsmenuDetailpager;


    @ViewInject(R.id.indicator)
    private TabPageIndicator indicator;

    /**
     * 页签页面的集合
     */
    private ArrayList<TabDetailPager> detailPagers;

    /**
     * 构造方法
     *
     * @param context
     * @param newsCenterData
     */
    public NewsMenuDetailPager(Context context, NewsCenterBean2.NewsCenterData newsCenterData) {
        super(context);
        this.childrens = newsCenterData.getChildren();
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.newsmenu_detailpager, null);
        x.view().inject(this, view);
        return view;
    }

    /**
     * XUtils3点击事件
     * @param view
     */
    @Event(value = R.id.tab_next)
    private void nextTab(View view) {
        mVpNewsmenuDetailpager.setCurrentItem(mVpNewsmenuDetailpager.getCurrentItem() + 1);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("新闻详情页面的数据被初始化了...");
        //1.准备数据-页面
        detailPagers = new ArrayList<>();
        for (int i = 0; i < childrens.size(); i++) {

            detailPagers.add(new TabDetailPager(context, childrens.get(i)));
        }

        //2.设置适配器
        mVpNewsmenuDetailpager.setAdapter(new NewsMenuDetailPagerAdapter(childrens,detailPagers));


        //关联ViewPager
        indicator.setViewPager(mVpNewsmenuDetailpager);

        //以后监听页面的变化，用TabPageIndicator监听页面的改变

        indicator.setOnPageChangeListener(new MyOnPageChangeListener());

        indicator.setCurrentItem(0);
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if(position==0){
                //SlidingMenu可以滑动
                isEnableSlidemenu(true);
            }else{
                //不可以滑动
                isEnableSlidemenu(false);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * 是否让左侧菜单可以滑动
     * @param isEnableSlidemenu
     */
    private void isEnableSlidemenu(Boolean isEnableSlidemenu) {
        MainActivity mainActivity = (MainActivity) context;
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
        if (isEnableSlidemenu) {
            //可以滑动
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            //不可以滑动
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }

    }

}
