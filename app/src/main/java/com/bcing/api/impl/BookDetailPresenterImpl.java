package com.bcing.api.impl;

import com.bcing.R;
import com.bcing.api.ApiCompleteListener;
import com.bcing.api.IBookDetailModel;
import com.bcing.api.IBookDetailPresenter;
import com.bcing.api.IBookDetailView;
import com.bcing.bean.http.douban.BaseResponse;
import com.bcing.utils.common.NetworkUtils;
import com.bcing.utils.common.UIUtils;

/**
 * Author   :hymanme
 * Email    :hymanme@163.com
 * Create at 2016/2/23 0023
 * Description:
 */
public class BookDetailPresenterImpl implements IBookDetailPresenter, ApiCompleteListener {
    private IBookDetailView mBookDetailView;
    private IBookDetailModel mBookDetailModel;

    public BookDetailPresenterImpl(IBookDetailView view) {
        mBookDetailView = view;
        mBookDetailModel = new BookDetailModelImpl();
    }

//    @Override
//    public void loadReviews(String bookId, int start, int count, String fields) {
//        if (!NetworkUtils.isConnected(UIUtils.getContext())) {
//            mBookDetailView.showMessage(UIUtils.getContext().getString(R.string.poor_network));
//            mBookDetailView.hideProgress();
//        }
//        mBookDetailView.showProgress();
//        mBookDetailModel.loadReviewsList(bookId, start, count, fields, this);
//    }

    @Override
    public void loadSeries(String SeriesId, int start, int count, String fields) {
        if (!NetworkUtils.isConnected(UIUtils.getContext())) {
            mBookDetailView.showMessage(UIUtils.getContext().getString(R.string.poor_network));
//            mBookDetailView.hideProgress();
        }
//        mBookDetailView.showProgress();
        mBookDetailModel.loadSeriesList(SeriesId, start, count, fields, this);
    }

    @Override
    public void cancelLoading() {
        mBookDetailModel.cancelLoading();
    }

    /**
     * 访问接口成功
     *
     * @param result
     */
    @Override
    public void onComplected(Object result) {
//        mBookDetailView.updateView(result);
        mBookDetailView.hideProgress();
    }

    /**
     * 取消正在加载的http请求
     */
    @Override
    public void onFailed(BaseResponse msg) {
        mBookDetailView.hideProgress();
        if (msg == null) {
            return;
        }
        mBookDetailView.showMessage(msg.getMsg());
    }
}
