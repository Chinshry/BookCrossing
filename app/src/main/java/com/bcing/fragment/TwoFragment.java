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



