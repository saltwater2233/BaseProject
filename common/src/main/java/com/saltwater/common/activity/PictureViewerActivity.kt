package com.saltwater.common.activity

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.view.View
import androidx.core.app.ActivityCompat
import com.saltwater.common.R
import com.saltwater.common.base.BaseActivity
import com.saltwater.common.utils.ImageLoadUtil
import com.saltwater.common.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_picture_viewer.*

/*
* 图片预览
* 使用时给ImageView加上android:transitionName="sharedView"
* */
class PictureViewerActivity : BaseActivity() {

    companion object {
        private const val EXTRA_PICTURE = "extra_picture"

        fun newInstance(activity: Activity, view: View, url: String) {
            val intent = Intent(activity, PictureViewerActivity::class.java)
            intent.putExtra(EXTRA_PICTURE, url)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val elementName = ActivityOptions.makeSceneTransitionAnimation(activity, view, "sharedView").toBundle()
                activity.startActivity(intent, elementName)
            } else {
                activity.startActivity(intent)
            }

        }
    }


    override fun bindLayout(): Int {
        return R.layout.activity_picture_viewer
    }

    override fun initView() {
        StatusBarUtil.transparent(this)
        intent.getStringExtra(EXTRA_PICTURE)?.let { ImageLoadUtil.loadImage(this, it, img_viewer) }
        img_viewer.setOnClickListener {
            ActivityCompat.finishAfterTransition(this)
        }
    }

    override fun initData() {

    }


}
