package com.saltwater.modulecommon.utils

import android.graphics.Bitmap
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import java.util.*


/**
 * <pre>
 * author : wenxin
 * e-mail : wenxin2233@outlook.com
 * time   : 2019/03/25
 * desc   :
 * version: 1.0
</pre> *
 */
object QRCodeUtil {

    /**
     * 解析图片中的 二维码 或者 条形码
     *
     * @param bitmap 待解析的图片
     * @return Result 解析结果，解析识别时返回NULL
     */
    fun scanner(bitmap: Bitmap): String {
        val multiFormatReader = MultiFormatReader()
        // 解码的参数
        val hints = Hashtable<DecodeHintType, Any>(2)
        // 可以解析的编码类型
        var decodeFormats = Vector<BarcodeFormat>()
        if (decodeFormats.isEmpty()) {
            decodeFormats = Vector()

            val PRODUCT_FORMATS = Vector<BarcodeFormat>(5)
            PRODUCT_FORMATS.add(BarcodeFormat.UPC_A)
            PRODUCT_FORMATS.add(BarcodeFormat.UPC_E)
            PRODUCT_FORMATS.add(BarcodeFormat.EAN_13)
            PRODUCT_FORMATS.add(BarcodeFormat.EAN_8)
            // PRODUCT_FORMATS.add(BarcodeFormat.RSS14);
            val ONE_D_FORMATS = Vector<BarcodeFormat>(PRODUCT_FORMATS.size + 4)
            ONE_D_FORMATS.addAll(PRODUCT_FORMATS)
            ONE_D_FORMATS.add(BarcodeFormat.CODE_39)
            ONE_D_FORMATS.add(BarcodeFormat.CODE_93)
            ONE_D_FORMATS.add(BarcodeFormat.CODE_128)
            ONE_D_FORMATS.add(BarcodeFormat.ITF)
            val QR_CODE_FORMATS = Vector<BarcodeFormat>(1)
            QR_CODE_FORMATS.add(BarcodeFormat.QR_CODE)
            val DATA_MATRIX_FORMATS = Vector<BarcodeFormat>(1)
            DATA_MATRIX_FORMATS.add(BarcodeFormat.DATA_MATRIX)

            // 这里设置可扫描的类型，我这里选择了都支持
            decodeFormats.addAll(ONE_D_FORMATS)
            decodeFormats.addAll(QR_CODE_FORMATS)
            decodeFormats.addAll(DATA_MATRIX_FORMATS)
        }
        hints[DecodeHintType.POSSIBLE_FORMATS] = decodeFormats
        // 设置继续的字符编码格式为UTF8
        // hints.put(DecodeHintType.CHARACTER_SET, "UTF8");
        // 设置解析配置参数
        multiFormatReader.setHints(hints)

        // 开始对图像资源解码
        val source = BitmapLuminanceSource(bitmap)
        val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
        val result = multiFormatReader.decodeWithState(binaryBitmap)
        return result.text
    }


    internal class BitmapLuminanceSource(bitmap: Bitmap) : LuminanceSource(bitmap.width, bitmap.height) {

        private val bitmapPixels: ByteArray

        init {
            // 首先，要取得该图片的像素数组内容
            val data = IntArray(bitmap.width * bitmap.height)
            this.bitmapPixels = ByteArray(bitmap.width * bitmap.height)
            bitmap.getPixels(data, 0, width, 0, 0, width, height)

            // 将int数组转换为byte数组，也就是取像素值中蓝色值部分作为辨析内容
            for (i in data.indices) {
                this.bitmapPixels[i] = data[i].toByte()
            }
        }

        override fun getMatrix(): ByteArray {
            // 返回我们生成好的像素数据
            return bitmapPixels
        }

        override fun getRow(y: Int, row: ByteArray): ByteArray {
            // 这里要得到指定行的像素数据
            System.arraycopy(bitmapPixels, y * width, row, 0, width)
            return row
        }
    }
}
