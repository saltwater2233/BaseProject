package com.saltwater.modulecommon.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import java.io.File

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade

/**
 * <pre>
 * author : wenxin
 * e-mail : wenxin2233@outlook.com
 * time   : 2018/02/26
 * desc   : 封装的Glide，以后换了直接修改这里
 * version: 1.0
</pre> *
 */

object ImageLoadUtil {

    //加载url地址
    fun loadImage(context: Context, url: String, imageView: ImageView) {
        Glide.with(context).load(url).into(imageView)
    }

    //加载bitmap
    fun loadImage(context: Context, bitmap: Bitmap, imageView: ImageView) {
        Glide.with(context).load(bitmap).into(imageView)
    }

    //加载res资源
    fun loadImage(context: Context, res: Int, imageView: ImageView) {
        Glide.with(context).load(res).into(imageView)
    }

    //加载Uri
    fun loadImage(context: Context, uri: Uri, imageView: ImageView) {
        Glide.with(context).load(uri).into(imageView)
    }

    //加载文件
    fun loadImage(context: Context, file: File, imageView: ImageView) {
        Glide.with(context).load(file).into(imageView)
    }


    //加载图片url地址，带加载中图片
    fun loadImageWithPlaceholder(context: Context, url: String, imageView: ImageView, loadingImage: Int) {
        Glide.with(context)
            .load(url)
            .apply(RequestOptions().placeholder(loadingImage))
            .into(imageView)
    }

    //加载图片url地址，带加载中图片和加载失败图片
    fun loadImageWithPlaceholder(
        context: Context,
        url: String,
        imageView: ImageView,
        loadingImage: Int,
        errorImage: Int
    ) {
        Glide.with(context)
            .load(url)
            .apply(RequestOptions().placeholder(loadingImage).error(errorImage))
            .into(imageView)
    }

    //设置加载动画
    fun loadImageViewWithAnim(context: Context, url: String, imageView: ImageView, anim: Int) {
        Glide.with(context)
            .load(url)
            .transition(withCrossFade(anim))
            .into(imageView)
    }

}
