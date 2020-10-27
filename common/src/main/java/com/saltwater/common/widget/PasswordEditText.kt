package com.saltwater.common.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import com.saltwater.common.R

/**
 * 密码EditText
 */
class PasswordEditText : ClearEditText {

    //资源
    private val INVISIBLE = R.mipmap.ic_common_edit_show_password
    private val VISIBLE = R.mipmap.ic_common_edit_hide_password
    //按钮宽度dp
    private var mWidth: Int = 0
    //按钮的bitmap
    private var mInvisibleBitmap: Bitmap? = null
    private var mVisibleBitmap: Bitmap? = null

    //内容是否可见
    private var isVisible = false

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        setSingleLine()
        //设置EditText文本为隐藏的(注意！需要在setSingleLine()之后调用)
        transformationMethod = PasswordTransformationMethod.getInstance()

        mWidth = mClearIconWidth
        addRight(mWidth + interval)
        mInvisibleBitmap = createBitmap(INVISIBLE, context)
        mVisibleBitmap = createBitmap(VISIBLE, context)

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val right = width + scrollX - interval
        val left = right - mWidth
        val top = (height - mWidth) / 2
        val bottom = top + mWidth
        val rect = Rect(left, top, right, bottom)

        if (isVisible) {
            canvas.drawBitmap(mVisibleBitmap!!, null, rect, null)
        } else {
            canvas.drawBitmap(mInvisibleBitmap!!, null, rect, null)
        }
    }

    /**
     * 改写父类的方法
     */
    override fun drawClear(translationX: Int, canvas: Canvas) {
        val scale = 1f - translationX.toFloat() / (mClearIconWidth + interval).toFloat()
        val right = (width + scrollX - interval - mWidth - interval - mClearIconWidth * (1f - scale) / 2f).toInt()
        val left =
            (width + scrollX - interval - mWidth - interval - mClearIconWidth * (scale + (1f - scale) / 2f)).toInt()
        val top = ((height - mClearIconWidth * scale) / 2).toInt()
        val bottom = (top + mClearIconWidth * scale).toInt()
        val rect = Rect(left, top, right, bottom)
        canvas.drawBitmap(mClearIconBitmap, null, rect, null)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            val touchable = width - mWidth - interval < event.x && event.x < width - interval
            if (touchable) {
                isVisible = !isVisible
                if (isVisible) {
                    //设置EditText文本为可见的
                    transformationMethod = HideReturnsTransformationMethod.getInstance()
                } else {
                    //设置EditText文本为隐藏的
                    transformationMethod = PasswordTransformationMethod.getInstance()
                }
            }
        }
        return super.onTouchEvent(event)
    }
}
