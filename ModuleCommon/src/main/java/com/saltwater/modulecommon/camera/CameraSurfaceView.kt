package com.saltwater.modulecommon.camera

import android.content.Context
import android.content.res.Configuration
import android.hardware.Camera
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView

/**
 * Author       wildma
 * Github       https://github.com/wildma
 * Date         2018/6/24
 * Desc	        ${相机预览}
 */
class CameraSurfaceView : SurfaceView, SurfaceHolder.Callback {
    private val TAG = CameraSurfaceView::class.java.name

    val camera: Camera by lazy {
        Camera.open()
    }
    private val mAutoFocusManager: AutoFocusManager by lazy {
        AutoFocusManager(camera)
    }

    private val mSensorController: SensorController by lazy {
        SensorController.getInstance(context)
    }
    private val mSurfaceHolder: SurfaceHolder by lazy {
        holder
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }


    private fun init(context: Context) {
        mSurfaceHolder.addCallback(this)
        mSurfaceHolder.setKeepScreenOn(true)
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)

        mSensorController.setCameraFocusListener(object : SensorController.CameraFocusListener {
            override fun onFocus() {
                focus()
            }
        })


    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        try {
            camera.setPreviewDisplay(holder)

            val parameters = camera.parameters
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                //竖屏拍照时，需要设置旋转90度，否者看到的相机预览方向和界面方向不相同
                camera.setDisplayOrientation(90)
                parameters.setRotation(90)
            } else {
                camera.setDisplayOrientation(0)
                parameters.setRotation(0)
            }
            /*
            List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();//获取所有支持的预览大小
            Camera.Size bestSize = getOptimalPreviewSize(sizeList, ScreenInfoUtil.getScreenWidth(mContext), ScreenInfoUtil.getScreenHeight(mContext));
            parameters.setPreviewSize(bestSize.width, bestSize.height);//设置预览大小*/

            camera.parameters = parameters
            camera.startPreview()
            focus()//首次对焦
        } catch (e: Exception) {
            Log.d(TAG, "Error setting camera preview: " + e.message)
            try {
                val parameters = camera.parameters
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    //竖屏拍照时，需要设置旋转90度，否者看到的相机预览方向和界面方向不相同
                    camera.setDisplayOrientation(90)
                    parameters.setRotation(90)
                } else {
                    camera.setDisplayOrientation(0)
                    parameters.setRotation(0)
                }
                camera.parameters = parameters
                camera.startPreview()
                focus()//首次对焦
            } catch (e1: Exception) {
                e.printStackTrace()
            }

        }
    }

    /**
     * 获取最佳预览大小
     *
     * @param sizes 所有支持的预览大小
     * @param w     SurfaceView宽
     * @param h     SurfaceView高
     * @return
     */
    private fun getOptimalPreviewSize(sizes: List<Camera.Size>?, w: Int, h: Int): Camera.Size? {
        val ASPECT_TOLERANCE = 0.1
        val targetRatio = w.toDouble() / h
        if (sizes == null)
            return null

        var optimalSize: Camera.Size? = null
        var minDiff = java.lang.Double.MAX_VALUE

// Try to find an size match aspect ratio and size
        for (size in sizes) {
            val ratio = size.width.toDouble() / size.height
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue
            if (Math.abs(size.height - h) < minDiff) {
                optimalSize = size
                minDiff = Math.abs(size.height - h).toDouble()
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = java.lang.Double.MAX_VALUE
            for (size in sizes) {
                if (Math.abs(size.height - h) < minDiff) {
                    optimalSize = size
                    minDiff = Math.abs(size.height - h).toDouble()
                }
            }
        }
        return optimalSize
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, w: Int, h: Int) {
        //因为设置了固定屏幕方向，所以在实际使用中不会触发这个方法
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        holder.removeCallback(this)
        //回收释放资源
        release()
    }

    /**
     * 释放资源
     */
    private fun release() {
        camera.setPreviewCallback(null)
        camera.stopPreview()
        camera.release()

        mAutoFocusManager.stop()
    }

    /**
     * 对焦，在CameraActivity中触摸对焦或者自动对焦
     */
    fun focus() {
        try {
            camera.autoFocus(null)
        } catch (e: Exception) {
            Log.d(TAG, "takePhoto $e")
        }
    }

    /**
     * 开关闪光灯
     *
     * @return 闪光灯是否开启
     */
    fun switchFlashLight(): Boolean {
        val parameters = camera.parameters
        if (parameters.flashMode == Camera.Parameters.FLASH_MODE_OFF) {
            parameters.flashMode = Camera.Parameters.FLASH_MODE_TORCH
            camera.parameters = parameters
            return true
        } else {
            parameters.flashMode = Camera.Parameters.FLASH_MODE_OFF
            camera.parameters = parameters
            return false
        }
    }


    fun startPreview() {
        camera.startPreview()
    }

    fun onStart() {
        mSensorController.onStart()
    }

    fun onStop() {
        mSensorController.onStop()
    }

    fun addCallback() {
        mSurfaceHolder.addCallback(this)
    }

}
