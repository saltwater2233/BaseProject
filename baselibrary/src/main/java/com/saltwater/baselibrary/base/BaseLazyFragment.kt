package com.saltwater.baseproject.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.saltwater.baseproject.widget.LoadingDialog

/**
 * <pre>
 * author : wenxin
 * e-mail : wenxin2233@outlook.com
 * time   : 2018/07/09
 * desc   : 懒加载Fragment
 * version: 2.0
</pre> *
 */
abstract class BaseLazyFragment<V, T : BasePresenter<V>> : Fragment() {
    protected val TAG = this.javaClass.simpleName
    protected lateinit var mContext: Context
    protected var mPresenter: T? = null
    protected var isInit = false//视图是否已经初初始化
    protected var isLoad = false
    private var mLoadingDialog: LoadingDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(bindLayout(), container, false)
        mContext = this!!.context!!
        isInit = true

        //判断是否使用MVP模式
        mPresenter = createPresenter()
        if (mPresenter != null) {
            mPresenter!!.attachView(this as V)//因为之后所有的子类都要实现对应的View接口
        }

        isCanLoadData()//初始化的时候去加载数据
        return view
    }


    /**
     * 视图是否已经对用户可见，系统的方法
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isCanLoadData()
    }

    /**
     * 是否可以加载数据
     * 可以加载数据的条件：
     * 1.视图已经初始化
     * 2.视图对用户可见
     */
    private fun isCanLoadData() {
        if (!isInit) {
            return
        }

        if (userVisibleHint) {
            lazyLoad()
            isLoad = true
        } else {
            if (isLoad) {
                stopLoad()
            }
        }
    }


    /**
     * 设置Fragment要显示的布局
     *
     * @return 布局的layoutId
     */
    protected abstract fun bindLayout(): Int

    //用于创建Presenter和判断是否使用MVP模式(由子类实现)
    protected abstract fun createPresenter(): T


    /**
     * 当视图初始化并且对用户可见的时候去真正的加载数据
     */
    protected abstract fun lazyLoad()

    /**
     * 当视图已经对用户不可见并且加载过数据，如果需要在切换到其他页面时停止加载数据，可以覆写此方法
     */
    protected fun stopLoad() {}


    /**
     * 视图销毁的时候讲Fragment是否初始化的状态变为false
     */
    override fun onDestroyView() {
        super.onDestroyView()
        isInit = false
        isLoad = false

        if (mPresenter != null) {
            mPresenter!!.detachView()
        }
    }

    protected fun showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = LoadingDialog.Builder(mContext)
                    .setCancelable(false)
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