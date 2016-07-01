package com.atguigu.beijingnews.menudetailpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.base.MenuDetailBasePager;
import com.atguigu.beijingnews.domain.NewsCenterBean2;
import com.atguigu.beijingnews.domain.TabDetailBean;
import com.atguigu.beijingnews.utils.CacheUtils;
import com.atguigu.beijingnews.utils.Constant;
import com.atguigu.beijingnews.utils.LogUtil;
import com.atguigu.beijingnews.view.HorizontalScrollViewPager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * 作者：杨光福 on 2016/6/4 09:21
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：新闻菜单对应的详情页面的页签页面
 */
public class TopicTabDetailPager extends MenuDetailBasePager {

    @ViewInject(R.id.viewpager)
    private HorizontalScrollViewPager viewpager;

    @ViewInject(R.id.tv_title)
    private TextView tv_title;


    @ViewInject(R.id.ll_poing_group)
    private LinearLayout ll_poing_group;

    @ViewInject(R.id.pull_refresh_list)
    private PullToRefreshListView pullToRefreshListView;


    private ListView listview;

    private MyTabDetailPagerAdapter adapter;

    private final NewsCenterBean2.NewsCenterData.ChildrenData childrenData;
    /**
     * 联网请求的路径
     */
    private String url;

    /**
     * 上一次被高亮显示的位置
     */
    private int prePosition = 0;
    /**
     * 顶部新闻的数据
     */
    private List<TabDetailBean.TabDetailData.TopnewsData> topnews;
    /**
     * 新闻列表
     */
    private List<TabDetailBean.TabDetailData.NewsData> news;
    /**
     * 加载更多的连接
     */
    private String moreUrl;

    /**
     * 是否加载更多数据
     */
    private boolean isLoadMore = false;


    /**
     * 构造方法
     *
     * @param context
     * @param childrenData
     */
    public TopicTabDetailPager(Context context, NewsCenterBean2.NewsCenterData.ChildrenData childrenData) {
        super(context);
        this.childrenData = childrenData;
    }


    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.topic_tabdetail_pager, null);
        x.view().inject(this, view);

        View topnewsview = View.inflate(context, R.layout.topnews, null);
        x.view().inject(this, topnewsview);

        listview = pullToRefreshListView.getRefreshableView();

        //把顶部新闻已头的方式添加到ListView上
        listview.addHeaderView(topnewsview);

//        listview.addTopNews(topnewsview);

//        listview.setOnRefreshListener(new MyOnRefreshListener());

        //监听下拉刷新
        pullToRefreshListView.setOnRefreshListener(new MyOnRefreshListener2());

        return view;
    }

    class MyOnRefreshListener2 implements PullToRefreshBase.OnRefreshListener2 {

        @Override
        public void onPullDownToRefresh(PullToRefreshBase refreshView) {
            if(TextUtils.isEmpty(moreUrl)){
                //没有更多数据
                Toast.makeText(context,"没有更多数据",Toast.LENGTH_SHORT).show();
                pullToRefreshListView.onRefreshComplete();
            }else{

                //刷新
                getDataFromNet();
            }
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase refreshView) {
            if(TextUtils.isEmpty(moreUrl)){
                //没有更多数据
                Toast.makeText(context,"没有更多数据",Toast.LENGTH_SHORT).show();
                pullToRefreshListView.onRefreshComplete();
            }else{

                //加载更多
                getMoreDataFromNet();
            }
        }
    }

//    class MyOnRefreshListener implements RefreshListView.OnRefreshListener {
//
//        @Override
//        public void onPullDownRefresh() {
//            getDataFromNet();
//        }
//
//        @Override
//        public void onLoadMore() {
//
//            if(TextUtils.isEmpty(moreUrl)){
//                //没有更多数据
//                Toast.makeText(context,"没有更多数据",Toast.LENGTH_SHORT).show();
//                listview.onFinishRefrsh(false);
//            }else{
//
//                //加载更多
//                getMoreDataFromNet();
//            }
//
//        }
//    }

    private void getMoreDataFromNet() {
        RequestParams params = new RequestParams(moreUrl);
        params.setConnectTimeout(4000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                isLoadMore = true;
                LogUtil.e("联网成功==" + result);
                //2.解析和显示数据
                processData(result);

                pullToRefreshListView.onRefreshComplete();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("联网失败==" + ex.getMessage());
                pullToRefreshListView.onRefreshComplete();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("onFinished==");
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        url = Constant.BASE_URL + childrenData.getUrl();
        LogUtil.e(url + "----------------");

        String saveJson = CacheUtils.getString(context, url);
        if (!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }
        getDataFromNet();
    }

    private void getDataFromNet() {
        RequestParams params = new RequestParams(url);
        params.setConnectTimeout(4000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("联网成功==" + result);
                //1.缓存数据
                CacheUtils.putString(context, url, result);
                //2.解析和显示数据
                processData(result);

                pullToRefreshListView.onRefreshComplete();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("联网失败==" + ex.getMessage());
                pullToRefreshListView.onRefreshComplete();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("onFinished==");
            }
        });
    }

    /**
     * 解析json数据和显示数据
     *
     * @param json
     */
    private void processData(String json) {
        TabDetailBean tabDetailBean = parseJson(json);
        LogUtil.e("数据解析成功==" + tabDetailBean.getData().getNews().get(0).getTitle());


        String more =  tabDetailBean.getData().getMore();

        if(TextUtils.isEmpty(more)){
            moreUrl = "";
        }else{
            moreUrl = Constant.BASE_URL + more;
        }

        if(!isLoadMore){
           //原来的
            //设置ViewPager的适配器
            topnews = tabDetailBean.getData().getTopnews();
            viewpager.setAdapter(new MyPagerAdapter());

            //把所有的点先移除
            ll_poing_group.removeAllViews();
            //设置红点和文本
            for(int i=0;i<topnews.size();i++){

                ImageView point = new ImageView(context);
                point.setBackgroundResource(R.drawable.point_bg_selector);
                if(i==0){
                    point.setEnabled(true);
                }else{
                    point.setEnabled(false);
                }

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(5),DensityUtil.dip2px(5));
                if(i != 0){
                    params.leftMargin = DensityUtil.dip2px(5);
                }
                point.setLayoutParams(params);


                //添加到显示布局里面
                ll_poing_group.addView(point);

            }

            //监听ViewPager页面的变化
            viewpager.addOnPageChangeListener(new MyOnPageChangeListener());

            tv_title.setText(topnews.get(prePosition).getTitle());


            //设置ListView的适配器
            news = tabDetailBean.getData().getNews();
            adapter = new MyTabDetailPagerAdapter();
            listview.setAdapter(adapter);
        }else{
            //加载更多

            news.addAll(tabDetailBean.getData().getNews());
            //刷新适配器
            adapter.notifyDataSetChanged();

            isLoadMore = false;


        }




    }
    class MyTabDetailPagerAdapter extends BaseAdapter{

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
            if(convertView == null){
                convertView = View.inflate(context,R.layout.item_tabdetailpager,null);
                viewHoder = new ViewHoder();
                viewHoder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                viewHoder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                viewHoder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                convertView.setTag(viewHoder);
            }else{
                viewHoder = (ViewHoder) convertView.getTag();
            }

            //根据位置得到数据
            TabDetailBean.TabDetailData.NewsData newsData = news.get(position);

            //请求图片
            Glide.with(context).load(Constant.BASE_URL+newsData.getListimage())
                    .placeholder(R.drawable.news_pic_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.news_pic_default).into(viewHoder.iv_icon);

            //设置标题
            viewHoder.tv_title.setText(newsData.getTitle());

            viewHoder.tv_time.setText(newsData.getPubdate());


            return convertView;
        }
    }

    static class ViewHoder {
        ImageView iv_icon;
        TextView tv_title;
        TextView tv_time;
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //设置标题
            tv_title.setText(topnews.get(position).getTitle());

            //1.把上一次的设置默认
            ll_poing_group.getChildAt(prePosition).setEnabled(false);
            //2.把当前页面对应的点设置高亮
            ll_poing_group.getChildAt(position).setEnabled(true);

            prePosition = position;
        }

        @Override
        public void onPageSelected(int position) {


        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class MyPagerAdapter extends PagerAdapter {

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

    /**
     * 解析json数据
     *
     * @param json
     * @return
     */
    private TabDetailBean parseJson(String json) {
        return new Gson().fromJson(json, TabDetailBean.class);
    }
}
