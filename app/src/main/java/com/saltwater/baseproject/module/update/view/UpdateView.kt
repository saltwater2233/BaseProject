package com.saltwater.baseproject.module.update.view

import com.saltwater.baseproject.base.BasePresenter
import com.saltwater.baseproject.base.BaseView

/**
 * <pre>
 * author : wenxin
 * e-mail : wenxin2233@outlook.com
 * time   : 2018/03/22
 * desc   :
 * version: 1.0
</pre> *
 */

interface UpdateView : BaseView {
    fun setUpdateInfo(msg: String)

    fun showToast(msg: String)
}
