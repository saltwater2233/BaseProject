package com.saltwater.baseproject.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.saltwater.baseproject.MyApp;
import com.saltwater.baseproject.utils.ActivityStackManager;
import com.saltwater.baseproject.utils.LeakFixUtil;
import com.saltwater.baseproject.utils.ScreenAdaptationUtil;

import org.greenrobot.eventbus.ThreadMode;

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
    private boolean isAllowScreenRotate = false;//是否禁止旋转屏幕
    protected final String TAG = this.getClass().getSimpleName();

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

        setContentView(bindLayout());

        if (!isAllowScreenRotate) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        mContext = this;
        mUnbinder = ButterKnife.bind(this);

        ScreenAdaptationUtil.setCustomDesity(this, getApplication(), false);

        initView();
        initData();
    }


    //用于创建Presenter和判断是否使用MVP模式(由子类实现)
    protected abstract T createPresenter();

    //获取显示view的xml文件ID
    protected abstract int bindLayout();

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


    //设置是否可旋转
    public void setScreenRotate(boolean isAllowScreenRotate) {
        this.isAllowScreenRotate = isAllowScreenRotate;
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
