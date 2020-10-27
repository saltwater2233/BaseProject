package com.saltwater.common.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.Animation
import android.view.animation.CycleInterpolator
import android.view.animation.TranslateAnimation
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.saltwater.common.R


/**
 * <pre>
 * author : wenxin
 * e-mail : wenxin2233@outlook.com
 * time   : 2018/09/04
 * desc   : 带删除按钮的EditText
 * version: 1.0
</pre> *
 */
open class ClearEditText : AppCompatEditText {
    //按钮资源
    private val CLEAR = R.mipmap.ic_common_edit_clear
    //动画时长
    private val ANIMATOR_TIME = 200L
    //按钮左右间隔,单位DP
    private val INTERVAL = 5
    //清除按钮宽度,单位DP
    private val WIDTH_OF_CLEAR = 23


    //间隔记录
    //----------------以下方法为方便子类继承，只使用ClearEditText就没有用处---------------------------------------------------------------------

    var interval: Int = 0
        private set
    //清除按钮宽度记录
    var mClearIconWidth: Int = 0
    //右内边距
    private var mPaddingRight: Int = 0
    //清除按钮的bitmap
    lateinit var mClearIconBitmap: Bitmap


    //清除按钮出现动画
    private val mAnimator_visible: ValueAnimator by lazy {
        ValueAnimator.ofInt(mClearIconWidth + interval, 0).setDuration(ANIMATOR_TIME)
    }

    //消失动画
    private val mAnimator_gone: ValueAnimator by lazy {
        ValueAnimator.ofFloat(1f, 0f).setDuration(ANIMATOR_TIME)
    }
    //是否显示的记录
    private var isVisible = false

    //右边添加其他按钮时使用
    private var mRight = 0

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

        //setSingleLine();这个方法不推荐写在代码中，原因请看博客尾部更新

        mClearIconBitmap = createBitmap(CLEAR, context)

        interval = dp2px(INTERVAL.toFloat())
        mClearIconWidth = dp2px(WIDTH_OF_CLEAR.toFloat())
        mPaddingRight = interval + mClearIconWidth + interval

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        //设置内边距
        setPadding(paddingLeft, paddingTop, mPaddingRight + mRight, paddingBottom)

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawFilter = PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)//抗锯齿
        if (mAnimator_visible.isRunning) {
            val x = mAnimator_visible.animatedValue as Int
            drawClear(x, canvas)
            invalidate()
        } else if (isVisible) {
            drawClear(0, canvas)
        }

        if (mAnimator_gone.isRunning) {
            val scale = mAnimator_gone.animatedValue as Float
            drawClearGone(scale, canvas)
            invalidate()
        }
    }

    /**
     * 绘制清除按钮出现的图案
     *
     * @param translationX 水平移动距离
     * @param canvas
     */
    protected open fun drawClear(translationX: Int, canvas: Canvas) {
        val right = width + scrollX - interval - mRight + translationX
        val left = right - mClearIconWidth
        val top = (height - mClearIconWidth) / 2
        val bottom = top + mClearIconWidth
        val rect = Rect(left, top, right, bottom)
        canvas.drawBitmap(mClearIconBitmap, null, rect, null)

    }

    /**
     * 绘制清除按钮消失的图案
     *
     * @param scale  缩放比例
     * @param canvas
     */
    protected fun drawClearGone(scale: Float, canvas: Canvas) {
        val right =
                ((width + scrollX).toFloat() - interval.toFloat() - mRight.toFloat() - mClearIconWidth * (1f - scale) / 2f).toInt()
        val left =
                ((width + scrollX).toFloat() - interval.toFloat() - mRight.toFloat() - mClearIconWidth * (scale + (1f - scale) / 2f)).toInt()
        val top = ((height - mClearIconWidth * scale) / 2).toInt()
        val bottom = (top + mClearIconWidth * scale).toInt()
        val rect = Rect(left, top, right, bottom)
        canvas.drawBitmap(mClearIconBitmap, null, rect, null)
    }

    /**
     * 开始清除按钮的显示动画
     */
    private fun startVisibleAnimator() {
        endAnaimator()
        mAnimator_visible.start()
        invalidate()
    }

    /**
     * 开始清除按钮的消失动画
     */
    private fun startGoneAnimator() {
        endAnaimator()
        mAnimator_gone.start()
        invalidate()
    }

    /**
     * 结束所有动画
     */
    private fun endAnaimator() {
        mAnimator_gone.end()
        mAnimator_visible.end()
    }

    /**
     * Edittext内容变化的监听
     */
    override fun onTextChanged(text: CharSequence, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)

        if (text.length > 0) {
            if (!isVisible) {
                isVisible = true
                startVisibleAnimator()
            }
        } else {
            if (isVisible) {
                isVisible = false
                startGoneAnimator()
            }
        }
    }

    /**
     * 触控执行的监听
     *
     * @param event
     * @return
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {

            val touchable = width - interval - mRight - mClearIconWidth < event.x && event.x < width - interval - mRight
            if (touchable) {
                error = null
                this.setText("")
            }
        }
        return super.onTouchEvent(event)
    }

    /**
     * 开始晃动动画
     */
    fun startShakeAnimation() {
        if (animation == null) {
            this.animation = shakeAnimation(4)
        }
        this.startAnimation(animation)
    }

    /**
     * 晃动动画
     *
     * @param counts 0.5秒钟晃动多少下
     * @return
     */
    private fun shakeAnimation(counts: Int): Animation {
        val translateAnimation = TranslateAnimation(0f, 10f, 0f, 0f)
        translateAnimation.interpolator = CycleInterpolator(counts.toFloat())
        translateAnimation.duration = 500
        return translateAnimation
    }

    /**
     * 给图标染上当前提示文本的颜色并且转出Bitmap
     *
     * @param resources
     * @param context
     * @return
     */
    fun createBitmap(resources: Int, context: Context): Bitmap {
        val drawable = ContextCompat.getDrawable(context, resources)
        val wrappedDrawable = DrawableCompat.wrap(drawable!!)
        DrawableCompat.setTint(wrappedDrawable, currentHintTextColor)
        return drawableToBitamp(wrappedDrawable)
    }

    /**
     * drawable转换成bitmap
     *
     * @param drawable
     * @return
     */
    private fun drawableToBitamp(drawable: Drawable): Bitmap {
        val w = drawable.intrinsicWidth
        val h = drawable.intrinsicHeight
        val config = if (drawable.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
        val bitmap = Bitmap.createBitmap(w, h, config)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, w, h)
        drawable.draw(canvas)
        return bitmap
    }

    fun dp2px(dipValue: Float): Int {
        val scale = resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    fun addRight(right: Int) {
        mRight += right
    }
}
