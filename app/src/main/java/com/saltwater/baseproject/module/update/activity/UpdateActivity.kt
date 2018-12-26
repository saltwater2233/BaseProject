package com.saltwater.baseproject.module.update.activity

import android.content.Intent
import com.saltwater.baselibrary.utils.ToastUtil
import com.saltwater.baseproject.R
import com.saltwater.baseproject.base.BaseActivity
import com.saltwater.baseproject.module.update.presenter.UpdatePresenter
import com.saltwater.baseproject.module.update.view.UpdateView
import kotlinx.android.synthetic.main.activity_main.*

class UpdateActivity : BaseActivity<UpdateView, UpdatePresenter>(), UpdateView {

    override fun bindLayout(): Int {
        return R.layout.activity_main
    }

    override fun createPresenter(): UpdatePresenter {
        return UpdatePresenter(this)
    }

    override fun initView() {
        hideLoadingDialog()
        btn_location.setOnClickListener {
            showLoadingDialog()
            mPresenter!!.loadUpdate()
        }
        tv_location.setOnClickListener {
            startActivity(Intent(mContext, UpdateActivity::class.java))
            finish()
        }
    }

    override fun initData() {

    }

    override fun setUpdateInfo(info: String) {
        hideLoadingDialog()
        ToastUtil.showShort(mContext, info)
        tv_location.text = info
    }

    override fun showToast(msg: String) {
        ToastUtil.showShort(mContext, msg)
    }

}
