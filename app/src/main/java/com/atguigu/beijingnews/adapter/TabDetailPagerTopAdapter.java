package com.atguigu.beijingnews.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.domain.TabDetailBean;
import com.atguigu.beijingnews.menudetailpager.TabDetailPager;
import com.atguigu.beijingnews.utils.Constant;
import com.atguigu.beijingnews.utils.LogUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * 作者：杨光福 on 2016/6/13 14:51
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：xxxx
 */
public class TabDetailPagerTopAdapter extends PagerAdapter {

    private final Context context;
    private final List<TabDetailBean.TabDetailData.TopnewsData> topnews;
    private final TabDetailPager.InternalHander internalHander;
    private final TabDetailPager tabDetailPager;

    public TabDetailPagerTopAdapter(Context context,List<TabDetailBean.TabDetailData.TopnewsData> topnews,TabDetailPager.InternalHander internalHander,TabDetailPager tabDetailPager){

        this.context =context;
        this.topnews = topnews;
        this.internalHander = internalHander;
        this.tabDetailPager = tabDetailPager;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        imageView.setBackgroundResource(R.drawable.home_scroll_default);
        //设置空间拉伸
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        //联网请求图片-Glide

        Glide.with(context).load(Constant.BASE_URL+topnews.get(position).getTopimage())
                .placeholder(R.drawable.home_scroll_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.home_scroll_default).into(imageView);

        //添加到容器中
        container.addView(imageView);

        //设置触摸事件
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        //把消息移除
                        internalHander.removeCallbacksAndMessages(null);
                        LogUtil.e("ACTION_DOWN");
                        break;

                    case MotionEvent.ACTION_MOVE:
                        LogUtil.e("ACTION_MOVE");
                        //把消息移除
                        internalHander.removeCallbacksAndMessages(null);
                        break;

                    case MotionEvent.ACTION_UP:
                        LogUtil.e("ACTION_UP");
                        //把消息移除
                        internalHander.removeCallbacksAndMessages(null);
                        internalHander.postDelayed(tabDetailPager .new InternalRunnable(),4000);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        LogUtil.e("ACTION_CANCEL");
                        break;
                }
                return true;
            }
        });

        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);

    }

    @Override
    public int getCount() {
        return topnews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}