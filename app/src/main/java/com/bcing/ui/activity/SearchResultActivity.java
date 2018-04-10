package com.bcing.ui.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bcing.R;
import com.bcing.adapter.BookListAdapter;
import com.bcing.api.impl.BookListPresenterImpl;
import com.bcing.api.IBookListView;
import com.bcing.bean.http.douban.BookInfoResponse;
import com.bcing.bean.http.douban.BookListResponse;
import com.bcing.ui.BaseActivity;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author   :hymanme
 * Email    :hymanme@163.com
 * Create at 2016/1/14 0014
 * Description:
 */
public class SearchResultActivity extends BaseActivity implements IBookListView, SwipeRefreshLayout.OnRefreshListener {
    //接口调用参数 tag：标签，q：搜索关键词，fields：过滤词，count：一次返回数据数，
    // page：当前已经加载的页数，PS:tag,q只存在其中一个，另一个置空
    private static final int PRO_LOADING_SIZE = 2;//上滑加载提前N个item开始加载更多数据(暂时有bug)
    private static final String fields = "id,title,subtitle,origin_title,rating,author,translator,publisher,pubdate,summary,images,pages,price,binding,isbn13,alt";
    private int count = 20;
    private int page = 0;
    private String q;

    private boolean isLoadAll;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_widget)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private GridLayoutManager mLayoutManager;
    private BookListAdapter mListAdapter;
    private List<BookInfoResponse> bookInfoResponses;
    private BookListPresenterImpl bookListPresenter;
    private int spanCount = 1;

    private View view1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        KLog.e("TAG", "SearchResultActivity onCreate");
        if (savedInstanceState != null) {
            isLoadAll = savedInstanceState.getBoolean("isLoadAll");
        }
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initEvents() {
        KLog.e("TAG", "initEvents");
        q = getIntent().getStringExtra("q");
        setTitle(q);
        spanCount = (int) getResources().getInteger(R.integer.home_span_count);
        bookListPresenter = new BookListPresenterImpl(this);
        bookInfoResponses = new ArrayList<>();
        mSwipeRefreshLayout.setColorSchemeResources(R.color.recycler_color1, R.color.recycler_color2,
                R.color.recycler_color3, R.color.recycler_color4);

        mLayoutManager = new GridLayoutManager(SearchResultActivity.this, spanCount);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mListAdapter.getItemColumnSpan(position);
            }
        });
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //设置adapter
        mListAdapter = new BookListAdapter(this, bookInfoResponses, spanCount);
        mRecyclerView.setAdapter(mListAdapter);

        //设置Item增加、移除动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mListAdapter.getItemCount()) {
                    KLog.e("TAG", "onScrollStateChanged");
                    onLoadMore();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                KLog.e("TAG", "onScrolled");
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(this);
        onRefresh();
    }

    @Override
    public void showMessage(String msg) {
        view1 = View.inflate(this, R.layout.activity_search, null);
        Snackbar.make(view1, msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showProgress() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void hideProgress() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void refreshData(Object result) {
        bookInfoResponses.clear();
        bookInfoResponses.addAll(((BookListResponse) result).getBooks());
        mListAdapter.notifyDataSetChanged();
        if (((BookListResponse) result).getTotal() > page * count) {
            isLoadAll = false;
        } else {
            isLoadAll = true;
        }
        page++;
    }

    @Override
    public void addData(Object result) {
        bookInfoResponses.addAll(((BookListResponse) result).getBooks());
        mListAdapter.notifyDataSetChanged();
        if (((BookListResponse) result).getTotal() > page * count) {
            page++;
            isLoadAll = false;
        } else {
            isLoadAll = true;
        }
    }

    @Override
    public void onRefresh() {
        page = 1;
        bookListPresenter.loadBooks(q, null, 0, count, fields);
    }

    private void onLoadMore() {
        if (!isLoadAll) {
            if (!mSwipeRefreshLayout.isRefreshing()) {
                bookListPresenter.loadBooks(q, null, page * count, count, fields);
            }
        } else {
            showMessage(getResources().getString(R.string.no_more));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isLoadAll", isLoadAll);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        bookListPresenter.cancelLoading();
        super.onDestroy();
    }
}
