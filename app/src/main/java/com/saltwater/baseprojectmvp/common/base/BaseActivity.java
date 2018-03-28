package com.saltwater.baseprojectmvp.common.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.trello.rxlifecycle2.components.RxActivity;

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
public abstract class BaseActivity<T> extends RxActivity {
    private Unbinder bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());//设置布局给ButtterKnife使用
        bind = ButterKnife.bind(this);//初始化黄油刀控件绑定框架
    }

    /**
     * 设置布局layout
     *
     * @return
     */
    public abstract int getLayoutId();
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
