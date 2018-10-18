package com.saltwater2233.baselibrary.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.saltwater2233.baselibrary.R;

import java.io.File;

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
        File file = new File(Environment.getExternalStorageDirectory(), "/funxue/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        Uri imageUri = Uri.fromFile(file);
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


   /* 获取返回的数据复制下面的代码就ojbk了

   @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case 0:
                String path = data.getStringExtra(TakePhotoUtilActivity.sRESULT);
                Glide.with(this).load(new File(path)).into(img);
                break;
        }
    }*/


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
