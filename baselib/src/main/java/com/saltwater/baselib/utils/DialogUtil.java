package com.saltwater.baselib.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.WindowManager;

/**
 * <pre>
 *     author : wenxin
 *     e-mail : wenxin2233@outlook.com
 *     time   : 2018/04/26
 *     desc   : 对话框工具类
 *     version: 1.0
 * </pre>
 */
public class DialogUtil extends DialogFragment {

    /**可取消
     */
    public static AlertDialog getConfirmDialog(Context context, String title, String content, String positive, String negative,
                                               DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(content)
                .setCancelable(true)
                .setPositiveButton(positive, positiveListener)
                .setNegativeButton(negative, negativeListener);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }

    /*不可取消*/
    public static AlertDialog getConfirmDialogNonCancel(Context context, String title, String content, String positive, String negative,
                                                        DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(content)
                .setCancelable(false)
                .setPositiveButton(positive, positiveListener)
                .setNegativeButton(negative, negativeListener);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }

    /**
     * 显示Loading对话框
     *
     * @param context
     */
    public static AlertDialog getLoadingDialog(Context context, int LayoutResource) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(LayoutInflater.from(context).inflate(LayoutResource, null)).setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();//先show，才能获取到大小
        setDialogParams(context,alertDialog, 130, 130);
        return alertDialog;
    }

    /**
     * 设置对话框大小,直接用dp
     *
     * @param context
     * @param dialog
     * @param width
     * @param height
     */
    public static void setDialogParams(Context context, AlertDialog dialog, int width, int height) {
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = ScreenInfoUtil.dp2px(context, width);
        params.height = ScreenInfoUtil.dp2px(context, height);
        dialog.getWindow().setAttributes(params);
    }

}
