package com.atguigu.beijingnews.menudetailpager;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.activity.NewsDetailActivity;
import com.atguigu.beijingnews.adapter.MyTabDetailPagerAdapter;
import com.atguigu.beijingnews.adapter.TabDetailPagerTopAdapter;
import com.atguigu.beijingnews.base.MenuDetailBasePager;
import com.atguigu.beijingnews.domain.NewsCenterBean2;
import com.atguigu.beijingnews.domain.TabDetailBean;
import com.atguigu.beijingnews.utils.CacheUtils;
import com.atguigu.beijingnews.utils.Constant;
import com.atguigu.beijingnews.utils.LogUtil;
import com.atguigu.beijingnews.view.HorizontalScrollViewPager;
import com.atguigu.refreshlistview.RefreshListView;
import com.google.gson.Gson;

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
public class TabDetailPager extends MenuDetailBasePager {

    public static final String READ_ARRAY_ID = "read_array_id";
    @ViewInject(R.id.viewpager)
    private HorizontalScrollViewPager viewpager;

    @ViewInject(R.id.tv_title)
    private TextView tv_title;


    @ViewInject(R.id.ll_poing_group)
    private LinearLayout ll_poing_group;


    @ViewInject(R.id.listview)
    private RefreshListView listview;

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
    public TabDetailPager(Context context, NewsCenterBean2.NewsCenterData.ChildrenData childrenData) {
        super(context);
        this.childrenData = childrenData;
    }


    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.tabdetail_pager, null);
        x.view().inject(this, view);

        View topnewsview = View.inflate(context, R.layout.topnews, null);
        x.view().inject(this, topnewsview);

        //把顶部新闻已头的方式添加到ListView上
//        listview.addHeaderView(topnewsview);

        listview.addTopNews(topnewsview);

        listview.setOnRefreshListener(new MyOnRefreshListener());

        //设置监听点击某一条
        listview.setOnItemClickListener(new MyOnItemClickListener());

        return view;
    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            //因为加了头
            int realposition = position - 1;
            TabDetailBean.TabDetailData.NewsData newsData = news.get(realposition);

            LogUtil.e("id==" + newsData.getId() + ",title==" + newsData.getTitle() + ",url===" + newsData.getUrl());

            //获取保持的id
            String readArrayId = CacheUtils.getString(context, READ_ARRAY_ID);// //35311,35312,35313,35314,
            //""
            //35315
            if (!readArrayId.contains(newsData.getId() + "")) {
                //保持数据
                String value = readArrayId + newsData.getId() + ",";
                CacheUtils.putString(context, READ_ARRAY_ID, value);

                //适配器刷新
                adapter.notifyDataSetChanged();//getCount()-->getView();
            }


            //跳转到新闻浏览页面
            Intent intent = new Intent(context, NewsDetailActivity.class);
            intent.putExtra("url", Constant.BASE_URL + newsData.getUrl());
            context.startActivity(intent);


        }
    }

    class MyOnRefreshListener implements RefreshListView.OnRefreshListener {

        @Override
        public void onPullDownRefresh() {
            getDataFromNet();
        }

        @Override
        public void onLoadMore() {

            if (TextUtils.isEmpty(moreUrl)) {
                //没有更多数据
                Toast.makeText(context, "没有更多数据", Toast.LENGTH_SHORT).show();
                listview.onFinishRefrsh(false);
            } else {

                //加载更多
                getMoreDataFromNet();
            }

        }
    }

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

                listview.onFinishRefrsh(true);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("联网失败==" + ex.getMessage());
                listview.onFinishRefrsh(false);
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

                listview.onFinishRefrsh(true);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("联网失败==" + ex.getMessage());
                listview.onFinishRefrsh(false);
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

    private InternalHander internalHander;

//    public InternalHander getInternalHander() {
//        return internalHander;
//    }


    /**
     * 解析json数据和显示数据
     *
     * @param json
     */
    private void processData(String json) {
        TabDetailBean tabDetailBean = parseJson(json);
        LogUtil.e("数据解析成功==" + tabDetailBean.getData().getNews().get(0).getTitle());


        String more = tabDetailBean.getData().getMore();

        if (TextUtils.isEmpty(more)) {
            moreUrl = "";
        } else {
            moreUrl = Constant.BASE_URL + more;
        }

        if (!isLoadMore) {
            //原来的
            //设置ViewPager的适配器
            topnews = tabDetailBean.getData().getTopnews();
            viewpager.setAdapter(new TabDetailPagerTopAdapter(context, topnews, internalHander, this));

            //把所有的点先移除
            ll_poing_group.removeAllViews();
            //设置红点和文本
            for (int i = 0; i < topnews.size(); i++) {

                ImageView point = new ImageView(context);
                point.setBackgroundResource(R.drawable.point_bg_selector);
                if (i == 0) {
                    point.setEnabled(true);
                } else {
                    point.setEnabled(false);
                }

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(5), DensityUtil.dip2px(5));
                if (i != 0) {
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
            adapter = new MyTabDetailPagerAdapter(context, news);
            listview.setAdapter(adapter);
        } else {
            //加载更多

            news.addAll(tabDetailBean.getData().getNews());
            //刷新适配器
            adapter.notifyDataSetChanged();

            isLoadMore = false;


        }


        //发消息或者任务开始循环播放
        if (internalHander == null) {
            internalHander = new InternalHander();
        }

        //移除所有的消息和任务
        internalHander.removeCallbacksAndMessages(null);
        //4秒后执行任务
        internalHander.postDelayed(new InternalRunnable(), 4000);


    }


    public class InternalHander extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int item = (viewpager.getCurrentItem() + 1) % topnews.size();
            viewpager.setCurrentItem(item);

            internalHander.postDelayed(new InternalRunnable(), 4000);
        }
    }

    public class InternalRunnable implements Runnable {

        @Override
        public void run() {
            internalHander.sendEmptyMessage(0);
        }
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
            if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                isDrag = true;
                LogUtil.e("SCROLL_STATE_DRAGGING----");
                internalHander.removeCallbacksAndMessages(null);
            } else if (state == ViewPager.SCROLL_STATE_IDLE && isDrag) {
                isDrag = false;
                LogUtil.e("SCROLL_STATE_IDLE----");
                internalHander.removeCallbacksAndMessages(null);
                internalHander.postDelayed(new InternalRunnable(), 4000);

            } else if (state == ViewPager.SCROLL_STATE_SETTLING && isDrag) {
                LogUtil.e("SCROLL_STATE_SETTLING----");
                internalHander.removeCallbacksAndMessages(null);
                internalHander.postDelayed(new InternalRunnable(), 4000);

            }

        }
    }

    private boolean isDrag = false;

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
