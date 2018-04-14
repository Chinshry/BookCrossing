package com.bcing.ui.activity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.bcing.R;
import com.bcing.ui.BaseActivity;

/**
 * Created by chinshry on 2018/3/14.
 * Project Name:BookCrossing
 * Package Name:com.bcing.ui.activity
 * File Name:EChartActivity
 * Describe：Application
 */

public class EChartActivity extends BaseActivity {
    WebView webview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_echart);

        webview = (WebView) findViewById(R.id.webview);

        WebSettings webSettings = webview.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);

        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        //加载需要显示的网页
        webview.loadUrl("file:///android_asset/echart.html");
//        //设置Web视图
//        webview.setWebViewClient(new WebViewClient());
    }

    @Override
    protected void initEvents() {

    }
}
