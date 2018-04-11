package com.bcing.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcing.R;
import com.bcing.adapter.AllBookDetailAdapter;
import com.bcing.common.URL;
import com.bcing.entity.CrossInfo;
import com.bcing.entity.ItemBookDetail;
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
//    private ListView mListView;


    private TextView bookset_title;
    private TextView bookset_author;
    private TextView bookset_publisher;
    private ImageView bookset_image;

    private long lastTime = 0L;
    private SwipeRefreshLayout swipeRefresh;
    private List<ItemBookDetail> BookDetailList = new ArrayList<>();
    private AllBookDetailAdapter adapter;

    public OneFragment() {
        super();
        KLog.e("TAG", "super");
//        initBookDetailData();

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

//        View view = inflater.inflate(R.layout.book_set_item, null);
//        findView(view);
        return view;
    }

    public void initRecyclerView() {
        KLog.e("TAG", "initRecyclerView");

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_View);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(LayoutManager);
        adapter = new AllBookDetailAdapter(this.getContext(), BookDetailList);
        recyclerView.setAdapter(adapter);
    }

    public void initBookDetailData() {
        KLog.e("TAG", "initBookDetailData你执行了几次啊");

        BmobQuery<CrossInfo> query = new BmobQuery<CrossInfo>();
//        query.addWhereContains("createdAt", "2018");
        String start = "2019-01-01 00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;

        try {
            date = sdf.parse(start);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        query.addWhereLessThan("createdAt", new BmobDate(date));

        query.order("-createdAt");

        query.findObjects(new FindListener<CrossInfo>() {
            @Override
            public void done(List<CrossInfo> object, BmobException e) {
                KLog.e("TAG", "done");

                if (e == null) {
                    Date lastrefreshdate = null;
                    try {
                        lastrefreshdate = sdf.parse(object.get(0).getCreatedAt());
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }

                    toast("更新了" + object.size() + "条数据。最新数据时间;" + lastrefreshdate);
                    KLog.e("TAG", "第一条 " + object.get(0).getIsbn());
                    KLog.e("TAG", "第一条 " + object.get(1).getIsbn());
                    KLog.e("TAG", "第一条 " + object.get(2).getIsbn());


                    for (CrossInfo CrossInformation : object) {
                        //解析接口
                        String url = URL.HOST_URL_DOUBAN_ISBN + CrossInformation.getIsbn();
                        KLog.e("TAG", "for循环 " + url);

                        RxVolley.get(url, new HttpCallback() {
                            @Override
                            public void onSuccess(String t) {
                                //Toast.makeText(getActivity(),t,Toast.LENGTH_LONG).show();
                                parsingJson(t);
                                KLog.e("TAG", "onSuccess");

                            }
                        });
//            ItemBookDetail a = new ItemBookDetail("","","","","");
//            BookDetailList.add(a);
                    }
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

//    //初始化View
//    private void findView(View view) {
//
//
//        bookset_title = (TextView)view.findViewById(R.id.bookset_title);
//        bookset_author = (TextView) view.findViewById(R.id.bookset_author);
//        bookset_publisher = (TextView) view.findViewById(R.id.bookset_publisher);
//        bookset_image = (ImageView) view.findViewById(R.id.bookset_image);
//
//
//
//        mListView = (ListView) view.findViewById(R.id.mListView);
//
//
//    }

    //解析json
    private void parsingJson(String t) {
        String[] bookData = new String[]{"", "", "", "", "", "",};
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONArray jsonAuthor = jsonObject.getJSONArray("author");
//            for (int i = 0; i < 1; i++) {
//                BookData data = new BookData();
//                data.setTitle(jsonObject.getString("title"));
//                data.setAuthor((String) jsonAuthor.get(0));
//                data.setPublisher(jsonObject.getString("publisher"));
//                data.setImage(jsonImages.getString("medium"));
//
//                mList.add(data);
//            }
            MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);

            bookData[0] = userInfo.getUsername();
            bookData[1] = jsonObject.getString("title");
            bookData[2] = (String) jsonAuthor.get(0);
            bookData[3] = jsonObject.getString("publisher");
            bookData[4] = userInfo.getCity();
            bookData[5] = jsonObject.getString("image");


//            bookset_title.setText(jsonObject.getString("title"));
//            bookset_author.setText((String) jsonAuthor.get(0));
//            bookset_publisher.setText(jsonObject.getString("publisher"));
//            bookset_image.setText(jsonImages.getString("medium"));
//
//            BookAdapter adapter = new BookAdapter(getActivity(), mList);
//            mListView.setAdapter(adapter);
            KLog.e("TAG", "json获取" + bookData[1]);



        } catch (JSONException e) {
            e.printStackTrace();
        }

        ItemBookDetail a = new ItemBookDetail(bookData[0], bookData[1], bookData[2], bookData[3], bookData[4], bookData[5]);


        BookDetailList.add(a);
        KLog.e("TAG", "BookDetailList 书名啦" + BookDetailList.get(0).getBookName());

        initRecyclerView();

        return;

    }


    public void initSwipe_refresh() {
        KLog.e("TAG", "refreshBook开始清空");
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.orange);
        swipeRefresh.setOnRefreshListener(() -> {
            KLog.e("TAG", "setOnRefreshListener监听到了");
            refreshBook();
        });
    }

    public void refreshBook() {
        KLog.e("TAG", "refreshBook开始清空" + BookDetailList.get(0).getcity());

        new Thread(() -> {
            KLog.e("TAG", "Thread");

            ;// do somethings
            try {
//                    OkHttpClient client = new OkHttpClient();
//                    RequestBody requestBody = new FormBody.Builder().add("lastTime", lastTime + "").build();
//                    lastTime = new Date().getTime() / 1000L;
//                    Request request = new Request.Builder().url("http://120.24.217.191/Book/APP/queryPose").post(requestBody).build();
//                    Response response = client.newCall(request).execute();
//                    String responseData = response.body().string();
//                    handleResponseData(responseData);
                BookDetailList.clear();
                KLog.e("TAG", "清空了还有数据吗" + BookDetailList.size());

                initBookDetailData();
//                    initRecyclerView();
            } catch (Exception e) {
                e.printStackTrace();
            }


            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //initBookDetailData();
                    adapter.notifyDataSetChanged();
                    swipeRefresh.setRefreshing(false);
                }
            });
        }).start();
    }

//    public void handleResponseData(final String responseData) {
//        try {
//            JSONArray jsonArray = new JSONArray(responseData);
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                String username = jsonObject.getString("username");
//                String bookName = jsonObject.getString("bookName");
//                String author = jsonObject.getString("author");
//                String press = jsonObject.getString("press");
//                String recommendedReason = jsonObject.getString("recommendedReason");
//                ItemBookDetail a = new ItemBookDetail(username, "《" + bookName + "》", author, press, recommendedReason);
//                BookDetailList.add(0, a);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        KLog.e("TAG", "oneFragment--lazyLoad");
    }

//    @Override
//    protected void initEvents() {
//
//    }
//
//    @Override
//    protected void initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//    }

}
