package com.saltwater.baseproject.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.saltwater.baseproject.MyApp;
import com.saltwater2233.baselibrary.utils.ActivityStackManager;
import com.saltwater2233.baselibrary.utils.LeakFixUtil;

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
public abstract class BaseActivity<V, T extends BasePresenter<V>> extends AppCompatActivity {
    protected Context mContext;
    protected Unbinder mUnbinder;
    protected T mPresenter;
    private ProgressDialog mProgressDialog;

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            LeakFixUtil.fixFocusedViewLeak(MyApp.getInstance());
        }
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


    protected void showLoadingDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext, android.R.style.Theme_Material_Light_Dialog);
            //mProgressDialog.setTitle();
            mProgressDialog.setCancelable(false); // disable dismiss by tapping outside of the dialog
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    protected void hideLoadingDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
