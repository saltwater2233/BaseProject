package com.saltwater2233.baselibrary.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * <pre>
 *     author : wenxin
 *     e-mail : wenxin2233@outlook.com
 *     time   : 2018/04/25
 *     desc   : 调用相机
 *     version: 1.0
 * </pre>
 */
public class MediaUtil {
    private static final String TAG = "MediaUtil";
    private static final String sFileName = "image.jpg";
    private static Uri mUri;

    public MediaUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }



    private static Uri createUri(Context context){
        Uri imageUri;
        //存放在SD卡根目录
        //File file = new File(Environment.getExternalStorageDirectory(), sFileName);
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), sFileName);  //照片目录
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
        mUri = imageUri;
       return imageUri;
    }

    /**
     * 进入系统拍照
     * @param activity
     * @param requestCode
     */
    public static void takeCameraPhoto(Activity activity, int requestCode) {
        createUri(activity);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE"); //照相
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
        activity.startActivityForResult(intent, requestCode);
    }

    /**从相册选取照片
     * @param activity
     * @param requestCode
     */
    public static void takeGalleryPhoto(Activity activity, int requestCode) {
        // 弹出系统图库
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 照片剪裁-覆盖原文件
     * @param activity
     * @param requestCode
     */
    public static void cropPhoto(Activity activity, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(mUri, "image/*");
        intent.putExtra("scale", true);
        //设置宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //设置裁剪框宽高
        intent.putExtra("outputX", 340);
        intent.putExtra("outputY", 340);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
        activity.startActivityForResult(intent, requestCode);
    }




    /**
     * @param context
     * @return 通过Uri解析成Bitmap对象
     */
    public static Bitmap getBitmap(Context context) {
        Bitmap bitmap = null;
        InputStream inputStream = null;
        try {
            inputStream = context.getContentResolver().openInputStream(mUri);
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if (inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }


    /**
     * @param context
     * @return 通过Uri获取到File
     */
    public static File getFile(Context context) {
        String path = null;
        if (mUri==null){
            return null;
        }
        if ("file".equals(mUri.getScheme())) {
            path = mUri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);

                ContentResolver cr = context.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=").append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA}, buff.toString(), null, null);
                int index = 0;
                int dataIdx = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    index = cur.getInt(index);
                    dataIdx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    path = cur.getString(dataIdx);
                }
                cur.close();
                if (index == 0) {
                } else {
                    Uri u = Uri.parse("content://media/external/images/media/" + index);
                    Log.i(TAG, "temp uri is :" + u);
                }
            }
            if (path != null) {
                return new File(path);
            }
        } else if ("content".equals(mUri.getScheme())) {
            // 4.2.2以后
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(mUri, proj, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
            }
            cursor.close();
            return new File(path);
        } else {
            Log.i(TAG, "Uri Scheme:" + mUri.getScheme());
        }
        return null;
    }

    /**压缩图片
     * @param bmp
     * @param file
     */
    public static void compressBitmapToFile(Bitmap bmp, File file){
        // 尺寸压缩倍数,值越大，图片尺寸越小
        int ratio = 3;
        // 压缩Bitmap到对应尺寸
        Bitmap result = Bitmap.createBitmap(bmp.getWidth() / ratio, bmp.getHeight() / ratio, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Rect rect = new Rect(0, 0, bmp.getWidth() / ratio, bmp.getHeight() / ratio);
        canvas.drawBitmap(bmp, null, rect, null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 把压缩后的数据存放到baos中
        result.compress(Bitmap.CompressFormat.JPEG, 100 ,baos);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
