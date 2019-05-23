package com.saltwater.modulecommon.utils

import android.content.Context
import android.graphics.*
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.*


/**
 * <pre>
 *     author : wenxin
 *     e-mail : wenxin2233@outlook.com
 *     time   : 2019/05/08
 *     desc   : Bitmap相关
 *     version: 1.0
 * </pre>
 */
object BitmapUtil {

    private const val BITMAP_SIZE = 1000.0


    interface OnResourceListener {
        //成功
        fun onResourceReady(resource: Bitmap)
    }

    /**********************获取bitmap**************************/
    //从url获取bitmap
    fun getBitmap(context: Context, url: String, listener: OnResourceListener) {
        Glide.with(context).asBitmap().load(url).into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                listener.onResourceReady(resource)
            }
        })
    }

    //从资源文件获取bitmap
    fun getBitmap(context: Context, res: Int): Bitmap {
        return BitmapFactory.decodeResource(context.resources, res)
    }

    //从uri获取bitmap
    fun getBitmap(context: Context, uri: Uri): Bitmap? {
        var input = context.contentResolver.openInputStream(uri)

        val onlyBoundsOptions = BitmapFactory.Options()
        onlyBoundsOptions.inJustDecodeBounds = true
        onlyBoundsOptions.inDither = true//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions)
        input?.close()


        if (onlyBoundsOptions.outWidth == -1 || onlyBoundsOptions.outHeight == -1) {
            return null
        }

        val originalSize =
            if (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) onlyBoundsOptions.outHeight else onlyBoundsOptions.outWidth

        val ratio = if (originalSize > BITMAP_SIZE) originalSize / BITMAP_SIZE else 1.0
        var sampleSize = Integer.highestOneBit(Math.floor(ratio).toInt())
        if (sampleSize == 0) {
            sampleSize = 1
        }
        val bitmapOptions = BitmapFactory.Options()
        bitmapOptions.inSampleSize = sampleSize
        bitmapOptions.inDither = true //optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888//
        input = context.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions)
        input?.close()
        return bitmap
    }

    //从filepath获取bitmap
    fun getBitmap(filepath: String): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = false
        options.inPreferredConfig = Bitmap.Config.RGB_565
        options.inDither = true
        return BitmapFactory.decodeFile(filepath, options)
    }

    fun getBitmap(bytes: ByteArray, width: Int, height: Int): Bitmap? {
        val image = YuvImage(bytes, ImageFormat.NV21, width, height, null)
        val os = ByteArrayOutputStream(bytes.size)
        if (!image.compressToJpeg(Rect(0, 0, width, height), 100, os)) {
            return null
        }
        val tmp = os.toByteArray()
        return BitmapFactory.decodeByteArray(tmp, 0, tmp.size)
    }


    /**
     * 从view获取截图
     * WebView和scrollview(scrollview需要传入子view)之类的view能够截取整个长度的bitmap
     * */
    fun getBitmap(context: Context, view: View): Bitmap? {
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view.measure(measureSpec, measureSpec)

        if (view.measuredWidth <= 0 || view.measuredHeight <= 0) {
            LogUtil.e("ImageUtils.viewShot size error")
            return null
        }
        var bitmap: Bitmap
        try {
            bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        } catch (e: OutOfMemoryError) {
            LogUtil.e("ImageUtils.viewShot error")
            return null
        }

        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)//图片背景颜色
        val paint = Paint()
        val iHeight = bitmap.height

        val pfd = PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        paint.isFilterBitmap = true//针对绘制bitmap添加抗锯齿
        canvas.drawFilter = pfd//对canvas设置抗锯齿的滤镜，防止变化canvas引起画质降低
        canvas.drawBitmap(bitmap, 0f, iHeight.toFloat(), paint)
        view.draw(canvas)
        return bitmap
    }


    /**********************保存bitmap**************************/


    //把bitmap保存为Uri地址
    fun getUri(context: Context, inImage: Bitmap): Uri {
        return Uri.parse(MediaStore.Images.Media.insertImage(context.contentResolver, inImage, null, null))
    }


    //把图片保存成文件
    fun getFile(context: Context, bitmap: Bitmap, fileName: String): File? {
        val file = File(context.cacheDir, fileName)//将要保存图片的路径
        try {
            val outputStream = BufferedOutputStream(FileOutputStream(file))
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            return file
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }


    /**********************图片操作**************************/

    //旋转图片
    fun rotateBitmap(bitmap: Bitmap, angle: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle.toFloat())
        // 创建新的图片
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    //矫正照片角度,用于三星手机拍照bug
    fun correctPhoto(path: String): Bitmap {
        val degree = getRotateDegree(path)
        // 根据旋转角度，生成旋转矩阵
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())

        val bitmap = getBitmap(path)
        val rotatedBitmap: Bitmap =
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        if (bitmap != rotatedBitmap) {
            bitmap.recycle()
        }
        return rotatedBitmap
    }

    //获取照片旋转角度
    fun getRotateDegree(path: String): Int {
        var degree = 0
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            val exifInterface = ExifInterface(path)
            // 获取图片的旋转信息
            val orientation = exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )

            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return degree
    }


    //调整Bitmap大小
    fun resizeBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap {
        val w = bitmap.width
        val h = bitmap.height
        val matrix = Matrix()
        val scaleWidth = width.toFloat() / w
        val scaleHeight = height.toFloat() / h
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true)
    }

    interface CompressListener {
        // TODO 压缩开始前调用，可以在方法内启动 loading UI
        fun onStart()

        // TODO 压缩成功后调用，返回压缩后的图片文件
        fun onSuccess(file: File)

        // TODO 当压缩过程出现问题时调用
        fun onError(e: Throwable)
    }

    //压缩图片
    fun compressImage(context: Context, file: File, compressListener: CompressListener) {
        Luban.with(context)
            .load(file)
            .ignoreBy(1000)//不压缩的阈值，单位为K
            .filter { path -> !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif")) }
            .setCompressListener(object : OnCompressListener {
                override fun onStart() {
                    compressListener.onStart()
                }

                override fun onSuccess(file: File) {
                    compressListener.onSuccess(file)
                }

                override fun onError(e: Throwable) {
                    compressListener.onError(e)
                }
            })
            .launch()
    }

    fun compressImage(context: Context, uri: Uri, compressListener: CompressListener) {
        Luban.with(context)
            .load(uri)
            .ignoreBy(1000)//不压缩的阈值，单位为K
            .filter { path -> !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif")) }
            .setCompressListener(object : OnCompressListener {
                override fun onStart() {
                    compressListener.onStart()
                }

                override fun onSuccess(file: File) {
                    compressListener.onSuccess(file)
                }

                override fun onError(e: Throwable) {
                    compressListener.onError(e)
                }
            })
            .launch()
    }


}