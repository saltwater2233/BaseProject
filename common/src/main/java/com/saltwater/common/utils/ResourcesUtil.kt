package com.saltwater.common.utils

import android.content.Context
import androidx.core.content.ContextCompat

/**
 * <pre>
 * author : wenxin
 * e-mail : wenxin2233@outlook.com
 * time   : 2018/07/11
 * desc   : 通过id获取资源
 * version: 1.0
</pre> *
 */
object ResourcesUtil {

    fun getStringById(context: Context, id: Int): String {
        return context.resources.getString(id)
    }

    fun getColorById(context: Context, id: Int): Int {
        return ContextCompat.getColor(context,id)
    }

    fun getStringReplace(context: Context, id: Int, replaceString: String): String {
        val format = context.resources.getString(id)
        return String.format(format, replaceString)
    }

    fun getStringReplace(context: Context, id: Int, replaceString: String, replaceString2: String): String {
        val format = context.resources.getString(id)
        return String.format(format, replaceString, replaceString2)
    }

    fun getStringReplace(context: Context, id: Int, replaceInt: Int): String {
        val format = context.resources.getString(id)
        return String.format(format, replaceInt)
    }

}
