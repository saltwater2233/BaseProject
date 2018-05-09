package com.saltwater.baselib.utils;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * <pre>
 *     author : wenxin
 *     e-mail : wenxin2233@outlook.com
 *     time   : 2018/02/26
 *     desc   : 封装的Glide，以后换了直接修改这里
 *     version: 1.0
 * </pre>
 */

public class ImgLoadUtil {
    private ImgLoadUtil()
    {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }
    //加载图片url地址
    public static void loadImage(Context mContext, String url, ImageView mImageView) {
        Glide.with(mContext).load(url).into(mImageView);
    }

    //加载res资源
    public static void loadImage(Context mContext, int res, ImageView mImageView) {
        Glide.with(mContext).load(res).into(mImageView);
    }

    //加载图片url地址，带加载中图片和加载失败图片
    public static void loadImageWithLoading(Context mContext, String url, ImageView mImageView, int loadingImage, int errorImage) {
        Glide.with(mContext).load(url).placeholder(loadingImage).error(errorImage).into(mImageView);
    }

    //加载res资源，带加载中图片和加载失败图片
    public static void loadImageWithLoading(Context mContext, int res, ImageView mImageView, int loadingImage, int errorImage) {
        Glide.with(mContext).load(res).placeholder(loadingImage).error(errorImage).into(mImageView);
    }


    //设置加载动画
    public static void loadImageViewWithAnim(Context mContext, String url, int anim, ImageView mImageView) {
        Glide.with(mContext).load(url).animate(anim).into(mImageView);
    }

}
