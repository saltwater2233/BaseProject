package com.saltwater.modulecommon.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build

/**
 * <pre>
 * author : wenxin
 * e-mail : wenxin2233@outlook.com
 * time   : 2018/02/26
 * desc   : 复制粘贴
 * version: 1.0
</pre> *
 */
object ClipboardUtil {

    /**
     * 为剪切板设置内容
     */
    fun setText(context: Context, text: CharSequence) {
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            val clip = ClipData.newPlainText("simple text", text)
            clipboardManager.primaryClip = clip
        } else {
            clipboardManager.text = text
        }
    }


    /**
     * 获取剪切板的内容
     */
    fun getText(context: Context): CharSequence {
        val stringBuilder = StringBuilder()
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (clipboardManager.hasPrimaryClip()) {
                return stringBuilder.toString()
            } else {
                val clipData = clipboardManager.primaryClip
                val count = clipData!!.itemCount
                for (i in 0 until count) {
                    val item = clipData.getItemAt(i)
                    val str = item.coerceToText(context)
                    stringBuilder.append(str)
                }
            }
        } else {
            stringBuilder.append(clipboardManager.text)
        }
        return stringBuilder.toString()
    }
}
