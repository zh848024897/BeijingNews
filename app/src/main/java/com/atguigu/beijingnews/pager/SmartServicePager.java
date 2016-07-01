package com.atguigu.beijingnews.pager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.adapter.SmartServicePagerAdapter;
import com.atguigu.beijingnews.base.BasePager;
import com.atguigu.beijingnews.domain.SmartServicePagerBean;
import com.atguigu.beijingnews.utils.CacheUtils;
import com.atguigu.beijingnews.utils.Constant;
import com.atguigu.beijingnews.utils.LogUtil;
import com.atguigu.beijingnews.volley.VolleyManager;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 作者：杨光福 on 2016/6/3 09:34
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：智慧服页面
 */
public class SmartServicePager extends BasePager {

    private MaterialRefreshLayout refresh;
    private RecyclerView recyclerview;
    /**
     * 每页返回多少数据
     */
    private int pageSize = 10;

    /**
     * 当前是第几页
     */
    private int curPage = 1;

    /**
     * 总页数
     */
    private int totalPage = 1;
    private String url;

    private SmartServicePagerAdapter adapter;
    /**
     * 商品列表数据
     */
    private List<SmartServicePagerBean.Wares> datas;

    /**
     * 正常状态
     */
    private static final int STATE_NORMAL = 1;

    /**
     * 下拉刷新状态
     */
    private static final int STATE_REFRESH = 2;

    /**
     * 上拉刷新状态-加载更多状态
     */
    private static final int STATE_MORE = 3;

    /**
     * 当前状态
     */
    private int state = STATE_NORMAL;


    /**
     * 构造方法
     *
     * @param context
     */
    public SmartServicePager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("智慧的数据被初始化了");
        //1.设置标题
        tv_title.setText("商品热卖");

        //2.创建视图
//        TextView textView = new TextView(context);
//        textView.setText("智慧的内容");
//        textView.setTextColor(Color.RED);
//        textView.setGravity(Gravity.CENTER);
//        textView.setTextSize(25);
        View view = View.inflate(context, R.layout.smartservcie_pager, null);
        refresh = (MaterialRefreshLayout) view.findViewById(R.id.refresh);
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);

        fl_content.removeAllViews();
        //3.把视图添加到FrameLayout
        fl_content.addView(view);
        normalShowData();

        refresh.setMaterialRefreshListener(new MyMaterialRefreshListener() {
        });
    }
    class MyMaterialRefreshListener extends MaterialRefreshListener {

        @Override
        public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
            //下拉刷新
            initRefresh();
        }

        @Override
        public void onfinish() {
            super.onfinish();
            Toast.makeText(context, "刷新完成", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
//            super.onRefreshLoadMore(materialRefreshLayout);

            if(curPage <totalPage){
                //上拉刷新
                initMore();
            }else{
                refresh.finishRefreshLoadMore();
                Toast.makeText(context, "没有更多数据了，哥们", Toast.LENGTH_SHORT).show();

            }

        }
    }

    private void initMore() {
        curPage = curPage +1;
        state = STATE_MORE;
        url = Constant.WARSES_URL + pageSize + "&curPage=" + curPage;
        getDataFromNet();
    }

    private void initRefresh() {
        curPage = 1;
        state = STATE_REFRESH;
        url = Constant.WARSES_URL + pageSize + "&curPage=" + curPage;
        getDataFromNet();
    }

    private void normalShowData() {
        state = STATE_NORMAL;
        curPage = 1;
        url = Constant.WARSES_URL + pageSize + "&curPage=" + curPage;
        String saveJson = CacheUtils.getString(context,url);
        if(!TextUtils.isEmpty(saveJson)){
            processData(saveJson);
        }
        getDataFromNet();
    }

    private void getDataFromNet() {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                LogUtil.e("热卖商品请求数据成功==" + s);
                CacheUtils.putString(context,url,s);
                processData(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.e("热卖商品请求数据失败==" + volleyError.getMessage());
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String parsed;
                try {
                    parsed = new String(response.data, "UTF-8");
                    return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException var4) {
                }
                return super.parseNetworkResponse(response);
            }
        };
        VolleyManager.getRequestQueue().add(request);
    }

    private void processData(String json) {
        SmartServicePagerBean bean = new Gson().fromJson(json,SmartServicePagerBean.class);
        totalPage = bean.getTotalPage();
        curPage = bean.getCurrentPage();
        datas = bean.getList();
        showData();


    }

    private void showData() {
        switch (state){
            case STATE_NORMAL://正常，正在加载数据 第一页
                //设置适配器
                //1.设置适配器-item布局，绑定数据，传入数据
                adapter = new SmartServicePagerAdapter(context,datas);
                recyclerview.setAdapter(adapter);
                //2.设置布局管理器
                recyclerview.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
                //3.设置分割线
                //4.设置动画
                break;
            case STATE_REFRESH: //加载第一页，把原来的数据清空
                //1.把数据清空-适配器
                adapter.cleanData();
                //2.从新设置数据
                adapter.addData(datas);
                refresh.finishRefresh();
                break;
            case STATE_MORE://把得到的数据加载到原来的集合中
                //1.把数据添加到原来集合的末尾
                adapter.addData(adapter.getDate(),datas);
                //2.把状态还原
                refresh.finishRefreshLoadMore();
                break;
        }

    }
}
