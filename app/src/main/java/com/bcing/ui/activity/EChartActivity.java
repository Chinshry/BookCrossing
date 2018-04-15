package com.bcing.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bcing.R;
import com.bcing.entity.WantCrossInfo;
import com.bcing.ui.BaseActivity;
import com.google.gson.JsonArray;
import com.socks.library.KLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static com.kymjs.rxvolley.toolbox.RxVolleyContext.toast;

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
        //从BookDetail页面得到的crosscode
        KLog.e("TAG","EChartActivity的bookCode获取了吗" + getIntent().getIntExtra("bookCode",0));

        WebSettings webSettings = webview.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);

        BmobQuery<WantCrossInfo> query = new BmobQuery<WantCrossInfo>();
        query.addWhereEqualTo("crosscode",getIntent().getIntExtra("bookCode",0));

        query.findObjects(new FindListener<WantCrossInfo>() {
            @Override
            public void done(List<WantCrossInfo> object, BmobException e) {
                if(e==null){
                    KLog.e("TAG","对象大小" + object.size());

                    if (object.size()!=0){
                        KLog.e("TAG","inten吧" );

                        for (WantCrossInfo wantCross : object) {
                            List city = wantCross.getWantusercity();
                            webview.setWebViewClient(new WebViewClient() {
                                @Override
                                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                    super.onPageStarted(view, url, favicon);
                                }

                                @Override
                                public void onPageFinished(WebView view, String url) {
                                    super.onPageFinished(view, url);
                                    try {
                                        //当页面加载完成后，调用js方法
                                        JSONObject json = new JSONObject();
                                        JsonArray array = new JsonArray();


                                        array.add(getIntent().getStringExtra("owncity").substring(0,getIntent().getStringExtra("owncity").length()-1));
                                        for (int i = 0; i < wantCross.getWantusercity().size(); i++) {
                                            array.add(city.get(i).toString().substring(0,city.get(i).toString().length()-1));

                                        }
                                        json.put("BJData",array);
                                        KLog.e("TAG","求漂者城市数组" + array);
                                        KLog.e("TAG","求漂者城市json" + json);
                                        webview.loadUrl("javascript:showMessage("+json.toString()+")");



                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            webview.setWebChromeClient(new WebChromeClient() {
                                @Override
                                public void onProgressChanged(WebView view, int newProgress) {
                                    super.onProgressChanged(view, newProgress);

                                }

                                @Override
                                public void onReceivedTitle(WebView view, String title) {
                                    super.onReceivedTitle(view, title);
                                }
                            });

                            //加载需要显示的网页
                            webview.loadUrl("file:///android_asset/echart.html");

                        }
                    } else {
                        KLog.e("TAG","不inten了吧" );

                        toast("本书还未起漂 无法查看漂流记录");


                    }

                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });





    }

    @Override
    protected void initEvents() {

    }
}
