package com.atguigu.beijingnews.menudetailpager;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.base.MenuDetailBasePager;
import com.atguigu.beijingnews.domain.PhotosMenuDetalPagerBean;
import com.atguigu.beijingnews.utils.BitmapCacheUitls;
import com.atguigu.beijingnews.utils.CacheUtils;
import com.atguigu.beijingnews.utils.Constant;
import com.atguigu.beijingnews.utils.LogUtil;
import com.atguigu.beijingnews.utils.NetCacheUtils;
import com.atguigu.beijingnews.volley.VolleyManager;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 作者：杨光福 on 2016/6/3 14:33
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：互动菜单详情页面
 */
public class InteracMenuDetailPager extends MenuDetailBasePager {
    private static final String TAG = InteracMenuDetailPager.class.getSimpleName();
    private final BitmapCacheUitls bitmapCacheUitls;
    @ViewInject(R.id.listview)
    private ListView listView;

    @ViewInject(R.id.gridview)
    private GridView gridView;

    protected ImageLoader imageLoader =ImageLoader.getInstance();
    /**
     * 请求图片时候的配置
     */
    private DisplayImageOptions options;
    /**
     * 互动的数据
     */
    private List<PhotosMenuDetalPagerBean.DataEntity.NewsData> news;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case NetCacheUtils.SUCCESS://请求成功
                    int position = msg.arg1;
                    Bitmap bitmap = (Bitmap) msg.obj;
                    Log.e("test", "请求图片成功==" + position);
                    if(listView != null&& listView.isShown()){
                        ImageView imageView = (ImageView) listView.findViewWithTag(position);
                        if(imageView != null&& bitmap != null){
                            imageView.setImageBitmap(bitmap);
                        }
                    }

                    if(gridView != null&& gridView.isShown()){
                        ImageView imageView = (ImageView) gridView.findViewWithTag(position);
                        if(imageView != null&& bitmap != null){
                            imageView.setImageBitmap(bitmap);
                        }
                    }
                    break;
                case NetCacheUtils.FAIL://请求失败
                    position = msg.arg1;
                    Log.e("test","请求图片失败=="+position);
                    break;
            }
        }
    };

    /**
     * 构造方法
     *
     * @param context
     */
    public InteracMenuDetailPager(Context context) {
        super(context);
        bitmapCacheUitls = new BitmapCacheUitls(handler);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.pic_item_list_default)
                .showImageForEmptyUri(R.drawable.pic_item_list_default)
                .showImageOnFail(R.drawable.pic_item_list_default)
                .cacheInMemory(true)////设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)////设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)//是否考虑JPEG图像EXIF参数（旋转，翻转）
                .displayer(new RoundedBitmapDisplayer(20))//设置图片圆角
                .build();
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.photos_menudetail_pager, null);
        x.view().inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("互动详情页面的数据被初始化了...");
        String saveJson = CacheUtils.getString(context, Constant.PHOTOS_URL);
        if (!TextUtils.isEmpty(saveJson)) {
            processeData(saveJson);
        }

//        getDataFromNet();
        getDataFromNetUseOkhttp();
    }


    /**
     * get请求
     * 请求文本信息
     */
    public void getDataFromNetUseOkhttp()
    {

        OkHttpUtils
                .get()
                .url(Constant.PHOTOS_URL)
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onError(okhttp3.Request request, Exception e) {
                        Log.e("test","okhttp请求数据失败==" + e.getMessage()+",request=="+request.toString());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("test","okhttp请求数据成功==" + response);
                        //缓存数据
                        CacheUtils.putString(context, Constant.PHOTOS_URL, response);
                        processeData(response);
                    }
                });
    };



    /**
     * 解析和显示数据
     *
     * @param json
     */
    private void processeData(String json) {
        PhotosMenuDetalPagerBean pagerBean = parseJson(json);

        news = pagerBean.getData().getNews();

        //设置适配器
        listView.setAdapter(new PhotosMenuDetailPagerAdapter());

    }

    /**
     * 是否显示ListView
     * 如果是true就是显示ListView，但是隐藏GridView
     * false隐藏Listview,显示GridView
     */
    private boolean isShowListView = true;

    public void switchListGrid(ImageButton ib_switch_gird_list) {
        if (isShowListView) {
            //显示GridView
            gridView.setAdapter(new PhotosMenuDetailPagerAdapter());
            gridView.setVisibility(View.VISIBLE);
            //隐藏Listview
            listView.setVisibility(View.GONE);

            isShowListView = false;
            //按钮的状态 -ListView
            ib_switch_gird_list.setImageResource(R.drawable.icon_pic_list_type);

        } else {
            //显示ListView
            listView.setAdapter(new PhotosMenuDetailPagerAdapter());
            listView.setVisibility(View.VISIBLE);
            //隐藏GridView
            gridView.setVisibility(View.GONE);
            isShowListView = true;
            //按钮的状态 -GridView
            ib_switch_gird_list.setImageResource(R.drawable.icon_pic_grid_type);
        }

    }

    class PhotosMenuDetailPagerAdapter extends BaseAdapter {
        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

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
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_photos, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //根据位置请求数据
            PhotosMenuDetalPagerBean.DataEntity.NewsData newsData = news.get(position);
            viewHolder.tv_title.setText(newsData.getTitle());

            //1.用Volley请求网络图片
            //请求图片：XUtils3,Glide,Volley，ImagerLoader
//            loaderImager(viewHolder,newsData.getListimage());

//            viewHolder.iv_icon.setTag(position);
            //2.自定义三级缓存请求图片
//            Bitmap bitmap = bitmapCacheUitls.getBitmapFromUrl(newsData.getListimage(),position);//本地，内存
//            if (bitmap != null) {
//                viewHolder.iv_icon.setImageBitmap(bitmap);
//            }

            //3.使用ImagerLoader请求图片

            //第一个参数是：图片的地址，第二个参数是要绑定的图片，第三个参数处理图片的配置，第四个参数
//            imageLoader.displayImage(newsData.getListimage(), viewHolder.iv_icon, options, animateFirstListener);

            //4.使用  Picasso请求图片
//            Picasso.with(context).load(newsData.getListimage())
//                  .placeholder(R.drawable.pic_item_list_default)
//                    .error(R.drawable.pic_item_list_default).
//                    into(viewHolder.iv_icon);
//
            //5.使用Glide请求网络图片
            Glide.with(context).load(Constant.BASE_URL+newsData.getListimage())
                  .placeholder(R.drawable.pic_item_list_default)
                    .error(R.drawable.pic_item_list_default).
                    into(viewHolder.iv_icon);
            return convertView;
        }
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);//显示图片的过程有一个渐变动画
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    static class ViewHolder {
        ImageView iv_icon;
        TextView tv_title;
    }

//    /**
//     * @param viewHolder
//     * @param imageurl
//     */
//    private void loaderImager(final ViewHolder viewHolder, String imageurl) {
//
//        viewHolder.iv_icon.setTag(imageurl);
//        //直接在这里请求会乱位置
//        ImageLoader.ImageListener listener = new ImageLoader.ImageListener() {
//            @Override
//            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
//                if (imageContainer != null) {
//
//                    if (viewHolder.iv_icon != null) {
//                        if (imageContainer.getBitmap() != null) {
//                            viewHolder.iv_icon.setImageBitmap(imageContainer.getBitmap());
//                        } else {
//                            viewHolder.iv_icon.setImageResource(R.drawable.pic_item_list_default);
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                //如果出错，则说明都不显示（简单处理），最好准备一张出错图片
//                viewHolder.iv_icon.setImageResource(R.drawable.pic_item_list_default);
//            }
//        };
//        VolleyManager.getImageLoader().get(imageurl, listener);
//    }

    /*
    用Gson解析数据
     */
    private PhotosMenuDetalPagerBean parseJson(String json) {
        return new Gson().fromJson(json, PhotosMenuDetalPagerBean.class);
    }

    private void getDataFromNet() {
        StringRequest request = new StringRequest(Request.Method.GET, Constant.PHOTOS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                LogUtil.e("请求数据成功==" + s);
                //缓存数据
                CacheUtils.putString(context, Constant.PHOTOS_URL, s);
                processeData(s);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.e("请求数据失败-onErrorResponse==" + volleyError.getMessage());
            }
        }) {

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String parsed;
                try {
                    parsed = new String(response.data, "UTF-8");
                    return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException var4) {
                    var4.printStackTrace();
                }

                return super.parseNetworkResponse(response);
            }
        };
        VolleyManager.getRequestQueue().add(request);
    }
}
