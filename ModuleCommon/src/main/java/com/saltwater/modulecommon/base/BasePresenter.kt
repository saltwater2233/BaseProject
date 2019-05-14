package com.saltwater.modulecommon.base

import java.lang.ref.Reference
import java.lang.ref.WeakReference

import io.reactivex.disposables.CompositeDisposable

/**
 * <pre>
 * author : wenxin
 * e-mail : wenxin2233@outlook.com
 * time   : 2018/09/18
 * desc   :
 * version: 1.0
</pre> *
 */
open class BasePresenter<V> {
    private var mViewRef: Reference<V>? = null

    protected lateinit var mCompositeDisposable: CompositeDisposable


    fun attachView(view: V) {
        mViewRef = WeakReference(view)
        mCompositeDisposable = CompositeDisposable()
    }

    fun detachView() {
        mViewRef?.clear()
        mCompositeDisposable.dispose()
    }

    fun getView(): V {
        return mViewRef?.get()!!
    }
}
