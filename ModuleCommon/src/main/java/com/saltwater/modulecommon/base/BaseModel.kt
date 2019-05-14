package com.saltwater.modulecommon.base

import io.reactivex.disposables.CompositeDisposable

/**
 * <pre>
 * author : wenxin
 * e-mail : wenxin2233@outlook.com
 * time   : 2018/09/26
 * desc   :
 * version: 1.0
</pre> *
 */
class BaseModel {
    protected lateinit var mCompositeDisposable: CompositeDisposable

    fun attachPresenter() {
        mCompositeDisposable = CompositeDisposable()
    }

    fun detachPresenter() {
        mCompositeDisposable.dispose()
    }
}
