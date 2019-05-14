package com.saltwater.modulecommon.activity

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.saltwater.modulecommon.R
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import kotlinx.android.synthetic.main.activity_video_player.*

class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var orientationUtils: OrientationUtils

    companion object {
        private const val PATH = "path"

        fun newInstance(context: Context, path: String) {
            val intent = Intent(context, VideoPlayerActivity::class.java)
            intent.putExtra(PATH, path)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_video_player)
        init()
    }


    private fun init() {
        video_player.setUp(intent.getStringExtra(PATH), true, "")

        video_player.backButton.visibility = View.VISIBLE//设置返回键
        orientationUtils = OrientationUtils(this, video_player)//设置旋转
        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        video_player.fullscreenButton.setOnClickListener { orientationUtils?.resolveByClick() }
        video_player.setIsTouchWiget(true)//是否可以滑动调整
        //设置返回按键功能
        video_player.backButton.setOnClickListener { onBackPressed() }
        video_player.startPlayLogic()
    }


    override fun onPause() {
        super.onPause()
        video_player.onVideoPause()
    }

    override fun onResume() {
        super.onResume()
        video_player.onVideoResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        GSYVideoManager.releaseAllVideos()
        orientationUtils?.releaseListener()

    }

    override fun onBackPressed() {
        //先返回正常状态
        if (orientationUtils?.screenType == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            video_player.fullscreenButton.performClick()
            return
        }
        //释放所有
        video_player.setVideoAllCallBack(null)
        super.onBackPressed()
    }
}
