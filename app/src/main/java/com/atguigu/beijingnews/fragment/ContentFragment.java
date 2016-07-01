package com.atguigu.beijingnews.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.activity.MainActivity;
import com.atguigu.beijingnews.base.BaseFragment;
import com.atguigu.beijingnews.base.BasePager;
import com.atguigu.beijingnews.pager.GovaffairPager;
import com.atguigu.beijingnews.pager.HomePager;
import com.atguigu.beijingnews.pager.NewsCenterPager;
import com.atguigu.beijingnews.pager.SettingPager;
import com.atguigu.beijingnews.pager.SmartServicePager;
import com.atguigu.beijingnews.utils.LogUtil;
import com.atguigu.beijingnews.view.NoScrollViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 作者：杨光福 on 2016/6/1 15:27
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：正文Fragment
 */
public class ContentFragment extends BaseFragment {


    @ViewInject(R.id.viewpager)
    private NoScrollViewPager mViewpager;

    @ViewInject(R.id.rg_main)
    private RadioGroup mRgMain;

    /**
     * 无个页面的集合
     */
    private ArrayList<BasePager> basePagers;

    @Override
    public View initView() {
        LogUtil.e("正文Fragment的视图被初始化了");
        View view = View.inflate(context, R.layout.content_fragment, null);
        x.view().inject(this, view);//把View注入到
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("正文Fragment的数据被初始化了");

        //默认设置主页面
        mRgMain.check(R.id.rb_home);

        //设置ViewPager的适配器
        //1.准备数据
        basePagers = new ArrayList<>();
        basePagers.add(new HomePager(context));//主页面
        basePagers.add(new NewsCenterPager(context));//新闻中心
        basePagers.add(new SmartServicePager(context));//智慧服务
        basePagers.add(new GovaffairPager(context));//政要指南
        basePagers.add(new SettingPager(context));//设置中心
        //2.设置适配器
        mViewpager.setAdapter(new MyPagerAdapter());

        //设置监听RadioGroup选中状态的变化
        mRgMain.setOnCheckedChangeListener(new MyOnCheckedChangeListener());

        //监听页面的变化
        mViewpager.addOnPageChangeListener(new MyOnPageChangeListener());

        //默认初始化首页的数据
        basePagers.get(0).initData();
        //默认SlidingMenu不可以滑动
        isEnableSlidemenu(false);
    }

    /**
     * 得到新闻中心
     * @return
     */
    public NewsCenterPager getNewsCenterPager() {
        return (NewsCenterPager) basePagers.get(1);
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            basePagers.get(position).initData();

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            isEnableSlidemenu(false);
            switch (checkedId) {
                case R.id.rb_home://首页
                    mViewpager.setCurrentItem(0, false);
                    break;
                case R.id.rb_newscenter://新闻中心
                    mViewpager.setCurrentItem(1, false);
                    //可以侧滑
                    isEnableSlidemenu(true);
                    break;
                case R.id.rb_smartservice://智慧服务
                    mViewpager.setCurrentItem(2, false);
                    break;
                case R.id.rb_govaffair://政要指南
                    mViewpager.setCurrentItem(3, false);

                    break;
                case R.id.rb_setting://设置中心
                    mViewpager.setCurrentItem(4, false);

                    break;
            }

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

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return basePagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
//            return super.instantiateItem(container, position);
            BasePager basePager = basePagers.get(position);//HomePager,NewsCenterPager....

            LogUtil.e(basePager.toString());
            View rootView = basePager.rootView;
            //少调用了initData();
//            basePager.initData();//调用//HomePager,NewsCenterPager....
            container.addView(rootView);
            return rootView;

        }
    }
}
