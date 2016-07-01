package com.atguigu.beijingnews.fragment;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.atguigu.beijingnews.R;
import com.atguigu.beijingnews.activity.MainActivity;
import com.atguigu.beijingnews.base.BaseFragment;
import com.atguigu.beijingnews.domain.NewsCenterBean2;
import com.atguigu.beijingnews.pager.NewsCenterPager;
import com.atguigu.beijingnews.utils.DensityUtil;
import com.atguigu.beijingnews.utils.LogUtil;

import java.util.List;

/**
 * 作者：杨光福 on 2016/6/1 15:24
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：左侧菜单
 */
public class LeftMenuFragment extends BaseFragment {


    /**
     * 左侧菜单对应的数据
     */
    private List<NewsCenterBean2.NewsCenterData> leftmenudata;

    private ListView listView;

    private MyLeftMenuAdapter adapter;

    /**
     * 记录点击后的位置
     */
    private int selectPosition = 0;

    @Override
    public View initView() {
        LogUtil.e("左侧菜单的视图被初始化了");
        listView = new ListView(context);
        //防止在低版本按下整个ListView变灰色
        listView.setCacheColorHint(Color.TRANSPARENT);
        listView.setDividerHeight(0);
        listView.setPadding(0, DensityUtil.dip2px(context, 40), 0, 0);
        listView.setBackgroundColor(context.getResources().getColor(android.R.color.black));

        //设置点击某一条的监听
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //1.点击某个后变色-
                selectPosition = position;

                adapter.notifyDataSetChanged();//getCount()--getView();
                //2.把左侧菜单收起来
                MainActivity mainActivity = (MainActivity) context;
                mainActivity.getSlidingMenu().toggle();//关和开相互切换
                //3.点击的时候切换页面
                swichPager(selectPosition);


            }
        });
        return listView;
    }

    private void swichPager(int position) {
        MainActivity mainActivity = (MainActivity) context;
        //3.切换到具体的某个页面
        ContentFragment contentFragment = mainActivity.getContentFragment();
        NewsCenterPager newsCenterPager = contentFragment.getNewsCenterPager();
        newsCenterPager.swichPager(position);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("左侧菜单的数据被初始化了");
    }

    public void setData(List<NewsCenterBean2.NewsCenterData> leftmenudata) {
        this.leftmenudata= leftmenudata;

        //设置适配器
        adapter = new MyLeftMenuAdapter();
        listView.setAdapter(adapter);

        swichPager(selectPosition);
    }

    class MyLeftMenuAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return leftmenudata.size();
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
            TextView textView = (TextView) View.inflate(context, R.layout.item_leftmenu,null);
            textView.setText(leftmenudata.get(position).getTitle());

//            if(position ==selectPosition){
//                //红色
//                textView.setEnabled(true);
//            }else{
//                //白色
//                textView.setEnabled(false);
//
//            }
            textView.setEnabled(position ==selectPosition);
            return textView;
        }
    }
}
