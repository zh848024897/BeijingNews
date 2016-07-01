package com.atguigu.beijingnews.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.domain.TabDetailBean;
import com.atguigu.beijingnews.menudetailpager.TabDetailPager;
import com.atguigu.beijingnews.utils.CacheUtils;
import com.atguigu.beijingnews.utils.Constant;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * 作者：杨光福 on 2016/6/13 14:59
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：xxxx
 */
public class MyTabDetailPagerAdapter extends BaseAdapter {

    private final Context context;
    private final List<TabDetailBean.TabDetailData.NewsData> news;

    public MyTabDetailPagerAdapter(Context context,List<TabDetailBean.TabDetailData.NewsData> news){
        this.context = context;
        this.news = news;
    }

    @Override
    public int getCount() {
        return news.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoder viewHoder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_tabdetailpager, null);
            viewHoder = new ViewHoder();
            viewHoder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHoder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHoder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(viewHoder);
        } else {
            viewHoder = (ViewHoder) convertView.getTag();
        }

        //根据位置得到数据
        TabDetailBean.TabDetailData.NewsData newsData = news.get(position);

        //请求图片
        Glide.with(context).load(Constant.BASE_URL + newsData.getListimage())
                .placeholder(R.drawable.news_pic_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.news_pic_default).into(viewHoder.iv_icon);

        //设置标题
        viewHoder.tv_title.setText(newsData.getTitle());

        viewHoder.tv_time.setText(newsData.getPubdate());

        String readArrayId = CacheUtils.getString(context, TabDetailPager.READ_ARRAY_ID);
        if (readArrayId.contains(newsData.getId() + "")) {
            //设置灰色
            viewHoder.tv_title.setTextColor(Color.GRAY);
        } else {
            //设置黑色
            viewHoder.tv_title.setTextColor(Color.BLACK);
        }


        return convertView;
    }


    static class ViewHoder {
        ImageView iv_icon;
        TextView tv_title;
        TextView tv_time;
    }
}


