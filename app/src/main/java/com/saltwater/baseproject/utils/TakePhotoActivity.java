package com.saltwater.baseproject.utils;

import android.Manifest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;

import com.saltwater.baseproject.BuildConfig;
import com.saltwater.baseproject.Constants;
import com.saltwater.baseproject.base.BaseActivity;
import com.saltwater.baseproject.base.BasePresenter;
import com.saltwater.baseproject.base.Event;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;


public class TakePhotoActivity extends BaseActivity {
    private static final String sTYPE = "type";

    public static final int PHOTO = 00;//拍照
    public static final int PHOTO_CROP = 01;//拍照并剪裁

    public static final int GALLERY = 10;//选择图片
    public static final int GALLERY_CROP = 11;//选择图片并剪裁

    public static final int CROPPER = 21;//剪裁


    private String mOriginImgPath;    //原图像 路径
    private Uri mOriginImgUri;    //原图像 URI
    private String mCropImgPath;    //裁剪图像 路径
    private Uri mImgUriCrop;    //裁剪图像 URI


    public static void newInstance(Context context, int type) {
        Intent intent = new Intent(context, TakePhotoActivity.class);
        intent.putExtra(sTYPE, type);
        context.startActivity(intent);
    }


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int bindLayout() {
        return 0;
    }


    @Override
    protected void initView() {
        StatusBarUtil.hideStatusBar(this);
        ScreenAdaptationUtil.resetDensity(this, getApplication());//重设DisplayMetrics
    }

    @Override
    protected void initData() {
        PermissionUtil.getPermissionAll(this, new PermissionUtil.PermissionsResultListener() {
            @Override
            public void onSuccessful() {
                initTakePhotoOption();
            }

            @Override
            public void onFailure() {
                ToastUtil.showShort(mContext, "获取权限失败");
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
    }

    /**
     * 初始化拍照设置
     */
    private void initTakePhotoOption() {
        switch (getIntent().getIntExtra(sTYPE, PHOTO)) {
            case PHOTO:
                openCamera(PHOTO);
                break;
            case PHOTO_CROP:
                openCamera(PHOTO_CROP);
                break;
            case GALLERY:
                startGallery(GALLERY);
                break;
            case GALLERY_CROP:
                startGallery(GALLERY_CROP);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            ToastUtil.showLong(this, "拍照失败");
            sendEvent(null);
            return;
        }
        switch (requestCode) {
            case PHOTO:
                try {
                    Bitmap bitmap = rotateBitmapByDegree(mOriginImgPath);
                    File file = ImageConvertUtil.saveFileFromBitmap(mContext, bitmap, "originImage", 1);
                    sendEvent(file.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtil.showShort(mContext, e.getMessage());
                    sendEvent(null);
                }
                break;
            case PHOTO_CROP:
                doCrop(mOriginImgUri);
                break;
            case GALLERY:
                try {
                    String path = ImageConvertUtil.getFilePathFromUri(this, data.getData());
                    sendEvent(path);
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtil.showShort(mContext, e.getMessage());
                    sendEvent(null);
                }
                break;
            case GALLERY_CROP:
                doCrop(data.getData());
                break;
            case CROPPER:
                try {
                    sendEvent(mCropImgPath);
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtil.showShort(mContext, e.getMessage());
                    sendEvent(null);
                }
                break;
            default:
                break;
        }
    }


    private void openCamera(int requestCode) {
        File file = new File(getExternalCacheDir(), "originImage.jpg");
        mOriginImgPath = file.getAbsolutePath();
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            mOriginImgUri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", file);
        } else {
            mOriginImgUri = Uri.fromFile(file);
        }

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePhotoIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);//私有目录读写权限
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mOriginImgUri); //指定图片输出地址
        startActivityForResult(takePhotoIntent, requestCode); //启动照相
    }

    private void startGallery(int requestCode) {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, requestCode); // 打开相册
    }

    /**
     * 照片剪裁
     */
    private void doCrop(Uri inputUri) {
        Intent cropPhotoIntent = new Intent("com.android.camera.action.CROP");
        cropPhotoIntent.setDataAndType(inputUri, "image/*");
        // 授权应用读取 Uri，这一步要有，不然裁剪程序会崩溃
        cropPhotoIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        File file = new File(getExternalCacheDir(), "cropImage.jpg");
        mCropImgPath = file.getAbsolutePath();
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mImgUriCrop = Uri.fromFile(file);

        // 设置图片的最终输出目录
        cropPhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImgUriCrop);
        try {
            startActivityForResult(cropPhotoIntent, CROPPER);
        } catch (Exception e) {
            ToastUtil.showLong(mContext, "剪裁失败");
        }
    }

    private void sendEvent(String path) {
        Event<String> event = new Event<>(Constants.EventCode.TAKE_PHOTO, path);
        EventBus.getDefault().post(event);
        finish();
    }


    //获取图片旋转角度
    private int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    //旋转图片
    public Bitmap rotateBitmapByDegree(String path) {
        Bitmap bm = ImageConvertUtil.getBitmapFromPath(mOriginImgPath);
        Bitmap returnBm = null;
        int degree = getBitmapDegree(path);
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }


}