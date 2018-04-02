package com.saltwater.baseprojectmvp.common.base;

import android.support.annotation.LayoutRes;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * <pre>
 *     author : wenxin
 *     e-mail : wenxin2233@outlook.com
 *     time   : 2018/02/24
 *     desc   : Activity基类
 *     version: 1.0
 * </pre>
 */
public abstract class BaseActivity<T> extends RxAppCompatActivity {
    private Unbinder bind;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        bind = ButterKnife.bind(this);
    }

    /*Presenter里的RxJava注册*/
    public abstract void subscribePresenter();

    /*Presenter里的RxJava取消注册*/
    public abstract void unsubscribePresenter();

    @Override
    protected void onResume() {
        super.onResume();
        subscribePresenter();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unsubscribePresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }
}
