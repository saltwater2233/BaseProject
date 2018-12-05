package com.saltwater.baseproject.widget;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * <pre>
 *     author : wenxin
 *     e-mail : wenxin2233@outlook.com
 *     time   : 2018/04/26
 *     desc   : 不规则剪裁框背景
 *     version: 1.0
 * </pre>
 */
public class BottomImageView extends android.support.v7.widget.AppCompatImageView {

    private Context _context;


    public BottomImageView(Context context) {
        super(context);
        _context = context;
    }

    public BottomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._context = context;
    }

    public BottomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this._context = context;
    }



    // 根据TopView投递过来的动作进行图片的放大区的跟踪
    public boolean perform(MotionEvent event) {

        if (event.getX()>this.getWidth() || event.getY()>this.getHeight()
                || event.getX()<0 || event.getY()<0)
            return false;

        // 这里的event获取到的 x,y 已经是基于这个view的偏移了
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE:

                break;
            default:
                break;
        }

        return true;
    }

    private Matrix imageXY2ViewXY(int xi, int yi, int imageViewWidth, int imageViewHeight) {
        Matrix mm = new Matrix();
        float dx = xi-(imageViewWidth/2);
        float dy = yi - (imageViewHeight/2);
        mm.postTranslate(-dx, -dy);

        return mm;
    }

    private int[] xy2ImageXY(int x, int y, int viewWidth, int viewHeight, int imageWidth, int imageHeight) {
        int[] xy = new int[2];
        xy[0] = (x)*imageWidth/viewWidth;
        xy[1] = (y)*imageHeight/viewHeight;

        return xy;
    }
}
