package com.saltwater.baseproject.base

import android.app.ProgressDialog
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.saltwater.baseproject.widget.LoadingDialog


/**
 * <pre>
 * author : wenxin
 * e-mail : wenxin2233@outlook.com
 * time   : 2018/09/18
 * desc   : Activity基类
 * version: 3.0
</pre> *
 */
abstract class BaseActivity<V, T : BasePresenter<V>> : AppCompatActivity() {
    protected val TAG = this.javaClass.simpleName
    private var isAllowScreenRotate = true//是否禁止旋转屏幕
    protected lateinit var mContext: Context
    protected var mPresenter: T? = null
    private var mLoadingDialog: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = createPresenter()
        if (mPresenter != null) {
            mPresenter!!.attachView(this as V)//因为之后所有的子类都要实现对应的View接口
        }


        setContentView(bindLayout())

        if (!isAllowScreenRotate) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        }

        mContext = this

        initView()
        initData()
    }


    //用于创建Presenter和判断是否使用MVP模式(由子类实现)
    protected abstract fun createPresenter(): T

    //获取显示view的xml文件ID
    protected abstract fun bindLayout(): Int

    //初始化view
    protected abstract fun initView()

    //初始化Data
    protected abstract fun initData()

    override fun onDestroy() {
        super.onDestroy()

        if (mPresenter != null) {
            mPresenter!!.detachView()
        }

    }


    //设置是否可旋转
    fun setScreenRotate(isAllowScreenRotate: Boolean) {
        this.isAllowScreenRotate = isAllowScreenRotate
    }


    protected fun showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = LoadingDialog.Builder(this)
                    .setCancelable(true)
                    .create()
        }
        mLoadingDialog!!.show()
    }

    protected fun hideLoadingDialog() {
        if (mLoadingDialog?.isShowing == true) {
            mLoadingDialog!!.dismiss()
        }
    }
}