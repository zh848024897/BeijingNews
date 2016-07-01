package com.atguigu.beijingnews.utils;

/**
 * 作者：杨光福 on 2016/6/3 11:00
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：xxxx
 */
public class Constant {

//    public static final String BASE_URL = "http://192.168.10.165:8080/zhbj";

    //本地的模拟器，访问本地电脑服务器
//    public static final String BASE_URL = "http://10.0.2.2:8080/zhbj";
////    public static final String BASE_URL = "http://192.168.191.1:8080/zhbj";
//
//    /**
//     * 新闻中心的连接地址
//     */
//    public static final String NEWSCENTER_URL = BASE_URL + "/categories.json";
//
//
//    /**
//     * 图组的网络地址
//     */
//    public static final String PHOTOS_URL = BASE_URL + "/photos/photos_1.json";



//    public static final String BASE_URL = "http://10.0.2.2:8080/web_home";
//    public static final String BASE_URL = "http://192.168.191.1:8080/zhbj";
    public static final String BASE_URL = "http://192.168.0.68:8080/web_home";
//    public static final String BASE_URL = "http://172.24.56.1:8080/zhbj";

    /**
     * 新闻中心的连接地址
     */
    public static final String NEWSCENTER_URL = BASE_URL + "/static/api/news/categories.json";


    /**
     * 图组的网络地址
     */
    public static final String PHOTOS_URL = BASE_URL + "/static/api/news/10003/list_1.json";
    /**
     * 商品热卖网络地址
     */
    public static final String WARSES_URL ="http://112.124.22.238:8081/course_api/wares/hot?pageSize=";
}