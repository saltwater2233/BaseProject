package com.saltwater.baseproject.base;

import io.reactivex.disposables.CompositeDisposable;

/**
 * <pre>
 *     author : wenxin
 *     e-mail : wenxin2233@outlook.com
 *     time   : 2018/09/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class BaseModel {
    protected CompositeDisposable mCompositeDisposable;

    public void attachPresenter() {
        mCompositeDisposable = new CompositeDisposable();
    }

    public void detachPresenter() {
        mCompositeDisposable.dispose();
    }
}
