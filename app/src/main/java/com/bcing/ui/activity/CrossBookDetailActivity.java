package com.bcing.ui.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bcing.R;
import com.bcing.dialog.ConfirmCancelDialog;
import com.bcing.entity.CrossBookData;
import com.bcing.entity.CrossInfo;
import com.bcing.entity.MyUser;
import com.bcing.entity.WantCrossInfo;
import com.bcing.ui.BaseActivity;
import com.bcing.utils.common.UIUtils;
import com.bumptech.glide.Glide;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.kymjs.rxvolley.toolbox.RxVolleyContext.toast;

/**
 * Created by chinshry on 2018/3/14.
 * Project Name:BookCrossing
 * Package Name:com.bcing.ui.activity
 * File Name:CossBookDetailActivity
 * Describe：Application
 */

public class CrossBookDetailActivity extends BaseActivity {
    private View view;

    TextView tv_crosscode;
    ImageView book_img;
    TextView tv_book_title;
    TextView tv_book_isbn;
    TextView tv_book_pub;
    TextView tv_book_pubdate;
    TextView tv_book_pages;
    TextView tv_book_summary;
    FloatingActionButton fab;

    private CrossBookData mCrossBookInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crossbook_detail);
        initView();
    }

    private void initView() {
        tv_crosscode = (TextView) findViewById(R.id.tv_crosscode);
        book_img = (ImageView) findViewById(R.id.book_img);
        tv_book_title = (TextView) findViewById(R.id.tv_book_title);
        tv_book_isbn = (TextView) findViewById(R.id.tv_book_isbn);
        tv_book_pub = (TextView) findViewById(R.id.tv_book_pub);
        tv_book_pubdate = (TextView) findViewById(R.id.tv_book_pubdate);
        tv_book_pages = (TextView) findViewById(R.id.tv_book_pages);
        tv_book_summary = (TextView) findViewById(R.id.tv_book_summary);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        mCrossBookInfo = (CrossBookData) getIntent().getSerializableExtra(CrossBookData.serialVersionName);
        setTitle(mCrossBookInfo.getBookName());

        KLog.e("TAG", mCrossBookInfo.getBookName() + mCrossBookInfo.getcity());

        //Bmob复合查询 通过isbn和用户定位漂流码

        BmobQuery<CrossInfo> query1 = new BmobQuery<CrossInfo>();
        BmobQuery<CrossInfo> query2 = new BmobQuery<CrossInfo>();
        query1.addWhereEqualTo("crossuser", mCrossBookInfo.getUsername());
        query2.addWhereEqualTo("isbn", mCrossBookInfo.getIsbn());
        List<BmobQuery<CrossInfo>> andQuerys = new ArrayList<BmobQuery<CrossInfo>>();
        andQuerys.add(query1);
        andQuerys.add(query2);

        BmobQuery<CrossInfo> query = new BmobQuery<CrossInfo>();
        query.and(andQuerys);
        query.findObjects(new FindListener<CrossInfo>() {
            @Override
            public void done(List<CrossInfo> object, BmobException e) {
                KLog.e("bmob", "List is Empty：" + object.isEmpty());
                if (e == null) {
                    for (CrossInfo crossinforesult : object) {
                        mCrossBookInfo.setCrosscode(crossinforesult.getCrosscode());
                        KLog.e("TAG", "getCrosscode：" + crossinforesult.getCrosscode());
                        tv_crosscode.setText("漂流码: " + String.format("%05d", mCrossBookInfo.getCrosscode()));
                    }

                } else {
                    KLog.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }

            }
        });

        Glide.with(UIUtils.getContext()).load(mCrossBookInfo.getBookImage()).into(book_img);
        tv_book_title.setText(mCrossBookInfo.getBookName());
        tv_book_isbn.setText("ISBN: " + mCrossBookInfo.getIsbn());
        tv_book_pub.setText(mCrossBookInfo.getAuthor() + " | " + mCrossBookInfo.getpublish());
        tv_book_pubdate.setText("出版日: " + mCrossBookInfo.getPubdate());
        tv_book_pages.setText("页数: " + mCrossBookInfo.getPages());
        tv_book_summary.setText(mCrossBookInfo.getSummary());

        fab.setOnClickListener(v -> {
//            BmobQuery<MyUser> queryuser = new BmobQuery<MyUser>();
//            final MyUser myUser = new MyUser();
//            queryuser.getObject("7e9978cf8c", new QueryListener<MyUser>() {
//                @Override
//                public void done(MyUser object, BmobException e) {
//                    if (e == null) {
//                        //获得playerName的信息
//                        object.getUsername();
//                        myUser.setUsername(object.getUsername());
//                        myUser.setCity(object.getCity());
//
//
//                        KLog.e("bmob", "done内 本用户名为：" + object.getUsername());
//                        KLog.e("bmob", "done内 本用户名为：" + myUser.getUsername());
//
//                    } else {
//                        KLog.e("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
//                    }
//                }
//            });


//            if (){
//
//            }else {
//            }

            final String currentusername = (String) BmobUser.getObjectByKey("username");
            final String currentcity = (String) BmobUser.getObjectByKey("city");

            KLog.e("TAG", "用户名还在吗" + currentusername + currentcity);

            ConfirmCancelDialog dialog = ConfirmCancelDialog.getInstance(CrossBookDetailActivity.this,
                    new ConfirmCancelDialog.DialogSetListener() {

                        @Override
                        public void setDialog(TextView title, TextView message, Button leftBtn, Button betBtn, Button rightBtn) {
                            title.setText("确认");
                            message.setText("您确定要求漂《" + mCrossBookInfo.getBookName() + "》吗？");
                            message.setGravity(Gravity.CENTER);
                            leftBtn.setText("确定求漂");
                            betBtn.setText("先收藏");
                            rightBtn.setText("取消");
                        }
                    });
            dialog.setDialogClickListener(new ConfirmCancelDialog.DialogClickListener() {
                @Override
                public void leftClickListener() {

                    WantCrossInfo wantCrossInfo = new WantCrossInfo();
                    String [] array = {currentusername};
                    //Bmob复合查询
//                    BmobQuery<WantCrossInfo> eq1 = new BmobQuery<WantCrossInfo>();
//                    eq1.addWhereContainsAll("wantuser", Arrays.asList(array));

                    BmobQuery<WantCrossInfo> eq2 = new BmobQuery<WantCrossInfo>();
                    eq2.addWhereEqualTo("isbn", mCrossBookInfo.getIsbn());

                    BmobQuery<WantCrossInfo> eq3 = new BmobQuery<WantCrossInfo>();
                    eq3.addWhereEqualTo("ownuser", mCrossBookInfo.getUsername());

                    List<BmobQuery<WantCrossInfo>> andQuerys = new ArrayList<BmobQuery<WantCrossInfo>>();
//                    andQuerys.add(eq1);
                    andQuerys.add(eq2);
                    andQuerys.add(eq3);

                    BmobQuery<WantCrossInfo> query = new BmobQuery<WantCrossInfo>();
                    query.and(andQuerys);
                    query.findObjects(new FindListener<WantCrossInfo>() {
                        @Override
                        public void done(List<WantCrossInfo> object, BmobException e) {

                            if (e == null) {
                                if (object.isEmpty()) {
                                    KLog.e("bmob", "List：" + object.isEmpty());
                                    wantCrossInfo.setIsbn(mCrossBookInfo.getIsbn());
                                    wantCrossInfo.setCrosscode(mCrossBookInfo.getCrosscode());
                                    wantCrossInfo.setOwnuser(mCrossBookInfo.getUsername());
                                    wantCrossInfo.add("wantuser", currentusername);
                                    wantCrossInfo.add("wantusercity", currentcity);

                                    {
                                        wantCrossInfo.save(new SaveListener<String>() {
                                            @Override
                                            public void done(String objectId, BmobException e) {
                                                if (e == null) {
                                                    toast("您已成功求漂《" + mCrossBookInfo.getBookName() + "》已开始漂流！");
                                                } else {
                                                    toast("创建数据失败：" + e.getMessage());
                                                    KLog.e("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                                                }
                                            }
                                        });
                                    }


                                } else {
                                    toast("您已求漂过该书籍");
                                }
                            } else {
                                KLog.e("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
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
                    String[] isbn = {mCrossBookInfo.getIsbn()};
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
                                                    toast("您已成功收藏《" + mCrossBookInfo.getBookName() + "》！");

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
                    Toast.makeText(CrossBookDetailActivity.this, "您取消了操作", Toast.LENGTH_SHORT).show();
                }

            });
            dialog.show(getSupportFragmentManager(), "confirmCancelDialog");


//            Intent intent = new Intent(BookDetailActivity.this, WebViewActivity.class);
//            intent.putExtra("url", mBookInfoResponse.getUrl());
//            startActivity(intent);
        });


    }

    @Override
    protected void initEvents() {

    }

}
