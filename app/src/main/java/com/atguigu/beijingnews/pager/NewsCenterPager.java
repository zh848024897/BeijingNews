package com.atguigu.beijingnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.atguigu.beijingnews.activity.MainActivity;
import com.atguigu.beijingnews.base.BasePager;
import com.atguigu.beijingnews.base.MenuDetailBasePager;
import com.atguigu.beijingnews.domain.NewsCenterBean2;
import com.atguigu.beijingnews.fragment.LeftMenuFragment;
import com.atguigu.beijingnews.menudetailpager.InteracMenuDetailPager;
import com.atguigu.beijingnews.menudetailpager.NewsMenuDetailPager;
import com.atguigu.beijingnews.menudetailpager.PhotosMenuDetailPager;
import com.atguigu.beijingnews.menudetailpager.TopicMenuDetailPager;
import com.atguigu.beijingnews.utils.CacheUtils;
import com.atguigu.beijingnews.utils.Constant;
import com.atguigu.beijingnews.utils.LogUtil;
import com.atguigu.beijingnews.volley.VolleyManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：杨光福 on 2016/6/3 09:34
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：新闻中心页面
 */
public class NewsCenterPager extends BasePager {
    /**
     * 左侧菜单对应的数据
     */
    private List<NewsCenterBean2.NewsCenterData> leftmenudata;

    /**
     * 左侧菜单对应的页面
     */
    private ArrayList<MenuDetailBasePager> detailBasePagers;
    private long startTime;

    /**
     * 构造方法
     *
     * @param context
     */
    public NewsCenterPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        //把按钮显示出来
        ib_menu.setVisibility(View.VISIBLE);
        LogUtil.e("新闻的数据被初始化了");
        //1.设置标题
        tv_title.setText("新闻");

        //2.创建视图
        TextView textView = new TextView(context);
        textView.setText("新闻的内容");
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(25);

        //3.把视图添加到FrameLayout
        fl_content.addView(textView);

        //取出缓存的数据
        String saveJson = CacheUtils.getString(context, Constant.NEWSCENTER_URL);
        if (!TextUtils.isEmpty(saveJson)) {
            precessData(saveJson);
        }

         startTime = SystemClock.uptimeMillis();
        //联网请求数据
//        getDataFromNet();
        getDataUseVolleyFromNet();
    }

    private void getDataUseVolleyFromNet() {
        //队列
//        RequestQueue queue = Volley.newRequestQueue(context);
        //请求文本
        StringRequest request = new StringRequest(Request.Method.GET, Constant.NEWSCENTER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                long endTime = SystemClock.uptimeMillis();
                long passTime = endTime - startTime;

                Log.e("Test","Volley --passTime==="+passTime);
                LogUtil.e("Volley联网成功==" + result);
                //缓存数据
                CacheUtils.putString(context, Constant.NEWSCENTER_URL, result);
                precessData(result);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.e("Volley联网失败==" + volleyError.getMessage());
            }
        }){

            /**
             * 解决乱码问题
             * @param response
             * @return
             */
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String parsed = new String(response.data,"UTF-8");

                    return  Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return super.parseNetworkResponse(response);
            }
        };

        VolleyManager.getRequestQueue().add(request);


    }

    /**
     * 解析和显示数据
     *
     * @param json
     */
    private void precessData(String json) {
        //1.解析json数据：手动解析数据和第三方（gson,fastjson）
//        NewsCenterBean newsCenterBean = parseJson(json);
        NewsCenterBean2 newsCenterBean = parseJson2(json);
//        LogUtil.e(newsCenterBean.getData().get(0).getChildren().get(0).getTitle()+"-----------------");

        //传递数据给左侧菜单
        leftmenudata = newsCenterBean.getData();
        //2.显示数据-把页面创建出来
        detailBasePagers = new ArrayList<>();
        detailBasePagers.add(new NewsMenuDetailPager(context, leftmenudata.get(0)));//新闻菜单详情页面
        detailBasePagers.add(new TopicMenuDetailPager(context,leftmenudata.get(0)));//专题菜单详情页面
        detailBasePagers.add(new PhotosMenuDetailPager(context));//图组菜单详情页面
        detailBasePagers.add(new InteracMenuDetailPager(context));//互动菜单详情页面


        MainActivity mainActivity = (MainActivity) context;
        LeftMenuFragment leftMenuFragment = mainActivity.getLeftMenuFragment();
        leftMenuFragment.setData(leftmenudata);


//        swichPager(0);//默认切换到0个页面
    }

    public void swichPager(int position) {

        //1.设置标题
        tv_title.setText(leftmenudata.get(position).getTitle());

        //2.替换内容
        MenuDetailBasePager menuDetailBasePager = detailBasePagers.get(position);
        View rootView = menuDetailBasePager.rootView;
        menuDetailBasePager.initData();//千万不能少哦
        fl_content.removeAllViews();//移除之前所有的视图
        fl_content.addView(rootView);

        if(position ==2){
            ib_switch_gird_list.setVisibility(View.VISIBLE);

            ib_switch_gird_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PhotosMenuDetailPager photosMenuDetailPager = (PhotosMenuDetailPager) detailBasePagers.get(2);
                    photosMenuDetailPager.switchListGrid(ib_switch_gird_list);
                }
            });
        }else{
            ib_switch_gird_list.setVisibility(View.GONE);
        }


    }

    /**
     * 解析json
     *
     * @param json
     * @return
     */
    private NewsCenterBean2 parseJson(String json) {
        return new Gson().fromJson(json, NewsCenterBean2.class);
    }

    /**
     * 手动解析json数据
     *
     * @param json
     * @return
     */
    private NewsCenterBean2 parseJson2(String json) {
        NewsCenterBean2 centerBean2 = new NewsCenterBean2();
        try {
            JSONObject jsonObject = new JSONObject(json);

            int retcode = jsonObject.optInt("retcode");
            centerBean2.setRetcode(retcode);

            //得到数组
            JSONArray jsonArrayData = jsonObject.optJSONArray("data");

            if (jsonArrayData != null) {
                List<NewsCenterBean2.NewsCenterData> data = new ArrayList<>();
                //把集合设置到Bean对象中
                centerBean2.setData(data);
                for (int i = 0; i < jsonArrayData.length(); i++) {

                    JSONObject jsonObjectData = (JSONObject) jsonArrayData.get(i);

                    if (jsonObjectData != null) {

                        NewsCenterBean2.NewsCenterData newsCenterData = new NewsCenterBean2.NewsCenterData();


                        newsCenterData.setId(jsonObjectData.optInt("id"));

                        newsCenterData.setTitle(jsonObjectData.optString("title"));

                        newsCenterData.setType(jsonObjectData.optInt("type"));

                        newsCenterData.setUrl(jsonObjectData.optString("url"));

                        newsCenterData.setUrl1(jsonObjectData.optString("url1"));

                        newsCenterData.setDayurl(jsonObjectData.optString("dayurl"));
                        newsCenterData.setExcurl(jsonObjectData.optString("excurl"));
                        newsCenterData.setWeekurl(jsonObjectData.optString("weekurl"));


                        //解析Children

                        JSONArray jsonArrayChildren =  jsonObjectData.optJSONArray("children");
                        if(jsonArrayChildren != null){

                            List<NewsCenterBean2.NewsCenterData.ChildrenData> children = new ArrayList<>();

                            newsCenterData.setChildren(children);

                            for(int j=0;j<jsonArrayChildren.length();j++){

                                NewsCenterBean2.NewsCenterData.ChildrenData childrenData = new NewsCenterBean2.NewsCenterData.ChildrenData();

                                JSONObject jsonArrayChildrenItem = (JSONObject) jsonArrayChildren.get(j);

                                int id = jsonArrayChildrenItem.optInt("id");
                                childrenData.setId(id);

                                String title = jsonArrayChildrenItem.optString("title");
                                childrenData.setTitle(title);

                                int type = jsonArrayChildrenItem.optInt("type");
                                childrenData.setType(type);

                                String url = jsonArrayChildrenItem.optString("url");
                                childrenData.setUrl(url);


                                //把每个childrenData添加到结合里面
                                children.add(childrenData);


                            }


                        }

                        data.add(newsCenterData);//把数据添加到集合中


                    }
                }


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return centerBean2;
    }

    private void getDataFromNet() {
        RequestParams params = new RequestParams(Constant.NEWSCENTER_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                long endTime = SystemClock.uptimeMillis();
                long passTime = endTime - startTime;

                Log.e("Test","xUtils --passTime==="+passTime);
                LogUtil.e("联网成功==" + result);
                //缓存数据
                CacheUtils.putString(context, Constant.NEWSCENTER_URL, result);
                precessData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("联网失败==" + ex.getMessage());
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
}
