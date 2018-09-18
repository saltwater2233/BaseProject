package com.saltwater.baseproject.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.saltwater.baseproject.MyApp;
import com.saltwater2233.baselibrary.utils.ActivityStackManager;
import com.saltwater2233.baselibrary.utils.ScreenAdaptationUtil;
import com.saltwater2233.baselibrary.utils.StatusBarUtil;
import com.saltwater2233.baselibrary.widget.LoadingDialog;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * <pre>
 *     author : wenxin
 *     e-mail : wenxin2233@outlook.com
 *     time   : 2018/09/18
 *     desc   : Activity基类
 *     version: 2.0
 * </pre>
 */
public abstract class BaseActivity<V, T extends BasePresenter<V>> extends RxAppCompatActivity {
    protected Context mContext;
    protected Unbinder mUnbinder;
    private LoadingDialog mLoadingDialog;

    protected T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView((V) this);//因为之后所有的子类都要实现对应的View接口
        }

        ActivityStackManager.getInstance().push(this);
        setContentView(getContentViewId());
        mContext = this;
        mUnbinder = ButterKnife.bind(this);

        initView();
        initData();
        initStatusBar();
    }

    //设置状态栏为白底黑字
    private void initStatusBar() {
        ScreenAdaptationUtil.setCustomDesity(this, MyApp.getInstance(), true);
        StatusBarUtil.StatusBarLightMode(this);
    }

    public void showLoading() {
        if (mLoadingDialog == null)
            mLoadingDialog = new LoadingDialog(mContext, true);
        if (!mLoadingDialog.isShowing())
            mLoadingDialog.show();
    }

    public void hideLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing())
            mLoadingDialog.dismiss();
    }

    //toolbar的返回键事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //用于创建Presenter和判断是否使用MVP模式(由子类实现)
    protected abstract T createPresenter();

    //获取显示view的xml文件ID
    protected abstract int getContentViewId();

    //初始化view
    protected abstract void initView();

    //初始化Data
    protected abstract void initData();


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        ActivityStackManager.getInstance().remove(this);
    }

}
