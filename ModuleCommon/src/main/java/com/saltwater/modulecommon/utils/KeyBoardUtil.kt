package com.saltwater.modulecommon.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * <pre>
 * author : wenxin
 * e-mail : wenxin2233@outlook.com
 * time   : 2018/02/26
 * desc   : 键盘显示和隐藏
 * version: 1.0
</pre> *  *
 */
object KeyBoardUtil {

    fun openKeyboard(mEditText: EditText, mContext: Context) {
        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN)
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    fun closeKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive && activity.currentFocus != null) {
            if (activity.currentFocus!!.windowToken != null) {
                imm.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
    }
}
