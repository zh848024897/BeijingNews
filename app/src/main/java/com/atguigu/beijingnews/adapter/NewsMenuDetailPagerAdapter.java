package com.atguigu.beijingnews.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.atguigu.beijingnews.domain.NewsCenterBean2;
import com.atguigu.beijingnews.menudetailpager.TabDetailPager;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：杨光福 on 2016/6/13 14:46
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：xxxx
 */
public class NewsMenuDetailPagerAdapter  extends PagerAdapter {

    private final List<NewsCenterBean2.NewsCenterData.ChildrenData> childrens;
    private final ArrayList<TabDetailPager> detailPagers;

    public NewsMenuDetailPagerAdapter(List<NewsCenterBean2.NewsCenterData.ChildrenData> childrens, ArrayList<TabDetailPager> detailPagers) {
        this.childrens = childrens;
        this.detailPagers = detailPagers;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return childrens.get(position).getTitle();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TabDetailPager tabDetailPager = detailPagers.get(position);
        View view = tabDetailPager.rootView;
        tabDetailPager.initData();//初始化数据
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//            TabDetailPager tabDetailPager = detailPagers.get(position);
//            tabDetailPager.getInternalHander().removeCallbacksAndMessages(null);
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return detailPagers.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}