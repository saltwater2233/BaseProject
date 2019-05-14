package com.saltwater.modulecommon.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.saltwater.modulecommon.R
import com.saltwater.modulecommon.camera.CameraUtil
import com.saltwater.modulecommon.utils.BitmapUtil
import com.saltwater.modulecommon.utils.PermissionUtil
import com.saltwater.modulecommon.utils.StatusBarUtil
import com.saltwater.modulecommon.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_camera.*

/*
* 拍照
* */
class CameraActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_RESULT = "extra_result"

        fun newInstance(activity: FragmentActivity, requestCode: Int) {
            PermissionUtil.getPermission(
                activity,
                object : PermissionUtil.PermissionsResultListener {
                    override fun onSuccessful() {
                        val intent = Intent(activity, CameraActivity::class.java)
                        activity.startActivityForResult(intent, requestCode)
                    }

                    override fun onFailure() {
                        ToastUtil.show(R.string.toast_Permission_failure)
                    }
                },
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        initView()
    }

    private fun initView() {
        StatusBarUtil.hide(this)

        iv_camera_take.setOnClickListener {
            getPhoto()
        }
        iv_camera_close.setOnClickListener {
            finish()
        }
        iv_camera_flash.setOnClickListener {
            val isFlashOn = camera_surfaceView.switchFlashLight()
            iv_camera_flash.setImageResource(if (isFlashOn) R.mipmap.ic_common_camera_flash_off else R.mipmap.ic_common_camera_flash_on)
        }
        camera_surfaceView.setOnClickListener {
            camera_surfaceView.focus()
        }
    }


    override fun onStart() {
        super.onStart()
        if (camera_surfaceView != null) {
            startSurfaceView()
        }
    }

    override fun onStop() {
        super.onStop()
        if (camera_surfaceView != null) {
            camera_surfaceView.onStop()
        }
    }

    private fun startSurfaceView() {
        camera_surfaceView.isEnabled = true
        camera_surfaceView.addCallback()
        camera_surfaceView.startPreview()
        camera_surfaceView.onStart()
    }

    private fun getPhoto() {
        camera_surfaceView.isEnabled = false
        camera_surfaceView.onStop()
        CameraUtil.getCamera().setOneShotPreviewCallback { bytes, camera ->
            val size = camera.parameters.previewSize //获取预览大小
            camera.stopPreview()
            Thread {

                val bitmap = BitmapUtil.getBitmap(bytes, size.width, size.height)
                val info = android.hardware.Camera.CameraInfo()
                android.hardware.Camera.getCameraInfo(0, info)
                val rotatedBitmap = BitmapUtil.rotateBitmap(bitmap!!, info.orientation)
                val uri = BitmapUtil.getUri(this, rotatedBitmap).toString()
                setResult(Activity.RESULT_OK, Intent().putExtra(EXTRA_RESULT, uri))
                finish()
            }.start()
        }
    }
}
