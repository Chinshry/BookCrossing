
package com.bcing.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bcing.R;
import com.bcing.entity.MyUser;
import com.bcing.ui.EditProfileAcitivity;
import com.bcing.ui.LoginActivity;
import com.bcing.ui.activity.SearchResultActivity;
import com.socks.library.KLog;

import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author fml
 *         created at 2016/6/20 13:41
 *         description：LazyFragment使用懒加载方法，避免切换fragment的时候造成其它fragment onstart方法运行,lazyLoad方法代替onstart方法
 */
public class FiveFragment extends LazyFragment implements View.OnClickListener {

    private CircleImageView img_user;
    private TextView username;
    private TextView usercity;
    private View my_wish;
    private View exit_user;
    private View edit_user;


    // 标志fragment是否初始化完成
    private boolean isPrepared;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_five, container, false);
            findView(view);
            ButterKnife.bind(this, view);
            KLog.e("TAG", "fiveFragment--onCreateView");
            isPrepared = true;
            lazyLoad();
        }

        initData();
        return view;
    }

    //初始化view
    private void findView(View view) {
        img_user = (CircleImageView) view.findViewById(R.id.img_user);
        username = (TextView) view.findViewById(R.id.username);
        usercity = (TextView) view.findViewById(R.id.usercity);

        my_wish = view.findViewById(R.id.my_wish);
        my_wish.setOnClickListener(this);
        edit_user = view.findViewById(R.id.edit_user);
        edit_user.setOnClickListener(this);
        exit_user = view.findViewById(R.id.exit_user);
        exit_user.setOnClickListener(this);

    }

    private void initData() {
        MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
        username.setText(userInfo.getUsername());
        usercity.setText(userInfo.getCity());
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        KLog.e("TAG", "fiveFragment--lazyLoad");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_wish:
//                KeyBoardUtils.closeKeyBord(et_search_content, mContext);
//                mListener.onSearch(SearchViewHolder.RESULT_SEARCH_SEARCH);
//                KLog.e("TAG", "onClick my_wish");
//                Intent intent = new Intent(mContext, SearchResultActivity.class);
//                intent.putExtra("q", et_search_content.getText().toString());
//                mContext.startActivity(intent);

//                startActivity(new Intent(getActivity(), MyWishActivity.class));

                Intent intent = new Intent(getActivity(), SearchResultActivity.class);
//                intent.putExtra("q", et_search_content.getText().toString());
                intent.putExtra("q", "SOTUS");
                startActivity(intent);

                break;
            case R.id.exit_user:
                //退出登录
                //清除缓存用户对象
                MyUser.logOut();
                // 现在的currentUser是null了
                BmobUser currentUser = MyUser.getCurrentUser();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();

                break;

            case R.id.edit_user:
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




