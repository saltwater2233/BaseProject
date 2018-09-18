package com.saltwater2233.baselibrary.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import com.saltwater2233.baselibrary.R;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.io.IOException;

/**
 * <pre>
 *     author : wenxin
 *     e-mail : wenxin2233@outlook.com
 *     time   : 2018/04/25
 *     desc   : 调用相机
 *     version: 1.0
 * </pre>
 */
public class ImageTakeUtil {

    private static final String TAG = "ImageTakeUtil";

    private static Uri mUri;

    public static Uri getUri() {
        return mUri;
    }


    private static Uri createUri(Context context) {
        Uri imageUri;
        //存放在SD卡根目录
        //File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), sFileName);  //照片目录
        File file = new File(Environment.getExternalStorageDirectory(), "/Pictures/" + System.currentTimeMillis() + ".jpg");
        try {
            if (file.exists()) {
                //要删除已经有的文件，不然imageUri获取不到,先删除文件，然后删除数据库
                file.delete();
                String params[] = new String[]{file.getPath()};
                context.getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.DATA + " LIKE ?", params);
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= 24) {
            //24以上需要对原始Uri进行封装
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
            imageUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        } else {
            imageUri = Uri.fromFile(file);
        }
        return imageUri;
    }

    /**
     * 进入系统拍照
     *
     * @param context
     * @return
     */
    public static Intent getTakePhotoIntent(Context context) {
        Uri uri = createUri(context);
        mUri = uri;
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE"); //照相
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        return intent;
    }

    /**
     * 从相册选取照片,原生
     *
     * @return
     */
    public static Intent getSelectGalleryPhotoIntent() {
        // 弹出系统图库
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        return intent;
    }

    /**
     * 照片剪裁-覆盖原文件
     */
    public static Intent getCropPhotoIntent(Context context, Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        intent.putExtra("scale", true);

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", false);
        intent.putExtra("noFaceDetection", true);
        mUri = createUri(context);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        return intent;
    }


    private static final String authority = "com.treeinart.funxue.MyFileProvider";

    //选择一张图片，Matisse库
    public static void selectImage(Activity activity, int requestCode) {
        Matisse.from(activity)
                .choose(MimeType.ofAll(), false) // 选择 mime 的类型
                .countable(false)//是否显示选择数量
                .maxSelectable(1) // 图片选择的最多数量
                .capture(true)
                .captureStrategy(new CaptureStrategy(false, authority))
                .theme(R.style.Matisse_Zhihu)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)//方向
                .thumbnailScale(0.85f) // 缩略图的压缩值
                .imageEngine(new GlideEngine()) // 使用的图片加载引擎
                .forResult(requestCode); // 设置作为标记的请求码
    }

    //选择多张图片，Matisse库
    public static void startSelectImage(Activity activity, int imageNumber, int requestCode) {
        Matisse.from(activity)
                .choose(MimeType.ofAll(), false) // 选择 mime 的类型
                .countable(true)//是否显示选择数量
                .maxSelectable(imageNumber) // 图片选择的最多数量
                .capture(true)
                .captureStrategy(new CaptureStrategy(false, "com.johnshen.wrongbook.MyFileProvider"))
                .theme(R.style.Matisse_Zhihu)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)//方向
                .thumbnailScale(0.85f) // 缩略图的压缩值
                .imageEngine(new GlideEngine()) // 使用的图片加载引擎
                .forResult(requestCode); // 设置作为标记的请求码
    }
}
