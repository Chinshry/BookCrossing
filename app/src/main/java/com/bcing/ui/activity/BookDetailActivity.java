package com.bcing.ui.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bcing.R;
import com.bcing.adapter.BookDetailAdapter;
import com.bcing.api.IBookDetailView;
import com.bcing.api.impl.BookDetailPresenterImpl;
import com.bcing.bean.http.douban.BookInfoResponse;
import com.bcing.bean.http.douban.BookSeriesListResponse;
import com.bcing.dialog.ConfirmCancelDialog;
import com.bcing.entity.CrossInfo;
import com.bcing.entity.MyUser;
import com.bcing.ui.BaseActivity;
import com.bcing.utils.common.Blur;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.kymjs.rxvolley.toolbox.RxVolleyContext.toast;

/**
 * Author   :hymanme
 * Email    :hymanme@163.com
 * Create at 2016/2/19 0019
 * Description: 图书详情页
 */
public class BookDetailActivity extends BaseActivity implements IBookDetailView {
    private static final String COMMENT_FIELDS = "id,rating,author,title,updated,comments,summary,votes,useless";
    private static final String SERIES_FIELDS = "id,title,subtitle,origin_title,rating,author,translator,publisher,pubdate,summary,images,pages,price,binding,isbn13,series";
    //    private static final int REVIEWS_COUNT = 5;
//    private static final int SERIES_COUNT = 6;
    private static final int PAGE = 0;

    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout mCollapsingLayout;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
//    @BindView(R.id.webView)
//    WebView webView;
    private LinearLayoutManager mLayoutManager;
    private BookDetailAdapter mDetailAdapter;
    private ImageView iv_book_img;
    private ImageView iv_book_bg;

    private BookInfoResponse mBookInfoResponse;
    //    private BookReviewsListResponse mReviewsListResponse;
    private BookSeriesListResponse mSeriesListResponse;

    private BookDetailPresenterImpl bookDetailPresenter;

    private Integer num ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_book_detail);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        mToolbar.setNavigationIcon(AppCompatResources.getDrawable(this, R.drawable.ic_action_clear));

    }

    @Override
    protected void initEvents() {
        bookDetailPresenter = new BookDetailPresenterImpl(this);
//        mReviewsListResponse = new BookReviewsListResponse();
        mSeriesListResponse = new BookSeriesListResponse();
        mBookInfoResponse = (BookInfoResponse) getIntent().getSerializableExtra(BookInfoResponse.serialVersionName);
        mLayoutManager = new LinearLayoutManager(BookDetailActivity.this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mDetailAdapter = new BookDetailAdapter(mBookInfoResponse);
        mRecyclerView.setAdapter(mDetailAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //头部图片
        iv_book_img = (ImageView) findViewById(R.id.iv_book_img);
        iv_book_bg = (ImageView) findViewById(R.id.iv_book_bg);
        mCollapsingLayout.setTitle(mBookInfoResponse.getTitle());

        Bitmap book_img = getIntent().getParcelableExtra("book_img");
        if (book_img != null) {
            iv_book_img.setImageBitmap(book_img);
            iv_book_bg.setImageBitmap(Blur.apply(book_img));
            iv_book_bg.setAlpha(0.9f);
        } else {
            Glide.with(this)
                    .load(mBookInfoResponse.getImages().getLarge())
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            iv_book_img.setImageBitmap(resource);
                            iv_book_bg.setImageBitmap(Blur.apply(resource));
                            iv_book_bg.setAlpha(0.9f);
                        }
                    });
        }
        mFab.setOnClickListener(v -> {
            final String currentcity = (String) BmobUser.getObjectByKey("city");

            if (currentcity.isEmpty()) {
                toast("请到个人页面完善信息！");
            } else {
                ConfirmCancelDialog dialog = ConfirmCancelDialog.getInstance(BookDetailActivity.this,
                        new ConfirmCancelDialog.DialogSetListener() {
                            @Override
                            public void setDialog(TextView title, TextView message, Button leftBtn, Button betBtn, Button rightBtn) {
                                title.setText("确认");
                                message.setText("您要对《" + mBookInfoResponse.getTitle() + "》进行什么操作？");
                                message.setGravity(Gravity.CENTER);
                                leftBtn.setText("我要漂流");
                                betBtn.setText("我要收藏");
                                rightBtn.setText("取消");
                            }
                        });
                dialog.setDialogClickListener(new ConfirmCancelDialog.DialogClickListener() {
                    @Override
                    public void leftClickListener() {

                        CrossInfo crossinfo = new CrossInfo();
                        MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);


                        //为crosscode统计个数
                        BmobQuery<CrossInfo> querynum = new BmobQuery<CrossInfo>();
                        querynum.addQueryKeys("objectId");
                        querynum.count(CrossInfo.class, new CountListener() {
                            @Override
                            public void done(Integer count, BmobException e) {
                                if(e==null){
                                    num = count;
                                    KLog.e("bmob", "count对象个数为：" + num);
                                }else{
                                    num = 0;
                                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                }
                            }
                        });


                        //Bmob复合查询不允许用户重复漂流同一本书
                        BmobQuery<CrossInfo> eq1 = new BmobQuery<CrossInfo>();
                        eq1.addWhereEqualTo("crossuser", userInfo.getUsername());

                        BmobQuery<CrossInfo> eq2 = new BmobQuery<CrossInfo>();
                        eq2.addWhereEqualTo("isbn", mBookInfoResponse.getIsbn13());

                        List<BmobQuery<CrossInfo>> andQuerys = new ArrayList<BmobQuery<CrossInfo>>();
                        andQuerys.add(eq1);
                        andQuerys.add(eq2);

                        BmobQuery<CrossInfo> query = new BmobQuery<CrossInfo>();
                        query.and(andQuerys);
                        query.findObjects(new FindListener<CrossInfo>() {
                            @Override
                            public void done(List<CrossInfo> object, BmobException e) {
                                KLog.e("bmob", "List：" + object.isEmpty());
                                if (e == null) {
                                    if (object.isEmpty()) {
                                        crossinfo.setIsbn(mBookInfoResponse.getIsbn13());
                                        crossinfo.setCrossuser(userInfo.getUsername());
                                        crossinfo.setCrosscity(userInfo.getCity());
                                        KLog.e("bmob", "表里有多少条数据" + num);
                                        crossinfo.setCrosscode(num + 1);

                                        {
                                            crossinfo.save(new SaveListener<String>() {
                                                @Override
                                                public void done(String objectId, BmobException e) {
                                                    if (e == null) {
                                                        toast("您的《" + mBookInfoResponse.getTitle() + "》已开始漂流！");
                                                    } else {
                                                        toast("创建数据失败：" + e.getMessage());
                                                        KLog.e("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                                                    }
                                                }
                                            });
                                        }
                                    } else {
                                        toast("您已漂流过该书籍");
                                    }
                                } else {
                                    KLog.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                                }
                            }
                        });
                    }

                    @Override
                    public void betClickListener() {

//                    CrossInfo crossinfo = new CrossInfo();
                        MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);


                        //Bmob复合查询
                        BmobQuery<MyUser> eq1 = new BmobQuery<MyUser>();
                        eq1.addWhereEqualTo("username", userInfo.getUsername());
                        String[] isbn = {mBookInfoResponse.getIsbn13()};
                        BmobQuery<MyUser> eq2 = new BmobQuery<MyUser>();
                        eq2.addWhereContainsAll("wish", Arrays.asList(isbn));

                        List<BmobQuery<MyUser>> andQuerys = new ArrayList<BmobQuery<MyUser>>();
                        andQuerys.add(eq1);
                        andQuerys.add(eq2);

                        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
                        query.and(andQuerys);
                        query.findObjects(new FindListener<MyUser>() {
                            @Override
                            public void done(List<MyUser> object, BmobException e) {
                                KLog.e("bmob", "List：" + object.isEmpty());
                                if (e == null) {
                                    if (object.isEmpty()) {
                                        userInfo.add("wish", isbn[0]);
                                        {
                                            userInfo.update(new UpdateListener() {

                                                @Override
                                                public void done(BmobException e) {
                                                    if (e == null) {
                                                        toast("您已成功收藏《" + mBookInfoResponse.getTitle() + "》！");

                                                    } else {
                                                        toast("创建数据失败：" + e.getMessage());
                                                        KLog.e("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                                                    }
                                                }

                                            });
//                                        userInfo.save(new SaveListener<String>() {
//                                            @Override
//                                            public void done(String objectId, BmobException e) {
//                                                if (e == null) {
//                                                    toast("您已成功收藏《" + mBookInfoResponse.getTitle() + "》！");
//                                                } else {
//                                                    toast("创建数据失败：" + e.getMessage());
//                                                    KLog.e("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
//                                                }
//                                            }
//                                        });
                                        }
                                    } else {
                                        toast("您已收藏过该书籍");
                                    }
                                } else {
                                    KLog.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                                }

                            }
                        });


                    }

                    @Override
                    public void rightClickListener() {
                        Toast.makeText(BookDetailActivity.this, "您取消了漂流", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show(getSupportFragmentManager(), "confirmCancelDialog");
            }

//            Intent intent = new Intent(BookDetailActivity.this, WebViewActivity.class);
//            intent.putExtra("url", mBookInfoResponse.getUrl());
//            startActivity(intent);
        });
//        bookDetailPresenter.loadReviews(mBookInfoResponse.getId(), PAGE * REVIEWS_COUNT, REVIEWS_COUNT, COMMENT_FIELDS);
    }

//    @Override
//    protected int getMenuID() {
//        return R.menu.menu_book_detail;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                startActivity(new Intent(this, MainActivity.class));
                KLog.e("TAG", "android.R.id.home");
                this.finish();
                return true;
//            case R.id.action_share:
//                KLog.e("TAG", "action_share");

//                StringBuilder sb = new StringBuilder();
//                sb.append(getString(R.string.your_friend));
//                sb.append(getString(R.string.share_book_1));
//                sb.append(mBookInfoResponse.getTitle());
//                sb.append(getString(R.string.share_book_2));
//                UIUtils.share(this, sb.toString(), null);
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    if (item.getIcon() instanceof Animatable) {
//                        ((Animatable) item.getIcon()).start();
//                    }
//                }
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showMessage(String msg) {
        Snackbar.make(mToolbar, msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (mFab.getDrawable() instanceof Animatable) {
                ((Animatable) mFab.getDrawable()).start();
            }
        } else {
            //低于5.0，显示其他动画
//            showMessage(getString(R.string.loading));
        }
    }

    @Override
    public void hideProgress() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (mFab.getDrawable() instanceof Animatable) {
                ((Animatable) mFab.getDrawable()).stop();
            }
        } else {
            //低于5.0，显示其他动画
        }
    }

//    @Override
//    public void updateView(Object result) {
//        if (result instanceof BookReviewsListResponse) {
//            final BookReviewsListResponse response = (BookReviewsListResponse) result;
//            mReviewsListResponse.setTotal(response.getTotal());
//            mReviewsListResponse.getReviews().addAll(response.getReviews());
//            mDetailAdapter.notifyDataSetChanged();
//            if (mBookInfoResponse.getSeries() != null) {
//                bookDetailPresenter.loadSeries(mBookInfoResponse.getSeries().getId(), PAGE * SERIES_COUNT, 6, SERIES_FIELDS);
//            }
//        } else if (result instanceof BookSeriesListResponse) {
//            final BookSeriesListResponse response = (BookSeriesListResponse) result;
//            mSeriesListResponse.setTotal(response.getTotal());
//            mSeriesListResponse.getBooks().addAll(response.getBooks());
//            mDetailAdapter.notifyDataSetChanged();
//        }
//    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        bookDetailPresenter.cancelLoading();
        if (mFab.getDrawable() instanceof Animatable) {
            ((Animatable) mFab.getDrawable()).stop();
        }

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            super.onBackPressed();
        }
    }
}
