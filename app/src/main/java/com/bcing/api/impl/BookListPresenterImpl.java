package com.bcing.api.impl;


import com.bcing.R;
import com.bcing.api.ApiCompleteListener;
import com.bcing.api.IBookListModel;
import com.bcing.api.IBookListPresenter;
import com.bcing.api.IBookListView;
import com.bcing.application.BaseApplication;
import com.bcing.bean.http.douban.BaseResponse;
import com.bcing.bean.http.douban.BookListResponse;
import com.bcing.utils.common.NetworkUtils;

/**
 * Author   :hymanme
 * Email    :hymanme@163.com
 * Create at 2016/8/5
 * Description:
 */
public class BookListPresenterImpl implements IBookListPresenter, ApiCompleteListener {
    private IBookListView mBookListView;
    private IBookListModel mBookListModel;

    public BookListPresenterImpl(IBookListView view) {
        mBookListView = view;
        mBookListModel = new BookListModelImpl();
    }

    /**
     * 加载数据
     */
    @Override
    public void loadBooks(String q, String tag, int start, int count, String fields) {
        if (!NetworkUtils.isConnected(BaseApplication.getApplication())) {
            mBookListView.showMessage(BaseApplication.getApplication().getString(R.string.poor_network));
            mBookListView.hideProgress();
            return;
        }
        mBookListView.showProgress();
        mBookListModel.loadBookList(q, tag, start, count, fields, this);
    }

    @Override
    public void cancelLoading() {
        mBookListModel.cancelLoading();
    }

    /**
     * 访问接口成功
     *
     * @param result 返回结果
     */
    @Override
    public void onComplected(Object result) {
        if (result instanceof BookListResponse) {
            int index = ((BookListResponse) result).getStart();
            if (index == 0) {
                mBookListView.refreshData(result);
            } else {
                mBookListView.addData(result);
            }
            mBookListView.hideProgress();
        }
    }

    /**
     * 请求失败
     *
     * @param msg 错误信息
     */
    @Override
    public void onFailed(BaseResponse msg) {
        mBookListView.hideProgress();
        if (msg == null) {
            return;
        }
        mBookListView.showMessage(msg.getMsg());
    }
}
