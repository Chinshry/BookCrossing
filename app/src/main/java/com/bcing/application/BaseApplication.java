package com.bcing.application;

import android.app.Application;
import android.content.Context;

import com.bcing.adapter.base.DemoMessageHandler;
import com.bcing.adapter.base.UniversalImageLoader;
import com.bcing.ui.BaseActivity;
import com.bcing.utils.StaticClass;
import com.orhanobut.logger.Logger;
import com.socks.library.KLog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import cn.bmob.newim.BmobIM;
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

    private static BaseApplication INSTANCE;
    public static BaseApplication INSTANCE(){
        return INSTANCE;
    }
    private void setInstance(BaseApplication app) {
        setBmobIMApplication(app);
    }
    private static void setBmobIMApplication(BaseApplication a) {
        BaseApplication.INSTANCE = a;
    }

    //创建
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        //初始化Bmob
        Bmob.initialize(this, StaticClass.BMOB_APP_ID);

        setInstance(this);
        //初始化
        Logger.init("smile");
        //只有主进程运行的时候才需要初始化
        if (getApplicationInfo().packageName.equals(getMyProcessName())){
            //im初始化
            BmobIM.init(this);
            KLog.e("IM", "BMOBIM 初始化");

            //注册消息接收器
            BmobIM.registerDefaultMessageHandler(new DemoMessageHandler(this));
        }
        //uil初始化
        UniversalImageLoader.initImageLoader(this);





    }
    /**
     * 获取application
     *
     * @return
     */
    public static Context getApplication() {
        return application;
    }


    /**
     * 获取当前运行的进程名
     * @return
     */
    public static String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
