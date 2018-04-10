
package com.bcing.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bcing.R;
import com.bcing.entity.MyUser;
import com.bcing.ui.EditProfileAcitivity;
import com.bcing.ui.LoginActivity;
import com.socks.library.KLog;

import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;

/**
 * @author fml
 * created at 2016/6/20 13:41
 * description：LazyFragment使用懒加载方法，避免切换fragment的时候造成其它fragment onstart方法运行,lazyLoad方法代替onstart方法
 */
public class FiveFragment extends LazyFragment implements View.OnClickListener {


    private Button btn_exit_user;
    private Button btn_edit_user;


    // 标志fragment是否初始化完成
    private boolean isPrepared;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_five , container , false);
            findView(view);
            ButterKnife.bind(this, view);
            KLog.e("TAG" , "fiveFragment--onCreateView");
            isPrepared = true;
            lazyLoad();
        }
        return view;
    }

    //初始化view
    private void findView(View view) {
        btn_exit_user = (Button) view.findViewById(R.id.btn_exit_user);
        btn_exit_user.setOnClickListener(this);

        btn_edit_user = (Button) view.findViewById(R.id.btn_edit_user);
        btn_edit_user.setOnClickListener(this);
    }



    @Override
    protected void lazyLoad() {
        if(!isPrepared || !isVisible) {
            return;
        }
        KLog.e("TAG" , "fiveFragment--lazyLoad");
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_exit_user:
                //退出登录
                //清除缓存用户对象
                MyUser.logOut();
                // 现在的currentUser是null了
                BmobUser currentUser = MyUser.getCurrentUser();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();

                break;

            case R.id.btn_edit_user:
                //编辑资料
                startActivity(new Intent(getActivity(), EditProfileAcitivity.class));

                break;
        }
    }


//    @Override
//    protected void initEvents() {
//
//    }

//    @Override
//    protected void initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//    }
}




