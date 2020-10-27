package com.saltwater.common.utils

import android.content.Context
import android.graphics.*
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore.Images
import android.text.TextUtils
import android.widget.ImageView
import android.widget.ScrollView
import androidx.core.content.FileProvider
import androidx.core.widget.NestedScrollView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.*
import java.net.URI


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
    fun getBitmap(context: Context, url: String?, listener: OnResourceListener) {
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
     * 从ScrollView获取截图
     *
     * */
    fun getBitmapByView(scrollView: ScrollView): Bitmap? {
        var h = 0
        var bitmap: Bitmap? = null
        // 获取scrollview实际高度
        for (i in 0 until scrollView.childCount) {
            h += scrollView.getChildAt(i).height
            scrollView.getChildAt(i).setBackgroundColor(
                    Color.parseColor("#ffffff"))
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(scrollView.width, h,
                Bitmap.Config.RGB_565)
        val canvas = Canvas(bitmap)
        scrollView.draw(canvas)
        return bitmap
    }

    fun getBitmapByView(scrollView: NestedScrollView): Bitmap? {
        var h = 0
        var bitmap: Bitmap? = null
        // 获取scrollview实际高度
        for (i in 0 until scrollView.childCount) {
            h += scrollView.getChildAt(i).height
            scrollView.getChildAt(i).setBackgroundColor(
                    Color.parseColor("#ffffff"))
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(scrollView.width, h,
                Bitmap.Config.RGB_565)
        val canvas = Canvas(bitmap)
        scrollView.draw(canvas)
        return bitmap
    }


    /**********************保存bitmap**************************/

    //把bitmap插入相册
    fun insertImage(context: Context, bitmap: Bitmap): Uri {
        return Uri.parse(Images.Media.insertImage(context.contentResolver, bitmap, null, null))
    }

    fun getUri(context: Context, filePath: String): Uri {
        val file = File(filePath)
        return if (Build.VERSION.SDK_INT >= 24) {
            FileProvider.getUriForFile(context.applicationContext, context.applicationContext.packageName + ".provider", file)
        } else {
            Uri.fromFile(file)
        }
    }

    //把图片保存成文件
    fun getFile(context: Context, bitmap: Bitmap, fileName: String): File? {
        val file = File(context.externalCacheDir, "$fileName.jpg")
        return try {
            val outputStream = BufferedOutputStream(FileOutputStream(file))
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            file
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun getPngImage(context: Context, bitmap: Bitmap, fileName: String): File? {
        val file = File(context.externalCacheDir, "$fileName.png")
        return try {
            val outputStream = BufferedOutputStream(FileOutputStream(file))
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            file
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }


    fun getFile(uri: Uri): File? {
        return File(URI(uri.toString()))
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

    //调整Bitmap大小
    //调整图片大小
    fun resizeBitmap(pathName: String, reqWidth: Int, reqHeight: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(pathName, options)
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        options.inJustDecodeBounds = false
        val src = BitmapFactory.decodeFile(pathName, options)
        // 如果是放大图片，filter决定是否平滑，如果是缩小图片，filter无影响，我们这里是缩小图片，所以直接设置为false
        val dst = Bitmap.createScaledBitmap(src, reqWidth, reqHeight, false)
        if (src != dst) { // 如果没有缩放，那么不回收
            src.recycle() // 释放Bitmap的native像素数组
        }

        return dst
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight = height / 2
            val halfWidth = width / 2

            while (halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth) {
                inSampleSize *= 2
            }

            var totalPixels = (width * height / inSampleSize).toLong()

            // Anything more than 2x the requested pixels we'll sample down further
            val totalReqPixelsCap = (reqWidth * reqHeight * 2).toLong()

            while (totalPixels > totalReqPixelsCap) {
                inSampleSize *= 2
                totalPixels /= 2
            }
        }
        return inSampleSize
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


    //获取ImageView的宽高和drawable的宽的差值
    fun getDrawablePaddingWidth(imageView: ImageView): Float {

        // 得到imageview中的矩阵，准备得到drawable的拉伸比率
        val m = imageView.imageMatrix
        val values = FloatArray(10)
        m.getValues(values)

        // drawable的本身宽高
        val dOriginalWidth = imageView.drawable.intrinsicWidth
        val dOriginalHeight = imageView.drawable.intrinsicHeight
        val dRatio = dOriginalWidth / dOriginalHeight//如果大于1，表示drawable宽>高

        //Image在绘制过程中的变换矩阵，从中获得x和y方向的缩放系数  value[0],[4]
        //得到drawable的实际显示时的宽高
        val dWidth = (dOriginalWidth * values[0]).toInt()
        val dHeight = (dOriginalHeight * values[4]).toInt()

        //得到imageview的宽高和drawable的宽高的差值

        val result = FloatArray(2)

        val w = imageView.width - dWidth
        val h = imageView.height - dHeight

        return (w / 2) + 0.5f
    }

    //获取ImageView的宽高和drawable的高的差值
    fun getDrawablePaddingHeight(imageView: ImageView): Float {

        // 得到imageview中的矩阵，准备得到drawable的拉伸比率
        val m = imageView.imageMatrix
        val values = FloatArray(10)
        m.getValues(values)

        // drawable的本身宽高
        val dOriginalWidth = imageView.drawable.intrinsicWidth
        val dOriginalHeight = imageView.drawable.intrinsicHeight
        val dRatio = dOriginalWidth / dOriginalHeight//如果大于1，表示drawable宽>高

        //Image在绘制过程中的变换矩阵，从中获得x和y方向的缩放系数  value[0],[4]
        //得到drawable的实际显示时的宽高
        val dWidth = (dOriginalWidth * values[0]).toInt()
        val dHeight = (dOriginalHeight * values[4]).toInt()

        //得到imageview的宽高和drawable的宽高的差值

        val result = FloatArray(2)

        val w = imageView.width - dWidth
        val h = imageView.height - dHeight

        return (h / 2) + 0.5f
    }


}