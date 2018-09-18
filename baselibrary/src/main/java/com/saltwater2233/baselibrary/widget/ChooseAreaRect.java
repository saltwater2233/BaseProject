package com.saltwater2233.baselibrary.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.saltwater2233.baselibrary.utils.ScreenInfoUtil;


/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-7-6.
 * 矩形剪裁框
 */
public class ChooseAreaRect extends View {

    private Context _context;
    private Paint paintFillRegion = new Paint();
    private Paint paintDrawCircle = new Paint();
    private Paint paintDrawLine = new Paint();


    public ChooseAreaRect(Context context) {
        super(context);
        _context = context;
        init();
    }

    public ChooseAreaRect(Context context, AttributeSet attrs) {
        super(context, attrs);
        _context = context;
        init();
    }

    public ChooseAreaRect(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        _context = context;
        init();
    }

    private void init() {
        // 半透明背景区域的画笔
        paintFillRegion.setStyle(Paint.Style.FILL);
        paintFillRegion.setStrokeWidth(10);
        paintFillRegion.setAlpha(80);

        // 四个角的画笔
        paintDrawCircle.setStyle(Paint.Style.FILL);
        paintDrawCircle.setStrokeWidth(8);
        paintDrawCircle.setColor(Color.parseColor("#FFDB2F"));
        mBorderThickness = ScreenInfoUtil.dp2px(_context, 3);//边的宽
        mCornerThickness = ScreenInfoUtil.dp2px(_context, 5);//角宽
        mCornerLength = ScreenInfoUtil.dp2px(_context, 20);////长度


        // 边框的画笔
        paintDrawLine.setStyle(Paint.Style.STROKE);
        paintDrawLine.setStrokeWidth(ScreenInfoUtil.dp2px(_context, 2));
    }


    // 这4个点就是代表着4个控制点，顺序是这样的：
    /**
     * 0*********1
     * *         *
     * *         *
     * *         *
     * 3*********2
     */
    private Point[] p = null;

    public void setRegion(Point p0, Point p1, Point p2, Point p3) {
        p = new Point[4];
        p[0] = new Point(p0);
        p[1] = new Point(p1);
        p[2] = new Point(p2);
        p[3] = new Point(p3);
        invalidate();
    }

    public Point[] getRegion() {
        return p;
    }

    private int currentPoint = -1;

    private BottomImageView bottomView = null;

    public void setBottomView(BottomImageView bottomView) {
        this.bottomView = bottomView;
    }


    private int sx;
    private int sy;
    private boolean mChecked = false;
    private OnClickListener mListener;

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean mChecked) {
        this.mChecked = mChecked;
        invalidate();
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        mListener = l;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                //找出移动的是哪个点
                int index = findTheCoveredPoint(event.getX(), event.getY());
                if (index == -1) {
                    return false;
                }
                if (index == 5) {
                    sx = (int) event.getRawX();// 获取手指第一次接触屏幕
                    sy = (int) event.getRawY();
                }
                currentPoint = index;
                break;
            case MotionEvent.ACTION_MOVE:
                if (currentPoint == -1)
                    return false;
                zoomRect(currentPoint, event);
                break;
            case MotionEvent.ACTION_UP:
                currentPoint = -1;
                mListener.onClick(this);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                currentPoint = -1;
                return false;
            default:
                return false;
        }

        // 把touch动作投递到底部的bottomView去，用于控制ZoomArea区域的同步移动
        bottomView.perform(event);
        invalidate();
        return true;
    }


    //缩放矩形
    private void zoomRect(int currentPoint, MotionEvent event) {
        //左上点
        if (currentPoint == 0) {
            //限制最小距离
            if (p[1].x - event.getX() > 2 * mCornerLength) {
                //重新设置左上和坐下X坐标
                p[0].x = (int) event.getX();
                p[3].x = (int) event.getX();
            }
            if (p[3].y - event.getY() > 2 * mCornerLength) {
                //重新设置左上和右上Y坐标
                p[0].y = (int) event.getY();
                p[1].y = (int) event.getY();
            }
        }
        //右上点
        if (currentPoint == 1) {
            if (event.getX() - p[0].x > 2 * mCornerLength) {
                p[1].x = (int) event.getX();
                p[2].x = (int) event.getX();
            }
            if (p[2].y - event.getY() > 2 * mCornerLength) {
                p[1].y = (int) event.getY();
                p[0].y = (int) event.getY();
            }
        }
        //右下点
        if (currentPoint == 2) {
            if (event.getX() - p[3].x > 2 * mCornerLength) {
                p[2].x = (int) event.getX();
                p[1].x = (int) event.getX();
            }
            if (event.getY() - p[1].y > 2 * mCornerLength) {
                p[2].y = (int) event.getY();
                p[3].y = (int) event.getY();
            }
        }
        //左下点
        if (currentPoint == 3) {
            if (p[2].x - event.getX() > 2 * mCornerLength) {
                p[3].x = (int) event.getX();
                p[0].x = (int) event.getX();
            }
            if (event.getY() - p[0].y > 2 * mCornerLength) {
                p[3].y = (int) event.getY();
                p[2].y = (int) event.getY();
            }
        }

        //在中间,平移
        if (currentPoint == 5) {
            int x = (int) event.getRawX();
            int y = (int) event.getRawY();
            // 获取手指移动的距离
            int dx = x - sx;
            int dy = y - sy;
            for (int i = 0; i < 4; i++) {
                p[i].x += dx;
                p[i].y += dy;
            }
            // 获取移动后的位置
            sx = (int) event.getRawX();
            sy = (int) event.getRawY();
        }
    }


    // 与控制点相邻80个像素点就选中，因为我的手机是1080p的分辨率，如果分辨率不同这里最好设置成不同的数值
    private static final int BOUND = 80;

    // 计算出当前手指触控的是哪个控制点
    private int findTheCoveredPoint(float x, float y) {
        if (p == null)
            return -1;
        if (isInRect(x, y, p[0].x, p[0].y, p[2].x, p[2].y)) {
            return 5;
        }
        for (int i = 0; i < 4; i++) {
            if (Math.sqrt((p[i].x - x) * (p[i].x - x) + (p[i].y - y) * (p[i].y - y)) - BOUND <= 0) {
                return i;
            }
        }
        return -1;
    }

    //在矩形内部
    private boolean isInRect(float x, float y, float left, float top, float right, float bottom) {
        return x >= left && x <= right && y >= top && y <= bottom;
    }


    private Point leftTop, rightTop, leftBottom, rightBottom;
    private int width, height;
    private boolean getParams = false;

    private float mCornerThickness;
    private float mBorderThickness;
    private float mCornerLength;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!getParams) {
            if (bottomView == null)
                throw new IllegalStateException("you must set the bottom mView !");

            // 这里根据底部的BottomView的位置和宽高来对表面的View进行设置，以便使得表面的这一层View与底部的View位置上完全重合
            this.setLayoutParams(new FrameLayout.LayoutParams(bottomView.getWidth(), bottomView.getHeight()));
            int[] tt = new int[2];
            bottomView.getLocationInWindow(tt);
            WidgetController.setLayout(this, tt[0], 0);
            //*************************************************************************************************

            width = bottomView.getWidth();
            height = bottomView.getHeight();

            // 这里的几个Point的位置，是相对于这个View的，所以这里的x, y都设置为 0
            int x = 0, y = 0;
            leftTop = new Point(x, y);
            leftBottom = new Point(x, height);
            rightTop = new Point(x + width, y);
            rightBottom = new Point(x + width, y + height);

            getParams = true;
        }
        if (p == null) {
            return;
        }

        /*Path[] paths = new Path[4];
        paths[0] = getPath(leftTop, rightTop, p[1], p[0]);
        paths[1] = getPath(rightTop, rightBottom, p[2], p[1]);
        paths[2] = getPath(rightBottom, leftBottom, p[3], p[2]);
        paths[3] = getPath(leftBottom, leftTop, p[0], p[3]);
        for (int i = 0; i < 4; i++) {
            canvas.drawPath(paths[i], paintFillRegion);
        }*/

        // 画出中间全透明区域的边框
        drawBorder(canvas);
        // 画出四个角的小圈
        drawCorners(canvas);
    }



    private Path getPath(Point... points) {
        Path path = new Path();
        path.moveTo(points[0].x, points[0].y);
        for (int i = 1; i < points.length; i++) {
            path.lineTo(points[i].x, points[i].y);
        }
        path.close();

        return path;
    }

    //画矩形
    private void drawBorder(@NonNull Canvas canvas) {
        if (mChecked) {
            paintDrawLine.setColor(Color.parseColor("#FF0000"));
        } else {
            paintDrawLine.setColor(Color.parseColor("#AAFFFFFF"));

        }
        canvas.drawRect(p[0].x, p[0].y, p[2].x, p[2].y, paintDrawLine);
    }

    //画粗的边框
    private void drawCorners(@NonNull Canvas canvas) {

        final float left = p[0].x;
        final float top = p[0].y;
        final float right = p[2].x;
        final float bottom = p[2].y;

        //简单的数学计算

        final float lateralOffset = (mCornerThickness - mBorderThickness) / 2f;
        final float startOffset = mCornerThickness - (mBorderThickness / 2f);

        //左上角左面的短线
        canvas.drawLine(left - lateralOffset, top - startOffset, left - lateralOffset, top + mCornerLength, paintDrawCircle);
        //左上角上面的短线
        canvas.drawLine(left - startOffset, top - lateralOffset, left + mCornerLength, top - lateralOffset, paintDrawCircle);

        //右上角右面的短线
        canvas.drawLine(right + lateralOffset, top - startOffset, right + lateralOffset, top + mCornerLength, paintDrawCircle);
        //右上角上面的短线
        canvas.drawLine(right + startOffset, top - lateralOffset, right - mCornerLength, top - lateralOffset, paintDrawCircle);

        //左下角左面的短线
        canvas.drawLine(left - lateralOffset, bottom + startOffset, left - lateralOffset, bottom - mCornerLength, paintDrawCircle);
        //左下角底部的短线
        canvas.drawLine(left - startOffset, bottom + lateralOffset, left + mCornerLength, bottom + lateralOffset, paintDrawCircle);

        //右下角左面的短线
        canvas.drawLine(right + lateralOffset, bottom + startOffset, right + lateralOffset, bottom - mCornerLength, paintDrawCircle);
        //右下角底部的短线
        canvas.drawLine(right + startOffset, bottom + lateralOffset, right - mCornerLength, bottom + lateralOffset, paintDrawCircle);
    }
}
