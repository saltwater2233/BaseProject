package com.saltwater.common.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import android.webkit.MimeTypeMap
import java.util.ArrayList


/**
 * <pre>
 *     author : wenxin
 *     e-mail : wenxin2233@outlook.com
 *     time   : 2019/05/07
 *     desc   : 分享工具类
 *     version: 1.0
 * </pre>
 */
object ShareUtil {


    /**
     * 启动隐式Activity,会先判断是否存在响应目标的Activity
     *
     * @return
     */
    private fun startTargetActivity(context: Context, intent: Intent) {
        if (intent.resolveActivity(context.packageManager) == null) {
            ToastUtil.show("没有找到符合要求的应用")
        } else {
            context.startActivity(intent)
        }
    }

    fun shareUrl(context: Context, uriString: String) {
        val uri = Uri.parse(uriString)
        val intent = Intent()
        intent.action = "android.intent.action.VIEW"
        intent.data = uri
        startTargetActivity(context, intent)
    }


    fun shareMsg(context: Context, msgText: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain" // 纯文本
        intent.putExtra(Intent.EXTRA_TEXT, msgText)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun shareImage(context: Context, uri: Uri) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND//设置分享行为
        intent.type = "image/*"//设置分享内容的类型
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        context.startActivity(Intent.createChooser(intent, "分享"))
    }


    /**
     * 分享多张照片
     *
     * @param context
     * @param list    ArrayList＜ImageUri＞
     */
    fun sendMultiple(context: Context, list: ArrayList<out Parcelable>) {
        val intent = Intent(Intent.ACTION_SEND_MULTIPLE)
        intent.type = "image/*"
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, list)
        intent.putExtra(Intent.EXTRA_SUBJECT, "")
        intent.putExtra(Intent.EXTRA_TEXT, "")
        intent.putExtra(Intent.EXTRA_TITLE, "")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }


    /**
     * 分享多文件
     *
     * @param context
     * @param uri
     */
    fun shareFile(context: Context, uri: Uri) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra("subject", "") //
        intent.putExtra("body", "") // 正文
        intent.putExtra(Intent.EXTRA_STREAM, uri) // 添加附件，附件为file对象
        if (uri.toString().endsWith(".gz")) {
            intent.type = "application/x-gzip" // 如果是gz使用gzip的mime
        } else if (uri.toString().endsWith(".txt")) {
            intent.type = "text/plain" // 纯文本则用text/plain的mime
        } else {
            intent.type = "application/octet-stream" // 其他的均使用流当做二进制数据来发送
        }
        context.startActivity(intent) // 调用系统的mail客户端进行发送
    }


    /**
     * 分享多文件
     *
     * @param context
     * @param uris
     */
    fun shareMultipleFiles(context: Context, uris: ArrayList<Uri>) {

        val multiple = uris.size > 1
        val intent = Intent(
                if (multiple)
                    Intent.ACTION_SEND_MULTIPLE
                else
                    Intent.ACTION_SEND)

        if (multiple) {
            intent.type = "*/*"
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
        } else {
            val value = uris[0]
            val ext = MimeTypeMap.getFileExtensionFromUrl(value.toString())
            var mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext)
            if (mimeType == null) {
                mimeType = "*/*"
            }
            intent.type = mimeType
            intent.putExtra(Intent.EXTRA_STREAM, value)
        }
        context.startActivity(Intent.createChooser(intent, "Share"))
    }


}