package com.saltwater.baseproject.widget

import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.saltwater.baselibrary.R

class LoadingDialog : Dialog {


    constructor(context: Context) : super(context) {}

    constructor(context: Context, themeResId: Int) : super(context, themeResId) {}

    class Builder(private val context: Context) {
        private var message: String? = null
        private var isCancelable = false
        private var isCancelOutside = false

        /**
         * 设置提示信息
         *
         * @param message
         * @return
         */

        fun setMessage(message: String): Builder {
            this.message = message
            return this
        }

        /**
         * 设置是否可以按返回键取消
         *
         * @param isCancelable
         * @return
         */

        fun setCancelable(isCancelable: Boolean): Builder {
            this.isCancelable = isCancelable
            return this
        }

        /**
         * 设置是否可以取消
         *
         * @param isCancelOutside
         * @return
         */
        fun setCancelOutside(isCancelOutside: Boolean): Builder {
            this.isCancelOutside = isCancelOutside
            return this
        }

        fun create(): LoadingDialog {

            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.dialog_loading, null)
            val loadingDialog = LoadingDialog(context, R.style.MyDialogStyle)
            val msgText = view.findViewById<View>(R.id.tipTextView) as TextView
            if (TextUtils.isEmpty(message)) {
                msgText.visibility = View.GONE
            } else {
                msgText.text = message
            }
            loadingDialog.setContentView(view)
            loadingDialog.setCancelable(isCancelable)
            loadingDialog.setCanceledOnTouchOutside(isCancelOutside)
            return loadingDialog
        }
    }
}
