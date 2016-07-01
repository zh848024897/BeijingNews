package com.atguigu.beijingnews.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.utils.CacheUtils;
import com.atguigu.beijingnews.utils.DensityUtil;
import com.atguigu.beijingnews.utils.LogUtil;

import java.util.ArrayList;

/**
 * 作者：杨光福 on 2016/6/1 10:49
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：xxxx
 */
public class GuideActivity extends Activity {
    public static final String START_MAIN = "start_main";
    private ViewPager viewpager_guide;
    private Button btn_start_main;
    private LinearLayout ll_poing_group;
    private ArrayList<ImageView> imageViews;
    private ImageView iv_red;
    /**
     * 两点间的间距
     */
    private int leftMargin;

    private int widthDpi;
//    private String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
//        s.toString();
        viewpager_guide = (ViewPager) findViewById(R.id.viewpager_guide);
        btn_start_main = (Button) findViewById(R.id.btn_start_main);
        ll_poing_group = (LinearLayout) findViewById(R.id.ll_poing_group);
        iv_red = (ImageView) findViewById(R.id.iv_red);

        widthDpi = DensityUtil.dip2px(this,10);//转换成不同的像数
        LogUtil.e("widthDpi=="+widthDpi);
        int[] ids = new int[]{R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};

        //准备数据
        imageViews = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {

            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(ids[i]);//要设置背景
            //添加到集合中
            imageViews.add(imageView);


            //添加三个灰色的点
            ImageView point = new ImageView(this);
            point.setBackgroundResource(R.drawable.point_normal);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthDpi, widthDpi);
            if (i != 0) {
                //设置间距
                params.leftMargin = widthDpi;
            }
            point.setLayoutParams(params);

            ll_poing_group.addView(point);
        }


        //设置适配器
        viewpager_guide.setAdapter(new MyPagerAdapter());

        //计算两点间的间距
        //控件创建到显示--测量-布局-绘制
        iv_red.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener());

        //得到屏幕滑动的百分比
        viewpager_guide.addOnPageChangeListener(new MyOnPageChangeListener());

        //设置按钮的点击事件
        btn_start_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.记录曾经进入过主页面
                CacheUtils.putBoolen(GuideActivity.this, START_MAIN, true);

                //2.跳转到主页面
                startActivity(new Intent(GuideActivity.this,MainActivity.class));

                //3.关闭引导页面
                finish();
            }
        });


    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        /**
         *
         * @param position 当前滑动页面的下标位置
         * @param positionOffset 页面滑动的百分比
         * @param positionOffsetPixels 在屏幕滑动多少像数
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //红点移动的距离 = 间距*屏幕滑动百分比
//            float reddistance = leftMargin*positionOffset;

            //红点在屏幕上的坐标 = 起始坐标 +  红点移动的距离；
            float reddistance =leftMargin* (position + positionOffset);


            RelativeLayout.LayoutParams params  = new RelativeLayout.LayoutParams(widthDpi,widthDpi);
            params.leftMargin  = (int) reddistance;
            iv_red.setLayoutParams(params);

            System.out.println("position==" + position + ",positionOffset==" + positionOffset + ",positionOffsetPixels==" + positionOffsetPixels);

        }

        @Override
        public void onPageSelected(int position) {
            if(position ==imageViews.size()-1){
                //按钮显示出来
                btn_start_main.setVisibility(View.VISIBLE);
            }else{
                btn_start_main.setVisibility(View.GONE);
            }


        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onGlobalLayout() {
            //取出注册
            iv_red.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            //间距 = 第1个点距离左边的距离 - 第0个点距离左边的距离
            leftMargin = ll_poing_group.getChildAt(1).getLeft() - ll_poing_group.getChildAt(0).getLeft();
            System.out.println("leftMargin==" + leftMargin);
        }
    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imageViews.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
