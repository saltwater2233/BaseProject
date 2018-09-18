package com.saltwater.baseproject.base;

import android.content.Context;
import com.saltwater2233.baselibrary.utils.LogUtil;
import com.saltwater2233.baselibrary.utils.ToastUtil;
import com.trello.rxlifecycle2.LifecycleProvider;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * <pre>
 *     author : wenxin
 *     e-mail : wenxin2233@outlook.com
 *     time   : 2018/09/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class BasePresenter<V> {
    protected Reference<V> mViewRef;
    protected Reference<LifecycleProvider> mProviderRef;
    private Context mContext;

    public BasePresenter(Context context, LifecycleProvider provider) {
        mContext = context;
        mProviderRef = new WeakReference<>(provider);
    }

    public void attachView(V view) {
        mViewRef = new WeakReference<>(view);
    }

    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
        if (mProviderRef != null) {
            mProviderRef.clear();
            mProviderRef = null;
        }
        mContext = null;
    }

    public V getView() {
        return mViewRef != null ? mViewRef.get() : null;
    }

    public LifecycleProvider getProvider() {
        return mProviderRef != null ? mProviderRef.get() : null;
    }

    public Context getContext() {
        return mContext;
    }

    //请求失败
    public void requestFail(Throwable throwable) {
        LogUtil.d(throwable.getMessage());
        ToastUtil.showShort(mContext, "请求失败 " + throwable.getMessage());
    }


}
