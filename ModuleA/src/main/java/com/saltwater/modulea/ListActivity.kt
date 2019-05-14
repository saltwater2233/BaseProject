package com.saltwater.modulea

import com.alibaba.android.arouter.facade.annotation.Route
import com.saltwater.modulecommon.ARouterManager
import com.saltwater.modulecommon.base.BaseActivity
import kotlinx.android.synthetic.main.modulea_activity_list.*

@Route(path = ARouterManager.AModuleListActivity)
class ListActivity : BaseActivity<Nothing, Nothing>() {


    override fun createPresenter(): Nothing? {
        return null
    }

    override fun bindLayout(): Int {
        return R.layout.modulea_activity_list
    }

    override fun initView() {
        btn_to_list.setOnClickListener {
            ARouterManager.navigation(ARouterManager.BModuleDetailActivity)
        }
    }

    override fun initData() {

    }

}
