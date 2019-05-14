package com.saltwater.modulecommon.base

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.saltwater.modulecommon.R


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
    private var isAllowScreenRotate = true //是否禁止旋转屏幕
    protected lateinit var mContext: Context
    protected var mPresenter: T? = null

    private val mLoadingDialog: MaterialDialog by lazy {
        val dialog = MaterialDialog(this)
        dialog.setContentView(R.layout.dialog_loading)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return@lazy dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this

        mPresenter = createPresenter()
        mPresenter?.attachView(this as V)//因为之后所有的子类都要实现对应的View接口

        setContentView(bindLayout())

        if (!isAllowScreenRotate) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        }

        initView()
        initData()
    }

    //用于创建Presenter和判断是否使用MVP模式(由子类实现)
    protected abstract fun createPresenter(): T?

    //获取显示view的xml文件ID
    protected abstract fun bindLayout(): Int

    //初始化view
    protected abstract fun initView()

    //初始化Data
    protected abstract fun initData()


    //设置是否可旋转
    fun setScreenRotate(isAllowScreenRotate: Boolean) {
        this.isAllowScreenRotate = isAllowScreenRotate
    }


    protected fun showLoadingDialog() {
        mLoadingDialog.show()
    }

    protected fun hideLoadingDialog() {
        mLoadingDialog.hide()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.detachView()

        if (mLoadingDialog.isShowing) {
            mLoadingDialog.dismiss()
        }
    }

}