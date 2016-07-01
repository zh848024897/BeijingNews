package com.atguigu.beijingnews.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.atguigu.beijingnews.R;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 作者：杨光福 on 2016/6/6 15:51
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：新闻浏览页面
 */
public class NewsDetailActivity  extends Activity implements View.OnClickListener {

    /**
     * 1.加载网页-显示网页
     * 2.只有把网页加载到WebView，js才能和Java互调 - 支持H5
     * 3.WebView可以封装成浏览器-321浏览器
     * 4.做很牛的浏览器，可以用开源内核
     */
    private WebView webview;
    private ProgressBar pb_loading;

    private String url;
    private WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsdetail);
        findViewById(R.id.tv_title).setVisibility(View.GONE);
        findViewById(R.id.ib_back).setVisibility(View.VISIBLE);
        findViewById(R.id.ib_textsize).setVisibility(View.VISIBLE);
        findViewById(R.id.ib_share).setVisibility(View.VISIBLE);

        findViewById(R.id.ib_back).setOnClickListener(this);
        findViewById(R.id.ib_textsize).setOnClickListener(this);
        findViewById(R.id.ib_share).setOnClickListener(this);
        webview = (WebView) findViewById(R.id.webview);
        pb_loading = (ProgressBar) findViewById(R.id.pb_loading);

        url = getIntent().getStringExtra("url");
//        url = "http://atguigu.com/teacher.shtml";
        webview.loadUrl(url);
         webSettings = webview.getSettings();
        //设置WebView支持javaScript
        webSettings.setJavaScriptEnabled(true);
        //用户双击页面页面变大变小-页面要支持才可以
        webSettings.setUseWideViewPort(true);

        //增加缩放按钮 --页面要支持才可以
        webSettings.setBuiltInZoomControls(true);


        webview.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                pb_loading.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_back://退出页面
                finish();
                break;
            case R.id.ib_textsize://设置字体
                showChangeTextSizeDialog();
//                Toast.makeText(NewsDetailActivity.this, "设置字体", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_share://分享页面
                showShare();
//                Toast.makeText(NewsDetailActivity.this, "分享页面", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("尚硅谷北京新闻");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://photo.blog.sina.com.cn/showpic.html#blogid=61dfc88f0102v3z1&url=http://album.sina.com.cn/pic/001N7TJlzy6MSNiAVhc6e");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("阿福在给大牛们上课");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("\"http://photo.blog.sina.com.cn/showpic.html#blogid=61dfc88f0102v3z1&url=http://album.sina.com.cn/pic/001N7TJlzy6MSNiAVhc6e\"");

        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本--呵呵");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://www.atguigu.com");

// 启动分享GUI
        oks.show(this);
    }

    private int tempSize = 2;
    private int realSize = tempSize;

    private void showChangeTextSizeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置文字大小");
        String[] items = {"超大字体","大号字体","正常字体","小号字体","超小号字体"};
        builder.setSingleChoiceItems(items, realSize, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                tempSize = which;
            }
        });
        builder.setNegativeButton("取消",null );
        builder.setPositiveButton("确定" ,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                realSize = tempSize;
                changeTextSize();
            }
        });
        builder.show();


    }

    private void changeTextSize() {
        switch (realSize){
            case 0://超大字号
//                webSettings.setTextSize(WebSettings.TextSize.LARGEST);
                webSettings.setTextZoom(200);
                break;
            case 1://大字号
//                webSettings.setTextSize(WebSettings.TextSize.LARGER);
                webSettings.setTextZoom(150);
                break;
            case 2://正常字号
//                webSettings.setTextSize(WebSettings.TextSize.NORMAL);
                webSettings.setTextZoom(100);
                break;
            case 3://小字号
//                webSettings.setTextSize(WebSettings.TextSize.SMALLER);
                webSettings.setTextZoom(75);
                break;
            case 4://超小字号
//                webSettings.setTextSize(WebSettings.TextSize.SMALLEST);
                webSettings.setTextZoom(50);
                break;
        }
    }
}
