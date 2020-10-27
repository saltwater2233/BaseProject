package com.saltwater.baseproject.ui.main

import com.saltwater.baseproject.R
import com.saltwater.common.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private val mViewMode:MainViewModel by lazy {
        createViewModel(MainViewModel::class)
    }
    override fun bindLayout(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        mViewMode.friend.observe(this,{
            tv_test.text = it.toString()
        })
    }

    override fun initData() {
        mViewMode.getData()
    }


}
