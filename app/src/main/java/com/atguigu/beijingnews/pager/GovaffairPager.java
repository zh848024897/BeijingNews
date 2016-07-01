package com.atguigu.beijingnews.pager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.activity.MainActivity;
import com.atguigu.beijingnews.adapter.GovaffairPagerAdapter;
import com.atguigu.beijingnews.base.BasePager;
import com.atguigu.beijingnews.domain.ShoppingCart;
import com.atguigu.beijingnews.pay.PayResult;
import com.atguigu.beijingnews.pay.SignUtils;
import com.atguigu.beijingnews.utils.CartProvider;
import com.atguigu.beijingnews.utils.LogUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * 作者：杨光福 on 2016/6/3 09:34
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：政要指南页面
 */
public class GovaffairPager extends BasePager {

    private CartProvider cartProvider;
    private RecyclerView recyclerview;
    private CheckBox checkbox_all;
    private TextView tv_total_price;
    private Button btn_order;
    private Button btn_delete;
    /**
     * 购物车的数据
     */
    private List<ShoppingCart> datas;

    private GovaffairPagerAdapter adapter;

    /**
     * 编辑状态
     */
    private static final int ACTION_EDIT = 1;
    /**
     * 完成状态
     */
    private static final int ACITON_COMPLETE = 2;

    /**
     * 构造方法
     *
     * @param context
     */
    public GovaffairPager(Context context) {
        super(context);
        cartProvider = new CartProvider(context);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("政要的数据被初始化了");
        //1.设置标题
        tv_title.setText("购物车");

        //2.创建视图
//        TextView textView = new TextView(context);
//        textView.setText("政要的内容");
//        textView.setTextColor(Color.RED);
//        textView.setGravity(Gravity.CENTER);
//        textView.setTextSize(25);
        View view = View.inflate(context, R.layout.govaffair_pager, null);
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        checkbox_all = (CheckBox) view.findViewById(R.id.checkbox_all);
        tv_total_price = (TextView) view.findViewById(R.id.tv_total_price);
        btn_order = (Button) view.findViewById(R.id.btn_order);
        btn_delete = (Button) view.findViewById(R.id.btn_delete);
        btn_cart.setVisibility(View.VISIBLE);
        fl_content.removeAllViews();

        btn_cart.setTag(ACTION_EDIT);
        //设置点击事件
        btn_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //得到状态
                int action = (int) btn_cart.getTag();

                if (action == ACTION_EDIT) {
                    //显示删除按钮
                    showDeleteButton();
                } else if (action == ACITON_COMPLETE) {
                    //隐藏删除按钮
                    hideDeleteButton();
                }

            }
        });

        //设置删除按钮的点击事件
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.deleteData();
            }
        });

        //设置去结算的点击事件
        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay(v);
            }
        });

        //3.把视图添加到FrameLayout
        fl_content.addView(view);
        setAdapter();
    }


    /**
     * call alipay sdk pay. 调用SDK支付
     *
     */
    public void pay(View v) {
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(context).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
//                            finish();
                        }
                    }).show();
            return;
        }
        String orderInfo = getOrderInfo("尚硅谷商城-购物", "尚硅谷商城-购物-很多东西", adapter.getTotalPrice()+"");

        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = sign(orderInfo);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask((MainActivity)context);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    // 商户PID
    public static final String PARTNER = "2088911876712776";
    // 商户收款账号
    public static final String SELLER = "chenlei@atguigu.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAK/Z2s6+paGoh7Jv\n" +
            "D+UENSBuQqA2u/YO6Mro6v8BZmT1p2hhytvC+U9WVyJ6xFOlQxmeJagLJlMoOkjk\n" +
            "Oty/WnV73sEzELTww0sirNCq9M0qyofgjFG5l2kL0B5YIhLb/Z90J3+B0axLQI1z\n" +
            "i9/0s9aI3hjwGpEbxddgd2KWJ9tLAgMBAAECgYAO6HU4WclI7zSx1+CNFdI7MZ3M\n" +
            "X1eIALOToDNr8v5vSLQskT3Va64fH767WZUTUkth/JpMqMVS/3IRg+fJlx9f+mMe\n" +
            "I2Uk/MFQB25NQpYmzU5EKqZg2yYV8lB4Qa0goxr6d/wFW3ke9UtjRYXQIVWIkkeY\n" +
            "YTmmYNVzSCU9UcdWwQJBANz3EfmGgG/+DrqBYTl+fhJyNcSvQ0ChsUe+lyGxRSJd\n" +
            "Epc3RrzuuGgzQrJFlOCa96WgcJuyiqdd7gDwfA02P7kCQQDLu5x+gMmz5r5Oz/zy\n" +
            "3x1bkTQGXeiem1uYRtu1u1c0wsIsz+gREyDuL7sjpMQFpQoGER4QHrCjaWF3dm4L\n" +
            "Zs0jAkEAuVZbM0qHzGfyfqkqnYjlwh8dzl2bMUjuY9kO9umzrUEX6NIqeSpabrwg\n" +
            "Q1ttpn8VHU1XZqD4/60fhpYv1v0bsQJAKFD7GgmQYLpzxJLiIonGfH1Sh0QEKYbi\n" +
            "7FJxXhgxnK5B0K5f5Skc5iWp6dbMN7W+Mw8mHNiF6gCLmiFki3sPcwJBAKPSbQpO\n" +
            "+a9wDUBSOvS5RQd3vQoN9tGw3IB60gftRyufWCO3MJ0FZwNgLYe2dAn7B3HILmhJ\n" +
            "22KYXsRbitfoTWI=";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCv2drOvqWhqIeybw/lBDUgbkKg\n" +
            "Nrv2DujK6Or/AWZk9adoYcrbwvlPVlciesRTpUMZniWoCyZTKDpI5Drcv1p1e97B\n" +
            "MxC08MNLIqzQqvTNKsqH4IxRuZdpC9AeWCIS2/2fdCd/gdGsS0CNc4vf9LPWiN4Y\n" +
            "8BqRG8XXYHdilifbSwIDAQAB";
    private static final int SDK_PAY_FLAG = 1;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(context, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(context, "支付失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };


    /**
     * create the order info. 创建订单信息
     *
     */
    private String getOrderInfo(String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     *
     */
    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }


    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content
     *            待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     *
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }

    private void hideDeleteButton() {
        //1.设置文本为编辑
        btn_cart.setText("编辑");
        //2.设置全部为 勾选和全选按钮为 勾选
        adapter.all_none(true);
        checkbox_all.setChecked(true);
        //3.显示价格
        tv_total_price.setVisibility(View.VISIBLE);
        //4.隐藏删除按钮
        btn_delete.setVisibility(View.GONE);
        //5.显示结算按钮
        btn_order.setVisibility(View.VISIBLE);
        //6.设置状态 ACTION_EDIT
        btn_cart.setTag(ACTION_EDIT);
    }

    private void showDeleteButton() {
        //1.设置文本为完成
        btn_cart.setText("完成");
        //2.设置全部为非勾选和全选按钮为非勾选
        adapter.all_none(false);
        checkbox_all.setChecked(false);
        //3.隐藏价格
        tv_total_price.setVisibility(View.GONE);
        //4.显示删除按钮
        btn_delete.setVisibility(View.VISIBLE);
        //5.隐藏结算按钮
        btn_order.setVisibility(View.GONE);
        //6.设置状态 ACTION_COMPLETE
        btn_cart.setTag(ACITON_COMPLETE);

    }

    private void setAdapter() {
        datas = cartProvider.getAllData();
        adapter = new GovaffairPagerAdapter(context,datas,checkbox_all,tv_total_price);
        recyclerview.setAdapter(adapter);

        //设置布局管理器
        recyclerview.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
    }
}
