package com.bcing.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * @author fml
 * created at 2016/6/24 15:17
 * description：懒加载
 */
public class LazyFragment extends Fragment{
    //用于标记视图是否初始化
    protected boolean isVisible;
    protected View mRootView;

    //在onCreate方法之前调用，用来判断Fragment的UI是否是可见的
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        initRootView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, mRootView);
        initEvents();
        initData(savedInstanceState == null);

        return mRootView;
    }

    /**
     * 初始化根布局
     *
     * @return View 视图
     */
//    protected void initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//    }

    /**
     * 初始化监听事件等
     */
    protected void initEvents(){}

    public static LazyFragment newInstance() {

        Bundle args = new Bundle();

        LazyFragment fragment = new LazyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 视图可见
     * */
    protected void onVisible(){
        lazyLoad();
    }
    /**
     * 自定义抽象加载数据方法
     * */
    protected void lazyLoad(){};
    /**
     * 视图不可见
     * */
    protected void onInvisible(){}
    /**
     * 加载数据
     * @param isSavedNull
     */
    protected void initData(boolean isSavedNull){}







}
