package com.bcing.bean;

import android.content.Context;
import android.text.TextUtils;

import com.bcing.R;
import com.bcing.application.BaseApplication;
import com.bcing.entity.NewFriend;
import com.bcing.utils.Config;
import com.bcing.utils.NewFriendManager;

import static com.kymjs.rxvolley.toolbox.RxVolleyContext.toast;

//import com.bcing.BmobIMApplication;
//import com.bcing.Config;
//import com.bcing.db.NewFriend;
//import com.bcing.db.NewFriendManager;
//import com.bcing.ui.NewFriendActivity;

/**
 * 新朋友会话
 * Created by Administrator on 2016/5/25.
 */
public class NewFriendConversation extends Conversation{

    NewFriend lastFriend;

    public NewFriendConversation(NewFriend friend){
        this.lastFriend=friend;
        this.cName="新朋友";
    }

    @Override
    public String getLastMessageContent() {
        if(lastFriend!=null){
            Integer status =lastFriend.getStatus();
            String name = lastFriend.getName();
            if(TextUtils.isEmpty(name)){
                name = lastFriend.getUid();
            }
            //目前的好友请求都是别人发给我的
            if(status==null || status== Config.STATUS_VERIFY_NONE||status ==Config.STATUS_VERIFY_READED){
                return name+"请求添加好友";
            }else{
                return "我已添加"+name;
            }
        }else{
            return "";
        }
    }

    @Override
    public long getLastMessageTime() {
        if(lastFriend!=null){
            return lastFriend.getTime();
        }else{
            return 0;
        }
    }

    @Override
    public Object getAvatar() {
        return R.mipmap.new_friends_icon;
    }

    @Override
    public int getUnReadCount() {
        return NewFriendManager.getInstance(BaseApplication.INSTANCE()).getNewInvitationCount();
    }

    @Override
    public void readAllMessages() {
        //批量更新未读未认证的消息为已读状态
        NewFriendManager.getInstance(BaseApplication.INSTANCE()).updateBatchStatus();
    }

    @Override
    public void onClick(Context context) {
//        Intent intent = new Intent();
//        intent.setClass(context, NewFriendActivity.class);
//        context.startActivity(intent);

        toast("NewFriendActivity");

    }

    @Override
    public void onLongClick(Context context) {
        NewFriendManager.getInstance(context).deleteNewFriend(lastFriend);
    }
}
