package com.saltwater.baseproject.base;

import android.content.Context;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;

/**
 * <pre>
 *     author : wenxin
 *     e-mail : wenxin2233@outlook.com
 *     time   : 2018/09/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class BasePresenter<V, M> {
    private Reference<V> mViewRef;
    protected CompositeDisposable mCompositeDisposable;
    protected Context mContext;

    public BasePresenter(Context context) {
        mContext = context;
    }

    public void attachView(V view) {
        mViewRef = new WeakReference<>(view);
        mCompositeDisposable = new CompositeDisposable();
    }

    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }

        mContext = null;
        mCompositeDisposable.dispose();
    }

    public V getView() {
        return mViewRef != null ? mViewRef.get() : null;
    }



}
