package com.bcing.application;

import android.app.Application;
import android.content.Context;

import com.bcing.ui.BaseActivity;
import com.bcing.utils.StaticClass;

import cn.bmob.v3.Bmob;

/**
 * Created by chinshry on 2018/3/14.
 * Project Name:SlideFragmentDemo
 * Package Name:com.bcing.application
 * File Name:BaseApplication
 * Describe：Application
 */

public class BaseApplication extends Application
{
    public static BaseActivity activity;
    private static BaseApplication application;

    //创建
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        //初始化Bmob
        Bmob.initialize(this, StaticClass.BMOB_APP_ID);
    }
    /**
     * 获取application
     *
     * @return
     */
    public static Context getApplication() {
        return application;
    }
}
