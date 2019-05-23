/*
 * Copyright (C) 2012 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.saltwater.modulecommon.camera

import android.annotation.SuppressLint
import android.hardware.Camera
import android.os.AsyncTask
import android.os.Build
import android.util.Log

import java.util.ArrayList
import java.util.concurrent.RejectedExecutionException

/**
 * Author       wildma
 * Github       https://github.com/wildma
 * Date         2018/6/24
 * Desc	        ${自动对焦，定时对焦方式实现}
 */
class AutoFocusManager(private val camera: Camera) : Camera.AutoFocusCallback {

    private val useAutoFocus: Boolean
    private var stopped: Boolean = false
    private var focusing: Boolean = false
    private var outstandingTask: AsyncTask<*, *, *>? = null

    companion object {

        private val TAG = AutoFocusManager::class.java.simpleName

        private const val AUTO_FOCUS_INTERVAL_MS = 2000L
        private val FOCUS_MODES_CALLING_AF: MutableCollection<String> = ArrayList(2)

    }

    init {
        FOCUS_MODES_CALLING_AF.add(Camera.Parameters.FOCUS_MODE_AUTO)
        FOCUS_MODES_CALLING_AF.add(Camera.Parameters.FOCUS_MODE_MACRO)
        val currentFocusMode = camera.parameters.focusMode
        useAutoFocus = FOCUS_MODES_CALLING_AF.contains(currentFocusMode)
        //  Log.i(TAG, "Current focus mode '" + currentFocusMode + "'; use auto focus? " + useAutoFocus);
        start()
    }

    @Synchronized
    override fun onAutoFocus(success: Boolean, theCamera: Camera) {
        focusing = false
        autoFocusAgainLater()
    }

    @SuppressLint("NewApi")
    @Synchronized
    private fun autoFocusAgainLater() {
        if (!stopped && outstandingTask == null) {
            val newTask = AutoFocusTask()
            try {
                if (Build.VERSION.SDK_INT >= 11) {
                    newTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
                } else {
                    newTask.execute()
                }
                outstandingTask = newTask
            } catch (ree: RejectedExecutionException) {
                Log.w(TAG, "Could not request auto focus", ree)
            }

        }
    }

    @Synchronized
    fun start() {
        if (useAutoFocus) {
            outstandingTask = null
            if (!stopped && !focusing) {
                try {
                    camera.autoFocus(this)
                    Log.w(TAG, "自动对焦")
                    focusing = true
                } catch (re: RuntimeException) {
                    // Have heard RuntimeException reported in Android 4.0.x+;
                    // continue?
                    Log.w(TAG, "Unexpected exception while focusing", re)
                    // Try again later to keep cycle going
                    autoFocusAgainLater()
                }

            }
        }
    }

    @Synchronized
    private fun cancelOutstandingTask() {
        if (outstandingTask != null) {
            if (outstandingTask!!.status != AsyncTask.Status.FINISHED) {
                outstandingTask!!.cancel(true)
            }
            outstandingTask = null
        }
    }

    @Synchronized
    fun stop() {
        stopped = true
        if (useAutoFocus) {
            cancelOutstandingTask()
            // Doesn't hurt to call this even if not focusing
            try {
                camera.cancelAutoFocus()
            } catch (re: RuntimeException) {
                // Have heard RuntimeException reported in Android 4.0.x+;
                // continue?
                Log.w(TAG, "Unexpected exception while cancelling focusing", re)
            }

        }
    }

    private inner class AutoFocusTask : AsyncTask<Any, Any, Any>() {
        override fun doInBackground(vararg voids: Any): Any? {
            try {
                Thread.sleep(AUTO_FOCUS_INTERVAL_MS)
            } catch (e: InterruptedException) {
            }

            start()
            return null
        }
    }


}
