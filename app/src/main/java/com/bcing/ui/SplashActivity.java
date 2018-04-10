package com.bcing.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.bcing.R;
import com.bcing.utils.ShareUtils;
import com.bcing.utils.StaticClass;
import com.bcing.utils.UtilTools;

/**
 * Created by chinshry on 2018/3/14.
 * Project Name:BookCrossing
 * Package Name:com.bcing.ui
 * File Name:SplashActivity
 * Describe：闪屏页
 */

public class SplashActivity extends AppCompatActivity{



    /*
     * 1、判断延时2000ms
     * 2、判断程序是否第一次运行
     * 3、自定义字体
     * 4、Activity全屏主题
     *
     */
    private TextView tv_splash;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case StaticClass.HANDLER_SPLASH:
                    //判断程序是否是第一次运行

                    if (isFirst()){
                        startActivity(new Intent(SplashActivity.this,GuideActivity.class));
                    }
                    else {
                        //方便调试 记得改回MainActivity
                        startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                    }

                    finish();

                    break;
            }
        }
    };



    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_splash);

        initView();
    }

    //初始化View
    private void initView(){

        //延时2000ms
        handler.sendEmptyMessageDelayed(StaticClass.HANDLER_SPLASH,2000);

        tv_splash = (TextView) findViewById(R.id.tv_splash);

        //设置字体
        UtilTools.setFont(this,tv_splash);

    }

    //判断程序是否第一次运行的方法
    private boolean isFirst() {
        boolean isFirst = ShareUtils.getBoolean(this,StaticClass.SHARE_IS_FIRST,true);
        if (isFirst){
            //标记已启动过APP
            ShareUtils.putBoolean(this,StaticClass.SHARE_IS_FIRST,false);
            //是第一运行
            return true;
        }
        else {
            return false;
        }
    }

    //禁止返回键
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
