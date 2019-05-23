package com.saltwater.baseproject

import android.content.Intent
import com.saltwater.modulea.ListActivity
import com.saltwater.modulecommon.base.BaseActivity

class MainActivity : BaseActivity<Nothing, Nothing>() {


    override fun createPresenter(): Nothing? {
        return null
    }

    override fun bindLayout(): Int {
        return R.layout.activity_main
    }

    override fun initView() {

         val intent = Intent(this, ListActivity::class.java)
        startActivity(intent)

    }

    override fun initData() {

    }


}
