package com.bcing.main;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.bcing.R;
import com.bcing.entity.MyUser;
import com.bcing.fragment.FiveFragment;
import com.bcing.fragment.FourFragment;
import com.bcing.fragment.LazyFragment;
import com.bcing.fragment.OneFragment;
import com.bcing.fragment.ThreeFragment;
import com.bcing.fragment.TwoFragment;
import com.bcing.holder.SearchViewHolder;
import com.bcing.ui.activity.SearchResultActivity;
import com.bcing.utils.RefreshEvent;
import com.bcing.utils.common.KeyBoardUtils;
import com.bcing.utils.common.PermissionUtils;
import com.bcing.utils.customtabs.CustomTabActivityHelper;
import com.orhanobut.logger.Logger;
import com.socks.library.KLog;
import com.zhy.autolayout.AutoLayoutActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

import static com.kymjs.rxvolley.toolbox.RxVolleyContext.toast;

/**
 * @author fml
 * created at 2016/6/20 13:41
 * description：
 */
public class MainActivity extends AutoLayoutActivity {
    @BindView(R.id.m_main_viewpager)
    ViewPager mMainViewPager;
    @BindView(R.id.m_main_fw_btn_true)
    Button mMainFwBtnTrue;
    @BindView(R.id.m_main_sj_btn_true)
    Button mMainSjBtnTrue;
    @BindView(R.id.m_main_gz_btn_true)
    Button mMainGzBtnTrue;
    @BindView(R.id.m_main_lxr_btn_true)
    Button mMainLxrBtnTrue;
    @BindView(R.id.m_main_wd_btn_true)
    Button mMainWdBtnTrue;

    //fragment
    private Fragment mFragmentOne, mFragmentTwo, mFragmentThree, mFragmentFour, mFragmentFive;
    //fragment 适配器
    private FragmentAdapter mFragmentAdapter = null;
    //fragment 集合
    private List<Fragment> mFragmentList = new ArrayList<>();
    //Button集合
    private List<Button> mButtonList = new ArrayList<>();

//    private FragmentManager fragmentManager = getSupportFragmentManager();
    public static MainActivity activity;

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private LazyFragment currentFragment;
    private int currentIndex;
    private long lastTime = 0;
    private static final int EXIT_APP_DELAY = 1000;

    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);



        if (fragmentManager == null) {
            Log.e("TAG", "fragmentManager");
            fragmentManager = getSupportFragmentManager();
        }

//        currentFragment = HomeFragment.newInstance();
//        FragmentTransaction ft = fragmentManager.beginTransaction();
//        ft.replace(R.id.fl_content, currentFragment).commit();

//        if (savedInstanceState != null)
////        {
////            Log.e("TAG", "savedInstanceState");
////            currentFragment = HomeFragment.newInstance();
////            FragmentTransaction ft = fragmentManager.beginTransaction();
////            ft.replace(R.id.fl_content, currentFragment).commit();
////        } else
//            {
//            //activity销毁后记住销毁前所在页面
//            Log.e("TAG", "savedInstanceState != null");
//            currentIndex = savedInstanceState.getInt("currentIndex");
//            switch (this.currentIndex) {
//                case 0:
//                    Log.e("TAG", "0");
//                    if (currentFragment == null) {
//                        currentFragment = LazyFragment.newInstance();
//                    }
//                    switchContent(null, currentFragment);
//                    break;
//                case 1:
//                    Log.e("TAG", "1");
//                    break;
//            }
//        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        if (savedInstanceState == null) {
//            Log.e("TAG", "savedInstanceState");
//            currentFragment = TwoFragment.newInstance(0);
////            FragmentTransaction ft = fragmentManager.beginTransaction();
////            ft.replace(R.id.bookset_image, currentFragment).commit();
//        }

//        View view = View.inflate(this, R.layout.fragment_three, null);
//
//        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
//        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
//
//        if (viewPager != null) {
//            KLog.e("viewPager", "setupViewPager");
//            setupViewPager(viewPager);
//        }
        initFragment();
        initAdapter();
        changAlpha(0);
        iniDate();
        iniView();


//connect server
        MyUser user = BmobUser.getCurrentUser(MyUser.class);
        BmobIM.connect(user.getObjectId(), new ConnectListener() {
            @Override
            public void done(String uid, BmobException e) {
                if (e == null) {
                    Logger.i("connect success");
                    //服务器连接成功就发送一个更新事件，同步更新会话及主页的小红点
                    EventBus.getDefault().post(new RefreshEvent());
                } else {
                    Logger.e(e.getErrorCode() + "/" + e.getMessage());
                }
            }
        });
        //监听连接状态，也可通过BmobIM.getInstance().getCurrentStatus()来获取当前的长连接状态
        BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
            @Override
            public void onChange(ConnectionStatus status) {
                toast("" + status.getMsg());
            }
        });
//        //解决leancanary提示InputMethodManager内存泄露的问题
//        IMMLeaks.fixFocusedViewLeak(getApplication());

    }



//    private void setupViewPager(ViewPager viewPager) {
//        adapter = new MyViewPagerAdapter(getSupportFragmentManager(), this);
//        adapter.addFragment(new ThreeFragmentTab1().newInstance("Page1"), getString(R.string.myset));
//        adapter.addFragment(new ThreeFragmentTab2().newInstance("Page2"), getString(R.string.mysearch));
//
//        viewPager.setAdapter(adapter);
//        tabLayout.setupWithViewPager(viewPager);
//    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        KLog.e("TAG", "android.R.id.home");
        outState.putInt("currentIndex", currentIndex);
        super.onSaveInstanceState(outState);
    }


    /**
     * 初始化数据
     */
    private void iniDate(){

    }

    /**
     * 初始化View
     */
    private void iniView(){

    }

//    @Override
//    public void onBackPressed() {
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            if (currentFragment == null) {
//                currentFragment = LazyFragment.newInstance();
//            }
//
//            if ((System.currentTimeMillis() - lastTime) > EXIT_APP_DELAY) {
//                Snackbar.make(drawer, getString(R.string.press_twice_exit), Snackbar.LENGTH_SHORT)
//                        .setAction(R.string.exit_directly, v -> {
//                            MainActivity.super.onBackPressed();
//                        })
//                        .show();
//                lastTime = System.currentTimeMillis();
//            } else {
//                moveTaskToBack(true);
//            }
//        }
//    }




    /**
     * 初始化fagment
     */
    private void initFragment() {
        mFragmentOne = new OneFragment();
        mFragmentTwo = new TwoFragment();
        mFragmentThree = new ThreeFragment();
        mFragmentFour = new FourFragment();
        mFragmentFive = new FiveFragment();
        mFragmentList.add(mFragmentOne);
        mFragmentList.add(mFragmentTwo);
        mFragmentList.add(mFragmentThree);
        mFragmentList.add(mFragmentFour);
        mFragmentList.add(mFragmentFive);

        mButtonList.add(mMainFwBtnTrue);
        mButtonList.add(mMainSjBtnTrue);
        mButtonList.add(mMainGzBtnTrue);
        mButtonList.add(mMainLxrBtnTrue);
        mButtonList.add(mMainWdBtnTrue);
    }

    /**
     * switch fragment.
     *
     * @param from
     * @param to
     */

    @SuppressLint("RestrictedApi")
    public void switchContent(LazyFragment from, LazyFragment to) {
        if (currentFragment == to) {
            return;
        } else {
            currentFragment = to;
            //添加渐隐渐现的动画
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.setCustomAnimations(R.anim.fragment_fade_in, R.anim.fragment_fade_out);
            ft.replace(R.id.fl_content, to).commit();
        }
        invalidateOptionsMenu();
    }

    /**
     * 初始化adapter
     */
    private void initAdapter() {
        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), mFragmentList);
        mMainViewPager.setAdapter(mFragmentAdapter);
        //viewpager滑动监听
        mMainViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                changAlpha(position, positionOffset);
            }
            @Override
            public void onPageSelected(int position) {
                changAlpha(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    @OnClick({R.id.m_main_fw_btn_true, R.id.m_main_sj_btn_true, R.id.m_main_gz_btn_true, R.id.m_main_lxr_btn_true, R.id.m_main_wd_btn_true})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.m_main_fw_btn_true:
                mMainViewPager.setCurrentItem(0, false);
                break;
            case R.id.m_main_sj_btn_true:
                mMainViewPager.setCurrentItem(1, false);
                break;
            case R.id.m_main_gz_btn_true:
                mMainViewPager.setCurrentItem(2, false);
                break;
            case R.id.m_main_lxr_btn_true:
                mMainViewPager.setCurrentItem(3, false);
                break;
            case R.id.m_main_wd_btn_true:
                mMainViewPager.setCurrentItem(4, false);
                break;
        }
    }

     /**
      * 一开始运行、滑动和点击tab结束后设置tab的透明度，fragment的透明度和大小
      */
    private void changAlpha(int postion) {
        for (int i = 0; i < mButtonList.size(); i++) {
            if (i == postion) {
                mButtonList.get(i).setAlpha(1.0f);
                if(null != mFragmentList.get(i).getView()){
                    mFragmentList.get(i).getView().setAlpha(1.0f);
                    mFragmentList.get(i).getView().setScaleX(1.0f);
                    mFragmentList.get(i).getView().setScaleY(1.0f);
                }
            } else {
                mButtonList.get(i).setAlpha(0.0f);
                if(null != mFragmentList.get(i).getView()){
                    mFragmentList.get(i).getView().setAlpha(0.0f);
                    mFragmentList.get(i).getView().setScaleX(0.0f);
                    mFragmentList.get(i).getView().setScaleY(0.0f);
                }
            }
        }
    }

    /**
     * 根据滑动设置透明度
     */
    private void changAlpha(int pos, float posOffset) {
        int nextIndex = pos + 1;
        if(posOffset > 0){
            //设置tab的颜色渐变效果
            mButtonList.get(nextIndex).setAlpha(posOffset);
            mButtonList.get(pos).setAlpha(1 - posOffset);
            //设置fragment的颜色渐变效果
            mFragmentList.get(nextIndex).getView().setAlpha(posOffset);
            mFragmentList.get(pos).getView().setAlpha(1 - posOffset);
            //设置fragment滑动视图由大到小，由小到大的效果
            mFragmentList.get(nextIndex).getView().setScaleX(0.5F + posOffset/2);
            mFragmentList.get(nextIndex).getView().setScaleY(0.5F + posOffset/2);
            mFragmentList.get(pos).getView().setScaleX(1-(posOffset/2));
            mFragmentList.get(pos).getView().setScaleY(1-(posOffset/2));
        }
    }

    private PopupWindow mPopupWindow;
    private SearchViewHolder holder;
//    private LazyFragment currentFragment;
    protected Toolbar mToolbar;


//    public void setToolbar(Toolbar toolbar) {
//        if (toolbar != null) {
//            mToolbar = toolbar;
//            setSupportActionBar(toolbar);
//            getSupportActionBar().setHomeButtonEnabled(true);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//            drawer.setDrawerListener(toggle);
//            toggle.syncState();
//            mNavigationView.setNavigationItemSelectedListener(this);
//        }
//    }

    /**
     * 搜索框
     */
    public void showSearchView() {
        final WindowManager.LayoutParams lp = getWindow().getAttributes();
        if (mPopupWindow == null) {
            holder = new SearchViewHolder(this, code -> {
                KLog.e("TAG", "code "+code);

                switch (code) {
                    case SearchViewHolder.RESULT_SEARCH_EMPTY_KEYWORD:
                        KLog.e("TAG", "RESULT_SEARCH_EMPTY_KEYWORD");
//                        Snackbar.make(drawer, R.string.keyword_is_empty, Snackbar.LENGTH_SHORT).show();
                        break;
                    case SearchViewHolder.RESULT_SEARCH_SEARCH:
                        KLog.e("TAG", "RESULT_SEARCH_SEARCH");
                        String q = holder.et_search_content.getText().toString();
                        if (q.startsWith("@")) {
                            KLog.e("TAG", "RESULT_SEARCH_SEARCH if");
                            CustomTabActivityHelper.openCustomTab(
                                    this,
                                    new CustomTabsIntent.Builder()
                                            .setShowTitle(true)
                                            .setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
                                            .addDefaultShareMenuItem()
                                            .build(),
                                    Uri.parse(q.replace("@", "")));
                        } else {
                            KLog.e("TAG", "RESULT_SEARCH_SEARCH else");
                            Intent intent;
//                            if (currentFragment instanceof EBookFragment) {
//                                KLog.e("TAG", "RESULT_SEARCH_SEARCH else instanceof");
////                                intent = new Intent(this, ESearchResultActivity.class);
////                                intent.putExtra("type", 0);
//                            } else {
                                KLog.e("TAG", "RESULT_SEARCH_SEARCH else not instanceof");
                                intent = new Intent(this, SearchResultActivity.class);
//                            }
                            intent.putExtra("q", q);
                            startActivity(intent);
                        }
                        break;

                        //二维码
                   case SearchViewHolder.RESULT_SEARCH_GO_SCAN:
                       KLog.e("TAG", "RESULT_SEARCH_GO_SCAN");
                       if (PermissionUtils.requestCameraPermission(this)) {
//                            startActivity(new Intent(this, CaptureActivity.class));
                        }
                        break;

                    case SearchViewHolder.RESULT_SEARCH_CANCEL:
                        KLog.e("TAG", "RESULT_SEARCH_CANCEL");
                        mPopupWindow.dismiss();
                        break;
                }
            });
            mPopupWindow = new PopupWindow(holder.getContentView(),
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT, true);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(true);
            // 设置popWindow的显示和消失动画
//                mPopupWindow.setAnimationStyle(R.style.PopupWindowStyle);
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    holder.et_search_content.setText("");
                    KeyBoardUtils.closeKeyBord(holder.et_search_content, MainActivity.this);
                    ValueAnimator animator = ValueAnimator.ofFloat(0.7f, 1f);
                    animator.setDuration(500);
                    animator.addUpdateListener(animation -> {
                        lp.alpha = (float) animation.getAnimatedValue();
                        lp.dimAmount = (float) animation.getAnimatedValue();
                        getWindow().setAttributes(lp);
                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    });
                    animator.start();
                }
            });
            mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
        }
        KeyBoardUtils.openKeyBord(holder.et_search_content, MainActivity.this);
        ValueAnimator animator = ValueAnimator.ofFloat(1f, 0.7f);
        animator.setDuration(500);
        animator.addUpdateListener(animation -> {
            lp.alpha = (float) animation.getAnimatedValue();
            lp.dimAmount = (float) animation.getAnimatedValue();
            getWindow().setAttributes(lp);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        });
        mPopupWindow.showAtLocation(this.findViewById(R.id.toolbar), Gravity.NO_GRAVITY, 0, 16);
        animator.start();
    }


}
