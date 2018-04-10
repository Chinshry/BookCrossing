package com.bcing.api;

import com.bcing.bean.http.douban.BaseResponse;

/**
 * Author   :hymanme
 * Email    :hymanme@163.com
 * Create at 2016/8/5
 * Description: 网络请求回调接口
 */
public interface ApiCompleteListener {
    void onComplected(Object result);

    void onFailed(BaseResponse msg);
}