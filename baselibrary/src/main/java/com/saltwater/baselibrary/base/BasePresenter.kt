package com.saltwater.baseproject.base

import android.content.Context

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
open class BasePresenter<V>(protected var mContext: Context?) {
    private var mViewRef: Reference<V>? = null
    protected lateinit var mCompositeDisposable: CompositeDisposable


    fun attachView(view: V) {
        mViewRef = WeakReference(view)
        mCompositeDisposable = CompositeDisposable()
    }

    fun detachView() {
        if (mViewRef != null) {
            mViewRef!!.clear()
            mViewRef = null
        }

        mContext = null
        mCompositeDisposable.dispose()
    }

    fun getView(): V {
        return mViewRef?.get()!!
    }
}
