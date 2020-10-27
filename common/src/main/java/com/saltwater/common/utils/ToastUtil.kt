package com.saltwater.common.utils

import android.graphics.drawable.Drawable
import android.widget.Toast
import com.saltwater.common.BaseApplication
import es.dmoral.toasty.Toasty

/**
 * <pre>
 * author : wenxin
 * e-mail : wenxin2233@outlook.com
 * time   : 2018/02/26
 * desc   : Toast统一管理类
 * version: 1.0
</pre> *
 */

object ToastUtil {

    /***
     * 通常的Toast
     */
    fun show(message: Int) {
        Toasty.normal(BaseApplication.context, message, Toast.LENGTH_SHORT).show()
    }

    fun show(message: CharSequence) {
        Toasty.normal(BaseApplication.context, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * 错误Toast：
     */
    fun showError(message: Int) {
        Toasty.error(BaseApplication.context, message, Toast.LENGTH_SHORT).show()
    }

    fun showError(message: CharSequence) {
        Toasty.error(BaseApplication.context, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * 成功Toast：
     */
    fun showSuccess(message: Int) {
        Toasty.success(BaseApplication.context, message, Toast.LENGTH_SHORT).show()
    }

    fun showSuccess(message: CharSequence) {
        Toasty.success(BaseApplication.context, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * 信息Toast：
     */
    fun showInfo(message: Int) {
        Toasty.info(BaseApplication.context, message, Toast.LENGTH_SHORT).show()
    }

    fun showInfo(message: CharSequence) {
        Toasty.info(BaseApplication.context, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * 警告Toast：
     */
    fun showWarning(message: Int) {
        Toasty.warning(BaseApplication.context, message, Toast.LENGTH_SHORT).show()
    }

    fun showWarning(message: CharSequence) {
        Toasty.warning(BaseApplication.context, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * 带有图标的常用Toast：
     */
    fun showIcon(message: Int, icon: Drawable) {
        Toasty.normal(BaseApplication.context, message, icon).show()
    }

}
