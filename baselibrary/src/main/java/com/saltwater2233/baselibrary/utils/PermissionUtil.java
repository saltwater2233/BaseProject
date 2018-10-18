package com.saltwater2233.baselibrary.utils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

/**
 * <pre>
 *     author : wenxin
 *     e-mail : wenxin2233@outlook.com
 *     time   : 2018/02/26
 *     desc   : 权限请求
 *     version: 1.0
 * </pre>
 */

public class PermissionUtil {


    public interface PermissionsResultListener {
        //成功
        void onSuccessful();
        //失败
        void onFailure();
    }
    /*获取多个权限，当某一个权限被拒绝，跳到onFailure()*/
    public static void getPermissionAll(final Activity activity, final PermissionsResultListener permissionsResultListener, String... permissions){
        RxPermissions rxPermission = new RxPermissions(activity);
        rxPermission.request(permissions)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            // 用户已经同意该权限
                            permissionsResultListener.onSuccessful();
                        } else{
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            permissionsResultListener.onFailure();
                        }
                    }
                });

    }
    /*获取单个权限*/
    public static void getPermissionEach(final Activity activity, final PermissionsResultListener permissionsResultListener, String... permissions) {
        RxPermissions rxPermission = new RxPermissions(activity);
        rxPermission
                .requestEach(permissions)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            permissionsResultListener.onSuccessful();
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            permissionsResultListener.onFailure();
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                            startAppSettings(activity);
                        }
                    }
                });
    }

    /*跳到权限设置*/
    private static void startAppSettings(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("请开启相应的权限");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                try {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + context.getPackageName()));
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        builder.create().show();
    }
}
