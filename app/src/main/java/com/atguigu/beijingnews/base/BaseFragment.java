package com.atguigu.beijingnews.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 作者：杨光福 on 2016/6/1 15:28
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：基类，公共类：LeftMenuFragment和ContentFragment
 */
public abstract class BaseFragment extends Fragment {

    public Context context;

    /**
     * 当LeftMenuFragment被创建的时候回调这个方法
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }


    /**
     * 当View创建的时候回调这个方法
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return  initView();
    }

    /**
     * 强制孩子实现该方法，实现不同的效果
     * @return
     */
    public abstract View initView() ;
    /**
     * 当Activity被创建的时候回调这个方法
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    /**
     * 当孩子需要初始化数据的时候，重新该方法
     */
    public void initData() {

    }


}
