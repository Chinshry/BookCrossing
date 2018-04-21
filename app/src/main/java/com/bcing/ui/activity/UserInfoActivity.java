package com.bcing.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcing.R;
import com.bcing.entity.MyUser;
import com.bcing.ui.BaseActivity;
import com.socks.library.KLog;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static com.kymjs.rxvolley.toolbox.RxVolleyContext.toast;

/**
 * Created by chinshry on 2018/3/14.
 * Project Name:BookCrossing
 * Package Name:com.bcing.ui.activity
 * File Name:MyWishActivity
 * Describe：Application
 */


public class UserInfoActivity extends BaseActivity {





    //圆形头像
    private ImageView iv_avator;
    private TextView tv_name;
    private TextView tv_sex;
    private TextView tv_city;

    private Button btn_chat;

    MyUser user;
    BmobIMUserInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initView();
        KLog.e("TAG","username获取了吗" + getIntent().getStringExtra("username"));

    }



    private void initView() {

        iv_avator = (ImageView) findViewById(R.id.iv_avator);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        tv_city = (TextView) findViewById(R.id.tv_city);
        btn_chat = (Button) findViewById(R.id.btn_chat);
        String name = getIntent().getStringExtra("username");
        tv_name.setText(name);


        //构造聊天方的用户信息:传入用户id、用户名和用户头像三个参数

        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
        query.addWhereEqualTo("username", name);
        query.findObjects(new FindListener<MyUser>() {
            @Override
            public void done(List<MyUser> object, BmobException e) {
                if(e==null){
                    tv_city.setText(object.get(0).getCity());
                    String objectId = object.get(0).getObjectId();
                    if (object.get(0).getSex()) {
                        tv_sex.setText("男");
                    } else {
                        tv_sex.setText("女");
                    }
                    info = new BmobIMUserInfo(objectId, name, null);

                }else{
                    toast("查找用户信息失败:" + e.getMessage());
                }
            }
        });

        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KLog.e("TAG","userid获取了吗" + info.getUserId());
                if (!(name.equals(BmobUser.getObjectByKey("username")))){
                    //                BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info,false,null);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("c", c);
                    BmobIMConversation c_info = BmobIM.getInstance().startPrivateConversation(info,false,null);
                    Bundle c = new Bundle();
                    c.putSerializable("c",c_info);

                    Intent intent = new Intent(activity, ChatActivity.class);
                    intent.putExtras(c);

                    startActivity(intent);
                }else {
                    toast("不能和自己聊天");

                }


            }
        });


    }

    @Override
    protected void initEvents() {

    }
}

