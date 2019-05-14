package com.saltwater.modulecommon.utils

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.fragment.app.FragmentActivity
import com.tbruyelle.rxpermissions2.RxPermissions

/**
 * <pre>
 * author : wenxin
 * e-mail : wenxin2233@outlook.com
 * time   : 2018/02/26
 * desc   : 权限请求
 * version: 1.0
</pre> *
 */

object PermissionUtil {

    interface PermissionsResultListener {
        //成功
        fun onSuccessful()

        //失败
        fun onFailure()
    }

    fun getPermission(
        fragmentActivity: FragmentActivity,
        Listener: PermissionsResultListener,
        vararg permissions: String
    ) {
        val rxPermissions = RxPermissions(fragmentActivity)
        rxPermissions.requestEachCombined(*permissions)
            .subscribe { permission ->
                if (permission.granted) {
                    Listener.onSuccessful()
                } else if (permission.shouldShowRequestPermissionRationale) {
                    Listener.onFailure()
                } else {
                    showHelpDialog(getPermissionTranslation(permission.name), fragmentActivity)
                }
            }
    }


    private fun getPermissionTranslation(permissions: String): String {
        var string = permissions.replace(Manifest.permission.READ_EXTERNAL_STORAGE, "读取存储")
        string = string.replace(Manifest.permission.WRITE_EXTERNAL_STORAGE, "读取存储")
        string = string.replace(Manifest.permission.CAMERA, "相机")
        string = string.replace(Manifest.permission.READ_PHONE_STATE, "手机状态")
        return string
    }


    private fun showHelpDialog(name: String, context: Context) {
        AlertDialog.Builder(context)
            .setTitle("帮助")
            .setMessage("当前应用缺少" + name + "权限。\n请点击\"设置\"-\"应用权限\"-打开所需权限")
            .setNegativeButton("确定") { dialog, which ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = Uri.parse("package:" + context.packageName)
                    context.startActivity(intent)
                    dialog.dismiss()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            .create()
            .show()

    }
}
