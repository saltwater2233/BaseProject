package com.saltwater2233.baselibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.saltwater2233.baselibrary.BuildConfig;
import com.saltwater2233.baselibrary.R;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.app.TakePhotoActivity;
import org.devio.takephoto.compress.CompressConfig;
import org.devio.takephoto.model.CropOptions;
import org.devio.takephoto.model.TResult;
import org.devio.takephoto.model.TakePhotoOptions;

import java.io.File;

/**
 * 使用方法：1.创建TakePhotoUtilActivity
 * 2.再AndroidManifest中创建provider
 * 3.再res的xml里创建file_paths
 * 4.在onActivityResult里接受返回的path = data.getStringExtra(TakePhotoUtilActivity.sRESULT);
 */
public class TakePhotoUtilActivity extends TakePhotoActivity {
    private static final String sTYPE = "type";
    public static final String sRESULT = "result";
    public static final int sPHOTO = 00;//拍照
    public static final int sPHOTO_CROP = 01;//拍照并剪裁
    public static final int sGALLERY = 10;//选择图片
    public static final int sGALLERY_CROP = 11;//选择图片并剪裁
    public static final int sGALLERY_MULTI = 12;//选择多张图片


    public static void newInstance(Context context, int type, int requestCode) {
        Intent intent = new Intent(context, TakePhotoUtilActivity.class);
        intent.putExtra(sTYPE, type);
        Activity activity = (Activity) context;
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo_util);
        init(getTakePhoto());
    }


    private int limit = 1;//从画廊里选择的照片数量

    private void init(TakePhoto takePhoto) {
        File file = new File(Environment.getExternalStorageDirectory(), getPackageName() + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        Uri imageUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", file);
        configCompress(takePhoto);
        configTakePhotoOption(takePhoto);

        switch (getIntent().getIntExtra(sTYPE, sPHOTO)) {
            case sPHOTO:
                takePhoto.onPickFromCapture(imageUri);
                break;
            case sPHOTO_CROP:
                takePhoto.onPickFromCaptureWithCrop(imageUri, getCropOptions());
                break;
            case sGALLERY:
                takePhoto.onPickFromGallery();
                break;
            case sGALLERY_CROP:
                takePhoto.onPickFromGalleryWithCrop(imageUri, getCropOptions());
                break;
            case sGALLERY_MULTI:
                takePhoto.onPickMultiple(limit);
                break;

        }

    }

    /**
     * 配置图片选择的方式
     *
     * @param takePhoto
     */
    private void configTakePhotoOption(TakePhoto takePhoto) {
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        builder.setWithOwnGallery(true);//使用自带相册
        builder.setCorrectImage(true);//纠正旋转角度
        takePhoto.setTakePhotoOptions(builder.create());

    }

    /**
     * 配置压缩
     *
     * @param takePhoto
     */
    private void configCompress(TakePhoto takePhoto) {
        CompressConfig config = new CompressConfig.Builder()
                .setMaxSize(1024 * 1024)//大小不超过1M
                .setMaxPixel(800)//最大像素800
                .enableReserveRaw(true)//是否压缩
                .create();

        takePhoto.onEnableCompress(config, true);//这个trued代表显示压缩进度条

    }

    /**
     * 配置图片剪裁
     *
     * @return
     */
    private CropOptions getCropOptions() {
        CropOptions.Builder builder = new CropOptions.Builder();
        builder.setWithOwnCrop(false);//s使用第三方还是takephoto自带的裁剪工具
        return builder.create();
    }


    @Override
    public void takeCancel() {
        super.takeCancel();
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        Intent data = new Intent();
        data.putExtra(sRESULT, result.getImages().get(0).getCompressPath());//这里只需要一张图片所以只穿了一个path
        setResult(RESULT_OK, data);
        finish();
    }
}
