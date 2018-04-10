package com.bcing.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcing.R;
import com.bcing.main.MainActivity;
import com.socks.library.KLog;

import butterknife.ButterKnife;

/**
 * @author fml
 *         created at 2016/6/20 13:41
 *         description：LazyFragment使用懒加载方法，避免切换fragment的时候造成其它fragment onstart方法运行,lazyLoad方法代替onstart方法
 */
public class TwoFragment extends LazyFragment implements View.OnClickListener {
    // 标志fragment是否初始化完成
    private boolean isPrepared;
    private View view;

    private TextView tv_search;
    private ImageView iv_scan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_two, container, false);
            findView(view);
            ButterKnife.bind(this, view);
            KLog.e("TAG", "twoFragment--onCreateView");
            isPrepared = true;
            lazyLoad();
        }
        return view;
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        KLog.e("TAG", "twoFragment--lazyLoad");
    }

//    @BindView(R.id.recyclerView)
//    RecyclerView mRecyclerView;
//    @BindView(R.id.swipe_refresh_widget)
//    SwipeRefreshLayout mSwipeRefreshLayout;

//    private LinearLayoutManager mLayoutManager;
//    private DiscoverAdapter mDiscoverAdapter;
//
//    private int type;//0:douban,1:ebook
//
//    public static TwoFragment newInstance(int type) {
//
//        Bundle args = new Bundle();
////        args.putInt("type", type);
//
//        TwoFragment fragment = new TwoFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    protected void initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        mRootView = inflater.inflate(R.layout.recycler_content, null, false);
////        type = getArguments().getInt("type");
//    }

//    protected void initEvents() {
//        mSwipeRefreshLayout.setEnabled(false);
//        //设置布局管理器
//        mLayoutManager = new LinearLayoutManager(getActivity());
//        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//
//        //设置adapter
//        mDiscoverAdapter = new DiscoverAdapter(type);
//        mRecyclerView.setAdapter(mDiscoverAdapter);
//
//        //设置Item增加、移除动画
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//    }

//    class DiscoverAdapter extends RecyclerView.Adapter {
//        private static final int TYPE_SEARCH_HEADER = 0;
////        private static final int TYPE_HOT_SEARCH = 1;
//        private int type;//0:douban,1:ebook
//
//        public DiscoverAdapter(int type) {
//            this.type = type;
//        }
//
//        @Override
//        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view;
//            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_two, parent, false);
//            KLog.e("TAG", "onCreateViewHolder");
//            return new SearchHeaderHolder(view);
//
//        }
//
//        @Override
//        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
////            if (holder instanceof HotSearchHolder) {
////                //设置监听(单击和长按事件)
////                ((HotSearchHolder) holder).tagFlowLayout.setTagListener(new OnTagClickListener() {
////                    @Override
////                    public void onClick(TagFlowLayout parent, View view, int position) {
////                        Intent intent;
////                        if (type == 0) {
////                            intent = new Intent(UIUtils.getContext(), SearchResultActivity.class);
////                        } else {
////                            intent = new Intent(UIUtils.getContext(), ESearchResultActivity.class);
////                            intent.putExtra("type", 0);
////                        }
////                        intent.putExtra("q", mTags.get(position));
////                        UIUtils.startActivity(intent);
////                    }
////
////                    @Override
////                    public void onLongClick(TagFlowLayout parent, View view, int position) {
////                    }
////                });
////                TagAdapter<String> tagAdapter = new TagAdapter<String>() {
////
////                    @Override
////                    public View getView(int position, View convertView, ViewGroup parent) {
////                        //定制tag的样式，包括背景颜色，点击时背景颜色，背景形状等
////                        DefaultTagView textView = new ColorfulTagView(UIUtils.getContext());
////                        textView.setText((String) getItem(position));
////                        return textView;
////                    }
////                };
////                //设置adapter
////                ((HotSearchHolder) holder).tagFlowLayout.setTagAdapter(tagAdapter);
////
////                //给adapter绑定数据
////                tagAdapter.addAllTags(mTags);
////            }
//        }
//
//
//        @Override
//        public int getItemViewType(int position) {
//            return TYPE_SEARCH_HEADER;
//        }
//
//        @Override
//        public int getItemCount() {
//            return 2;
//        }
//    }

//    class SearchHeaderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

//        @BindView(R.id.tv_search)
//        TextView tv_search;
//        @BindView(R.id.iv_scan)
//        ImageView iv_scan;

//        public SearchHeaderHolder(View itemView) {
//            super(itemView);
//            ButterKnife.bind(this, itemView);
//            KLog.e("TAG", "SearchHeaderHolder");
//            tv_search.setOnClickListener(this);
//            iv_scan.setOnClickListener(this);
//
//
//        }


    //初始化view
    private void findView(View view) {
        tv_search = (TextView) view.findViewById(R.id.tv_search);
        tv_search.setOnClickListener(this);

        iv_scan = (ImageView) view.findViewById(R.id.iv_scan);
        iv_scan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_search) {
            ((MainActivity) getActivity()).showSearchView();
            KLog.e("TAG", "SEARCH");
        } else if (v.getId() == R.id.iv_scan) {
            //二维码
//                if (PermissionUtils.requestCameraPermission(getActivity())) {
//                    UIUtils.startActivity(new Intent(UIUtils.getContext(), CaptureActivity.class));
            KLog.e("TAG", "SCAN");
        }

    }
}
//}



