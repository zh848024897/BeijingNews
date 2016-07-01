package com.atguigu.beijingnews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.domain.ShoppingCart;
import com.atguigu.beijingnews.domain.SmartServicePagerBean;
import com.atguigu.beijingnews.utils.CartProvider;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * 作者：杨光福 on 2016/6/14 10:14
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：xxxx
 */
public class SmartServicePagerAdapter extends RecyclerView.Adapter<SmartServicePagerAdapter.ViewHoder> {

    private final Context context;
    private final List<SmartServicePagerBean.Wares> datas;
    private CartProvider cartProvider;

    public SmartServicePagerAdapter(Context context, List<SmartServicePagerBean.Wares> datas) {
        this.context = context;
        this.datas = datas;
        cartProvider = new CartProvider(context);
    }

    @Override
    public ViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_smartservcie_pager,null);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(ViewHoder holder, int position) {
        //得到数据
        SmartServicePagerBean.Wares wares = datas.get(position);

        holder.tv_name.setText(wares.getName());
        holder.tv_price.setText("￥"+wares.getPrice());

        Glide.with(context).load(wares.getImgUrl())
                .placeholder(R.drawable.news_pic_default)
                .error(R.drawable.news_pic_default).
                into(holder.iv_icon);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    /**
     * 清空数据
     */
    public void cleanData() {
        datas.clear();
        notifyItemRangeRemoved(0,datas.size());
    }

    /**
     * 添加数据
     * @param data
     */
    public void addData(List<SmartServicePagerBean.Wares> data) {
       addData(0,data);
    }

    /**
     * 得到多少条数据
     * @return
     */
    public int getDate() {
        return datas.size();

    }

    /**
     * 添加数据到指定位置
     * @param positon
     * @param data
     */
    public void addData(int positon, List<SmartServicePagerBean.Wares> data) {
        if(data != null && data.size() >0 ){
            datas.addAll(positon,data);
            notifyItemRangeChanged(positon,datas.size());
        }
    }

    class ViewHoder extends RecyclerView.ViewHolder{

        private ImageView iv_icon;
        private TextView tv_name;
        private TextView tv_price;
        private Button btn_add;

        public ViewHoder(View itemView) {
            super(itemView);
            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            btn_add = (Button) itemView.findViewById(R.id.btn_add);

            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SmartServicePagerBean.Wares wares =  datas.get(getLayoutPosition());
                    ShoppingCart cart = cartProvider.conversion(wares);
                    cartProvider.update(cart);

//                    Toast.makeText(context, ""+datas.get(getLayoutPosition()).getPrice(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
