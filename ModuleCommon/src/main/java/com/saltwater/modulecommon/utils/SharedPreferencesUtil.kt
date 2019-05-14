package com.saltwater.modulecommon.utils

import android.content.Context
import android.content.SharedPreferences

import java.lang.reflect.Method

/**
 * <pre>
 * author : wenxin
 * e-mail : wenxin2233@outlook.com
 * time   : 2018/02/26
 * desc   : SharedPreferences封装类
 * version: 1.0
</pre> *
 */

object SharedPreferencesUtil {
    /**
     * 保存在手机里面的文件名
     */
    private const val FILE_NAME = "USER_DATA"


    /**
     * 保存数据的方法，根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param data
     */
    fun put(context: Context, key: String, data: Any) {
        val editor = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).edit()

        when (data) {
            is String -> editor.putString(key, data)
            is Int -> editor.putInt(key, data)
            is Boolean -> editor.putBoolean(key, data)
            is Float -> editor.putFloat(key, data)
            is Long -> editor.putLong(key, data)
            else -> editor.putString(key, data.toString())
        }
        SharedPreferencesUtil.SharedPreferencesCompat.apply(editor)
    }


    operator fun get(context: Context, key: String, defaultValue: String): String? {
        val sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, defaultValue)
    }

    operator fun get(context: Context, key: String, defaultValue: Int?): Int {
        val sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(key, defaultValue!!)
    }

    operator fun get(context: Context, key: String, defaultValue: Boolean?): Boolean {
        val sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(key, defaultValue!!)
    }

    operator fun get(context: Context, key: String, defaultValue: Float?): Float {
        val sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getFloat(key, defaultValue!!)
    }

    operator fun get(context: Context, key: String, defaultValue: Long?): Long {
        val sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getLong(key, defaultValue!!)
    }


    /**
     * 移除某个key值已经对应的值
     *
     * @param context
     * @param key
     */
    fun remove(context: Context, key: String) {
        val editor = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).edit()
        editor.remove(key)
        SharedPreferencesUtil.SharedPreferencesCompat.apply(editor)
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    fun clear(context: Context) {
        val editor = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).edit()
        editor.clear()
        SharedPreferencesUtil.SharedPreferencesCompat.apply(editor)
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context
     * @param key
     * @return
     */
    fun contains(context: Context, key: String): Boolean {
        val sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.contains(key)
    }

    /**
     * 返回所有的键值对
     *
     * @param context
     * @return
     */
    fun getAll(context: Context): Map<String, *> {
        val sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.all
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private object SharedPreferencesCompat {
        private val sApplyMethod =
            findApplyMethod()

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        private fun findApplyMethod(): Method? {
            try {
                val clz = SharedPreferences.Editor::class.java
                return clz.getMethod("apply")
            } catch (e: NoSuchMethodException) {
            }
            return null
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        fun apply(editor: SharedPreferences.Editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor)
                    return
                }
            } catch (e: Exception) {
                ToastUtil.showError(e.message.toString())
            }
            editor.commit()
        }
    }
}
