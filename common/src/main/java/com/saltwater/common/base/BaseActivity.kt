package com.saltwater.common.base

import android.app.Dialog
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.saltwater.common.R
import com.saltwater.common.utils.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.reflect.KClass


/**
 * <pre>
 * author : wenxin
 * e-mail : wenxin2233@outlook.com
 * time   : 2018/09/18
 * desc   : Activity基类
 * version: 3.0
</pre> *
 */
abstract class BaseActivity : AppCompatActivity() {
    protected val TAG = this.javaClass.simpleName
    private var isAllowScreenRotate = true //是否禁止旋转屏幕
    protected lateinit var mContext: Context


    protected val mLoadingDialog: Dialog by lazy {
        val dialog = Dialog(this, R.style.BaseLoadingDialog)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_loading, null)
        dialog.setContentView(view)
        dialog.setCanceledOnTouchOutside(false)
        return@lazy dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityStackManager.instance.addActivity(this)
        ScreenAdaptationUtil.setDefaultScreenDensity(this)//屏幕适配
        mContext = this

        setContentView(bindLayout())
        StatusBarUtil.setLightMode(this)

        if (!isAllowScreenRotate) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        }


        initView()
        initData()

        if (isRegisteredEventBus()) {
            EventBusUtil.register(this)
        }
    }


    protected fun <BVM : BaseViewModel> createViewModel( vmClass: KClass<BVM>):BVM{
       return ViewModelProvider(this).get(vmClass.java)
    }

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

    override fun onDestroy() {
        super.onDestroy()

        if (mLoadingDialog.isShowing) {
            mLoadingDialog.dismiss()
        }

        if (isRegisteredEventBus()) {
            EventBusUtil.unregister(this)
        }

        ActivityStackManager.instance.removeActivity(this)
    }

    /**
     * 是否注册事件分发
     *
     * @return true绑定EventBus事件分发，默认不绑定，子类需要绑定的话复写此方法返回true.
     */
    protected open fun isRegisteredEventBus(): Boolean {
        return false
    }

    /**
     * 接受到分发的EventBus事件
     *
     * @param event 事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onReceiveEvent(event: EventMessage<*>) {

    }

    /**
     * 接受到分发的粘性EventBus事件
     *
     * @param event 粘性事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    open fun onReceiveStickyEvent(event: EventMessage<*>) {

    }


}