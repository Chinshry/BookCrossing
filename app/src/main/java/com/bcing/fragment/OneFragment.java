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
import com.bcing.entity.CrossInfo;
import com.bcing.entity.MyUser;
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
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static com.kymjs.rxvolley.toolbox.RxVolleyContext.toast;

//import com.bcing.adapter.BookDetailAdapter;
//import com.bcing.bean.http.douban.BookInfoResponse;

/**
 * @author fml
 *         created at 2016/6/20 13:41
 *         description：LazyFragment使用懒加载方法，避免切换fragment的时候造成其它fragment onstart方法运行,lazyLoad方法代替onstart方法
 */
public class OneFragment extends LazyFragment {
    // 标志fragment是否初始化完成
    private boolean isPrepared;
    private View view;
    private SwipeRefreshLayout swipeRefresh;
    private List<CrossBookData> HomeBookList = new ArrayList<>();
    private HomeBookListAdapter adapter;

    public OneFragment() {
        super();
        KLog.e("TAG", "super");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_one, container, false);
            ButterKnife.bind(this, view);
            KLog.e("TAG", "oneFragment--onCreateView");
            isPrepared = true;
            lazyLoad();
            initBookDetailData();
//            initRecyclerView();
            initSwipe_refresh();
        }
        return view;
    }

    public void initRecyclerView() {
        KLog.e("TAG", "initRecyclerView");
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_View);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(LayoutManager);
        adapter = new HomeBookListAdapter(this.getContext(), HomeBookList);
        recyclerView.setAdapter(adapter);
    }

    public void initBookDetailData()
    {
        //查询数据库中CrossInfo表所有数据
        BmobQuery<CrossInfo> query = new BmobQuery<CrossInfo>();
        String start = "2019-01-01 00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try
        {
            date = sdf.parse(start);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        query.addWhereLessThan("createdAt", new BmobDate(date));
        query.order("-createdAt");
        query.findObjects(new FindListener<CrossInfo>()
        {
            @Override
            public void done(List<CrossInfo> object, BmobException e) {
                KLog.e("TAG", "done");
                if (e == null)
                {
                    Date lastrefreshdate = null;
                    try
                    {
                        lastrefreshdate = sdf.parse(object.get(0).getCreatedAt());
                    } catch (ParseException e1)
                    {
                        e1.printStackTrace();
                    }
                    toast("更新了" + object.size() + "条数据。最新数据时间;" + lastrefreshdate);
                    for (CrossInfo CrossInformation : object)
                    {
                        //解析Url
                        analysisUrl(CrossInformation);
                    }
                } else
                {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    private void analysisUrl(CrossInfo CrossInformation)
    {
        //拼接豆瓣api接口
        String url = URL.HOST_URL_DOUBAN_ISBN + CrossInformation.getIsbn();
        String crossuser = CrossInformation.getCrossuser();
        String crosscity = CrossInformation.getCrosscity();
        //网络请求
        RxVolley.get(url, new HttpCallback()
        {
            @Override
            public void onSuccess(String t)
            {
                //Toast.makeText(getActivity(),t,Toast.LENGTH_LONG).show();
                parsingJson(t,crossuser,crosscity);
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

            HomeBookList.add(bookData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        initRecyclerView();
        return;
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
                    KLog.e("TAG", "刷新了");

//                    initBookDetailData();
//                    adapter.notifyDataSetChanged();
                    swipeRefresh.setRefreshing(false);
                }
            });
        }).start();
    }


    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        KLog.e("TAG", "oneFragment--lazyLoad");
    }



}
