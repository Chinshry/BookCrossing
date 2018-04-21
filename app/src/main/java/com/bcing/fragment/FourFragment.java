package com.bcing.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.bcing.R;
import com.bcing.adapter.ConversationAdapter;
import com.bcing.adapter.OnRecyclerViewListener;
import com.bcing.adapter.base.IMutlipleItem;
import com.bcing.bean.Conversation;
import com.bcing.bean.PrivateConversation;
import com.bcing.utils.RefreshEvent;
import com.socks.library.KLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;

/**
 * @author fml
 * created at 2016/6/20 13:41
 * description：LazyFragment使用懒加载方法，避免切换fragment的时候造成其它fragment onstart方法运行,lazyLoad方法代替onstart方法
 */
public class FourFragment extends LazyFragment{
    // 标志fragment是否初始化完成
    private boolean isPrepared;
    private View view;

    @BindView(R.id.rc_view)
    RecyclerView rc_view;
    @BindView(R.id.sw_refresh)
    SwipeRefreshLayout sw_refresh;

    ConversationAdapter adapter;
    LinearLayoutManager layoutManager;

//    @Override
//    protected String title() {
//        return "会话";
//    }
//
//    @Override
//    public Object right() {
//        return R.drawable.base_action_bar_add_bg_selector;
//    }
//
//    @Override
//    public ParentWithNaviActivity.ToolBarListener setToolBarListener() {
//        return new ParentWithNaviActivity.ToolBarListener() {
//            @Override
//            public void clickLeft() {
//
//            }
//
//            @Override
//            public void clickRight() {
//                startActivity(SearchUserActivity.class,null);
//            }
//        };
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_four , container , false);
            ButterKnife.bind(this, view);
            KLog.e("TAG" , "fourFragment--onCreateView");
            isPrepared = true;
            lazyLoad();
//            findView(view);
//            initNaviView();
            //单一布局
            IMutlipleItem<Conversation> mutlipleItem = new IMutlipleItem<Conversation>() {
                @Override
                public int getItemViewType(int postion, Conversation c) {
                    return 0;
                }

                @Override
                public int getItemLayoutId(int viewtype) {
                    return R.layout.item_conversation;
                }

                @Override
                public int getItemCount(List<Conversation> list) {
                    return list.size();
                }
            };
            sw_refresh.setColorSchemeResources(R.color.orange);

            adapter = new ConversationAdapter(getActivity(),mutlipleItem,null);
            rc_view.setAdapter(adapter);
            layoutManager = new LinearLayoutManager(getActivity());
            rc_view.setLayoutManager(layoutManager);
            sw_refresh.setEnabled(true);
            setListener();


        }
        return view;
    }

//    //初始化view
//    private void findView(View view) {
//
//        rc_view = (RecyclerView) view.findViewById(R.id.rc_view);
//        rc_view.setOnClickListener(this);
//
//        sw_refresh = (SwipeRefreshLayout) view.findViewById(R.id.sw_refresh);
//        sw_refresh.setOnClickListener(this);
//    }

    private void setListener(){
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                sw_refresh.setRefreshing(true);
                query();
            }
        });
        sw_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });
        adapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                adapter.getItem(position).onClick(getActivity());
            }

            @Override
            public boolean onItemLongClick(int position) {
                adapter.getItem(position).onLongClick(getActivity());
                adapter.remove(position);
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        sw_refresh.setRefreshing(true);
        query();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    /**
     查询本地会话
     */
    public void query(){
//        adapter.bindDatas(BmobIM.getInstance().loadAllConversation());
        adapter.bindDatas(getConversations());
        adapter.notifyDataSetChanged();
        sw_refresh.setRefreshing(false);
    }

    /**
     * 获取会话列表的数据：增加新朋友会话
     * @return
     */
    private List<Conversation> getConversations(){
        //添加会话
        List<Conversation> conversationList = new ArrayList<>();
        conversationList.clear();
        List<BmobIMConversation> list = BmobIM.getInstance().loadAllConversation();
        if(list!=null && list.size()>0){
            for (BmobIMConversation item:list){
                switch (item.getConversationType()){
                    case 1:
                        //私聊
                        conversationList.add(new PrivateConversation(item));
                        break;
                    default:
                        break;
                }
            }
        }
        //添加新朋友会话-获取好友请求表中最新一条记录
//        List<NewFriend> friends = NewFriendManager.getInstance(getActivity()).getAllNewFriend();
//        if(friends!=null && friends.size()>0){
//            conversationList.add(new NewFriendConversation(friends.get(0)));
//        }
        //重新排序
        Collections.sort(conversationList);
        return conversationList;
    }

    /**注册自定义消息接收事件
     * @param event
     */
    @Subscribe
    public void onEventMainThread(RefreshEvent event){
        KLog.e("TAG", "会话页接收到自定义消息 ");
        //因为新增`新朋友`这种会话类型
        adapter.bindDatas(getConversations());
        adapter.notifyDataSetChanged();
    }

    /**注册离线消息接收事件
     * @param event
     */
    @Subscribe
    public void onEventMainThread(OfflineMessageEvent event){
        //重新刷新列表
        adapter.bindDatas(getConversations());
        adapter.notifyDataSetChanged();
    }

    /**注册消息接收事件
     * @param event
     * 1、与用户相关的由开发者自己维护，SDK内部只存储用户信息
     * 2、开发者获取到信息后，可调用SDK内部提供的方法更新会话
     */
    @Subscribe
    public void onEventMainThread(MessageEvent event){
        //重新获取本地消息并刷新列表
        adapter.bindDatas(getConversations());
        adapter.notifyDataSetChanged();
    }



    @Override
    protected void lazyLoad() {
        if(!isPrepared || !isVisible) {
            return;
        }
        KLog.e("TAG" , "fourFragment--lazyLoad");
    }
}
