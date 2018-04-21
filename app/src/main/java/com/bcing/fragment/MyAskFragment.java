package com.bcing.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bcing.R;
import com.bcing.adapter.HomeBookListAdapter;
import com.bcing.common.URL;
import com.bcing.entity.CrossBookData;
import com.bcing.entity.MyUser;
import com.bcing.entity.WantCrossInfo;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.socks.library.KLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static com.kymjs.rxvolley.toolbox.RxVolleyContext.toast;

/**
 * Created by chinshry on 2018/3/14.
 * Project Name:BookCrossing
 * Package Name:com.bcing.fragment
 * File Name:MyAskFragment
 * Describe：Application
 */

public class MyAskFragment extends LazyFragment {

    // 标志fragment是否初始化完成
    private boolean isPrepared;
    private View view;
    private SwipeRefreshLayout swipeRefresh;
    private List<CrossBookData> HomeBookList = new ArrayList<>();
    private HomeBookListAdapter adapter;

    public MyAskFragment() {
        super();
        KLog.e("TAG", "super");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_my_ask, container, false);
            ButterKnife.bind(this, view);
            KLog.e("TAG", "MyAskFragment--onCreateView");
            isPrepared = true;
            initBookDetailData();
            initSwipe_refresh();
            lazyLoad();
        }
        return view;
    }

    public void initBookDetailData() {
        KLog.e("TAG", "MySetFragment initBookDetailData执行了");
        String start = "2019-01-01 00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(start);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final String currentusername = (String) BmobUser.getObjectByKey("username");

        BmobQuery<WantCrossInfo> query = new BmobQuery<WantCrossInfo>();
        query.addWhereEqualTo("wantuser", currentusername);

        query.order("-createdAt");
        query.findObjects(new FindListener<WantCrossInfo>() {
            @Override
            public void done(List<WantCrossInfo> object, BmobException e) {
                KLog.e("TAG", "done");
                if (e == null) {
                    Date lastrefreshdate = null;
                    try {
                        lastrefreshdate = sdf.parse(object.get(0).getCreatedAt());
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                    toast("更新了" + object.size() + "条数据。最新数据时间;" + lastrefreshdate);

                    for (WantCrossInfo WantCrossInformation : object) {
                        Log.i("bmob", "CrossInfo CrossInformation : object" );

                        analysisUrl(WantCrossInformation);

                    }
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    private void analysisUrl(WantCrossInfo WantCrossInformation){
        //解析接口
        String url = URL.HOST_URL_DOUBAN_ISBN + WantCrossInformation.getIsbn();
        String crossuser = WantCrossInformation.getOwnuser();

        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
        query.addWhereEqualTo("username", crossuser);
        query.findObjects(new FindListener<MyUser>() {
            @Override
            public void done(List<MyUser> object,BmobException e) {
                if(e==null){
                    KLog.e("TAG", "查询用户成功"  + object.size());
                    for (MyUser user : object) {
                        String crosscity = user.getCity();
                        RxVolley.get(url, new HttpCallback() {
                            @Override
                            public void onSuccess(String t) {
                                //Toast.makeText(getActivity(),t,Toast.LENGTH_LONG).show();
                                parsingJson(t,crossuser,crosscity);
                                KLog.e("TAG", "onSuccess");
                            }
                        });
                    }
                }else{
                    KLog.e("TAG", "查询用户失败"  + e.getMessage());
                }
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
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_View);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(LayoutManager);
        adapter = new HomeBookListAdapter(this.getContext(), HomeBookList);
        recyclerView.setAdapter(adapter);
    }

    public void initSwipe_refresh() {
        KLog.e("TAG", "初始化刷新");
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
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

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //initBookDetailData();
//                    adapter.notifyDataSetChanged();
                    swipeRefresh.setRefreshing(false);
                }
            });
        }).start();
    }


    @Override
    protected void lazyLoad() {
        if(!isPrepared || !isVisible) {
            return;
        }
        KLog.e("TAG" , "myAsk--lazyLoad");
    }


}