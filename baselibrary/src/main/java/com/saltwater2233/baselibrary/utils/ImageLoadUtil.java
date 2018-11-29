package com.saltwater2233.baselibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * <pre>
 *     author : wenxin
 *     e-mail : wenxin2233@outlook.com
 *     time   : 2018/02/26
 *     desc   : 封装的Glide，以后换了直接修改这里
 *     version: 1.0
 * </pre>
 */

public class ImageLoadUtil {

    private ImageLoadUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    //加载图片url地址
    public static void loadImage(Context mContext, String url, ImageView imageView) {
        Glide.with(mContext).load(url).into(imageView);
    }

    //加载图片url地址
    public static void loadImage(Context mContext, Bitmap bitmap, ImageView imageView) {
        //先将bitmap转换为字节
        ByteArrayOutputStream ByteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, ByteArrayOutputStream);
        byte[] bytes = ByteArrayOutputStream.toByteArray();

        Glide.with(mContext).load(bytes).into(imageView);
    }

    //加载res资源
    public static void loadImage(Context mContext, int res, ImageView imageView) {
        Glide.with(mContext).load(res).into(imageView);
    }

    //加载Uri地址图片
    public static void loadImage(Context mContext, Uri uri, ImageView imageView) {
        Glide.with(mContext).load(uri).into(imageView);
    }

    public static void loadFilePath(Context mContext, String filePath, ImageView imageView) {
        Glide.with(mContext).load(new File(filePath)).into(imageView);
    }


    //加载图片url地址，带加载中图片和加载失败图片
    public static void loadImageWithLoading(Context mContext, String url, ImageView imageView, int loadingImage, int errorImage) {
        Glide.with(mContext)
                .load(url)
                .apply(new RequestOptions().placeholder(loadingImage).error(errorImage))
                .into(imageView);
    }

    //加载res资源，带加载中图片和加载失败图片
    public static void loadImageWithLoading(Context mContext, int res, ImageView imageView, int loadingImage, int errorImage) {
        Glide.with(mContext)
                .load(res)
                .apply(new RequestOptions().placeholder(loadingImage).error(errorImage))
                .into(imageView);
    }

    /**
     *
     */
    public static void loadImageWithLoading(Context mContext, Uri uri, ImageView imageView, int loadingImage, int errorImage) {
        Glide.with(mContext)
                .load(uri)
                .apply(new RequestOptions().placeholder(loadingImage).error(errorImage))
                .into(imageView);
    }

    //设置加载动画
    public static void loadImageViewWithAnim(Context mContext, String url, int anim, ImageView imageView) {
        Glide.with(mContext)
                .load(url)
                .transition(withCrossFade(anim))
                .into(imageView);
    }
}
