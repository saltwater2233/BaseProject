package com.saltwater.common.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.saltwater.common.R
import com.saltwater.common.utils.EventBusUtil
import com.saltwater.common.utils.ScreenAdaptationUtil
import com.saltwater.common.utils.StatusBarUtil
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.reflect.KClass

/**
 * <pre>
 * author : wenxin
 * e-mail : wenxin2233@outlook.com
 * time   : 2018/07/09
 * desc   : 懒加载Fragment
 * version: 2.0
</pre> *
 */
abstract class BaseLazyFragment : Fragment() {
    protected val TAG: String = this.javaClass.simpleName
    protected var isInit = false//视图是否已经初初始化
    protected var isLoad = false



    protected val mLoadingDialog: Dialog by lazy {
        val dialog = Dialog(context!!, R.style.BaseLoadingDialog)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null)
        dialog.setContentView(view)
        dialog.setCanceledOnTouchOutside(false)
        return@lazy dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(bindLayout(), container, false)
        isInit = true
        activity?.let {
            ScreenAdaptationUtil.setDefaultScreenDensity(it)
            StatusBarUtil.setLightMode(it)
        }


        if (isRegisteredEventBus()) {
            EventBusUtil.register(this)
        }

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isLazyLoadMode()) {
            isCanLoadData()//初始化的时候去加载数据
        } else {
            lazyLoad()
        }
    }


    /**
     * 视图是否已经对用户可见，系统的方法
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isCanLoadData()
    }

    protected fun <BVM : BaseViewModel> createViewModel( vmClass: KClass<BVM>):BVM{
        return ViewModelProvider(this).get(vmClass.java)
    }

    /**
     * 是否可以加载数据
     * 可以加载数据的条件：
     * 1.视图已经初始化
     * 2.视图对用户可见
     */
    private fun isCanLoadData() {
        if (isLazyLoadMode()) {
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
    }

    /**
     * 是否为懒加载模式
     *
     */
    protected open fun isLazyLoadMode(): Boolean {
        return true
    }


    /**
     * 设置Fragment要显示的布局
     *
     * @return 布局的layoutId
     */
    protected abstract fun bindLayout(): Int


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

        if (mLoadingDialog.isShowing) {
            mLoadingDialog.dismiss()
        }
    }

    protected fun showLoadingDialog() {
        mLoadingDialog.show()
    }

    protected fun hideLoadingDialog() {
        mLoadingDialog.hide()
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