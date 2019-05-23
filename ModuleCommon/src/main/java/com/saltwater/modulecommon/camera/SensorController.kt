package com.saltwater.modulecommon.camera

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

import java.util.Calendar

/**
 * Author       wildma
 * Github       https://github.com/wildma
 * Date         2018/6/24
 * Desc	        ${加速度控制器，用来控制对焦}
 */
class SensorController private constructor(context: Context) : SensorEventListener {


    private val mSensorManager: SensorManager by lazy {
        context.getSystemService(Activity.SENSOR_SERVICE) as SensorManager
    }
    private val mSensor: Sensor by lazy {
        mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    private var mX: Int = 0
    private var mY: Int = 0
    private var mZ: Int = 0
    private var lastStaticStamp: Long = 0
    private lateinit var mCalendar: Calendar
    private var foucsing = 1  //1 表示没有被锁定 0表示被锁定

    private var isFocusing = false
    private var canFocusIn = false  //内部是否能够对焦控制机制
    private var canFocus = false
    private var STATUE = STATUS_NONE


    companion object {
        private const val TAG = "SensorController"
        private const val DELAY_DURATION = 500
        private const val STATUS_NONE = 0
        private const val STATUS_STATIC = 1
        private const val STATUS_MOVE = 2

        fun getInstance(context: Context): SensorController {
            return SensorController(context)
        }
    }


    /**
     * 对焦是否被锁定
     *
     * @return
     */
    fun isFocusLocked(): Boolean {
        if (canFocus) {
            return foucsing <= 0
        }
        return false
    }


    private var mCameraFocusListener: CameraFocusListener? = null


    fun onStart() {
        restParams()
        canFocus = true
        mSensorManager.registerListener(
            this, mSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    fun onStop() {
        mSensorManager.unregisterListener(this, mSensor)
        canFocus = false
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor == null) {
            return
        }

        if (isFocusing) {
            restParams()
            return
        }

        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0].toInt()
            val y = event.values[1].toInt()
            val z = event.values[2].toInt()
            mCalendar = Calendar.getInstance()
            val stamp = mCalendar.timeInMillis// 1393844912

            val second = mCalendar.get(Calendar.SECOND)// 53

            if (STATUE != STATUS_NONE) {
                val px = Math.abs(mX - x)
                val py = Math.abs(mY - y)
                val pz = Math.abs(mZ - z)
                //                Log.d(TAG, "pX:" + px + "  pY:" + py + "  pZ:" + pz + "    stamp:"
                //                        + stamp + "  second:" + second);
                val value = Math.sqrt((px * px + py * py + pz * pz).toDouble())
                if (value > 1.4) {
                    //                    textviewF.setText("检测手机在移动..");
                    //                    Log.i(TAG,"mobile moving");
                    STATUE = STATUS_MOVE
                } else {
                    //                    textviewF.setText("检测手机静止..");
                    //                    Log.i(TAG,"mobile static");
                    //上一次状态是move，记录静态时间点
                    if (STATUE == STATUS_MOVE) {
                        lastStaticStamp = stamp
                        canFocusIn = true
                    }

                    if (canFocusIn) {
                        if (stamp - lastStaticStamp > DELAY_DURATION) {
                            //移动后静止一段时间，可以发生对焦行为
                            if (!isFocusing) {
                                canFocusIn = false
                                //                                onCameraFocus();
                                if (mCameraFocusListener != null) {
                                    mCameraFocusListener!!.onFocus()
                                }
                                //                                Log.i(TAG,"mobile focusing");
                            }
                        }
                    }

                    STATUE = STATUS_STATIC
                }
            } else {
                lastStaticStamp = stamp
                STATUE = STATUS_STATIC
            }

            mX = x
            mY = y
            mZ = z
        }
    }

    /**
     * 重置参数
     */
    private fun restParams() {
        STATUE = STATUS_NONE
        canFocusIn = false
        mX = 0
        mY = 0
        mZ = 0
    }

    /**
     * 锁定对焦
     */
    fun lockFocus() {
        isFocusing = true
        foucsing--
        Log.i(TAG, "lockFocus")
    }

    /**
     * 解锁对焦
     */
    fun unlockFocus() {
        isFocusing = false
        foucsing++
        Log.i(TAG, "unlockFocus")
    }

    fun restFocus() {
        foucsing = 1
    }

    interface CameraFocusListener {
        fun onFocus()
    }

    fun setCameraFocusListener(mCameraFocusListener: CameraFocusListener) {
        this.mCameraFocusListener = mCameraFocusListener
    }


}
