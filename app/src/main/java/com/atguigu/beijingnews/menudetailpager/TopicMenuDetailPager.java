package com.atguigu.beijingnews.menudetailpager;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.activity.MainActivity;
import com.atguigu.beijingnews.base.MenuDetailBasePager;
import com.atguigu.beijingnews.domain.NewsCenterBean2;
import com.atguigu.beijingnews.utils.LogUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：杨光福 on 2016/6/3 14:33
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：专题菜单详情页面
 */
public class TopicMenuDetailPager extends MenuDetailBasePager {


    /**
     * 页签页面的数据集合
     */
    private final List<NewsCenterBean2.NewsCenterData.ChildrenData> childrens;
    /**
     * 左侧菜单对应的专题详情页面的数据
     */
    @ViewInject(R.id.vp_newsmenu_detailpager)
    private ViewPager viewpager;

    private MyPagerAdapter adapter;


    //    @ViewInject(R.id.indicator)
//    private TabPageIndicator indicator;

    @ViewInject(R.id.tabpage_indicator)
    private TabLayout tabpageindicator;

    /**
     * 页签页面的集合
     */
    private ArrayList<TopicTabDetailPager> detailPagers;

    /**
     * 构造方法
     *
     * @param context
     * @param newsCenterData
     */
    public TopicMenuDetailPager(Context context, NewsCenterBean2.NewsCenterData newsCenterData) {
        super(context);
        this.childrens = newsCenterData.getChildren();
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.topicmenu_detailpager, null);
        x.view().inject(this, view);
        return view;
    }

    /**
     * XUtils3点击事件
     *
     * @param view
     */
    @Event(value = R.id.tab_next)
    private void nextTab(View view) {
        viewpager.setCurrentItem(viewpager.getCurrentItem() + 1);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("专题详情页面的数据被初始化了...");
        //1.准备数据-页面
        detailPagers = new ArrayList<>();
        for (int i = 0; i < childrens.size(); i++) {

            detailPagers.add(new TopicTabDetailPager(context, childrens.get(i)));
        }

        //2.设置适配器
        adapter = new MyPagerAdapter();
        viewpager.setAdapter(adapter);


        //关联ViewPager
//        indicator.setViewPager(viewpager);
        //在设置ViewPager一定要在设置适配器之后
        tabpageindicator.setupWithViewPager(viewpager);

        //以后监听页面的变化，用TabPageIndicator监听页面的改变

//        indicator.setOnPageChangeListener(new MyOnPageChangeListener());
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());

        viewpager.setCurrentItem(0);

        //设置Tab的自由滑动
        tabpageindicator.setTabMode(TabLayout.MODE_SCROLLABLE);

        //自定义Tab
        for (int i = 0; i < tabpageindicator.getTabCount(); i++) {
            TabLayout.Tab tab = tabpageindicator.getTabAt(i);
            tab.setCustomView(getTabView(i));
        }

    }



    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                //SlidingMenu可以滑动
                isEnableSlidemenu(true);
            } else {
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
     *
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

    public View getTabView(int position){
        View view = LayoutInflater.from(context).inflate(R.layout.tab_item, null);
        TextView tv= (TextView) view.findViewById(R.id.textView);
        tv.setText(childrens.get(position).getTitle());
        ImageView img = (ImageView) view.findViewById(R.id.imageView);
        img.setImageResource(R.drawable.dot_focus);
        return view;
    }

    class MyPagerAdapter extends PagerAdapter {



        @Override
        public CharSequence getPageTitle(int position) {
            return childrens.get(position).getTitle();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TopicTabDetailPager tabDetailPager = detailPagers.get(position);
            View view = tabDetailPager.rootView;
            tabDetailPager.initData();//初始化数据
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
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
}
