package com.saltwater.modulecommon.activity

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import com.saltwater.modulecommon.R
import com.saltwater.modulecommon.utils.ImageLoadUtil
import com.saltwater.modulecommon.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_picture_viewer.*

/*
* 图片预览
* 使用时给ImageView加上android:transitionName="sharedView"
* */
class PictureViewerActivity : AppCompatActivity() {

    companion object {
        private const val PICTURE = "picture"

        fun newInstance(activity: Activity, view: View, url: String) {
            val intent = Intent(activity, PictureViewerActivity::class.java)
            intent.putExtra(PICTURE, url)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val elementName = ActivityOptions.makeSceneTransitionAnimation(activity, view, "sharedView").toBundle()
                activity.startActivity(intent, elementName)
            } else {
                activity.startActivity(intent)
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture_viewer)

        StatusBarUtil.transparent(this)
        ImageLoadUtil.loadImage(this, intent.getStringExtra(PICTURE), img_viewer)
        img_viewer.setOnClickListener {
            ActivityCompat.finishAfterTransition(this)
        }
    }

}
