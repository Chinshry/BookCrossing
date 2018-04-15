package com.bcing.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bcing.R;
import com.bcing.adapter.HomeBookListAdapter;
import com.bcing.common.URL;
import com.bcing.entity.CrossBookData;
import com.bcing.entity.MyUser;
import com.bcing.ui.BaseActivity;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.socks.library.KLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

import static com.bcing.utils.common.UIUtils.getContext;

/**
 * Created by chinshry on 2018/3/14.
 * Project Name:BookCrossing
 * Package Name:com.bcing.ui.activity
 * File Name:MyWishActivity
 * Describe：Application
 */


public class MyWishActivity extends BaseActivity {

    private View view;
    private SwipeRefreshLayout swipeRefresh;
    private List<CrossBookData> HomeBookList = new ArrayList<>();
    private HomeBookListAdapter adapter;

    public MyWishActivity() {
        super();
        KLog.e("TAG", "super");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_my_wish);

            KLog.e("TAG", "MyWishActivity--onCreate");
            initBookDetailData();
            initSwipe_refresh();
    }

    public void initBookDetailData() {
        KLog.e("TAG", "MySetFragment initBookDetailData执行了");

//        BmobUser bmobUser = BmobUser.getCurrentUser();

        final JSONArray currentuserwish = (JSONArray) BmobUser.getObjectByKey("wish");
        try {
            KLog.e("TAG", "getString" + currentuserwish.getString(0));
            if(currentuserwish.length()>0){
                for(int i = 0; i < currentuserwish.length(); i++ ){
//                    遍历 jsonarray 数组，把每一个对象转成 json 对象
                    String isbn = currentuserwish.getString(i);
                    analysisUrl(isbn);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void analysisUrl(String isbn){
        //解析接口
        String url = URL.HOST_URL_DOUBAN_ISBN + isbn;
        String crossuser = (String) BmobUser.getObjectByKey("username");
        String crosscity = (String) BmobUser.getObjectByKey("city");
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                //Toast.makeText(getActivity(),t,Toast.LENGTH_LONG).show();

                parsingJson(t,crossuser,crosscity);
                KLog.e("TAG", "onSuccess");
            }
        });


    }

    //解析json
    private void parsingJson(String t, String crossuser, String crosscity) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONArray jsonAuthor = jsonObject.getJSONArray("author");
            CrossBookData bookData = new CrossBookData();
            MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);

            bookData.setIsbn(jsonObject.getString("isbn13"));

            bookData.setUsername(crossuser);
            bookData.setcity(crosscity);

            bookData.setBookName(jsonObject.getString("title"));
            bookData.setAuthor((String) jsonAuthor.get(0));
            bookData.setpublish(jsonObject.getString("publisher"));
            bookData.setBookImage(jsonObject.getString("image"));
            bookData.setPubdate(jsonObject.getString("pubdate"));
            bookData.setPages(jsonObject.getString("pages"));
            bookData.setSummary(jsonObject.getString("summary"));

            KLog.e("TAG", "json获取" + bookData.getBookName());

            HomeBookList.add(bookData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        KLog.e("TAG", "HomeBookList 书名啦" + HomeBookList.get(0).getBookName());

        initRecyclerView();
        return;
    }

    public void initRecyclerView() {
        KLog.e("TAG", "initRecyclerView");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_View);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(LayoutManager);
        adapter = new HomeBookListAdapter(this, HomeBookList);
        recyclerView.setAdapter(adapter);
    }

    public void initSwipe_refresh() {
        KLog.e("TAG", "初始化刷新");
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.orange);
        swipeRefresh.setOnRefreshListener(() -> {
            KLog.e("TAG", "setOnRefreshListener监听到了");
            refreshBook();
        });
    }

    public void refreshBook() {
        KLog.e("TAG", "refreshBook开始清空");

        new Thread(() -> {
            KLog.e("TAG", "Thread");
            ;// do somethings
            try {
                HomeBookList.clear();
                KLog.e("TAG", "清空了还有数据吗" + HomeBookList.size());

                initBookDetailData();
            } catch (Exception e) {
                e.printStackTrace();
            }

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //initBookDetailData();
                    adapter.notifyDataSetChanged();
                    swipeRefresh.setRefreshing(false);
                }
            });
        }).start();
    }





    @Override
    protected void initEvents() {

    }
}

