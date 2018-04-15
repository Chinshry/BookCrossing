package com.bcing.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bcing.R;
import com.bcing.adapter.MyFragmentPagerAdapter;
import com.socks.library.KLog;

import butterknife.ButterKnife;

/**
 * @author fml
 *         created at 2016/6/20 13:41
 *         description：LazyFragment使用懒加载方法，避免切换fragment的时候造成其它fragment onstart方法运行,lazyLoad方法代替onstart方法
 */
public class ThreeFragment extends LazyFragment {

    // 标志fragment是否初始化完成
    private boolean isPrepared;

    private View view;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private MyFragmentPagerAdapter myFragmentPagerAdapter;

    private TabLayout.Tab one;
    private TabLayout.Tab two;
    private TabLayout.Tab three;
    private TabLayout.Tab four;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null){
            view = inflater.inflate(R.layout.fragment_three, container, false);
            initViews(view);
            ButterKnife.bind(this, view);
            KLog.e("TAG", "threeFragment--onCreateView");
            isPrepared = true;
            lazyLoad();
        }


        return view;
    }




    private void initViews(View view) {

        //使用适配器将ViewPager与Fragment绑定在一起
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getFragmentManager());
        mViewPager.setAdapter(myFragmentPagerAdapter);

        //将TabLayout与ViewPager绑定在一起
        mTabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);

        //指定Tab的位置
        one = mTabLayout.getTabAt(0);
        two = mTabLayout.getTabAt(1);

    }


//    // 标志fragment是否初始化完成
//    private boolean isPrepared;
//    private View view;
//
//
//    private TabLayout tabLayout;
//    private ViewPager viewPager;
//
//    private List<String> list;
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
//        if(view == null){
//            KLog.e("TAG" , "threeFragment--onCreateView");
//            view = inflater.inflate(R.layout.fragment_three , container , false);
////            findView(view);
//            ButterKnife.bind(this, view);
//
//            isPrepared = true;
//            lazyLoad();
//
//            initConentView(view);
//            initData();
////            tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
////            viewPager = (ViewPager) view.findViewById(R.id.viewPager);
////            KLog.e("viewPager", "setupViewPager");
////            setupViewPager(viewPager);
//
//        }
//        return view;
//    }
//
////    //初始化view
////    private void findView(View view) {
////        KLog.e("viewPager","settab" );
////
////        tabLayout= (TabLayout) view.findViewById(R.id.tabLayout);
////
////        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.myset)));
////        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.mysearch)));
////
////    }
//
////    private void setupViewPager(ViewPager viewPager) {
////        adapter = new MyViewPagerAdapter(getSupportFragmentManager(), this);
////        adapter.addFragment(new ThreeFragmentTab1().newInstance("Page1"), "Tab 1");
////        adapter.addFragment(new ThreeFragmentTab2().newInstance("Page2"), "Tab 2");
////
////        viewPager.setAdapter(adapter);
////        tabLayout.setupWithViewPager(viewPager);
////    }
//
////    public static ThreeFragment newInstance(String text){
////        Bundle bundle = new Bundle();
////        bundle.putString("text",text);
////        ThreeFragment blankFragment = new ThreeFragment();
////        blankFragment.setArguments(bundle);
////        return  blankFragment;
////    }
//
////    @Override
////    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
////        super.onViewCreated(view, savedInstanceState);
////        TextView textView = (TextView) view.findViewById(R.id.tv_fragment);
////        textView.setText(getArguments().getString("text"));
////    }
//
//    public void initConentView(View viewContent) {
//        this.tabLayout = (TabLayout) viewContent.findViewById(R.id.tabLayout);
//        this.viewPager = (ViewPager) viewContent.findViewById(R.id.viewPager);
//    }
//
//    public void initData() {
//        //获取标签数据
//        String[] titles = getResources().getStringArray(R.array.my_cross_tab);
//
//        //创建一个viewpager的adapter
//        TabFragmentAdapter adapter = new TabFragmentAdapter(getFragmentManager(), Arrays.asList(titles));
//        this.viewPager.setAdapter(adapter);
//
//        //将TabLayout和ViewPager关联起来
//        this.tabLayout.setupWithViewPager(this.viewPager);
//    }

    @Override
    protected void lazyLoad() {
        if(!isPrepared || !isVisible) {
            return;
        }
        KLog.e("TAG" , "threeFragment--lazyLoad");
    }

}
