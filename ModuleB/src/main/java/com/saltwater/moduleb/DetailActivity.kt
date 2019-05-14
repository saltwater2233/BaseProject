package com.saltwater.moduleb

import com.alibaba.android.arouter.facade.annotation.Route
import com.saltwater.modulecommon.ARouterManager
import com.saltwater.modulecommon.base.BaseActivity

@Route(path = ARouterManager.BModuleDetailActivity)
class DetailActivity : BaseActivity<Nothing, Nothing>() {

    override fun createPresenter(): Nothing? {
        return null
    }

    override fun bindLayout(): Int {
        return R.layout.moduleb_activity_detail
    }

    override fun initView() {


    }

    override fun initData() {

    }


}
